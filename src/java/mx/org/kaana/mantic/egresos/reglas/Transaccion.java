package mx.org.kaana.mantic.egresos.reglas;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticEgresosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticEgresosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticEgresosDto;
import mx.org.kaana.mantic.db.dto.TcManticEgresosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.enums.ECuentasEgresos;
import mx.org.kaana.mantic.enums.EEstatusEgresos;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final Log LOG= LogFactory.getLog(Transaccion.class);
	private String messageError;
	private Long idEgreso;
	private Long idTipoEgreso;
	private ECuentasEgresos accionTipoEgreso;
	private String nota;
	private Importado xml;
	private Importado pdf;
	private TcManticEgresosDto egreso;
	private TcManticEgresosBitacoraDto bitacora;

	public Transaccion(TcManticEgresosBitacoraDto bitacora) {
		this.bitacora = bitacora;
	}	
	
	public Transaccion(Long idEgreso, String nota) {
		this.idEgreso= idEgreso;
		this.nota    = nota;
	}

	public Transaccion(Long idTipoEgreso, ECuentasEgresos accionTipoEgreso) {
		this(idTipoEgreso, accionTipoEgreso, null);
	}	
	
	public Transaccion(TcManticEgresosDto egreso, Importado xml, Importado pdf) {
		this.egreso= egreso;
		this.pdf   = pdf;
		this.xml   = xml;
	} // Transaccion
	
	public Transaccion(Long idTipoEgreso, ECuentasEgresos accionTipoEgreso, Long idEgreso) {
		this.idTipoEgreso    = idTipoEgreso;
		this.accionTipoEgreso= accionTipoEgreso;
		this.idEgreso        = idEgreso;
	}
	
	public String getMessageError() {
		return messageError;
	}
			
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {
			switch(accion){
				case JUSTIFICAR:
					regresar= registrarNota(sesion);
					break;				
				case ELIMINAR:
					regresar= procesarDocumento(sesion);
					break;				
				case ASIGNAR:
					regresar= asociarDocumento(sesion);
					break;
				case REGISTRAR:
					regresar= true;
					toUpdateDeleteXml(sesion);
					break;
				case MODIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L){
						this.egreso= (TcManticEgresosDto) DaoFactory.getInstance().findById(sesion, TcManticEgresosDto.class, this.bitacora.getIdEgreso());
						this.egreso.setIdEgresoEstatus(this.bitacora.getIdEgresoEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.egreso)>= 1L;
					} // if
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	} // ejecutar		
	
	private boolean registrarNota(Session sesion) throws Exception{
		boolean regresar            = false;
		TcManticEgresosNotasDto nota= null;
		try {
			nota= new TcManticEgresosNotasDto(this.idEgreso, JsfBase.getIdUsuario(), -1L, this.nota);
			regresar= DaoFactory.getInstance().insert(sesion, nota)>= 1L;
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // registrarNota	
	
	private boolean procesarDocumento(Session sesion) throws Exception{
		boolean regresar                    = false;		
		TcManticEmpresasPagosDto empresaPago= null;		
		try {
			switch(this.accionTipoEgreso){				
				case EMPRESA_PAGO: 
					empresaPago= (TcManticEmpresasPagosDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasPagosDto.class, this.idTipoEgreso);
					empresaPago.setIdEgreso(null);
					regresar= DaoFactory.getInstance().update(sesion, empresaPago)>= 1L;
					break;
				case NOTA:
					regresar= DaoFactory.getInstance().delete(sesion, TcManticEgresosNotasDto.class, this.idTipoEgreso)>= 1L;
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarDocumento
	
	private boolean asociarDocumento(Session sesion) throws Exception{
		boolean regresar                    = false;		
		TcManticEmpresasPagosDto empresaPago= null;		
		TcManticEgresosDto afectado         = null;
		try {
			switch(this.accionTipoEgreso){				
				case EMPRESA_PAGO: 
					empresaPago= (TcManticEmpresasPagosDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasPagosDto.class, this.idTipoEgreso);
					empresaPago.setIdEgreso(this.idEgreso);
					regresar= DaoFactory.getInstance().update(sesion, empresaPago)>= 1L;
					afectado= (TcManticEgresosDto) DaoFactory.getInstance().findById(TcManticEgresosDto.class, this.idEgreso);
					if(afectado.getIdEgresoEstatus().equals(EEstatusEgresos.REGISTRADO.getKey())){
						afectado.setIdEgresoEstatus(EEstatusEgresos.INCOMPLETO.getKey());
						DaoFactory.getInstance().update(afectado);
					} // if
					break;				
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarDocumento
	
	protected void toUpdateDeleteXml(Session sesion) throws Exception {
		TcManticEgresosArchivosDto tmp= null;
		if(this.egreso.isValid()) {
			if(this.xml!= null) {
				tmp= new TcManticEgresosArchivosDto(
					-1L, //idEgresoArchivo
					this.egreso.getIdEgreso(), //idEgreso
					this.xml.getFileSize(), //tamanio
					JsfBase.getIdUsuario(),	//idUsuario
					1L, //idTipoArchivo
					1L, //idPrincipal
					this.xml.getObservaciones(), //observaciones
					Configuracion.getInstance().getPropiedadSistemaServidor("egresos").concat(this.xml.getRuta()).concat(this.xml.getName()), //alias
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1), //mes
					this.xml.getName(), //nombre
					new Long(Calendar.getInstance().get(Calendar.YEAR)), //ejercicio
					this.xml.getRuta(), //ruta
					this.xml.getOriginal()
				);
				TcManticEgresosArchivosDto exists= (TcManticEgresosArchivosDto)DaoFactory.getInstance().toEntity(TcManticEgresosArchivosDto.class, "TcManticEgresosArchivosDto", "identically", tmp.toMap());
				File reference= new File(tmp.getAlias());
				if(exists== null && reference.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticEgresosArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!reference.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("egresos").concat(this.xml.getRuta()), ".".concat(this.xml.getFormat().name()), this.toListFile(sesion, this.xml, 1L));
			} // if	
			if(this.pdf!= null) {
				tmp= new TcManticEgresosArchivosDto(
					-1L, //idEgresoArchivo
					this.egreso.getIdEgreso(), //idEgreso
					this.pdf.getFileSize(), //tamanio
					JsfBase.getIdUsuario(),	//idUsuario
					2L, //idTipoArchivo
					1L, //idPrincipal
					this.pdf.getObservaciones(), //observaciones
					Configuracion.getInstance().getPropiedadSistemaServidor("egresos").concat(this.pdf.getRuta()).concat(this.pdf.getName()), //alias
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1), //mes
					this.pdf.getName(), //nombre
					new Long(Calendar.getInstance().get(Calendar.YEAR)), //ejercicio
					this.pdf.getRuta(), //ruta										
					this.pdf.getOriginal()
				);
				TcManticEgresosArchivosDto exists= (TcManticEgresosArchivosDto)DaoFactory.getInstance().toEntity(TcManticEgresosArchivosDto.class, "TcManticEgresosArchivosDto", "identically", tmp.toMap());
				File reference= new File(tmp.getAlias());
				if(exists== null && reference.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticEgresosArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!reference.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("egresos").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
			} // if	
  	} // if	
	} // toUpdateDeleteXml
	
	private List<Nombres> toListFile(Session sesion, Importado tmp, Long idTipoArchivo) throws Exception {
		List<Nombres> regresar= null;
		Map<String, Object> params=null;
		try {
			params  = new HashMap<>();
			params.put("idTipoArchivo", idTipoArchivo);
			params.put("ruta", tmp.getRuta());
			regresar= (List<Nombres>)DaoFactory.getInstance().toEntitySet(sesion, Nombres.class, "TcManticEgresosArchivosDto", "listado", params);
			regresar.add(new Nombres(tmp.getName()));
		} // try  // try 
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toListFile
	
	private void toDeleteAll(String path, String type, List<Nombres> listado) {
    FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File(path), type.toLowerCase());
    if(fileSearch.getResult().size()> 0){
		  for (String matched: fileSearch.getResult()) {
				String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
				if(listado.indexOf(new Nombres(name))< 0) {
				  File file= new File(matched);
				  file.delete();
				} // if
      } // for
		} // if
	} // toDeleteAll
}
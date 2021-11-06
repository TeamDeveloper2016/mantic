package mx.org.kaana.mantic.egresos.reglas;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticEgresosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticEgresosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticEgresosDto;
import mx.org.kaana.mantic.db.dto.TcManticEgresosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
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
	private Importado jpg;
	private TcManticEgresosDto egreso;
	private TcManticEgresosBitacoraDto bitacora;
  private Entity documento;

	public Transaccion(Entity documento) {
		this.documento = documento;
	}	
  
	public Transaccion(TcManticEgresosDto egreso) {
		this.egreso = egreso;
	}	
	
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
	
	public Transaccion(TcManticEgresosDto egreso, Importado xml, Importado pdf, Importado jpg) {
		this.egreso= egreso;
		this.pdf   = pdf;
		this.xml   = xml;
		this.jpg   = jpg;
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
		boolean regresar = false;
    this.messageError= "";
		try {
			switch(accion){
				case JUSTIFICAR:
					regresar= this.registrarNota(sesion);
					break;				
				case ELIMINAR:
          regresar= DaoFactory.getInstance().delete(sesion, this.egreso)>= 1L;
          break;
				case DEPURAR:
					regresar= this.procesarDocumento(sesion);
					break;				
				case ASIGNAR:
					regresar= this.asociarDocumento(sesion);
					break;
				case REGISTRAR:
					regresar= this.toUpdateDeleteXml(sesion);
          this.idEgreso= this.egreso.getIdEgreso();
          this.toCheckDocumentos(sesion);
					break;
				case COMPLEMENTAR:
          if(this.checkDocumentos(sesion)) {
            if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
              this.egreso= (TcManticEgresosDto) DaoFactory.getInstance().findById(sesion, TcManticEgresosDto.class, this.bitacora.getIdEgreso());
              this.egreso.setIdEgresoEstatus(this.bitacora.getIdEgresoEstatus());
              regresar= DaoFactory.getInstance().update(sesion, this.egreso)>= 1L;
            } // if
          } // if
          else 
            throw new RuntimeException("Las cuentas por pagar no estan completas en sus documentos !");
					break;
        case MODIFICAR:  
          regresar= DaoFactory.getInstance().update(sesion, this.egreso)>= 1L;
          break;
        case MOVIMIENTOS:  
          this.idEgreso= this.documento.toLong("idEgreso");
          regresar= this.toDeleteDocumento(sesion);
          this.toCheckDocumentos(sesion);
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
	
	private boolean registrarNota(Session sesion) throws Exception {
		boolean regresar            = false;
		TcManticEgresosNotasDto item= null;
		try {
			item= new TcManticEgresosNotasDto(this.idEgreso, JsfBase.getIdUsuario(), -1L, this.nota, 1L);
			regresar= DaoFactory.getInstance().insert(sesion, item)>= 1L;
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
			switch(this.accionTipoEgreso) {		
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
	
	protected boolean toUpdateDeleteXml(Session sesion) throws Exception {
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
					this.xml.getOriginal(),
          this.xml.getIdTipoDocumento(),
          2L      
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
        this.toCheckDeleteFile(sesion, this.xml.getName());
				// this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("egresos").concat(this.xml.getRuta()), ".".concat(this.xml.getFormat().name()), this.toListFile(sesion, this.xml, 1L));
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
					this.pdf.getOriginal(),
          this.pdf.getIdTipoDocumento(),
          2L      
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
        this.toCheckDeleteFile(sesion, this.pdf.getName());
				// this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("egresos").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
			} // if	
			if(this.jpg!= null) {
				tmp= new TcManticEgresosArchivosDto(
					-1L, //idEgresoArchivo
					this.egreso.getIdEgreso(), // idEgreso
					this.jpg.getFileSize(), // tamanio
					JsfBase.getIdUsuario(),	// idUsuario
					17L, // idTipoArchivo
					1L, // idPrincipal
					this.jpg.getObservaciones(), // observaciones
					Configuracion.getInstance().getPropiedadSistemaServidor("egresos").concat(this.jpg.getRuta()).concat(this.jpg.getName()), // alias
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1), // mes
					this.jpg.getName(), // nombre
					new Long(Calendar.getInstance().get(Calendar.YEAR)), // ejercicio
					this.jpg.getRuta(), // ruta										
					this.jpg.getOriginal(), // original
          this.jpg.getIdTipoDocumento(), // idTipoDicumento
          2L      
				);
				TcManticEgresosArchivosDto exists= (TcManticEgresosArchivosDto)DaoFactory.getInstance().toEntity(TcManticEgresosArchivosDto.class, "TcManticEgresosArchivosDto", "identically", tmp.toMap());
				File file= new File(tmp.getAlias());
				if(exists== null && file.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticEgresosArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!file.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
        this.toCheckDeleteFile(sesion, this.jpg.getName());
				// this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").concat(this.jpg.getRuta()), ".".concat(this.jpg.getFormat().name()), this.toListFile(sesion, this.jpg, 17L));
			} // if	      
  	} // if	
    return Boolean.TRUE;
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
 
  private Boolean checkDocumentos(Session sesion) throws Exception {
    Boolean regresar          = false;
    Map<String, Object> params= null;
    try {      
      params= new HashMap<>();      
      params.put("idEgreso", this.bitacora.getIdEgreso());      
      List<Entity> documentos= (List<Entity>)DaoFactory.getInstance().toEntitySet(sesion, "VistaEgresosDto", "revisar", params);
      if(documentos!= null && !documentos.isEmpty()) {
        int count= 0;
        for (Entity item: documentos) {
          if(Objects.equals(item.toLong("idCompleto"), 3L))
            count++;
        } // for
        regresar= Objects.equals(documentos.size(), count);
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
 
  private boolean toDeleteDocumento(Session sesion) throws Exception {
    boolean regresar= Boolean.FALSE;
    try {      
      TcManticEgresosArchivosDto item= (TcManticEgresosArchivosDto)DaoFactory.getInstance().findById(sesion, TcManticEgresosArchivosDto.class, this.documento.getKey());
      if(item!= null) {
        item.setIdEliminado(Objects.equals(item.getIdEliminado(), 1L)? 2L: 1L);
        regresar= DaoFactory.getInstance().update(sesion, item)> 0L;
        this.documento.getValue("idEliminado").setData(item.getIdEliminado());
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
 
  private void toCheckDocumentos(Session sesion) throws Exception {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idEgreso", this.idEgreso);      
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet(sesion, "TcManticEgresosNotasDto", "egresos", params);
      if(items!= null && !items.isEmpty()) {
        for (Entity item : items) {
          this.toCheckEstatusNota(sesion, item.toLong("idNotaEntrada"));
        } // for
        sesion.flush();
        this.toCheckEstatusGlobal(sesion);
      } // if  
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
  private Boolean toCheckEstatusNota(Session sesion, Long idNotaEntrada) throws Exception {
    Boolean regresar          = false;
    Map<String, Object> params= null;
    TcManticNotasEntradasDto entrada= null;
    try {      
      entrada= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, idNotaEntrada);
      params= new HashMap<>();  
      params.put("sortOrder", "");
      params.put("idEmpresa", entrada.getIdEmpresa());
      params.put("idProveedor", entrada.getIdProveedor());
      params.put(Constantes.SQL_CONDICION, "tc_mantic_notas_entradas.id_nota_entrada= "+ entrada.getIdNotaEntrada());
      // FALTA VERIFICAR QUE EN LOS DOCUMENTOS DE LA FACTURA REALMENTE SE TENGAN UN XML Y EL PDF DE LA FACTURA
      boolean factura= this.toCheckTwoDocumentos(sesion, entrada.getIdNotaEntrada());
      Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "VistaEmpresasDto", "documentos", params);
      if(entity!= null && !entity.isEmpty()) {
        Long idCompleto= 1L; // INICIAL
        if(factura && entity.toLong("facturas")> 0 && entity.toLong("comprobantes")> 0 && entity.toLong("vouchers")> 0)
          idCompleto= 3L; // COMPLETO
        else
          if(entity.toLong("facturas")> 0 || entity.toLong("comprobantes")> 0 || entity.toLong("vouchers")> 0)
            idCompleto= 2L; // INCOMPLETO
        params.put("idNotaEntrada", entrada.getIdNotaEntrada());
        TcManticEmpresasDeudasDto item= (TcManticEmpresasDeudasDto)DaoFactory.getInstance().toEntity(sesion, TcManticEmpresasDeudasDto.class, "TcManticEmpresasDeudasDto", "unica", params);
        if(item!= null) {
          item.setIdCompleto(idCompleto);
          DaoFactory.getInstance().update(sesion, item);
        } // if
      } // if
      regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private boolean toCheckTwoDocumentos(Session sesion, Long idNotaEntrada) throws Exception {
    boolean regresar= Boolean.FALSE;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idNotaEntrada", idNotaEntrada);      
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet(sesion, "TcManticNotasArchivosDto", "all", params);
      int countPdf= 0, countXml= 0;
      if(items!= null && !items.isEmpty())
        for (Entity item : items) {
          if(Objects.equals(item.toLong("idTipoDocumento"), 13L)) {
            if(Objects.equals(item.toLong("idTipoArchivo"), 1L) && Objects.equals(item.toLong("idEliminado"), 2L)) // XML
              countPdf++;
            if(Objects.equals(item.toLong("idTipoArchivo"), 2L) && Objects.equals(item.toLong("idEliminado"), 2L)) // PDF
              countXml++;
          } // if  
        } // for
      regresar= countPdf> 0 && countXml> 0;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private Boolean toCheckEstatusGlobal(Session sesion) throws Exception {
    Boolean regresar          = false;
    Map<String, Object> params= null;
    try {      
      params= new HashMap<>();  
      params.put("idEgreso", this.idEgreso);      
      Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "VistaEgresosDto", "documentos", params);
      Long idEgresoEstatus= 1L;
      if(entity!= null && !entity.isEmpty() && Objects.equals(entity.toLong("total"), entity.toLong("completos")))
        idEgresoEstatus= 3L;
      else
        if(entity!= null && !entity.isEmpty() && entity.toLong("completos")> 0)
          idEgresoEstatus= 2L;
      TcManticEgresosDto item= (TcManticEgresosDto)DaoFactory.getInstance().toEntity(sesion, TcManticEgresosDto.class, "TcManticEgresosDto", "detalle", params);
      if(item!= null) {
        this.bitacora= new TcManticEgresosBitacoraDto("CAMBIO AUTOMATICO", idEgresoEstatus, item.getIdEgreso(), JsfBase.getIdUsuario(), -1L);
        DaoFactory.getInstance().insert(sesion, this.bitacora);
        item.setIdEgresoEstatus(idEgresoEstatus);
        DaoFactory.getInstance().update(sesion, item);
      } // if
      regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
}
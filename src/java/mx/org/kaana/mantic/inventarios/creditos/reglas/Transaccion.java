package mx.org.kaana.mantic.inventarios.creditos.reglas;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticCreditosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticCreditosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticCreditosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
	private static final long serialVersionUID=-6069204157451117543L;
 
	private TcManticCreditosNotasDto orden;	
	private Double importe;
	private Importado xml;
	private Importado pdf;
	private String messageError;
	private TcManticCreditosBitacoraDto bitacora;

	public Transaccion(TcManticCreditosNotasDto orden, TcManticCreditosBitacoraDto bitacora) {
		this(orden, 0D, null, null);
		this.bitacora= bitacora;
	}
	
	public Transaccion(TcManticCreditosNotasDto orden, Double importe, Importado xml, Importado pdf) {
		this.orden  = orden;		
		this.importe= importe;
		this.xml    = xml;
	  this.pdf    = pdf;
	} 

	protected void setMessageError(String messageError) {
		this.messageError=messageError;
	}

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		TcManticCreditosBitacoraDto bitacoraNota= null;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el nota de crédito.");
			switch(accion) {
				case AGREGAR:
					Siguiente consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(consecutivo.getConsecutivo());
					this.orden.setOrden(consecutivo.getOrden());
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
  				this.orden.setIdCreditoEstatus(3L);
					switch(this.orden.getIdTipoCreditoNota().intValue()) {
						case 1:
							this.orden.setIdNotaEntrada(null);
							this.orden.setIdProveedor(null);
							break;
						case 2:
							this.orden.setIdDevolucion(null);
							this.orden.setIdProveedor(null);
							break;
						case 3:
							this.orden.setIdDevolucion(null);
							this.orden.setIdNotaEntrada(null);
							break;
					} // switch
					this.orden.setSaldo(this.orden.getImporte());
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					bitacoraNota= new TcManticCreditosBitacoraDto(this.orden.getConsecutivo(), "", this.orden.getIdCreditoEstatus(), -1L, JsfBase.getIdUsuario(), this.orden.getIdCreditoNota(), this.orden.getImporte());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					if(this.orden.getIdTipoCreditoNota().equals(1L))
						this.toCheckOrdenDevolucion(sesion);
     	    this.toUpdateDeleteXml(sesion);	
					break;				
				case MODIFICAR:
					TcManticCreditosNotasDto anterior= (TcManticCreditosNotasDto)DaoFactory.getInstance().findById(TcManticCreditosNotasDto.class, this.orden.getIdCreditoNota());
					this.orden.setIdCreditoEstatus(3L);
					bitacoraNota= new TcManticCreditosBitacoraDto(this.orden.getConsecutivo(), "", this.orden.getIdCreditoEstatus(), -1L, JsfBase.getIdUsuario(), this.orden.getIdCreditoNota(), this.orden.getImporte());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					
					this.importe+= (this.orden.getImporte()- anterior.getImporte());
					this.orden.setSaldo(this.orden.getSaldo()+ this.importe);
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					
					if(this.orden.getIdTipoCreditoNota().equals(1L))
						this.toCheckOrdenDevolucion(sesion);
     	    this.toUpdateDeleteXml(sesion);	
					break;
				case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					this.orden.setIdCreditoEstatus(2L);
					bitacoraNota= new TcManticCreditosBitacoraDto(this.orden.getConsecutivo(), "", this.orden.getIdCreditoEstatus(), -1L, JsfBase.getIdUsuario(), this.orden.getIdCreditoNota(), this.orden.getImporte());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					this.importe-= this.orden.getImporte();
					if(this.orden.getIdTipoCreditoNota().equals(1L)) 
						this.toCheckOrdenDevolucion(sesion);
     	    this.toDeleteXmlPdf();	
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.orden.setIdCreditoEstatus(this.bitacora.getIdCreditoEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					} // if
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		LOG.info("Se genero de forma correcta la nota de crédito: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticCreditosNotasDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= new Siguiente(next.toLong());
			else
			  regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}	
	
	private void toCheckOrdenDevolucion(Session sesion) throws Exception {
		try {
			sesion.flush();
			TcManticDevolucionesDto devolucion= (TcManticDevolucionesDto)DaoFactory.getInstance().findById(sesion, TcManticDevolucionesDto.class, this.orden.getIdDevolucion());
			if(this.orden.getImporte().equals(devolucion.getTotal()))
				devolucion.setIdDevolucionEstatus(5L); // TERMINADA
			else 
  			if(this.importe.equals(this.orden.getImporte()))
  				devolucion.setIdDevolucionEstatus(6L); // SALDADA
				else
  				devolucion.setIdDevolucionEstatus(4L); // PARCIALIZADA
			DaoFactory.getInstance().update(sesion, devolucion);
			TcManticDevolucionesBitacoraDto estatus= new TcManticDevolucionesBitacoraDto(devolucion.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, devolucion.getConsecutivo(), devolucion.getTotal());
			DaoFactory.getInstance().insert(sesion, estatus);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} 
	
	private void toDeleteAll(String path, String type, List<Nombres> listado) {
    FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File(path), type.toLowerCase());
    if(fileSearch.getResult().size()> 0)
		  for (String matched: fileSearch.getResult()) {
				String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
				if(listado.indexOf(new Nombres(name))< 0) {
          LOG.warn("Nota crédito: "+ this.orden.getConsecutivo()+ " delete file: ".concat(matched));
				  File file= new File(matched);
				  file.delete();
				} // if
      } // for 
	}
	
	private List<Nombres> toListFile(Session sesion, Importado tmp, Long idTipoArchivo) throws Exception {
		List<Nombres> regresar= null;
		Map<String, Object> params=null;
		try {
			params  = new HashMap<>();
			params.put("idTipoArchivo", idTipoArchivo);
			params.put("ruta", tmp.getRuta());
			regresar= (List<Nombres>)DaoFactory.getInstance().toEntitySet(sesion, Nombres.class, "TcManticCreditosArchivosDto", "listado", params);
			regresar.add(new Nombres(tmp.getName()));
		} // try  // try 
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} 
	
	protected void toUpdateDeleteXml(Session sesion) throws Exception {
		//		this(idNotaArchivo, ruta, tamanio, idUsuario, idTipoArchivo, alias, mes, idNotaEntrada, nombre, observacion, ejercicio);
		TcManticCreditosArchivosDto tmp= null;
		if(this.orden.getIdCreditoNota()!= -1L) {
			if(this.xml!= null) {
				tmp= new TcManticCreditosArchivosDto(
					-1L,
					this.xml.getRuta(),
					this.xml.getFileSize(),
					JsfBase.getIdUsuario(),
					1L,
					Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").concat(this.xml.getRuta()).concat(this.xml.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1),
					this.orden.getIdCreditoNota(),
					this.xml.getName(),
					this.xml.getObservaciones(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					1L,
					this.xml.getOriginal()
				);
				TcManticCreditosArchivosDto exists= (TcManticCreditosArchivosDto)DaoFactory.getInstance().toEntity(TcManticCreditosArchivosDto.class, "TcManticCreditosArchivosDto", "identically", tmp.toMap());
				File file= new File(tmp.getAlias());
				if(exists== null && file.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticCreditosArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!file.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").concat(this.xml.getRuta()), ".".concat(this.xml.getFormat().name()), this.toListFile(sesion, this.xml, 1L));
			} // if	
			if(this.pdf!= null) {
				tmp= new TcManticCreditosArchivosDto(
					-1L,
					this.pdf.getRuta(),
					this.pdf.getFileSize(),
					JsfBase.getIdUsuario(),
					2L,
					Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").concat(this.pdf.getRuta()).concat(this.pdf.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1),
					this.orden.getIdCreditoNota(),
					this.pdf.getName(),
					this.pdf.getObservaciones(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					1L,
					this.pdf.getOriginal()
				);
				TcManticCreditosArchivosDto exists= (TcManticCreditosArchivosDto)DaoFactory.getInstance().toEntity(TcManticCreditosArchivosDto.class, "TcManticCreditosArchivosDto", "identically", tmp.toMap());
				File file= new File(tmp.getAlias());
				if(exists== null && file.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticCreditosArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
					if(!file.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
			} // if	
  	} // if	
	}

	public void toDeleteXmlPdf() throws Exception {
		List<TcManticCreditosArchivosDto> list= (List<TcManticCreditosArchivosDto>)DaoFactory.getInstance().findViewCriteria(TcManticCreditosArchivosDto.class, this.orden.toMap(), "all");
		if(list!= null)
			for (TcManticCreditosArchivosDto item: list) {
				LOG.info("Nota crédito: "+ this.orden.getConsecutivo()+ " delete file: "+ item.getAlias());
				File file= new File(item.getAlias());
				file.delete();
			} // for
	}		
	
} 
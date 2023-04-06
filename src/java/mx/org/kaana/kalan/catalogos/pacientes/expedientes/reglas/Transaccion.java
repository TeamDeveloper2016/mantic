package mx.org.kaana.kalan.catalogos.pacientes.expedientes.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.kalan.catalogos.pacientes.expedientes.beans.Expediente;
import mx.org.kaana.kalan.db.dto.TcKalanDiagnosticosDto;
import mx.org.kaana.kalan.db.dto.TcKalanExpedientesBitacoraDto;
import mx.org.kaana.kalan.db.dto.TcKalanExpedientesDto;
import mx.org.kaana.kalan.db.dto.TcKalanMensajesBitacoraDto;
import mx.org.kaana.kalan.db.dto.TcKalanMensajesDetallesDto;
import mx.org.kaana.kalan.db.dto.TcKalanMensajesDto;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Saras;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Transaccion extends IBaseTnx {

	private static final Log LOG= LogFactory.getLog(Transaccion.class);
	
	private Entity cliente;
  private Long idExpediente;
	private List<Expediente> documentos;
  private TcKalanDiagnosticosDto diagnostico;
  private String messageError;
  private List<Entity> clientes;
  private TcKalanMensajesDto mensaje;

	public Transaccion(Long idExpediente) {
    this.idExpediente= idExpediente;
  }
  
	public Transaccion(TcKalanDiagnosticosDto diagnostico) {
    this.diagnostico= diagnostico;
  }
  
	public Transaccion(TcKalanMensajesDto mensaje) {
    this.mensaje = mensaje;
  }
  
	public Transaccion(List<Entity> clientes, TcKalanMensajesDto mensaje) {
    this.clientes= clientes;
    this.mensaje = mensaje;
  }
  
	public Transaccion(Entity cliente, List<Expediente> documentos) {
		this.cliente   = cliente;
		this.documentos= documentos;
	}

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {
      switch (accion) {
				case REGISTRAR:
					regresar= this.toUpdateDeleteFile(sesion);
					break;
				case ELIMINAR:
					regresar= this.toDeleteFile(sesion);
					break;
				case ACTIVAR:
					regresar= this.toDiagnostico(sesion);
					break;
				case PROCESAR:
					regresar= this.toNotificar(sesion);
					break;
				case REPROCESAR:
					regresar= this.toProgramado(sesion);
					break;
				case DEPURAR:
					regresar= this.toCancelar(sesion);
					break;
      } // switch
      if (!regresar) {
        throw new Exception("");
      }
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
  } // ejecutar

	protected Boolean toUpdateDeleteFile(Session sesion) throws Exception {
    Boolean regresar          = Boolean.FALSE;
		TcKalanExpedientesDto tmp = null;
    Map<String, Object> params= new HashMap<>();
    try {
      if(this.cliente.toLong("idCliente")!= -1L) {			
        for (Expediente documento: this.documentos) {
          tmp= new TcKalanExpedientesDto(
            this.cliente.toLong("idCliente"), // Long idCliente, 
            documento.getOriginal(), // String archivo, 
            documento.getRuta(), // String ruta, 
            -1L, // Long idExpediente, 
            Objects.equals(documento.getIdCita(), -1L)? null: documento.getIdCita(), // Long idCita, 
            documento.getName(), // String nombre, 
            documento.getFileSize(), // Long tamanio, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            documento.getIdComodin(), // Long idTipoArchivo, 
            1L, // Long idExpedienteEstatus, 
            documento.getObservaciones(), // String observaciones,
            Configuracion.getInstance().getPropiedadSistemaServidor("path.expedientes").concat(documento.getRuta()).concat(documento.getName()) // String alias     
          );
          params.put("nombre", documento.getName());
          TcKalanExpedientesDto exists= (TcKalanExpedientesDto)DaoFactory.getInstance().toEntity(TcKalanExpedientesDto.class, "TcKalanExpedientesDto", "identically", tmp.toMap());
          if(exists== null) 
            DaoFactory.getInstance().insert(sesion, tmp);
          else {
            tmp.setIdCita(Objects.equals(documento.getIdCita(), -1L)? null: documento.getIdCita());
            tmp.setIdExpediente(exists.getIdExpediente());
            DaoFactory.getInstance().update(sesion, tmp);
          } // if  
          sesion.flush();
          this.toCheckDeleteFile(sesion, documento.getName());
        } // for
        regresar= Boolean.TRUE;
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch	      
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
	} // toUpdateDeleteXml
 
	protected Boolean toDeleteFile(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      TcKalanExpedientesDto item= (TcKalanExpedientesDto)DaoFactory.getInstance().findById(TcKalanExpedientesDto.class, this.idExpediente);
      if(item!= null) {
        item.setIdExpedienteEstatus(2L); // ELIMINADA
        DaoFactory.getInstance().update(item);
        TcKalanExpedientesBitacoraDto bitacora= new TcKalanExpedientesBitacoraDto(
          JsfBase.getIdUsuario(), // Long idUsuario, 
          item.getIdExpedienteEstatus(), // Long idExpedienteEstatus, 
          item.getIdExpediente(), // Long idExpediente, 
          null, // String observaciones, 
          -1L // Long idExpedienteBitacora
        );
        DaoFactory.getInstance().insert(bitacora);
      } // if
      regresar= Boolean.TRUE;
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  }  

  private boolean toDiagnostico(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      if(this.diagnostico!= null) {
        this.diagnostico.setIdUsuario(JsfBase.getIdUsuario());
        if(this.diagnostico.isValid())
          regresar= DaoFactory.getInstance().update(sesion, this.diagnostico)> 0L;
        else
          regresar= DaoFactory.getInstance().insert(sesion, this.diagnostico)> 0L;
      } // if
      else
        regresar= Boolean.TRUE;
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  }
 
  private boolean toNotificar(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      Siguiente consecutivo= this.toSiguiente(sesion);
      this.mensaje.setConsecutivo(consecutivo.getConsecutivo());
      this.mensaje.setEjercicio(consecutivo.getEjercicio());
      this.mensaje.setOrden(consecutivo.getOrden());
      this.mensaje.setIdUsuario(JsfBase.getIdUsuario());
      this.mensaje.setCuando(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      this.mensaje.setAplicado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      this.mensaje.setIdMensajeEstatus(2L);
      DaoFactory.getInstance().insert(sesion, this.mensaje);
      Saras notificar= new Saras();
      for (Entity item: this.clientes) {
        if(!Objects.equals(item.toString("celular"), null)) {
          notificar.setNombre(item.toString("cliente"));
          notificar.setCelular(item.toString("celular"));
          String descripcion= this.mensaje.getDescripcion().replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("<br>", "\\\\n").replaceAll("<strong>", "*").replaceAll("</strong>", "*").replaceAll("<em>", "_").replaceAll("</em>", "_").replaceAll("<u>", "~").replaceAll("</u>", "~");
          notificar.doSendPromocion(descripcion.replaceAll("\n", "\\n"));
        } // if
        TcKalanMensajesDetallesDto detalle= new TcKalanMensajesDetallesDto(
          -1L, // Long idMensajeDetalle, 
          item.toLong("idCliente"), // Long idCliente, 
          this.mensaje.getIdMensaje(), // Long idMensaje
          item.toString("celular") // String celular
        );
        DaoFactory.getInstance().insert(sesion, detalle);
      } // for
      TcKalanMensajesBitacoraDto bitacora= new TcKalanMensajesBitacoraDto(
        -1L, // Long idMensajeBitacora, 
        this.mensaje.getIdMensaje(), // Long idMensaje, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        this.mensaje.getIdMensajeEstatus(), // Long idMensajeEstatus, 
        null // String observaciones
      );
      regresar= DaoFactory.getInstance().insert(sesion, bitacora)> 0L;
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  }
  
  private boolean toProgramado(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      if(this.mensaje.isValid()) {
        this.mensaje.setIdMensajeEstatus(4L);
        this.mensaje.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        this.mensaje.setIdUsuario(JsfBase.getIdUsuario());
        regresar= DaoFactory.getInstance().update(sesion, this.mensaje)> 0L;
        TcKalanMensajesBitacoraDto bitacora= new TcKalanMensajesBitacoraDto(
          -1L, // Long idMensajeBitacora, 
          this.mensaje.getIdMensaje(), // Long idMensaje, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          this.mensaje.getIdMensajeEstatus(), // Long idMensajeEstatus, 
          null // String observaciones
        );
        regresar= DaoFactory.getInstance().insert(sesion, bitacora)> 0L;
      } // if
      else {
        Siguiente consecutivo= this.toSiguiente(sesion);
        this.mensaje.setConsecutivo(consecutivo.getConsecutivo());
        this.mensaje.setEjercicio(consecutivo.getEjercicio());
        this.mensaje.setOrden(consecutivo.getOrden());
        this.mensaje.setIdUsuario(JsfBase.getIdUsuario());
        this.mensaje.setAplicado(null);
        DaoFactory.getInstance().insert(sesion, this.mensaje);
        for (Entity item: this.clientes) {
          TcKalanMensajesDetallesDto detalle= new TcKalanMensajesDetallesDto(
            -1L, // Long idMensajeDetalle, 
            item.toLong("idCliente"), // Long idCliente, 
            this.mensaje.getIdMensaje(), // Long idMensaje
            item.toString("celular") // String celular
          );
          DaoFactory.getInstance().insert(sesion, detalle);
        } // for
        TcKalanMensajesBitacoraDto bitacora= new TcKalanMensajesBitacoraDto(
          -1L, // Long idMensajeBitacora, 
          this.mensaje.getIdMensaje(), // Long idMensaje, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          this.mensaje.getIdMensajeEstatus(), // Long idMensajeEstatus, 
          null // String observaciones
        );
        regresar= DaoFactory.getInstance().insert(sesion, bitacora)> 0L;
      } // else
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  }
  
  private boolean toCancelar(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      this.mensaje.setIdMensajeEstatus(3L);
      DaoFactory.getInstance().update(sesion, this.mensaje);
      TcKalanMensajesBitacoraDto bitacora= new TcKalanMensajesBitacoraDto(
        -1L, // Long idMensajeBitacora, 
        this.mensaje.getIdMensaje(), // Long idMensaje, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        this.mensaje.getIdMensajeEstatus(), // Long idMensajeEstatus, 
        null // String observaciones
      );
      regresar= DaoFactory.getInstance().insert(sesion, bitacora)> 0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  }
    
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("ejercicio", this.getCurrentYear());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcKalanMensajesDto", "siguiente", params, "siguiente");
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
  
}
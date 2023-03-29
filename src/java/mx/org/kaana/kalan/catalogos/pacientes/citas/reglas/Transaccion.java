package mx.org.kaana.kalan.catalogos.pacientes.citas.reglas;

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
import mx.org.kaana.kalan.catalogos.pacientes.citas.beans.Paciente;
import mx.org.kaana.kalan.db.dto.TcKalanCitasBitacoraDto;
import mx.org.kaana.kalan.db.dto.TcKalanCitasDetallesDto;
import mx.org.kaana.kalan.db.dto.TcKalanCitasDto;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Citas;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import org.hibernate.Session;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

public class Transaccion extends IBaseTnx {

  private Paciente paciente;
  private Entity[] servicios;
	private String messageError;

	public Transaccion(Paciente paciente, Entity[] servicios) {
    this.paciente = paciente;
    this.servicios= servicios;
	}

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= Boolean.FALSE;
		try {
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" un cliente");
			switch(accion){
				case AGREGAR:
          if(Objects.equals(this.paciente.getIdCliente(), null) || Objects.equals(this.paciente.getIdCliente(), -1L))
            regresar= this.toAgregar(sesion);
          else
            regresar= this.toExisteCliente(sesion);
					break;
				case MODIFICAR:
          regresar= this.toModificar(sesion);
					break;				
				case ELIMINAR:
          regresar= this.toEliminar(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	// ejecutar
	
  private Boolean toAgregar(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      this.paciente.setClave(this.toLoadRfc(sesion));
      this.paciente.setRfc(this.paciente.getClave());
      this.paciente.setPlazoDias(30L);
      this.paciente.setLimiteCredito(10000D);
      this.paciente.setIdCredito(1L);
      this.paciente.setSaldo(0D);
      this.paciente.setIdUsuario(JsfBase.getIdUsuario());
      this.paciente.setIdUsoCfdi(3L);
      this.paciente.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.paciente.setIdTipoVenta(1L);
      this.paciente.setEspecial(0D);
      this.paciente.setIdRegimenFiscal(null);
      this.paciente.setObservaciones("");
      regresar= DaoFactory.getInstance().insert(sesion, this.paciente)> 0L;
      if(regresar) {
        TcManticDomiciliosDto pivote   = (TcManticDomiciliosDto)DaoFactory.getInstance().findById(sesion, TcManticDomiciliosDto.class, 1L);
        TcManticDomiciliosDto domicilio= new TcManticDomiciliosDto(
          pivote.getAsentamiento(), // String asentamiento, 
          pivote.getIdLocalidad(), // Long idLocalidad, 
          pivote.getCodigoPostal(), // String codigoPostal, 
          pivote.getLatitud(), // String latitud, 
          pivote.getEntreCalle(), // String entreCalle, 
          pivote.getCalle(), // String calle, 
          -1L, // Long idDomicilio, 
          pivote.getNumeroInterior(), // String numeroInterior,  
          pivote.getYcalle(), // String ycalle, 
          pivote.getLongitud(), // String longitud, 
          pivote.getNumeroExterior(), // String numeroExterior, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          ""
        );
        DaoFactory.getInstance().insert(sesion, domicilio);
        TrManticClienteDomicilioDto relacion= new TrManticClienteDomicilioDto(
          this.paciente.getIdCliente(), // Long idCliente, 
          -1L, // Long idClienteDomicilio, 
          domicilio.getIdUsuario(), // Long idUsuario, 
          1L, // Long idTipoDomicilio, 
          domicilio.getIdDomicilio(), // Long idDomicilio, 
          1L, // Long idPrincipal, 
          "" // String observaciones      
        );
        DaoFactory.getInstance().insert(sesion, relacion);
        Long orden= this.toLoadContactos(sesion, this.paciente.getIdCliente());
        if(!Cadena.isVacio(this.paciente.getCelular())) {
          TrManticClienteTipoContactoDto celular= new TrManticClienteTipoContactoDto(
            this.paciente.getIdCliente(), // Long idCliente, 
            domicilio.getIdUsuario(), // Long idUsuario, 
            this.paciente.getCelular(), // String valor, 
            null, // String observaciones, 
            -1L, // Long idClienteTipoContacto, 
            orden++, // Long orden, 
            6L, // Long idTipoContacto, 
            1L // Long idPreferido
          );
          DaoFactory.getInstance().insert(sesion, celular);
        } // if
        if(!Cadena.isVacio(this.paciente.getCorreo())) {
          TrManticClienteTipoContactoDto correo= new TrManticClienteTipoContactoDto(
            this.paciente.getIdCliente(), // Long idCliente, 
            domicilio.getIdUsuario(), // Long idUsuario, 
            this.paciente.getCorreo(), // String valor, 
            null, // String observaciones, 
            -1L, // Long idClienteTipoContacto, 
            orden, // Long orden, 
            9L, // Long idTipoContacto, 
            1L // Long idPreferido
          );
          DaoFactory.getInstance().insert(sesion, correo);
        } // if
        Siguiente consecutivo= this.toSiguiente(sesion);
        TcKalanCitasDto cita= new TcKalanCitasDto(
          this.paciente.getIdCliente(), // Long idCliente, 
          1L, // Long idCitaEstatus, 
          this.paciente.getInicio(), // Timestamp inicio, 
          -1L, // Long idCita, 
          this.paciente.getNotificacion(), // Long notificacion, 
          consecutivo.getEjercicio(), // Long ejercicio, 
          consecutivo.getConsecutivo(), // String consecutivo, 
          Objects.equals(this.paciente.getIkAtendio().getKey(), -1L)? null: this.paciente.getIkAtendio().getKey(), // Long idAtendio, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          this.paciente.getRecordatorio(), // Long recordatorio, 
          null, // String otro, 
          null, // String observaciones, 
          this.paciente.getTermino(), // Timestamp termino, 
          consecutivo.getOrden(), // Long orden, 
          null // Long idVenta
        );
        DaoFactory.getInstance().insert(sesion, cita);
        for (Entity item: this.servicios) {
          TcKalanCitasDetallesDto detalle= new TcKalanCitasDetallesDto(
            -1L, // Long idCitaDetalle, 
            JsfBase.getIdUsuario(), //  Long idUsuario, 
            cita.getIdCita(), // Long idCita, 
            item.toLong("idArticulo") // Long idArticulo
          );
          DaoFactory.getInstance().insert(sesion, detalle);
        } // for
        TcKalanCitasBitacoraDto bitacora= new TcKalanCitasBitacoraDto(
          -1L, // Long idCitaBitacora, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          cita.getIdCitaEstatus(), // Long idCitaEstatus, 
          this.paciente.getComentarios(), // String observaciones, 
          cita.getIdCita() // Long idCita
        );
        this.paciente.setIdCita(cita.getIdCita());
        regresar= DaoFactory.getInstance().insert(sesion, bitacora)> 0L;
        // NOTIFICAR POR WHASTAPP AL CLIENTE
        Citas notificar= new Citas(this.paciente.getRazonSocial().concat(" ").concat(this.paciente.getPaterno()), this.paciente.getCelular(), this.paciente.getInicio(), "agendada", Arrays.asList(this.servicios));
        notificar.doSendCitaCliente(sesion);

        // NOTIFICAR POR WHASTAPP A LA PERSONA QUE LO VA ATENDER
        if(this.paciente.getIkAtendio()!= null && !Objects.equals(this.paciente.getIkAtendio().getKey(), -1L)) {
          notificar.setNombre(this.paciente.getIkAtendio().toString("empleado"));
          if(this.paciente.getIkAtendio().toString("celular")!= null && !Objects.equals(this.paciente.getIkAtendio().toString("celular"), null)) { 
            notificar.setCelular(this.paciente.getIkAtendio().toString("celular"));
            notificar.doSendCitaAtiende(sesion, this.paciente.getRazonSocial().concat(" ").concat(this.paciente.getPaterno()));
          } // if  
        } // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }
  
  private String toLoadRfc(Session sesion) throws Exception {
    String regresar= Fecha.getNombreMesCorto(Fecha.getMesActual()- 1).concat(Fecha.getHoyEstandar().substring(2));
    Map<String, Object> params= new HashMap<>();
    try {
      params.put("mes", Fecha.getHoyEstandar().substring(0, 6));
      Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "TcManticClientesDto", "total", params);
      if(entity!= null && !entity.isEmpty())
        regresar= regresar.concat(Cadena.rellenar(entity.toString("total"), 3, '0', true));
      else
        regresar= regresar.concat(Cadena.rellenar("1", 3, '0', true));
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    }// finally
    return regresar;
  }
  
  private Long toLoadContactos(Session sesion, Long idCliente) throws Exception {
    Long regresar             = 1L;
    Map<String, Object> params= new HashMap<>();
    try {
      params.put("idCliente", idCliente);
      Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "TrManticClienteTipoContactoDto", "total", params);
      if(entity!= null && !entity.isEmpty())
        regresar= entity.toLong("total");
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    }// finally
    return regresar;
  }
  
  private Boolean toExisteCliente(Session sesion) throws Exception {
    Boolean regresar          = Boolean.FALSE;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idCliente", this.paciente.getIdCliente());      
      Paciente clon= (Paciente)DaoFactory.getInstance().toEntity(Paciente.class, "VistaClientesCitasDto", "paciente", params);
      if(clon!= null && (!Objects.equals(this.paciente.getRazonSocial(), clon.getRazonSocial()) || 
         !Objects.equals(this.paciente.getPaterno(), clon.getPaterno()) || 
         !Objects.equals(this.paciente.getMaterno(), clon.getMaterno()))) {
        if(Objects.equals(this.paciente.getIdRegimenFiscal(), -1L))
          this.paciente.setIdRegimenFiscal(null);
        DaoFactory.getInstance().update(sesion, this.paciente);
      } // if  

      TrManticClienteTipoContactoDto celular= (TrManticClienteTipoContactoDto)DaoFactory.getInstance().toEntity(sesion, TrManticClienteTipoContactoDto.class, "TrManticClienteTipoContactoDto", "celulares", params);
      if(celular!= null && !Objects.equals(this.paciente.getCelular(), celular.getValor())) {
        celular.setValor(this.paciente.getCelular());
        DaoFactory.getInstance().update(sesion, celular);
      } // if  
      
      TrManticClienteTipoContactoDto correo= (TrManticClienteTipoContactoDto)DaoFactory.getInstance().toEntity(sesion, TrManticClienteTipoContactoDto.class, "TrManticClienteTipoContactoDto", "correos", params);
      if(correo!= null && !Objects.equals(this.paciente.getCorreo(), correo.getValor())) {
        correo.setValor(this.paciente.getCorreo());
        DaoFactory.getInstance().update(sesion, correo);
      } // if  
      
      Siguiente consecutivo= this.toSiguiente(sesion);
      TcKalanCitasDto cita= new TcKalanCitasDto(
        this.paciente.getIdCliente(), // Long idCliente, 
        1L, // Long idCitaEstatus, 
        this.paciente.getInicio(), // Timestamp inicio, 
        -1L, // Long idCita, 
        this.paciente.getNotificacion(), // Long notificacion, 
        consecutivo.getEjercicio(), // Long ejercicio, 
        consecutivo.getConsecutivo(), // String consecutivo, 
        Objects.equals(this.paciente.getIkAtendio().getKey(), -1L)? null: this.paciente.getIkAtendio().getKey(), // Long idAtendio, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        this.paciente.getRecordatorio(), // Long recordatorio, 
        null, // String otro, 
        null, // String observaciones, 
        this.paciente.getTermino(), // Timestamp termino, 
        consecutivo.getOrden(), // Long orden, 
        null // Long idVenta
      );
      DaoFactory.getInstance().insert(sesion, cita);
      for (Entity item: this.servicios) {
        TcKalanCitasDetallesDto detalle= new TcKalanCitasDetallesDto(
          -1L, // Long idCitaDetalle, 
          JsfBase.getIdUsuario(), //  Long idUsuario, 
          cita.getIdCita(), // Long idCita, 
          item.toLong("idArticulo") // Long idArticulo
        );
        DaoFactory.getInstance().insert(sesion, detalle);
      } // for
      TcKalanCitasBitacoraDto bitacora= new TcKalanCitasBitacoraDto(
        -1L, // Long idCitaBitacora, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        cita.getIdCitaEstatus(), // Long idCitaEstatus, 
        this.paciente.getComentarios(), // String observaciones, 
        cita.getIdCita() // Long idCita
      );
      this.paciente.setIdCita(cita.getIdCita());
      regresar= DaoFactory.getInstance().insert(sesion, bitacora)> 0L;
      
      // NOTIFICAR POR WHASTAPP AL CLIENTE
      Citas notificar= new Citas(this.paciente.getRazonSocial().concat(" ").concat(this.paciente.getPaterno()), this.paciente.getCelular(), this.paciente.getInicio(), "agendo", Arrays.asList(this.servicios));
      notificar.doSendCitaCliente(sesion);
      // NOTIFICAR POR WHASTAPP A LA PERSONA QUE LO VA ATENDER
      if(this.paciente.getIkAtendio()!= null && !Objects.equals(this.paciente.getIkAtendio().getKey(), -1L)) {
        notificar.setNombre(this.paciente.getIkAtendio().toString("empleado"));
        if(this.paciente.getIkAtendio().toString("celular")!= null && !Objects.equals(this.paciente.getIkAtendio().toString("celular"), null)) { 
          notificar.setCelular(this.paciente.getIkAtendio().toString("celular"));
          notificar.doSendCitaAtiende(sesion, this.paciente.getRazonSocial().concat(" ").concat(this.paciente.getPaterno()));
        } // if  
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    }// finally
    return regresar;
  }
  
  private Boolean toModificar(Session sesion) throws Exception {
    Boolean regresar          = Boolean.FALSE;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idCliente", this.paciente.getIdCliente());      
      Paciente clon= (Paciente)DaoFactory.getInstance().toEntity(Paciente.class, "VistaClientesCitasDto", "paciente", params);
      if(clon!= null && (!Objects.equals(this.paciente.getRazonSocial(), clon.getRazonSocial()) || 
         !Objects.equals(this.paciente.getPaterno(), clon.getPaterno()) || 
         !Objects.equals(this.paciente.getMaterno(), clon.getMaterno()))) {
        if(Objects.equals(this.paciente.getIdRegimenFiscal(), -1L))
          this.paciente.setIdRegimenFiscal(null);
        DaoFactory.getInstance().update(sesion, this.paciente);
      } // if  

      TrManticClienteTipoContactoDto celular= (TrManticClienteTipoContactoDto)DaoFactory.getInstance().toEntity(sesion, TrManticClienteTipoContactoDto.class, "TrManticClienteTipoContactoDto", "celulares", params);
      if(celular!= null && !Objects.equals(this.paciente.getCelular(), celular.getValor())) {
        celular.setValor(this.paciente.getCelular());
        DaoFactory.getInstance().update(sesion, celular);
      } // if  
      
      TrManticClienteTipoContactoDto correo= (TrManticClienteTipoContactoDto)DaoFactory.getInstance().toEntity(sesion, TrManticClienteTipoContactoDto.class, "TrManticClienteTipoContactoDto", "correos", params);
      if(correo!= null && !Objects.equals(this.paciente.getCorreo(), correo.getValor())) {
        correo.setValor(this.paciente.getCorreo());
        DaoFactory.getInstance().update(sesion, correo);
      } // if  
      
      TcKalanCitasDto cita= (TcKalanCitasDto)DaoFactory.getInstance().findById(sesion, TcKalanCitasDto.class, this.paciente.getIdCita());
      if(cita!= null && (
         !Objects.equals(this.paciente.getInicio(), cita.getInicio()) ||
         !Objects.equals(this.paciente.getTermino(), cita.getTermino()) ||
         !Objects.equals(this.paciente.getRecordatorio(), cita.getRecordatorio()) ||
         !Objects.equals(this.paciente.getNotificacion(), cita.getNotificacion()))) {
        if(!Objects.equals(this.paciente.getInicio(), cita.getInicio()) || !Objects.equals(this.paciente.getTermino(), cita.getTermino())) {
          cita.setIdCitaEstatus(5L);

          // NOTIFICAR POR WHASTAPP AL CLIENTE
          Citas notificar= new Citas(this.paciente.getRazonSocial().concat(" ").concat(this.paciente.getPaterno()), this.paciente.getCelular(), this.paciente.getInicio(), "reprogramo", Arrays.asList(this.servicios));
          notificar.doSendCitaCliente(sesion);
          // NOTIFICAR POR WHASTAPP A LA PERSONA QUE LO VA ATENDER
          if(this.paciente.getIkAtendio()!= null && !Objects.equals(this.paciente.getIkAtendio().getKey(), -1L)) {
            notificar.setNombre(this.paciente.getIkAtendio().toString("empleado"));
            if(this.paciente.getIkAtendio().toString("celular")!= null && !Objects.equals(this.paciente.getIkAtendio().toString("celular"), null)) { 
              notificar.setCelular(this.paciente.getIkAtendio().toString("celular"));
              notificar.doSendCitaAtiende(sesion, this.paciente.getRazonSocial().concat(" ").concat(this.paciente.getPaterno()));
            } // if  
          } // if
        } // if  
        cita.setInicio(this.paciente.getInicio());
        cita.setTermino(this.paciente.getTermino());
        cita.setRecordatorio(this.paciente.getRecordatorio());
        cita.setNotificacion(this.paciente.getNotificacion());
        DaoFactory.getInstance().update(sesion, cita);
        this.toFillArticulos(sesion);
        TcKalanCitasBitacoraDto bitacora= new TcKalanCitasBitacoraDto(
          -1L, // Long idCitaBitacora, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          cita.getIdCitaEstatus(), // Long idCitaEstatus, 
          this.paciente.getComentarios(), // String observaciones, 
          this.paciente.getIdCita() // Long idCita
        );
        regresar= DaoFactory.getInstance().insert(sesion, bitacora)> 0L;
      } // if  
      else
        regresar= Boolean.TRUE;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    }// finally
    return regresar;
  }
  
	private void toFillArticulos(Session sesion) throws Exception {
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idCita", this.paciente.getIdCita());      
      List<TcKalanCitasDetallesDto> todos=(List<TcKalanCitasDetallesDto>) DaoFactory.getInstance().toEntitySet(sesion, TcKalanCitasDetallesDto.class, "TcKalanCitasDetallesDto", "detalle", params);
      for (TcKalanCitasDetallesDto item: todos) {
        int index= this.toIndexOf(item);
        if (index< 0) 
          DaoFactory.getInstance().delete(sesion, item);
      } // if  
      for (Entity articulo: this.servicios) {
        params.put("idArticulo", articulo.toLong("idArticulo"));
        TcKalanCitasDetallesDto item=(TcKalanCitasDetallesDto) DaoFactory.getInstance().toEntity(sesion, TcKalanCitasDetallesDto.class, "TcKalanCitasDetallesDto", "identically", params);
        if (item== null) {
          item= new TcKalanCitasDetallesDto(
            -1L, // Long idCitaDetalle, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            this.paciente.getIdCita(), // Long idCita, 
            articulo.toLong("idArticulo") // Long idArticulo
          );
          DaoFactory.getInstance().insert(sesion, item);
        } // if  
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    }// finally
	}
  
  private int toIndexOf(TcKalanCitasDetallesDto articulo) {
    int regresar= -1;
    if(this.servicios!= null && this.servicios.length> 0) {
      regresar= 0;
      while (regresar< this.servicios.length) {
        if(Objects.equals(this.servicios[regresar].toLong("idArticulo"), articulo.getIdArticulo()))
          break;
        regresar++;
      } // while 
      regresar= regresar>= this.servicios.length? -1: regresar;
    } // if
    return regresar;
  }  
  
  private Boolean toEliminar(Session sesion) throws Exception {
    Boolean regresar          = Boolean.FALSE;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idCita", this.paciente.getIdCita());      
      TcKalanCitasDto cita= (TcKalanCitasDto)DaoFactory.getInstance().findById(sesion, TcKalanCitasDto.class, this.paciente.getIdCita());
      if(cita!= null) {
        cita.setIdCitaEstatus(6L);
        DaoFactory.getInstance().update(sesion, cita);
        TcKalanCitasBitacoraDto bitacora= new TcKalanCitasBitacoraDto(
          -1L, // Long idCitaBitacora, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          cita.getIdCitaEstatus(), // Long idCitaEstatus, 
          this.paciente.getComentarios(), // String observaciones, 
          this.paciente.getIdCita() // Long idCita
        );
        DaoFactory.getInstance().insert(sesion, bitacora);

        // NOTIFICAR POR WHASTAPP AL CLIENTE
        Citas notificar= new Citas(this.paciente.getRazonSocial().concat(" ").concat(this.paciente.getPaterno()), this.paciente.getCelular(), this.paciente.getInicio(), "eliminado", Arrays.asList(this.servicios));
        notificar.doSendCitaCliente(sesion);
        // NOTIFICAR POR WHASTAPP A LA PERSONA QUE LO VA ATENDER
        if(this.paciente.getIkAtendio()!= null && !Objects.equals(this.paciente.getIkAtendio().getKey(), -1L)) {
          notificar.setNombre(this.paciente.getIkAtendio().toString("empleado"));
          if(this.paciente.getIkAtendio().toString("celular")!= null && !Objects.equals(this.paciente.getIkAtendio().toString("celular"), null)) { 
            notificar.setCelular(this.paciente.getIkAtendio().toString("celular"));
            notificar.doSendCitaAtiende(sesion, this.paciente.getRazonSocial().concat(" ").concat(this.paciente.getPaterno()));
          } // if  
        } // if
      } // if
      regresar= Boolean.TRUE;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
 
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("ejercicio", this.getCurrentYear());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcKalanCitasDto", "siguiente", params, "siguiente");
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
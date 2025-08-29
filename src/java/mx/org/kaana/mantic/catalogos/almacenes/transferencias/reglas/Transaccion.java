package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Bonanza;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans.Umbral;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.solicitudes.beans.Persona;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends ComunInventarios implements Serializable {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
  private static final long serialVersionUID = -5795360019231558776L;
	
  private TcManticTransferenciasDto dto;
	private Long idTransferenciaEstatus;
	private Long idFaltante;
  private List<Umbral> fuentes;
  private List<Umbral> destinos;
  private List<Persona> personas;

	public Transaccion(Long idFaltante) {
		this(new TcManticTransferenciasDto(-1L));
		this.idFaltante= idFaltante;
	}
	
	public Transaccion(TcManticTransferenciasDto dto) {
		this(dto, 1L);
    this.personas= new ArrayList<>();
	}
	
	public Transaccion(TcManticTransferenciasDto dto, TcManticTransferenciasBitacoraDto bitacora) throws Exception {
		this(dto, bitacora.getIdTransferenciaEstatus());
		this.bitacora = bitacora;
    this.toLoadArticulos(dto.getIdTransferencia());
    this.toLoadPersonas(dto.getIdTransferencia());
	}
	
	public Transaccion(TcManticTransferenciasDto dto, List<Articulo> articulos) {
		this(dto, new ArrayList<>(), articulos);
	}
	
	public Transaccion(TcManticTransferenciasDto dto, List<Persona> personas, List<Articulo> articulos) {
		this(dto, dto.getIdTransferenciaEstatus());
    this.personas = personas;
		this.articulos= articulos;
	}
	
	public Transaccion(TcManticTransferenciasDto dto, Long idTransferenciaEstatus) {
		this.dto= dto;
		if(this.dto.getIdSolicito()!= null && this.dto.getIdSolicito()< 0L)
		  this.dto.setIdSolicito(null);
		this.idTransferenciaEstatus= idTransferenciaEstatus;
	}

	public Transaccion(List<Umbral> fuentes) {
    this.fuentes = fuentes;
    this.personas= new ArrayList<>();
  }
  
	public Transaccion(List<Umbral> fuentes, List<Umbral> destinos) {
    this.fuentes = fuentes;
    this.destinos= destinos;
    this.personas= new ArrayList<>();
  }
  
  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= false;
    try {			
    	this.messageError    = "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para transferencia de articulos");
  		Siguiente consecutivo= null;
      this.idUsuario       = JsfBase.getIdUsuario();
      this.idEmpresa       = JsfBase.getAutentifica().getEmpresa().getIdEmpresa();
      switch (accion) {
        case ACTIVAR:
        case AGREGAR:
					consecutivo= this.toSiguiente(sesion);
					this.dto.setConsecutivo(consecutivo.getConsecutivo());
					this.dto.setOrden(consecutivo.getOrden());
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
					this.toFillArticulos(sesion, accion);
          this.toPersonas(sesion);
          this.toBitacora(sesion);
					break;
        case MODIFICAR:
          this.dto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					this.toFillArticulos(sesion, accion);
          regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
          this.toPersonas(sesion);
          this.toBitacora(sesion);
          break;
        case ELIMINAR:
					this.dto.setIdTransferenciaEstatus(2L); // ELIMINADO
          regresar= DaoFactory.getInstance().update(sesion, this.dto)>= 1L;
          this.toBitacora(sesion);
          break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, TcManticFaltantesDto.class, this.idFaltante)>= 1L;
					break;
        case REGISTRAR:
      		this.articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaAlmacenesTransferenciasDto", "detalle", dto.toMap());
					this.dto.setIdTransferenciaEstatus(this.idTransferenciaEstatus);
					if(!Objects.equals(this.idTransferenciaEstatus, 1L))
					  this.toFillArticulos(sesion, accion);
					regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
          this.toBitacora(sesion, this.bitacora);
          // SOLICITUDES Y SE PASAN A TRANSITO
					if(Objects.equals(this.dto.getIdTransferenciaTipo(), 4L) && Objects.equals(this.idTransferenciaEstatus, 3L)) 
            this.notificar(sesion);
          break;
        case PROCESAR:
          regresar= this.toProcesar(sesion, this.fuentes) && this.toProcesar(sesion, this.destinos);
          break;
        case REPROCESAR:
          regresar= this.toProcesar(sesion, this.fuentes);
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

	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", this.dto.getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticTransferenciasDto", "siguiente", params, "siguiente");
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

	private void toFillArticulos(Session sesion, EAccion accion) throws Exception {
		List<Articulo> todos=(List<Articulo>) DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaAlmacenesTransferenciasDto", "detalle", this.dto.toMap());
		for (Articulo item: todos) 
			if (this.articulos.indexOf(item)< 0) 
				DaoFactory.getInstance().delete(sesion, item.toTransferenciaDetalle());
		for (Articulo articulo: this.articulos) {
			TcManticTransferenciasDetallesDto item= articulo.toTransferenciaDetalle();
			item.setIdTransferencia(this.dto.getIdTransferencia());
			if (DaoFactory.getInstance().findIdentically(sesion, TcManticTransferenciasDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
			else 
				DaoFactory.getInstance().update(sesion, item);
			if(EAccion.ACTIVAR.equals(accion)) 
				this.toMovimientos(sesion, this.dto.getConsecutivo(), this.dto.getIdAlmacen(), this.dto.getIdDestino(), articulo, this.idTransferenciaEstatus);
			else
  			if(EAccion.REGISTRAR.equals(accion)) {
					TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
          articulo.setSolicitados(articulo.getCantidad());
					switch(this.idTransferenciaEstatus.intValue()) {
						case 3: // TRANSITO
    					this.toMovimientosAlmacenOrigen(sesion, this.dto.getConsecutivo(), this.dto.getIdAlmacen(), articulo, umbrales, this.idTransferenciaEstatus);
							break;
						case 4: // CANCELAR
    					this.toMovimientosAlmacenOrigen(sesion, this.dto.getConsecutivo(), this.dto.getIdAlmacen(), articulo, umbrales, this.idTransferenciaEstatus);
							break;
						case 5: // RECEPCION
    					this.toMovimientosAlmacenDestino(sesion, this.dto.getConsecutivo(), this.dto.getIdDestino(), articulo, umbrales, articulo.getCantidad());
							break;
					} // switch
				} // if
		} // for
	}

  private Boolean toProcesar(Session sesion, List<Umbral> articulos) throws Exception {
    Boolean regresar          = Boolean.FALSE;
    Map<String, Object> params= new HashMap<>();
    try {  
      for (Umbral item: articulos) {
        params.put("idAlmacen", item.getIdAlmacen());      
        params.put("idArticulo", item.getIdArticulo());      
        params.put("minimo", item.getMinimo());      
        params.put("maximo", item.getMaximo());      
        if(Objects.equals(item.getAction(), ESql.UPDATE)) {
          DaoFactory.getInstance().updateAll(sesion, TcManticAlmacenesArticulosDto.class, params);
          DaoFactory.getInstance().updateAll(sesion, TcManticArticulosDto.class, params, "verificado");
        } // if  
      } // for
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

  private Boolean toPersonas(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {      
      for (Persona item: this.personas) {
        switch(item.getSql()) {
          case SELECT:
            break;
          case INSERT:
            item.setIdTransferencia(this.dto.getIdTransferencia());
            DaoFactory.getInstance().insert(sesion, item);
            break;
          case UPDATE:
            item.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            DaoFactory.getInstance().update(sesion, item);
            break;
          case DELETE:
            DaoFactory.getInstance().delete(sesion, item);
            break;
        } // switch
      } // for
      regresar= Boolean.TRUE;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }

  private Boolean toBitacora(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      regresar= this.toBitacora(sesion, new TcManticTransferenciasBitacoraDto(-1L, null, JsfBase.getIdUsuario(), null, this.dto.getIdTransferenciaEstatus(), this.dto.getIdTransferencia()));
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
  
  private Boolean toBitacora(Session sesion, TcManticTransferenciasBitacoraDto temporal) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      regresar= DaoFactory.getInstance().insert(sesion, temporal).intValue()> 0;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }

  private void notificar(Session sesion) throws Exception {
    Bonanza bonanza           = null;
    List<Entity> celulares    = null;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idUsuario", this.dto.getIdUsuario());
      Entity fuente= (Entity)DaoFactory.getInstance().toEntity(sesion, "TcJanalUsuariosDto", "usuario", params);
      if(!Objects.equals(fuente, null) && !fuente.isEmpty()) {
        params.put("idPersona", fuente.toLong("idPersona"));
        celulares= (List<Entity>)DaoFactory.getInstance().toEntitySet("TrManticPersonaTipoContactoDto", "celular", params);
        if(!Objects.equals(celulares, null) && !celulares.isEmpty()) {
          bonanza= new Bonanza(Cadena.letraCapital(fuente.toString("nombre")), "celular", String.valueOf(this.articulos.size()), this.dto.getConsecutivo(), this.dto.getConsecutivo());
          for (Entity item: celulares) {
            bonanza.setCelular(item.toString("valor"));
            bonanza.doSendSolicitudFuente(sesion);
          } // for
        } // if  
      } // if
      params.put("idTransferencia", this.dto.getIdTransferencia());      
      this.personas= (List<Persona>)DaoFactory.getInstance().toEntitySet(Persona.class, "TcManticTransferenciasPersonasDto", "igual", params);
      if(!Objects.equals(personas, null) && !personas.isEmpty()) {
        for (Persona item: this.personas) {
          params.put("idPersona", item.getIdPersona());
          celulares= (List<Entity>)DaoFactory.getInstance().toEntitySet("TrManticPersonaTipoContactoDto", "celular", params);
          if(!Objects.equals(celulares, null) && !celulares.isEmpty()) {
            bonanza  = new Bonanza(Cadena.letraCapital(item.getNombre()), "celular", String.valueOf(this.articulos.size()), this.dto.getConsecutivo(), this.dto.getConsecutivo());
            for (Entity celular: celulares) {
              bonanza.setCelular(celular.toString("valor"));
              bonanza.doSendSolicitudDestino(sesion, Cadena.letraCapital(fuente.toString("nombre")));
            } // for  
          } // if
        } // for  
      } // if  
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }

  private void toLoadPersonas(Long idTransferencia) throws Exception {
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idTransferencia", idTransferencia);      
      this.personas= (List<Persona>)DaoFactory.getInstance().toEntitySet(Persona.class, "TcManticTransferenciasPersonasDto", "igual", params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  } 

  private void toLoadArticulos(Long idTransferencia) throws Exception {
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idTransferencia", idTransferencia);      
      this.articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaAlmacenesTransferenciasDto", "detalle", params, Constantes.SQL_TOPE_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  } 

}

package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
  private TcManticTransferenciasDto dto;
	private List<Articulo> articulos;
	private Long idTransferenciaEstatus;
	private Long idFaltante;
  private TcManticTransferenciasBitacoraDto bitacora;
  private String messageError;

	public Transaccion(Long idFaltante) {
		this(new TcManticTransferenciasDto(-1L));
		this.idFaltante= idFaltante;
	}
	
	public Transaccion(TcManticTransferenciasDto dto) {
		this(dto, 1L);
	}
	
	public Transaccion(TcManticTransferenciasDto dto, TcManticTransferenciasBitacoraDto bitacora) {
		this(dto, bitacora.getIdTransferenciaEstatus());
		this.bitacora = bitacora;
	}
	
	public Transaccion(TcManticTransferenciasDto dto, List<Articulo> articulos) {
		this(dto, dto.getIdTransferenciaEstatus());
		this.articulos= articulos;
	}
	
	public Transaccion(TcManticTransferenciasDto dto, Long idTransferenciaEstatus) {
		this.dto= dto;
		if(this.dto.getIdSolicito()!= null && this.dto.getIdSolicito()< 0L)
		  this.dto.setIdSolicito(null);
		this.idTransferenciaEstatus= idTransferenciaEstatus;
	}

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= false;
    try {			
    	this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para transferencia de articulos.");
  		Long consecutivo= 0L;
      switch (accion) {
        case ACTIVAR:
        case AGREGAR:
					consecutivo= this.toSiguiente(sesion);
					this.dto.setConsecutivo(this.dto.getEjercicio()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
					this.dto.setOrden(consecutivo);
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
					this.toFillArticulos(sesion, accion);
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.dto.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
					break;
        case MODIFICAR:
          this.dto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					this.toFillArticulos(sesion, accion);
          regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
          break;
        case ELIMINAR:
          //if (DaoFactory.getInstance().deleteAll(sesion, TcManticTransferenciasBitacoraDto.class, this.dto.toMap())> -1L) 
					this.dto.setIdTransferenciaEstatus(2L);
          regresar= DaoFactory.getInstance().update(sesion, this.dto)>= 1L;
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.dto.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
          break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, TcManticFaltantesDto.class, this.idFaltante)>= 1L;
					break;
        case REGISTRAR:
      		this.articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaAlmacenesTransferenciasDto", "detalle", dto.toMap());
					this.dto.setIdTransferenciaEstatus(this.idTransferenciaEstatus);
					this.toFillArticulos(sesion, accion);
					regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, this.bitacora).intValue()> 0;
          // throw new RuntimeException("ERROR PROVACADO INTENCIONALMENTE");
          break;
      } // switch
      if(!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e.getMessage());
    } // catch		
    return regresar;
  } // ejecutar

	private Long toUbicacion(Session sesion, Long idAlmacen, Long idArticulo) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idAlmacen", idAlmacen);
			params.put("idArticulo", idArticulo);
			TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class, params, "ubicacion");
			if(ubicacion== null) {
				TcManticAlmacenesUbicacionesDto general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
				if(general== null) {
					general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), this.dto.getIdDestino(), -1L);
					DaoFactory.getInstance().insert(sesion, general);
				} // if	
				regresar= general.getIdAlmacenUbicacion();
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

	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar= 1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", this.dto.getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticTransferenciasDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= next.toLong();
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
				this.toMovimientos(sesion, item);
			else
  			if(EAccion.REGISTRAR.equals(accion)) {
					TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
					switch(this.idTransferenciaEstatus.intValue()) {
						case 3: // TRANSITO
    					this.toMovimientosAlmacenOrigen(sesion, item, umbrales);
							break;
						case 4: // CANCELAR
    					this.toMovimientosAlmacenOrigen(sesion, item, umbrales);
							break;
						case 5: // RECEPCION
    					this.toMovimientosAlmacenDestino(sesion, item, umbrales);
							break;
					} // switch
				} // if
		} // for
	}
	
	private void toMovimientos(Session sesion, TcManticTransferenciasDetallesDto articulo) throws Exception {
		TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
		this.toMovimientosAlmacenOrigen(sesion, articulo, umbrales);
		this.toMovimientosAlmacenDestino(sesion, articulo, umbrales);
	}
	
	private void toMovimientosAlmacenOrigen(Session sesion, TcManticTransferenciasDetallesDto articulo, TcManticArticulosDto umbrales) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			//Afectar el almacen original restando los articulos que fueron extraidos
			params.put("idAlmacen", this.dto.getIdAlmacen());
			params.put("idArticulo", articulo.getIdArticulo());
			TcManticAlmacenesArticulosDto origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findIdentically(TcManticAlmacenesArticulosDto.class, params);
			if(origen== null) 
				origen= this.toCreateAlmacenArticulo(sesion, articulo, this.dto.getIdAlmacen(), umbrales);
			if(origen.getStock()< articulo.getCantidad())
 				LOG.warn("No existen suficientes articulos ["+ articulo.getIdArticulo()+ "] en el stock del almacen origen ["+ this.dto.getIdAlmacen()+ "] stock["+ origen.getStock()+ "] cantidad["+ articulo.getCantidad()+ "] !");
			// si el estatus es el de cancelar entonces hacer los movimientos inversos al traspaso
			if(this.idTransferenciaEstatus.intValue()== 4)
			  origen.setStock(Numero.toRedondearSat(origen.getStock()+ articulo.getCantidad()));
			else
			  origen.setStock(Numero.toRedondearSat(origen.getStock()- articulo.getCantidad()));
			DaoFactory.getInstance().update(sesion, origen);
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario== null)
			  this.toCreateInvetario(sesion, articulo, this.dto.getIdAlmacen(), false);
			else {
   			// si el estatus es el de cancelar entonces hacer los movimientos inversos al traspaso
  			if(this.idTransferenciaEstatus.intValue()== 4) {
	  			inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()- articulo.getCantidad()));
		  		inventario.setStock(Numero.toRedondearSat(inventario.getStock()+ articulo.getCantidad()));
				} // if
				else {
	  			inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()+ articulo.getCantidad()));
		  		inventario.setStock(Numero.toRedondearSat(inventario.getStock()- articulo.getCantidad()));
				} // if
  			DaoFactory.getInstance().update(sesion, inventario);
			} // if
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
	private void toMovimientosAlmacenDestino(Session sesion, TcManticTransferenciasDetallesDto articulo, TcManticArticulosDto umbrales) throws Exception {
		Map<String, Object> params= null;
		try {
			//Afectar el almacen destino sumando los articulos que fueron agregados
			params=new HashMap<>();
			params.put("idAlmacen", this.dto.getIdDestino());
			params.put("idArticulo", articulo.getIdArticulo());
			TcManticAlmacenesArticulosDto origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findIdentically(TcManticAlmacenesArticulosDto.class, params);
			if(origen== null) 
				origen= this.toCreateAlmacenArticulo(sesion, articulo, this.dto.getIdDestino(), umbrales);
			origen.setStock(Numero.toRedondearSat(origen.getStock()+ articulo.getCantidad()));
			DaoFactory.getInstance().update(sesion, origen);
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario== null)
			  this.toCreateInvetario(sesion, articulo, this.dto.getIdDestino(), true);
			else {
				inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()+ articulo.getCantidad()));
				inventario.setStock(Numero.toRedondearSat(inventario.getStock()- articulo.getCantidad()));
  			DaoFactory.getInstance().update(sesion, inventario);
			} // if
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
	private TcManticAlmacenesArticulosDto toCreateAlmacenArticulo(Session sesion,  TcManticTransferenciasDetallesDto articulo, Long idAlmacen, TcManticArticulosDto umbrales) throws Exception {
    TcManticAlmacenesArticulosDto regresar= new TcManticAlmacenesArticulosDto(
			umbrales.getMinimo(), // Double minimo, 
			-1L, // Long idAlmacenArticulo, 
			JsfBase.getIdUsuario(), // Long idUsuario, 
			idAlmacen, // Long idAlmacen, 
			umbrales.getMaximo(), // Double maximo, 
			this.toUbicacion(sesion, idAlmacen, articulo.getIdArticulo()), // Long idAlmacenUbicacion, 
			articulo.getIdArticulo(), // Long idArticulo, 
			0D // Double stock
		);
		DaoFactory.getInstance().insert(sesion, regresar);
		return regresar;
	}
		
	private TcManticInventariosDto toCreateInvetario(Session sesion,  TcManticTransferenciasDetallesDto articulo, Long idAlmacen, boolean inicial) throws Exception {
    TcManticInventariosDto regresar= new TcManticInventariosDto(
			JsfBase.getIdUsuario(), // Long idUsuario, 
			idAlmacen, // Long idAlmacen, 
			0D, // Double entradas, 
			-1L, // Long idInventario, 
			articulo.getIdArticulo(), // Long idArticulo, 
			inicial? articulo.getCantidad(): articulo.getCantidad()* -1D, // Double inicial, 
			inicial? articulo.getCantidad(): articulo.getCantidad()* -1D, // Double stock, 
			articulo.getCantidad(), // Double salidas, 
			new Long(Fecha.getAnioActual()), // Long ejercicio, 
			1L // Long idAutomatico
		);
		DaoFactory.getInstance().insert(sesion, regresar);
		return regresar;
	}
		
}

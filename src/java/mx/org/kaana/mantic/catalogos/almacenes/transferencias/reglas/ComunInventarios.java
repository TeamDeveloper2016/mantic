package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public abstract class ComunInventarios extends IBaseTnx {

	private static final Log LOG=LogFactory.getLog(ComunInventarios.class);
	
	protected List<Articulo> articulos;
  protected TcManticTransferenciasBitacoraDto bitacora;
  protected String messageError;
	
	protected Long toUbicacion(Session sesion, Long idAlmacen, Long idArticulo) throws Exception {
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
					general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), idAlmacen, -1L);
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

	protected void toMovimientos(Session sesion, String consecutivo, Long idAlmacen, Long idDestino, Articulo articulo, Long idTransferenciaEstatus) throws Exception {
		TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
		this.toMovimientosAlmacenOrigen(sesion, consecutivo, idAlmacen, articulo, umbrales, idTransferenciaEstatus);
		this.toMovimientosAlmacenDestino(sesion, consecutivo, idDestino, articulo, umbrales, articulo.getCantidad());
	}
	
	protected void toMovimientosAlmacenOrigen(Session sesion, String consecutivo, Long idAlmacen, Articulo articulo, TcManticArticulosDto umbrales, Long idTransferenciaEstatus) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			//Afectar el almacen original restando los articulos que fueron extraidos
			params.put("idAlmacen", idAlmacen);
			params.put("idArticulo", articulo.getIdArticulo());
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario== null)
			  inventario= this.toCreateInvetario(sesion, articulo, idAlmacen, false);
			else {
   			// si el estatus es el de cancelar entonces hacer los movimientos inversos al traspaso
  			if(idTransferenciaEstatus.intValue()== 4) {
	  			inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()- articulo.getCantidad()));
		  		inventario.setStock(inventario.getIdAutomatico().equals(1L)? 0D: Numero.toRedondearSat(inventario.getStock()+ articulo.getCantidad()));
				} // if
				else {
	  			inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()+ articulo.getCantidad()));
		  		inventario.setStock(inventario.getIdAutomatico().equals(1L)? 0D: Numero.toRedondearSat(inventario.getStock()- articulo.getCantidad()));
				} // if
  			DaoFactory.getInstance().update(sesion, inventario);
			} // if
			TcManticAlmacenesArticulosDto origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findIdentically(TcManticAlmacenesArticulosDto.class, params);
			if(origen== null) 
				origen= this.toCreateAlmacenArticulo(sesion, articulo, idAlmacen, umbrales);
			if(origen.getStock()< articulo.getCantidad())
 				LOG.warn("No existen suficientes articulos ["+ articulo.getIdArticulo()+ "] en el stock del almacen origen ["+ idAlmacen+ "] stock["+ origen.getStock()+ "] cantidad["+ articulo.getCantidad()+ "] !");

			// generar un registro en la bitacora de movimientos de los articulos 
			TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
			  consecutivo, // String consecutivo, 
				4L, // Long idTipoMovimiento, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				idAlmacen, // Long idAlmacen, 
				-1L, // Long idMovimiento, 
				articulo.getCantidad()* -1, // Double cantidad, 
				articulo.getIdArticulo(), // Long idArticulo, 
				origen.getStock(), // Double stock, 
				Numero.toRedondearSat(origen.getStock()- articulo.getCantidad()), // Double calculo
				null // String observaciones
		  );
			if(idTransferenciaEstatus.intValue()== 4) {
        movimiento.setCantidad(articulo.getCantidad());
				movimiento.setObservaciones("SE CANCELO LA TRANSFERENCIA");
			} // if
			DaoFactory.getInstance().insert(sesion, movimiento);
			
			// si el estatus es el de cancelar entonces hacer los movimientos inversos al traspaso
			if(idTransferenciaEstatus.intValue()== 4)
			  origen.setStock(inventario.getIdAutomatico().equals(1L)? 0D: Numero.toRedondearSat(origen.getStock()+ articulo.getCantidad()));
			else
			  origen.setStock(inventario.getIdAutomatico().equals(1L)? 0D: Numero.toRedondearSat(origen.getStock()- articulo.getCantidad()));
			DaoFactory.getInstance().update(sesion, origen);
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
	protected void toMovimientosAlmacenDestino(Session sesion, String consecutivo, Long idDestino, Articulo articulo, TcManticArticulosDto umbrales, Double diferencia) throws Exception {
		Map<String, Object> params= null;
		try {
			//Afectar el almacen destino sumando los articulos que fueron agregados
			params=new HashMap<>();
			params.put("idAlmacen", idDestino);
			params.put("idArticulo", articulo.getIdArticulo());
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario== null)
			  this.toCreateInvetario(sesion, articulo, idDestino, true);
			else {
				inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()+ diferencia));
				inventario.setStock(Numero.toRedondearSat(inventario.getStock()- diferencia));
  			DaoFactory.getInstance().update(sesion, inventario);
			} // if
			TcManticAlmacenesArticulosDto origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findIdentically(TcManticAlmacenesArticulosDto.class, params);
			if(origen== null) 
				origen= this.toCreateAlmacenArticulo(sesion, articulo, idDestino, umbrales);
	
			// generar un registro en la bitacora de movimientos de los articulos 
			TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
			  consecutivo, // String consecutivo, 
				4L, // Long idTipoMovimiento, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				idDestino, // Long idAlmacen, 
				-1L, // Long idMovimiento, 
				diferencia, // Double cantidad, 
				articulo.getIdArticulo(), // Long idArticulo, 
				origen.getStock(), // Double stock, 
				Numero.toRedondearSat(origen.getStock()+ diferencia), // Double calculo
				null // String observaciones
		  );
			DaoFactory.getInstance().insert(sesion, movimiento);

			origen.setStock(Numero.toRedondearSat(origen.getStock()+ diferencia));
			DaoFactory.getInstance().update(sesion, origen);
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
	protected TcManticAlmacenesArticulosDto toCreateAlmacenArticulo(Session sesion, Articulo articulo, Long idAlmacen, TcManticArticulosDto umbrales) throws Exception {
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
		
	protected TcManticInventariosDto toCreateInvetario(Session sesion, Articulo articulo, Long idAlmacen, boolean inicial) throws Exception {
    TcManticInventariosDto regresar= new TcManticInventariosDto(
			JsfBase.getIdUsuario(), // Long idUsuario, 
			idAlmacen, // Long idAlmacen, 
			0D, // Double entradas, 
			-1L, // Long idInventario, 
			articulo.getIdArticulo(), // Long idArticulo, 
			inicial? articulo.getCantidad(): articulo.getCantidad()* -1D, // Double inicial, 
			inicial? articulo.getCantidad(): 0D, // articulo.getCantidad()* -1D, // Double stock, 
			articulo.getCantidad(), // Double salidas, 
			new Long(Fecha.getAnioActual()), // Long ejercicio, 
			1L // Long idAutomatico
		);
		DaoFactory.getInstance().insert(sesion, regresar);
		return regresar;
	}

}

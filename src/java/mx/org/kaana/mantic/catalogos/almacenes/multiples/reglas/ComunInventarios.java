package mx.org.kaana.mantic.catalogos.almacenes.multiples.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.reglas.IBaseTnx;
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
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasMultiplesBitacoraDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public abstract class ComunInventarios extends IBaseTnx {

	private static final Log LOG=LogFactory.getLog(ComunInventarios.class);
	
	protected List<Articulo> articulos;
  protected TcManticTransferenciasMultiplesBitacoraDto bitacora;
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
    articulo.setSolicitados(articulo.getCantidad());
		this.toMovimientosAlmacenOrigen(sesion, consecutivo, idAlmacen, articulo, umbrales, idTransferenciaEstatus);
		this.toMovimientosAlmacenDestino(sesion, consecutivo, idDestino, articulo, umbrales, articulo.getCantidad());
	}
	
//	protected void toAutorizarAlmacenOrigen(Session sesion, String consecutivo, Long idAlmacen, Articulo articulo, TcManticArticulosDto umbrales, Long idTransferenciaEstatus) throws Exception {
//    this.toAutorizarAlmacenOrigen(sesion, consecutivo, idAlmacen, articulo, umbrales, idTransferenciaEstatus, false);
//  }
  
	protected void toAutorizarAlmacenOrigen(Session sesion, String consecutivo, Long idAlmacen, Articulo articulo, TcManticArticulosDto umbrales, Long idTransferenciaEstatus) throws Exception {
		Map<String, Object> params= null;
    Double stock              = 0D;
		try {
			params=new HashMap<>();
			//Afectar el almacen original restando los articulos que fueron extraidos
			params.put("idAlmacen", idAlmacen);
			params.put("idArticulo", articulo.getIdArticulo());
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario== null) {
        stock     = 0D;
			  inventario= this.toCreateInvetario(sesion, articulo, idAlmacen, false);
      } // if
			else {
        stock     = inventario.getStock();
   			// si el estatus es el de cancelar entonces hacer los movimientos inversos al traspaso
  			if(idTransferenciaEstatus.intValue()== 4) 
	  			inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()- articulo.getSolicitados()));
				else 
   			  inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()+ (articulo.getDescuentos()- articulo.getSolicitados())));
				// ajustar el stock del inventrio del almacen origen con el nuevo valor
	  		inventario.setStock(Numero.toRedondearSat(Math.abs(inventario.getInicial()+ inventario.getEntradas())- inventario.getSalidas()));
  			DaoFactory.getInstance().update(sesion, inventario);
			} // if
      
			TcManticAlmacenesArticulosDto origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findIdentically(TcManticAlmacenesArticulosDto.class, params);
			if(origen== null) 
				origen= this.toCreateAlmacenArticulo(sesion, articulo, idAlmacen, umbrales);
      origen.setStock(inventario.getStock());
			DaoFactory.getInstance().update(sesion, origen);

			// generar un registro en la bitacora de movimientos de los articulos 
			TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
			  consecutivo, // String consecutivo, 
				4L, // Long idTipoMovimiento, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				idAlmacen, // Long idAlmacen, 
				-1L, // Long idMovimiento, 
				articulo.getCantidad(), // Double cantidad, 
				articulo.getIdArticulo(), // Long idArticulo, 
				stock, // Double stock, 
				inventario.getStock(), // Double calculo
				null // String observaciones
		  );
			if(idTransferenciaEstatus.intValue()== 4) {
        movimiento.setCantidad(articulo.getCantidad());
				movimiento.setObservaciones("SE CANCELO LA TRANSFERENCIA");
			} // if
			DaoFactory.getInstance().insert(sesion, movimiento);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
  
	protected void toMovimientosAlmacenOrigen(Session sesion, String consecutivo, Long idAlmacen, Articulo articulo, TcManticArticulosDto umbrales, Long idTransferenciaEstatus) throws Exception {
		Map<String, Object> params= null;
    Double stock              = 0D; 
		try {
			params=new HashMap<>();
			//Afectar el almacen original restando los articulos que fueron extraidos
			params.put("idAlmacen", idAlmacen);
			params.put("idArticulo", articulo.getIdArticulo());
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario== null) {
        stock= 0D;
			  inventario= this.toCreateInvetario(sesion, articulo, idAlmacen, false);
      } // if
			else {
        stock= inventario.getStock();
   			// si el estatus es el de cancelar entonces hacer los movimientos inversos al traspaso
  			if(idTransferenciaEstatus.intValue()== 4) 
	  			inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()- articulo.getSolicitados()));
				else 
	  			inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()+ articulo.getCantidad()));
				// ajustar el stock del inventrio del almacen origen con el nuevo valor
	  		inventario.setStock(Numero.toRedondearSat(Math.abs(inventario.getInicial()+ inventario.getEntradas())- inventario.getSalidas()));
  			DaoFactory.getInstance().update(sesion, inventario);
			} // if
      
			TcManticAlmacenesArticulosDto origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findIdentically(TcManticAlmacenesArticulosDto.class, params);
			if(origen== null) 
				origen= this.toCreateAlmacenArticulo(sesion, articulo, idAlmacen, umbrales);
      origen.setStock(inventario.getStock());
			DaoFactory.getInstance().update(sesion, origen);

			// generar un registro en la bitacora de movimientos de los articulos 
			TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
			  consecutivo, // String consecutivo, 
				4L, // Long idTipoMovimiento, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				idAlmacen, // Long idAlmacen, 
				-1L, // Long idMovimiento, 
				articulo.getCantidad()* -1, // Double cantidad, 
				articulo.getIdArticulo(), // Long idArticulo, 
				stock, // Double stock, 
				inventario.getStock(), // Double calculo
				null // String observaciones
		  );
			if(idTransferenciaEstatus.intValue()== 4) {
        movimiento.setCantidad(articulo.getCantidad());
				movimiento.setObservaciones("SE CANCELO LA TRANSFERENCIA");
			} // if
			DaoFactory.getInstance().insert(sesion, movimiento);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
	
	protected void toMovimientosAlmacenDestino(Session sesion, String consecutivo, Long idDestino, Articulo articulo, TcManticArticulosDto umbrales, Double diferencia) throws Exception {
		Map<String, Object> params= null;
    Double stock              = 0D;
		try {
			//Afectar el almacen destino sumando los articulos que fueron agregados
			params=new HashMap<>();
			params.put("idAlmacen", idDestino);
			params.put("idArticulo", articulo.getIdArticulo());
			params.put("consecutivo", consecutivo);
			params.put("idTipoMovimiento", 4);
			Value existe= (Value)DaoFactory.getInstance().toField(sesion, "TcManticMovimientosDto", "existe", params, "consecutivo");
			if(existe== null) {
				TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(sesion, TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
				if(inventario== null) {
          stock     = 0D;
					inventario= this.toCreateInvetario(sesion, articulo, idDestino, true);
        } // if
				else {
          stock= inventario.getStock();
					inventario.setEntradas(Numero.toRedondearSat(inventario.getEntradas()+ diferencia));
					inventario.setStock(Numero.toRedondearSat(Math.abs(inventario.getInicial()+ inventario.getEntradas())- inventario.getSalidas()));
					DaoFactory.getInstance().update(sesion, inventario);
				} // if
        
        // afectar el almacen con la cantidad que se tiene en el stock
				TcManticAlmacenesArticulosDto origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findIdentically(sesion, TcManticAlmacenesArticulosDto.class, params);
				if(origen== null) 
					origen= this.toCreateAlmacenArticulo(sesion, articulo, idDestino, umbrales);
        origen.setStock(inventario.getStock());
				DaoFactory.getInstance().update(sesion, origen);
        
				// generar un registro en la bitacora de movimientos de los articulos 
				TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
					consecutivo, // String consecutivo, 
					4L, // Long idTipoMovimiento, 
					JsfBase.getIdUsuario(), // Long idUsuario, 
					idDestino, // Long idAlmacen, 
					-1L, // Long idMovimiento, 
					diferencia, // Double cantidad, 
					articulo.getIdArticulo(), // Long idArticulo, 
					stock, // Double stock, 
					inventario.getStock(), // Double calculo
					null // String observaciones
				);
				DaoFactory.getInstance().insert(sesion, movimiento);

				Long idEmpresa= JsfBase.getAutentifica().getEmpresa().getIdEmpresa();
				params.put("idAlmacen", idDestino);
				Value empresa= DaoFactory.getInstance().toField(sesion, "TcManticAlmacenesDto", "empresa", params, "idEmpresa");
				if(empresa.getData()!= null)
					idEmpresa= empresa.toLong();
				// QUITAR DE LAS VENTAS PERDIDAS LOS ARTICULOS QUE FUERON YA SURTIDOS EN EL ALMACEN
				params.put("idArticulo", articulo.getIdArticulo());
				params.put("idEmpresa", idEmpresa);
				params.put("observaciones", "ESTE ARTICULO FUE SURTIDO CON NO. TRANSFERENCIA "+ consecutivo+ " EL DIA "+ Fecha.getHoyExtendido());
				DaoFactory.getInstance().updateAll(sesion, TcManticFaltantesDto.class, params);
			} // if
			else 
				LOG.info("Verificar porque se esta invocando dos veces cuando solo deberia de ser una sola vez !");
		} // try
		catch (Exception e) {
			throw e;
		} // catch
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
			inicial? articulo.getCantidad(): 0D, // Double entradas, 
			-1L, // Long idInventario, 
			articulo.getIdArticulo(), // Long idArticulo, 
			0D, // Double inicial, 
			// inicial? 0D: articulo.getCantidad()* -1D, // Double inicial, 
			inicial? articulo.getCantidad(): articulo.getCantidad()* -1D, // articulo.getCantidad()* -1D, // Double stock, 
			inicial? 0D: articulo.getCantidad(), // Double salidas, 
			new Long(Fecha.getAnioActual()), // Long ejercicio, 
			1L // Long idAutomatico
		);
		DaoFactory.getInstance().insert(sesion, regresar);
		return regresar;
	}

}

package mx.org.kaana.mantic.inventarios.devoluciones.reglas;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
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
 
	private TcManticDevolucionesDto orden;	
	private List<Articulo> articulos;
	private String messageError;
	private TcManticDevolucionesBitacoraDto bitacora;
	private boolean aplicar;

	public Transaccion(TcManticDevolucionesDto orden, TcManticDevolucionesBitacoraDto bitacora) {
		this(orden);
		this.bitacora= bitacora;
	}
	
	public Transaccion(TcManticDevolucionesDto orden) {
		this(orden, new ArrayList<Articulo>(), true);
	}

	public Transaccion(TcManticDevolucionesDto orden, List<Articulo> articulos, boolean aplicar) {
		this.orden    = orden;		
		this.articulos= articulos;
		this.aplicar  = aplicar;
	} 

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		TcManticDevolucionesBitacoraDto bitacoraNota= null;
		Map<String, Object> params= null;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la devolución de entrada.");
			if(this.orden!= null) {
				params= new HashMap<>();
				params.put("idNotaEntrada", this.orden.getIdNotaEntrada());
				params.put("idDevolucion", this.orden.getIdDevolucion());
			} // if
			switch(accion) {
				case AGREGAR:
					Siguiente consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(consecutivo.getConsecutivo());
					this.orden.setOrden(consecutivo.getOrden());
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
  				this.orden.setIdDevolucionEstatus(1L);
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					bitacoraNota= new TcManticDevolucionesBitacoraDto(this.orden.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					this.toFillArticulos(sesion);
					break;				
				case MODIFICAR:
  				if(this.aplicar) {
						this.orden.setIdDevolucionEstatus(3L);
  					bitacoraNota= new TcManticDevolucionesBitacoraDto(this.orden.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
	  				regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					} // if	
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					this.toRemoveOrdenDetalle(sesion);
					this.toFillArticulos(sesion);
					break;
				case ELIMINAR:
					this.toRemoveOrdenDetalle(sesion);
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticDevolucionesDetallesDto.class, params)>= 1L;
					regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					this.orden.setIdDevolucionEstatus(2L);
					bitacoraNota= new TcManticDevolucionesBitacoraDto(this.orden.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.orden.setIdDevolucionEstatus(this.bitacora.getIdDevolucionEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						if(this.bitacora.getIdDevolucionEstatus().equals(2L)) {
							this.toRemoveOrdenDetalle(sesion);
							regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticDevolucionesDetallesDto.class, params)>= 1L;
							regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
							this.orden.setIdDevolucionEstatus(2L);
							bitacoraNota= new TcManticDevolucionesBitacoraDto(this.orden.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
							regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
						} // if	
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
		LOG.info("Se generó de forma correcta la devolución de entrada: "+ this.orden.getConsecutivo());
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
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticDevolucionesDto", "siguiente", params, "siguiente");
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
	
	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaDevolucionesDto", "registro", this.orden.toMap());
  	TcManticNotasEntradasDto nota= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, this.orden.getIdNotaEntrada());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0) {
				this.toCancelOrdenDetalle(sesion, item);
				this.toCancelAlmacenes(sesion, this.orden.getConsecutivo(), this.orden.getIdNotaEntrada(), item, nota.getIdAlmacen());
				DaoFactory.getInstance().delete(sesion, item);
			} // if
		for (Articulo articulo: this.articulos) {
			TcManticDevolucionesDetallesDto item= articulo.toDevolucionDetalle();
			if(item.getCantidad()> 0) {
				item.setIdDevolucion(this.orden.getIdDevolucion());
				if(DaoFactory.getInstance().findIdentically(sesion, TcManticDevolucionesDetallesDto.class, item.toMap())== null) {
					this.toAffectOrdenDetalle(sesion, articulo);
					if(item.isValid())
						DaoFactory.getInstance().update(sesion, item);
					else
						DaoFactory.getInstance().insert(sesion, item);
					this.toAffectAlmacenes(sesion, this.orden.getConsecutivo(), this.orden.getIdNotaEntrada(), articulo, nota.getIdAlmacen());
				} // if
		  } // if
		} // for
	}

	private void toCancelOrdenDetalle(Session sesion, Articulo articulo) throws Exception {
		if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
			TcManticNotasDetallesDto detalle= (TcManticNotasDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticNotasDetallesDto.class, articulo.getIdOrdenDetalle());
      detalle.setCantidades(detalle.getCantidades()+ articulo.getCantidad());
			DaoFactory.getInstance().update(sesion, detalle);
		} // if
	}
	
	private void toAffectOrdenDetalle(Session sesion, Articulo articulo) throws Exception {
		if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
			TcManticNotasDetallesDto detalle= (TcManticNotasDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticNotasDetallesDto.class, articulo.getIdOrdenDetalle());
      detalle.setCantidades(detalle.getCantidades()- articulo.getCantidad());
			DaoFactory.getInstance().update(sesion, detalle);
		} // if
	}
	
	private void toRemoveOrdenDetalle(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaDevolucionesDto", "registro", this.orden.toMap());
		TcManticNotasEntradasDto nota= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, this.orden.getIdNotaEntrada());
		for (Articulo articulo: todos) {
			if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
				TcManticNotasDetallesDto detalle= (TcManticNotasDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticNotasDetallesDto.class, articulo.getIdOrdenDetalle());
				detalle.setCantidades(detalle.getCantidades()+ articulo.getCantidad());
				DaoFactory.getInstance().update(sesion, detalle);
				this.toCancelAlmacenes(sesion, this.orden.getConsecutivo(), this.orden.getIdNotaEntrada(), articulo, nota.getIdAlmacen());
			} // if
		} // for
	}
	
	protected void toCancelAlmacenes(Session sesion, String consecutivo, Long idNotaEntrada, Articulo item, Long idAlmacen) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idArticulo", item.getIdArticulo());
			params.put("idAlmacen", idAlmacen);
			params.put("idNotaEntrada", idNotaEntrada);
			
			double stock= 0D;
			// registar el stock de los articulos en la bitacora de articulo 
			TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
			// afectar el inventario general de articulos dentro del almacen
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().findFirst(sesion, TcManticInventariosDto.class, "inventario", params);
			if(inventario== null)
				DaoFactory.getInstance().insert(sesion, new TcManticInventariosDto(JsfBase.getIdUsuario(), idAlmacen, item.getCantidad(), -1L, item.getIdArticulo(), 0D, 0D, item.getCantidad(), new Long(Calendar.getInstance().get(Calendar.YEAR)), 1L));
			else {
				inventario.setEntradas(inventario.getEntradas()+ item.getCantidad());
				inventario.setStock(inventario.getStock()+ item.getCantidad());
				DaoFactory.getInstance().update(sesion, inventario);
			} // else
			// afectar el almacen de articulos proque se cancelo la devolucion
			TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class,  params, "ubicacion");
			if(ubicacion!= null) {
				stock= ubicacion.getStock();
				ubicacion.setStock(ubicacion.getStock()+ item.getCantidad());
				DaoFactory.getInstance().update(sesion, ubicacion);
			} // if
			else {
				LOG.warn("VERIFICAR PORQUE RAZON NO ESTA EL ARTICULO["+ item.getIdArticulo()+ "] EN EL ALMACEN["+ idAlmacen+ "] SI ES QUE EN LA NOTA DE ENTRADA SE REGISTRO SU INGRESO.");
			} // else
			global.setStock(global.getStock()+ item.getCantidad());
			DaoFactory.getInstance().update(sesion, global);
			
			// generar un registro en la bitacora de movimientos de los articulos 
			TcManticMovimientosDto entrada= new TcManticMovimientosDto(
			  consecutivo, // String consecutivo, 
				3L, // Long idTipoMovimiento, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				idAlmacen, // Long idAlmacen, 
				-1L, // Long idMovimiento, 
				item.getCantidad(), // Double cantidad, 
				item.getIdArticulo(), // Long idArticulo, 
				stock, // Double stock, 
				Numero.toRedondearSat(stock+ item.getCantidad()), // Double calculo
				"SE CANCELO LA DEVOLUCIÓN DEL ARTICULO" // String observaciones
		  );
			DaoFactory.getInstance().insert(sesion, entrada);

		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
	
	protected void toAffectAlmacenes(Session sesion, String consecutivo, Long idNotaEntrada, Articulo item, Long idAlmacen) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idArticulo", item.getIdArticulo());
			params.put("idAlmacen", idAlmacen);
			params.put("idNotaEntrada", idNotaEntrada);
			
			// registar el cambio de precios en la bitacora de articulo 
			TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
			
			// afectar el inventario general de articulos dentro del almacen
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().findFirst(sesion, TcManticInventariosDto.class, "inventario", params);
			if(inventario== null)
				DaoFactory.getInstance().insert(sesion, new TcManticInventariosDto(JsfBase.getIdUsuario(), idAlmacen, item.getCantidad(), -1L, item.getIdArticulo(), 0D, 0D, item.getCantidad()* -1L, new Long(Calendar.getInstance().get(Calendar.YEAR)), 1L));
			else {
				inventario.setEntradas(inventario.getEntradas()- item.getCantidad());
				inventario.setStock(inventario.getStock()- item.getCantidad());
				DaoFactory.getInstance().update(sesion, inventario);
			} // else
			
			double stock= 0D;
			// afectar el almacen de articulos con las devoluciones realizadas
			TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class,  params, "ubicacion");
			if(ubicacion!= null) {
				stock= ubicacion.getStock();
				ubicacion.setStock(ubicacion.getStock()- item.getCantidad());
				DaoFactory.getInstance().update(sesion, ubicacion);
			} // if
			else {
				LOG.warn("VERIFICAR PORQUE RAZON NO ESTA EL ARTICULO ["+ item.getIdArticulo()+ "] EN EL ALMACEN ["+ idAlmacen+ "] SI ES QUE EN LA NOTA DE ENTRADA SE REGISTRO SU INGRESO.");
			} // else
			// afectar los precios del catalogo de articulos
			if(this.aplicar) {
  			TcManticArticulosBitacoraDto movimiento= new TcManticArticulosBitacoraDto(global.getIva(), JsfBase.getIdUsuario(), global.getMayoreo(), -1L, global.getMenudeo(), global.getCantidad()* -1L, global.getIdArticulo(), idNotaEntrada, global.getMedioMayoreo(), global.getPrecio(), global.getLimiteMedioMayoreo(), global.getLimiteMayoreo(), global.getDescuento(), global.getExtra());
	  		DaoFactory.getInstance().insert(sesion, movimiento);
				TcManticArticulosBitacoraDto ultimo= (TcManticArticulosBitacoraDto)DaoFactory.getInstance().findFirst(sesion, TcManticArticulosBitacoraDto.class, "ultimo", params);
				if(ultimo!= null) {
	   			Descuentos descuentos= new Descuentos(ultimo.getCosto(), ultimo.getDescuento());
					
		  		// aplicar el descuento sobre el valor del costo del articulo para afectar el catalogo
  			  global.setPrecio(Numero.toRedondearSat(descuentos.getFactor()== 0D? descuentos.getImporte(): descuentos.toImporte()));
					global.setPrecio(ultimo.getCosto());
					global.setMenudeo(Numero.toAjustarDecimales(ultimo.getMenudeo()));
					global.setMedioMayoreo(Numero.toAjustarDecimales(ultimo.getMedioMayoreo()));
					global.setMayoreo(Numero.toAjustarDecimales(ultimo.getMayoreo()));
					global.setDescuento(ultimo.getDescuento());
					global.setExtra(ultimo.getExtras());
					global.setActualizado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				} // if	
			} // if
			global.setStock(global.getStock()- item.getCantidad());
			DaoFactory.getInstance().update(sesion, global);
			
			// generar un registro en la bitacora de movimientos de los articulos 
			TcManticMovimientosDto entrada= new TcManticMovimientosDto(
			  consecutivo, // String consecutivo, 
				3L, // Long idTipoMovimiento, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				idAlmacen, // Long idAlmacen, 
				-1L, // Long idMovimiento, 
				item.getCantidad()* -1, // Double cantidad, 
				item.getIdArticulo(), // Long idArticulo, 
				stock, // Double stock, 
				Numero.toRedondearSat(stock- item.getCantidad()), // Double calculo
				null // String observaciones
		  );
			DaoFactory.getInstance().insert(sesion, entrada);

		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}	
	
} 
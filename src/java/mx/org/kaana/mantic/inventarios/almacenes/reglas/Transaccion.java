package mx.org.kaana.mantic.inventarios.almacenes.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.db.dto.TcManticArticulosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosCodigosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticPedidosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticPedidosDto;
import mx.org.kaana.mantic.inventarios.almacenes.beans.TiposVentas;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
 
	private Long idArticulo;
	private Long idPedido;
	private Long idRedondear;
	private Double cantidad;
	private Double precio;
	private String descuento;
	private String extra;
	private String sat;
	private List<TiposVentas> articulos;
	protected String messageError;		
	private UISelectEntity almacen;

	public Transaccion() {
		this(-1L, -1L, 0D);
	} // Transaccion
	
	public Transaccion(Long idArticulo, Long idPedido, Double cantidad) {
		this.idArticulo= idArticulo;
		this.idPedido  = idPedido;
		this.cantidad  = cantidad;
	} // Transaccion
	
	public Transaccion(Long idArticulo, Long idRedondear) {
		this.idArticulo= idArticulo;
		this.idRedondear= idRedondear;
	}

	public Transaccion(Long idArticulo, String sat) {
		this.idArticulo=idArticulo;
		this.sat=sat;
	}
	
	public Transaccion(Long idArticulo, Double precio, String descuento, String extra, List<TiposVentas> articulos, String sat) {
		this.idArticulo= idArticulo;
		this.precio    = precio;
		this.descuento = descuento;
		this.extra     = extra;
		this.articulos = articulos;
		this.sat       = sat;
	} // Transaccion

	public Transaccion(UISelectEntity almacen) {
	  this.almacen= almacen;
	}
	
	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		TcManticPedidosDto pedido         = null;		
		TcManticPedidosDetallesDto detalle= null;
		TcManticArticulosDto articulo     = null;
		boolean regresar                  = false;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el precio del tipo de venta del articulo.");
			switch(accion) {
				case MODIFICAR:
					articulo= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idArticulo);
					articulo.setPrecio(this.precio);
					articulo.setDescuento(this.descuento);
					articulo.setExtra(this.extra);
					articulo.setMenudeo(this.articulos.get(0).getPrecio());
					articulo.setLimiteMedioMayoreo(this.articulos.get(0).getLimite());
					articulo.setMedioMayoreo(this.articulos.get(1).getPrecio());
					articulo.setLimiteMayoreo(this.articulos.get(1).getLimite());
					articulo.setMayoreo(this.articulos.get(2).getPrecio());
					articulo.setActualizado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				  regresar= DaoFactory.getInstance().update(sesion, articulo)>= 1L;
					TcManticArticulosBitacoraDto movimiento= new TcManticArticulosBitacoraDto(articulo.getIva(), JsfBase.getIdUsuario(), articulo.getMayoreo(), -1L, articulo.getMenudeo(), articulo.getCantidad(), articulo.getIdArticulo(), null, articulo.getMedioMayoreo(), this.precio, articulo.getLimiteMedioMayoreo(), articulo.getLimiteMayoreo(), articulo.getDescuento(), articulo.getExtra());
					regresar= DaoFactory.getInstance().insert(sesion, movimiento)>= 1L;
					break;
				case REGISTRAR:
					pedido= (TcManticPedidosDto) DaoFactory.getInstance().findById(sesion, TcManticPedidosDto.class, this.idPedido);					
					detalle= toArticuloDetalle(sesion);
					detalle.setIdPedido(this.idPedido);
					if(DaoFactory.getInstance().insert(sesion, detalle)>= 1L){
						pedido.setIdPedidoEstatus(4L);
						pedido.setSubTotal(pedido.getSubTotal()+ detalle.getSubTotal());
						pedido.setTotal(pedido.getTotal()+ detalle.getImporte());
						pedido.setDescuentos(pedido.getDescuentos()+ detalle.getDescuentos());
						pedido.setImpuestos(pedido.getImpuestos()+ detalle.getImpuestos());						
						regresar= DaoFactory.getInstance().update(sesion, pedido)>= 1L;
					} // if
					break;
				case PROCESAR:
					articulo= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idArticulo);
					articulo.setIdRedondear(this.idRedondear);
					articulo.setActualizado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					regresar= DaoFactory.getInstance().update(sesion, articulo)>= 1L;
					break;
				case COMPLEMENTAR:
					articulo= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idArticulo);
					articulo.setSat(this.sat);
					articulo.setActualizado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					regresar= DaoFactory.getInstance().update(sesion, articulo)>= 1L;
					break;
				case ASIGNAR:
					this.sat= this.sat.toUpperCase().replaceAll(Constantes.CLEAN_ART, "").trim();
					//Entity auxiliar= this.toFindCodigoAuxiliar(sesion, this.sat);
					//if(auxiliar== null || auxiliar.isEmpty()) {
						TcManticArticulosCodigosDto codigos= new TcManticArticulosCodigosDto(
							this.sat, // String codigo, 
							null, // Long idProveedor, 
							JsfBase.getIdUsuario(), // Long idUsuario, 
							2L, // Long idPrincipal, 
							null, // String observaciones, 
							-1L, // Long idArticuloCodigo, 
							this.toSiguiente(sesion), // Long orden, 
							this.idArticulo // Long idArticulo
						);
						regresar= DaoFactory.getInstance().insert(sesion, codigos)>= 1L;
					//} // if
					//else {
						//this.messageError= "El código ya lo tiene asignado el articulo !\n ["+ auxiliar.toString("codigo")+ " "+ auxiliar.toString("nombre")+ " como "+ auxiliar.toString("principal");
						//regresar= false;
					//} // else	
					break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, TcManticArticulosCodigosDto.class, this.idArticulo)>= 1L;
					break;
				case MOVIMIENTOS:
					TcManticAlmacenesArticulosDto almacenArticulo= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findById(TcManticAlmacenesArticulosDto.class, this.almacen.getKey());
					if(almacenArticulo!= null) {
						almacenArticulo.setMinimo(this.almacen.toDouble("min"));
						almacenArticulo.setMaximo(this.almacen.toDouble("max"));
						regresar= DaoFactory.getInstance().update(sesion, almacenArticulo)> 0;
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
		return regresar;
	}	// ejecutar	
	
	protected Double toCalculateCostoPorCantidad(Session sesion, Long idArticulo, Double cantidad) {
		TcManticArticulosDto validate= null;
		Double regresar                 = 0D;
		try {			
			validate= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, idArticulo);
			if(validate!= null) {				
				regresar= validate.getMenudeo();				
				if (cantidad >= validate.getLimiteMayoreo())
					regresar=validate.getMayoreo();						
				else if(cantidad >= validate.getLimiteMedioMayoreo() && cantidad< validate.getLimiteMayoreo())
					regresar=validate.getMedioMayoreo();																		
			} // if 
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // toCalculateCostoPorCantidad
	
	protected TcManticPedidosDetallesDto toArticuloDetalle(Session sesion) throws Exception{
		return toArticuloDetalle(sesion, this.idArticulo, this.cantidad);
	} // toArticuloDetalle
	
	protected TcManticPedidosDetallesDto toArticuloDetalle(Session sesion, Long idArticulo, Double cantidad) throws Exception{
		TcManticPedidosDetallesDto regresar= null;
		ArticuloVenta articuloPedido       = null;
		Map<String, Object>params          = null;		
		try {
			params= new HashMap<>();
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
			params.put("idArticulo", idArticulo);
			params.put("codigo", "WXYZ");
			articuloPedido= (ArticuloVenta) DaoFactory.getInstance().toEntity(sesion, ArticuloVenta.class, "VistaOrdenesComprasDto", "porNombre", params);					
			articuloPedido.setCantidad(cantidad);
			articuloPedido.setCosto(toCalculateCostoPorCantidad(sesion, idArticulo, cantidad));
			articuloPedido.setSinIva(true);
			articuloPedido.toCalculate();
			regresar= articuloPedido.toPedidoDetalle();					
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toArticuloDetalle

	private Entity toFindCodigoAuxiliar(Session sesion, String codigo) {
		Entity regresar= null;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("codigo", codigo);
  		params.put(Constantes.SQL_CONDICION, " tc_mantic_articulos_codigos.id_articulo!= "+ this.idArticulo);
			regresar= (Entity)DaoFactory.getInstance().toEntity(sesion, "VistaArticulosDto", "existeCodigo", params);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toFindCodigoAuxiliar

	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idArticulo", this.idArticulo);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticArticulosCodigosDto", "siguiente", params, "siguiente");
			if(next!= null && next.getData()!= null)
			  regresar= next.toLong();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente
	
} 

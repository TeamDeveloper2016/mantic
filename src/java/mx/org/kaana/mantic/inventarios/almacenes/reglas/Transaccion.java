package mx.org.kaana.mantic.inventarios.almacenes.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticArticulosBitacoraDto;
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
	private Double cantidad;
	private Double precio;
	private String descuento;
	private String extra;
	private List<TiposVentas> articulos;
	private String messageError;		

	public Transaccion(Long idArticulo, Long idPedido, Double cantidad) {
		this.idArticulo= idArticulo;
		this.idPedido  = idPedido;
		this.cantidad  = cantidad;
	} // Transaccion
	
	public Transaccion(Long idArticulo, Double precio, String descuento, String extra, List<TiposVentas> articulos) {
		this.idArticulo= idArticulo;
		this.precio    = precio;
		this.descuento = descuento;
		this.extra     = extra;
		this.articulos = articulos;
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		TcManticPedidosDto pedido         = null;
		Articulo articuloPedido           = null;
		TcManticPedidosDetallesDto detalle= null;
		boolean regresar                  = false;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el precio del tipo de venta del articulo.");
			switch(accion) {
				case MODIFICAR:
					TcManticArticulosDto articulo= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idArticulo);
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
					articuloPedido= (Articulo) DaoFactory.getInstance().toEntity(sesion, Articulo.class, "TcManticArticulosDto", "row", Variables.toMap("condicion~".concat("id_articulo=").concat(this.idArticulo.toString())));					
					articuloPedido.setCantidad(this.cantidad);
					articuloPedido.setCosto(toCalculateCostoPorCantidad(sesion));
					articuloPedido.toCalculate(false, 0);
					detalle= articuloPedido.toPedidoDetalle();
					detalle.setIdPedido(this.idPedido);
					if(DaoFactory.getInstance().insert(sesion, detalle)>= 1L){
						pedido.setIdPedidoEstatus(4L);
						pedido.setSubTotal(pedido.getSubTotal()+ detalle.getSubTotal());
						pedido.setTotal(pedido.getTotal()+ detalle.getImporte());
						regresar= DaoFactory.getInstance().update(sesion, pedido)>= 1L;
					} // if
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("<br/>")+ e.getMessage());
		} // catch		
		return regresar;
	}	// ejecutar

	private Double toCalculateCostoPorCantidad(Session sesion) {
		TcManticArticulosDto validate= null;
		Double regresar                 = 0D;
		try {			
			validate= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, this.idArticulo);
			if(validate!= null) {				
				regresar= validate.getMenudeo();				
				if (this.cantidad>= validate.getLimiteMayoreo())
					regresar=validate.getMayoreo();						
				else if(this.cantidad>= validate.getLimiteMedioMayoreo() && this.cantidad< validate.getLimiteMayoreo())
					regresar=validate.getMedioMayoreo();																		
			} // if 
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // toCalculateCostoPorCantidad
} 
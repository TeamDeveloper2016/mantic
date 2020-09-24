package mx.org.kaana.mantic.ventas.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AdminTickets extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID= 8594649943986572245L;
	private static final Log LOG              = LogFactory.getLog(AdminTickets.class);

	private TicketVenta orden;

	public AdminTickets(TicketVenta orden) throws Exception {
		this(orden, true);
	}
	
	public AdminTickets(TicketVenta orden, boolean loadDefault) throws Exception {
		boolean oldPrecios= true;
		this.orden        = orden;
		if(this.orden.isValid()) {
			oldPrecios= orden.getIdVentaEstatus()== 3L || orden.getIdVentaEstatus()== 6L || orden.getIdVentaEstatus()== 8L || orden.getIdVentaEstatus()== 12L || 
									orden.getIdVentaEstatus()== 14L || orden.getIdVentaEstatus()== 13L || orden.getIdVentaEstatus()== 15L || orden.getIdVentaEstatus()== 16L ||
									orden.getIdVentaEstatus()== 18L || orden.getIdVentaEstatus()== 19L;
  	  this.setArticulos((List<ArticuloVenta>)DaoFactory.getInstance().toEntitySet(ArticuloVenta.class, "VistaTcManticVentasDetallesDto", oldPrecios? "facturacion": "detalle", orden.toMap()));
      this.orden.setIkAlmacen(new UISelectEntity(new Entity(this.orden.getIdAlmacen())));
      this.orden.setIkProveedor(new UISelectEntity(new Entity(this.orden.getIdCliente())));
			this.orden.setIdServicio(toServicio());
      /* RECUPERAR SI ES UN CLIENTE CON PRECIO ESPECIAL Y CALCULAR SU COSTO Y SU FACTOR POR CADA ARTICULO */
			if(!oldPrecios)
			  this.validatePrecioArticulo();
		}	// if
		else	{
		  this.setArticulos(new ArrayList<>());
			this.orden.setConsecutivo(1L);
			this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
			this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.orden.setIdAlmacen(JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
		} // else	
		if(loadDefault)
			this.getArticulos().add(new ArticuloVenta(-1L));
		this.setIdSinIva(1L);
		this.toCalculate();
		this.cleanPrecioDescuentoArticulo();
	}

	@Override
	public Long getIdAlmacen() {
		return this.orden.getIdAlmacen();
	}

	public Long getIdCliente() {
		return this.orden.getIdCliente();
	}
	
	@Override
	public IBaseDto getOrden() {
		return orden;
	}

	@Override
	public void setOrden(IBaseDto orden) {
		this.orden= (TicketVenta)orden;
	}

	@Override
	public Double getTipoDeCambio() {
		return this.orden.getTipoDeCambio();
	}
	
	@Override
	public String getDescuento() {
		return this.orden.getDescuento();		
	}
	
	@Override
	public String getExtras() {
		return this.orden.getExtras();
	}
	
	@Override
	public Long getIdSinIva() {
		return this.orden.getIdSinIva();
	}
	
	@Override
	public void setIdSinIva(Long idSinIva) {
		this.orden.setIdSinIva(idSinIva);
	}

	@Override
	public Long getIdProveedor() {
		return -1L;
	}

	@Override
	public void setDescuento(String descuento){
		this.orden.setDescuento(descuento);
	}
	
	private Long toServicio(){
		Long regresar            = -1L;
		Entity servicio          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idVenta", this.orden.getIdVenta());
			servicio= (Entity) DaoFactory.getInstance().toEntity("TcManticServiciosDto", "venta", params);
			if(servicio!= null)
				regresar= servicio.getKey();
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // toServicio
	
	public void loadTipoMedioPago() throws Exception{
		Entity tipoMedioPago     = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idVenta", this.orden.getIdVenta());
			tipoMedioPago= (Entity) DaoFactory.getInstance().toEntity("TrManticVentaMedioPagoDto", "ticket", params);
			if(tipoMedioPago!= null){
				this.orden.setIdTipoMedioPago(tipoMedioPago.toLong("idTipoMedioPago"));
				if(!tipoMedioPago.toLong("idTipoMedioPago").equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
					if(tipoMedioPago.toLong("idBanco") != null)
						this.orden.setIdBanco(tipoMedioPago.toLong("idBanco"));
					else
						this.orden.setIdBanco(1L);
					if(tipoMedioPago.toString("referencia")!= null)
						this.orden.setReferencia(tipoMedioPago.toString("referencia"));
					else
						this.orden.setReferencia("");
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // loadTipoMedioPago
}

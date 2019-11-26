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
		List<ArticuloVenta> arts= null;
		this.orden  = orden;
		if(this.orden.isValid()) {
			arts=(List<ArticuloVenta>)DaoFactory.getInstance().toEntitySet(ArticuloVenta.class, "VistaTcManticVentasDetallesDto", "detalle", orden.toMap());
  	  this.setArticulos(arts);
      this.orden.setIkAlmacen(new UISelectEntity(new Entity(this.orden.getIdAlmacen())));
      this.orden.setIkProveedor(new UISelectEntity(new Entity(this.orden.getIdCliente())));
			this.orden.setIdServicio(toServicio());
			validatePrecioArticulo();
		}	// if
		else	{
			arts= new ArrayList<>();
		  this.setArticulos(arts);
			this.orden.setConsecutivo(1L);
			this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
			this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.orden.setIdAlmacen(JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
		} // else	
		if(loadDefault)
			this.getArticulos().add(new ArticuloVenta(-1L));
		this.setIdSinIva(1L);
		this.toCalculate();
		cleanPrecioDescuentoArticulo();
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
				if(!tipoMedioPago.getKey().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
					this.orden.setIdBanco(tipoMedioPago.toLong("idBanco"));
					this.orden.setReferencia(tipoMedioPago.toString("referencia"));
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

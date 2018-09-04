package mx.org.kaana.mantic.ventas.garantias.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AdminGarantia extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID= 8594649943986572245L;
	private static final Log LOG              = LogFactory.getLog(AdminGarantia.class);

	private TicketVenta orden;

	public AdminGarantia(TicketVenta orden) throws Exception {
		this(orden, EAccion.AGREGAR);
	}
	
	public AdminGarantia(TicketVenta orden, EAccion accion) throws Exception {
		this(orden, EAccion.AGREGAR, -1L);
	}
	
	public AdminGarantia(TicketVenta orden, EAccion accion, Long idGarantia) throws Exception {
		this(orden, true, accion, idGarantia);
	}
	
	public AdminGarantia(TicketVenta orden, boolean loadDefault) throws Exception {
		this(orden, loadDefault, EAccion.AGREGAR, -1L);
	}
	
	public AdminGarantia(TicketVenta orden, boolean loadDefault, EAccion accion, Long idGarantia) throws Exception {		
		this.orden  = orden;
		if(this.orden.isValid()) {
			switch(accion){
				case CONSULTAR:
					Map<String, Object>params= new HashMap<>();
					params.put("idGarantia", idGarantia);
					this.setArticulos((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaTcManticGarantiasArticulosDto", "detalleGarantia", params));
					break;
				default:
					this.setArticulos((List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaTcManticGarantiasArticulosDto", "detalle", orden.toMap()));
					validaArticulos();
					break;
			} // switch
      this.orden.setIkAlmacen(new UISelectEntity(new Entity(this.orden.getIdAlmacen())));
      this.orden.setIkProveedor(new UISelectEntity(new Entity(this.orden.getIdCliente())));
		}	// if
		else	{
		  this.setArticulos(new ArrayList<>());
			this.orden.setConsecutivo(1L);
			this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
			this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.orden.setIdAlmacen(JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
		} // else	
		if(loadDefault && !accion.equals(EAccion.CONSULTAR))
			this.getArticulos().add(new Articulo(-1L));
		this.toCalculate();
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
	
	public void validaArticulos(){
		List<Articulo>arts= null;
		int count         = 0;
		try {
			arts= this.getArticulos();
			for(Articulo art: arts){
				if(art.getCantidadGarantia().equals(0D))
					this.getArticulos().remove(count);
				count++;
			} // for			
		} // try
		catch (Exception e) {
			Error.mensaje(e);			
		} // catch		
	} // validaArticulos
}

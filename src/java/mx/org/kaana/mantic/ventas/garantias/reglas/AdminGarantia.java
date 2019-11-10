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

public final class AdminGarantia extends IAdminArticulos implements Serializable {

	private static final long serialVersionUID= 8594649943986572245L;
	private static final Log LOG              = LogFactory.getLog(AdminGarantia.class);

	private TicketVenta orden;
	private List<ArticuloVenta> articulosTerminada;
	private List<ArticuloVenta> articulosRecibida;

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
		List<ArticuloVenta> arts= null;
		this.orden= orden;
		if(this.orden.isValid()) {
			switch(accion){
				case CONSULTAR:
					Map<String, Object>params= new HashMap<>();
					params.put("idGarantia", idGarantia);
					arts= (List<ArticuloVenta>)DaoFactory.getInstance().toEntitySet(ArticuloVenta.class, "VistaTcManticGarantiasArticulosDto", "detalleGarantia", params); 
					this.setArticulos(arts);
					validatePrecioArticulo();
					break;				
				case ASIGNAR:
					arts= (List<ArticuloVenta>)DaoFactory.getInstance().toEntitySet(ArticuloVenta.class, "VistaTcManticGarantiasArticulosDto", "detalleCantidad", orden.toMap());
					this.setArticulos(arts);
					validatePrecioArticulo();
					validaArticulos();
					break;
				default:
					arts= (List<ArticuloVenta>)DaoFactory.getInstance().toEntitySet(ArticuloVenta.class, "VistaTcManticGarantiasArticulosDto", "detalle", orden.toMap());
					this.setArticulos(arts);
					validatePrecioArticulo();
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
			this.getArticulos().add(new ArticuloVenta(-1L));
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

	public List<ArticuloVenta> getArticulosTerminada() {
		return articulosTerminada;
	}

	public List<ArticuloVenta> getArticulosRecibida() {
		return articulosRecibida;
	}	
	
	@Override
	public void toCalculate(boolean modificado) {
		getTotales().reset();
		for (Articulo articulo: getArticulos()) {			
			articulo.setCostoLibre(true);
	    articulo.toCalculate(this.getIdSinIva().equals(1L), this.getTipoDeCambio());
		  articulo.setModificado(modificado);
			getTotales().addArticulo(articulo);
		} // for
		if(getArticulos().size()> 0)
			getTotales().removeUltimo(getArticulos().get(getArticulos().size()- 1));
		getTotales().removeTotal();
		this.setAjusteDeuda(getTotales().getTotal());
	} // toCalculate
	
	public void validaArticulos(){
		List<Articulo>arts= null;		
		try {
			arts= new ArrayList<>();
			arts.addAll(this.getArticulos());
			for(Articulo art: this.getArticulos()){
				if(art.getCantidadGarantia().equals(0D))					
					arts.remove(arts.indexOf(art));								
			} // for
			setArticulos(arts);
		} // try
		catch (Exception e) {
			Error.mensaje(e);			
		} // catch		
	} // validaArticulos	
	
	@Override
	public void toCalculateGarantia(boolean recibida) {				
		if(recibida){
			this.articulosRecibida = new ArrayList<>();
			toCalculateRecibida();
		} // if
		else{
			this.articulosTerminada= new ArrayList<>();
			toCalculateTerminada();
		} // else
	} // toCalculte
	
	public void toCalculateRecibida() {
		ArticuloVenta artVenta= null;
		getTotales().reset();
		for (Articulo articulo: getArticulos()) {
			artVenta= (ArticuloVenta) articulo;
			if(artVenta.isAplicar()){
				artVenta.toCalculate(true, this.getTipoDeCambio());
				artVenta.setModificado(false);
				getTotales().addArticulo(artVenta);
				this.articulosRecibida.add(artVenta);
			} // if
		} // for
		getTotales().removeUltimo(getArticulos().get(getArticulos().size()- 1));
		getTotales().removeTotal();
		this.setAjusteDeuda(getTotales().getTotal());
	} // toCalculte
	
	public void toCalculateTerminada() {
		ArticuloVenta artVenta= null;
		getTotales().reset();
		for (Articulo articulo: getArticulos()) {
			artVenta= (ArticuloVenta) articulo;
			if(!artVenta.isAplicar()){
				artVenta.toCalculate(true, this.getTipoDeCambio());
				artVenta.setModificado(false);
				getTotales().addArticulo(artVenta);
				this.articulosTerminada.add(artVenta);
			} // if
		} // for
		getTotales().removeUltimo(getArticulos().get(getArticulos().size()- 1));
		getTotales().removeTotal();
		this.setAjusteDeuda(getTotales().getTotal());
	} // toCalculte
}
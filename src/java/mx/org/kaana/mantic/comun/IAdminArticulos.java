package mx.org.kaana.mantic.comun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20/05/2018
 *@time 02:08:29 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class IAdminArticulos implements Serializable {

	private static final Log LOG=LogFactory.getLog(IAdminArticulos.class);
	private static final long serialVersionUID=506956550372353914L;
	
	private List<Articulo> articulos;
	private List<Articulo> filtrados;
	private Totales totales;

	public IAdminArticulos() throws Exception {
		this.filtrados= new ArrayList<>();
		this.totales  = new Totales();
	}

	public List<Articulo> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<? extends Articulo> articulos) {
		this.articulos= (List<Articulo>) articulos;
	}

	public List<Articulo> getFiltrados() {
		return filtrados;
	}

	public void setFiltrados(List<Articulo> filtrados) {
		this.filtrados=filtrados;
	}

	public Totales getTotales() {
		return totales;
	}

	public abstract Long getIdAlmacen();

	public abstract Long getIdProveedor();
	
	public abstract IBaseDto getOrden();
	
	public abstract void setOrden(IBaseDto orden);
	
	public abstract Double getTipoDeCambio();
	
	public abstract String getDescuento();
	
	public abstract void setDescuento(String descuento);
	
	public abstract String getExtras();
	
	public abstract Long getIdSinIva();
	
	public abstract void setIdSinIva(Long idSinIva);
	
	public void setAjusteDeuda(double deuda) {
	}
	
  public boolean add(Articulo articulo) throws Exception {
		if(this.articulos.indexOf(articulo)< 0) {
			this.toFillCodigo(articulo);
			if(this.articulos.size()<= 1)
		    this.articulos.add(0, articulo);
			else
		    this.articulos.add(this.articulos.size()- 1, articulo);
			return true;
		} // if
		else
		  throw new KajoolBaseException("El articulo ["+ articulo.getNombre()+ "] ya esta dentro de la lista seleccionada !");
	}

	private void toFillCodigo(Articulo articulo) throws Exception {
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idArticulo", articulo.getIdArticulo());
  		params.put("idProveedor", articulo.getIdProveedor());
			List<Entity> codigos= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaOrdenesComprasDto", "articulo", params);
			for (Entity codigo: codigos) {
				if(codigo.toInteger("idPrincipal")== 1L)
					articulo.setPropio(codigo.toString("codigo"));
				else
					articulo.setCodigo(codigo.toString("codigo"));
			} // for
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
	
	public void remove(Articulo seleccionado) {
		int index= this.articulos.indexOf(seleccionado);
		if(index>= 0) {
			this.toRemoveArticulo(index);
		  this.articulos.remove(seleccionado);
		} // if
	}

  public String toConsecutivo(String value) {
		return Fecha.getAnioActual()+ Cadena.rellenar(value, 6, '0', true);
	}	
		
	public void toRemoveArticulo(Integer index) {
		if(index>= 0 && index< this.articulos.size()) {
			Articulo articulo= this.articulos.get(index);
			this.totales.removeArticulo(articulo);
		} // if
	}
	
	public void toAddUltimo(Integer index) {
		this.toAddArticulo(index);
		this.totales.removeUltimo(this.articulos.get(this.articulos.size()- 1));
	}
	
	public void toAddArticulo(Integer index) {
		if(index>= 0 && index< this.articulos.size()) {
			Articulo articulo= this.articulos.get(index);
			articulo.toCalculate(this.getIdSinIva().equals(1L), this.getTipoDeCambio());
			articulo.setModificado(true);
			this.totales.addArticulo(articulo);
		} // if
	}
	
	public void toCalculate(Integer index) {
		if(index>= 0 && index< this.articulos.size()) {
			Articulo articulo= this.articulos.get(index);
			this.totales.removeArticulo(articulo);
			articulo.toCalculate(this.getIdSinIva().equals(1L), this.getTipoDeCambio());
			articulo.setModificado(true);
			this.totales.addArticulo(articulo);
		} // if
		this.totales.removeTotal();
		this.setAjusteDeuda(this.totales.getTotal());
	}
	
	public void toCalculate() {
		this.toCalculate(false);
	}
	
	public void toStartCalculate() {
		this.articulos.add(new Articulo(-1L));
		this.toCalculate(false);
	}
	
	public void toCalculate(boolean modificado) {
		this.totales.reset();
		for (Articulo articulo: this.articulos) {
	    articulo.toCalculate(this.getIdSinIva().equals(1L), this.getTipoDeCambio());
	    articulo.setModificado(modificado);
			this.totales.addArticulo(articulo);
		} // for
		this.totales.removeUltimo(this.articulos.get(this.articulos.size()- 1));
		this.totales.removeTotal();
		this.setAjusteDeuda(this.totales.getTotal());
	}

	public void toRefreshCalculate() {
		this.totales.reset();
		for (Articulo articulo: this.articulos) {
	    articulo.toCalculate(this.getIdSinIva().equals(1L), this.getTipoDeCambio());
			this.totales.addArticulo(articulo);
		} // for
		this.totales.removeUltimo(this.articulos.get(this.articulos.size()- 1));
		this.totales.removeTotal();
		this.setAjusteDeuda(this.totales.getTotal());
	}

	public void toCalculateGarantia(boolean modificado) {
		this.toCalculate(modificado);
	}
	
	public void toUpdatePorcentajes() {
		for (Articulo articulo: this.articulos) {
   		articulo.setDescuento(this.getDescuento());
 			articulo.setExtras(this.getExtras());
		} // for
		this.toCalculate(true);
	}
	
	public void toUpdateDescuento() {
		for (Articulo articulo: this.articulos) {
			articulo.setDescuento(this.getDescuento());
		} // for
		this.toCalculate(true);
	}
	
	public void toUpdateExtras() {
		for (Articulo articulo : this.articulos) {
			articulo.setExtras(this.getExtras());
		} // for
		this.toCalculate(true);
	}
	
	public void toAdjustArticulos() {
		int count= 0;
		while(count< this.articulos.size()) {
			if(!this.articulos.get(count).isValid())
				this.articulos.remove(count);
			else
				if(count> 0 && this.articulos.get(count- 1).getKey().equals(this.articulos.get(count).getKey())) {
					this.articulos.get(count- 1).setCantidad(this.articulos.get(count- 1).getCantidad()+ this.articulos.get(count).getCantidad());
					this.articulos.remove(count);
				} // if
				else
				  count++;
		} // while
		this.toRefreshCalculate();
	}

	public void toCantidad() {
		this.totales.reset();
		for (Articulo articulo : this.articulos) 
			this.totales.addArticulo(articulo.getIdArticulo(), articulo.getCantidad());
	}

	public void toCheckTotales() {
		// verificar que el importe total de la factura sea igual que el importe del detalle de sus articulos
		double total  = this.totales.getTotal();
		double detalle= 0D; 
		for (Articulo item : this.articulos) {
			detalle= Numero.toRedondear(detalle+ item.getImporte());
		} // for
		if(!Numero.toTruncate(total,  1).equals(Numero.toTruncate(detalle, 1))) {
			LOG.warn("Diferencias en los importes del documento ["+ this.getClass().getSimpleName()+ "] id: "+ this.getOrden().getKey()+ " verificar situacion, total ["+ total+ "] detalle["+ detalle+ "]");
			this.toCalculate();
			throw new KajoolBaseException("Por favor verifique que los importes sean correctos y vuelva a guardar el documento !");	
		} // if
	}

	public void validatePrecioArticulo() throws Exception{
		TcManticArticulosDto validate= null;
		ArticuloVenta articuloPivote = null;
		try {
			for(Articulo articulo: this.getArticulos()){
				validate= (TcManticArticulosDto) DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
				if(validate!= null && validate.isValid()){
					articuloPivote= (ArticuloVenta) articulo;
					articuloPivote.setDescuentoAsignado(articulo.getCosto().equals(validate.getMayoreo()) || articulo.getCosto().equals(validate.getMedioMayoreo()));
				} // if
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // validatePrecioArticulo
	
	public void cleanPrecioDescuentoArticulo() throws Exception{		
		ArticuloVenta articuloPivote = null;
		try {
			for(Articulo articulo: this.getArticulos()){				
				articuloPivote= (ArticuloVenta) articulo;
				articuloPivote.setDescuentoAsignado(false);				
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // validatePrecioArticulo
	
	@Override
	protected void finalize() throws Throwable {
		Methods.clean(this.articulos);
		Methods.clean(this.filtrados);
	}	
	
}
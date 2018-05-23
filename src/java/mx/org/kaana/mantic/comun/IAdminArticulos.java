package mx.org.kaana.mantic.comun;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20/05/2018
 *@time 02:08:29 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class IAdminArticulos implements Serializable {

	private static final long serialVersionUID=506956550372353914L;
	
	private List<Articulo> articulos;
	private Totales totales;

	public IAdminArticulos() throws Exception {
		this.totales= new Totales();
	}

	public List<Articulo> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<Articulo> articulos) {
		this.articulos=articulos;
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
	
	public abstract String getExtras();
	
	public abstract Long getIdSinIva();
	
	public abstract void setIdSinIva(Long idSinIva);
	
  public boolean add(Articulo articulo) throws Exception {
		if(this.articulos.indexOf(articulo)< 0) {
			this.toFillCodigo(articulo);
		  this.articulos.add(0, articulo);
   		this.toCalculate();
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
			for (Entity codigo : codigos) {
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
		if(this.articulos.indexOf(seleccionado)>= 0) {
		  this.articulos.remove(seleccionado);
  		toCalculate();
		} // if
	}

  public String toConsecutivo(String value) {
		return Fecha.getAnioActual()+ Cadena.rellenar(value, 6, '0', true);
	}	
		
	public void toCalculate() {
		this.totales.reset();
		for (Articulo articulo : this.articulos) {
		  articulo.toCalculate(this.getIdSinIva().equals(1L), this.getTipoDeCambio());
			this.totales.addImporte(articulo.getImportes().getImporte());
			this.totales.addDescuento(articulo.getImportes().getDescuento());
			this.totales.addExtra(articulo.getImportes().getExtra());
			this.totales.addIva(articulo.getImportes().getIva());
			this.totales.addSubTotal(articulo.getImportes().getSubTotal());
			this.totales.addTotal(articulo.getImportes().getTotal());
			this.totales.addArticulo(articulo.getIdArticulo());
		} // for
	}

	public void toUpdatePorcentajes() {
		for (Articulo articulo : this.articulos) {
			articulo.setDescuento(this.getDescuento());
			articulo.setExtras(this.getExtras());
		} // for
		this.toCalculate();
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
		this.toCalculate();
	}
	
}

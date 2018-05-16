package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.OrdenCompra;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AdminOrdenes implements Serializable {

	private static final long serialVersionUID=-5550539230224591510L;

	private OrdenCompra orden;
	private TcManticProveedoresDto proveedor;
	private TcManticAlmacenesDto almacen;
	private List<Articulo> articulos;
	private Totales totales;

	public AdminOrdenes(OrdenCompra orden) throws Exception {
		this.orden  = orden;
		this.totales= new Totales();
		if(this.orden.isValid()) {
			this.articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "TcManticOrdenesDetallesDto", "detalle", this.orden.toMap());
      this.orden.setIkAlmacen(new UISelectEntity(new Entity(this.orden.getIdAlmacen())));
      this.orden.setIkCliente(new UISelectEntity(new Entity(this.orden.getIdCliente())));
      this.orden.setIkProveedor(new UISelectEntity(new Entity(this.orden.getIdProveedor())));
      this.orden.setIkProveedorPago(new UISelectEntity(new Entity(this.orden.getIdProveedorPago())));
			toCalculate();
		}	// if
		else	{
		  this.articulos= new ArrayList<>();
			this.orden.setConsecutivo(this.toConsecutivo("0"));
			this.orden.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
			this.orden.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
		} // else	
	}

	public OrdenCompra getOrden() {
		return orden;
	}

	public void setOrden(OrdenCompra orden) {
		this.orden=orden;
	}

	public TcManticProveedoresDto getProveedor() {
		return proveedor;
	}

	public void setProveedor(TcManticProveedoresDto proveedor) {
		this.proveedor=proveedor;
	}

	public TcManticAlmacenesDto getAlmacen() {
		return almacen;
	}

	public void setAlmacen(TcManticAlmacenesDto almacen) {
		this.almacen=almacen;
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
	
  public void add(Articulo articulo) throws Exception {
		if(this.articulos.indexOf(articulo)< 0) {
			toFillCodigo(articulo);
		  this.articulos.add(articulo);
   		toCalculate();
		} // if
		else
		  throw new KajoolBaseException("El articulo ["+ articulo.getCodigo()+ "] ya esta dentro de la lista seleccionada !");
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
			Error.mensaje(e);
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
  	this.totales.setArticulos(this.articulos.size());
		for (Articulo articulo : this.articulos) {
		  articulo.toCalculate(this.orden.getIdSinIva().equals(1L), this.getOrden().getTipoDeCambio());
			this.totales.addImporte(articulo.getImportes().getImporte());
			this.totales.addDescuento(articulo.getImportes().getDescuento());
			this.totales.addExtra(articulo.getImportes().getExtra());
			this.totales.addIva(articulo.getImportes().getIva());
			this.totales.addSubTotal(articulo.getImportes().getSubTotal());
			this.totales.addTotal(articulo.getImportes().getTotal());
		} // for
	}

	public void toUpdatePorcentajes() {
		this.articulos.stream().map((articulo) -> {
			articulo.setDescuento(this.getOrden().getDescuento());
			return articulo;
		}).forEachOrdered((articulo) -> {
			articulo.setExtras(this.getOrden().getExtras());
		}); // for
		this.toCalculate();
	}
	
}

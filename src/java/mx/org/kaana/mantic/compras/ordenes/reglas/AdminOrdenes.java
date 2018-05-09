package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class AdminOrdenes implements Serializable {

	private static final long serialVersionUID=-5550539230224591510L;

	private TcManticOrdenesComprasDto orden;
	private TcManticProveedoresDto proveedor;
	private TcManticAlmacenesDto almacen;
	private List<Articulo> articulos;

	public AdminOrdenes(TcManticOrdenesComprasDto orden) throws Exception {
		this.orden=orden;
		if(this.orden.isValid()) {
			this.articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaOrdenesComprasDto", "articulos", this.orden.toMap());
			for (Articulo articulo: articulos) 
				articulo.getIdEntity().setKey(articulo.getIdArticulo());
		}	
		else	
		  this.articulos= new ArrayList<>();
	}

	public TcManticOrdenesComprasDto getOrden() {
		return orden;
	}

	public void setOrden(TcManticOrdenesComprasDto orden) {
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
	
  public void add() {
		Long idArticuloDetalle= new Long((int)(Math.random()*10000));
		this.articulos.add(new Articulo());
	}

	public void remove(Articulo seleccionado) {
		if(this.articulos.indexOf(seleccionado)>= 0)
		  this.articulos.remove(seleccionado);
	}

  public String toConsecutivo() {
		return Fecha.getAnioActual()+ Cadena.rellenar("1", 5, '0', true);
	}	
		
}

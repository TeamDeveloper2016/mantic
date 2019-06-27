package mx.org.kaana.mantic.inventarios.almacenes.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.mantic.inventarios.almacenes.enums.ETiposVentas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 27/05/2018
 *@time 10:26:25 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class AdminKardex implements Serializable {

	private static final long serialVersionUID=-2268878245925910845L;
	
	private Long idArticulo;
	private double costo;
	private double iva;
	private List<TiposVentas> tiposVentas;
	
	public AdminKardex(Long idArticulo, boolean rounded) {
		this(idArticulo, 1D, 16D, 0D, 0D, 0D, 3L, 8L, rounded);
	}

	public AdminKardex(Long idArticulo, double costo, double iva, double menudeo, double medioMayoreo, double mayoreo, long limiteMedioMayoreo, long limiteMayoreo, boolean rounded) {
		this.idArticulo= idArticulo;
		this.costo     = costo;
		this.iva       = iva;
		this.tiposVentas= new ArrayList<>();
		if(this.idArticulo> 0) {
			this.add(ETiposVentas.MENUDEO.ordinal(), "MENUDEO", menudeo, limiteMedioMayoreo, menudeo, rounded);
			this.add(ETiposVentas.MEDIO_MAYOREO.ordinal(), "MEDIO-MAYOREO", medioMayoreo, limiteMayoreo, menudeo, rounded);
			this.add(ETiposVentas.MAYOREO.ordinal(), "MAYOREO", mayoreo, 0, menudeo, rounded);
		} // if	
	}

	public Long getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(Long idArticulo) {
		this.idArticulo=idArticulo;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo=costo;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva=iva;
	}

	public List<TiposVentas> getTiposVentas() {
		return tiposVentas;
	}

  private void add(Integer index, String nombre, double precio, long limite, double pivote, boolean rounded) {
		this.tiposVentas.add(new TiposVentas(index, nombre, this.costo, precio, this.iva, limite, pivote, rounded));
	}	

	public void toUpdateUtilidad(Integer index, Double value) {
		int posicion= this.tiposVentas.indexOf(new TiposVentas(index));
		if(posicion>= 0)
			((TiposVentas)this.tiposVentas.get(posicion)).toUpdateUtilidad(value);
	}
	
	public void toCalculate(Integer index) {
		int posicion= this.tiposVentas.indexOf(new TiposVentas(index));
		if(posicion>= 0)
			((TiposVentas)this.tiposVentas.get(posicion)).toCalculate();
	}
	
}

package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/09/2019
 *@time 09:02:26 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class PrecioVenta implements Serializable {

	private static final long serialVersionUID=-5902307046480075898L;

	private double costo;
	private double precio;
	private double utilidad;
	private double iva;

	public PrecioVenta() {
		this(1D);
	}

	public PrecioVenta(double costo) {
		this(costo, costo);
	}

	public PrecioVenta(double costo, double precio) {
		this(costo, precio, 0.5);
	}

	public PrecioVenta(double costo, double precio, double utilidad) {
		this(costo, precio, utilidad, 16D);
	}

	public PrecioVenta(double costo, double precio, double utilidad, double iva) {
		this.costo=costo;
		this.precio=precio;
		this.utilidad=utilidad;
		this.iva=iva;
	}
	
}

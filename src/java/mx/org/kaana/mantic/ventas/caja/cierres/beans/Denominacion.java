package mx.org.kaana.mantic.ventas.caja.cierres.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.db.dto.TcManticCierresMonedasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/08/2018
 *@time 01:46:25 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Denominacion extends TcManticCierresMonedasDto implements Serializable, Cloneable {

	private static final long serialVersionUID=-190192763738981546L;
  
	private String nombre;
	private double denominacion;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public double getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(double denominacion) {
		this.denominacion=denominacion;
	}
	
	@Override
	public Class toHbmClass() {
		return TcManticCierresMonedasDto.class;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return (Denominacion)super.clone();
	}

  public Denominacion copy() throws CloneNotSupportedException {
		return (Denominacion)this.clone();
	}	
	
}

package mx.org.kaana.libs.cfg;

import java.io.Serializable;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 03:54:53 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Configuracion implements Cloneable, Serializable {

	private static final long serialVersionUID=4314677498795621046L;
	private String descripcion;
	private Integer longitud;

	public Configuracion(String descripcion, Integer longitud) {
		this.descripcion=descripcion;
		this.longitud   =longitud;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Integer getLongitud() {
		return longitud;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		} // if
		if (getClass()!=obj.getClass()) {
			return false;
		} // if
		final Configuracion other=(Configuracion) obj;
		if ((this.descripcion==null) ? (other.descripcion!=null) : !this.descripcion.equals(other.descripcion)) {
			return false;
		} // if
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=89*hash+(this.descripcion!=null ? this.descripcion.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "Configuracion["+"descripcion="+descripcion+", longitud=" /*+ longitud*/+']';
	}

	@Override
	public Configuracion clone() throws CloneNotSupportedException {
		return (Configuracion) super.clone();
	}
}

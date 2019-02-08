package mx.org.kaana.mantic.inventarios.entradas.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Nombres implements Serializable {

	private static final long serialVersionUID=323884892456452488L;
	
	private String nombre;
	private String ruta;
	private String alias;

	public Nombres() {
		this("XYZ");
	}

	public Nombres(String nombre) {
		this(nombre, "", "");
	}

	public Nombres(String nombre, String ruta, String alias) {
		this.nombre= nombre;
		this.ruta  = ruta;
		this.alias = alias;
	}	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}	
	
	@Override
	public int hashCode() {
		int hash=3;
		hash=67*hash+Objects.hashCode(this.nombre);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final Nombres other=(Nombres) obj;
		if (!Objects.equals(this.nombre, other.nombre)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Listado{nombre="+ nombre+ '}';
	}
	
}

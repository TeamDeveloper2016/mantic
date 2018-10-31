package mx.org.kaana.mantic.facturas.beans;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 *@company MANTIC
 *@project KAANA (Sistema de seguimiento y control de proyectos)
 *@date 30/10/2018
 *@time 07:10:48 PM 
 *@author Team Developer 2018 <team.developer@gmail.com>
 */

public class Correo implements Serializable {

	private static final long serialVersionUID=2828183989138497937L;

	private Long idCorreo;
	private String descripcion;

	public Correo() {
		this((new Random()).nextLong(), "team.developer@gmail.com");
	}

	public Correo(Long idCorreo, String descripcion) {
		this.idCorreo= idCorreo;
		this.descripcion=descripcion;
	}

	public Long getIdCorreo() {
		return idCorreo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion=descripcion;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=53*hash+Objects.hashCode(this.descripcion);
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
		final Correo other=(Correo) obj;
		if (!Objects.equals(this.descripcion, other.descripcion)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Correo{"+"idCorreo="+idCorreo+", descripcion="+descripcion+'}';
	}
		
}

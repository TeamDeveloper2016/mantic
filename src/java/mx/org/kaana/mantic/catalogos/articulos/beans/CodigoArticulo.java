package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/10/2018
 *@time 11:15:22 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class CodigoArticulo implements Serializable {

	private static final long serialVersionUID=795752101228019032L;
	
	private Long idArticulo;
	private String propio;
	private String nombre;
	private Long cantidad;
	private Long tope;
	private String codigo;
	private Boolean agregado;

	public CodigoArticulo(Long idArticulo) {
		this(idArticulo, null, null);
	}

	public CodigoArticulo(Long idArticulo, String propio, String nombre) {
		this(idArticulo, propio, nombre, 0L, 999999L, "code128", true);
	}
	
	public CodigoArticulo(Long idArticulo, String propio, String nombre, Long cantidad, Long tope, String codigo, Boolean agregado) {
		this.idArticulo=idArticulo;
		this.propio=propio;
		this.nombre=nombre;
		this.cantidad=cantidad;
		this.tope=tope;
		this.codigo=codigo;
		this.agregado=agregado;
	}

	public Long getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(Long idArticulo) {
		this.idArticulo=idArticulo;
	}

	public String getPropio() {
		return propio;
	}

	public void setPropio(String propio) {
		this.propio=propio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public Long getCantidad() {
		return cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad=cantidad;
	}

	public Long getTope() {
		return tope;
	}

	public void setTope(Long tope) {
		this.tope=tope;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodio(String codigo) {
		this.codigo=codigo;
	}

	public Boolean getAgregado() {
		return agregado;
	}

	public void setAgregado(Boolean agregado) {
		this.agregado=agregado;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=23*hash+Objects.hashCode(this.idArticulo);
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
		final CodigoArticulo other=(CodigoArticulo) obj;
		if (!Objects.equals(this.idArticulo, other.idArticulo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CodigoArticulo{"+"idArticulo="+idArticulo+", propio="+propio+", nombre="+nombre+", cantidad="+cantidad+", tope="+tope+", codigo="+codigo+", agregado="+agregado+'}';
	}

}

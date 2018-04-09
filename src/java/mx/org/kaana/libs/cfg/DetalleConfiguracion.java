package mx.org.kaana.libs.cfg;

import java.io.Serializable;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 03:56:45 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class DetalleConfiguracion implements Cloneable, Serializable {

	private static final long serialVersionUID=-8358917143048009268L;
	private Integer nivel;
	private Integer longitud;
	private Integer idJustificacion;
	private String relleno;
	private String dominio;

	public DetalleConfiguracion(Integer nivel, Integer longitud, Integer idJustificacion, String relleno, String dominio) {
		this.nivel=nivel;
		this.longitud=longitud;
		this.idJustificacion=idJustificacion;
		this.relleno=relleno;
		this.dominio=dominio;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio=dominio;
	}

	public Integer getIdJustificacion() {
		return idJustificacion;
	}

	public void setIdJustificacion(Integer idJustificacion) {
		this.idJustificacion=idJustificacion;
	}

	public Integer getLongitud() {
		return longitud;
	}

	public void setLongitud(Integer longitud) {
		this.longitud=longitud;
	}

	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel=nivel;
	}

	public String getRelleno() {
		return relleno;
	}

	public void setRelleno(String relleno) {
		this.relleno=relleno;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final DetalleConfiguracion other=(DetalleConfiguracion) obj;
		if (this.nivel!=other.nivel&&(this.nivel==null||!this.nivel.equals(other.nivel))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=97*hash+(this.nivel!=null ? this.nivel.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "DetalleConfiguracion{"+"nivel="+nivel+", longitud="+longitud+", justificacion="+idJustificacion+", relleno="+relleno+", dominio="+dominio+'}';
	}

	@Override
	public DetalleConfiguracion clone() throws CloneNotSupportedException {
		return (DetalleConfiguracion) super.clone();
	}
}

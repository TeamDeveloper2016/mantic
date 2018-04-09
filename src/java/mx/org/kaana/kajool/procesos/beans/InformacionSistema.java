package mx.org.kaana.kajool.procesos.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/10/2016
 *@time 06:14:39 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class InformacionSistema implements Serializable{

  private static final long serialVersionUID = 6588417661766535286L;
  private String espacioAsignado;
	private String unidadMedida;
	private String descripcion;
	private String nombre;

	public InformacionSistema() {
		this("", "", "");
	}	
	
	public InformacionSistema(String nombre, String descripcion) {
		this("", "", descripcion, nombre);
	}
	
	public InformacionSistema(String espacioAsignado, String unidadMedida, String descripcion) {
		this(espacioAsignado, unidadMedida, descripcion, "");
	}
	
	public InformacionSistema(String espacioAsignado, String unidadMedida, String descripcion, String nombre) {
		this.espacioAsignado= espacioAsignado;
		this.unidadMedida   = unidadMedida;
		this.descripcion    = descripcion;
		this.nombre         = nombre;
	}

	public String getEspacioAsignado() {
		return espacioAsignado;
	}

	public void setEspacioAsignado(String espacioAsignado) {
		this.espacioAsignado= espacioAsignado;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida= unidadMedida;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion= descripcion;
	}		

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre= nombre;
	}	
}

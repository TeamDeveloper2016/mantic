package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/09/2015
 *@time 03:55:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EPropiedadesServidor {

	PROPIEDAD_SISTEMA_TITULO      ("sistema.titulo", false),
	PROPIEDAD_SISTEMA_DNS         ("sistema.dns", true),
	PROPIEDAD_SISTEMA_LOG_ERROR   ("sistema.log.error", true),
	PROPIEDAD_SISTEMA_CSS         ("sistema.css", false),
	PROPIEDAD_SISTEMA_FIRMARSE    ("sistema.firmarse", true),
	PROPIEDAD_SISTEMA_SERVIDOR    ("sistema.servidor", false),
	PROPIEDAD_SISTEMA_HIBERNATE   ("hibernate.db.connection", true),
  PROPIEDAD_SISTEMA_QUARTZ      ("sistema.quartz", true),
  PROPIEDAD_SISTEMA_QUARTZ_TAREA("sistema.quartz.tareas", false);
	
	private String propiedad;
	private boolean requiereServidor;

	private EPropiedadesServidor(String propiedad, boolean requiereServidor) {
		this.propiedad= propiedad;
		this.requiereServidor= requiereServidor;
	}	
	
	public Long getKey(){
		return new Long(ordinal() + 1);
	}
	
	public String getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(String propiedad) {
		this.propiedad= propiedad;
	}	

	public boolean isRequiereServidor() {
		return requiereServidor;
	}

	public void setRequiereServidor(boolean requiereServidor) {
		this.requiereServidor= requiereServidor;
	}	
}

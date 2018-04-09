package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 4/09/2015
 *@time 09:10:41 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EPaginasPrivilegios {

	DEFAULT   ("Bienvenida", "/Paginas/Contenedor/bienvenida.jsf"),
	ESCUELAS  ("Centros de trabajo", "/Paginas/Escuelas/filtro.jsf"),
	PERFILES  ("Perfiles", "/Paginas/Acceso/Perfil/filtro.jsf");
	
	private String descripcion;
	private String path;

	private EPaginasPrivilegios(String descripcion, String path) {
		this.descripcion= descripcion;
		this.path       = path;
	}

	public Long getKey(){
		return new Long(ordinal() + 1);
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public String getPath() {
		return path;
	}	
}

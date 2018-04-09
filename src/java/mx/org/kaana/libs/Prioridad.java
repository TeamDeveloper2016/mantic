package mx.org.kaana.libs;


/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 14, 2012
 *@time 1:00:53 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum Prioridad {

  IN_KEY("kajool0A"),      // valor del campo llave
  IN_FORCE("kajool0B"),    // forzados a que se utilicen
  IN_AUTH("kajool0C"),     // autentifica
  IN_ICON("kajool0D"),     // botones tabla
  IN_FILTER("kajool0E"),   // filtro
  IN_FREE("kajool0F"),     // uso libre, mantener informacion entre paginas
  IN_USER("kajool0G"),     // agregar - modificar
  IN_TOKEN ("kajool0H"),   // archivos
  IN_SHOW("kajool0I"),     // mostrar - eliminar
  IN_PREVIOUS("kajool0J"), // valores de pag anterior fuera de filtros
  IN_ACTION("kajool0K"),   // detalle accion
  IN_MOVEMENT("kajool0L"), // valores entre filtros anidados son los campos llave entre los filtro
  IN_SAVE("kajool0M"),     // valores para la pagina de registrar
  PREFIX("kajool0");       // valores para la pagina de registrar
	
	private String code;

	private Prioridad(String code) {
		this.code=code;
	}

	public String getCode() {
		return code;
	}

	public int length() {
		return 6;
	}
}
	

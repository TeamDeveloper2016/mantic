package mx.org.kaana.mantic.correos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/03/2019
 *@time 01:08:38 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ECorreos {
  FACTURACION("/mx/org/kaana/mantic/correos/templates/facturacion.html", "/mx/org/kaana/mantic/correos/templates/"), 
	COTIZACIONES("/mx/org/kaana/mantic/correos/templates/cotizacion.html", "resources/janal/img/correo/"),
	ORDENES_COMPRA("/mx/org/kaana/mantic/correos/templates/ordenesCompra.html", "resources/janal/img/correo/");
	 
	private String template;
	private String images;

	private ECorreos(String template, String images) {
		this.template=template;
		this.images=images;
	}

	public String getTemplate() {
		return template;
	}

	public String getImages() {
		return images;
	}
	
}

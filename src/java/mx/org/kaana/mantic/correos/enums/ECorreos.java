package mx.org.kaana.mantic.correos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/03/2019
 *@time 01:08:38 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ECorreos {
	
  FACTURACION   ("/mx/org/kaana/mantic/correos/templates/facturacion.html", "resources/janal/img/sistema/"), 
	COTIZACIONES  ("/mx/org/kaana/mantic/correos/templates/cotizacion.html", "resources/janal/img/sistema/"),
	ORDENES_COMPRA("/mx/org/kaana/mantic/correos/templates/ordenes.html", "resources/janal/img/sistema/"),
	CUENTAS       ("/mx/org/kaana/mantic/correos/templates/cuentas.html", "resources/janal/img/sistema/");
	 
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
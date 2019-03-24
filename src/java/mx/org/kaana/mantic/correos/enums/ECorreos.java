package mx.org.kaana.mantic.correos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/03/2019
 *@time 01:08:38 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ECorreos {
  INVITACION("/mx/org/kaana/mantic/correos/templates/invitacion.html", "/mx/org/kaana/mantic/correos/templates/"), 
	REGISTRO("/mx/org/kaana/mantic/correos/templates/invitacion.html", "resources/janal/img/correo/"), 
	NOTIFICACION("/mx/org/kaana/mantic/correos/templates/invitacion.html", "resources/janal/img/correo/");
	 
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

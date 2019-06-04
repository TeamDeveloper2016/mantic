package mx.org.kaana.mantic.correos.enums;

import mx.org.kaana.libs.recurso.TcConfiguraciones;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/03/2019
 *@time 01:08:38 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ECorreos {
	
  FACTURACION   ("/mx/org/kaana/mantic/correos/templates/facturacion.html", "resources/janal/img/sistema/", "correo.admin.user", "correo.admin.pass", "facturas@ferreteriabonanza.com", "Facturas F. Bonanza"), 
	COTIZACIONES  ("/mx/org/kaana/mantic/correos/templates/cotizacion.html", "resources/janal/img/sistema/", "correo.admin.user", "correo.admin.pass", "ventas@ferreteriabonanza.com", "Ventas F. Bonanza"),
	ORDENES_COMPRA("/mx/org/kaana/mantic/correos/templates/ordenes.html", "resources/janal/img/sistema/", "correo.admin.user", "correo.admin.pass", "compras@ferreteriabonanza.com", "Compras F. Bonanza"),
	CUENTAS       ("/mx/org/kaana/mantic/correos/templates/cuentas.html", "resources/janal/img/sistema/", "correo.admin.user", "correo.admin.pass", "ventas@ferreteriabonanza.com", "Ventas F. Bonanza");
	 
	private String template;
	private String images;
	private String user;
	private String password;
	private String email;
	private String alias;

	private ECorreos(String template, String images, String user, String password, String email, String alias) {
		this.template=template;
		this.images=images;
		this.user= user;
		this.password= password;
		this.email= email;
		this.alias= alias;
	}

	public String getTemplate() {
		return template;
	}

	public String getImages() {
		return images;
	}	

	public String getUser() {
		return TcConfiguraciones.getInstance().getPropiedadServidor(this.user);
	}

	public String getPassword() {
		return TcConfiguraciones.getInstance().getPropiedadServidor(this.password);
	}

	public String getEmail() {
		return email;
	}

	public String getAlias() {
		return alias;
	}

}
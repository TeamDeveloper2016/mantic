package mx.org.kaana.kajool.procesos.acceso.reglas;

import java.io.File;
import java.io.Serializable;
import mx.org.kaana.libs.correo.Correo;
import mx.org.kaana.libs.pagina.JsfBase;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/09/2015
 *@time 02:15:43 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Notificar implements Serializable {

	private static final long serialVersionUID=-335441183083019643L;
	
  private String remitente;
  private String destinatario;
  private String asunto;
  private String url;

  public Notificar() {
  }

  public Notificar(String remitente, String destinatario, String asunto, String url) {
    this.remitente = remitente;
    this.destinatario = destinatario;
    this.asunto = asunto;
    this.url=url;
  }

  public String getRemitente() {
    return remitente;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public void setRemitente(String remitente) {
    this.remitente = remitente;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  public String getAsunto() {
    return asunto;
  }

  public void setAsunto(String encabezado) {
    this.asunto = encabezado;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void enviar() throws Exception {
    Correo correo= new Correo(this.remitente, this.destinatario, this.asunto);
    correo.setContenido(contenido());
    correo.enviar();
  }
  private StringBuilder contenido() throws Exception {
    //String proyecto      = null;
    StringBuilder regresar = null;
    //String nombre        = null;
    File archivoImagen     = null;
    byte[] encodedBytes    = null;
    String logoJanal       = null;
    String logoKajool      = null;

    archivoImagen = new File (JsfBase.getApplication().getRealPath("/resources/janal/img/sistema/logo.png"));
    encodedBytes = Base64.encodeBase64(FileUtils.readFileToByteArray(archivoImagen));
    logoJanal = new String(encodedBytes);
    archivoImagen = new File (JsfBase.getApplication().getRealPath("/resources/janal/img/sistema/logo.png"));
    encodedBytes = Base64.encodeBase64(FileUtils.readFileToByteArray(archivoImagen));
    logoKajool = new String(encodedBytes);
    regresar= new StringBuilder();
    regresar.append("<html><title><head></head></title><body>");
    regresar.append("<table  align=\"center\">");
    regresar.append("<tr>");
    regresar.append("<th style=\"text-align: center\">");
    regresar.append("<strong> Confirmaci&oacute;n de cambio de contrase&ntilde;a</strong>");
    regresar.append("</th>");
    regresar.append("</tr>");
    regresar.append("</table>");
    regresar.append("<br/>");
    regresar.append("<br/>");
    regresar.append("<table align=\"center\">");
    regresar.append("<tr>");
    regresar.append("<td>");
    regresar.append("<strong> Bienvenido </strong>");
    //regresar.append(nombre.toUpperCase());
    regresar.append("</td>");
    regresar.append("</tr>");
    regresar.append("<tr><td>");
    regresar.append("&nbsp;");
    regresar.append("</td></tr>");
    regresar.append("<tr><td>");
    regresar.append("Para completar el proceso de cambio de contrase&ntilde;a, es necesario ");
    regresar.append("acceder a la siguiente liga en la cual se le pedir&aacute; capturar los datos requeridos. ");
    regresar.append("</td></tr>");
    regresar.append("<tr><td>");
    regresar.append("Al ingresar a la liga, ser&aacute; necesario proporcionar su cuenta registrada en el sistema. ");
    regresar.append("</td></tr>");
    regresar.append("<tr><td>");
    regresar.append("Favor de completar el cambio de contrase&ntilde;a en la siguiente liga: ");
    regresar.append("</td></tr>");
    regresar.append("<tr><td>");
    regresar.append("&nbsp;");
    regresar.append("</td></tr>");
    regresar.append("<tr><td>");
    regresar.append("<strong>Liga: </strong> <a href=");
    regresar.append(this.url);
    regresar.append(">");
    regresar.append(this.url);
    regresar.append("</a>");
    regresar.append("</td></tr>");
    regresar.append("<tr><td>");
    regresar.append("&nbsp;");
    regresar.append("</td></tr>");
    regresar.append("<tr><td text-align:center><br/>");
    regresar.append("Atte. ");
    regresar.append("Administrador KAJOOL");
    regresar.append("</td></tr>");
    regresar.append("<tr><td>");
    regresar.append("<img alt=\"Ferreteria Bonanza\" width=\"277\" height=\"80\" src=\"data:image/png;base64,");
    regresar.append(logoJanal);
    regresar.append("\" /> </td> ");
    regresar.append("\" />");
    regresar.append("</tr></table>");
    regresar.append("</body></html>");
    return regresar;
  }

	public static void main(String ... args) throws Exception {
		Notificar notificar= new Notificar("compras@ferreteriabonanza.com", "jimenez76@yahoo.com", "demostracion", "https://ferreteriabonanza.com/");
		notificar.enviar();
	}

}

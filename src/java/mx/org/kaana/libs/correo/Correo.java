package mx.org.kaana.libs.correo;

import java.io.ByteArrayOutputStream;


public class Correo {

  private String remitente;
  private String destinatario;
  private String asunto;
  private StringBuilder contenido;

  public Correo(String remitente, String destinatario, String asunto) {
    this.setRemitente(remitente);
    this.setDestinatario(destinatario);
    this.setAsunto(asunto);
    this.setContenido(new StringBuilder());
  }

  public void enviar() {
    Envio.asuntoMensaje(this.getRemitente(), this.getDestinatario(), null ,this.getAsunto(), this.getContenido().toString(), null, true);
  }

  public void enviarConAdjunto(ByteArrayOutputStream anexo, String adjunto) {
    Envio.asuntoMensaje(getRemitente(), getDestinatario(), anexo, getAsunto(), getContenido().toString(), adjunto, true);
  }
	
	public void enviarConInscrustracion(String [] rutasInLine) {
    Envio.asuntoMensaje(this.getRemitente(), this.getDestinatario(), null ,this.getAsunto(), this.getContenido().toString(), null, true, null,rutasInLine);
  }

  public void setRemitente(String remitente) {
    this.remitente = remitente;
  }

  public String getRemitente() {
    return remitente;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public void setAsunto(String asunto) {
    this.asunto = asunto;
  }

  public String getAsunto() {
    return asunto;
  }

  public void setContenido(StringBuilder contenido) {
    this.contenido = contenido;
  }

  public StringBuilder getContenido() {
    return contenido;
  }	

}

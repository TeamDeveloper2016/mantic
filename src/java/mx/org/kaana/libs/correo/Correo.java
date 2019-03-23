package mx.org.kaana.libs.correo;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;
import javax.mail.Authenticator;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;


public class Correo implements Serializable {

	private static final long serialVersionUID=-2482664376517548312L;

  private String remitente;
  private String destinatario;
	private String conCopia;
  private String asunto;
  private StringBuilder contenido;
	private Authenticator autenticar;

	public Correo(String remitente, String destinatario, String asunto) {  
    this.setRemitente(remitente);
    this.setDestinatario(destinatario);
    this.setAsunto(asunto);
    this.setContenido(new StringBuilder());
    this.autenticar = new SMTPAuthenticator(); 
    this.conCopia="";
  }

	public Correo(String remitente, String destinatario, String conCopia, String asunto) {  
    this(remitente, destinatario, asunto);
    this.conCopia= conCopia;
  }
  
  public Correo (Authenticator autenticar, String remitente, String destinatario, String asunto) {
    this (autenticar, remitente, destinatario, "", asunto);    
  }  
  
  public Correo (Authenticator autenticar, String remitente,  String destinatario, String conCopia, String asunto) {
    this.setRemitente(remitente);
    this.setDestinatario(destinatario);
    this.setAsunto(asunto);
    this.setContenido(new StringBuilder());     
    this.conCopia   = conCopia;
    this.autenticar = autenticar;
  } 
	
  public void enviar() {
    Envio  envio = new Envio(this.autenticar);
    envio.asuntoMensaje(this.getRemitente(), this.getDestinatario(), Cadena.isVacio(this.conCopia) ? "" : this.conCopia, null ,this.getAsunto(), this.getContenido().toString(), null, true);
  } // enviar
  
  public void enviarConEncoding(String encoding) {
    Envio envio           = null;
    String encodingMessage= null;
    try {
      envio= new Envio(this.autenticar);
      encodingMessage= new String(this.getContenido().toString().getBytes(), encoding);
      envio.asuntoMensaje(this.getRemitente(), this.getDestinatario(), "", null, this.getAsunto(), encodingMessage, null, true);
    } // try
    catch (Exception e) {      
      Error.mensaje(e);      
    } // catch            
  } // enviarEncoding

  public void enviarConAdjunto(ByteArrayOutputStream anexo, String adjunto, EFormatos formato) {
    Envio  envio = new Envio(this.autenticar);
    envio.asuntoMensaje(getRemitente(), getDestinatario(), conCopia,  anexo, getAsunto(), getContenido().toString(), adjunto, true, formato.name());
  } // enviarConAdjunto
  
  public void enviarConAdjunto(ByteArrayOutputStream anexo, String adjunto) {
    Envio  envio = new Envio(this.autenticar);
    envio.asuntoMensaje(getRemitente(), getDestinatario(), "",anexo, getAsunto(), getContenido().toString(), adjunto, true);
  } // enviarConAdjunto
  
  public void enviarConVariosAdjunto(ByteArrayOutputStream anexo, List<String> adjunto) {
    Envio  envio = new Envio(this.autenticar);
    envio.asuntoMensaje(getRemitente(), getDestinatario(), anexo, getAsunto(), getContenido().toString(), adjunto, true, null);
  } // enviarConAdjunto
	
	public void enviarConInscrustracion(String [] rutasInLine) {
    Envio  envio = new Envio(this.autenticar);
    envio.asuntoMensaje(this.getRemitente(), this.getDestinatario(), null ,this.getAsunto(), this.getContenido().toString(), null, true, null,rutasInLine);
  } // enviarConInscrustracion

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
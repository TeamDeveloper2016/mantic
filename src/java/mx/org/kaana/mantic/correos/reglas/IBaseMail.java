package mx.org.kaana.mantic.correos.reglas;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import mx.org.kaana.libs.correo.SMTPAuthenticator;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.correos.beans.Attachment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/03/2019
 *@time 07:22:45 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class IBaseMail implements Serializable {

	private static final Log LOG=LogFactory.getLog(IBaseMail.class);
	private static final long serialVersionUID=2578645644776553114L;

	private String from;
	private String to;
	private String copies;
	private String subject;
	private List<Attachment> files;
	private Authenticator authenticator;
	private String alias;

	public IBaseMail(String to, String subject) {
		this(Configuracion.getInstance().getPropiedadServidor("mail.user.default"), to, null, subject, Collections.EMPTY_LIST);
	}

	public IBaseMail(String from, String to, String subject) {
		this(from, to, null, subject, Collections.EMPTY_LIST);
	}

	public IBaseMail(String from, String to, String copies, String subject, List<Attachment> files) {
    this(from, to, copies, subject, files, "");
	}
	
	public IBaseMail(String from, String to, String copies, String subject, List<Attachment> files, String alias) {
    this(from, to, copies, subject, files, new SMTPAuthenticator());
		this.alias= alias;
	}
	
	public IBaseMail(String from, String to, String copies, String subject, Authenticator authenticator) {
    this(from, to, copies, subject, Collections.EMPTY_LIST, authenticator);
	}
	
	public IBaseMail(String from, String to, String copies, String subject, List<Attachment> files, Authenticator authenticator) {
		this.from=from;
		this.to=to;
		this.copies=copies;
		this.subject=subject;
		this.files=files;
    this.authenticator= authenticator; 
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to=to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject=subject;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias=alias;
	}
	
	private Address[] toPrepare(String emails) throws AddressException {
    StringTokenizer tokens= new StringTokenizer(emails, ",");     
		Address[] regresar= new InternetAddress[tokens.countTokens()];
		int count= 0;
		while(tokens.hasMoreTokens()) {
			String email= tokens.nextToken();
			if(!Cadena.isVacio(email))
			  regresar[count++]= new InternetAddress(email.trim());
		} // while
		return regresar;
	}
		
	private Address[] toPrepareAlias(String emails, String alias) throws AddressException {
    StringTokenizer tokens= new StringTokenizer(emails, ",");     
		Address[] regresar= new InternetAddress[tokens.countTokens()];
		int count= 0;
		while(tokens.hasMoreTokens()) {
			String email= tokens.nextToken();
			if(!Cadena.isVacio(email)) {
				try {
					if(Cadena.isVacio(alias))
	  		    regresar[count]= new InternetAddress(email.trim());
					else
  			    regresar[count]= new InternetAddress(email.trim(), alias);
				} // try
				catch(UnsupportedEncodingException e) {
					regresar[count]= new InternetAddress(email.trim());
				} // catch
				count++;
			} // if
		} // while
		return regresar;
	}
		
	public void send(String content) throws MessagingException, UnsupportedEncodingException {
    Properties properties= null;
    Session session      = null;
    MimeMessage message  = null;
    MimeBodyPart body    = null;
    DataSource ds        = null;
    Multipart multipart  = null;    
		List<BodyPart> files = null;
    try {
      properties= new Properties();
			files     = new ArrayList<>();
      properties.put("mail.smtp.host", Configuracion.getInstance().getPropiedadServidor("mail.smtp.server"));
      properties.put("mail.transport.protocol", "smtp");
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.port", "26");			
			session    = Session.getInstance(properties, this.authenticator);            
      message= new MimeMessage(session);
			if(Cadena.isVacio(this.alias))
        message.setFrom(new InternetAddress(this.from));
			else
        message.setFrom(new InternetAddress(this.from, this.alias));
      // SI SON VARIOS CORREOS TIENEN QUE ESTAR SEPARADOS POR COMAS Y SIN ESPACIOS EN BLANCO
      message.addRecipients(javax.mail.Message.RecipientType.TO, this.toPrepare(this.to));
			if(this.copies!= null)
        message.addRecipients(javax.mail.Message.RecipientType.BCC, this.toPrepare(this.copies));
      message.setSubject(this.subject);
			if(this.files!= null && !this.files.isEmpty()) {
				multipart = new MimeMultipart(); // Multipart
				for(Attachment item: this.files) {                   
					body= new MimeBodyPart();
					if(item.getCid()) {
            LOG.info("Add file to content-id to email :" + item.getAbsolute());
						ds  = new FileDataSource(item.getFile());
						body.setDataHandler(new DataHandler(ds));
						body.setFileName(item.getName());
						body.setDisposition(MimeBodyPart.INLINE);
						body.setHeader("Content-ID", item.getId());
						multipart.addBodyPart(body);               
					} // if 
          else {
            LOG.info("Add file to attachment to email :" + item.getAbsolute());
            body.setDataHandler(new DataHandler(new FileDataSource(item.getAbsolute())));
            body.setFileName(item.getName());
            files.add(body);          
          }
				} // for item
				body = new MimeBodyPart(); // MimeBodyPart
				body.setContent(Cadena.toCharSet(content), "text/html");
				multipart.addBodyPart(body);
				// add files to attachment for email
        for(BodyPart bp: files)
          multipart.addBodyPart(bp);
				message.setContent(multipart);   
      } // if
			else
  			message.setContent(Cadena.toCharSet(content), "text/html");
      Transport.send(message);
			LOG.info("Correo enviado al buzon de: "+ this.to);
		} // try
    finally {
      if(properties!= null)
        properties = null;
      if(session!= null)
        session = null;
      if(message != null)
        message = null;
      if(body != null)
        body = null;
      if(ds != null)
        ds = null;
      if(multipart!=null)
        multipart = null;      
			Methods.clean(files);
    } // finally
  } 

  public static void main(String ... args) throws AddressException, UnirestException {
		// https://www.emailverifierapp.com/email-verification-api-for-developers/sample-codes/#java
		// API User Name: jimenez7616336
		String email= "jimenez76%40yahoo.com";
		String token= "8c2de22f7ea516f59ee8fad67f60e4e0164d8166";
		HttpResponse<String> response = Unirest.post("https://api.evasrv.com/email_verification/")
		.header("cache-control", "no-cache")
		.header("content-type", "application/x-www-form-urlencoded")
		.body("email="+ email+ "&user_API_token="+ token+ "&free=true&disposable=true&did_you_mean=true&role=true&bad=true&ev_score=true")
		.asString();
		LOG.info(response.getBody());
		LOG.info("ok.");
	}	
	
}

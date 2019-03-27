package mx.org.kaana.mantic.correos.reglas;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
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

	public IBaseMail(String to, String subject) {
		this(Configuracion.getInstance().getPropiedadServidor("mail.user.default"), to, null, subject, Collections.EMPTY_LIST);
	}

	public IBaseMail(String from, String to, String subject) {
		this(from, to, null, subject, Collections.EMPTY_LIST);
	}

	public IBaseMail(String from, String to, String copies, String subject, List<Attachment> files) {
    this(from, to, copies, subject, files, new SMTPAuthenticator());
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
	
	private Address[] toPrepare(String emails) throws AddressException {
    StringTokenizer tokens= new StringTokenizer(emails, ",");     
		Address[] regresar= new InternetAddress[tokens.countTokens()];
		int count= 0;
		while(tokens.hasMoreTokens()) {
			regresar[count++]= new InternetAddress(tokens.nextToken());
		} // while
		return regresar;
	}
		
	public void send(String content) throws MessagingException {
    Properties properties= null;
    Session session      = null;
    MimeMessage message  = null;
    MimeBodyPart body    = null;
    DataSource ds        = null;
    Multipart multipart  = null;    
    try {
      properties = new Properties();
      properties.put("mail.smtp.host", Configuracion.getInstance().getPropiedadServidor("mail.smtp.server"));
      properties.put("mail.transport.protocol", "smtp");
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.port", "26");			
			session    = Session.getInstance(properties, this.authenticator);            
      message= new MimeMessage(session);
      message.setFrom(new InternetAddress(this.from));
      // SI SON VARIOS CORREOS TIENEN QUE ESTAR SEPARADOS POR COMAS Y SIN ESPACIOS EN BLANCO
      message.addRecipients(javax.mail.Message.RecipientType.TO, this.toPrepare(this.to));
			if(this.copies!= null)
        message.addRecipients(javax.mail.Message.RecipientType.BCC, this.toPrepare(this.copies));
      message.setSubject(this.subject);
			if(this.files!= null && !this.files.isEmpty()) {
				multipart = new MimeMultipart(); //Multipart
				for(Attachment item: this.files) {                   
					if(item.getCid()) {
						body= new MimeBodyPart();
						ds  = new FileDataSource(item.getFile());
						body.setDataHandler(new DataHandler(ds));
						body.setFileName(item.getName());
						body.setDisposition(MimeBodyPart.INLINE);
						body.setHeader("Content-ID", item.getId());
						multipart.addBodyPart(body);               
				    body = new MimeBodyPart(); //MimeBodyPart
  			    body.setContent(Cadena.toCharSet(content), "text/html");
						multipart.addBodyPart(body);
					} // if
				} // for rutas                           
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
    } // finally
  } 

  public static void main(String ... args) throws AddressException {
		InternetAddress internetAddress = new InternetAddress("xyz@yahoo.com");
    // internetAddress.validate();
		LOG.info("ok.");
	}	
	
}

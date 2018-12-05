package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import javax.mail.Authenticator;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.correo.Correo;
import mx.org.kaana.libs.correo.CorreoVentas;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.recurso.TcConfiguraciones;

public class GestorCorreo implements Serializable{

	private static final long serialVersionUID = 7329374146353200583L;
	private String consecutivo;
	private String mails;
	private String file;

	public GestorCorreo(String consecutivo, String mails, String file) {
		this.consecutivo= consecutivo;
		this.mails      = mails;
		this.file       = file;
	} // GestorCorreo	
	
	public void doSendMail(){
		Correo buildCorreo= null;
		try {
			buildCorreo= buildMail();
			buildCorreo.enviarConAdjunto(new ByteArrayOutputStream(), this.file, EFormatos.PDF);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // doSendMail	
	
	private Correo buildMail(){
		Correo regresar         = null;
		Authenticator autenticar= null;
		String destinatario     = null;
		try {
			destinatario= this.mails.substring(0, this.mails.length()-2);
			autenticar= new CorreoVentas();
			regresar= new mx.org.kaana.libs.correo.Correo(autenticar, TcConfiguraciones.getInstance().getPropiedad("correo.mantic.ventas.cuenta"), destinatario, toAsunto());
			regresar.setContenido(new StringBuilder(Cadena.tranformaAcentoPorHtmlAcute(toContenido().toString())));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // buildMail
	
	private String toAsunto(){
		String regresar= null;		
		try {			
			regresar= "Orden de compra ".concat(this.consecutivo);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toAsunto
	
	private StringBuilder toContenido(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			regresar.append("<HTML><HEAD><META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=ISO-8859-1\"></HEAD><BODY><div ALIGN=\"CENTER\" STYLE=\"margin-bottom: 0.11in;text-align: justify;\"><FONT COLOR=\"#2f5496\"><FONT SIZE=3>");
			 regresar.append("<div ALIGN=JUSTIFY STYLE=\"margin-bottom: 0.11in;text-align: justify;\"><FONT COLOR=\"#2f5496\" size=3>");
      regresar.append("Estimado(a) <B> Proveedor");      
      regresar.append("</B></FONT>:</div>");

			regresar.append("<div    STYLE=\"margin-bottom: 0.11in;text-align: justify;\"><FONT COLOR=\"#2f5496\" size=3>");
			regresar.append("Solicitud de órden de compra <B>N 000/00</B> <B> </B> <B> </B>") ;
			regresar.append(" para el intercambio de la información relativa a <B> </B>");
			regresar.append("</FONT></div>");
			regresar.append("<div    STYLE=\"margin-bottom: 0.11in;text-align: justify;\"><FONT COLOR=\"#2f5496\" size=3>");
			regresar.append("<B></B> ");
			regresar.append("</FONT></div>");
			
			regresar.append("</FONT></div>");
			regresar.append("<div  STYLE=\"margin-bottom: 0.11in;text-align: justify;\"><FONT  style=\"font-style:italic;\" COLOR=\"#2f5496\"><B>");
			regresar.append("");
			regresar.append("</B></FONT></div>");
			regresar.append("<BR></BR>");
			regresar.append("<BR></BR>");
			regresar.append("<div   STYLE=\"margin-bottom: 0.11in;text-align: justify;\"><FONT COLOR=\"#2f5496\" size=1>");
			regresar.append("");
			regresar.append("</FONT></div>");
			regresar.append("<div   STYLE=\"margin-bottom: 0.11in;text-align: justify;\"><FONT COLOR=\"#2f5496\" size=1>");
			regresar.append("");
			regresar.append("<B></B>; ");    
			regresar.append("");
			regresar.append("");
			regresar.append("</FONT></div>");
			regresar.append("</BODY></HTML>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toContenido		
}

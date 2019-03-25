package mx.org.kaana.mantic.correos.reglas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mx.org.kaana.libs.correo.Correo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/03/2019
 *@time 01:32:11 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Manejador implements Serializable {

	private static final long serialVersionUID=4190795152468978106L;
	private static final Log LOG=LogFactory.getLog(Manejador.class);

  private ECorreos type;
	private String from;
	private String to;
	private String subject;
	private Map<String, Object> params;
	private List<String> files;

	public Manejador(ECorreos type, String subject, Map<String, Object> params) {
		this(type, "", subject, params);
	}
	
	public Manejador(ECorreos type, String to, String subject, Map<String, Object> params) {
		this(type, Configuracion.getInstance().getPropiedadServidor("mail.user.default"), to, subject, params);
	}
	
	public Manejador(ECorreos type, String from, String to, String subject, Map<String, Object> params) {
		this.type=type;
		this.from=from;
		this.to=to;
		this.subject=subject;
		this.params=params;
	}

	public String getFrom() {
		return from;
	}

	public void setTo(String to) {
		this.to=to;
	}

	public void setParams(Map<String, Object> params) {
		this.params=params;
	}
	
	public void send(String to, Map<String, Object> params) throws Exception {
		this.setTo(to);
		this.setParams(params);
		this.send();
	}
	
	public void send() throws Exception {
		BufferedReader input= new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(this.type.getTemplate())));
		String html= input.lines().collect(Collectors.joining());
		StringBuilder content= new StringBuilder(Cadena.replaceParams(html, this.params));
		LOG.info("----------------------------------------------------------------------------------------");
		LOG.info(content.toString());
    Correo correo= new Correo(this.from, this.to, this.subject);
    correo.setContenido(content);
    correo.enviar();
	}
	
	public static void main(String ... args) {
		String info= StringEscapeUtils.escapeHtml4("Alejandro Jiménez García");
	  LOG.info(info);	
	}
	
}

package mx.org.kaana.mantic.correos.reglas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mx.org.kaana.libs.correo.Correo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.correos.enums.ECorreos;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/03/2019
 *@time 01:32:11 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Manejador {

  private ECorreos type;
	private String from;
	private String to;
	private String subject;
	private Map<String, Object> params;
	private List<String> files;

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

	public void setFrom(String from) {
		this.from=from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to=to;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params=params;
	}
	
	public void send() throws Exception {
		BufferedReader input= new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(this.type.getTemplate())));
		String html= input.lines().collect(Collectors.joining());
		StringBuilder content= new StringBuilder(Cadena.replaceParams(html, this.params));
    Correo correo= new Correo(this.to, this.from, this.subject);
    correo.setContenido(content);
    correo.enviar();
	}
	
}

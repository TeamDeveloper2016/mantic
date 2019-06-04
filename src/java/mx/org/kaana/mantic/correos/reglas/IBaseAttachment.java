package mx.org.kaana.mantic.correos.reglas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/03/2019
 *@time 01:32:11 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class IBaseAttachment extends IBaseMail implements Serializable {

	private static final long serialVersionUID=4190795152468978106L;
	private static final Log LOG=LogFactory.getLog(IBaseAttachment.class);

  private ECorreos type;
	private Map<String, Object> params;

	public IBaseAttachment(ECorreos type, String subject, Map<String, Object> params) {
		this(type, Configuracion.getInstance().getPropiedadServidor("mail.user.default"), Configuracion.getInstance().getPropiedadServidor("mail.user.default"), subject, params, Collections.EMPTY_LIST);
	}
	
	public IBaseAttachment(ECorreos type, String from, String subject, Map<String, Object> params, List<Attachment> files) {
	  this(type, from, from, subject, params, files);
	}
	
	public IBaseAttachment(ECorreos type, String from, String to, String subject, Map<String, Object> params, List<Attachment> files) {
	  this(type, from, to, null, subject, params, files);
	}
	
	public IBaseAttachment(ECorreos type, String from, String to, String copies, String subject, Map<String, Object> params, List<Attachment> files) {
    this(type, from, to, copies, subject, params, files, "");	
	}
	
	public IBaseAttachment(ECorreos type, String from, String to, String copies, String subject, Map<String, Object> params, List<Attachment> files, String alias) {
		super(from, to, copies, subject, files, alias);
		this.type=type;
		this.params=params;
	}

	public void setParams(Map<String, Object> params) {
		this.params=params;
	}
	
	public void sendTo(String to, Map<String, Object> params) throws Exception {
		this.setTo(to);
		this.setParams(params);
		this.send();
	}
	
	public void sendTo(String to) throws Exception {
		this.setTo(to);
		this.send();
	}
	
	public void send() throws Exception {
		BufferedReader input= new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(this.type.getTemplate())));
		String html= input.lines().collect(Collectors.joining());
		StringBuilder content= new StringBuilder(Cadena.replaceHtml(html, this.params));
		LOG.info("----------------------------------------------------------------------------------------");
		LOG.info(content.toString());
    this.send(content.toString());
	}
	
}

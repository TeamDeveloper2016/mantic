package mx.org.kaana.kajool.utilerias.configuracion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/03/2019
 *@time 03:30:24 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.Manejador;
import mx.org.kaana.xml.Dml;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Named(value="kajoolUtileriasConfiguracionFiltro")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

	private static final Log LOG = LogFactory.getLog(Filtro.class);
	private static final long serialVersionUID=5252498555921952323L;
	
	private Boolean xml;
	private Boolean properties;
	private Boolean correo;

	public Boolean getXml() {
		return xml;
	}

	public void setXml(Boolean xml) {
		this.xml=xml;
	}

	public Boolean getProperties() {
		return properties;
	}

	public void setProperties(Boolean properties) {
		this.properties=properties;
	}

	public Boolean getCorreo() {
		return correo;
	}

	public void setCorreo(Boolean correo) {
		this.correo=correo;
	}

  @PostConstruct
	@Override
	protected void init() {
    try {
  		this.xml= Boolean.FALSE;
  		this.properties= Boolean.FALSE;
  		this.correo= Boolean.FALSE;
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  public void doXml() {
		Dml.getInstance().reload();
		LOG.info("Se recargo el archivo de configuracion XML de forma exitosa");
		this.xml= Boolean.TRUE;
		JsfBase.addMessage("Se recargarón los archivos XML con éxito.", ETipoMensaje.INFORMACION);
	}	
	
  public void doProperties() {
		Configuracion.getInstance().reload();
		LOG.info("Se recargo el archivo de configuracion PROPIEDADES de forma exitosa");
		this.properties= Boolean.TRUE;
		JsfBase.addMessage("Se recargó el archivo de configuración con éxito.", ETipoMensaje.INFORMACION);
	}	
	
//  public void doNotificar() {
//		try {
//  		Notificar notificar= new Notificar("compras@ferreteriabonanza.com", "jimenez76@yahoo.com", "demostracion", "https://ferreteriabonanza.com/");
//	  	notificar.enviar();
//	  	LOG.info("Se envio el correo de forma exitosa");
//		  this.correo= Boolean.TRUE;
//		  JsfBase.addMessage("Se envió el correo de forma exitosa.", ETipoMensaje.INFORMACION);
//		} // try
//		catch(Exception e) {
//			Error.mensaje(e);
//			JsfBase.addMessageError(e);
//		} // catch
//	}	
//	
  public void doNotificar() {
		Map<String, Object> params= new HashMap<>();
		//String[] correos= {"jimenez76@yahoo.com", "claudio.alvarez@inegi.org.mx", "suani.vazquez@inegi.org.mx", "miguelangel.martinez@inegi.org.mx"};
		String[] correos= {"alejandro.jimenez@inegi.org.mx", "jimenez76@yahoo.com"};
		try {
	    File image     = new File(JsfBase.getApplication().getRealPath(ECorreos.NOTIFICACION.getImages().concat("invitacion.png")));
      byte[]  encoded= Base64.encodeBase64(FileUtils.readFileToByteArray(image));
			params.put("header", "...");
			params.put("footer", "...");
			params.put("empresa", StringEscapeUtils.escapeHtml4("Instituto Nacional de Estadistica y Geografía"));
			params.put("invitado", StringEscapeUtils.escapeHtml4("Alejandro Jiménez García"));
			params.put("puesto", StringEscapeUtils.escapeHtml4("Subsecretario de Obras Públicas"));
			params.put("correo", "fegem@inegi.org.mx");
      params.put("background", new String(encoded));
 	  	Manejador notificar= new Manejador(ECorreos.NOTIFICACION, "fegem@inegi.org.mx", "alejandro.jimenez@inegi.org.mx", "Invitación al evento de FEGEMS", params);
			for (String item: correos) {
				notificar.setTo(item);
  	  	LOG.info("Enviando correo a la cuenta: "+ item);
	    	notificar.send();
			} // for
		  this.correo= Boolean.TRUE;
	  	LOG.info("Se envio el correo de forma exitosa");
		  JsfBase.addMessage("Se envió el correo de forma exitosa.", ETipoMensaje.INFORMACION);
		} // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}	
	
}

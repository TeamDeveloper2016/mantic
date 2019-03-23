package mx.org.kaana.kajool.utilerias.configuracion.backing;

/**
 *@company INEGI
 *@project IKTAN (Sistema de seguimiento y control de proyectos)
 *@date 19/03/2019
 *@time 03:30:24 PM 
 *@author Alejandro Jimenez Garcia <alejandro.jimenez@inegi.org.mx>
 */

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.acceso.reglas.Notificar;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.xml.Dml;
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
	
  public void doNotificar() {
		try {
  		Notificar notificar= new Notificar("compras@ferreteriabonanza.com", "jimenez76@yahoo.com", "demostracion", "https://ferreteriabonanza.com/");
	  	notificar.enviar();
	  	LOG.info("Se envio el correo de forma exitosa");
		  this.correo= Boolean.TRUE;
		  JsfBase.addMessage("Se envió el correo de forma exitosa.", ETipoMensaje.INFORMACION);
		} // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}	
	
}

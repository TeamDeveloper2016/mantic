package mx.org.kaana.kajool.utilerias.configuracion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/03/2019
 *@time 03:30:24 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.IBaseAttachment;
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
	private Boolean configuration;
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

	public Boolean getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Boolean configuration) {
		this.configuration=configuration;
	}

  @PostConstruct
	@Override
	protected void init() {
    try {
  		this.xml= Boolean.FALSE;
  		this.properties= Boolean.FALSE;
  		this.correo= Boolean.FALSE;
  		this.configuration= Boolean.FALSE;
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
		JsfBase.addMessage("Se recargó el archivo de PROPIEDADES con éxito.", ETipoMensaje.INFORMACION);
	}	
	
  public void doConfiguration() {
		TcConfiguraciones.getInstance().reload();
		LOG.info("Se recargo el archivo de CONFIGURACIONES de forma exitosa");
		this.configuration= Boolean.TRUE;
		JsfBase.addMessage("Se recargó el archivo de CONFIGURACIONES con éxito.", ETipoMensaje.INFORMACION);
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
	private String toWriteInvitacion(String nombre, String puesto) throws MalformedURLException, IOException, IOException {
		String regresar= Constantes.RUTA_TEMPORALES.concat(Archivo.toFormatNameFile("fegems")).concat(".jpg");
		//final BufferedImage image = ImageIO.read(new File(JsfBase.getRealPath(ECorreos.FACTURACION.getImages().concat("invitacion.jpg"))));
		final BufferedImage image = ImageIO.read(new URL("https://bonanza.jvmhost.net/MANTIC/resources/janal/img/correo/invitacion.jpg"));
   	Graphics g = image.getGraphics();
    g.setColor(new Color(157, 197, 23));
		final int width= 693;
    g.setFont(g.getFont().deriveFont(33f));
    g.drawString(nombre, (int)(width/2)- (int)(g.getFontMetrics().stringWidth(nombre)/2), 450);
    g.setColor(new Color(113, 112, 111));
    g.setFont(g.getFont().deriveFont(20f));
    g.drawString(puesto, (int)(width/ 2)- (int)(g.getFontMetrics().stringWidth(puesto)/ 2), 480);
    g.dispose();	
		ImageIO.write(image, "jpg", new File(JsfBase.getRealPath(regresar)));
		return regresar;
	}
	
  public void doNotificar() {
		Map<String, Object> params= new HashMap<>();
		//String[] correos= {"jimenez76@yahoo.com", "claudio.alvarez@inegi.org.mx", "suani.vazquez@inegi.org.mx", "miguelangel.martinez@inegi.org.mx"};
		// files.add(new Attachment(ECorreos.NOTIFICACION.getImages().concat("invitacion.jpg"), Boolean.TRUE));
		// for (Attachment item: files) {
		//	 params.put(item.getId(), item.getId());
		// } // for
		String[] correos      = {"jimenez76@yahoo.com"};
		List<Attachment> files= new ArrayList<>(); 
		try {
			params.put("header", "...");
			params.put("footer", "...");
			params.put("empresa", "Instituto Nacional de Estadistica y Geografía");
			params.put("invitado", "M.C. Alejandro Jiménez García");
			params.put("puesto", "Subsecretario de Obras Públicas");
			params.put("correo", "fegem@inegi.org.mx");
			for (String item: correos) {
				String image   = this.toWriteInvitacion((String)params.get("invitado"), (String)params.get("puesto"));
				Attachment user= new Attachment(image, Boolean.TRUE);
				try {
					LOG.info("Generando invitacion personalizada: "+ image);
					params.put("invitacion", user.getId());
					files.add(user);
					IBaseAttachment notificar= new IBaseAttachment(ECorreos.FACTURACION, "fegem@inegi.org.mx", item, "Invitación al evento de FEGEMS", params, files);
					LOG.info("Enviando correo a la cuenta: "+ item);
					notificar.send();
					files.remove(user);
				} // try
				finally {
				  if(user.getFile().exists()) {
   	  	    LOG.info("Eliminando archivo temporal: "+ user.getAbsolute());
				    // user.getFile().delete();
				  } // if	
				} // finally	
			} // for
		  this.correo= Boolean.TRUE;
	  	LOG.info("Se envio el correo de forma exitosa");
		  JsfBase.addMessage("Se envió el correo de forma exitosa.", ETipoMensaje.INFORMACION);
		} // try // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}	
	
	public static void main(String ... args) throws MalformedURLException, IOException, IOException {
		final BufferedImage image = ImageIO.read(new URL("https://bonanza.jvmhost.net/MANTIC/resources/janal/img/correo/invitacion.jpg"));
   	Graphics g = image.getGraphics();
    g.setColor(new Color(157, 197, 23));
		final int width= 895;
    g.setFont(g.getFont().deriveFont(33f));
    g.drawString("Mto. Alejandro Jiménez García", (int)(width/2)- (int)(g.getFontMetrics().stringWidth("Mto. Alejandro Jiménez García")/2), 450);
    g.setColor(new Color(113, 112, 111));
    g.setFont(g.getFont().deriveFont(20f));
    g.drawString("Subprocurador de eventos especiales dentro del territorio", (int)(width/ 2)- (int)(g.getFontMetrics().stringWidth("Subprocurador de eventos especiales dentro del territorio")/ 2), 480);
    g.dispose();	
		ImageIO.write(image, "jpg", new File("d:/test.png"));
		LOG.info("Ok");
	}
	
}

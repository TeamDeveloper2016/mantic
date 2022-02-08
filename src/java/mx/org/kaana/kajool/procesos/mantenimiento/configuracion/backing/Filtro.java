package mx.org.kaana.kajool.procesos.mantenimiento.configuracion.backing;

/**
 *@company KAANA
 *@project  KAJOOL (Control system polls)
 *@date 29/11/2016
 *@time 07:31:29 PM
 *@author One Developer 2016 <one.developer@kaana.org.mx>
 */
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import mx.org.kaana.kajool.control.bean.Portal;
import mx.org.kaana.kajool.procesos.mantenimiento.contadores.reglas.Ayudas;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.Messages;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.xml.Dml;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

@Named(value="kajoolMantenimientoConfiguracionFiltro")
@javax.faces.view.ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= -832641138031197051L;
  
  @PostConstruct
  @Override
	protected void init() {
    try {			  
      this.attrs.put("cfg", false);
      this.attrs.put("sql", false);
      this.attrs.put("msg", false);
      this.attrs.put("hlp", false);
      this.attrs.put("bd", false);
      this.attrs.put("img", false);
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init
  
  public void doReloadCFG() {
    Configuracion.getInstance().reload();
    JsfBase.addMessage("Se recargó el archivo configuración kajool.properties");
    LOG.info("Se recargaron de forma manual el archivo configuración kajool.properties");
    this.attrs.put("cfg", false);
  }
  
  public void doReloadSQL() {
    Dml.getInstance().reload();
    JsfBase.addMessage("Se recargaron los archivos de configuración XML-SQL");
    LOG.info("Se recargaron de forma manual los archivos kajool.xml vistas.xml etc");
    this.attrs.put("sql", false);
  }
  
  public void doReloadMSG() {
    Messages.getInstance().reload();
    JsfBase.addMessage("Se recargó el archivo de configuración msg.properties");
    LOG.info("Se recargaron de forma manual el archivo msg.properties");
     this.attrs.put("msg", false);
  }
  
  public void doReloadHLP() {
    Ayudas.getInstance().reload();
    JsfBase.addMessage("Se recargaron los mensajes de ayuda del sistema");
    LOG.info("Se recargaron de forma manual los mensajes de ayuda del sistema");
    this.attrs.put("hlp", false);
  }

  public void doReloadBD() {
    TcConfiguraciones.getInstance().reload();
    JsfBase.addMessage("Se recargaron de la base de datos la tabla de configuracion");
    LOG.info("Se recargaron de la base de datos la tabla de configuracion");
    this.attrs.put("bd", false);
  }
  
  public void doReloadIMG() {
    Portal.getInstance().reload();
    JsfBase.addMessage("Se recargaron las imagenes del portal");
    LOG.info("Se recargaron las imagenes del portal");
    this.attrs.put("img", false);
  }
  
}

package mx.org.kaana.kajool.seguridad.init;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 09:49:40 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import javax.servlet.ServletContextEvent;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.kajool.seguridad.quartz.Especial;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public final class Loader {

  private static final Log LOG = LogFactory.getLog(Loader.class);
  private static Object mutex;
  private static Loader instance;
  private Scheduler scheduler;

  private Loader(ServletContextEvent servletContext) {
    this.start(servletContext);
  }

  static {
    mutex = new Object();
  }

  public static Loader getInstance(ServletContextEvent servletContext) {
    if (instance== null || instance.scheduler== null) {
      synchronized (mutex) {
        if (instance == null || instance.scheduler== null) {
          instance = new Loader(servletContext);
        } // if
      } // synchronized
    } // if
    return instance;
  }

  private String toParameter(ServletContextEvent servletContextEvent, String parameter) {
    return servletContextEvent.getServletContext().getInitParameter(parameter);
  }

  public void start(ServletContextEvent cfg) {
    String startOnLoad = null;
    try {
      startOnLoad = Configuracion.getInstance().getPropiedadServidor("sistema.quartz");
      if (startOnLoad.equals("true")) {
        Especial.getInstance().validate(cfg);
        Especial.getInstance().init();
        LOG.error("Quartz ha sido inicializado ...");
      } // if
			else {
        LOG.error("Scheduler has not been started. Verify configuration.properties");
        cfg.getServletContext().log("Scheduler has not been started. Verify configuration.properties.");
      } // else
    } // try
    catch (Exception e) {
      cfg.getServletContext().log("Ocurrio un error al inicializar Quartz ...");
      LOG.info("Ocurrio un error al inicializar Quartz");
      Error.mensaje(e);
    } // catch
  }

  public void shutdown() {
    try {
      if (this.scheduler != null) {
        this.scheduler.shutdown();
        LOG.info("Quartz Scheduler successful shutdown.");
      }// if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

}

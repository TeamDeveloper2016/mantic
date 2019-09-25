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
import org.quartz.impl.StdSchedulerFactory;

public final class Loader {

  private static final Log LOG = LogFactory.getLog(Loader.class);
  private static final String QUARTZ_FACTORY_KEY = "org.quartz.janal.SchedulerFactory";
  private static Object mutex;
  private static Loader instance;
  private Scheduler scheduler;

  private Loader() {
  }

  static {
    mutex = new Object();
  }

  public static Loader getInstance(ServletContextEvent servletContext) {
    if (instance == null) {
      synchronized (mutex) {
        if (instance == null) {
          instance = new Loader();
          instance.start(servletContext);
        }
      } // synchronized
    } // if
    return instance;
  }

  private String toParameter(ServletContextEvent servletContextEvent, String parameter) {
    return servletContextEvent.getServletContext().getInitParameter(parameter);
  }

  private void addParameters(ServletContextEvent cfg) throws SchedulerException {
    this.scheduler.getContext().put("pathContext", cfg.getServletContext().getRealPath("/"));
    this.scheduler.getContext().put("application", cfg.getServletContext());
    this.scheduler.getContext().put("servidor", toParameter(cfg, "servidor"));
  }

  private void start(ServletContextEvent servletContextEvent) {
    String startOnLoad = null;
    try {
      startOnLoad = Configuracion.getInstance().getPropiedadServidor("sistema.quartz");
      if (startOnLoad.equals("true")) {
        loadEspecialScheduler(servletContextEvent);
      } 
			else {
        Especial.getInstance().refreshPath(servletContextEvent);
        LOG.info("Scheduler has not been started. verify configuration.properties");
        servletContextEvent.getServletContext().log("Scheduler has not been started. verify configuration.properties.");
      } // else
    } // try
    catch (Exception e) {
      servletContextEvent.getServletContext().log("Ocurrio un error al inicializar Quartz ...");
      LOG.info("Ocurrio un error al inicializar Quartz");
      Error.mensaje(e);
    } // catch
  }

  public void loadScheduler(ServletContextEvent servletContextEvent) {
    StdSchedulerFactory factory = null;
    try {
      factory = new StdSchedulerFactory(toParameter(servletContextEvent, "quartz-config-file"));
      scheduler = factory.getScheduler();
      scheduler.start();
      addParameters(servletContextEvent);
      servletContextEvent.getServletContext().setAttribute(QUARTZ_FACTORY_KEY, factory);
      servletContextEvent.getServletContext().log("Scheduler has been started...");
    } // try
    catch (Exception e) {
      LOG.info("Ocurrio un error al inicializar Quartz");
      Error.mensaje(e);
    } // catch		
  }

  private void loadEspecialScheduler(ServletContextEvent servletContextEvent) {
    try {
      Especial.getInstance().validate(servletContextEvent);
      Especial.getInstance().init();
    } // try
    catch (Exception e) {
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

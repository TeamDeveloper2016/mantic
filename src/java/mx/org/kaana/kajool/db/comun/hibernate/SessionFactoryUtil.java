package mx.org.kaana.kajool.db.comun.hibernate;

import java.io.File;
import mx.org.kaana.libs.formato.Error;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryUtil {

  private static final Log LOG = LogFactory.getLog(SessionFactoryUtil.class);
  /**
   * The single instance of hibernate SessionFactory
   */
  private static SessionFactory sessionFactory;
  private static Object mutex;
  private static SessionFactoryUtil instance;

  /**
   * disable contructor to guaranty a single instance
   */
  private SessionFactoryUtil() {
    try {
      LOG.info("Configurando hibernate.cfg.xml");
      CfgFile cfgFile = new CfgFile();
      Configuration configure = new Configuration().configure(new File(cfgFile.getPathFile()));
      cfgFile.toBuildMetaData(configure);
      ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configure.getProperties()).build();
      sessionFactory = configure.buildSessionFactory(serviceRegistry);
      LOG.info("Configuracion se cargo a memoria de forma correcta");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  static {
    mutex = new Object();
  }

  public static SessionFactoryUtil getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new SessionFactoryUtil();        
      } //if
    } // synchronized
    return instance;
  }

  /**
   * Opens a session and will not bind it to a session context
   *
   * @return the session
   */

  public Session openSession() {
    //Interceptor interceptor= new Interceptor();
    //Session regresar = sessionFactory.withOptions().interceptor(interceptor).openSession();
    Session regresar = sessionFactory.openSession();
    //interceptor.setSession(regresar);
    return regresar;
  }

  /**
   * Returns a session from the session context. If there is no session in the context it opens a session, stores it in
   * the context and returns it. This factory is intended to be used with a hibernate.cfg.xml including the following
   * property <property
   * name="current_session_context_class">thread</property> This would return the current open session or if this does
   * not exist, will create a new session
   *
   * @return the session
   */
  public Session getCurrentSession() {
    return sessionFactory.getCurrentSession();
  }

  /**
   * closes the session factory
   */
  public static void close() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
    sessionFactory = null;
  }
}

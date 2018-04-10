package mx.org.kaana.kajool.db.comun.hibernate;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jul 3, 2012
 * @time 10:07:00 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.sql.Connection;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.fuentes.SessionFactoryDinamico;
import mx.org.kaana.libs.formato.Cadena;

public class SessionFactoryFacade {

  private static Object mutex;
  private static SessionFactoryFacade instance;
  private static String dataBase;

  static {
    mutex = new Object();
  }

  private SessionFactoryFacade() throws Exception {

  }

  public static SessionFactoryFacade getInstance() throws Exception {
    synchronized (mutex) {
      if (instance == null) {
        instance = new SessionFactoryFacade();
      }
    }
    return instance;
  } // getInstance

  /**
   * *
   * Recupera la session de hibernate en base a la fuente de Dato *
   */
  public Session getSession() throws Exception {
    return getSession(null);
  }

  public Session getSession(Long idFuenteDato) throws Exception {
    Session session = null;
    try {
      if (idFuenteDato == null || idFuenteDato.equals(-1L)) {
        session = SessionFactoryUtil.getInstance().openSession();
      } else {
        SessionFactoryDinamico.getInstance().load(idFuenteDato);
        session = SessionFactoryDinamico.getInstance().openSession(idFuenteDato);
      } // else
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return session;
  }

  public Session getSession(Long idFuenteDato, boolean properties) throws Exception {
    Session session = null;
    try {
      if (idFuenteDato == null || idFuenteDato.equals(-1L)) {
        session = SessionFactoryUtil.getInstance().openSession();
      } else {
        SessionFactoryDinamico.getInstance().load(idFuenteDato, properties);
        session = SessionFactoryDinamico.getInstance().openSession(idFuenteDato);
      } // else
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return session;
  }

  public Connection getConnection(Long idFuenteDato) throws Exception {
    Connection regresar = null;
    try {
      regresar = getConnection(getSession(idFuenteDato));
    } // try
    catch (Exception e) {
      throw e;
    } // catch 
    return regresar;
  }

  public Connection getConnection(Long idFuenteDato, boolean properties) throws Exception {
    Connection regresar = null;
    try {
      regresar = getConnection(getSession(idFuenteDato, properties));
    } // try
    catch (Exception e) {
      throw e;
    } // catch 
    return regresar;
  }

  public Connection getConnection(Session session) throws Exception {
    Connection regresar = null;
    Worker work = null;
    try {
      work = new Worker();
      session.doWork(work);
      regresar = work.getConnection();
    } // try
    catch (Exception e) {
      throw e;
    } // catch 
    return regresar;
  }

  public Connection getConnection() throws Exception {
    Connection regresar = null;
    Session session = null;
    try {
      session = SessionFactoryFacade.getInstance().getSession();
      regresar = getConnection(session);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public String getDataBase() throws Exception {
    Connection connection = null;
    try {
      if (Cadena.isVacio(this.dataBase)) {
        connection = getConnection();
        this.dataBase = connection.getMetaData().getDatabaseProductName().toUpperCase();
      }//if  
    }// try
    catch (Exception e) {
      throw e;
    }// catch
    finally {
      if (connection != null) {
        connection.close();
      }// if
      connection = null;
    }// finally
    return this.dataBase;
  }

  public boolean isOracle() throws Exception {
    return getDataBase().indexOf("ORACLE") != -1;
  }
}

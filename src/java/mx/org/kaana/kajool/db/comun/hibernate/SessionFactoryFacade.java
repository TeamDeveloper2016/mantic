package mx.org.kaana.kajool.db.comun.hibernate;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 3, 2012
 *@time 10:07:00 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.fuentes.SessionFactoryDinamico;

public class SessionFactoryFacade {

  private static Object mutex;
  private static SessionFactoryFacade instance;

  static {
    mutex = new Object();
  }

  private SessionFactoryFacade() {
  }

  public static SessionFactoryFacade getInstance() {
    synchronized(mutex) {
      if ( instance == null)
        instance = new SessionFactoryFacade();
    }
    return instance;
  } // getInstance

  /***
   * Recupera la session de hibernate en base a la fuente de Dato
   ***/
	public Session getSession() throws Exception {
    return getSession(null);
  }
	
  public Session getSession(Long idFuenteDato) throws Exception {
    Session session= null;    		
    try {
      if (idFuenteDato == null || idFuenteDato.equals(-1L))
        session= SessionFactoryUtil.getInstance().openSession();
      else {				
        SessionFactoryDinamico.getInstance().load(idFuenteDato);
        session= SessionFactoryDinamico.getInstance().openSession(idFuenteDato);
      } // else
    } // try
    catch(Exception e) {
      throw e;
    } // catch
    return session;
  }
}

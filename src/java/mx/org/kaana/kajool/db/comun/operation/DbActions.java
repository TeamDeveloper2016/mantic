package mx.org.kaana.kajool.db.comun.operation;

import java.util.ArrayList;

import mx.org.kaana.kajool.db.comun.hibernate.SessionFactoryUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import mx.org.kaana.libs.formato.Error;

public class DbActions extends ArrayList<IActions> {

	private static final long serialVersionUID=6814569720128545612L;

  public void execute() throws Exception {
    Session session     = null;
    Transaction trans   = null;
    try {
      session = SessionFactoryUtil.getInstance().openSession();
      trans =  session.beginTransaction();
      session.clear();
      for(IActions actions: this) {
        actions.ejecutar(session);
      } // end for
      trans.commit();
    } // end try
    catch(Exception e) {
      Error.mensaje(e);
      if (trans!= null) {
        trans.rollback();
      }
      throw e;
    } // end catch
    finally {
      if (session!=null) {
        session.close();
      } //  end if
    }
  }

}

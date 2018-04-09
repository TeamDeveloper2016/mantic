package mx.org.kaana.kajool.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 2, 2012
 *@time 1:53:33 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import mx.org.kaana.kajool.enums.EAccion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.hibernate.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class IBaseCall {	

  protected abstract boolean ejecutar(Session sesion, EAccion accion) throws Exception;

  protected boolean ejecutar(Session session, String name, Map<Integer, Object> params) throws Exception {
		boolean regresar                   = false;
		Connection connection              = DaoFactory.getInstance().getConnection(session);
		CallableStatement callableStatement= connection.prepareCall(name);
		callableStatement.clearParameters();
		for(Integer key: params.keySet())
			callableStatement.setObject(key, params.get(key));
		regresar= callableStatement.execute();
		return regresar;
	}
	
  public final boolean ejecutar(EAccion accion) throws Exception {
		boolean regresar                   = false;
    Session session                    = null;
    Transaction transaction            = null;
    try {
      session    = SessionFactoryUtil.getInstance().openSession();
      transaction= session.beginTransaction();
      session.clear();
			regresar= ejecutar(session, accion);
      transaction.commit();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      if(transaction != null) {
        transaction.rollback();
			}	
      throw e;
    } // catch
    finally {
      if(session != null) {
        session.close();
			}		
      transaction= null;
      session    = null;
    } // finally
		return regresar;
  }	
	
}

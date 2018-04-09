package mx.org.kaana.kajool.db.comun.operation;

import org.hibernate.Session;

public interface IActions {

  public Long ejecutar(Session session) throws Exception;

}

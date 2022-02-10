package mx.org.kaana.kajool.db.comun.dao.exception;

public class DaoInsertIdenticallyException extends Exception {

  private static final long serialVersionUID = 5668503385215086972L;

  public DaoInsertIdenticallyException(Class dao) {
    super("El registro ya existe en la base de datos ["+ dao.getName()+ "]");
  }
  
}

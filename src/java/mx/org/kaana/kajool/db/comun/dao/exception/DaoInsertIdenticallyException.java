package mx.org.kaana.kajool.db.comun.dao.exception;

public class DaoInsertIdenticallyException extends Exception {

  public DaoInsertIdenticallyException() {
    super("El registro ya existe en la base de datos.");
  }
}

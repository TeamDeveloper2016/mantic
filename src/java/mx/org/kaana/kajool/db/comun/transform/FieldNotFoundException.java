package mx.org.kaana.kajool.db.comun.transform;

public class FieldNotFoundException extends RuntimeException {

  public FieldNotFoundException(String name, String fields) {
    super("No existen ".concat(fields).concat(" estos atributos en la clase ").concat(name));
  }

}

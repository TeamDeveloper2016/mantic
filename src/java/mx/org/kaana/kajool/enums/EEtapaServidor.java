package mx.org.kaana.kajool.enums;

public enum EEtapaServidor {

  DESARROLLO,
  PRUEBAS,
  CAPACITACION,
  PRODUCCION;

  public String toLowerCase() {
    return name().toLowerCase();
  }
  public String toUpperCase() {
    return name().toUpperCase();
  }
}

package mx.org.kaana.libs.parser.funciones.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Oct 1, 2012
 *@time 10:01:40 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Celda {

  private String id;
  private String typeCelda;
  private String texto;

  public Celda() {
  }

  public Celda(String id, String typeCelda, String texto) {
    this.id        = id;
    this.typeCelda = typeCelda;
    this.texto     = texto;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setTypeCelda(String typeCelda) {
    this.typeCelda = typeCelda;
  }

  public String getTypeCelda() {
    return typeCelda;
  }

  public void setTexto(String texto) {
    this.texto = texto;
  }

  public String getTexto() {
    return texto;
  }
}


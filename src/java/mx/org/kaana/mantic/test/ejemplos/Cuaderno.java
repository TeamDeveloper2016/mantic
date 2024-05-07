package mx.org.kaana.mantic.test.ejemplos;

import java.io.Serializable;

/**
 *@company INEGI
 *@project IKTAN (Sistema de seguimiento y control de proyectos)
 *@date 6/05/2024
 *@time 09:50:42 AM 
 *@author Alejandro Jimenez Garcia <alejandro.jimenez@inegi.org.mx>
 */

public class Cuaderno implements Serializable {
  
  private static final long serialVersionUID = 1005721095717189995L;
  
  private String tipo;
  private String color;
  private String largo;
  private String ancho;

  public Cuaderno() {
    this("", "", "", "");
  }

  public Cuaderno(String tipo, String color, String largo, String ancho) {
    this.tipo = tipo;
    this.color = color;
    this.largo = largo;
    this.ancho = ancho;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getLargo() {
    return largo;
  }

  public void setLargo(String largo) {
    this.largo = largo;
  }

  public String getAncho() {
    return ancho;
  }

  public void setAncho(String ancho) {
    this.ancho = ancho;
  }

  @Override
  public String toString() {
    return "Cuaderno{" + "tipo=" + tipo + ", color=" + color + ", largo=" + largo + ", ancho=" + ancho + '}';
  }

}

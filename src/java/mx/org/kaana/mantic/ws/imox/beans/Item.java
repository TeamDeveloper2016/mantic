package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.libs.formato.Fecha;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/03/2024
 * @time 20:06:20 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Item implements Serializable {

  private static final long serialVersionUID = 8305083903727049346L;

  private Long a; // idProducto;
  private Double b; // cantidad;
  private String c; // codigo;
  private String d; // descripcion;
  private String e; // registro;

  public Item() {
  }

  public Item(Long a, Double b, String c, String d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = Fecha.toRegistro();
  }

  public Long getA() {
    return a;
  }

  public void setA(Long a) {
    this.a = a;
  }

  public Double getB() {
    return b;
  }

  public void setB(Double b) {
    this.b = b;
  }

  public String getC() {
    return c;
  }

  public void setC(String c) {
    this.c = c;
  }

  public String getD() {
    return d;
  }

  public void setD(String d) {
    this.d = d;
  }

  public String getE() {
    return e;
  }

  public void setE(String e) {
    this.e = e;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.a);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) 
      return true;
    if (obj == null) 
      return false;
    if (getClass() != obj.getClass()) 
      return false;
    final Item other = (Item) obj;
    if (!Objects.equals(this.a, other.a)) 
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Item{" + "a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + '}';
  }
  
}

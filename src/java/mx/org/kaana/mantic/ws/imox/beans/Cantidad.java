package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 15/01/2024
 * @time 11:54:22 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Cantidad implements Serializable {

  private static final long serialVersionUID = 8305083903727049346L;

  private Long idProducto;
  private Double cantidad;
  private String descripcion;
  private Timestamp registro;

  public Cantidad() {
  }

  public Cantidad(Long idProducto, Double cantidad, String descripcion) {
    this.idProducto = idProducto;
    this.cantidad = cantidad;
    this.descripcion = descripcion;
    this.registro = new Timestamp(Calendar.getInstance().getTimeInMillis());
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 31 * hash + Objects.hashCode(this.idProducto);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Cantidad other = (Cantidad) obj;
    if (!Objects.equals(this.idProducto, other.idProducto)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Cantidad{" + "idProducto=" + idProducto + ", cantidad=" + cantidad + ", descripcion=" + descripcion + ", registro=" + registro + '}';
  }
  
}

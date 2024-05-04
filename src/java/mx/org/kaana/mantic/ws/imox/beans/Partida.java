package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.libs.formato.Fecha;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 04/05/2024
 * @time 14:00:10 PM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Partida implements Serializable {

  private static final long serialVersionUID = 8449185207269928081L;

  private Long idProducto;
  private String nombre;
  private Long idAlmacen;
  private String codigo;
  private double min;
  private double max;
  private double sugerido;
  private double stock;
  private String registro;

  public Partida() {
    this(-1L, null, -1L, null, 0D, 0D, 1D, 1D);
  }

  public Partida(Long idProducto, String nombre, Long idAlmacen, String codigo, double min, double max, double sugerido, double stock) {
    this.idProducto = idProducto;
    this.nombre = nombre;
    this.idAlmacen = idAlmacen;
    this.codigo = codigo;
    this.min = min;
    this.max = max;
    this.sugerido = sugerido;
    this.stock = stock;
    this.registro = Fecha.toRegistro();
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  public double getMax() {
    return max;
  }

  public void setMax(double max) {
    this.max = max;
  }

  public double getSugerido() {
    return sugerido;
  }

  public void setSugerido(double sugerido) {
    this.sugerido = sugerido;
  }

  public double getStock() {
    return stock;
  }

  public void setStock(double stock) {
    this.stock = stock;
  }

  public String getRegistro() {
    return registro;
  }

  public void setRegistro(String registro) {
    this.registro = registro;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.idProducto);
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
    final Partida other = (Partida) obj;
    if (!Objects.equals(this.idProducto, other.idProducto)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Partida{" + "idProducto=" + idProducto + ", nombre=" + nombre + ", idAlmacen=" + idAlmacen + ", codigo=" + codigo + ", min=" + min + ", max=" + max + ", sugerido=" + sugerido + ", stock=" + stock + ", registro=" + registro + '}';
  }

}

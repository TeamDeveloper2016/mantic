package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.libs.formato.Fecha;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 25/01/2024
 * @time 14:47:10 PM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Inventario implements Serializable {

  private static final long serialVersionUID = 8449185207269928086L;

  private Long idProducto;
  private Long idAlmacen;
  private Long idUbicacion;
  private String registro;

  public Inventario() {
  }

  public Inventario(Long idProducto, Long idAlmacen, Long idUbicacion, String codigos) {
    this.idProducto = idProducto;
    this.idAlmacen = idAlmacen;
    this.idUbicacion = idUbicacion;
    this.registro = Fecha.toRegistro();
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdUbicacion() {
    return idUbicacion;
  }

  public void setIdUbicacion(Long idUbicacion) {
    this.idUbicacion = idUbicacion;
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
    final Inventario other = (Inventario) obj;
    if (!Objects.equals(this.idProducto, other.idProducto)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Producto{" + "idProducto=" + idProducto + ", idAlmacen=" + idAlmacen + ", idUbicacion=" + idUbicacion + ", registro=" + registro + '}';
  }
  
}

package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.libs.formato.Fecha;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 15/01/2024
 * @time 11:42:10 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Producto implements Serializable {

  private static final long serialVersionUID = 8449185207269928086L;

  private Long idProducto;
  private String nombre;
  private Long idAlmacen;
  private Long idUbicacion;
  private String codigos;
  private String registro;

  public Producto() {
  }

  public Producto(Long idProducto, String nombre, Long idAlmacen, Long idUbicacion, String codigos) {
    this.idProducto = idProducto;
    this.nombre = nombre;
    this.idAlmacen = idAlmacen;
    this.idUbicacion = idUbicacion;
    this.codigos = codigos;
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

  public Long getIdUbicacion() {
    return idUbicacion;
  }

  public void setIdUbicacion(Long idUbicacion) {
    this.idUbicacion = idUbicacion;
  }

  public String getCodigos() {
    return codigos;
  }

  public void setCodigos(String codigos) {
    this.codigos = codigos;
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
    final Producto other = (Producto) obj;
    if (!Objects.equals(this.idProducto, other.idProducto)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Producto{" + "idProducto=" + idProducto + ", nombre=" + nombre + ", idAlmacen=" + idAlmacen + ", idUbicacion=" + idUbicacion + ", codigos=" + codigos + ", registro=" + registro + '}';
  }
  
}

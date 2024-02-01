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

public class Articulo implements Serializable {

  private static final long serialVersionUID = 8449185207269928086L;

  private Long idProducto;
  private String nombre;
  private String codigos;
  private Long idActivo;
  private String registro;

  public Articulo() {
  }

  public Articulo(Long idProducto, String nombre, String codigos, Long idActivo) {
    this.idProducto = idProducto;
    this.nombre = nombre;
    this.codigos = codigos;
    this.idActivo= idActivo;
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

  public String getCodigos() {
    return codigos;
  }

  public void setCodigos(String codigos) {
    this.codigos = codigos;
  }

  public Long getIdActivo() {
    return idActivo;
  }

  public void setIdActivo(Long idActivo) {
    this.idActivo = idActivo;
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
    final Articulo other = (Articulo) obj;
    if (!Objects.equals(this.idProducto, other.idProducto)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Articulo{" + "idProducto=" + idProducto + ", nombre=" + nombre + ", codigos=" + codigos + ", idActivo=" + idActivo + ", registro=" + registro + '}';
  }
  
}

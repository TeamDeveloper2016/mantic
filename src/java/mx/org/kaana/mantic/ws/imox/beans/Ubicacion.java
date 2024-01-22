package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.libs.formato.Fecha;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 15/01/2024
 * @time 11:37:08 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Ubicacion implements Serializable {

  private static final long serialVersionUID = 7827342035308515585L;

  private Long idAlmacen;
  private Long idUbicacion;
  private String nombre;
  private String registro;

  public Ubicacion() {
  }

  public Ubicacion(Long idAlmacen, Long idUbicacion, String nombre) {
    this.idAlmacen = idAlmacen;
    this.idUbicacion = idUbicacion;
    this.nombre = nombre;
    this.registro = Fecha.toRegistro();
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

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
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
    hash = 97 * hash + Objects.hashCode(this.idAlmacen);
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
    final Ubicacion other = (Ubicacion) obj;
    if (!Objects.equals(this.idAlmacen, other.idAlmacen)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Ubicacion{" + "idAlmacen=" + idAlmacen + ", idUbicacion=" + idUbicacion + ", nombre=" + nombre + ", registro=" + registro + '}';
  }
  
 }

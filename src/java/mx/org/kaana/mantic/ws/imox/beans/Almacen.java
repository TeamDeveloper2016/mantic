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

public class Almacen implements Serializable {

  private static final long serialVersionUID = 7827342035308515585L;

  private Long idEmpresa;
  private Long idAlmacen;
  private Long idPrincipal;
  private String nombre;
  private String registro;

  public Almacen() {
  }

  public Almacen(Long idEmpresa, Long idAlmacen, Long idPrincipal, String nombre) {
    this.idEmpresa = idEmpresa;
    this.idAlmacen = idAlmacen;
    this.idPrincipal = idPrincipal;
    this.nombre = nombre;
    this.registro = Fecha.toRegistro();
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdPrincipal() {
    return idPrincipal;
  }

  public void setIdPrincipal(Long idPrincipal) {
    this.idPrincipal = idPrincipal;
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
    hash = 71 * hash + Objects.hashCode(this.idAlmacen);
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
    final Almacen other = (Almacen) obj;
    if (!Objects.equals(this.idAlmacen, other.idAlmacen)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Almacen{" + "idEmpresa=" + idEmpresa + ", idAlmacen=" + idAlmacen + ", idPrincipal=" + idPrincipal + ", nombre=" + nombre + ", registro=" + registro + '}';
  }

 }

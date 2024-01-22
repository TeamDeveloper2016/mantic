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

public class Empresa implements Serializable {

  private static final long serialVersionUID = 7827342035308515585L;

  private Long idEmpresa;
  private Long idTipo;
  private String nombre;
  private String registro;

  public Empresa() {
  }

  public Empresa(Long idEmpresa, String nombre) {
    this.idEmpresa = idEmpresa;
    this.nombre = nombre;
    this.registro = Fecha.toRegistro();
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdTipo() {
    return idTipo;
  }

  public void setIdTipo(Long idTipo) {
    this.idTipo = idTipo;
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
    hash = 29 * hash + Objects.hashCode(this.idEmpresa);
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
    final Empresa other = (Empresa) obj;
    if (!Objects.equals(this.idEmpresa, other.idEmpresa)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Empresa{" + "idEmpresa=" + idEmpresa + ", idTipo=" + idTipo + ", nombre=" + nombre + ", registro=" + registro + '}';
  }
  
}

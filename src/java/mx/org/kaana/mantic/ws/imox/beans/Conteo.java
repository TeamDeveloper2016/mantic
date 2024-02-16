package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 16/01/2024
 * @time 12:06:20 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Conteo implements Serializable {

  private static final long serialVersionUID = -8330660760447754833L;

  private Long idConteo;
  private Long idEmpresa;
  private Long idAlmacen;
  private Long idUsuario;
  private String nombre;
  private List<Cantidad> productos;
  private String semilla;
  private String registro;

  public Conteo() {
  }

  public Conteo(Long idConteo, Long idUsuario, String nombre, String registro, Long idEmpresa, Long idAlmacen, String semilla) {
    this.idConteo = idConteo;
    this.idConteo = idEmpresa;
    this.idConteo = idAlmacen;
    this.idUsuario = idUsuario;
    this.nombre = nombre;
    this.productos = new ArrayList<>();
    this.semilla = semilla;
    this.registro = registro;
  }

  public Long getIdConteo() {
    return idConteo;
  }

  public void setIdConteo(Long idConteo) {
    this.idConteo = idConteo;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<Cantidad> getProductos() {
    return productos;
  }

  public void setProductos(List<Cantidad> productos) {
    this.productos = productos;
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

  public String getSemilla() {
    return semilla;
  }

  public void setSemilla(String semilla) {
    this.semilla = semilla;
  }

  public String getRegistro() {
    return registro;
  }

  public void setRegistro(String registro) {
    this.registro = registro;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 71 * hash + Objects.hashCode(this.idConteo);
    hash = 71 * hash + Objects.hashCode(this.idUsuario);
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
    final Conteo other = (Conteo) obj;
    if (!Objects.equals(this.idConteo, other.idConteo)) {
      return false;
    }
    if (!Objects.equals(this.idUsuario, other.idUsuario)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Conteo{" + "idConteo=" + idConteo + ", idEmpresa=" + idEmpresa + ", idAlmacen=" + idAlmacen + ", idUsuario=" + idUsuario + ", nombre=" + nombre + ", productos=" + productos + ", semilla=" + semilla + ", registro=" + registro + '}';
  }
  
}

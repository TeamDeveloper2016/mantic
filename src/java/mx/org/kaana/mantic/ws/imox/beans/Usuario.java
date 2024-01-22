package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.libs.formato.Fecha;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 15/01/2024
 * @time 11:15:47 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Usuario implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private Long idEmpresa;
  private Long idUsuario;
  private String nombre;
  private String cuenta;
  private String contrasenia;
  private Long idPerfil;
  private String registro;

  public Usuario() {
  }

  public Usuario(Long idEmpresa, Long idUsuario, String nombre, String cuenta, String contrasenia, Long idPerfil) {
    this.idEmpresa = idEmpresa;
    this.idUsuario = idUsuario;
    this.nombre = nombre;
    this.cuenta = cuenta;
    this.contrasenia = contrasenia;
    this.idPerfil = idPerfil;
    this.registro= Fecha.toRegistro();
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
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

  public String getCuenta() {
    return cuenta;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  public String getContrasenia() {
    return contrasenia;
  }

  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setIdPerfil(Long idPerfil) {
    this.idPerfil = idPerfil;
  }

  public String getRegistro() {
    return registro;
  }

  public void setRegistro(String registro) {
    this.registro = registro;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.idUsuario);
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
    final Usuario other = (Usuario) obj;
    if (!Objects.equals(this.idUsuario, other.idUsuario)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Usuario{" + "idEmpresa=" + idEmpresa + ", idUsuario=" + idUsuario + ", nombre=" + nombre + ", cuenta=" + cuenta + ", contrasenia=" + contrasenia + ", idPerfil=" + idPerfil + ", registro=" + registro + '}';
  }
    
}

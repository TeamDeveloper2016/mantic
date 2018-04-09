package mx.org.kaana.kajool.procesos.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/10/2016
 *@time 11:09:35 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Perfil implements Serializable{

  private static final long serialVersionUID = 7577176228954087582L;
  private Long idPerfil;
  private Integer total;
  private String descripcion;

  public Perfil(Long idPerfil, String descripcion) {
    this(idPerfil, descripcion, 1);
  } // Perfil

  public Perfil(Long idPerfil, String descripcion, Integer total) {
    this.idPerfil   = idPerfil;
    this.descripcion= descripcion;
    this.total      = total;
  } // Perfil

  public Long getIdPerfil() {
    return idPerfil;
  }

  public Integer getTotal() {
    return total;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void add() {
   ++total;
  } // add

  public void remain() {
   --total;
  } // remain

  @Override
  public int hashCode() {
    int hash=3;
    hash=59*hash+(this.idPerfil!= null ? this.idPerfil.hashCode() : 0);
    return hash;
  } // hashCode

  @Override
  public boolean equals(Object obj) {
    if (obj==null)
      return false;
    if (getClass()!=obj.getClass())
      return false;
    final Perfil other=(Perfil) obj;
    if (this.idPerfil != other.idPerfil && (this.idPerfil== null || !this.idPerfil.equals(other.idPerfil)))
      return false;
    return true;
  } // equals
}

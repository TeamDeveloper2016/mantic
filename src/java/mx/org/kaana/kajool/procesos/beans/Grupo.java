package mx.org.kaana.kajool.procesos.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/10/2016
 *@time 11:08:48 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Grupo implements Serializable{

  private static final long serialVersionUID = 3598149223996343106L;
  private Long idGrupo;
  private String descripcion;
  private Map<Long,Perfil> perfiles;

  public Grupo(Long idGrupo, String descripcion) {
    this.idGrupo    = idGrupo;
    this.descripcion= descripcion;
    this.perfiles   = new HashMap<>();
  } // Grupo

  public Long getIdGrupo() {
    return idGrupo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Map<Long, Perfil> getPerfiles() {
    return perfiles;
  }

  public void addPerfil (Long idPerfil, String descripcion) {
    if (getPerfiles().containsKey(idPerfil))
      getPerfiles().get(idPerfil).add();
    else
      getPerfiles().put(idPerfil, new Perfil(idPerfil, descripcion));
  } // addPerfil

  public Long tototalPerfiles() {
    return ((Number)getPerfiles().size()).longValue();
  } // tototalPerfiles

  public void remainUsuarioPerfil (Long idPerfil) {
    if (getPerfiles().containsKey(idPerfil)) {
      getPerfiles().get(idPerfil).remain();
      if (getPerfiles().get(idPerfil).getTotal().equals(0))
        getPerfiles().remove(idPerfil);
    } // if
  } // remainUsuarioperfil
}

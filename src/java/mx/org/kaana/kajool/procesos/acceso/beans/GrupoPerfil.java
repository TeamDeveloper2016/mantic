package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 4/09/2015
 *@time 03:02:32 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class GrupoPerfil implements Serializable {

  private static final long serialVersionUID = 4304938302447327468L;

  private Long idGrupo;
  private Long idPerfil;

  public GrupoPerfil () {
     this(null,null);
  }

  public GrupoPerfil (Long idGrupo, Long idPerfil) {
    this.idGrupo = idGrupo;
    this.idPerfil = idPerfil;
  }

  @Override
  public String toString() {
    StringBuilder regresar = new StringBuilder();
    regresar.append("[");
    regresar.append(idGrupo);
    regresar.append(",");
    regresar.append(idPerfil);
    regresar.append("]");
    return regresar.toString();
  }
}

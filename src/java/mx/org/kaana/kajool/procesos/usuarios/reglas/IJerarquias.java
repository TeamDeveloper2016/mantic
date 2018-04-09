package mx.org.kaana.kajool.procesos.usuarios.reglas;

import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 3/09/2015
 *@time 04:01:11 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public interface IJerarquias {

  public void insertJerarquia(Session session, Long idSupervisa, Long idUsuario) throws Exception;
}

package mx.org.kaana.libs.correo;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.libs.reflection.Methods;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Oct 10, 2012
 *@time 9:59:31 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class CorreoPersona implements Serializable {

  private static final long serialVersionUID=5435878865932721894L;

  private List<? extends IEmpleado> empleados;
  private String usuario;
  private String password;

  public CorreoPersona(List<? extends IEmpleado> empleados, String usuario, String password) {
    this.empleados = empleados;
    this.usuario   = usuario;
    this.password  = password;
  }

  public CorreoPersona(List<? extends IEmpleado> empleados) {
    this(empleados, null, null);
    obtenerUsuarioPass();
  }

  private void obtenerUsuarioPass() {
    Encriptar encriptar = new Encriptar();
    this.usuario  =TcConfiguraciones.getInstance().getPropiedad("correo.user");
    this.password =encriptar.desencriptar(TcConfiguraciones.getInstance().getPropiedad("correo.pass"),Encriptar._CLAVE);
  }



  public void finalize() {
    Methods.clean(empleados);
  }

}

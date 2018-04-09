package mx.org.kaana.kajool.procesos.usuarios.reglas;

import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 3/09/2015
 *@time 04:05:52 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Cuenta {

  private static final Log LOG= LogFactory.getLog(Cuenta.class);
	
	private String login;

  public Cuenta(Long numeroEmpleado) {
    init(numeroEmpleado.toString());
  }

  public Cuenta(String numeroEmpleado) {
    init(numeroEmpleado);
  }

  private void init(String numeroEmpleado) {
    /*Directorio directorio= null;
    try {
      directorio= new Directorio();
      String usr= directorio.toLogin(numeroEmpleado);
      setLogin(usr!= null? usr.toLowerCase(): usr);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      directorio= null;
    } // finally*/
  } // init

  public String getContrasenia() {
    return Configuracion.getInstance().getPropiedad(Configuracion.getInstance().getEtapaServidor().toLowerCase().concat(".usuario.pass"));
  }

  public void setLogin(String login) {
    this.login = login;
  }

	public String getLogin() {
		return login;
	}
  
}

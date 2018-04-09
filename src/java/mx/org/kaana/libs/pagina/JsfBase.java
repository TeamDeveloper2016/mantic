package mx.org.kaana.libs.pagina;

import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import static mx.org.kaana.libs.pagina.JsfUtilities.getSession;
import mx.org.kaana.kajool.procesos.beans.UsuariosEnLinea;
import mx.org.kaana.kajool.seguridad.filters.control.LockUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/08/2015
 *@time 12:04:03 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class JsfBase extends JsfUtilities {

  private static final Log LOG              = LogFactory.getLog(JsfBase.class);	
	private static final String ADMIN         = "ADMINISTRADOR";	
	private static final String ADMIN_ENCUESTA= "ADMINISTRADORDEENCUESTA";	

  public static Autentifica getAutentifica() {
  	return (Autentifica)getSession().getAttribute(Constantes.ATRIBUTO_AUTENTIFICA);
  } // getAutentifica

  public static Long getIdUsuario() {
  	return getAutentifica().getEmpleado().getIdUsuario();
  } // getIdUsuario

	public static boolean isAdmin() throws Exception{
		boolean regresar= false;				
		try {
			regresar = Cadena.eliminaCaracter(getAutentifica().getEmpleado().getDescripcionPerfil(), ' ').toUpperCase().equals(ADMIN);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // isAdmin

	public static boolean isAdminEncuesta() throws Exception{
		boolean regresar= false;				
		try {
			regresar = Cadena.eliminaCaracter(getAutentifica().getEmpleado().getDescripcionPerfil(), ' ').toUpperCase().equals(ADMIN_ENCUESTA);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // isAdminEncuesta

	public static boolean isAdminEncuestaOrAdmin() throws Exception{
		boolean regresar= false;				
		try {
			regresar = isAdmin() || isAdminEncuesta();
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // isAdminEncuestaOrAdmin
	
	public static boolean isDirectivo(){
		return getAutentifica().getEmpleado().getDescripcionPerfil().toUpperCase().equals("DIRECTOR");
	} // idDirectivo

  public static UsuariosEnLinea getUsuariosSitio() {
  	return (UsuariosEnLinea)getApplication().getAttribute(Constantes.ATRIBUTO_USUARIOS_SITIO);
  } // getUsuariosSitio

  public static LockUser toLockUsers() {
    return (LockUser)getApplication().getAttribute(Constantes.ATRIBUTO_BLOQUEO_USUARIOS);
  }
  
  public static boolean isLockUsers(LockUser lockUser, Autentifica autentifica) {
    return lockUser.isLock() && !Cadena.eliminaCaracter(autentifica.getEmpleado().getDescripcionPerfil(), ' ').toUpperCase().equals(JsfBase.ADMIN_ENCUESTA);
  }

  public static boolean isLockUsers() {
    return isLockUsers(toLockUsers(), getAutentifica());
  }
  
  
}

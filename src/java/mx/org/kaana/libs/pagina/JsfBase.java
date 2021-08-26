package mx.org.kaana.libs.pagina;

import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.kajool.procesos.acceso.beans.UsuarioMenu;
import static mx.org.kaana.libs.pagina.JsfUtilities.getSession;
import mx.org.kaana.kajool.procesos.beans.UsuariosEnLinea;
import mx.org.kaana.kajool.seguridad.filters.control.LockUser;
import mx.org.kaana.kajool.template.backing.Reporte;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:04:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class JsfBase extends JsfUtilities {

  private static final Log LOG       = LogFactory.getLog(JsfBase.class);

  public static Autentifica getAutentifica() {
    return (Autentifica) getSession().getAttribute(Constantes.ATRIBUTO_AUTENTIFICA);
  } // getAutentifica

  public static Long getIdUsuario() {
    return getAutentifica().getPersona().getIdUsuario();
  } // getIdUsuario

  public static boolean isAdmin() throws Exception {
    boolean regresar = false;
    try {
			String perfil= getAutentifica()== null? "": Cadena.eliminaCaracter(getAutentifica().getPersona().getDescripcionPerfil(), ' ').toUpperCase();
      regresar = perfil.equals(Constantes.ADMIN) || perfil.equals(Constantes.ADMINS) || perfil.equals(Constantes.ADMINISTRATIVO) || perfil.equals(Constantes.GERENTE);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // isAdmin

  public static boolean isGerente() throws Exception {
    boolean regresar = false;
    try {
      String perfil= getAutentifica()== null? "": Cadena.eliminaCaracter(getAutentifica().getPersona().getDescripcionPerfil(), ' ').toUpperCase();
      regresar = perfil.equals(Constantes.GERENTE) || perfil.equals(Constantes.ADMINISTRATIVO);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // isAdminEncuesta

  public static boolean isCajero() throws Exception {
    boolean regresar = false;
    try {
      regresar = Cadena.eliminaCaracter(getAutentifica().getPersona().getDescripcionPerfil(), ' ').toUpperCase().equals(Constantes.CAJERO);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // isAdminEncuesta

  public static boolean isAdminEncuestaOrAdmin() throws Exception {
    boolean regresar = false;
    try {
      regresar = isAdmin();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // isAdminEncuestaOrAdmin

  public static boolean isDirectivo() {
    return getAutentifica().getPersona().getDescripcionPerfil().toUpperCase().equals("DIRECTOR");
  } // idDirectivo

  public static UsuariosEnLinea getUsuariosSitio() {
    return (UsuariosEnLinea) getApplication().getAttribute(Constantes.ATRIBUTO_USUARIOS_SITIO);
  } // getUsuariosSitio

  public static LockUser toLockUsers() {
    return (LockUser) getApplication().getAttribute(Constantes.ATRIBUTO_BLOQUEO_USUARIOS);
  }

  public static boolean isLockUsers(LockUser lockUser, Autentifica autentifica) {
    String perfil= autentifica== null? "": Cadena.eliminaCaracter(autentifica.getPersona().getDescripcionPerfil(), ' ').toUpperCase();
    return lockUser.isLock() && !perfil.equals(Constantes.GERENTE) && !perfil.equals(Constantes.ADMINISTRATIVO);
  }

  public static boolean isLockUsers() {
    return isLockUsers(toLockUsers(), getAutentifica());
  }

	public static Reporte toReporte () {
    return (Reporte) getFacesContext().getViewRoot().getViewMap().get("kajoolTemplateReporte");
  }
	
	public static String getCodigoModulo() {
	  String regresar= "";
		try {
			String rama= getParametro("opcionRama");
			if(!Cadena.isVacio(rama)) {
				for (UsuarioMenu modulo: getAutentifica().getModulos()) {
					if(modulo.getClave().equals(rama) && !Cadena.isVacio(modulo.getCodigo()))
						regresar= " ["+ modulo.getCodigo()+ "]";
					// LOG.info("módulo: "+ modulo.getClave()+ " "+ modulo.getDescripcion());
				} // if
			  if(Cadena.isVacio(regresar))
				  for (UsuarioMenu modulo: getAutentifica().getTopModulos()) {
					  if(modulo.getClave().equals(rama) && !Cadena.isVacio(modulo.getCodigo()))
						  regresar= " ["+ modulo.getCodigo()+ "]";
					  // LOG.info("módulo: "+ modulo.getClave()+ " "+ modulo.getDescripcion());
				  } // for	
			} // if	
		} // try
		catch(Exception e) {
			regresar= "";
		} // catch
		return regresar;
	}

  public static void toForwardMovil(String page) {
    try {
      if(true || JsfBase.getBrowser().isMobile()) {
        JsfBase.getResponse().sendRedirect(page.concat(Constantes.REDIRECIONAR));
        // RequestDispatcher navigation = JsfBase.getRequest().getRequestDispatcher(page.concat(Constantes.REDIRECIONAR));
        // navigation.forward(JsfBase.getRequest(), JsfBase.getResponse());
      } // if  
		} // try
	  catch (Exception e) {
			addMessageError(e);
    } // catch   
  } 
  
}

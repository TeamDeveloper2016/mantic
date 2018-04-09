package mx.org.kaana.kajool.procesos.acceso.exclusiones;

import java.util.Enumeration;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 31, 2015
 *@time 11:15:53 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@ManagedBean(name="kajoolAccesoInvalidar")
@ViewScoped
public class Invalidar {

	public void clean() {
    Autentifica autentifica  = null;
    Enumeration attributes   = null;
    HttpSession session      = null;
    try {
      session    = JsfBase.getSession();
      autentifica= JsfBase.getAutentifica();
      if (autentifica!=null) {
        JsfBase.getUsuariosSitio().deleteCuenta(session.getId(), autentifica.getEmpleado().getCuenta());
        //autentifica.cerrarSession();
        attributes=session.getAttributeNames();
        String elemento=null;
        while (attributes.hasMoreElements()) {
          elemento=attributes.nextElement().toString();
          try {
            if (elemento!=null)
              session.removeAttribute(elemento);
          } // try
          catch (Exception ex) {
            Error.mensaje(ex);
          } // catch
        } // while
      } // if
      session.setMaxInactiveInterval(0);
      FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
 }
	
}

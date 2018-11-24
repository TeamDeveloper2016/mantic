package mx.org.kaana.kajool.procesos.acceso.exclusiones;

import java.io.Serializable;
import java.util.Enumeration;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.libs.Constantes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Aug 31, 2015
 * @time 11:15:53 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@Named(value = "kajoolAccesoInvalidar")
@ViewScoped
public class Invalidar implements Serializable {
	
	private static final Log LOG=LogFactory.getLog(Invalidar.class);
	private static final long serialVersionUID=-5710731965721539429L;

  public void clean() {
    Autentifica autentifica= null;
    Enumeration attributes = null;
    HttpSession session    = null;
    try {
      session = JsfBase.getSession();
      autentifica = JsfBase.getAutentifica();
      if (autentifica != null) {
				LOG.warn("Se cerró la sesion: "+ autentifica.getPersona().getCuenta());
        JsfBase.getUsuariosSitio().deleteCuenta(session.getId(), autentifica.getPersona().getCuenta());
        //autentifica.cerrarSession();
        attributes = session.getAttributeNames();
        String elemento = null;
        while (attributes.hasMoreElements()) {
          elemento = attributes.nextElement().toString();
          try {
            if (elemento != null) {
              session.removeAttribute(elemento);
            }
          } // try
          catch (Exception ex) {
            Error.mensaje(ex);
          } // catch
        } // while
      } // if
			session.removeAttribute(Constantes.ATRIBUTO_AUTENTIFICA);
      session.setMaxInactiveInterval(0);
      FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			LOG.warn("Eliminando el objeto del autentifica de la sesión: "+ session.getId());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

}

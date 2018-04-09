package mx.org.kaana.kajool.seguridad.listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.kajool.procesos.acceso.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.beans.UsuariosEnLinea;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@WebListener("Monitoreo de sesiones del sitio web")
public class SessionListener implements HttpSessionListener {

  private static final Log LOG   = LogFactory.getLog(SessionListener.class);
  private int currentSessionCount= 0;
  private int maxSessionCount    = 0;

  private void loadUser(HttpSession session) throws Exception {
    String password = BouncyEncryption.decrypt(Configuracion.getInstance().getPropiedad("sistema.autenticar.contrasenia"));
    Autentifica autentifica = new Autentifica();
    if (autentifica.tieneAccesoBD(Configuracion.getInstance().getPropiedad("sistema.autenticar.cuenta"), password, "127.0.0.1")) {
      LOG.warn("Acceso libre con autentifica [".concat(autentifica.toString()).concat("]"));
      synchronized (session) {
        session.setAttribute(Constantes.ATRIBUTO_AUTENTIFICA, autentifica);
      } // synchronized
    } // if
  } // loadUser

  @Override
  public void sessionCreated(HttpSessionEvent event) {
    LOG.debug("Inciando session");
    try {
      this.currentSessionCount++;
      if (this.currentSessionCount > maxSessionCount)
        this.maxSessionCount = this.currentSessionCount;
      HttpSession session = event.getSession();
      if (session.getServletContext().getAttribute("sessionCounter") == null)
        storeInServletContext(event);
      UsuariosEnLinea usuarios= (UsuariosEnLinea) session.getServletContext().getAttribute(Constantes.ATRIBUTO_USUARIOS_SITIO);
      usuarios.add(session.getId());
      if (Configuracion.getInstance().isFreeAccess()) {
        LOG.warn("Acceso libre cargando usuario ".concat(Configuracion.getInstance().getPropiedad("sistema.autenticar.cuenta")));
        loadUser(session);
      }	// if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  } // sessionCreated

  @Override
  public void sessionDestroyed(HttpSessionEvent event) {
    HttpSession session= event.getSession();
    String sesionId    = "";
    if (session.getServletContext().getAttribute("sessionCounter") == null)
      storeInServletContext(event);
    UsuariosEnLinea usuarios= (UsuariosEnLinea) session.getServletContext().getAttribute(Constantes.ATRIBUTO_USUARIOS_SITIO);
    usuarios.delete(session.getId());
    if (!Configuracion.getInstance().isEtapaDesarrollo()) {
      sesionId = session.getId();
      try {
        LOG.info("Cerrando session-->".concat(sesionId));
        doUpdate(sesionId);
      } // try
      catch (Exception e) {
        Error.mensaje(e);
      } // catch
    } // if
    this.currentSessionCount--;
  } // sessionDestroyed

  private void storeInServletContext(HttpSessionEvent event) {
    HttpSession session= event.getSession();
    session.getServletContext().setAttribute("sessionCounter", this);
  } // storeInServletContext

  private void doUpdate(String sesionId)throws Exception {		
		Transaccion session= null;
		try {
			session = new Transaccion(sesionId);
			session.ejecutar(EAccion.COMPLEMENTAR);
		} // try
		catch(Exception e){
			throw e;
		} // catch
	} // doUpdate
}

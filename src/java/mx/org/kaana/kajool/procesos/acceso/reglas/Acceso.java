package mx.org.kaana.kajool.procesos.acceso.reglas;

import java.io.Serializable;
import java.util.Calendar;
import javax.servlet.http.HttpSession;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EPaginasPrivilegios;
import mx.org.kaana.kajool.procesos.acceso.beans.Cliente;
import mx.org.kaana.kajool.procesos.acceso.exceptions.AccesoDenegadoException;
import mx.org.kaana.kajool.procesos.acceso.exceptions.BloqueoSitioException;
import mx.org.kaana.kajool.procesos.beans.Usuario;
import mx.org.kaana.kajool.procesos.beans.UsuariosEnLinea;
import mx.org.kaana.kajool.seguridad.filters.control.LockUser;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.procesos.acceso.perfil.reglas.RegistroPerfil;

public class Acceso implements Serializable {

  private static final long serialVersionUID = 27811078704183889L;
  private static final Log LOG = LogFactory.getLog(Acceso.class);
  private Cliente cliente;

  public Acceso(Cliente cliente) {
    this.cliente = cliente;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public String toForward() throws Exception {		
    Transaccion transaccion = new Transaccion();
    transaccion.ejecutar(EAccion.AGREGAR);		
    return JsfBase.getAutentifica().redirectMenu();
  } // toForwardclean
	
  public void valida() throws Exception {
    LOG.info("Validar credenciales...");
    Autentifica autentifica= new Autentifica();		
    HttpSession session    = null;
		RegistroPerfil registro= null;
    autentifica.getCredenciales().setCuentaInicial(getCliente().getCuenta());
    LOG.info("cuenta[".concat(getCliente().getCuenta()).concat("]"));
    if (autentifica.tieneAccesoBD(getCliente().getCuenta(), getCliente().getContrasenia(), JsfBase.getRequest().getRemoteAddr())) {
      if(JsfBase.isLockUsers())
    		throw new BloqueoSitioException();
			registro= new RegistroPerfil(autentifica);
			session= JsfBase.getSession();				
			registro.addAutentifica(session);			
      agregarUsuariosSitio(session, autentifica);
			if(!autentifica.getRedirect().equals(EPaginasPrivilegios.PERFILES)){				
				registro.addMenuSesion(session);											
				registro.addTopMenuSesion(session);		
			} // if
    }//if
    else {
      if(session!= null)
        synchronized (session) {
          JsfBase.cleanSesion(session);
        } // synchronized
      throw new AccesoDenegadoException();
    } // else
  } // valida

  protected void agregarUsuariosSitio(HttpSession session, Autentifica autentifica) {		
		Usuario usuario         = null;
		UsuariosEnLinea usuarios= null;		
		usuario= new Usuario(
      autentifica.getEmpleado().getIdGrupo(),
      autentifica.getEmpleado().getIdPerfil(),
			autentifica.getEmpleado().getDescripcionPerfil(),
			session.getId(),
      autentifica.getEmpleado().getCuenta(),
      autentifica.getEmpleado().getNombreCompleto(),
      autentifica.getEmpleado().getIdEntidad(), null, 
      autentifica.getEmpleado().getEntidad(), 
      Calendar.getInstance(), 0, -1, false, null,
      autentifica.getEmpleado().getClaveGrupo());
		usuarios=(UsuariosEnLinea) JsfBase.getUsuariosSitio();
		usuarios.addCuenta(session.getId(), usuario);
	} // agregarUsuariosSitio
}

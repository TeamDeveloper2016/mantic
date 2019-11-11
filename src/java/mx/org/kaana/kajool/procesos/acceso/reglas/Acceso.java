package mx.org.kaana.kajool.procesos.acceso.reglas;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EPaginasPrivilegios;
import mx.org.kaana.kajool.procesos.acceso.beans.Cliente;
import mx.org.kaana.kajool.procesos.acceso.exceptions.AccesoDenegadoException;
import mx.org.kaana.kajool.procesos.acceso.exceptions.BloqueoSitioException;
import mx.org.kaana.kajool.procesos.beans.Usuario;
import mx.org.kaana.kajool.procesos.beans.UsuariosEnLinea;
import mx.org.kaana.libs.Constantes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.kajool.procesos.acceso.perfil.reglas.RegistroPerfil;
import mx.org.kaana.libs.reflection.Methods;

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
    String regresar           = null;
    Transaccion transaccion   = null;
    Map<String, Object> params= null;
    Value value               = null;
    String temaActivo         = null;
    try {
      params= new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "cuenta='".concat(getCliente().getCuenta()).concat("'"));
      value= DaoFactory.getInstance().toField("TcManticPersonasDto", "row", params, "rfc");
      if ((value.getData() != null) && (getCliente().getContrasenia().equals(value.toString().substring(0, 10)))) {
        regresar = "/Exclusiones/confirmacion.jsf".concat(Constantes.REDIRECIONAR);
      } // if
      else {
        temaActivo = JsfBase.getAutentifica().getPersona().getEstilo();
        transaccion = new Transaccion();
        transaccion.ejecutar(EAccion.AGREGAR);
        this.cliente.setTemaActivo( temaActivo != null ? temaActivo : Constantes.TEMA_INICIAL);        
				// ** verificar si no recupera pagaina default colocar una y mandar al log que no existe esa opcion del menu asociada al perfil
        regresar =  JsfBase.getAutentifica().redirectMenu();
      } // else    
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    }// finally
   
    return regresar;
  } // toForwardclean

  public void valida() throws Exception {
    LOG.info("Validar credenciales...");
    Autentifica autentifica = new Autentifica();
    HttpSession session     = null;
    RegistroPerfil registro = null;
    autentifica.getCredenciales().setCuentaInicial(getCliente().getCuenta());
    LOG.info("cuenta[".concat(getCliente().getCuenta()).concat("]"));
    if (autentifica.tieneAccesoBD(getCliente().getCuenta(), getCliente().getContrasenia(), JsfBase.getRequest().getRemoteAddr())) {
      if (JsfBase.isLockUsers()) {
        throw new BloqueoSitioException();
      }
      registro= new RegistroPerfil(autentifica);
      session = JsfBase.getSession();
      registro.addAutentifica(session);
      agregarUsuariosSitio(session, autentifica);
      if (!autentifica.getRedirect().equals(EPaginasPrivilegios.PERFILES)) {
        registro.addMenuSesion(session);
        registro.addTopMenuSesion(session);
      } // if
    }//if
    else {
      if (session != null) {
        synchronized (session) {
          JsfBase.cleanSesion(session);
        } // synchronized
      }
      throw new AccesoDenegadoException();
    } // else
  } // valida  

  protected void agregarUsuariosSitio(HttpSession session, Autentifica autentifica) {
    UsuariosEnLinea usuarios= null;
    Usuario usuario= new Usuario(
			autentifica.getPersona().getIdGrupo(),
			autentifica.getPersona().getIdPerfil(),
			autentifica.getPersona().getDescripcionPerfil(),
			session.getId(),
			autentifica.getPersona().getCuenta(),
			autentifica.getPersona().getNombreCompleto(),
			null, null,
			"",
			Calendar.getInstance(), 0, -1, false, null,
			autentifica.getPersona().getClaveGrupo());
    usuarios = (UsuariosEnLinea) JsfBase.getUsuariosSitio();
    usuarios.addCuenta(session.getId(), usuario);
  } // agregarUsuariosSitio
	
}

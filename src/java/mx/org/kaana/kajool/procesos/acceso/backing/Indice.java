package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.org.kaana.kajool.procesos.acceso.reglas.Acceso;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIMessage;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.procesos.acceso.beans.Cliente;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.backing.TemaActivo;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@RequestScoped
@Named(value = "kajoolIndice")
public class Indice extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 5323749709626263801L;
  private Cliente cliente;
  
  @Inject 
  private TemaActivo temaActivo;
  private static final Log LOG = LogFactory.getLog(Indice.class);

  @Override
  @PostConstruct
  protected void init() {
    setCliente(new Cliente("", "", "Bienvenido(a)", "", ""));

  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public TemaActivo getTemaActivo() {
    return temaActivo;
  }

  public void setTemaActivo(TemaActivo temaActivo) {
    this.temaActivo = temaActivo;
  }

  public String doIngresar() {
    String regresar = null;
    Acceso acceso = null;  
    try {     
      acceso = new Acceso(getCliente());
      acceso.valida();
      regresar = acceso.toForward();
      this.temaActivo.setName(getCliente().getTemaActivo());     
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch	    
    return regresar;
  } // doIngresar

  public void doVerificarCuenta() {
    LOG.info("Verificando cuenta de usuario para recuperar...");
    Entity vistaUsuario = null;
    Map param = new HashMap();
    try {
      param.put("cuenta", getCliente().getRecuperar());
      param.put("curp", getCliente().getCurp());
      vistaUsuario = (Entity) DaoFactory.getInstance().toEntity("VistaTcJanalUsuariosDto", "recuperar", param);
      if (vistaUsuario != null) {
        reiniciarContrasenia(vistaUsuario.toLong("idKey"));
        getCliente().setCuenta("");
        getCliente().setCurp("");
        JsfBase.addMessage("Se reinicio la contraseña con éxito.");
      }// if
      else {
        JsfBase.addMessage("Verifique su cuenta/CURP para recuperar su contraseña.");
      }// else			
    }// catch
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessage("Error al recuperar la contraseña.");
    }
  } // doVerificarCuenta

  private boolean reiniciarContrasenia(Long idEmpleado) throws Exception {
    Transaccion transaccion = null;
    Map<String, Object> params = null;
    boolean regresar = false;
    try {
      params = new HashMap();
      params.put("idPersona", idEmpleado);
      transaccion = new Transaccion(params);
      regresar = transaccion.ejecutar(EAccion.RESTAURAR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessage(UIMessage.toMessage("error_solicitud"));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // enviarCorreo

  @Override
  public void doLoad() {
  }

}

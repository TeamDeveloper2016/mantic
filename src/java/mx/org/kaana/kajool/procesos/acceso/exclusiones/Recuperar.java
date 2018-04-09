package mx.org.kaana.kajool.procesos.acceso.exclusiones;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 3/09/2015
 *@time 06:48:31 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIMessage;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.acceso.beans.Cliente;
import mx.org.kaana.kajool.procesos.acceso.reglas.TransaccionRecupera;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolRecuperar")
@ViewScoped
public class Recuperar extends IBaseAttribute implements Serializable{

  private static final Log LOG = LogFactory.getLog(Recuperar.class);
  private Cliente cliente;

  public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

  @PostConstruct
  @Override
	protected void init() {
    TcJanalUsuariosDto dto = null;
    try {
      setCliente(new Cliente("","","Bienvenido(a)", "", ""));
      this.attrs.put("cuenta", BouncyEncryption.decrypt(JsfBase.getParametro("data")));//desencriptamos la variable que viene en la url
      LOG.info(this.attrs.get("cuenta"));
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
	} // init

	public void doRecuperar(String proceso) {
    try {
       if(validarCuenta()){
         LOG.info("Actualizando contraseña...");
         Long idUsuario=obtenerIdUsuario();
         Map param=new HashMap();
         param.put("contrasenia",this.cliente.getContrasenia());
         TransaccionRecupera transaccion = new TransaccionRecupera(idUsuario,param);
         transaccion.ejecutar(EAccion.valueOf(proceso));
         JsfBase.addMessage(UIMessage.toMessage("correcto_modificar"));
       }
       else
         JsfBase.addMessage(UIMessage.toMessage("acceso_error"));
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // doRecuperar

  private boolean validarCuenta(){
    boolean regresar=false;
    if(this.cliente.getCuenta().equals(this.attrs.get("cuenta")))
      regresar=true;
    return regresar;
  }//validarCuenta

  private Long obtenerIdUsuario(){
    Entity vistaUsuario = null;
    Map param           = new HashMap();
    Long regresar=-1L;
    try{
      param.put("cuenta", this.attrs.get("cuenta"));
      vistaUsuario = (Entity)DaoFactory.getInstance().toEntity("VistaTcJanalUsuariosDto","recuperar",param);
      if(vistaUsuario!=null){
        regresar=vistaUsuario.getKey();
      }
      else
        JsfBase.addMessage(UIMessage.toMessage("acceso_error"));
    }
    catch(Exception e){
      JsfBase.addMessage(UIMessage.toMessage("error"));
      Error.mensaje(e);
    }
    finally{
      return regresar;
    }
  }//obtenerIdUsuario

}

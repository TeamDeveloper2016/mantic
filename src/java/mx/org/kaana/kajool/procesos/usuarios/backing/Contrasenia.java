package mx.org.kaana.kajool.procesos.usuarios.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/10/2016
 *@time 10:40:04 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>@kaana.org.mx>
 */

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalEmpleadosDto;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;

@ManagedBean(name="kajoolUsuariosContrasenia")
@ViewScoped
public class Contrasenia extends IBaseAttribute implements Serializable {		
  
  private static final long serialVersionUID = 1378606866445110109L;
		
  @PostConstruct
	@Override
	protected void init() {		
    try {
      this.attrs.put("usuario", JsfBase.getAutentifica().getEmpleado().getNombres()+ " "+ JsfBase.getAutentifica().getEmpleado().getPrimerApellido()+ " "+ (Cadena.isVacio(JsfBase.getAutentifica().getEmpleado().getSegundoApellido())? "": JsfBase.getAutentifica().getEmpleado().getSegundoApellido()));
      this.attrs.put("cuenta", JsfBase.getAutentifica().getEmpleado().getCuenta());
      this.attrs.put("perfil", JsfBase.getAutentifica().getEmpleado().getDescripcionPerfil());
      this.attrs.put("actual", "");
      this.attrs.put("anterior", BouncyEncryption.decrypt(JsfBase.getAutentifica().getEmpleado().getContrasenia()));
      this.attrs.put("nueva", "");			
      this.attrs.put("ratifica", "");	
    }
    catch(Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessage("Error", "Se presentó un error al preparar los datos", ETipoMensaje.ERROR);
    }
	}

	public void doAceptar() {
		Transaccion tx=null;
		try {
			this.attrs.put("idEmpleado", JsfBase.getAutentifica().getEmpleado().getIdEmpleado());
			this.attrs.put("nueva", this.attrs.get("nueva"));
			tx=new Transaccion(this.attrs);
			if (tx.ejecutar(EAccion.REGISTRAR)) {
				JsfBase.addMessage("La contraseña de ".concat(JsfBase.getAutentifica().getEmpleado().getCuenta()).concat(" se cambió con éxito."));
			} // if
			else {
				JsfBase.addMessage("Error", "No se puedó cambiar la contraseña del usuario", ETipoMensaje.ERROR);
			} // else
		} // try // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
			JsfBase.addMessage("Error", "Ocurrió un error al cambiar la contraseña del usuario".concat(e.getMessage()), ETipoMensaje.FATAL);
		} // catch    
	}
	

}

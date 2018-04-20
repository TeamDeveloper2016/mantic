package mx.org.kaana.kajool.procesos.usuarios.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 13/10/2016
 * @time 10:40:04 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>@kaana.org.mx>
 */
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

@Named(value = "kajoolUsuariosContrasenia")
@ViewScoped
public class Contrasenia extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 1378606866445110109L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("usuario", JsfBase.getAutentifica().getPersona().getNombres() + " " + JsfBase.getAutentifica().getPersona().getPrimerApellido() + " " + (Cadena.isVacio(JsfBase.getAutentifica().getPersona().getSegundoApellido()) ? "" : JsfBase.getAutentifica().getPersona().getSegundoApellido()));
      this.attrs.put("cuenta", JsfBase.getAutentifica().getPersona().getCuenta());
      this.attrs.put("perfil", JsfBase.getAutentifica().getPersona().getDescripcionPerfil());
      this.attrs.put("actual", "");
      this.attrs.put("anterior", BouncyEncryption.decrypt(JsfBase.getAutentifica().getPersona().getContrasenia()));
      this.attrs.put("nueva", "");
      this.attrs.put("ratifica", "");
    } catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessage("Error", "Se presentó un error al preparar los datos", ETipoMensaje.ERROR);
    }
  }

  public void doAceptar() {
    Transaccion tx = null;
    TcManticPersonasDto persona = null;
    try {
      persona = new TcManticPersonasDto(JsfBase.getAutentifica().getPersona().getIdPersona());
      persona.setContrasenia((String) this.attrs.get("nueva"));
      tx = new Transaccion(persona);
      if (tx.ejecutar(EAccion.REGISTRAR)) {
        JsfBase.addMessage("La contraseña de ".concat(JsfBase.getAutentifica().getPersona().getCuenta()).concat(" se cambió con éxito."));
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

package mx.org.kaana.kajool.procesos.usuarios.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 13/10/2016
 * @time 10:40:04 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>@kaana.org.mx>
 */
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.procesos.usuarios.reglas.CargaInformacionUsuarios;
import mx.org.kaana.kajool.procesos.usuarios.reglas.RandomCuenta;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.usuarios.reglas.beans.CriteriosBusqueda;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import org.primefaces.context.RequestContext;

@Named(value = "kajoolUsuariosAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 5319332808932704073L;
  private CriteriosBusqueda criteriosBusqueda;

  public CriteriosBusqueda getCriteriosBusqueda() {
    return criteriosBusqueda;
  }
    
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.criteriosBusqueda = new CriteriosBusqueda();
      this.attrs.put("tcJanalEmpleadoDto", new TcManticPersonasDto());
      this.attrs.put("tcJanalUsuarioDto", new TcJanalUsuariosDto());
      this.attrs.put("accion", (EAccion) JsfBase.getFlashAttribute("accion"));
      this.attrs.put("esperada", "");
      this.attrs.put("texto", "<div class=\"TexAlCenter\">Se encontró un usuario con el mismo nombre y apellidos<br/><span class=\"FontBold Fs14\">¿Desea tomar lo datos este usuario para esta cuenta de acceso?</span></div>");
      this.attrs.put("showConfirmDialog", true);
      loadPerfiles();
      if (((EAccion) this.attrs.get("accion")).equals(EAccion.MODIFICAR)) {
        this.attrs.put("titulo", "Modificar usuario de grupos de trabajo");
        cargarUsuario((Long) JsfBase.getFlashAttribute("idUsuario"));
      } // if
      else {
        this.attrs.put("titulo", "Agregar usuario a grupos de trabajo");
      }
    } // try // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public String doAceptar() {
    Transaccion transaccion = null;
    String regresar = null;
    TcManticPersonasDto persona = null;
    TcJanalUsuariosDto  usuario = null;
    try {
      persona = (TcManticPersonasDto) this.attrs.get("tcJanalEmpleadoDto");
      usuario = (TcJanalUsuariosDto) this.attrs.get("tcJanalUsuarioDto");
      usuario.setIdUsuarioModifica(JsfBase.getIdUsuario());
      usuario.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      persona.setIdUSuario(JsfBase.getIdUsuario());
      persona.setEstilo("sentinel");
      persona.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      transaccion = new Transaccion(usuario,persona);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage(((EAccion) this.attrs.get("accion")).equals(EAccion.AGREGAR) ? "Se agregó el usuario con éxito." : "Se modificó el usuario con éxito.");
      } // if
      else {
        JsfBase.addMessage("El usuario " + persona.getNombres() + persona.getPaterno() + " con ese perfil ya éxiste.");
      }
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  private void cargarUsuario(Long idUsuario) {
    try {
      this.attrs.put("tcJanalUsuarioDto", (TcJanalUsuariosDto) DaoFactory.getInstance().findById(TcJanalUsuariosDto.class, idUsuario));
      this.attrs.put("tcJanalEmpleadoDto", (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, ((TcJanalUsuariosDto) this.attrs.get("tcJanalUsuarioDto")).getIdPersona()));
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
  }

  public void doSearch() {
    Map<String, String> params = null;
    TcManticPersonasDto empleado = null;
    TcJanalUsuariosDto usuario = null;
    try {
      params = new HashMap();
      params.put("curp", ((TcManticPersonasDto) this.attrs.get("tcJanalEmpleadoDto")).getCurp());
      empleado = (TcManticPersonasDto) DaoFactory.getInstance().findFirst(TcManticPersonasDto.class, "curp", params);
      if (empleado != null) {
        this.attrs.put("tcJanalEmpleadoDto", empleado);
        params.put("idPersona", empleado.getIdPersona().toString());
        params.put("idPerfil", ((TcJanalUsuariosDto) this.attrs.get("tcJanalUsuarioDto")).getIdPerfil().toString());
        usuario = (TcJanalUsuariosDto) DaoFactory.getInstance().findFirst(TcJanalUsuariosDto.class, "identically", params);
        if (usuario != null) {
          this.attrs.put("tcJanalUsuarioDto", usuario);
        }
      } // if
      else {
        this.attrs.put("tcJanalEmpleadoDto", new TcManticPersonasDto());
        this.attrs.put("tcJanalUsuarioDto", new TcJanalUsuariosDto());
      } // else
    } // try // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // exception
    finally {
      Methods.clean(params);
    } // finally
  }


  protected void loadPerfiles() {
    CargaInformacionUsuarios cargarInformaUsuario= null;  
    try {      
      cargarInformaUsuario = new CargaInformacionUsuarios(this.criteriosBusqueda);
      cargarInformaUsuario.init(false);     
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // loadEntidades		

  public void doCuenta() {
    TcManticPersonasDto empleado = (TcManticPersonasDto) this.attrs.get("tcJanalEmpleadoDto");
    if (!Cadena.isVacio(empleado.getNombres()) || !Cadena.isVacio(empleado.getPaterno()) || !Cadena.isVacio(empleado.getMaterno())) {
      RandomCuenta random = new RandomCuenta(
              Cadena.isVacio(empleado.getNombres()) ? "kajool" : empleado.getNombres(),
              Cadena.isVacio(empleado.getPaterno()) ? "kajool" : empleado.getPaterno(),
              Cadena.isVacio(empleado.getMaterno()) ? "kajool" : empleado.getMaterno(), -1L);
      empleado.setCuenta(random.getCuentaGenerada());
    } // if
    if (!isLockField() && !Cadena.isVacio(empleado.getNombres()) && (!Cadena.isVacio(empleado.getPaterno()) || !Cadena.isVacio(empleado.getMaterno()))) {
      if ((boolean) this.attrs.get("showConfirmDialog") && toFindUserByName(empleado)) {
        RequestContext.getCurrentInstance().execute("janal.bloquear();PF('dialogoConfirmacion').show();");
        RequestContext.getCurrentInstance().update("confirmacion");
        this.attrs.put("showConfirmDialog", false);
      } // if  
    } // if  
  }

  public boolean isLockField() {
    return ((TcManticPersonasDto) this.attrs.get("tcJanalEmpleadoDto")).isValid();
  }

  private boolean toFindUserByName(TcManticPersonasDto empleado) {
    boolean regresar = false;
    Map<String, String> params = null;
    try {
      params = new HashMap();
      params.put("nombres", empleado.getNombres());
      params.put("paterno", empleado.getPaterno());
      params.put("materno", empleado.getMaterno());
      empleado = (TcManticPersonasDto) DaoFactory.getInstance().findFirst(TcManticPersonasDto.class, "nombre", params);
      if (empleado != null) {
        this.attrs.put("empleado", empleado);
        regresar = true;
      } // if
    } // try // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // exception
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  public void doUpdateEmpleado() {
    ((TcManticPersonasDto) this.attrs.get("tcJanalEmpleadoDto")).setCurp(((TcManticPersonasDto) this.attrs.get("empleado")).getCurp());
    doSearch();
  }

}

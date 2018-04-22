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
import java.util.ArrayList;
import java.util.List;
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
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.usuarios.reglas.CargaInformacionUsuarios;
import mx.org.kaana.kajool.procesos.usuarios.reglas.RandomCuenta;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.usuarios.reglas.beans.CriteriosBusqueda;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import mx.org.kaana.mantic.enums.ETipoPersona;
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
      this.attrs.put("accion", (EAccion) JsfBase.getFlashAttribute("accion"));
      this.attrs.put("showConfirmDialog", true);
      loadPerfiles();
      loadPersonas();
      loadTipoPersonas();
      cargarUsuario(JsfBase.getFlashAttribute("idUsuario") != null ? (Long) JsfBase.getFlashAttribute("idUsuario") : -1L);
      this.attrs.put("titulo", ((EAccion) this.attrs.get("accion")).equals(EAccion.MODIFICAR) ? "Modificar usuario cuenta [".concat(((TcManticPersonasDto) this.attrs.get("tcManticPersonaDto")).getCuenta()) : "Agregar usuario [...]");
      doBuscar();
    } // try // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  private void loadTipoPersonas() {
    List<Columna> formatos = null;
    Map<String, Object> params = null;
    List<UISelectEntity> personasTitulos;
    try {
      formatos = new ArrayList<>();
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      formatos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      formatos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      personasTitulos = UIEntity.build("TcManticPersonasTitulosDto", "row", params, formatos);
      this.attrs.put("titulosPersonas", personasTitulos);
      this.attrs.put("idTituloPersona", UIBackingUtilities.toFirstKeySelectEntity(personasTitulos));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(formatos);
    } // finally
  }

  private void loadPersonas() {
    List<Columna> formatos = null;
    Map<String, Object> params = null;
    Entity entityDefault = null;
    try {
      formatos = new ArrayList<>();
      params = new HashMap<>();
      formatos.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      formatos.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      formatos.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      params.put(Constantes.SQL_CONDICION, "id_tipo_persona=".concat(ETipoPersona.USUARIO.getIdTipoPersona().toString()));
      entityDefault = new Entity();
      this.criteriosBusqueda.getListaPersonas().addAll(UIEntity.build("TcManticPersonasDto", "row", params, formatos));
      entityDefault.put("idKey", new Value("idKey", -1L, "id_key"));
      entityDefault.put("nombres", new Value("nombres", "SELECCIONE...", "nombres"));
      // entityDefault.put("descripcion", new Value("paterno", "NUEVA...", "paterno"));
      // entityDefault.put("descripcion", new Value("paterno", "NUEVA...", "paterno"));
      this.criteriosBusqueda.getListaPersonas().add(0, new UISelectEntity(entityDefault));
      if (!getCriteriosBusqueda().getListaPersonas().isEmpty()) {
        getCriteriosBusqueda().setPersona((UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(this.criteriosBusqueda.getListaPersonas()));
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
    finally {
      Methods.clean(params);
      Methods.clean(formatos);
    } // finally 
  }

  public String doAceptar() {
    Transaccion transaccion = null;
    String regresar = null;
    TcManticPersonasDto persona = null;
    TcJanalUsuariosDto usuario = null;
    try {
      persona = (TcManticPersonasDto) this.attrs.get("tcManticPersonaDto");
      usuario = (TcJanalUsuariosDto) this.attrs.get("tcJanalUsuarioDto");
      usuario.setIdUsuarioModifica(JsfBase.getIdUsuario());
      usuario.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      persona.setIdUSuario(JsfBase.getIdUsuario());
      usuario.setActivo(1L);
      usuario.setIdPerfil(this.criteriosBusqueda.getPerfil().getKey());
      persona.setEstilo("sentinel");
      persona.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      transaccion = new Transaccion(usuario, persona);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro";
        JsfBase.addMessage(((EAccion) this.attrs.get("accion")).equals(EAccion.AGREGAR) ? "Se agregó el usuario con éxito." : "Se modificó el usuario con éxito.");
      } // if
      else {
        String perfil= this.criteriosBusqueda.getListaPerfiles().get(this.criteriosBusqueda.getListaPerfiles().indexOf(new UISelectEntity(this.criteriosBusqueda.getPerfil().getKey().toString()))).toString("descripcion");
        JsfBase.addMessage("El usuario " + persona.getNombres() + persona.getPaterno() + " ya existe con perfil de ".concat(perfil));
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
      if (!idUsuario.equals(-1L)) {
        this.attrs.put("tcJanalUsuarioDto", (TcJanalUsuariosDto) DaoFactory.getInstance().findById(TcJanalUsuariosDto.class, idUsuario));
        this.attrs.put("tcManticPersonaDto", (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, ((TcJanalUsuariosDto) this.attrs.get("tcJanalUsuarioDto")).getIdPersona()));
      } else {
        this.attrs.put("tcJanalUsuarioDto", new TcJanalUsuariosDto());
        this.attrs.put("tcManticPersonaDto", new TcManticPersonasDto());
      }
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
  }

  public void doBuscar() {
    Map<String, String> params = null;
    TcManticPersonasDto persona = null;
    TcJanalUsuariosDto usuario = null;
    Long idKeyPersona = null;
    int posPerfil = -1;
    int posTituloPersona = -1;
    List<UISelectEntity> titulosPersona = null;
    try {
      params = new HashMap();
      idKeyPersona = this.criteriosBusqueda.getPersona().getKey();
      if (!idKeyPersona.equals(-1L)) {
        persona = (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, idKeyPersona);
        this.attrs.put("tcManticPersonaDto", persona);
        titulosPersona = (List<UISelectEntity>) this.attrs.get("titulosPersonas");
        posTituloPersona = titulosPersona.indexOf(new UISelectEntity(persona.getIdPersonaTitulo().toString()));
        this.attrs.put("idTituloPersona", titulosPersona.get(posTituloPersona));
      } // if      
      usuario = (TcJanalUsuariosDto) this.attrs.get("tcJanalUsuarioDto");
      if (usuario.isValid()) {
        posPerfil = this.getCriteriosBusqueda().getListaPerfiles().indexOf(new UISelectEntity(new Entity(idKeyPersona)));
        this.criteriosBusqueda.setPerfil(this.getCriteriosBusqueda().getListaPerfiles().get(posPerfil));
      } else {
        this.attrs.put("tcJanalEmpleadoDto", new TcManticPersonasDto());
        this.attrs.put("tcJanalUsuarioDto", new TcJanalUsuariosDto());
      } // else
    } catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // exception
    finally {
      Methods.clean(params);
    } // finally
  }
  	public String doCancelar(){		
		return "filtro";
	} // doAccion

  protected void loadPerfiles() {
    CargaInformacionUsuarios cargarInformaUsuario = null;
    try {
      cargarInformaUsuario = new CargaInformacionUsuarios(this.criteriosBusqueda);
      cargarInformaUsuario.init(false);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // loadEntidades		

  public void doCuenta() {
    TcManticPersonasDto persona = (TcManticPersonasDto) this.attrs.get("tcManticPersonaDto");
    if (!Cadena.isVacio(persona.getNombres()) || !Cadena.isVacio(persona.getPaterno()) || !Cadena.isVacio(persona.getMaterno())) {
      RandomCuenta random = new RandomCuenta(
              Cadena.isVacio(persona.getNombres()) ? "kajool" : persona.getNombres(),
              Cadena.isVacio(persona.getPaterno()) ? "kajool" : persona.getPaterno(),
              Cadena.isVacio(persona.getMaterno()) ? "kajool" : persona.getMaterno(), -1L);
      persona.setCuenta(random.getCuentaGenerada());
    } // if
    if (!isLockField() && !Cadena.isVacio(persona.getNombres()) && (!Cadena.isVacio(persona.getPaterno()) || !Cadena.isVacio(persona.getMaterno()))) {
      if ((boolean) this.attrs.get("showConfirmDialog") && toFindUserByName(persona)) {
        RequestContext.getCurrentInstance().execute("janal.bloquear();PF('dialogoConfirmacion').show();");
        RequestContext.getCurrentInstance().update("confirmacion");
        this.attrs.put("showConfirmDialog", false);
      } // if  
    } // if  
  }

  public boolean isLockField() {
    return ((TcManticPersonasDto) this.attrs.get("tcManticPersonaDto")).isValid();
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
    ((TcManticPersonasDto) this.attrs.get("tcManticPersonaDto")).setCurp(((TcManticPersonasDto) this.attrs.get("empleado")).getCurp());
    doBuscar();
  }

}

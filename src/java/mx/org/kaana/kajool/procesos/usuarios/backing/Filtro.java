package mx.org.kaana.kajool.procesos.usuarios.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EReporte;
import mx.org.kaana.kajool.enums.ETipoBusqueda;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.usuarios.beans.Usuario;
import mx.org.kaana.kajool.procesos.usuarios.reglas.CargaInformacionUsuarios;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.usuarios.reglas.beans.CriteriosBusqueda;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 05:19:51 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@Named(value = "kajoolUsuariosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = -1279553224860143822L;
  private final String ESTATUS_ACTIVO = "1";
  private CriteriosBusqueda criteriosBusqueda;
  private Entity seleccionado;

  public CriteriosBusqueda getCriteriosBusqueda() {
    return criteriosBusqueda;
  }

  public void setCriteriosBusqueda(CriteriosBusqueda criteriosBusqueda) {
    this.criteriosBusqueda = criteriosBusqueda;
  }

  public Entity getSeleccionado() {
    return seleccionado;
  }

  public void setSeleccionado(Entity seleccionado) {
    this.seleccionado = seleccionado;
  }

  public void selectec(SelectEvent selectEvent) {
    this.seleccionado = (Entity) selectEvent.getObject();
  }

  @Override
  @PostConstruct
  protected void init() {
    CargaInformacionUsuarios carga = null;
    try {
      this.attrs.put("opcionSeleccionada", 1);
      setCriteriosBusqueda(new CriteriosBusqueda());
      carga = new CargaInformacionUsuarios(getCriteriosBusqueda());
      carga.init();
      this.attrs.put("isPermisoDelega", JsfBase.isAdmin());
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }

  /**
   * *
   * Recarga los datos segun la acicón que se ejecutó.
   */
  private void recargarTablaDatos(ETipoBusqueda tipoBusqueda) {
    try {
      CargaInformacionUsuarios carga = new CargaInformacionUsuarios(getCriteriosBusqueda());
      Map<String, Object> params = new HashMap<>();
      List<Columna> campos = new ArrayList<>();
      switch (tipoBusqueda) {
        case POR_NOMBRE:
          params.put(Constantes.SQL_CONDICION, carga.busquedaPorNombre());
          break;
        case POR_PERFIL:
          params.put(Constantes.SQL_CONDICION, carga.busquedaPorPerfil());
          break;
        case POR_ENTIDAD:
          params.put(Constantes.SQL_CONDICION, carga.busquedaPorEntidad());
          break;
      } // switch
      this.attrs.put("condicion", params.get(Constantes.SQL_CONDICION));
      params.put("sortOrder", "order by tc_janal_perfiles.id_perfil, tc_janal_empleados.primer_apellido, tc_janal_empleados.segundo_apellido, tc_janal_empleados.nombres");
      this.lazyModel = new FormatCustomLazy("VistaUsuariosDto", "row", params, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  /**
   * *
   * Metodo de interacción cuando se presiona el link en la busqueda por nombre
   *
   * @param event. Recibe el evento desde la pagina
   */
  public void doBusquedaNombre(ActionEvent event) {
    recargarTablaDatos(ETipoBusqueda.POR_NOMBRE);
  }

  /**
   * *
   * Metodo de interaccion cuando se presiona el link en la busqueda por Se realiza la consulta por perfil
   *
   * @param event
   */
  public void doBusquedaPerfil(ActionEvent event) {
    recargarTablaDatos(ETipoBusqueda.POR_PERFIL);
  }

  /**
   * *
   * Metodo de interaccion cuando se presiona el link en la busqueda por perfil Se realiza la consulta por perfil
   *
   * @param event
   */
  public void busquedaEntidad(ActionEvent event) {
    recargarTablaDatos(ETipoBusqueda.POR_ENTIDAD);
  }

  public void busquedaOrganizacion(ActionEvent event) {
    recargarTablaDatos(ETipoBusqueda.POR_ORGANIZACION);
    this.attrs.put("opcionSeleccionada", ETipoBusqueda.POR_ORGANIZACION.ordinal() + 1);
  }

  /**
   * *
   * Metodo de interacción cuando se presiona el botón de activiar usuario. Se verificar el estatus actual del usuario,
   * si es activo. cambiar a inactivo o viceversa
   *
   * @param event
   */
  public void doActivarUsuario() {
    CargaInformacionUsuarios carga = new CargaInformacionUsuarios();
    this.attrs.put("idUsuario", this.seleccionado.toLong("idKeyUsuario"));
    this.attrs.put("activo", this.seleccionado.toString("activo").equals(ESTATUS_ACTIVO) ? 0L : 1L);
    carga.cambiarEstatusUsuario(this.attrs);
    Integer opcionSeleccionada = (Integer) this.attrs.get("opcionSeleccionada");
    //Verificando tab Seleccionado
    switch (opcionSeleccionada) {
      case 1:
        recargarTablaDatos(ETipoBusqueda.POR_NOMBRE);
        break;
      case 2:
        recargarTablaDatos(ETipoBusqueda.POR_ENTIDAD);
        break;
      case 3:
        recargarTablaDatos(ETipoBusqueda.POR_PERFIL);
        break;
    } // switch
  }

  /**
   * *
   * Metodo escuchador que se ejecuta cuando hubo un cambio de tab.
   *
   * @param event
   */
  public void onTabChange(TabChangeEvent event) {
    this.lazyModel = null;
    this.attrs.put("opcionSeleccionada", event.getTab().getTitle().equals(getCriteriosBusqueda().getTitleTabNombre()) ? 1 : event.getTab().getTitle().equals(getCriteriosBusqueda().getTitleTabEntidad()) ? 2 : 3);
    this.seleccionado = null;
    UIBackingUtilities.resetDataTable();
  }

  public void doConfigMensaje(ActionEvent event) {
    Entity entity = (Entity) event.getComponent().getAttributes().get("current");
    doConfigMensaje(entity);
  }

  public void doConfigMensaje(Entity entity) {
    this.seleccionado = entity;
    StringBuilder mensaje = new StringBuilder();
    mensaje.append(this.seleccionado.toString("activo").equals(ESTATUS_ACTIVO) ? "desactivar " : " activar ");
    mensaje.append(" la cuenta de acceso de [");
    mensaje.append(this.seleccionado.toString("cuenta"));
    mensaje.append("]");
    this.attrs.put("mensajeAlerta", mensaje);
  }

  public String doAccion(EAccion accion) {
    try {
      JsfBase.setFlashAttribute("accion", accion);
      if (accion.equals(EAccion.MODIFICAR)) {
        JsfBase.setFlashAttribute("idUsuario", this.seleccionado.toLong("idKeyUsuario"));
      }
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return "accion";
  }

  public String doAceptar() {
    Map<String, Object> params = new HashMap<>();
    params.put("idGrupo", Numero.getLong(JsfBase.seekParameter("idGrupo").toString()));
    params.put(Constantes.SQL_CONDICION, this.attrs.get("condicion"));
    JsfBase.setFlashAttribute(Constantes.REPORTE_REFERENCIA, new Usuario(EReporte.USUARIO, params)); // CARGAR EL REPORTE
    return "/Paginas/Reportes/generar";
  } // doAceptar

  public void doRecuperarInformacionDeUsuario() {
    this.attrs.put("entidad", this.seleccionado.toString("descEntidad"));
    this.attrs.put("nombre", this.seleccionado.toString("nombres"));
    this.attrs.put("perfil", this.seleccionado.toString("descPerfil"));
    //return null;
  }

  public void doEliminar() {
    Transaccion tx = null;
    TcJanalUsuariosDto usuario = null;
    TcManticPersonasDto persona = null;
    try {
      usuario = (TcJanalUsuariosDto) DaoFactory.getInstance().findById(TcJanalUsuariosDto.class, this.seleccionado.toLong("idKeyUsuario"));
      persona = (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, usuario.getIdPersona());
      this.attrs.put("idUsuario", this.seleccionado.toLong("idKeyUsuario"));
      this.attrs.put("tcJanalUsuarioDto", usuario);
      tx = new Transaccion(this.attrs);
      if (tx.ejecutar(EAccion.ELIMINAR)) {
        JsfBase.addMessage("El usuario ".concat(persona.getCuenta()).concat(" fue eliminado correctamente."));
      } // if
      else {
        JsfBase.addMessage("Error", "El usuario no pudo se eliminado", ETipoMensaje.ERROR);
      } // else
    } // try // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessage("Error", "Ocurrió un error al eliminar el registro del usuario".concat(e.getMessage()), ETipoMensaje.FATAL);
    } // catch
  }

  public void doRecuperaRegistro(ActionEvent event) {
    this.seleccionado = (Entity) event.getComponent().getAttributes().get("current");
  }

  public String doRegresar() {
    //return UIBacking.returnMenu();
    return null;
  }

  @Override
  public void doLoad() {
    try {
      this.attrs.put("validaDelega", JsfBase.isAdmin() || JsfBase.getAutentifica().getPersona().getIdUsuario().equals(this.seleccionado.getKey()));
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }

  public String doDelegarUsuario() {
    String regresar = null;
    try {
      if (Boolean.valueOf(this.attrs.get("validaDelega").toString())) {
        JsfBase.setFlashAttribute("idUsuario", this.seleccionado.getKey());
        regresar = "delegar";
      } // if
      else {
        JsfBase.addMessage("Delegar usuario", "No tienes permisos para delegar a otro usuario", ETipoMensaje.ERROR);
      }
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public String doDelegarMismoUsuario() {
    JsfBase.setFlashAttribute("idUsuario", JsfBase.getAutentifica().getPersona().getIdUsuario());
    return "delegar";
  }

  public void doReset() {
    Transaccion tx = null;
    TcJanalUsuariosDto usuario = null;
    TcManticPersonasDto persona = null;
    try {
      usuario = (TcJanalUsuariosDto) DaoFactory.getInstance().findById(TcJanalUsuariosDto.class, this.seleccionado.toLong("idKeyUsuario"));
      persona = (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, usuario.getIdPersona());
      this.attrs.put("idPersona", usuario.getIdPersona());
      tx = new Transaccion(this.attrs);
      if (tx.ejecutar(EAccion.RESTAURAR)) {
        JsfBase.addMessage("La cuenta del usuario ".concat(persona.getCuenta()).concat(" ya fue resetada con exito."));
      } // if
      else {
        JsfBase.addMessage("Error", "No se puede resetar la contraseña del usuario", ETipoMensaje.ERROR);
      } // else
    } // try // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessage("Error", "Ocurrió un error al resetear la contraseña del usuario".concat(e.getMessage()), ETipoMensaje.FATAL);
    } // catch
  }
}

package mx.org.kaana.kajool.procesos.usuarios.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EReporte;
import mx.org.kaana.kajool.enums.ETipoBusqueda;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.usuarios.beans.Usuario;
import mx.org.kaana.kajool.procesos.usuarios.reglas.CargaInformacionUsuarios;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.usuarios.reglas.beans.CriteriosBusqueda;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UIMessage;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

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
  private final Long ESTATUS_ACTIVO = 1L;
  private CriteriosBusqueda criteriosBusqueda;

  public CriteriosBusqueda getCriteriosBusqueda() {
    return criteriosBusqueda;
  }

  @Override
  @PostConstruct
  protected void init() {
    CargaInformacionUsuarios carga = null;
    try {
      this.criteriosBusqueda = new CriteriosBusqueda();
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
    Map<String, Object> params = null;
    List<Columna> campos = null;
    CargaInformacionUsuarios carga = null;
    try {
      carga = new CargaInformacionUsuarios(getCriteriosBusqueda());
      params = new HashMap<>();
      campos = new ArrayList<>();
      campos.add(new Columna("primerApellido", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("segundoApellido", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("cuenta", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("descPerfil", EFormatoDinamicos.MAYUSCULAS));      
      switch (tipoBusqueda) {
        case NOMBRE:
          params.put(Constantes.SQL_CONDICION, carga.busquedaPorNombre());
          break;
        case PERFIL:
          params.put(Constantes.SQL_CONDICION, carga.busquedaPorPerfil());
          break;
        case TODOS:
          params.put(Constantes.SQL_CONDICION, carga.busquedaPorPerfilNombre());
          break;
        case SIN_CONDICION:
          params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
          break;
      } // switch
      this.attrs.put("condicion", params.get(Constantes.SQL_CONDICION));
      params.put("sortOrder", "order by tc_janal_perfiles.id_perfil, tc_mantic_personas.paterno, tc_mantic_personas.materno, tc_mantic_personas.nombres");
      this.lazyModel = new FormatCustomLazy("VistaUsuariosDto", "row", params, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(campos);
    } // finally
  }

  public void doActivarUsuario() {
    Entity seleccionado            = null;
    Transaccion  transaccion       = null;
    TcJanalUsuariosDto usuario     = null;
    try {
      seleccionado = (Entity) this.attrs.get("seleccionado");
      usuario = new TcJanalUsuariosDto(seleccionado.getKey());
      usuario.setActivo(seleccionado.toLong("activo").equals(ESTATUS_ACTIVO)?0L:ESTATUS_ACTIVO);
      transaccion = new Transaccion(usuario);
      if (transaccion.ejecutar(EAccion.ACTIVAR)) {
        JsfBase.addMessage(usuario.getActivo().equals(ESTATUS_ACTIVO) ? "Se activó el usuario con éxito." : "Se desactivó el usuario con éxito.");
      }
      doBuscar();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch  
  }

  public void doActivar() {
    Entity seleccionado = (Entity) this.attrs.get("seleccionado");
    StringBuilder mensaje = new StringBuilder();
    mensaje.append(seleccionado.toLong("activo").equals(ESTATUS_ACTIVO) ? "bloquear " : " activar ");
    mensaje.append(" la cuenta de acceso de [");
    mensaje.append(seleccionado.toString("cuenta"));
    mensaje.append("]");
    this.attrs.put("mensajeAlerta", mensaje);
  }

  public String doAccion(EAccion accion) {
    Entity seleccionado = (Entity) this.attrs.get("seleccionado");
    try {
      JsfBase.setFlashAttribute("accion", accion);
      JsfBase.setFlashAttribute("idUsuario", accion.equals(EAccion.MODIFICAR) || accion.equals(EAccion.CONSULTAR) ? seleccionado.toLong("idKeyUsuario") : null);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return "accion".concat(Constantes.REDIRECIONAR);
  }

  public String doAceptar() {
    Map<String, Object> params = new HashMap<>();
    params.put("idGrupo", Numero.getLong(JsfBase.seekParameter("idGrupo").toString()));
    params.put(Constantes.SQL_CONDICION, this.attrs.get("condicion"));
    JsfBase.setFlashAttribute(Constantes.REPORTE_REFERENCIA, new Usuario(EReporte.USUARIO, params)); // CARGAR EL REPORTE
    return "/Paginas/Reportes/generar";
  } // doAceptar

  public void doRecuperarInformacionDeUsuario() {
    Entity seleccionado = (Entity) this.attrs.get("seleccionado");
    this.attrs.put("nombre", seleccionado.toString("nombres"));
    this.attrs.put("perfil", seleccionado.toString("descPerfil"));
    //return null;
  }

  public void doEliminar() {
    Transaccion tx = null;
    TcJanalUsuariosDto usuario = null;
    Entity seleccionado = null;
    Map<String, Object> params = null;
    try {
      seleccionado = (Entity) this.attrs.get("seleccionado");
      params = new HashMap<>();
      usuario = new TcJanalUsuariosDto(seleccionado.getKey());
      tx = new Transaccion(usuario);
      if (tx.ejecutar(EAccion.ELIMINAR)) {
        params.put("elemento", "el usuario [".concat(seleccionado.toString("cuenta")).concat("]"));
        //JsfBase.addMessage(UIMessage.toMessage("correcto_eliminar_elemento", params));
        JsfBase.addMessage("Se elimino el usuario [".concat(seleccionado.toString("cuenta")).concat("]"));
      } // if
      else {
        JsfBase.addMessage("Error", "El usuario no puede ser eliminado debido a que tiene información asociada", ETipoMensaje.ERROR);
      } // else
    } // try // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessage("Error", "Ocurrió un error al eliminar el registro del usuario".concat(e.getMessage()), ETipoMensaje.FATAL);
    } // catch
    finally {
      Methods.clean(params);
    }// 
  }

  @Override
  public void doLoad() {
    Entity seleccionado = (Entity) this.attrs.get("seleccionado");
    try {
      //this.attrs.put("validaDelega", JsfBase.isAdmin() || JsfBase.getAutentifica().getPersona().getIdUsuario().equals(seleccionado.getKey()));      
      doBuscar();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }

  public String doDelegarUsuario() {
    String regresar = null;
    Entity seleccionado = (Entity) this.attrs.get("seleccionado");
    try {
      if (Boolean.valueOf(this.attrs.get("validaDelega").toString())) {
        JsfBase.setFlashAttribute("idUsuario", seleccionado.getKey());
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
    TcManticPersonasDto persona = null;
    Entity seleccionado = null;
    try {
      seleccionado = (Entity) this.attrs.get("seleccionado");
      persona = new TcManticPersonasDto(seleccionado.toLong("idPersona"));
      persona.setCurp(seleccionado.toString("curp"));
      tx = new Transaccion(persona);
      if (tx.ejecutar(EAccion.RESTAURAR)) {
        JsfBase.addMessage("La cuenta del usuario ".concat(seleccionado.toString("cuenta")).concat(" ya fue resetada con exito."));
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

  private void doBuscar() {
    try {
      if (Cadena.isVacio(this.criteriosBusqueda.getNombre()) && this.criteriosBusqueda.getPerfil().getKey().equals(-1L)) {
        recargarTablaDatos(ETipoBusqueda.SIN_CONDICION);
      } else {
        if (!Cadena.isVacio(this.criteriosBusqueda.getNombre()) && this.criteriosBusqueda.getPerfil().getKey().equals(-1L)) {
          recargarTablaDatos(ETipoBusqueda.NOMBRE);
        } // if
        else {
          recargarTablaDatos(Cadena.isVacio(this.criteriosBusqueda.getNombre()) ? ETipoBusqueda.PERFIL : ETipoBusqueda.TODOS);
        }
      }  // else        
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessage("Ocurrió un error en la busqueda de los usuarios", ETipoMensaje.ERROR);
    } // catch

  }
}

package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesmenu.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TrJanalMenusGruposDto;
import mx.org.kaana.kajool.db.dto.TrJanalMenusPerfilesDto;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.beans.OpcionMenu;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 27/08/2015
 * @time 11:05:55 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Transaccion extends IBaseTnx {

  private List<OpcionMenu> listaOpciones;
  private List<OpcionMenu> listaOpcionesEliminar;
  private Long idGrupo;
  private Long idPerfil;
  private boolean perfiles;

  public Transaccion(List<OpcionMenu> listaOpciones, List<OpcionMenu> listaOpcionesEliminar, Long idGrupo, Long idPerfil, boolean perfiles) {
    this.listaOpciones = listaOpciones;
    this.listaOpcionesEliminar = listaOpcionesEliminar;
    this.idGrupo = idGrupo;
    this.idPerfil = idPerfil;
    this.perfiles = perfiles;
  }

  @Override
  public boolean ejecutar(Session session, EAccion accion) throws Exception {
    boolean regresar = true;
    try {
      switch (accion) {
        case AGREGAR:
          if (this.perfiles) {
            eleiminarPerfiles(session);
            agregarPerfiles(session);
          } // if
          else {
            eleiminarGrupos(session);
            agregarGrupos(session);
          } // else
          break;
      } // switch
    } // try
    catch (Exception e) {
      regresar = false;
      throw e;
    } // catch
    return regresar;
  }

  private void agregarGrupos(Session session) throws Exception {
    try {
      for (OpcionMenu opcionMenu : this.listaOpciones) {
        TrJanalMenusGruposDto dto = new TrJanalMenusGruposDto();
        dto.setIdGrupo(this.idGrupo);
        dto.setIdMenu(opcionMenu.getIdMenu());
        dto.setIdUsuario(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
        DaoFactory.getInstance().insert(session, dto);
        session.flush();
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
  }

  private void agregarPerfiles(Session session) throws Exception {
    try {
      for (OpcionMenu opcionMenu : this.listaOpciones) {
        TrJanalMenusPerfilesDto dto = new TrJanalMenusPerfilesDto();
        dto.setIdPerfil(this.idPerfil);
        dto.setIdMenuGrupo(opcionMenu.getIdMenuGrupo());
        dto.setIdUsuario(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
        DaoFactory.getInstance().insert(session, dto);
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
  }

  private void eleiminarGrupos(Session session) throws Exception {
    Map<String, String> params = null;
    Long idMenuGrupo = -1L;
    try {
      params = new HashMap<>();
      for (OpcionMenu opcionMenu : this.listaOpcionesEliminar) {
        params.clear();
        idMenuGrupo = opcionMenu.getIdMenuGrupo();
        params.put(Constantes.SQL_CONDICION, "id_menu_grupo =".concat(idMenuGrupo.toString()));
        DaoFactory.getInstance().deleteAll(session, TrJanalMenusPerfilesDto.class, params);
        DaoFactory.getInstance().delete(session, TrJanalMenusGruposDto.class, idMenuGrupo);
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  }

  private void eleiminarPerfiles(Session session) throws Exception {
    Map params = null;
    try {
      params = new HashMap();
      for (OpcionMenu opcionMenu : this.listaOpcionesEliminar) {
        DaoFactory.getInstance().delete(session, TrJanalMenusPerfilesDto.class, opcionMenu.getIdMenuPerfil());
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  }
}

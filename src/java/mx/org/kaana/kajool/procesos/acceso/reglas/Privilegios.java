package mx.org.kaana.kajool.procesos.acceso.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.db.dto.TcJanalMenusDto;
import mx.org.kaana.kajool.enums.EPaginasPrivilegios;
import mx.org.kaana.kajool.procesos.acceso.beans.Persona;
import mx.org.kaana.kajool.procesos.acceso.beans.GrupoPerfiles;
import mx.org.kaana.kajool.procesos.acceso.beans.Sucursal;
import mx.org.kaana.kajool.procesos.acceso.beans.UsuarioMenu;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import mx.org.kaana.mantic.enums.ETipoEmpresa;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 4/09/2015
 * @time 02:55:50 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Privilegios implements Serializable {

  private static final long serialVersionUID = -2036425611298445588L;
  private static final Log LOG = LogFactory.getLog(Privilegios.class);

  private Persona persona;

  public Privilegios() {
    this(null);
  } // Privilegios	

  public Privilegios(Persona empleado) {
    this.persona = empleado;
  } // Privilegios

  public List<Sucursal> toSucursales() throws Exception {
    Map<String, Object> params= null;
    List<Sucursal> regresar   = null;
    try {
      LOG.info(" Verificar acceso a sucursales ");
      params = new HashMap<>();
      params.put("idPersona", this.persona.getIdPersona());
      regresar = (List<Sucursal>) DaoFactory.getInstance().toEntitySet(Sucursal.class, "VistaTcJanalUsuariosDto", "sucursales", params);
			if(regresar!= null && !regresar.isEmpty()) 
				for (Sucursal sucursal: regresar) {
          params.put("idEmpresa", sucursal.getIdEmpresa());
     			Value value= DaoFactory.getInstance().toField("TcManticAlmacenesDto", "almacen", params, "idAlmacen");
		     	if(value.getData()!= null)
						sucursal.setIdAlmacen(value.toLong());
				} // for
    }// try
    catch (Exception e) {
      throw e;
    }// catch
    finally {
      Methods.clean(params);
    }// finally
    return regresar;
  } // procresaCentroTrabajo

  public Long verificarPerfiles() throws Exception {
    Long regresar = -1L;
    Map<String, Object> params = null;
    TcManticPersonasDto persona = null;
    try {
      LOG.info("verificara perfiles ajenos al actual");
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_persona=".concat(this.persona.getIdPersona().toString()));
      regresar = DaoFactory.getInstance().toSize("TcJanalUsuariosDto", Constantes.DML_SELECT, params);
    }// try
    catch (Exception e) {
      throw e;
    }// catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // verificarPerfiles

  public Long verificarDelega() throws Exception {
    Long regresar = -1L;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idPersona", this.persona.getIdPersona());
      regresar = DaoFactory.getInstance().toSize("TrJanalUsuariosDelegaDto", "vigente", params);
    } // try
    catch (Exception e) {
      throw e;
    }// catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // verificarDelega		

  public List<UsuarioMenu> procesarModulosPerfil() throws Exception {
    Map<String, Object> params = null;
    List<UsuarioMenu> regresar = null;
    try {
      params = new HashMap<>();
      params.put("idPerfil", this.persona.getIdPerfil());
      regresar = DaoFactory.getInstance().toEntitySet(UsuarioMenu.class, "VistaPerfilesMenusDto", "menuPerfil", params, Constantes.SQL_TODOS_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // procesarModulosPerfil

  public List<UsuarioMenu> procesarTopModulos() throws Exception {
    Map<String, Object> params = null;
    List<UsuarioMenu> regresar = null;
    try {
      params = new HashMap<>();
      params.put("publicar", "1");
      params.put("idPerfil", this.persona.getIdPerfil());
      regresar = DaoFactory.getInstance().toEntitySet(UsuarioMenu.class, "VistaPerfilesMenusDto", "menuEncabezado", params, Constantes.SQL_TODOS_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // procesarModulosEncabezado

  public Long getIdPersona(String cuenta) throws Exception {
    Long regresar = -1L;
    Map<String, Object> params = null;
    Entity persona = null;
    try {
      params = new HashMap<>();
      params.put("cuenta", cuenta);
      persona = (Entity) DaoFactory.getInstance().toEntity("TrJanalUsuariosDelegaDto", "findLoginActivo", params);
      if (persona == null) {
        persona = (Entity) DaoFactory.getInstance().toEntity("VistaTcJanalUsuariosDto", "acceso", params);
      }
      if (persona != null) {
        regresar = persona.toLong("idPersona");
      }
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // getNumEmpleado

  public Persona toPersona(GrupoPerfiles grupoPerfil) throws Exception {
    return toPersona(grupoPerfil.getIdUsuario(), grupoPerfil.getIdPerfil(), grupoPerfil.getCuenta());
  }

  public Persona toPersona(Long idUsuario, Long idPerfil, String cuenta) throws Exception {
    Persona regresar = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idUsuario", idUsuario);
      params.put("idPerfil", idPerfil);
      params.put("cuenta", cuenta);
      regresar = (Persona) DaoFactory.getInstance().toEntity(Persona.class, "VistaTcJanalUsuariosDto", "detalleUsuarioPerfil", params);
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // toEmpleado

  public Persona toPersonaDelega(GrupoPerfiles grupoPerfil) throws Exception {
    Persona regresar = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idUsuario", grupoPerfil.getIdUsuario());
      params.put("idPerfil", grupoPerfil.getIdPerfil());
      params.put("cuenta", grupoPerfil.getCuenta());
      regresar = (Persona) DaoFactory.getInstance().toEntity(Persona.class, "VistaDelegaPrivilegiosDto", "detalleUsuarioDelega", params);
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // toEmpleadoDelega

  private List<Entity> getUsuariosDelega() throws Exception {
    List<Entity> regresar = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idUsuario", this.persona.getIdUsuario());
      regresar = DaoFactory.getInstance().toEntitySet("VistaDelegaPrivilegiosDto", "usuariosDelega", params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally		
    return regresar;
  } // getUsuariosDelega

  public List<GrupoPerfiles> toUsuariosDelega() throws Exception {
    List<GrupoPerfiles> regresar = null;
    List<Entity> detalleUsuarios = null;
    try {
      detalleUsuarios = getUsuariosDelega();
      if (!detalleUsuarios.isEmpty()) {
        regresar = new ArrayList<>();
        for (Entity detalle : detalleUsuarios) {
          regresar.add(new GrupoPerfiles(null, detalle.toString("nombre"), false, detalle.toString("cuenta")));
        }
      } // if				
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(detalleUsuarios);
    } // finally	
    return regresar;
  } // consultaUsuariosDelega

  public List<GrupoPerfiles> toGruposDelega(String cuenta) throws Exception {
    List<GrupoPerfiles> regresar = null;
    List<Entity> grupos = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      regresar = new ArrayList<>();
      params.put("cuenta", cuenta);
      params.put("idUsuario", this.persona.getIdUsuario());
      grupos = DaoFactory.getInstance().toEntitySet("VistaDelegaPrivilegiosDto", "gruposDelegados", params);
      if (!grupos.isEmpty()) {
        for (Entity grupo : grupos) {
          regresar.add(new GrupoPerfiles(null, Cadena.concat("[", grupo.toString("clave").toUpperCase(), "] ", grupo.toString("descripcion")), false, grupo.getKey()));
        }
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(grupos);
    } // finally
    return regresar;
  } // getProyectosDelega

  public List<GrupoPerfiles> toPerfilesDelega(Long idGrupo, String cuenta, Long idUsuario) throws Exception {
    List<GrupoPerfiles> regresar = null;
    Map<String, Object> params = null;
    List<Entity> perfiles = null;
    try {
      params = new HashMap<>();
      params.put("idGrupo", idGrupo);
      params.put("cuenta", cuenta);
      params.put("idUsuario", idUsuario);
      perfiles = DaoFactory.getInstance().toEntitySet("VistaDelegaPrivilegiosDto", "perfilesDelegados", params, Constantes.SQL_TODOS_REGISTROS);
      regresar = new ArrayList<>();
      for (Entity perfil : perfiles) {
        regresar.add(new GrupoPerfiles(perfil.toLong("idPerfil"), perfil.toString("descripcion"), true, idGrupo, cuenta, perfil.toLong("idUsuario"), true, perfil.toString("usuarioPerfil"), perfil.toLong("idMenu"), perfil.toLong("idUsuario"), null, null));
      }
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(perfiles);
    } // finally
    return regresar;
  } // getPerfilesDelega

  public List<GrupoPerfiles> toGrupos() throws Exception {
    List<GrupoPerfiles> regresar = null;
    List<Entity> gruposUsuarios = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idUsuario", this.persona.getIdUsuario());
      gruposUsuarios = DaoFactory.getInstance().toEntitySet("VistaGruposAccesoDto", params);
      regresar = new ArrayList<>();
      for (Entity grupo : gruposUsuarios) {
        regresar.add(new GrupoPerfiles(null, Cadena.concat("[", grupo.toString("clave").toUpperCase(), "] ", Methods.ajustar(grupo.toString("descripcion"))), false, grupo.getKey()));
      }
    } // try
    catch (Exception e) {
      throw new Exception("Ocurri� un error al recuperar los grupos asociados al usuario", e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(gruposUsuarios);
    } // finally		
    return regresar;
  } // recuperarEncuestas

  public List<GrupoPerfiles> toPerfiles(Long idGrupo, Long idEmpleado) throws Exception {
    List<GrupoPerfiles> regresar = null;
    Map<String, Object> params = null;
    List<Entity> perfiles = null;
    try {
      params = new HashMap<>();
      params.put("idGrupo", idGrupo);
      params.put("idPersona", idEmpleado);
      perfiles = DaoFactory.getInstance().toEntitySet("VistaGruposAccesoDto", "perfiles", params, Constantes.SQL_TODOS_REGISTROS);
      regresar = new ArrayList<>();
      for (Entity perfil : perfiles) {
        regresar.add(new GrupoPerfiles(perfil.toLong("idKey"), perfil.toString("descripcion"), true, idGrupo, perfil.toString("cuenta"), idEmpleado, true, perfil.toString("usuarioPerfil"), perfil.toLong("idMenu"), perfil.toLong("idUsuario"), "", perfil.toString("descripcion")));
      }
    } // try
    catch (Exception e) {
      throw new Exception("Ocurri� un error al recuperar los perfiles asociados del grupo [" + idGrupo + "]");
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(perfiles);
    } // finally
    return regresar;
  } // recuperarPerfiles	

  public List<Entity> toUsersDelegaActivo(String cuenta) throws Exception {
    List<Entity> regresar = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("cuenta", cuenta);
      regresar = DaoFactory.getInstance().toEntitySet("VistaDelegaPrivilegiosDto", "delegaActivo", params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // toUserDelegaActivo

  public String toPaginaMenu(Long idMenu) throws Exception {
    String regresar = EPaginasPrivilegios.DEFAULT.getPath();
    TcJanalMenusDto menu = null;
    try {
      menu = (TcJanalMenusDto) DaoFactory.getInstance().findById(TcJanalMenusDto.class, idMenu);
      if (menu != null) {
        regresar = menu.getRuta();
      }
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // toPaginaMenu
}

package mx.org.kaana.kajool.procesos.usuarios.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 11:34:06 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class GestorSQL implements Serializable {

  private static final long serialVersionUID = -5143559192605075233L;
  private Long idUsuario;
  private Long idPersona;

  public GestorSQL(Long idPersona) {
    this(null, idPersona);
  } // GestorSQL

  public GestorSQL(Long idUsuario, Long idPersona) {
    this.idUsuario = idUsuario;
    this.idPersona = idPersona;
  } // GestorSQL

  public Entity toDetalleUsuario() throws Exception {
    Entity regresar = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idUsuario", this.idUsuario);
      regresar = (Entity) DaoFactory.getInstance().toEntity("VistaUsuariosDto", "perfilUsuario", params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // toDetalleUsuario

  public boolean validaUsuarioDelega(Session sesion, Long idPersona) throws Exception {
    Entity usuariosDelega = getUsuarioDelega(sesion, idPersona);
    return usuariosDelega == null;
  } // validaUsuarioDelega

  private Entity getUsuarioDelega(Session sesion, Long idPersona) throws Exception {
    Entity regresar = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_empleado= ".concat(idPersona.toString()).concat(" and id_usuario= ").concat(this.idUsuario.toString()));
      regresar = (Entity) DaoFactory.getInstance().toEntity(sesion, "TrJanalUsuariosDelegaDto", params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // getUsuarioDelega	

  public Long getIdUsuarioDelega(Session sesion, Long idPersona) throws Exception {
    Long regresar = -1L;
    Entity usuariosDelega = null;
    try {
      usuariosDelega = getUsuarioDelega(sesion, idPersona);
      if (usuariosDelega != null) {
        regresar = usuariosDelega.toLong("idUsuarioDelega");
      }
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // getIdUsuarioDelega

  public Entity toEmpleado(Session sesion) throws Exception {
    Entity regresar = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_persona= " + this.idPersona);
      regresar = (Entity) DaoFactory.getInstance().toEntity(sesion, "TcJanalPersonasDto", params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // toEmpleado

  public TcManticPersonasDto toEmpleado() throws Exception {
    TcManticPersonasDto regresar = null;
    try {
      regresar = (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, this.idPersona);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // toPersona

  public Entity toMunicipioEntidad(Long idCodigoPostal) throws Exception {
    Entity regresar = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idCodigoPostal", idCodigoPostal);
      regresar = (Entity) DaoFactory.getInstance().toEntity("VistaEntidadesMunicipiosDto", params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // toMunicipioEntidad

  public Entity toUsuario() throws Exception {
    Entity regresar = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idPersona", this.idPersona);
      regresar = (Entity) DaoFactory.getInstance().toEntity("VistaUsuariosDto", "findIdPersona", params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // toUsuario
}

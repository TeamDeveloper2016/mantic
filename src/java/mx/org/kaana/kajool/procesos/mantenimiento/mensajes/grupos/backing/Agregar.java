package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.grupos.backing;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.kajool.db.dto.TcJanalMensajesDto;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIMessage;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesGruposDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesPerfilesDto;
import mx.org.kaana.kajool.procesos.mantenimiento.mensajes.grupos.reglas.Transaccion;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/09/2015
 *@time 15:33:37
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@ManagedBean(name="kajoolMantenimientoMensajesGruposAgregar")
@ViewScoped
public class Agregar extends IBaseAttribute implements Serializable{

  private static final long serialVersionUID = -4975820187060608461L;

  @Override
  @PostConstruct
  protected void init() {
    EAccion accion = null;
    Long idMensaje = -1L;
    this.attrs.put("deshabilitar", false);
    try {
      accion = ((EAccion) JsfUtilities.getFlashAttribute("accion"));
      this.attrs.put("accion", accion);
      switch (accion) {
        case CONSULTAR:
        case MODIFICAR:
          this.attrs.put("deshabilitar", true);
          idMensaje = (Long) JsfBase.getFlashAttribute("idMensaje");
          this.attrs.put("valida", "dos");
          doLoad(idMensaje);
          break;
        case AGREGAR:
          this.attrs.put("idGrupo", new Long(-1));
          this.attrs.put("idPrioridad", new Long(-1));
          this.attrs.put("valida", "uno,dos");
          this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
          this.attrs.put("grupos", UISelect.build("TcJanalGruposDto", this.attrs, "descripcion"));
          this.attrs.put("prioridades", UISelect.build("TcJanalPrioridadesDto", this.attrs, "descripcion"));
          this.attrs.put("dto", new TcJanalMensajesDto());
          this.attrs.put("dtoMensajesGrupos", new TrJanalMensajesGruposDto());
          break;
      } // switch
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
   } //init

  private void doLoad(Long idMensaje) {
    try {
      this.attrs.put("dto", DaoFactory.getInstance().findById(TcJanalMensajesDto.class, idMensaje));
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfUtilities.addMessageError(e);
    }//catch
  } // doLoad

  public String doAccion() {
    EAccion accion = null;
    String regresar= null;
    try {
      accion = (EAccion) this.attrs.get("accion");
      switch (accion) {
        case CONSULTAR:
          regresar = "filtro".concat(Constantes.REDIRECIONAR);
          break;
        case MODIFICAR:
          regresar = doModificar();
        case AGREGAR:
          regresar = doAgregar();
          break;
      } // switch
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  } // doAccion

  public String doAgregar() {
    String regresar                                                                                    = null;
    TcJanalMensajesDto dto                                                                             = null;
    TrJanalMensajesGruposDto dtoMensajesGrupos                                                         = null;
    TrJanalMensajesPerfilesDto dtoMensajesPerfiles                                                     = null;
    List<Entity> perfiles                                                                              = null;
    List<Entity> usuarios                                                                              = null;
    Map<String, Object> params                                                                         = new HashMap();
    mx.org.kaana.kajool.procesos.mantenimiento.mensajes.perfiles.reglas.Transaccion transaccionPerfiles= null;
    mx.org.kaana.kajool.procesos.mantenimiento.mensajes.usuarios.reglas.Transaccion transaccionUsuarios= null;
    try {
      dto = (TcJanalMensajesDto) this.attrs.get("dto");
      dto.setIdPrioridad(Long.valueOf(this.attrs.get("idPrioridad").toString()));
      dto.setIdUsuario(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
      dto.setIdUsuarioModifica(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
      dto.setFechaRepite(new java.sql.Date((Calendar.getInstance().getTimeInMillis())));
      dto.setActualizacion("n");
      dtoMensajesGrupos = (TrJanalMensajesGruposDto) this.attrs.get("dtoMensajesGrupos");
      dtoMensajesGrupos.setIdGrupo(Long.valueOf(this.attrs.get("idGrupo").toString()));
      Transaccion transaccion = new Transaccion(dto, dtoMensajesGrupos);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        params.put("idGrupo", this.attrs.get("idGrupo").toString());
        perfiles= DaoFactory.getInstance().toEntitySet("TcJanalPerfilesDto", "porGrupo", params);
        for(Entity perfil: perfiles){
          dtoMensajesPerfiles = new TrJanalMensajesPerfilesDto();
          dtoMensajesPerfiles.setIdMensaje(dto.getIdMensaje());
          dtoMensajesPerfiles.setIdPerfil(perfil.toLong("idKey"));
          dtoMensajesPerfiles.setIdUsuario(dto.getIdUsuario());
          dtoMensajesPerfiles.setRegistro(dto.getRegistro());
          transaccionPerfiles= new mx.org.kaana.kajool.procesos.mantenimiento.mensajes.perfiles.reglas.Transaccion(dto, dtoMensajesPerfiles);
          if(transaccionPerfiles.ejecutar(EAccion.AGREGAR)){
            this.attrs.put("idPerfil", perfil.toLong("idKey"));
            usuarios= DaoFactory.getInstance().toEntitySet("TcJanalUsuariosDto", "findIds", this.attrs);
            for(Entity idUsuario: usuarios){
              transaccionUsuarios= new mx.org.kaana.kajool.procesos.mantenimiento.mensajes.usuarios.reglas.Transaccion(dto, idUsuario.toLong("idUsuario"), dtoMensajesPerfiles.getIdUsuario());
              transaccionUsuarios.ejecutar(EAccion.AGREGAR);
            } // for
          } // if
        } // for
        JsfBase.addMessage(UIMessage.toMessage("correcto_agregar"));
      } //if
      else {
        JsfBase.addMessage(UIMessage.toMessage("error_agregar"));
      } //else
    } //try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } //catch
    finally{
      Methods.clean(params);
      this.attrs.put("dto", new TcJanalMensajesDto());
    } // finally
    regresar = "filtro".concat(Constantes.REDIRECIONAR);
    return regresar;
  } //doAgregar

  public String doModificar(){
    String regresar = null;
    TcJanalMensajesDto dto = null;
    dto = (TcJanalMensajesDto) this.attrs.get("dto");
    Transaccion transaccion = new Transaccion(dto);
    try {
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        JsfBase.addMessage(UIMessage.toMessage("correcto_modificar"));
      } //if
      else {
        JsfBase.addMessage(UIMessage.toMessage("error_modificar"));
      } //else
    } //try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } //catch
    regresar = "filtro".concat(Constantes.REDIRECIONAR);
    return regresar;
  } //doModificar

}

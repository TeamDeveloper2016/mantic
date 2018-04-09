package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.perfiles.backing;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
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
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesPerfilesDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETiposMensajes;
import mx.org.kaana.kajool.procesos.mantenimiento.mensajes.perfiles.reglas.Transaccion;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 3/09/2015
 * @time 09:41:43 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name="kajoolMensajesPerfilesAgregar")
@ViewScoped
public class Agregar extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 7918385112923122551L;

  @Override
  @PostConstruct
  protected void init() {
    Long idMensaje = -1L;
    EAccion accion = null;
    this.attrs.put("deshabilitar", false);
    try {
      accion = ((EAccion) JsfUtilities.getFlashAttribute("accion"));
      this.attrs.put("accion", accion);
      this.attrs.put("idGrupo", new Long(-1));
      this.attrs.put("descripcion", "");
      this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      this.attrs.put("grupos", UISelect.build("TcJanalGruposDto", this.attrs, "descripcion"));
      this.attrs.put("prioridades", UISelect.build("TcJanalPrioridadesDto", this.attrs, "descripcion"));
      switch (accion) {
        case AGREGAR:
          this.attrs.put("dto", new TcJanalMensajesDto());
          this.attrs.put("dtoPerfil", new TrJanalMensajesPerfilesDto());
          this.attrs.put("valida", "uno,dos");
          break;
        case MODIFICAR:
          this.attrs.put("deshabilitar", true);
          idMensaje = (Long) JsfUtilities.getFlashAttribute("idMensaje");
          this.attrs.put("valida", "dos");
          doLoad(idMensaje);
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

  public String doAceptar() {
    EAccion accion = null;
    String regresar= "";
    try {
      accion = (EAccion) this.attrs.get("accion");
      switch (accion) {
        case MODIFICAR:
          regresar = doModificar();
          break;
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
  } //doAceptar

  public String doAgregar() {
    String regresar                      = null;
    TcJanalMensajesDto dto               = null;
    TrJanalMensajesPerfilesDto dtoPerfil = null;
    List<Entity> idsUsuarios             = null;
    mx.org.kaana.kajool.procesos.mantenimiento.mensajes.usuarios.reglas.Transaccion transaccionUsuarios= null;
    try {
      dto = (TcJanalMensajesDto) this.attrs.get("dto");
      dtoPerfil = (TrJanalMensajesPerfilesDto) this.attrs.get("dtoPerfil");
      dto.setIdPrioridad(Long.valueOf(this.attrs.get("idPrioridad").toString()));
      dto.setIdUsuario(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
      dto.setIdUsuarioModifica(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
      dto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      dto.setIdTipoMensaje(ETiposMensajes.PERFIL.getKey());
      dto.setFechaRepite(new java.sql.Date((Calendar.getInstance().getTimeInMillis())));
      dto.setActualizacion("n");
      dtoPerfil.setIdPerfil(Long.valueOf(this.attrs.get("idPerfil").toString()));
      dtoPerfil.setIdUsuario(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
      dtoPerfil.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      Transaccion transaccion = new Transaccion(dto, dtoPerfil);
      if (transaccion.ejecutar(EAccion.AGREGAR)) {
        idsUsuarios= DaoFactory.getInstance().toEntitySet("TcJanalUsuariosDto", "findIds", this.attrs);
        for(Entity idUsuario: idsUsuarios){
          transaccionUsuarios= new mx.org.kaana.kajool.procesos.mantenimiento.mensajes.usuarios.reglas.Transaccion(dto, idUsuario.toLong("idUsuario"), dtoPerfil.getIdUsuario());
          transaccionUsuarios.ejecutar(EAccion.AGREGAR);
        } // for
        JsfUtilities.addMessage(UIMessage.toMessage("correcto_agregar"));
      }// if
      else {
        JsfUtilities.addMessage(UIMessage.toMessage("error_agregar"));
      }// else
    } //try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } //catch
    finally{
      this.attrs.put("dto", new TcJanalMensajesDto());
    } // finally
    regresar = "filtro".concat(Constantes.REDIRECIONAR);
    return regresar;
  } //doAgregar

  public String doModificar() {
    String regresar                     = null;
    TcJanalMensajesDto dto              = null;
    try {
      dto = (TcJanalMensajesDto) this.attrs.get("dto");
      Transaccion transaccion = new Transaccion(dto);
      if (transaccion.ejecutar(EAccion.MODIFICAR))
        JsfUtilities.addMessage(UIMessage.toMessage("correcto_modificar"));
      else
        JsfUtilities.addMessage(UIMessage.toMessage("error_modificar"));
    } //try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } //catch
    regresar = "filtro".concat(Constantes.REDIRECIONAR);
    return regresar;
  } // doModificar

  public void doPerfiles() {
    this.attrs.put("idPerfil", new Long(-1L));
    this.attrs.put("perfiles", UISelect.build("TcJanalPerfilesDto", "porGrupo", this.attrs, "descripcion"));
  }
}

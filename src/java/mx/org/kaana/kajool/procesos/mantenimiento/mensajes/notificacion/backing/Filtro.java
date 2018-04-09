package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.notificacion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 18/09/2015
 *@time 05:11:44 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UtilAplicacion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesUsuariosDto;
import mx.org.kaana.kajool.enums.EMenus;
import mx.org.kaana.kajool.procesos.mantenimiento.mensajes.notificacion.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;

@ManagedBean(name="kajoolMantenimientoMensajesNotificacionFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final Log LOG = LogFactory.getLog(Filtro.class);
  private static final long serialVersionUID = 7683891542411947722L;
	
  @PostConstruct
  @Override
	protected void init() {
    try {
      this.attrs.put("sortOrder", " order by tr_janal_mensajes_usuarios.id_mensaje_usuario");
      this.attrs.put("mensaje", "");
      this.attrs.put("prioridad", " ");
      this.attrs.put("estatus", " ");
      this.attrs.put("idUsuario", JsfBase.getAutentifica().getEmpleado().getIdUsuario());
			LOG.debug(JsfBase.getFacesContext().getCurrentPhaseId());
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  @Override
  public void doLoad() {
		List<Columna> columnas	= new ArrayList();
    try {
      columnas.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
      this.attrs.put("idPrioridad", this.attrs.get("prioridad").toString().equals(" ")?"1,2,3":this.attrs.get("prioridad"));
      this.attrs.put("idBooleano", this.attrs.get("estatus").toString().equals(" ")?"1,2":this.attrs.get("estatus"));
			this.lazyModel= new FormatCustomLazy("VistaTrJanalMensajesUsuariosDto", "notificacion", this.attrs, columnas);
			UIBackingUtilities.resetDataTable();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(columnas);
		}//finally
  } // doLoad

  public void doLeido(Entity mensaje){
    Transaccion transaccion       = null;
    TrJanalMensajesUsuariosDto dto= null;
    Map<String, Object> params    = new HashMap();
    HttpSession session           = JsfBase.getSession();
    String encabezadoAnterior     = "";
    String encabezadoNuevo        = "";
    Long totalMensajes            = 0L;
    try {
      totalMensajes= new UtilAplicacion().getContadorMensajes();
      if(totalMensajes==0)
        encabezadoAnterior="<span id=\"contadorMensajes\" class=\"alertBubble BordRad10 Fs9\" style=\"display: none\">".concat(totalMensajes.toString()).concat("</span>");
      else
        encabezadoAnterior="<span id=\"contadorMensajes\" class=\"alertBubble BordRad10 Fs9\">".concat(totalMensajes.toString()).concat("</span>");
      dto= new TrJanalMensajesUsuariosDto();
      dto.setKey(mensaje.getKey());
      params.put("idBooleano", mensaje.toLong("idBooleano")==1L? 2L: 1L);
      transaccion= new Transaccion(dto, params);
      transaccion.ejecutar(EAccion.MODIFICAR);
      if(totalMensajes==0)
        encabezadoNuevo="<span id=\"contadorMensajes\" class=\"alertBubble BordRad10 Fs9\" style=\"display: none\">".concat(totalMensajes.toString()).concat("</span>");
      else
        encabezadoNuevo="<span id=\"contadorMensajes\" class=\"alertBubble BordRad10 Fs9\">".concat(totalMensajes.toString()).concat("</span>");
      session.setAttribute(EMenus.ENCABEZADO.getVariableSesion(), session.getAttribute(EMenus.ENCABEZADO.getVariableSesion()).toString().replace(encabezadoAnterior, encabezadoNuevo));
      RequestContext.getCurrentInstance().execute("Sentinel.updateNotifications(".concat(totalMensajes.toString()).concat(")"));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public Long getIdBooleano(Value idBooleano){
    return idBooleano.toLong();
  }
}

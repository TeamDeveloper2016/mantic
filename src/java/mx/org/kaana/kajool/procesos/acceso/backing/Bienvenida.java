package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.procesos.acceso.beans.Persona;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.enums.EPerfiles;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "kajoolBienvenida")
@ViewScoped
public class Bienvenida extends Comun implements Serializable {

  private static final long serialVersionUID = 5323749709626263801L;
  private static final Log LOG = LogFactory.getLog(Bienvenida.class);  

  @PostConstruct
  @Override
  protected void init() {
    Persona empleado = null;
    try {
      empleado = JsfBase.getAutentifica().getPersona();
      this.attrs.put("isAdmin", JsfBase.isAdminEncuestaOrAdmin());
      this.attrs.put("titulotabla", EPerfiles.fromOrdinal(empleado.getIdPerfil()).getTituloTabla());
      this.attrs.put("isTablaGeneral", EPerfiles.CAPTURISTA.getKey().equals(empleado.getIdPerfil()));
      this.attrs.put("pathConfiguracion", JsfBase.getApplication().getContextPath() + "/Paginas/Utilerias/InformacionSistema/filtro.jsf");
      this.attrs.put("pathCaptura", JsfBase.getApplication().getContextPath() + "/Paginas/Captura/filtro.jsf");
      this.attrs.put("pathMensajes", JsfBase.getApplication().getContextPath() + "/Paginas/Mantenimiento/Mensajes/Notificacion/filtro.jsf");
      this.attrs.put("vigenciaInicial", new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
      this.attrs.put("vigenciaFin", new java.sql.Date(Calendar.getInstance().getTimeInMillis()));                                      
      toMensajesNoLeidos();            			
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch        
  }

  @Override
  public void doLoad() {
    List<Columna> campos= null;
    try {
      campos = new ArrayList<>();      
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idUsuario", JsfBase.getIdUsuario());
      this.attrs.put("codigo", "");
      this.attrs.put("almacen", "");
      this.attrs.put("expresion", "");
      this.attrs.put("sortOrder", "");
      this.lazyModel = new FormatLazyModel("VistaArticulosDto", "row", this.attrs, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
    } // catch
  } // doLoad  

  private void toMensajesNoLeidos() throws Exception {
    Entity mensajesNoLeidos = null;
    try {
      mensajesNoLeidos = (Entity) DaoFactory.getInstance().toEntity("VistaTrJanalMensajesUsuariosDto", "contadorNoLeidos", this.attrs);
      this.attrs.put("mensajesNoLeidos", mensajesNoLeidos != null ? mensajesNoLeidos.toString("cantidad") : "0");
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
  }  
}

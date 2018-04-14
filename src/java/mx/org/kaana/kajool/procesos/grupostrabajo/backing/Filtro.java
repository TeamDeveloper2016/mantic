package mx.org.kaana.kajool.procesos.grupostrabajo.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.enums.EPerfiles;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 11/10/2016
 * @time 11:49:11 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>@kaana.org.mx>
 */
@Named(value = "kajoolGruposTrabajoFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final Log LOG = LogFactory.getLog(Filtro.class);

  @PostConstruct
  @Override
  protected void init() {
    try {
      doCargarEntidades();
      doCargarPerfiles();
      doLoad();
      LOG.debug(JsfUtilities.getFacesContext().getCurrentPhaseId());
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // init

  @Override
  public void doLoad() {
    List<Columna> columnas = new ArrayList<>();
    try {
      columnas.add(new Columna("descEntidad", EFormatoDinamicos.MAYUSCULAS));
      columnas.add(new Columna("descPerfil", EFormatoDinamicos.MAYUSCULAS));
      columnas.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columnas.add(new Columna("primerApellido", EFormatoDinamicos.MAYUSCULAS));
      columnas.add(new Columna("segundoApellido", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatLazyModel("VistaGruposTrabajo", "row", this.attrs, columnas);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(columnas);
    }//finally
  } // doLoad

  public void doCargarEntidades() {
    List<String> fields = null;
    try {
      fields = new ArrayList();
      fields.add("clave");
      fields.add("descripcion");
      this.attrs.put("sortOrder", "");
      this.attrs.put(Constantes.SQL_CONDICION, "id_pais=1");
      this.attrs.put("listaEntidades", UISelect.build("TcJanalEntidadesDto", "mto", this.attrs, fields, " ", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("idEntidad", UIBackingUtilities.toFirstKeySelectItem((List<UISelectItem>) this.attrs.get("listaEntidades")));
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(fields);
    } // finally
  }

  public void doCargarPerfiles() {
    List<String> fields = null;
    try {
      fields = new ArrayList();
      fields.add("descripcion");
      this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      this.attrs.put("listaPerfiles", UISelect.build("TcJanalPerfilesDto", "row", this.attrs, fields, " ", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("idPerfil", UIBackingUtilities.toFirstKeySelectItem((List<UISelectItem>) this.attrs.get("listaPerfiles")));
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(fields);
    } // finally			
  }

  public void doConfigMensaje(ActionEvent event) {
    Entity entity = (Entity) event.getComponent().getAttributes().get("current");
    doConfigMensaje(entity);
  }

  public void doConfigMensaje(Entity entity) {
    try {
      this.attrs.put("seleccionado", entity);
      StringBuilder mensaje = new StringBuilder();
      mensaje.append(entity.toString("activo").equals("0") ? "desactivar " : " activar ");
      mensaje.append(" la cuenta de acceso de [");
      mensaje.append(entity.toString("cuenta"));
      mensaje.append("]");
      this.attrs.put("mensajeAlerta", mensaje);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }

  public void doEvento(String evento) {
    try {

    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // exception
  }

}

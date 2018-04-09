package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import org.primefaces.component.dialog.Dialog;
import java.util.UUID;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/09/2015
 *@time 16:23:12 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class KajoolDialog extends Dialog implements IComponent{

  private static String render = "org.primefaces.component.DialogRenderer";
  private String id ="";
  private String widgetVar= "";

  public KajoolDialog() {
    this(UUID.randomUUID().toString(),UUID.randomUUID().toString());
  }

  public KajoolDialog(String id, String widgetVar) {
    this.id = id;
    this.widgetVar= widgetVar;
  }

  @Override
  public UIComponentBase create() {
    Dialog regresar = (Dialog) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), Dialog.COMPONENT_TYPE, this.render);
    regresar.setId(this.id);
    regresar.setWidgetVar(this.widgetVar);
    return regresar;
  }

}

package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import org.primefaces.component.panel.Panel;
import java.util.UUID;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/09/2015
 *@time 10:29:33 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class KajoolPanel extends Panel implements IComponent{

  private static String render = "org.primefaces.component.PanelRenderer";
  private String id ="";

  public KajoolPanel() {
    this(UUID.randomUUID().toString());
  }

  public KajoolPanel(String id) {
    this.id = id;
  }

  @Override
  public UIComponentBase create() {
    Panel regresar = (Panel) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), Panel.COMPONENT_TYPE, this.render);
    regresar.setId(this.id);
    return regresar;
  }

}

package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import java.util.List;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.UUID;
import javax.el.ValueExpression;
import javax.faces.component.UISelectItems;
import mx.org.kaana.libs.pagina.UISelectItem;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 14:20:41 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolSelectOneMenu extends SelectOneMenu implements IComponent {

  private static String render = "org.primefaces.component.SelectOneMenuRenderer";
  private String id;
  private ValueExpression value;
  private List<UISelectItem> items;

  public KajoolSelectOneMenu() {
    this(UUID.randomUUID().toString());
  }

  public KajoolSelectOneMenu(String id) {
    this(id, null, null);
  }

  public KajoolSelectOneMenu(String id, ValueExpression value) {
    this(id, value, null);
  }

  public KajoolSelectOneMenu(String id, List<UISelectItem> items) {
    this(id, null, items);
  }

  public KajoolSelectOneMenu(String id, ValueExpression value, List<UISelectItem> items) {
    this.id = id;
    this.value = value;
    this.items = items;
  }

  @Override
  public UIComponentBase create() {
    SelectOneMenu regresar = (SelectOneMenu) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), SelectOneMenu.COMPONENT_TYPE, this.render);
    regresar.setId(this.id);
    regresar.setValueExpression("value", this.value);
    UISelectItems selectItems = new UISelectItems();
    selectItems.setValue(this.items);
    regresar.getChildren().add(selectItems);
    return regresar;
  }
}

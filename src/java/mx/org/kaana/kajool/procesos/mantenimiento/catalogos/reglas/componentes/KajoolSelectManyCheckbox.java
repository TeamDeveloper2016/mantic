package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import java.util.List;
import java.util.ArrayList;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.UUID;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 16:43:21 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolSelectManyCheckbox extends SelectManyCheckbox implements IComponent {

  private static String render = "org.primefaces.component.SelectManyCheckboxRenderer";
  private String id;
  private String value;

  public KajoolSelectManyCheckbox() {
    this(UUID.randomUUID().toString());
  }

  public KajoolSelectManyCheckbox(String id) {
    this(id, "");
  }

  public KajoolSelectManyCheckbox(String id, String value) {
    this.id = id;
    this.value = value;
  }

  @Override
  public UIComponentBase create() {
    SelectManyCheckbox regresar = (SelectManyCheckbox) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), SelectManyCheckbox.COMPONENT_TYPE, this.render);
    regresar.setId(this.id);
    regresar.setValue(this.value);

    final UISelectItem select = (UISelectItem) FacesContext.getCurrentInstance().getApplication().createComponent(UISelectItem.COMPONENT_TYPE);
    List<SelectItem> items = new ArrayList<SelectItem>();
    for (int k = 0; k < 5; k++) {
      items.add(new SelectItem(k,"Valor "+k));
    }
    UISelectItems selectItems = new UISelectItems();
    selectItems.setValue(items);
    regresar.getChildren().add(selectItems);

    regresar.setValue(items);
    return regresar;
  }
}

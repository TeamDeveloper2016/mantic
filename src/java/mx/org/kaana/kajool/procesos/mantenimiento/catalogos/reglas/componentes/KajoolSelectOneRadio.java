package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import java.util.List;
import java.util.ArrayList;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.UUID;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;
import org.primefaces.component.selectoneradio.SelectOneRadio;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 16:38:41 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolSelectOneRadio extends SelectOneRadio implements IComponent {

  private static String render = "org.primefaces.component.SelectOneRadioRenderer";
  private String id;
  private String value;

  public KajoolSelectOneRadio() {
    this(UUID.randomUUID().toString());
  }

  public KajoolSelectOneRadio(String id) {
    this(id, "");
  }

  public KajoolSelectOneRadio(String id, String value) {
    this.id = id;
    this.value = value;
  }

  @Override
  public UIComponentBase create() {
    SelectOneRadio regresar = (SelectOneRadio) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), SelectOneRadio.COMPONENT_TYPE, this.render);
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

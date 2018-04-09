package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.component.UISelectItems;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.Criterio;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldForaneo;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/10/2015
 * @time 01:54:42 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Busqueda implements Serializable {

  private static final long serialVersionUID = -8457128150078981812L;

  private Criterio criterio;
  private String value;
  private List<UISelectItem> items;
  private Long selected;

  public Busqueda(Criterio criterio) {
    this.criterio= criterio;
    this.value   = "";
    this.items   = new ArrayList<>();
  }

  public Criterio getCriterio() {
    return criterio;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public List<UISelectItem> getItems() {
    return items;
  }

  public Long getSelected() {
    return selected;
  }

  public void setSelected(Long selected) {
    this.selected = selected;
  }

  public void toLookUpItems() {
    this.items.addAll(((FieldForaneo)this.criterio).toItems(this.criterio.getNombre().concat(" like '%".concat(this.value).concat("%'"))));
  }

  @Override
  public String toString() {
    return "Busqueda{" + "criterio=" + criterio + ", value=" + value + '}';
  }

  public void toSelected() {
    int index= this.items.indexOf(new UISelectItem(this.selected));
    List<UISelectItem> selectItems= ((FieldForaneo)this.criterio).getItems();
    if(index> 0)
      if(selectItems.isEmpty())
        selectItems.add(this.items.get(index));
      else
        selectItems.set(0, this.items.get(index));
  }

}

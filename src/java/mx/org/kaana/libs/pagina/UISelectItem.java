package mx.org.kaana.libs.pagina;

import java.io.Serializable;
import javax.faces.model.SelectItem;

public class UISelectItem extends SelectItem implements Serializable  {

  private static final long serialVersionUID = -8719815632091938836L;

  public UISelectItem(Object key) {
    this(key, "");
  }

  public UISelectItem(Object key, String label) {
    this(key, label, false);
  }

  public UISelectItem(Object key, String label, boolean disabled) {
    super();
    setLabel(label);
    setValue(key);
    setDisabled(disabled);
  }

  public boolean equals(Object object) {
    boolean regresar = false;
    if(object== null)
      return false;
    if(object.getClass()!= getClass())
      return false;

    final UISelectItem other= (UISelectItem)object;
    if(getValue() != other.getValue() && (getValue()== null || !getValue().equals(other.getValue())))
      return false;
    return true;
  }

  public int hashCode() {
    int hash= 8;
    hash= hash* 56* (getValue()!= null? getValue().hashCode(): 0);
    return hash;
  }

	@Override
	public String toString() {
		return "UISelectItem{value="+ getValue()+ ", label="+ getLabel()+ ", disabled="+ isDisabled()+ '}';
	}
	
}

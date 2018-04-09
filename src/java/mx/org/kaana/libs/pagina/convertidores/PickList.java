package mx.org.kaana.libs.pagina.convertidores;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 27/01/2015
 *@time 02:28:49 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.UISelectItem;

@FacesConverter("janal.convertidor.PickList")
public class PickList implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		String[] itemValues = value.split(Constantes.AMPERSON);
    Object itemKey = itemValues[0];
    String itemLabel = itemValues[1];
		return new UISelectItem(itemKey, itemLabel);
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object value) {
		String regresar= null;
		if(value!= null) {
			UISelectItem item = (UISelectItem)value;
			regresar = item.getValue().toString().concat(Constantes.AMPERSON).concat(item.getLabel());
		} // if
	  return regresar;
	}

}

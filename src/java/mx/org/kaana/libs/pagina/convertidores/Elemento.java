package mx.org.kaana.libs.pagina.convertidores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.kajool.beans.SelectionItem;

/**
 *@company KAANA
 *@project  KAJOOL (Control system polls)
 *@date Oct 11, 2012
 *@time 5:04:47 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.SelectionItem")
public class Elemento implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		return new SelectionItem(value, "");
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object value) {
		String regresar= null;
		if(value!= null) {
			SelectionItem item = (SelectionItem)value;
			regresar = item.getKey().toString();
		}
	  return regresar;
	}

}

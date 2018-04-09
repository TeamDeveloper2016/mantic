package mx.org.kaana.libs.pagina.convertidores;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.kajool.catalogos.beans.SqlField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.component.orderlist.OrderList;
import org.primefaces.component.picklist.PickList;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.DualListModel;

/**
 *@company KAANA
 *@project  KAJOOL (Control system polls)
 *@date Dic 20, 2012
 *@time 16:18:47 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.Campo")
public class Campo implements Converter {

	private static Log LOG = LogFactory.getLog(Campo.class);
	
	private SqlField toDualList(List items, String name) {
		SqlField regresar= null;
		if(items!= null && items.indexOf(new SqlField(name))>= 0)
			regresar= (SqlField)items.get(items.indexOf(new SqlField(name)));
		return regresar;
	}
	
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		SqlField regresar = null;
		if(uic instanceof PickList) {
		  DualListModel dual= (DualListModel)((PickList)uic).getValue();
			regresar          = toDualList(dual.getTarget(), value);
		} // if
		else
			if(uic instanceof OrderList) {
  			regresar= toDualList((List)((OrderList)uic).getValue(), value);
			} // if
		  else
				 if(uic instanceof SelectOneMenu) {
					 regresar= toDualList((List)((UISelectItems)uic.getChildren().get(0)).getValue(), value);
				 } // if
		if(regresar== null) {
			FacesMessage message= new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo convertir el tipo de campo '"+ value+ "'");
			throw new ConverterException(message);
		}	// if
		return regresar;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object value) {
		String regresar= null;
		if(value!= null)
			regresar = ((SqlField)value).getName();
	  return regresar;
	}

}

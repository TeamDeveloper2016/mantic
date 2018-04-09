package mx.org.kaana.libs.pagina;

import mx.org.kaana.libs.formato.Error;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.kajool.enums.EAccion;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 22, 2012
 *@time 11:13:49 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("mx.org.kaana.kajool.Accion")
public class Accion implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object regresar= EAccion.LISTAR;
		try {
			regresar= EAccion.valueOf(value);
		} // try
		catch(Exception e) {
			Error.mensaje(e);
      FacesMessage msg = new FacesMessage("Error en la conversion de la acción.",  "El nombre de la accion no es valido.["+ value+ "]");
	  	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
  	  throw new ConverterException(msg);
		} // catch
		return regresar;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar = "El nombre de la acción es invalido";
    if(value instanceof EAccion)
			regresar=  ((EAccion)value).name();
  	return regresar;
	}

}

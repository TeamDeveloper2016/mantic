package mx.org.kaana.libs.pagina.convertidores;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.kajool.enums.ESubTotales;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 30, 2012
 *@time 10:39:43 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.SubTotales")
public class SubTotales  implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
		ESubTotales regresar= null;
		if(value!= null) {
			try {
			  regresar= ESubTotales.valueOf(value);	
			} // try
			catch(Exception e) {
        FacesMessage msg = new FacesMessage("El nombre de la funcion de calculo para los subtotales no se pudo convertir.",  "El nombre de la funcion de calculo para los subtotales no existe.["+ value+ "]");
	  		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		  	throw new ConverterException(msg, e);
			} // catch
		} // if	
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
		String regresar= null;
		if(value!= null)
			if(value instanceof ESubTotales)
			  regresar= ((ESubTotales)value).name();
		  else
				regresar= value.toString();
    return regresar;
  }

}

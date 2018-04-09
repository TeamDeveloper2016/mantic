package mx.org.kaana.libs.pagina.convertidores;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Numero;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 30, 2012
 *@time 10:39:43 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.Formato")
public class Formato  implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
		EFormatoDinamicos regresar= null;
		if(value!= null) {
			try {
			  regresar= EFormatoDinamicos.valueOf(value);	
			} // try
			catch(Exception e) {
        FacesMessage msg = new FacesMessage("El formato no se pudo convertir.",  "El nombre del formato no existe.["+ value+ "]");
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
			if(value instanceof EFormatoDinamicos)
			  regresar= ((EFormatoDinamicos)value).name();
		  else
				regresar= value.toString();
    return regresar;
  }

}

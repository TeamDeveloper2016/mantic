package mx.org.kaana.libs.pagina.convertidores;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.kajool.enums.EAlineacion;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 30, 2012
 *@time 10:39:43 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.Alineacion")
public class Alineacion implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
		HorizontalAlignment regresar= null;
		if(value!= null) {
			try {
			  regresar= EAlineacion.valueOf(value).getAlign();	
			} // try
			catch(Exception e) {
        FacesMessage msg = new FacesMessage("El nombre de la alineación no se pudo convertir.",  "El nombre de la alineación no existe.["+ value+ "]");
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
			if(value instanceof EAlineacion)
			  regresar= ((EAlineacion)value).name();
		  else
				regresar= value.toString();
    return regresar;
  }

}

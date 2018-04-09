package mx.org.kaana.libs.pagina.convertidores;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.formato.Numero;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Apr 26, 2012
 * @time 12:50:07 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.Color")
public class Color implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//java.awt.Color[r=255,g=175,b=175]
		java.awt.Color regresar= null;
		if(value!= null) {
			if(value.indexOf("=")> 0) {
			  int r=  Numero.getInteger(value.substring(value.indexOf("r=")+ 2, value.indexOf(",g")), -1);
			  int g=  Numero.getInteger(value.substring(value.indexOf("g=")+ 2, value.indexOf(",b")), -1);
			  int b=  Numero.getInteger(value.substring(value.indexOf("b=")+ 2, value.indexOf("]")) , -1);
			  regresar= new java.awt.Color(r, g, b);
			}
			else {
				int numero= Numero.getInteger(value, -1);
				if(numero>= 0)
  			  regresar= new java.awt.Color(numero);
  			else {
          FacesMessage msg = new FacesMessage("El color no se pudo convertir.",  "El número de color no es valido.["+ value+ "]");
	    		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		    	throw new ConverterException(msg);
			  }	// else
			} // else	
		} // if	
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
		String regresar= null;
		if(value!= null)
			regresar= ((java.awt.Color)value).toString();
    return regresar;
  }
	
}

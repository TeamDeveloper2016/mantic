package mx.org.kaana.libs.pagina.convertidores;

import java.sql.Date;
import mx.org.kaana.libs.formato.Error;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.enums.ETipoMensaje;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Apr 26, 2012
 * @time 12:50:07 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.Fecha")
public class Fecha implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    Date regresar= null;
    if(value!= null && !value.equals(""))
      try {
        regresar= new Date(mx.org.kaana.libs.formato.Fecha.getFechaCalendar(value).getTimeInMillis());
      } // try
      catch (Exception e){
        Error.mensaje(e);
        JsfUtilities.addMessage("EL formato de fecha es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato de fecha es incorrecto");
      } // catch
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar= null;
    if(value!= null)
      try {				
 	      regresar= mx.org.kaana.libs.formato.Fecha.formatear(mx.org.kaana.libs.formato.Fecha.FECHA_CORTA, (Date)value);
      } // try
      catch (Exception e){
        Error.mensaje(e);
        JsfUtilities.addMessage("EL formato de fecha es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato de fecha es incorrecto");
      } // catch
    return regresar;
  }
	
}

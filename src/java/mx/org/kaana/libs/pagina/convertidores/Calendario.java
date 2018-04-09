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
import mx.org.kaana.libs.formato.Fecha;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Apr 26, 2012
 * @time 12:50:07 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.Calendario")
public class Calendario implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    java.util.Date date     = null;
    boolean conversionError = false;
    if (value == null || (value.trim().length() == 0)) {
      conversionError = true;
    } // if
    else {
      try {
        if(!value.contains(":"))
          date = new Date(Fecha.getFechaHora(value).getTimeInMillis());
        else
          // suponiendo que me llega solo la hora HH:MM:SS concatenamos anio default de time 01/01/1970
          if(!value.contains("/"))
            date = new Time(Fecha.getFechaHora("01/01/1970 ".concat(value)).getTimeInMillis());
					else	
            date = new Timestamp(Fecha.getFechaHora(value).getTimeInMillis());
      } // try
      catch(Exception e) {
        conversionError = true;
      } // catch
    } // else
    if(conversionError) {
      FacesMessage msg = new FacesMessage("La fecha no se pudo convertir.",  "El formato de la fecha es invalido.["+ value+ "]");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
    } // if
    return date;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar = "El formato de la fecha es invalida";
    if(value instanceof Date) {
      regresar = Fecha.formatear(Fecha.FECHA_CORTA, value.toString().replaceAll("-", ""));
    } // if
		else
      if(value instanceof Time) {
        regresar = Fecha.formatear(Fecha.HORA_CORTA, value.toString().replaceAll("-", ""));
      } // if
      else
				if (value instanceof Timestamp) {
          String limpio = value.toString().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
          regresar = Fecha.formatear(Fecha.FECHA_HORA, limpio.substring(0, limpio.indexOf(".")));
        } // else-if
    return regresar;
  }
	
}

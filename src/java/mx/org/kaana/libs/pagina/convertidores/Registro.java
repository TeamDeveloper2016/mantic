package mx.org.kaana.libs.pagina.convertidores;

import java.sql.Timestamp;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import org.primefaces.component.calendar.Calendar;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Apr 26, 2012
 * @time 12:50:07 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter(value="janal.convertidor.Registro")
public class Registro implements Converter{

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    Timestamp regresar= null;
    if(value!= null)
      try {
				((Calendar)component).getValue();
        regresar= new Timestamp(mx.org.kaana.libs.formato.Fecha.getFechaHora(value).getTimeInMillis());
      } // try
      catch(Exception e) {
        Error.mensaje(e);
        JsfUtilities.addMessage("El formato de registro es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("El formato de registro es incorrecto");
      } // catch
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar= null;
    if(value!= null)
      try{
        regresar= Fecha.formatear(Fecha.FECHA_HORA, value.toString().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
      } // try
      catch(Exception e) {
        Error.mensaje(e);
        JsfUtilities.addMessage("El formato de registro es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("El formato de registro es incorrecto");
      } // catch
    return regresar;
  }
	
}

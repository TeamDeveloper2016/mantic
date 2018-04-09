package mx.org.kaana.libs.pagina.convertidores.entity;

import java.sql.Timestamp;
import mx.org.kaana.libs.formato.Error;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.db.comun.sql.Value;
/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 3, 2012
 *@time 8:43:59 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.entity.Registro")
public class Registro implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    Value regresar= null;
    if(value!= null)
      try {
        regresar= new Value("registro", new Timestamp(mx.org.kaana.libs.formato.Fecha.getFechaHora(value).getTimeInMillis()));
      } // try
      catch (Exception e){
        Error.mensaje(e);
        JsfUtilities.addMessage("EL formato de la fecha y hora es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato de la fecha y hora es incorrecto");
      } // catch
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar= null;
    if(((Value) value).getData()!= null)
      try {
				regresar= mx.org.kaana.libs.formato.Fecha.formatear(mx.org.kaana.libs.formato.Fecha.FECHA_HORA, (Timestamp)((Value) value).getData());
      } // try
      catch (Exception e){
        Error.mensaje(e);
        JsfUtilities.addMessage("EL formato de fecha y hora es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato de fecha y hora es incorrecto");
      } // catch
    return regresar;
  }
	
}

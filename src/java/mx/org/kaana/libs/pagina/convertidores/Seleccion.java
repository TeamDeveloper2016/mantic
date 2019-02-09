package mx.org.kaana.libs.pagina.convertidores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UISelectItem;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Apr 26, 2012
 * @time 12:50:07 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter(value="janal.convertidor.Seleccion")
public class Seleccion implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    UISelectItem regresar= null;
    if(value!= null)
      try {
        regresar= new UISelectItem(Long.valueOf(value), "janal");
      } // try
      catch(Exception e) {
        Error.mensaje(e);
        JsfUtilities.addMessage("El valor de la caja de selección no es numerico", ETipoMensaje.ERROR);
        throw new ConverterException("El valor de la caja de selección no es numerico");
      } // catch
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar= null;
    if(value!= null)
      try {
        regresar= String.valueOf(value);
      } // try
      catch(Exception e) {
        Error.mensaje(e);
        JsfUtilities.addMessage("El valor del Entity es nulo o esta vacio", ETipoMensaje.ERROR);
        throw new ConverterException("El valor del Entity es nulo o esta vacio");
      } // catch
    return regresar;
  }
	
}

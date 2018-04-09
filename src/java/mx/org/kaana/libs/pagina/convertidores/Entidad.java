package mx.org.kaana.libs.pagina.convertidores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.pagina.UISelectEntity;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Apr 26, 2012
 * @time 12:50:07 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter(value="janal.convertidor.Entity")
public class Entidad implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    UISelectEntity regresar= null;
    if(value!= null)
      try {
        regresar= new UISelectEntity(value);
      } // try
      catch(Exception e) {
        Error.mensaje(e);
        JsfUtilities.addMessage("El idKey de la lista no existe en la coleccion", ETipoMensaje.ERROR);
        throw new ConverterException("El idKey de la lista no existe en la coleccion");
      } // catch
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar= null;
    if(value!= null)
      try {
        regresar= value.toString();
      } // try
      catch(Exception e) {
        Error.mensaje(e);
        JsfUtilities.addMessage("El idKey del Entity es nulo o esta vacio", ETipoMensaje.ERROR);
        throw new ConverterException("El idKey del Entity es nulo o esta vacio");
      } // catch
    return regresar;
  }
	
}

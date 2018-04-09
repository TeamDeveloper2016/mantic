package mx.org.kaana.libs.pagina.convertidores;

import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfUtilities;

import mx.org.kaana.kajool.enums.ETipoMensaje;


/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Apr 26, 2012
 * @time 12:50:07 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.Mapa")
public class Mapa implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    Map regresar= null;
		try {
      if (value != null)
  		  regresar=Variables.toMap(value, Constantes.SEPARADOR.charAt(0), Constantes.TILDE.charAt(0));
		}
		catch (Exception e) {
      Error.mensaje(e);
      JsfUtilities.addMessage("El formato no es invalido.[".concat(value).concat("]"), ETipoMensaje.ERROR);
			throw new ConverterException("El formato no es invalido.[".concat(value).concat("]"));
		} // try
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
 		String regresar= null;
    try {
      if (value != null) {
        Map map= (Map)value;
        regresar= map.toString();
        regresar= regresar.substring(1, regresar.length()- 1).replace(", ", "|");
        regresar= regresar.substring(0, regresar.length()).replace("=", "~");
      } // if		
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfUtilities.addMessage("El formato no es invalido", ETipoMensaje.ERROR);
			throw new ConverterException("El formato no es invalido");
    } // catch
		return regresar;
  }

	
}

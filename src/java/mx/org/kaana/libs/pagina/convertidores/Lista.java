package mx.org.kaana.libs.pagina.convertidores;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.formato.Cadena;
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

@FacesConverter("janal.convertidor.Lista")
public class Lista implements Converter {

 @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    List<String> regresar= null;
    try {
      regresar= Variables.toList(value, '|');
    }
    catch (Exception e){
      JsfUtilities.addMessage("El formato de la lista es invalido.[".concat(value).concat("]"), ETipoMensaje.ERROR);
			throw new ConverterException("El formato de la lista es invalido.[".concat(value).concat("]"));
    } // catch
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar = null;
    if(value != null) {
      try {
        regresar = Cadena.reemplazarCaracter(value.toString(), ',', '|') ;
        regresar = regresar.substring(1, regresar.length()-1);
      } // try
      catch (Exception e) {
        Error.mensaje(e);
      } // catch
    } // try
    return regresar;
  }
	
}

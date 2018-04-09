package mx.org.kaana.libs.pagina.convertidores.entity;

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
 *@date Jul 12, 2012
 *@time 8:25:10 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.entity.Texto")
public class Texto  implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    Value regresar= null;
    if(value!= null)
      try {
        regresar= new Value("texto", value);
      } // try
      catch (Exception e){
        mx.org.kaana.libs.formato.Error.mensaje(e);
        JsfUtilities.addMessage("EL cadena es incorrecta", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato de la cadena incorrecta");
      } // catch
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar= null;
    if(value!= null)
      try {
				regresar= ((Value)value).toString();
      } // try
      catch (Exception e){
        mx.org.kaana.libs.formato.Error.mensaje(e);
        JsfUtilities.addMessage("EL formato de la cadena es incorrecta", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato de la cadena es incorrecta");
      } // catch
    return regresar;
  }

}

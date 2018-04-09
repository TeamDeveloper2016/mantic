package mx.org.kaana.libs.pagina.convertidores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.pagina.JsfUtilities;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Oct 22, 2012
 *@time 12:21:11 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.Entero")
public class Entero implements Converter{

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    Integer regresar= null;
    if(value!= null)
      try {
        regresar=  Numero.getInteger(Cadena.eliminaCaracter(value, ','), null);
      } // try
      catch (Exception e){
        mx.org.kaana.libs.formato.Error.mensaje(e);
        JsfUtilities.addMessage("EL número entero es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato del número entero es incorrecto");
      } // catch
    return regresar;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    String regresar= null;
    if(value!= null)
      try {
				regresar= ((Integer)value).toString();
      } // try
      catch (Exception e){
        mx.org.kaana.libs.formato.Error.mensaje(e);
        JsfUtilities.addMessage("EL formato del número entero es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato del número entero es incorrecto");
      } // catch
    return regresar;
  }

}

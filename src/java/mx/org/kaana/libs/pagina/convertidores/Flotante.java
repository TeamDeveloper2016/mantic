package mx.org.kaana.libs.pagina.convertidores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.enums.ETipoMensaje;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Feb 18, 2013
 *@time 4:03:16 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.Flotante")
public class Flotante implements Converter{

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		Double regresar	= null;
		if(value!= null)
			try {
        regresar=  Numero.getDouble(Cadena.eliminaCaracter(value, ','), null);
      } // try
      catch (Exception e){
        mx.org.kaana.libs.formato.Error.mensaje(e);
        JsfUtilities.addMessage("EL número flotante es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato del número flotante es incorrecto");
      } // catch
		return regresar;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object value) {
		String regresar= null;
    if(value!= null)
      try {
				regresar= ((Double)value).toString();
      } // try
      catch (Exception e){
        mx.org.kaana.libs.formato.Error.mensaje(e);
        JsfUtilities.addMessage("EL formato del número flotante es incorrecto", ETipoMensaje.ERROR);
        throw new ConverterException("EL formato del número flotante es incorrecto");
      } // catch
    return regresar;
	}

}

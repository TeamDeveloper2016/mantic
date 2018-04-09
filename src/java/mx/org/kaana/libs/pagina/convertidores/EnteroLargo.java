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
 *@date Apr 23, 2015
 *@time 10:07:15 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@FacesConverter("janal.convertidor.EnteroLargo")
public class EnteroLargo implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Long regresar= null;
		if(value!= null)
			try {
				regresar= Numero.getLong(Cadena.eliminaCaracter(value, ','), null);
			} // try
			catch (Exception e) {
				mx.org.kaana.libs.formato.Error.mensaje(e);
				JsfUtilities.addMessage("El numero entero es iincorrecto", ETipoMensaje.ERROR);
				throw new ConverterException("El formato del numero entero es incorrecto");
			} // catch
		return regresar;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String regresar= null;
		if(value!= null)
			try {
				regresar= ((Long)value).toString();
			} // try
			catch (Exception e) {
				mx.org.kaana.libs.formato.Error.mensaje(e);
				JsfUtilities.addMessage("El numero entero es incorrecto", ETipoMensaje.ERROR);
				throw new ConverterException("El formato del numero enetero es incorrecto");
			} // catch
		return regresar;
	}

}

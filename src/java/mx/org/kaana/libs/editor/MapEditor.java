/*
 * Code write by user.development
 * Date 22/09/2008
 */

package mx.org.kaana.libs.editor;

import java.beans.PropertyEditorSupport;
import java.util.Map;
import mx.org.kaana.libs.editor.exception.MapException;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Variables;

/**
 *
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class MapEditor extends PropertyEditorSupport {
  /**
 * Obtiene un String de un mapa separados por | los elementos
 * @return
 */
	@Override
	public String getAsText() {
		String regresar= null;
		Map map= (Map)getValue();
		if (map== null)
			regresar= "No se asigno el map de datos !";
		else {
		  regresar= map.toString();
		  regresar= regresar.substring(1, regresar.length()- 1).replace(", ", "|");
		} // if		
		return regresar;
	}
/**
 * Setea al value un tipo de dato Map<String, String>
 * @param value String de de lista
 */
	@Override
	public void setAsText(String value) {
		try {
  		setValue(Variables.toMap(value));
		}
		catch (Exception e) {
			Error.mensaje(e);
			throw new MapException();
		} // try
	}
	
}

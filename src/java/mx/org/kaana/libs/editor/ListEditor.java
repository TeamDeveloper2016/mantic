/*
 * Code write by user.development
 * Date 22/09/2008
 */

package mx.org.kaana.libs.editor;

import java.beans.PropertyEditorSupport;

import java.util.List;

import mx.org.kaana.libs.editor.exception.ListException;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.formato.Error;


/**
 *
 * @author alejandro.jimenez
 */
public class ListEditor extends PropertyEditorSupport {

  /**
 * Obtiene un String de la lista separados por | los elementos
 * @return
 */
	@Override
	public String getAsText() {
		String regresar= null;
		List list= (List)getValue();
		if (list== null)
			regresar= "No se asigno la lisata de datos !";
		else {
		  regresar= list.toString();
		  regresar= regresar.substring(1, regresar.length()- 1).replace(", ", "|");
		} // if		
		return regresar;
	}
/**
 * Setea al value un tipo de dato List<String>
 * @param value String de de lista
 */
	@Override
	public void setAsText(String value) {
		try {
      if(value!= null && value.startsWith("[") && value.endsWith("]"))
        value= value.substring(1, value.length()- 1);
  		setValue(Variables.toList(value));
		}
		catch (Exception e) {
			Error.mensaje(e);
			throw new ListException();
		} // try
	}

}

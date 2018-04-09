/*
 * Code write by user.development
 * Date 23/09/2008
 */

package mx.org.kaana.libs.editor;

import java.beans.PropertyEditorSupport;
import mx.org.kaana.libs.editor.exception.PathException;
import mx.org.kaana.libs.formato.Error;

/**
 *
 * @author alejandro.jimenez
 */
public class PathEditor extends PropertyEditorSupport {
  /**
 * Obtiene un String si se asignó la hora o un null
 * @return
 */
	@Override
	public String getAsText() {
		Object time= getValue();
		if (time== null)
			return "No se asigno una hora !";
		else
		  return "null";
	}
/**
 * Setea al value un null
 * @param value String de de lista
 */
	@Override
	public void setAsText(String value) {
		try {
			setValue(null);
		}
		catch (Exception e) {
			Error.mensaje(e);
			throw new PathException();
		} // try
	}
	
}

/*
 * Code write by user.development
 * Date 19/09/2008
 */
package mx.org.kaana.libs.editor;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import mx.org.kaana.libs.editor.exception.DateException;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;


/**
 *
 * @author alejandro.jimenez
 */
public class DateEditor extends PropertyEditorSupport {

/**
 * Obtiene un String de la fecha en dd/mm/yyyy    26/11/2003
 * @return
 */
	@Override
	public String getAsText() {
		Date time= (Date)getValue();
		if (time== null)
			return "No se asigno una fecha !";
		else
		  return Fecha.formatear(Fecha.FECHA_CORTA, time);
	}
/**
 * Setea al value un tipo de dato Date
 * @param value String de fecha
 */
	@Override
	public void setAsText(String value) {
		try {
			setValue(new Date(Fecha.getFechaCalendar(value).getTimeInMillis()));
		}
		catch (Exception e) {
			Error.mensaje(e);
			throw new DateException();
		} // try
	}
	
}

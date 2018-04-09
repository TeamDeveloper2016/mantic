/*
 * Code write by user.development
 * Time 19/09/2008
 */
package mx.org.kaana.libs.editor;

import java.beans.PropertyEditorSupport;
import java.sql.Time;
import mx.org.kaana.libs.editor.exception.TimeException;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;


/**
 *
 * @author alejandro.jimenez
 */
public class TimeEditor extends PropertyEditorSupport {
/**
 * Obtiene un String de la fecha en dd/mm/yyyy    26/11/2003 con un Time
 * @return
 */
	@Override
	public String getAsText() {
		Time time= (Time)getValue();
		if (time== null)
			return "No se asigno una hora !";
		else
		  return Fecha.formatear(Fecha.HORA_CORTA, time);
	}
/**
 * Setea al value un tipo de dato Time en base al parametro value
 * @param value String de fecha
 */
	@Override
	public void setAsText(String value) {
		try {
			setValue(new Time(Fecha.getHora(value).getTimeInMillis()));
		}
		catch (Exception e) {
			Error.mensaje(e);
			throw new TimeException();
		} // try
	}
	
}

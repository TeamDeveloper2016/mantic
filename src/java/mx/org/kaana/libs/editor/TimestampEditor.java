/*
 * Code write by user.development
 * Timestamp 19/09/2008
 */
package mx.org.kaana.libs.editor;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import mx.org.kaana.libs.editor.exception.TimestampException;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;


/**
 *
 * @author alejandro.jimenez
*/
public class TimestampEditor extends PropertyEditorSupport {
/**
 * Obtiene un String de la fecha en dd/mm/yyyy    26/11/2003 con un Timestamp
 * @return
 */
	@Override
	public String getAsText() {
		Timestamp value= (Timestamp)getValue();
		if (value== null)
			return "No se asigno una fecha y hora !";
		else
		  return Fecha.formatear(Fecha.FECHA_HORA, value);
	}
/**
 * Setea al value un tipo de dato Timestamp en base al parametro value
 * @param value String de fecha
 */
	@Override
	public void setAsText(String value) {
		try {
			setValue(new Timestamp(Fecha.getFechaHora(value).getTimeInMillis()));
		}
		catch (Exception e) {
			Error.mensaje(e);
			throw new TimestampException();
		} // try
	}
	
}

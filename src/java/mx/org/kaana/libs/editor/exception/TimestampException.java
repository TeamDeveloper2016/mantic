/*
 * Code write by user.development
 * Date 19/09/2008
 */

package mx.org.kaana.libs.editor.exception;

/**
 *
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class TimestampException extends IllegalArgumentException {

     /**
   * Excepcion que se laza cuando el formato de la  hora es incorrecto
   */

	public TimestampException() {
		super("Error en el formato de la hora");
	}
	
}

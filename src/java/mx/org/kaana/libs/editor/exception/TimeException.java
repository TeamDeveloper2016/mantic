/*
 * Code write by user.development
 * Date 19/09/2008
 */

package mx.org.kaana.libs.editor.exception;

/**
 *
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class TimeException extends IllegalArgumentException {
   /**
   * Excepcion que se laza cuando el formato de la fecha y hora es incorrecto
   */
	public TimeException() {
		super("Error en el formato de la fecha y hora");
	}
	
}

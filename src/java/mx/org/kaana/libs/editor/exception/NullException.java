/*
 * Code write by user.development
 * Date 15/09/2008
 */

package mx.org.kaana.libs.editor.exception;

/**
 *
 * @author alejandro.jimenez
 */
public class NullException extends RuntimeException {

   /**
   * Excepcion que se laza cuando el valor del dato es nulo y no puede serlo
   */

	public NullException(String fieldName) {
		super("El valor no puede ser NULO para este campo [".concat(fieldName).concat("]"));
	}

}

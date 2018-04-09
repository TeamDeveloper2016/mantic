/*
 * Code write by user.development
 * Date 22/09/2008
 */

package mx.org.kaana.libs.editor.exception;

/**
 *
 * @author alejandro.jimenez
 */
public class ListException extends IllegalArgumentException {

  /**
   * Excepcion que se laza cuando el formato de lista de datos es incorrecta ej:[param1|param1|...|param3|]
   */
	public ListException() {
		super("Error en el formato de la lista de datos, [param1|param1|...|param3|]");
	}
	
}

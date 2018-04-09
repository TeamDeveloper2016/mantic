/*
 * Code write by user.development
 * Date 22/09/2008
 */

package mx.org.kaana.libs.editor.exception;

/**
 *
 * @author alejandro.jimenez
 */
public class MapException extends IllegalArgumentException {

   /**
   * Excepcion que se laza cuando el formato del map de datos es incorrecta ej:[param1|param1|...|param3|]
   */

	public MapException() {
		super("Error en el formato del map de datos, [param1|param1|...|param3|]");
	}
	
}

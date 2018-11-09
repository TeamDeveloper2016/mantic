/*
 * Code write by user.development
 * Date 19/09/2008
 */

package mx.org.kaana.libs.editor.exception;

/**
 *
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class PathException extends IllegalArgumentException {

   /**
   * Excepcion que se laza cuando el formato del path es incorrecto
   */

	public PathException() {
		super("Error en el formato de la ruta del archivo");
	}
	
}

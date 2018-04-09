/*
 * Minusculas.java
 *
 * Created on 2 de diciembre de 2007, 11:24 PM
 * Write by, alejandro.jimenez
 *
 */

package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

/**
 *
 * @author alejandro.jimenez
 */
public class Lower extends NullField implements Function {

  private static final int INDEX_CADENA= 0;
	private static final long serialVersionUID=-4579238930873051575L;

  /** Creates a new instance of Search */
  public Lower() {
  }

	@Override
  public String getName() {
    return "LOWER";
  }

	@Override
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 1;
  }

	@Override
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    Object cadena= parametersValue[INDEX_CADENA];
    return new String(cadena.toString().toLowerCase());
  }

}

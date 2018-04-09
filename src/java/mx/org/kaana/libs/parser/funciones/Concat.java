/*
 * Concatenar.java
 *
 * Created on 2 de diciembre de 2007, 11:26 PM
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
public class Concat extends NullField implements Function {
	private static final long serialVersionUID=-4700441816821794113L;

  /** Creates a new instance of Search */
  public Concat() {
  }

	@Override
  public String getName() {
    return "CONCAT";
  }

	@Override
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount> 0;
  }

	@Override
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    StringBuffer sb= new StringBuffer();
    for(Object cadena: parametersValue) {
      sb.append(cadena.toString());
    } // for
    return new String(sb.toString());
  }

}

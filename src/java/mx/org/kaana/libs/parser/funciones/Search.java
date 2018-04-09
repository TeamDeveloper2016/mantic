/*
 * Search.java
 *
 * Created on 2 de diciembre de 2007, 08:42 PM
 *
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
public class Search extends NullField implements Function {

  private static final int INDEX_SUB_CADENA= 0;
  private static final int INDEX_CADENA    = 1;
	private static final long serialVersionUID=7702725810197075381L;

  /** Creates a new instance of Search */
  public Search() {
  }

	@Override
  public String getName() {
    return "SEARCH";
  }

	@Override
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 2;
  }

	@Override
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    Object cadena= parametersValue[INDEX_CADENA];
    Object subCadena= parametersValue[INDEX_SUB_CADENA];
    return cadena.toString().indexOf(subCadena.toString())>= 0;
  }

}

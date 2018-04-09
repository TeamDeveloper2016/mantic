package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

public class Equals extends NullField implements Function {

  private static final int INDEX_CADENA_1= 0;
  private static final int INDEX_CADENA_2= 1;
	private static final long serialVersionUID=-2742289587756458503L;

  /** Creates a new instance of Search */
  public Equals() {
  }

	@Override
  public String getName() {
    return "EQUALS";
  }

	@Override
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 2;
  }

	@Override
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    Object cadena= parametersValue[INDEX_CADENA_1];
    Object igual = parametersValue[INDEX_CADENA_2];
    return cadena.toString().equalsIgnoreCase(igual.toString());
  }

}

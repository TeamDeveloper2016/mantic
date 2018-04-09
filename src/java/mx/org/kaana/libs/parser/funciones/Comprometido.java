package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;


public class Comprometido implements Function{

  private static final int INDEX_NUMERO    = 0;
	private static final long serialVersionUID=2626708604576152597L;

  public Comprometido() {
  }

	@Override
  public String getName() {
    return "COMPROMETIDO";
  }

	@Override
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 1;
  }

  private void checkParameters(Object[] parametersValue) {
    for (Object cadena: parametersValue) {
      if (!(cadena instanceof Integer) && !(cadena instanceof Long) && !(cadena instanceof Short) && !(cadena instanceof Byte))
        throw new IllegalArgumentException(cadena.toString().concat(" no es un n�mero valido. !"));
    } // for
  }

	@Override
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    return true;
  }


}

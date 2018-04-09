package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;
import mx.org.kaana.libs.formato.Error;

public class Str implements Function {

  private static final int INDEX_CADENA= 0;
	private static final long serialVersionUID=3288590390551328401L;

	@Override
  public String getName() {
    return "STR";
  }

	@Override
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 1;
  }

  private String convert(Object value) {
    String regresar= "-1";
    try  {
      if(value!= null)
        regresar= value.toString();
    }
    catch (Exception e)  {
      Error.mensaje(e);
    }
    return regresar;
  }

	@Override
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    return convert(parametersValue[INDEX_CADENA]);
  }

}

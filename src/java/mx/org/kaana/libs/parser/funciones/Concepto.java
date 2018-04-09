package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;


public class Concepto implements Function{

  private static final int INDEX_NUMERO    = 0;
	private static final long serialVersionUID=5158030088429160323L;

  public Concepto() {
  }

	@Override
  public String getName() {
    return "CONCEPTO";
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
    return getConcepto(parametersValue[INDEX_NUMERO]);
  }

  private int getConcepto(Object parametro){
    int concepto= Integer.valueOf(parametro.toString())/100;
    return concepto*100;
  }

}

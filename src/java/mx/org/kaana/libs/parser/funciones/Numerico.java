package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;

public class Numerico extends NullField implements Function {

	private static final int INDEX_CADENA=0;
	private static final long serialVersionUID=5425331282437037822L;

	public Numerico() {
	}

	@Override
	public String getName() {
		return "VAL";
	}

	@Override
	public boolean isValidParameterCount(int parameterCount) {
		// At least one parameter
		return parameterCount==1;
	}

	private Double convert(String value) {
		Double regresar=0D;
		try {
			if (!value.equals("")) {
				if (!value.equals("null")) {
					regresar=Numero.getDouble(value, -1D);
				}
			} // if
		}
		catch (Exception e) {
			Error.mensaje(e);
		}
		return regresar;
	}

	@Override
	public Object computeFunction(Interpreter interpreter, Object[] parametersValue) {
		checkParameters(parametersValue);
		Object cadena=parametersValue[INDEX_CADENA];
		return convert(cadena.toString());
	}
}

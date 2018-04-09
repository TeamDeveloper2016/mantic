package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

public class Empty extends NullField implements Function {

	private static final int INDEX_CADENA=0;
	private static final long serialVersionUID=1695929010132222201L;

	public Empty() {
	}

	@Override
	public String getName() {
		return "EMPTY";
	}

	@Override
	public boolean isValidParameterCount(int parameterCount) {
		// At least one parameter
		return parameterCount==1;
	}

	@Override
	public Object computeFunction(Interpreter interpreter, Object[] parametersValue) {
		boolean regresar=false;
		checkParameters(parametersValue);
		Object cadena=parametersValue[INDEX_CADENA];
		if (isStringNullValue(cadena)) {
			regresar=true;
		}
		else if (cadena.toString().trim().length()==0) {
			regresar=true;
		}
		return regresar;
	}

	private boolean isStringNullValue(Object cadena) {
		boolean regresar=false;
		if ((cadena==null)||(cadena.toString().trim().length()==4&&cadena.equals("null"))) {
			regresar=true;
		}
		return regresar;
	}
}

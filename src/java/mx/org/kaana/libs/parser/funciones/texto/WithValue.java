package mx.org.kaana.libs.parser.funciones.texto;

import com.eteks.parser.Interpreter;
import mx.org.kaana.libs.formato.Cadena;

public class WithValue extends Texto {

	private static final int PARAMETERS_COUNT=1;
	private static final String NAME="WITH_VALUE";
	private static final long serialVersionUID=-4903565536470878579L;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isValidParameterCount(int parameterCount) {
		return parameterCount>=PARAMETERS_COUNT;
	}

	@Override
	public Object computeFunction(Interpreter interpreter, Object[] parametersValue) {
		short regresar=0;
		String cadena=null;
		checkParameters(parametersValue);
		for (Object object : parametersValue) {
			cadena=object.toString();
			if (cadena!=null&&!Cadena.isVacio(cadena)&&!isStringNullValue(cadena)) {
				regresar++;
			}
		} // for
		return regresar;
	}

	private boolean isStringNullValue(String cadena) {
		boolean regresar=false;
		cadena=cadena.trim();
		int length=cadena.length();
		if (length==4&&cadena.equals("null")) {
			regresar=true;
		}
		return regresar;
	}
}

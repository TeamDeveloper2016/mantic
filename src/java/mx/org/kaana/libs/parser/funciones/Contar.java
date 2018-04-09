package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

public class Contar implements Function {

	private static final int INDEX_VALOR=0;
	private static final long serialVersionUID=491283409701348011L;

	public Contar() {
	}

	@Override
	public String getName() {
		return "CUANTOS";
	}

	@Override
	public boolean isValidParameterCount(int parameterCount) {
		return parameterCount==1;
	}

	private void checkParameters(Object[] parametersValue) {
		if (!(parametersValue[INDEX_VALOR] instanceof String)) {
			throw new IllegalArgumentException(parametersValue[INDEX_VALOR].toString().concat(" no es una cadena. !"));
		}
	}

	@Override
	public Object computeFunction(Interpreter interpreter, Object[] parametersValue) {
		checkParameters(parametersValue);
		return contar((String) parametersValue[INDEX_VALOR]);
	}

	private int contar(String cadena) {
		int regresa=0;
		if (cadena!=null&&cadena.length()>2) {
			cadena=cadena.substring(1, cadena.length()-1);
			String[] words=null;
			words=cadena.split(",");
			regresa=words.length;
		}
		return regresa;
	}
}

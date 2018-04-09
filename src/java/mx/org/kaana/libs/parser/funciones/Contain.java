package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;
import java.util.StringTokenizer;

public class Contain implements Function {

	private static final int INDEX_SUBCADENA_CONTIENE=0;
	private static final int INDEX_CADENA_CONTIENE=1;
	private static final long serialVersionUID=-5759455575218320182L;

	/**
	 * Creates a new instance of Search
	 */
	public Contain() {
	}

	@Override
	public String getName() {
		return "CONTAIN";
	}

	@Override
	public boolean isValidParameterCount(int parameterCount) {
		// At least one parameter
		return parameterCount==2;
	}

	private void checkParameters(Object[] parametersValue) {
		for (Object cadena : parametersValue) {
			if (!(cadena instanceof String)) {
				throw new IllegalArgumentException(cadena.toString().concat(" no es una cadena. !"));
			}
		} // for
	}

	@Override
	public Object computeFunction(Interpreter interpreter, Object[] parametersValue) {
		checkParameters(parametersValue);
		String cadena=(String) parametersValue[INDEX_CADENA_CONTIENE];
		String subCadena=(String) parametersValue[INDEX_SUBCADENA_CONTIENE];
		return getContiene(cadena, subCadena);
	}

	private boolean getContiene(String cadena, String subCadena) {
		String token="";
		boolean contiene=false;
		StringTokenizer strToken=new StringTokenizer(cadena, "|", false);
		while (strToken.hasMoreTokens()&&!contiene) {
			token=strToken.nextToken();
			if (subCadena.indexOf(token)>=0) {
				contiene=true;
			}
		} // end
		return contiene;
	}
}

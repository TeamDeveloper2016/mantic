package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;
import mx.org.kaana.libs.formato.Numero;

public class SpaceBlank implements Function {

	private static final int INDEX_CADENA_CONTIENE=0;
	private static final int INDEX_NUMERO_CARACTERES=1;
	private static final long serialVersionUID=8585623982806421885L;

	/**
	 * Creates a new instance of Search
	 */
	public SpaceBlank() {
	}

	@Override
	public String getName() {
		return "SPACEBLANK";
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
		int numeroCaracteres=Numero.getInteger((String) parametersValue[INDEX_NUMERO_CARACTERES]);
		return getContiene(cadena, numeroCaracteres);
	}

	private boolean getContiene(String cadena, int numeroCaracteres) {
		boolean contiene=false;
		int maximo=0;
		int numBlancos=0;
		maximo=Math.min(cadena.length(), numeroCaracteres);
		for (int x=0; x<=maximo-1; x++) {
			if (cadena.charAt(x)==' ') {
				numBlancos++;
			}
			else {
				break;
			}
		} // for
		if (numBlancos>0) {
			contiene=true;
		}
		return contiene;
	}
}

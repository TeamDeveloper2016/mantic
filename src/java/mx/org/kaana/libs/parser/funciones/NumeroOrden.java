package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;

public class NumeroOrden implements Function {

	private static final int INDEX_ID_MUESTRA_ENGASTO=0;
	private static final int INDEX_NUMERO_ORDEN=1;
	private static final long serialVersionUID=5528290079863800999L;

	/**
	 * Creates a new instance of Search
	 */
	public NumeroOrden() {
	}

	@Override
	public String getName() {
		return "NUMERO_ORDEN";
	}

	@Override
	public boolean isValidParameterCount(int parameterCount) {
		// At least one parameter
		return parameterCount==2;
	}

	private void checkParameters(Object[] parametersValue) {
		if (!(parametersValue[INDEX_ID_MUESTRA_ENGASTO] instanceof Long)) {
			throw new IllegalArgumentException(parametersValue[INDEX_ID_MUESTRA_ENGASTO].toString().concat(" no es un número. !"));
		}
		if (!(parametersValue[INDEX_NUMERO_ORDEN] instanceof String)) {
			throw new IllegalArgumentException(parametersValue[INDEX_NUMERO_ORDEN].toString().concat(" no es una cadena. !"));
		}

	}

	@Override
	public Object computeFunction(Interpreter interpreter, Object[] parametersValue) {
		checkParameters(parametersValue);
		return find((Long) parametersValue[INDEX_ID_MUESTRA_ENGASTO], (String) parametersValue[INDEX_NUMERO_ORDEN]);
	}

	private boolean find(Long idMuestraEngasto, String numeroOrden) {
		boolean regresar=false;
		Long size=null;
		Map<String, Object> params=null;
		try {
			params=new HashMap<String, Object>();
			params.put("idMuestraEngasto", idMuestraEngasto);
			params.put("numeroOrden", numeroOrden);
			size=((Value)DaoFactory.getInstance().toField("TrIntegrantesHogaresDto","size", params,"records")).getToLong();
			regresar=size==0;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			if (params!=null) {
				params.clear();
			}
			params=null;
		} // finally
		return regresar;
	}
}

package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;



public class Consultar implements Function {

  private static final int UNIDAD = 0;
  private static final int SELECT = 1;
  private static final int ATRIBUTOS_CONDICION = 2;
  private static final int ATRIBUTO_RECUPERADO = 3;
	private static final long serialVersionUID=-539149823584657325L;


  /** Creates a new instance of Search */
  public Consultar() {
  }

	@Override
  public String getName() {
    return "CONSULTAR";
  }

	@Override
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount == 4;
  }

  private void checkParameters(Object[] parametersValue) {
    if (!(parametersValue[UNIDAD] instanceof String))
      throw new IllegalArgumentException(parametersValue[UNIDAD].toString().concat(" no es una cadena. !"));
    if (!(parametersValue[ATRIBUTOS_CONDICION] instanceof String))
      throw new IllegalArgumentException(parametersValue[ATRIBUTOS_CONDICION].toString().concat(" no es una cadena. !"));
    if (!(parametersValue[ATRIBUTO_RECUPERADO] instanceof String))
      throw new IllegalArgumentException(parametersValue[ATRIBUTO_RECUPERADO].toString().concat(" no es una cadena. !"));
  }


	@Override
  public Object computeFunction(Interpreter interpreter, Object[] parametersValue) {
    checkParameters(parametersValue);
    String unidad               = (String)parametersValue[UNIDAD];
    String select               = (String)parametersValue[SELECT];
    String condicion            = (String)parametersValue[ATRIBUTOS_CONDICION];
    String  atributoRrecuperado = (String)parametersValue[ATRIBUTO_RECUPERADO];
    Map params                      = new HashMap();
    Object regresar                 = null;
    Value value                     = null;
    try {
      params.put(Constantes.SQL_CONDICION, condicion);
      value = DaoFactory.getInstance().toField(unidad, select, params, atributoRrecuperado);
      if (value != null){
        regresar = value.getData();
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }
}


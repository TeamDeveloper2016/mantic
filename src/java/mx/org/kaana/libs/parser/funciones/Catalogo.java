/*
 * Minusculas.java
 *
 * Created on 2 de diciembre de 2007, 11:24 PM
 * Write by, alejandro.jimenez
 *
 */

package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;


/**
 *
 * @author alejandro.jimenez
 */
public class Catalogo implements Function {

  private static final int NOMBRE_DTO = 0;
  private static final int ATRIBUTOS_CONDICION = 1;
  private static final int ATRIBUTO_RECUPERADO = 2;
  private static final int VALORES_ATRIBUTOS_CONDICION = 3;


  /** Creates a new instance of Search */
  public Catalogo() {
  }

  public String getName() {
    return "CATALOGO";
  }

  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount == 4;
  }

  private void checkParameters(Object[] parametersValue) {
    if (!(parametersValue[NOMBRE_DTO] instanceof String))
      throw new IllegalArgumentException(parametersValue[NOMBRE_DTO].toString().concat(" no es una cadena. !"));
    if (!(parametersValue[ATRIBUTOS_CONDICION] instanceof String))
      throw new IllegalArgumentException(parametersValue[ATRIBUTOS_CONDICION].toString().concat(" no es una cadena. !"));
    if (!(parametersValue[ATRIBUTO_RECUPERADO] instanceof String))
      throw new IllegalArgumentException(parametersValue[ATRIBUTO_RECUPERADO].toString().concat(" no es una cadena. !"));
    if (!(parametersValue[VALORES_ATRIBUTOS_CONDICION] instanceof String))
      throw new IllegalArgumentException(parametersValue[VALORES_ATRIBUTOS_CONDICION].toString().concat(" no es una cadena. !"));
  }


  public Object computeFunction(Interpreter interpreter, Object[] parametersValue) {
    checkParameters(parametersValue);
    String dto = (String)parametersValue[NOMBRE_DTO];
    String atributosCondicion       = (String)parametersValue[ATRIBUTOS_CONDICION];
    String  atributoRrecuperado  = (String)parametersValue[ATRIBUTO_RECUPERADO];
    String valoresAtributosCondicion= (String)parametersValue[VALORES_ATRIBUTOS_CONDICION];
    String[] camposCondicion        = atributosCondicion.split("~");
    String[] valoresCondicion       = valoresAtributosCondicion.split("~");
    String cadenaCondicion          = " ";
    Map params                      = new HashMap();
    Object regresar                 = null;
    Value value                     = null;
    // Creamos la lista de parametros para la condicion
    if (camposCondicion.length> 1) {
      for (int i = 0; i < camposCondicion.length; i++)
        cadenaCondicion = cadenaCondicion.concat(camposCondicion[i]).concat("=").concat("'").concat(valoresCondicion[i]).concat("'").concat(" and ");
      cadenaCondicion = cadenaCondicion.substring(1, cadenaCondicion.length() - 4);
    }
    else
     cadenaCondicion= cadenaCondicion.concat(camposCondicion[0]).concat("=").concat(valoresCondicion[0]);
    try {
      params.put(Constantes.SQL_CONDICION, cadenaCondicion);
      value = DaoFactory.getInstance().toField(dto, "row", params, atributoRrecuperado);
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



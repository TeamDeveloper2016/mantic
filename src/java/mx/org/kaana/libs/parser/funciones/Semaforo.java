package mx.org.kaana.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.parser.funciones.beans.Celda;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;


public class Semaforo implements Function {

  private static final int INDEX_VALOR = 0;
  private static final int ID_INDICADOR = 1;
	private static final long serialVersionUID=7155282771972516383L;

  /** Creates a new instance of Search */
  public Semaforo() {
  }

	@Override
  public String getName() {
    return "SEMAFORO";
  }

	@Override
  public boolean isValidParameterCount(int parameterCount) {
    // 2 parametros, valor y id de indicador
    return parameterCount== 2;
  }

  private void checkParameters(Object [] parametersValue) {
    if(!(parametersValue[INDEX_VALOR] instanceof Long || parametersValue[INDEX_VALOR] instanceof Double))
      throw new IllegalArgumentException(parametersValue[INDEX_VALOR].toString().concat(" no es un n�mero. !"));
    if(!(parametersValue[ID_INDICADOR] instanceof Long))
      throw new IllegalArgumentException(parametersValue[ID_INDICADOR].toString().concat(" No es valor valido. !"));
  }

	@Override
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    Double valor = 0.0;
    if(parametersValue[INDEX_VALOR] instanceof Long)
      valor = Numero.getDouble(parametersValue[INDEX_VALOR].toString());
    else
      valor = (Double)parametersValue[INDEX_VALOR];

    Long idIndicador = (Long)parametersValue[ID_INDICADOR];

    return getValoresCelda(idIndicador, valor);
  }

  private Celda getValoresCelda(Long idIndicador, Double valor) {
    Map params                   = null;
    List<Entity> umbrales        = null;
    Long idUmbralSeleccionado    = null;
    Entity alerta                = null;
    Entity tipoAlerta            = null;
    Celda celda                  = null;
    try {
      params = new HashMap();
      params.put("idIndicadora", idIndicador);
      umbrales = DaoFactory.getInstance().toEntitySet("TrUmbralesDto", params,-1L);

      for(Entity umbral : umbrales) {
        if(umbral.toLong("valorMinimo")!= null) {
          if(umbral.toLong("valorMaximo")!= null) {
            if(valor >= umbral.toLong("valorMinimo") && valor <= umbral.toLong("valorMaximo")) {
              idUmbralSeleccionado = umbral.toLong("idUmbral");
            } // if
          } // if
          else if(valor > umbral.toLong("valorMinimo")) {
            idUmbralSeleccionado = umbral.toLong("idUmbral");
          } // else if
        } // if
        else if(umbral.toLong("valorMaximo")!= null){
          if(valor < umbral.toLong("valorMaximo"))
            idUmbralSeleccionado = umbral.toLong("idUmbral");
        }// else if
      } // for
      if(idUmbralSeleccionado > 0){
        params.clear();
        params.put("idUmbral", idUmbralSeleccionado);
        alerta = (Entity)DaoFactory.getInstance().toEntity("TrAlertasDto", params);
				params.clear();
				params.put("idTipoAlerta", alerta.toLong("idTipoAlerta"));
        tipoAlerta = (Entity)DaoFactory.getInstance().toEntity("TcTiposAlertasDto", params);
        celda  = new Celda("id", tipoAlerta.toString("descripcion"), alerta.toString("accion"));
      } // if

    }
    catch(Exception e) {
      Error.mensaje(e);
    }
    return celda;
  }

}

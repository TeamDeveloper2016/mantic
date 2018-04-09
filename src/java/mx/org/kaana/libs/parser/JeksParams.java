package mx.org.kaana.libs.parser;

import com.eteks.parser.ExpressionParameter;
import java.util.Collections;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.formato.Rango;

public class JeksParams implements ExpressionParameter {
	private static final long serialVersionUID=6729171265754390706L;

  private Map<String, Object> params;

  public JeksParams() {
    this(Collections.EMPTY_MAP);
  }

  public JeksParams(Map<String, Object> params) {
    this.params=params;
  }

  public void setParams(Map<String, Object> params) {
    this.params = params;
  }

  public Map<String, Object> getParams() {
    return params;
  }

	@Override
  public Object getParameterKey(String parameter) {
    Object regresar= null;
    String name    = parameter;
    if(parameter!= null && "|blanco|nulo|noespecificado|".indexOf(parameter.toLowerCase())> 0) {			
			switch (parameter.toLowerCase()) {
				case "blanco":
					regresar= Rango.BLANCO;
					break;
				case "noespecificado":
					regresar= Rango.NO_ESPECIFICADO;
					break;
				default:
					regresar= Rango.NULO;
					break;
			}// switch
    } // else
    else
      if(getParams().containsKey(name)) {
        Object value= getParams().get(name);
        if(value== null)
          value= getParams().get(parameter);
        if(value== null)
          regresar= Rango.BLANCO;
        else
          if(Cadena.isVacio(value.toString()))
            regresar= Rango.BLANCO;
          else
            if((value.toString()).trim().equals(Constantes.AMPERSON))
              regresar= Rango.NO_ESPECIFICADO;
            else
              if(Character.isDigit(value.toString().charAt(0)) || value.toString().charAt(0)== '-' || value.toString().charAt(0)== '+')
                regresar= Numero.getDouble(value.toString());
              else
                regresar= value;
      }
      else
        throw new IllegalArgumentException("La variable [".concat(parameter).concat("] no esta definida en su proceso."));
    return regresar;
  }

	@Override
  public Object getParameterValue(Object parameterKey) {
    return parameterKey;
  }

}

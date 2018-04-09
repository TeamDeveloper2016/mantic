package mx.org.kaana.libs.formato;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;


public class Formatos {

  private String codigos;
  private Map<String, Object> variables;
	private boolean clean;

  public Formatos(String codigos, Object object) {
    this.codigos  = codigos;
    this.variables= toHashMap(object);
		this.clean    = true;
  }

  public Formatos(String codigos, Map variables) {
    this.codigos  = codigos;
    this.variables= variables;
		this.clean    = false;
  }

  public String toFormato() {
    String sentencia = getCodigos();
    Iterator iterador= getVariables().keySet().iterator();
    String id = null;
    while (iterador.hasNext()) {
      id = iterador.next().toString();
      sentencia = sentencia.replaceAll(":".concat(id), String.valueOf(getVariables().get(id)));
    } // while
    return sentencia;
  }

  public String getSentencia(boolean nullable, String ... priority) {
    return getSentencia("null", nullable, priority);
  }

  public String getSentencia(String cadenaNull, boolean nullable, String ... priority) {
    StringTokenizer resultado = new StringTokenizer(getCodigos(), "{}", true);
    String token    = null;
    Object valor    = null;
    if(priority== null)
      priority= new String[] {""};
    StringBuilder sentencia= new StringBuilder();
    while (resultado.hasMoreTokens()) {
      token = resultado.nextToken();
      if (token.equals("{")) {
        token= resultado.nextToken();
        for(String item: priority) {
          valor= getVariables().get(item.concat(token));
          if (valor!= null)
            break;
        } // for
        if (valor!= null)
          sentencia.append(valor.toString());
        else
          if(nullable)
            sentencia.append("{").append(token).append("}");
          else
            sentencia.append(cadenaNull);
        token = resultado.nextToken();
      } // if
      else
        sentencia.append(token);
    } // while
		resultado= null;
    return sentencia.toString();
  }


  public String getSentencia(boolean nullable) {
    return getSentencia(nullable, "");
  }

  public String getSentencia() {
    return getSentencia(false);
  }

  public String getSentenciaJeks() {
    StringTokenizer resultado = new StringTokenizer(getCodigos(), "{}", true);
    String token    = null;
    Object valor    = null;
    StringBuilder sentencia= new StringBuilder();
    while (resultado.hasMoreTokens()) {
      token = resultado.nextToken();
      if (token.equals("{")) {
        token= resultado.nextToken();
        valor= getVariables().get(token);
        if (valor!= null)
          sentencia.append(valor.toString());
        else
          sentencia.append("\"null\"");
        resultado.nextToken();
      }
      else
        sentencia.append(token);
    }
		resultado= null;
    return sentencia.toString();
  }

  private Map<String, Object> toHashMap(Object o) {
    Map regresar = new HashMap();
    Field[] atributos = o.getClass().getDeclaredFields();
    String metodo = null;
    for (Field atributo: atributos) {
      Object resultado = null;
      try {
        metodo = atributo.getName();
        metodo = "get".concat(metodo.substring(0, 1).toUpperCase().concat(metodo.substring(1)));
        Method method = o.getClass().getMethod(metodo, new Class[] { });
        resultado = method.invoke(o, new Object[] { });
      } // try
      catch (Exception e) {
        Error.mensaje(e);
      } // catch
      regresar.put(atributo.getName(), resultado);
    }
    return regresar;
  }

  public void setCodigos(String codigos) {
    this.codigos = codigos;
  }

  public String getCodigos() {
    return codigos;
  }

  public void setVariables(Map<String, Object> variables) {
    this.variables = variables;
  }

  public Map<String, Object> getVariables() {
    return variables;
  }

	@Override
  public void finalize() {
		if(this.clean && this.variables!= null)
			this.variables.clear();
		this.variables= null;
  }

}

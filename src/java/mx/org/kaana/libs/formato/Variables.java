package mx.org.kaana.libs.formato;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import mx.org.kaana.libs.Constantes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class Variables implements Serializable {
	
  private static final Log LOG= LogFactory.getLog(Variables.class);
	private static final long serialVersionUID=-458562953593346388L;

  private String codigos;
  private char tokens;

  public Variables(String codigos) {
    setTokens(Constantes.SEPARADOR.charAt(0));
    if(codigos.indexOf("=")>= 0 || codigos.indexOf(Constantes.SEPARADOR)>= 0)
      init(codigos);
    else
      parse(codigos);
  }

  public Variables(String codigos, char tokens) {
    setTokens(tokens);
    setCodigos(codigos);
  }

  public Variables(Object ... codigos) {
    this(Constantes.SEPARADOR.charAt(0), codigos);
  }

  public Variables(char token, Object ... codigos) {
    setTokens(token);
    parse(codigos);
  }

  public void setCodigos(String codigos) {
    this.codigos = codigos;
  }

  public String getCodigos() {
    return codigos;
  }

  public void setTokens(char tokens) {
    this.tokens = tokens;
  }

  public char getTokens() {
    return tokens;
  }

  private void parse(Object ... codigos) {
    StringBuilder cadena= new StringBuilder(getTokens());
    for(int x= 0; x< codigos.length; x++) {
      cadena.append(x);
      cadena.append("=");
      cadena.append(codigos[x].toString());
      cadena.append(getTokens());
    } // for
    init(cadena.toString());
  }

  private void init(String cadena) {
    setCodigos(cadena);
  }

  public Map getMap(char delimited) {
    Map variables = new HashMap();
    String token  = null;
    String nomVar = null;
    String valVar = null;
    StringTokenizer st= null;
    //switchEquals();
    StringTokenizer _tokens = new StringTokenizer(getCodigos(), String.valueOf(getTokens()), false);
    while (_tokens.hasMoreTokens()) {
      token = _tokens.nextToken();
      if (token.trim().length()>0) {
        st = new StringTokenizer(token, String.valueOf(delimited), false);
        switch (st.countTokens()) {
          case 1:
            nomVar = st.nextToken();
            valVar = "";
            variables.put(nomVar, valVar);
            break;
          case 2:
            nomVar = st.nextToken();
            valVar = st.nextToken();
            variables.put(nomVar, valVar);
            break;
        } // switch
      } // if
    }
    return variables;
  }

  public Map getMap() {
   return getMap(Constantes.TILDE.charAt(0));
  }

  private void switchEquals() {
    StringBuilder    sb= new StringBuilder(Constantes.SEPARADOR);
    StringTokenizer st= new StringTokenizer(getCodigos(), Constantes.SEPARADOR, false);
    while(st.hasMoreTokens()) {
      String token= st.nextToken();
      if(token.indexOf("=")>= 0) {
        sb.append(token.substring(0, token.indexOf("=")+ 1));
        token= token.substring(token.indexOf("=")+ 1);
        if(token.indexOf("=")>= 0)
          token= token.replace('=', Constantes.TILDE.charAt(0));
      } // if
      sb.append(token);
      sb.append(Constantes.SEPARADOR);
    } // while
    setCodigos(sb.toString());
  }

  public static Map<String, String> toMap(String params, char separator, char delimited) {
    Map<String, String> regresar= new HashMap<String, String>();
    String token = null;
    String name  = null;
    String value = null;
    StringTokenizer st= null;
    StringTokenizer tokens_ = new StringTokenizer(params, String.valueOf(separator), false);
    while (tokens_.hasMoreTokens()) {
      token = tokens_.nextToken();
      if (token.trim().length()>0) {
        st = new StringTokenizer(token, String.valueOf(delimited), false);
        switch (st.countTokens()) {
          case 1:
            name = st.nextToken();
            value= "";
            regresar.put(name, value);
            break;
          case 2:
            name = st.nextToken();
            value= st.nextToken();
            regresar.put(name, value);
            break;
        } // switch
      } // if
    }
    return regresar;
  }

  public static Map<String, String> toMap(String params, String separator, String delimited) {
    Map<String, String> regresar= new HashMap<String, String>();
    String[] separados = params.toLowerCase().split(separator);
    String[] filtros   = null;
    for(String separado : separados) {
      filtros = separado.split(delimited);
      regresar.put(Cadena.toBeanName(filtros[0].trim()), filtros.length>1 ? filtros[1].trim() : "");
    } // for
    return regresar;
  }

  public static Map<String, String> toMap(String params, char tokens) {
    return toMap(params, tokens, Constantes.TILDE.charAt(0));
  }

  public static Map<String, String> toMap(String params) {
    return toMap(params, Constantes.SEPARADOR.charAt(0));
  }

  private static String parse(String params) {
    StringBuilder    sb= new StringBuilder();
    StringTokenizer st= new StringTokenizer(params, Constantes.SEPARADOR, false);
    while(st.hasMoreTokens()) {
      String token= st.nextToken();
      if(token.indexOf("=")>= 0) {
        sb.append(token.substring(0, token.indexOf("=")+ 1));
        token= token.substring(token.indexOf("=")+ 1);
        if(token.indexOf("=")>= 0)
          token= token.replace('=', Constantes.TILDE.charAt(0));
      } // if
      sb.append(token);
    } // while
    return sb.toString();
  }

  public static List<String> toList(String params, char tokens) {
    List<String> regresar= new ArrayList<String>();
    String token         = null;
    StringTokenizer values = new StringTokenizer(params, String.valueOf(tokens), false);
    while (values.hasMoreTokens()) {
      token = values.nextToken();
      regresar.add(token);
    }
    return regresar;
  }

  public static List<String> toList(String params) {
    return toList(params, Constantes.SEPARADOR.charAt(0));
  }

}

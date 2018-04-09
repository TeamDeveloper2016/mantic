/*
 * Clase: Cadena.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: alejandro.jimenez
 */

package mx.org.kaana.libs.formato;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.Prioridad;


public final class Cadena {

  private Cadena() {
  }
  /**
   * Elimina todas las concurrencias del caracter en la cadena msg
   * @param msg Cadena de caracteres a evaluar
   * @param caracter Caracter a eliminar
   * @return Resultado cadena de respuesta
   */
  public static String eliminaCaracter(String msg, char caracter) {
    StringBuilder resultado= new StringBuilder(msg);
    int x= 0;
    while(x< resultado.length()) {
      if (resultado.charAt(x)== caracter)
        resultado.deleteCharAt(x);
      else
        x++;
    } // while
    return resultado.toString();
  }
  /**
   * Concatena en una nueva cadena todos los caracteres que no coinciden con el caracter dado
   * @param campos Cadena de caracteres a evaluar
   * @param caracter  Caracter a eliminar
   * @return  Resultado cadena de respuesta
   */
  public static String eliminar(String campos, char caracter) {
    StringBuilder sb= new StringBuilder();
    for(int x= 0; x< campos.length(); x++) {
      if (campos.charAt(x)!= caracter) {
        sb.append(campos.charAt(x));
			} // if
    } // for
  	return sb.toString();
  }
  /**
   * Elimina prefijos de una cadena dada, recibiendolo como parametro devolviendo un caracter
   * @param cadena String a evaluar
   * @param prefijo String a buscar
   * @return
   */
  public static char eliminarCaracter(String cadena, String prefijo) {
    char elemento = 0;
    String x = null;
    if(cadena.contains(prefijo)){
      x = cadena.substring(cadena.lastIndexOf(prefijo)+1, cadena.length());
      elemento = x.charAt(0);
    }
    return elemento;
  }//elimina prefijos de una cadena dada, recibiendolo como parametro devolviendo un caracter
  /**
   * Elimina prefijos de una cadena dada, recibiendolo como parametro devolviendo un entero
   * @param cadena String a evaluar
   * @param prefijo String a buscar
   * @return
   */
   public static int eliminar(String cadena, String prefijo) {
     int elemento = 0;
     String x = null;
     if(cadena.contains(prefijo)){
       x = cadena.substring(cadena.lastIndexOf(prefijo)+1, cadena.length());
       elemento = Integer.valueOf(x);
     }
     return elemento;
   }//elimina prefijos de una cadena dada, recibiendolo como parametro devolviendo un entero
   /**
    * Formatea el nombre de una persona con Mayúsculas y minúsculas
    * @param nombre String del nombre de una persona
    * @return
    */
  public static String nombrePersona(String nombre) {
    StringBuilder sb= new StringBuilder();
   StringTokenizer st = new StringTokenizer(nombre, " ", false);
   String token = null;
   while(st.hasMoreTokens()) {
     token=st.nextToken();
     if (token.length()>3)
       sb.append(letraCapital(token).concat(" "));
     else
       sb.append(token.toLowerCase().concat(" "));
   } // while
   return sb.toString().trim();
  } // nombrePersona
/**
 * Formatea el cadena con la primera letra mayúsculas y las que le seguien en minúsculas
 * @param nombre Cadena de letras
 * @return
 */
  public static String letraCapital(String nombre) {
  	if(nombre!= null && nombre.length()!= 0)
  	  return nombre.substring(0, 1).toUpperCase().concat(nombre.substring(1).toLowerCase());
	  else
	    return nombre;
  }
/**
 * Regresa una cadena con  solo las letras, los numeros y el guión bajo (_) de una cadena y omite los demás caracteres
 * @param msg Cadena a evaluar
 * @return
 */
  public static String soloLetras(String msg)  {
    StringBuilder sb= new StringBuilder();
    for(int x= 0; x< msg.length(); x++) {
	    if("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".indexOf(msg.charAt(x))>=0)
  	     sb.append(msg.charAt(x));
	  } // for
	  return sb.toString();
  }
  /**
   * Pone entre comillas simples una cadena
   * @param cadena cadena a formatear
   * @return
   */
  public static String comillas(String cadena) {
    return "'".concat(cadena== null? "": cadena).concat("'");
  }
/**
 * Cuenta las repeticiones de un caracter que se encuetra en una cadena
 * @param cadena String a evaluar
 * @param caracter Caracter a buscar
 * @return
 */
  public static int contar(String cadena, char caracter) {
    int regresar= 0;
    for(int x= 0; x< cadena.length(); x++) {
      if(cadena.charAt(x)== caracter)
        regresar++;
    } // for x
    return regresar;
  }
/**
 * Rellena al inicio o al final un caracter dado a una cadena de caracter a diferenctes posiciones
 * @param cadena Cadena a formatear
 * @param cuantos Cauntos caracteres se van a rellenar
 * @param caracter Caracter que se va a agregar
 * @param alInicio true= Rellenar al inicio. false= rellenar al final
 * @return Regresa cadena formateada
 */
  public static String rellenar(String cadena, int cuantos, char caracter, boolean alInicio) {
    StringBuilder regresar= new StringBuilder(cadena);
    for (int x= cadena.length(); x<cuantos; x++)  {
      if(alInicio)
        regresar.insert(0, caracter);
      else
        regresar.append(caracter);
    } // for
    return regresar.toString();
  } // rellenar
  /**
   * Regresa un verdadero si la cadena es un null o viene vacía
   * @param cadena String a evaluar
   * @return
   */
  public static boolean isVacio(Object cadena) {
    return cadena== null || cadena.toString().trim().length()== 0;
  }
/**
 * Regresa un '' si la cadena es nulla o viene vacía y regresa el valor si la cadena no es vacía
 * @param cadena String a evaluar
 * @return
 */
  public static String getLlave(String cadena) {
    return isVacio(cadena)? "''": cadena;
  }
/**
 * Regresa una lista con los elementos separados por comas de una cadena
 * @param valores Cadena de valores separados por comas
 * @return Lista de valores
 */
  public static List<String> getArrayValues(String valores) {
    List<String> values= new ArrayList<>();
    StringTokenizer st = new StringTokenizer(valores, "[,]", false);
    while(st.hasMoreTokens()) {
      values.add(st.nextToken().trim());
    } // while
    return values;
  }
	/**
   * Obtiene un mapa con los elementos separdos por | ~ Ej valor1~10|valor2~12|valor3~2
   * @param valores String a evaluar
   * @return
   */
	public static Map<String,String> getMapValues(String valores) {
    Map<String,String> values= new HashMap<>();
		String []split = null;
    StringTokenizer st = new StringTokenizer(valores, "[|]", false);
    while(st.hasMoreTokens()) {
			split =st.nextToken().trim().split("~");
      values.put(split[0],split[1]);
    } // while
    return values;
  }
  /**
   * Reemplaza los parametros a una cadena select * from tc_tops_datos where id_tipo_dato = {idTipoDato}
   * @param sql Cadena a parcear
   * @param params parametros a remplazar
   * @return
   */
  public static String replaceParams(String sql, Map params) {
     Formatos formatos = new Formatos(sql, params);
     return formatos.getSentencia();
  }
    /**
   * Reemplaza los parametros a una cadena select * from tc_tops_datos where id_tipo_dato = {idTipoDato}
   * @param sql Cadena a parcear
   * @param params parametros a remplazar
   * @param nullable si se desea remplazar por un valor null en caso de no enviar el parametro
   * @return
   */

  public static String replaceParams(String sql, Map params, boolean nullable) {
     Formatos formatos = new Formatos(sql, params);
     return formatos.getSentencia(nullable);
  }
      /**
   * Reemplaza los parametros a una cadena
   * @param sql Cadena a parcear
   * @param params parametros a remplazar
   * @return
   */
  public static boolean isReplaceAllParams(String sql, Map params) {
     Formatos formatos = new Formatos(sql, params);
     return formatos.getSentencia("~",false,"").indexOf("~")==-1;
  }


  public static String replace(String sql, Map params, boolean nullable, String ... tokens) {
    if(sql!= null) {
      Formatos formatos = new Formatos(sql, params);
      return formatos.getSentencia(nullable, tokens);
    }
    else
      return "";
  }

  public static String replace(String sql, Map params, boolean nullable) {
    return replace(sql, params, nullable, new String[] {""});
  }

  public static List<String> toList(String params) {
    String strings[] = params.split("[".concat(Constantes.SEPARADOR).concat("]"));
    return Arrays.asList(strings);
  }

  public static String getNameVariable(String name) {
    StringBuilder sb= new StringBuilder();
    for (int x= 0; x < name.length(); x++) {
      if (x< name.length()-1) {
        if((name.charAt(x)== '_') && (name.charAt(x+ 1)!= ' ')) {
          sb.append(name.toUpperCase().charAt(x+ 1));
          x++;
        }
        else
          sb.append(name.toLowerCase().charAt(x));
      }
      else
        sb.append(name.toLowerCase().charAt(x));
    } // for x
    return sb.toString();
  }

  public static String replace(String sql, Map params, String nullAsString) {
    if (sql!= null && params!= null && !params.isEmpty()) {
      Pattern patron= Pattern.compile("'\\d+:\\d+'");
      Matcher match = patron.matcher(sql);
      List<String> lista = new ArrayList<>();
      int fin = 0;
      do {
         match = Pattern.compile(":\\w+").matcher(sql);
        if (match.find()) {
          fin = match.end();
          lista.add(sql.substring(0, fin));
          sql = sql.substring(fin);
        } // if
      } while (match.find());
      lista.add(sql);
      String param;
      StringBuilder sbDml = new StringBuilder();
      for (String linea : lista) {
        match = Pattern.compile(":\\w+").matcher(linea);
        if (match.find()) {
          param = linea.substring(match.start() + 1, match.end());
          if (params.get(param) != null)
            sbDml.append(match.replaceFirst(params.get(param).toString()));
          else
            if (nullAsString!= null)
              sbDml.append(match.replaceFirst(nullAsString));
            else
              sbDml.append(linea);
        }
        else
          sbDml.append(linea);
      } // if
      sql = sbDml.toString();
      if (nullAsString!= null)
        sql = sql.replaceAll("'NULL'", nullAsString);
    } // if
    return sql;
  }

  public static String replace(String sql, Map params) {
    return replace(sql, params, "NULL");
  }
/**
 * Obtiene el toString() de los objetos si son diferentes de null
 * @param value objeto que se le aplicará el .toString(); no tienen que ser tipos de datos primitivos
 * @param text parametro que devolverá si el objeto value es null
 * @return
 */
  public static String getString(Object value, String text) {
    return value== null? text: value.toString();
  }
/**
 * Devuelve la misma cadena mandada solo que con la primera letra en mayuscula
 * @param nombre Cadena a formatear
 * @return
 */
  public static String toNameBean(String nombre) {
    if(nombre!= null && nombre.length()!= 0)
      return nombre.substring(0, 1).toUpperCase().concat(nombre.substring(1));
    else
      return nombre;
  }

  public static String tokens(String sql, Map params, String nullAsString, char token) {
    if (params!= null && !params.isEmpty()) {
      Matcher match = null;
      List<String> lista = new ArrayList<>();
      int fin = 0;
      do {
        match = Pattern.compile(token+ "\\w+"+ token).matcher(sql);
        if (match.find()) {
          fin = match.end();
          lista.add(sql.substring(0, fin));
          sql = sql.substring(fin);
        } // if
      } while (match.find());
      lista.add(sql);
      String param;
      StringBuilder sbDml = new StringBuilder();
      for (String linea : lista) {
        match = Pattern.compile(token+ "\\w+"+ token).matcher(linea);
        if (match.find()) {
          param = linea.substring(match.start()+ 1, match.end()- 1);
          if (params.get(param) != null)
            sbDml.append(match.replaceFirst(params.get(param).toString()));
          else
            if (nullAsString!= null)
              sbDml.append(match.replaceFirst(nullAsString));
            else
              sbDml.append(linea);
        }
        else
          sbDml.append(linea);
      } // if
      sql = sbDml.toString();
      if (nullAsString!= null)
        sql = sql.replaceAll("'NULL'", nullAsString);
    } // if
    return sql;
  }
/**
 * De una cadena en notacion camello devuelve la misma cadena con quiones bajos. Normalmente se usa para los campos de la BD Ej TcTipoDatos = tc_tipos_datos
 * @param name Parametro a evaluar
 * @return
 */
  public static String toSqlName(String name) {
    StringBuilder regresar = new StringBuilder();
    for (int x = 0; x < name.length(); x++) {
      if (Character.isUpperCase(name.charAt(x)))
        regresar.append("_");
      regresar.append(Character.toLowerCase(name.charAt(x)));
    } // for x
    return regresar.toString();
  }
/**
 * De una cadena en notacion camello devuelve la misca cadena con quiones bajos con nombre de la tabla. Normalmente se usa para los campos de la BD Ej TcTipoDatos.idTipoDato = tc_tipos_datos.id_tipo_dato
 * @param name parametro a evaluar
 * @return
 */
  public static String toFullSqlName(String name) {
    StringBuilder regresar = new StringBuilder();
    String[] tokens= name.split("[.]");
    for (String token: tokens) {
      regresar.append(toSqlName(token).trim());
      regresar.append(".");
    } // for
    return regresar.toString().substring(0, regresar.length()- 1);
  }
/**
 * Convierte la cadena dada a con mayusculas y minusculas Ej. TrTiposDatos
 * @param name Cadena a formatear
 * @return
 */
  public static String toBeanName(String name) {
    StringBuilder regresar= new StringBuilder();
    boolean toUpper      = false;
    int start            = 0;
    while(start < name.length() && (name.charAt(start)== '_' || name.charAt(start)== ' '))
      start++;
    if(start< name.length()- 1) {
      for (int x= start; x < name.length(); x++) {
        if (name.charAt(x) != '_' && name.charAt(x) != ' ')
          if (toUpper)
            regresar.append(Character.toUpperCase(name.charAt(x)));
          else
            regresar.append(Character.toLowerCase(name.charAt(x)));
        toUpper= name.charAt(x) == '_' || name.charAt(x) == ' ';
      } // for x
    } // if
    else
      regresar.append(name);
    return regresar.toString();
  }
/**
 * Convierte la cadena dada a con mayusculas y minusculas Ej. TrTiposDatos omite los dobles guiones bajos ej. update__
 * @param name Cadena a formatear
 * @return
 */
  public static String toBeanNameEspecial(String name) {
    return toBeanName(toVariableEspecial(name));
  }
/**
 * Convierte la cadena dada a con mayusculas y minusculas Ej. idTipoDato omite los dobles guiones bajos ej. update__
 * @param name Cadena a formatear
 * @return
 */
  private static String toVariableEspecial(String name) {
    StringBuilder regresar= new StringBuilder();
    int count            = 0;
    for(int x= 0; x < name.length(); x++) {
      if(name.charAt(x)=='_') {
        if(count> 1)
          regresar.append(name.charAt(x));
        count= 0;
      }  // if
      else {
        count++;
        regresar.append(name.charAt(x));
      }
    } // for
    return regresar.toString();
  }
	/**
   * Obtiene un nombre en notacion camello pero la primera letra en mayusculas
   * @param name variable a formatear
   * @return
   */
	public static String toClassNameNemonico(String name) {
		StringBuilder regresar = new StringBuilder();
		if(name!= null && name.length()> 0) {
      int start            = 0;
      if(start< name.length()- 1)  {
        regresar.append(Character.toUpperCase(name.charAt(start)));
        for (int x= start+ 1; x < name.length(); x++) {
					if(name.charAt(x)!= '_')
						regresar.append(Character.toLowerCase(name.charAt(x)));
					else
						regresar.append(name.charAt(x));
        } // for x
			} // if
    } // if
		return regresar.toString();
	}
		/**
   * Obtiene un nombre en notacion camello.
   * @param name variable a formatear
   * @return
   */
	public static String toBeanNameNemonico(String name) {
		StringBuilder regresar = new StringBuilder();
		if(name!= null && name.length()> 0) {
      int start            = 0;
      if(start< name.length()- 1)  {
        for (int x= start; x < name.length(); x++) {
					if(name.charAt(x)!= '_')
						regresar.append(Character.toLowerCase(name.charAt(x)));
					else
						regresar.append(name.charAt(x));
        } // for x
			} // if
    } // if
		return regresar.toString();
	}

  public static String toClassName(String name) {
    StringBuilder regresar = new StringBuilder();
    if(name!= null && name.length()> 0) {
      int start            = 0;
      while(start < name.length() && (name.charAt(start)== '_' || name.charAt(start)== ' '))
        start++;
      if(start< name.length()- 1)  {
        boolean toUpper = false;
        regresar.append(Character.toUpperCase(name.charAt(start)));
        for (int x= start+ 1; x < name.length(); x++) {
          if (name.charAt(x) != '_' && name.charAt(x) != ' ')
            if (toUpper)
              regresar.append(Character.toUpperCase(name.charAt(x)));
            else
              regresar.append(Character.toLowerCase(name.charAt(x)));
          toUpper = name.charAt(x) == '_' || name.charAt(x) == ' ';
        } // for x
      } // if
      else
        regresar.append(name);
    } // if
    return regresar.toString();
  }

  public static String toClassNameEspecial(String name) {
    StringBuilder regresar = new StringBuilder();
    name= toVariableEspecial(name);
    if(name!= null && name.length()> 0) {
      boolean toUpper = false;
      regresar.append(Character.toUpperCase(name.charAt(0)));
      for (int x= 1; x < name.length(); x++) {
        if (name.charAt(x) != '_' && name.charAt(x) != ' ')
          if (toUpper)
            regresar.append(Character.toUpperCase(name.charAt(x)));
          else
            regresar.append(Character.toLowerCase(name.charAt(x)));
        toUpper = name.charAt(x) == '_' || name.charAt(x) == ' ';
      } // for x
    } // if
    return regresar.toString();
  }

  public static String tokens(String msg, Map params, String nullAsString) {
    return tokens(msg, params, nullAsString, Constantes.TILDE.charAt(0));
  }

  public static String tokens(String msg, Map params) {
    return tokens(msg, params, null);
  }

  public static String toEmpty(Object object) {
    return object== null? "": object.toString();
  }

  public static String parameter(Object object, String value) {
    return object== null? value: object.toString();
  }

  public static String parameter(Object object) {
    return parameter(object, "");
  }

  public static String toLowerCase(String cadena) {
    String regresar = "";
    for(int i = 0; i < cadena.length(); i++) {
      regresar = regresar.concat(String.valueOf(Character.toLowerCase(cadena.charAt(i))));
    } // for
    return regresar;
  }

  public static List<String> toListParams(String cadena) {
    List<String> regresar = null;
    String token = null;
    StringTokenizer resultado = new StringTokenizer(cadena, "{}", true);
    regresar = new ArrayList<>();
    while (resultado.hasMoreTokens()) {
      token = resultado.nextToken();
      if (token.equals("{")) {
        token= resultado.nextToken();
        regresar.add(token);
      } // if
    }  // while
    return regresar;
  }
  /**
   * Convierte un mapa en una cadena separados po | y los valores por ~ Ej. |key1~value1|key2~value2|
   * @param params Mapa de parametros a evaluar
   * @return
   */
 public static String toParams(Map<String, Object> params) {
   StringBuilder regresar = new StringBuilder();
   for(String key: params.keySet()) {
     regresar.append(Constantes.SEPARADOR);
     regresar.append(key);
     regresar.append(Constantes.TILDE);
		 Object value= params.get(key);
     regresar.append(value!= null? "": value);
   } // for
   regresar.append(Constantes.SEPARADOR);
   return regresar.toString();
 }
  /**
   * Convierte un mapa en una cadena separados por el carcter que se envie y los valores por ~ Ej. Si se envia el # = |key1#value1|key2#value2|
   * @param params Mapa de parametros a evaluar
   * @param token caracter que separará el key del valor
   * @return
   */
 public static String toLineParams(Map<String, String> params, char token) {
   StringBuilder regresar = new StringBuilder(Constantes.SEPARADOR);
   for(String key: params.keySet()) {
     regresar.append(key);
     regresar.append(token);
     regresar.append(params.get(key));
     regresar.append(Constantes.SEPARADOR);
   } // for
   return regresar.toString();
 }
  /**
   * Convierte un mapa en una cadena separados po | y los valores por ~ Ej. |key1~value1|key2~value2|
   * @param params Mapa de parametros a evaluar
   * @return
   */
 public static String toLineParams(Map<String, String> params) {
   return toLineParams(params, '~');
 }

 public static Map<String, Object> toParams(Map<String, String> params, String ... tokens) {
   Map<String, Object> regresar= new HashMap<>();
   if (params!= null && !params.isEmpty()) {
     for (String key: params.keySet()) {
       for(String token: tokens)
         if (key.startsWith(token)) {
           Object object= params.get(key);
					 if(!isVacio(object) && !object.toString().startsWith("{") && !object.toString().endsWith("{"))
             if(object instanceof String)
               regresar.put(key, object.toString());
             else
               regresar.put(key, ((String[])object)[0]);
					 else
						 regresar.put(key, "");
         } // if
     } // for
   } // if
   return regresar;
 }

 public static Map<String, Object> toParamsObject(Map<String, Object> params, String ... tokens) {
   return toParamsObject(false, params, tokens);
 }

 public static Map<String, Object> toParamsObject(boolean real, Map<String, Object> params, String ... tokens) {
   Map<String, Object> regresar = new HashMap<>();
   String realKey               = null;
   if (params!= null && !params.isEmpty()) {
     for (String key: params.keySet()) {
       for(String token: tokens)
         if (key.startsWith(token)) {
           Object object= params.get(key);
           realKey = real ? key : key.substring(token.length(), key.length());
					 if(!isVacio(object) && !object.toString().startsWith("{") && !object.toString().endsWith("{")) {
             if(object instanceof String)
               regresar.put(realKey, object.toString());
             else
               regresar.put(realKey, ((String[])object)[0]);
           }
					 else
						 regresar.put(realKey, "");
         } // if
     } // for
   } // if
   return regresar;
 }

 public static Map<String, Object> toUniqueParams(Map<String, Object> params, String ... tokens) {
   Map<String, Object> regresar= new HashMap<>();
   if (params!= null && !params.isEmpty()) {
     for(Object key: params.keySet()) {
       for(String token: tokens) {
         if(key.toString().startsWith(token)) {
           String item= key.toString().substring(Prioridad.PREFIX.length());
           if(!params.containsKey(item) && !isVacio(params.get(key).toString()))
             regresar.put(item, params.get(key));
         } // if
       } // for
     } // for
     for(String key: params.keySet()) {
			 if(!key.startsWith(Prioridad.PREFIX.getCode())) {
         if(!params.containsKey(key) && !isVacio(params.get(key)))
           regresar.put(key, params.get(key));
			 } // if
		 } // for
	 } // if
   return regresar;
 }

 public static void updateParams(Map<String, Object> params, Map<String, Object> page, String ... tokens) {
   try {
     for(String key: page.keySet()) {
       for(String token : tokens) {
         if(key.startsWith(token)) {
           String item= key.substring(Prioridad.PREFIX.length());
           if(!params.containsKey(item) && !isVacio(page.get(key)))
             params.put(item, page.get(key));
         } // if
       } // for
     } // for
     for(String key: page.keySet()) {
			 if(!key.startsWith(Prioridad.PREFIX.getCode())) {
         if(!params.containsKey(key) && !isVacio(page.get(key)))
           params.put(key, page.get(key));
			 } // if
		 } // for
   }
   catch(Exception e) {
     Error.mensaje(e);
   }
 }

 public static List<String> toParamsSQL(String sql) {
   StringTokenizer resultado = new StringTokenizer(sql, "{}", true);
   String token = null;
   List<String> regresar= new ArrayList<>();
   while (resultado.hasMoreTokens()) {
     token = resultado.nextToken();
     if (token.equals("{")) {
       token= resultado.nextToken();
       if(regresar.toString().indexOf(token)< 0)
         regresar.add(Constantes.SEPARADOR.concat(token).concat(Constantes.TILDE).concat("?").concat(Constantes.SEPARADOR));
       token= resultado.nextToken();
     } // if
   } // while
   return regresar;
 }
 /**
  * Remueve todos los paramentros de un mapa
  * @param params  mapa a limpiar
  */
 public static void cleanParams(Map<String, Object> params) {
   List<String> keys= new ArrayList<>();
   for(String key : params.keySet()) {
     if(isVacio(params.get(key)) || params.get(key).toString().startsWith("{"))
       keys.add(key);
   } // for
   for(String key: keys) {
     params.remove(key);
   } // for
   keys.clear();
 }
/**
 * Formatea el nombre de un archivo con el estandar de Kajool Ej. toFileName("resutadosPreliminares") = xKajool_Domingo,16 de Enero de 2015 08:24:05
 * @param name
 * @return
 */
 public static String toFileName(String name) {
  String fecha = Fecha.formatear(Fecha.FECHA_HORA_EXTENDIDA);
  return "xKajool".concat(fecha).concat("_").concat(name);
 }

 public static String toFileNameTime(String name) {
  String fecha = Fecha.getRegistro();
  return "xKajool".concat(fecha).concat("_").concat(name);
 }

 public static String toFileName() {
   return toFileName("");
 }
 /**
  * Recorta una descripcion a un determinado tamaño y le concatena a la descripcion ... al final
  * @param tamanio Cantidad de caracteres a recortar
  * @param cadena Cadena a recortar
  * @return
  */
 public static String shortDescription (int tamanio, String cadena) {
   String regresar = cadena;
   if(cadena.length() > tamanio) {
     regresar = cadena.substring(0, tamanio);
     regresar = regresar.concat("...");
   }
   return regresar;
 }

 public static String reemplazarCaracter(String cadena, char delete, char add) {
   StringBuilder regresar = new StringBuilder();
   for(int index = 0; index < cadena.length(); index++)
     regresar.append(cadena.charAt(index)!= delete? cadena.charAt(index): add);
   return regresar.toString();
 }
 /**
  * Obtiene el nombre de un metodo con un determinado prefijo Ej. nameMethods("agregar", "do") = doAgregar
  * @param name nombre del metodo
  * @param start nombre del prefijo que no debe ser igual al comienzo del nombre
  * @return
  */
  private static String nameMethods(String name, String start) {
   String regresar= name;
   if(!name.startsWith(start))
    regresar= start.concat(name.toUpperCase().substring(0, 1)).concat(name.substring(1));
     return regresar;
  }
/**
 * Obtiene en nombre get de un atributo Ej. idTipoDato = getIdTipoDato
 * @param name nombre del atributo a  obtener el nombre get
 * @return
 */
  public static String getNameMethod(String name) {
     return nameMethods(name, "get");
  }
/**
 * Obtiene en nombre set de un atributo Ej. idTipoDato = setIdTipoDato
 * @param name nombre del atributo a  obtener el nombre set
 * @return
 */
  public static String setNameMethod(String name) {
   return nameMethods(name, "set");
  }

  public synchronized static void removeParams(Map<String, Object> page, String ... tokens) {
		List<String> list= new ArrayList<>();
    try {
			for(String key : page.keySet()) {
				for(String token : tokens) {
					if (key.startsWith(token) || key.indexOf(token)> 0) {
						String item= key.substring(Prioridad.PREFIX.length());
						if(!page.containsKey(item) && !isVacio(page.get(key)))
							list.add(key);
					} // if
				} // for
			} // for
			for(String key: list) {
				page.remove(key);
			} // for
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
  }

  /**
   * Concatena los valores un arreglo de cadenas
   * @param args Arreglo de Strings a concatenar
   * @return
   */
  public static String concat(String... args) {
  	StringBuilder sb = new StringBuilder();
  	for ( String arg : args) {
  		sb.append(arg);
  	} // for
  	return sb.toString();
  }
  /**
   * Quita los acentos de una cadena de texto
   * @param cadena Cadena a formatear
   * @return
   */
   public static String sinAcentos(String cadena){
     String regresar = cadena;
     regresar = reemplazarCaracter(regresar, 'á', 'a');
     regresar = reemplazarCaracter(regresar, 'Á', 'A');
     regresar = reemplazarCaracter(regresar, 'é', 'e');
     regresar = reemplazarCaracter(regresar, 'É', 'E');
     regresar = reemplazarCaracter(regresar, 'í', 'i');
     regresar = reemplazarCaracter(regresar, 'Í', 'I');
     regresar = reemplazarCaracter(regresar, 'ó', 'o');
     regresar = reemplazarCaracter(regresar, 'Ó', 'O');
     regresar = reemplazarCaracter(regresar, 'ú', 'u');
     regresar = reemplazarCaracter(regresar, 'Ú', 'U');
     return regresar;
  }
 /**
  *  Crea el expresion laguaje de una accion para una pagina. Ej.  aceptar = #{manageBean.doAceptar}
  * @param nameManageBean Nombre del manage bean a concatenar
  * @param accion Nombre de la accion
  * @return
  */
  public static String nombreAccion(String nameManageBean, String accion){
     String regresar = "#{".concat(nameManageBean).concat(".do".concat(Cadena.letraCapital(accion))).concat("}");
     return regresar;

   }
  /**
   * Obtiene un arreglo cadena con los bytes separados por comas de una cadena a evaluar (Caracter por caracter)
   * @param text Cadena a evaluar
   * @return
   */
  public static String stringToBytes(String text) {
		StringBuilder regresar= new StringBuilder("");
		for (int x=0; x<text.length(); x++) {
		  regresar.append((int)text.charAt(x));
			if(x!= text.length()- 1)
    		regresar.append(",");
		} // for x
		return regresar.toString();
  }

}

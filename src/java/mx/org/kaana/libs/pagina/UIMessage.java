package mx.org.kaana.libs.pagina;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12-feb-2014
 *@time 14:26:27
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.enums.EModulosJar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UIMessage implements Serializable {

  private static final long serialVersionUID = -1348515403502762875L;
  private static final Log LOG = LogFactory.getLog(UIMessage.class);
  /**
   * Regresa el mensaje del archivo predeterminado .properties
   * @param key llave del mensaje
   * @return Regresa el mensaje
   */
  public static String toMessage(String key) {
    return toMessage(Constantes.NOMBRE_, key);
  }
/**
 * Obtiene el mensaje del archivo .properties de un proyecto enviado
 * @param proyecto Nombre del proyecto den donde se encuentra el .properties Ej. ("SEP", "mensaje_error") = En sep.properties busca la propiedad mensaje_error
 * @param key Nombre de la propiedad del mensaje
 * @return  Regresa el mensaje solicitado
 */
  public static String toMessage(String proyecto, String key) {
    return toMessage(proyecto, key, Collections.EMPTY_MAP);
  }

  /**
 * Obtiene el mensaje del archivo .properties de un proyecto enviado
 * @param proyecto Nombre del proyecto den donde se encuentra el .properties Ej. (EmodulosJar.ALUMNOS, "mensaje_error") = En sep.properties busca la propiedad mensaje_error
 * @param key Nombre de la propiedad del mensaje
 * @return  Regresa el mensaje solicitado
 */
  public static String toMessage(EModulosJar proyecto, String key) {
     return toMessage(proyecto.name().toLowerCase(), key, Collections.EMPTY_MAP);
  }
  /**
 * Obtiene el mensaje del archivo .properties de un proyecto enviado parseado con los paramentros
 * @param proyecto Nombre del proyecto den donde se encuentra el .properties Ej. ("SEP", "mensaje_error", params) = En sep.properties busca la propiedad mensaje_error y remplaza los paramentros en el mensaje {paramentro}
 * @param key Nombre de la propiedad del mensaje
 * @param params Mapa de parametros a reemplazar
 * @return  Regresa el mensaje solicitado
 */
  public static String toMessage(String key, Map<String, Object> params) {
    return toMessage(Constantes.NOMBRE_, key, params);
  }
  /**
 * Obtiene el mensaje del archivo .properties del archivo predeterminado .properties parseado con los paramentros
 * @param key Nombre de la propiedad del mensaje
 * @param params Mapa de parametros a reemplazar
 * @return  Regresa el mensaje solicitado
 */
  public static String toMessage(String proyecto, String key, Map<String, Object> params) {
    String regresar= Messages.getInstance().getPropiedad(proyecto, key);
    return Cadena.replaceParams(regresar, params);
  }

   public static String toMessage(EModulosJar proyecto, String key, Map<String, Object> params) {
    return toMessage(proyecto.name().toLowerCase(),key,params);
  }

    /**
 * Obtiene el mensaje del archivo .properties del archivo predeterminado .properties parseado con los paramentros
 * @param proyeco Enumerado del proyecto
 * @param key Nombre de la propiedad del mensaje
 * @param params Mapa de parametros a reemplazar
 * @return  Regresa el mensaje solicitado
 */

  public static void main(String ... args) {
    LOG.debug(toMessage("enh", "mensaje"));
  }

}

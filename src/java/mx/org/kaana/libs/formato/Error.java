/*
 * Clase: Error.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: alejandro.jimenez
 */
package mx.org.kaana.libs.formato;

import java.text.MessageFormat;

import mx.org.kaana.libs.recurso.Configuracion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public final class Error {

  private static final Log LOG= LogFactory.getLog(Error.class);

  private Error() {
  }

  public static String getPaquete(Object objeto)  {
    return objeto.getClass().getPackage().getName();
  }

  public static String getDominio(Object objeto)  {
    return objeto.getClass().getSimpleName();
  }

  public static String getNombre(Object objeto)  {
    return objeto.getClass().getName();
  }

  public static String getMensaje(Object objeto)  {
    return "[".concat(getNombre(objeto)).concat(".{0}] Error: {1}");
  }

  public static String getMensaje(Object objeto, Object ... valores)  {
    return MessageFormat.format(getMensaje(objeto), valores);
  }

  public static void mensaje(Object objeto, Throwable exception, Object ... valores)  {
    if (Configuracion.getInstance().getPropiedad("sistema.log.error.".concat(Configuracion.getInstance().getPropiedad("sistema.servidor"))).equals("si")){
      LOG.error(MessageFormat.format(getMensaje(objeto), valores));
      LOG.warn(exception);
    } // if
  }

  public static String getMensaje() {
    return "[{0}.{1}] Error: {2}";
  }

  public static void mensaje(Throwable exception, String propio)  {
    if (Configuracion.getInstance().getPropiedad("sistema.log.error.".concat(Configuracion.getInstance().getPropiedad("sistema.servidor"))).equals("si")){
      StackTraceElement[] stackTraceElements= exception.getStackTrace();
      Object[] valores= new Object[] {stackTraceElements[0].getClassName(), stackTraceElements[0].getMethodName(), propio};
      LOG.error(MessageFormat.format(getMensaje(), valores));
      LOG.warn(exception);
      exception.printStackTrace();
    } // if
  }

  public static void mensaje(Throwable exception)  {
    mensaje(exception, "");
  }

  public static void notificar(Throwable exception, String propio)  {
    StackTraceElement[] stackTraceElements= exception.getStackTrace();
    Object[] valores= new Object[] {stackTraceElements[0].getClassName(), stackTraceElements[0].getMethodName(), propio};
    LOG.info(MessageFormat.format(getMensaje(), valores));
    LOG.debug(exception);
  }

  public static void main(String[] args) {
    Error.mensaje(new Exception("Hola"));
    Error.mensaje(new Exception("Hola"), "MENSAJE PROPIO");
  }

}

/*
 * Code write by user.development
 * Date 24/09/2008
 */
package mx.org.kaana.libs.formato;

import mx.org.kaana.kajool.enums.IFormatosKajool;

public final class Global {

  private Global() {
  }

  public static String format(IFormatosKajool type, Object value) {
    return type.execute(value);
  } // format 
}

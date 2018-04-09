package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 11/10/2016
 *@time 02:51:34 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ETipoConsulta {

  ORIGEN,
  DESTINO;

  private static final Map<Integer, ETipoConsulta> lookup = new HashMap<>();

  static {
    for (ETipoConsulta item: EnumSet.allOf(ETipoConsulta.class))
      lookup.put(item.ordinal()+ 1, item);
  } // static

  public Long getKey(){
		return new Long(this.ordinal() + 1);
	}

  public static ETipoConsulta fromOrdinal(Long ordinal) {
    return lookup.get(ordinal.intValue());
  }
}

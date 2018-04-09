package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 12:01:16 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ETipoMovimiento {

  ALTA,
	BAJA;

  private static final Map<Integer, ETipoMovimiento> lookup = new HashMap<>();

  static {
    for (ETipoMovimiento item: EnumSet.allOf(ETipoMovimiento.class))
      lookup.put(item.ordinal()+ 1, item);
  } // static

  public Long getKey(){
		return new Long(this.ordinal() + 1);
	}

  public static ETipoMovimiento fromOrdinal(Long ordinal) {
    return lookup.get(ordinal.intValue());
  }
}

package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 06:33:44 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatus {

  DISPONIBLE,
  ASIGNADO,
  COMPLETO,
  PARCIAL,
  VACIO,
	LIBERADO_CAMPO;

  private static final Map<Integer, EEstatus> lookup = new HashMap<>();

  static {
    for (EEstatus item: EnumSet.allOf(EEstatus.class))
      lookup.put(item.ordinal()+ 1, item);
  } // static

  public Long getKey(){
		return new Long(this.ordinal() + 1);
	}

  public static EEstatus fromOrdinal(Long ordinal) {
    return lookup.get(ordinal.intValue());
  }
}

package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 04:04:16 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ETipoGrafica {

  COLUMNA ("column"),
  LINEAL  ("line"),
  BARRAS  ("bar"),  
  PIE     ("pie");

  private static final Map<Integer, ETipoGrafica> lookup = new HashMap<>();
  private String name;

  private ETipoGrafica(String name) {
    this.name= name;
  }

  static {
    for (ETipoGrafica item: EnumSet.allOf(ETipoGrafica.class))
      lookup.put(item.ordinal()+ 1, item);
  } // static

  public Long getKey(){
		return new Long(this.ordinal() + 1);
	}

  public String getName() {
    return name;
  }

  public static ETipoGrafica fromOrdinal(Long ordinal) {
    return lookup.get(ordinal.intValue());
  }
}

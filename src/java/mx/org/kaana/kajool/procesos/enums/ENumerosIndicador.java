package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 12:08:28 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ENumerosIndicador {

  UNO        (1L, 5D),
  DOS        (2L, 10D),
  TRES       (3L, 15D),
  CUATRO     (4L, 20D),
  CINCO      (5L, 25D),
  SEIS       (6L, 30D),
  SIETE      (7L, 35D),
  OCHO       (8L, 40D),
  NUEVE      (9L, 45D),
  DIEZ       (10L, 50D),
  ONCE       (11L, 55D),
  DOCE       (12L, 60D),
  TRECE      (13L, 65D),
  CATORCE    (14L, 70D),
  QUINCE     (15L, 75D),
  DIECISEIS  (16L, 80D),
  DIECISIETE (17L, 85D),
  DIECIOCHO  (18L, 90D),
  DIECINUEVE (19L, 95D),
  VEINTE     (20L, 100D);

  private static final Map<Integer, ENumerosIndicador> lookup = new HashMap<>();
  private Long numero;
  private Double porcentaje;

  private ENumerosIndicador(Long numero, Double porcentaje) {
    this.numero    = numero;
    this.porcentaje= porcentaje;
  }

  static {
    for (ENumerosIndicador item: EnumSet.allOf(ENumerosIndicador.class))
      lookup.put(item.ordinal()+ 1, item);
  } // static

  public Long getKey(){
		return new Long(this.ordinal() + 1);
	}

  public Long getNumero() {
    return numero;
  }

  public Double getPorcentaje() {
    return porcentaje;
  }

  public static ENumerosIndicador fromOrdinal(Long ordinal) {
    return lookup.get(ordinal.intValue());
  }
}

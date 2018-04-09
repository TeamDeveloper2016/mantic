package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 03:26:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusSemaforos {

  DISPONIBLE		(1L, ESemaforos.GRIS),
	ASIGNADO			(2L, ESemaforos.ROJO),
	COMPLETO			(3L, ESemaforos.VERDE),
	PARCIAL				(4L, ESemaforos.AMARILLO),
	SIN_CAPTURA		(5L, ESemaforos.ROJO),
	LIBERADO_CAMPO(6L, ESemaforos.GRIS);

  private static final Map<Long, EEstatusSemaforos> lookup = new HashMap<>();
  private ESemaforos semaforo;	
	private Long key;	

  static {
    for (EEstatusSemaforos item: EnumSet.allOf(EEstatusSemaforos.class))
      lookup.put(item.getKey(), item);
  } // static

  private EEstatusSemaforos(Long key, ESemaforos semaforo) {
		this.key     = key;
		this.semaforo= semaforo;
	}

	public Long getKey() {
		return key;
	}

	public ESemaforos getSemaforo() {
		return semaforo;
	}	
	
	public static EEstatusSemaforos toIdEstatus(Long idEstatus){
		return lookup.get(idEstatus);
	}	
}

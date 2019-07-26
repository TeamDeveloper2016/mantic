package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusCotizaciones {
  
  ELABORADA  (17L),
  FINALIZADA (18L),
  CANCELADA  (19L);

  private Long idEstatusFicticia;
	private static final Map<Long, EEstatusCotizaciones> lookup= new HashMap<>();

  static {
    for (EEstatusCotizaciones item: EnumSet.allOf(EEstatusCotizaciones.class)) 
      lookup.put(item.getIdEstatusFicticia(), item);    
  }

  private EEstatusCotizaciones(Long idEstatusFicticia) {
    this.idEstatusFicticia = idEstatusFicticia;
  }

  public Long getIdEstatusFicticia() {
    return idEstatusFicticia;
  }
	
	public static EEstatusCotizaciones fromIdFicticia(Long idEstatusFicticia) {
    return lookup.get(idEstatusFicticia);
  } 
}
package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusIncidentes {

  CAPTURADA (1L),
  ACEPTADA  (2L),
	APLICADA  (3L),
  CANCELADA (4L);

  private Long idEstatusInicidente;
	private static final Map<Long, EEstatusIncidentes> lookup= new HashMap<>();

  static {
    for (EEstatusIncidentes item: EnumSet.allOf(EEstatusIncidentes.class)) 
      lookup.put(item.getIdEstatusInicidente(), item);    
  }

  private EEstatusIncidentes(Long idEstatusInicidente) {
    this.idEstatusInicidente = idEstatusInicidente;
  }

  public Long getIdEstatusInicidente() {
    return idEstatusInicidente;
  }
	
	public static EEstatusIncidentes fromIdEstatusIncidente(Long idEstatusInicidente) {
    return lookup.get(idEstatusInicidente);
  } 
}
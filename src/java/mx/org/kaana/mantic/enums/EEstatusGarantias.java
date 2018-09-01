package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusGarantias {

  ELABORADA (1L),
  RECIBIDA  (2L),
  ENTREGADA (3L),
  GARANTIA  (4L),
  SUSTITUIDA(5L),
  TERMINADA (6L),
  ELIMINADA (7L);

  private Long idEstatusGarantia;
	private static final Map<Long, EEstatusGarantias> lookup= new HashMap<>();

  static {
    for (EEstatusGarantias item: EnumSet.allOf(EEstatusGarantias.class)) 
      lookup.put(item.getIdEstatusGarantia(), item);    
  }

  private EEstatusGarantias(Long idEstatusGarantia) {
    this.idEstatusGarantia = idEstatusGarantia;
  }

  public Long getIdEstatusGarantia() {
    return idEstatusGarantia;
  }
	
	public static EEstatusGarantias fromIdEstatus(Long idEstatusGarantia) {
    return lookup.get(idEstatusGarantia);
  } 
}
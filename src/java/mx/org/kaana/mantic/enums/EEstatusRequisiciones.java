package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusRequisiciones {

  ELABORADA (1L),
  SOLICITADA(2L),
  ELIMINADA (3L),
  CANCELADA (4L),
  COTIZADA  (5L);

  private Long idEstatusRequisicion;
	private static final Map<Long, EEstatusRequisiciones> lookup= new HashMap<>();

  static {
    for (EEstatusRequisiciones item: EnumSet.allOf(EEstatusRequisiciones.class)) 
      lookup.put(item.getIdEstatusRequisicion(), item);    
  }

  private EEstatusRequisiciones(Long idEstatusRequisicion) {
    this.idEstatusRequisicion = idEstatusRequisicion;
  }

  public Long getIdEstatusRequisicion() {
    return idEstatusRequisicion;
  }
	
	public static EEstatusRequisiciones fromIdTipoPago(Long idEstatusRequisicion) {
    return lookup.get(idEstatusRequisicion);
  } 
}
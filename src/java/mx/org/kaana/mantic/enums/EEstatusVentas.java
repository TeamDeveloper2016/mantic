package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusVentas {

  ELABORADA  (1L),
  ABIERTA    (2L),
  PAGADA     (3L),
  CREDITO    (4L),
  CANCELADA  (5L),
  TERMINADA  (6L),
  COTIZACION (7L),
  EXPIRADA   (8L),
  APARTADOS  (9L),
	EN_CAPTURA (10L),
	TIMBRADA   (15L),
	ELIMINADA  (16L);

  private Long idEstatusVenta;
	private static final Map<Long, EEstatusVentas> lookup= new HashMap<>();

  static {
    for (EEstatusVentas item: EnumSet.allOf(EEstatusVentas.class)) 
      lookup.put(item.getIdEstatusVenta(), item);    
  }

  private EEstatusVentas(Long idEstatusVenta) {
    this.idEstatusVenta = idEstatusVenta;
  }

  public Long getIdEstatusVenta() {
    return idEstatusVenta;
  }
	
	public static EEstatusVentas fromIdTipoPago(Long idEstatusVenta) {
    return lookup.get(idEstatusVenta);
  } 
}
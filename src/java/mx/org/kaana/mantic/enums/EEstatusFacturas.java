package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusFacturas {
  
  REGISTRADA(1L),
  TIMBRADA  (2L),  
  CANCELADA (3L),
	AUTOMATICO(4L),
	PROCESANDO(5L);

  private Long idEstatusFactura;
	private static final Map<Long, EEstatusFacturas> lookup= new HashMap<>();

  static {
    for (EEstatusFacturas item: EnumSet.allOf(EEstatusFacturas.class)) 
      lookup.put(item.getIdEstatusFactura(), item);    
  }

  private EEstatusFacturas(Long idEstatusFactura) {
    this.idEstatusFactura = idEstatusFactura;
  }

  public Long getIdEstatusFactura() {
    return idEstatusFactura;
  }
	
	public static EEstatusFacturas fromIdEstatusFactura(Long idEstatusFactura) {
    return lookup.get(idEstatusFactura);
  } 
}
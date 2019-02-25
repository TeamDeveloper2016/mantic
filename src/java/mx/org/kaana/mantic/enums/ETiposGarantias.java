package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ETiposGarantias {

  TERMINADA  (1L),
  RECIBIDA   (2L);

  private Long idTipoGarantia;
	private static final Map<Long, ETiposGarantias> lookup= new HashMap<>();

  static {
    for (ETiposGarantias item: EnumSet.allOf(ETiposGarantias.class)) 
      lookup.put(item.getIdTipoGarantia(), item);    
  }

  private ETiposGarantias(Long idTipoGarantia) {
    this.idTipoGarantia = idTipoGarantia;
  }

  public Long getIdTipoGarantia() {
    return idTipoGarantia;
  }
	
	public static ETiposGarantias fromIdTipoGarantia(Long idTipoGarantia) {
    return lookup.get(idTipoGarantia);
  } 
}
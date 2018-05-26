package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ETipoPago {

  EFECTIVO (1L),
  CREDITO  (2L);

  private Long idTipoPago;
	private static final Map<Long, ETipoPago> lookup= new HashMap<>();

  static {
    for (ETipoPago item: EnumSet.allOf(ETipoPago.class)) 
      lookup.put(item.getIdTipoPago(), item);    
  }

  private ETipoPago(Long idTipoPago) {
    this.idTipoPago = idTipoPago;
  }

  public Long getIdTipoPago() {
    return idTipoPago;
  }
	
	public static ETipoPago fromIdTipoPago(Long idTipoPago) {
    return lookup.get(idTipoPago);
  } 
}
package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ETipoPago {

  EFECTIVO (1L, "PUE"),
  CREDITO  (2L, "PPD");

  private Long idTipoPago;
	private String clave;
	private static final Map<Long, ETipoPago> lookup= new HashMap<>();

  static {
    for (ETipoPago item: EnumSet.allOf(ETipoPago.class)) 
      lookup.put(item.getIdTipoPago(), item);    
  }

  private ETipoPago(Long idTipoPago, String clave) {
    this.idTipoPago= idTipoPago;
		this.clave     = clave;
  }

  public Long getIdTipoPago() {
    return idTipoPago;
  }

	public String getClave() {
		return clave;
	}
	
	public static ETipoPago fromIdTipoPago(Long idTipoPago) {
    return lookup.get(idTipoPago);
  } 
}
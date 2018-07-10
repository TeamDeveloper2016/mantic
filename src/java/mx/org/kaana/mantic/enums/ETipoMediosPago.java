package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ETipoMediosPago {

  EFECTIVO       (1L),
  CHEQUE         (2L),
  TRANSFERENCIA  (3L),
  TARJETA_CREDITO(4L),
  TARJETA_DEBITO (18L),
  POR_DEFINIR    (22L);

  private Long idTipoMedioPago;
	private static final Map<Long, ETipoMediosPago> lookup= new HashMap<>();

  static {
    for (ETipoMediosPago item: EnumSet.allOf(ETipoMediosPago.class)) 
      lookup.put(item.getIdTipoMedioPago(), item);    
  }

  private ETipoMediosPago(Long idTipoMedioPago) {
    this.idTipoMedioPago = idTipoMedioPago;
  }

  public Long getIdTipoMedioPago() {
    return idTipoMedioPago;
  }
	
	public static ETipoMediosPago fromIdTipoPago(Long idTipoMedioPago) {
    return lookup.get(idTipoMedioPago);
  } 
}
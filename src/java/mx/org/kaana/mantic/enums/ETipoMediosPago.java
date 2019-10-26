package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;

public enum ETipoMediosPago {

  EFECTIVO            (1L,  true),
  CHEQUE              (2L,  true),
  TRANSFERENCIA       (3L,  true),
  TARJETA_CREDITO     (4L,  true),
  VALES_DESPENSA      (7L,  false),
  TARJETA_DEBITO      (18L, true),
	INTERMEDIARIO_PAGOS (21L, false);

  private Long idTipoMedioPago;
	private boolean caja;
	private static final Map<Long, ETipoMediosPago> lookup= new HashMap<>();

  static {
    for (ETipoMediosPago item: EnumSet.allOf(ETipoMediosPago.class)) 
      lookup.put(item.getIdTipoMedioPago(), item);    
  }

  private ETipoMediosPago(Long idTipoMedioPago, boolean caja) {
    this.idTipoMedioPago = idTipoMedioPago;
		this.caja= caja;
  }

  public Long getIdTipoMedioPago() {
    return idTipoMedioPago;
  }
	
	public static ETipoMediosPago fromIdTipoPago(Long idTipoMedioPago) {
    return lookup.get(idTipoMedioPago);
  } 

	public boolean isCaja() {
		return caja;
	}	
	
	public String getNombre(){
		return Cadena.reemplazarCaracter(this.name(), '_', ' ');
	}
}
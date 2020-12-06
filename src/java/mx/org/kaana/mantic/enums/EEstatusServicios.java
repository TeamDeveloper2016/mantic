package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusServicios {

  ELABORADA(1L),
  COTIZANDO(2L),
  EN_ESPERA(3L),
  EN_REPARACION(4L),
  REPARADO(5L),
  ENTREGADO(6L),
  CANCELADA(7L),
  EN_CAJA(8L),
  LISTO_ENTREGA(9L),
  PAGADO(10L);
  
  private Long idEstatusServicio;
  
	private static final Map<Long, EEstatusServicios> lookup= new HashMap<>();

  static {
    for (EEstatusServicios item: EnumSet.allOf(EEstatusServicios.class)) 
      lookup.put(item.getIdEstatusServicio(), item);    
  }

  private EEstatusServicios(Long idEstatusServicio) {
    this.idEstatusServicio = idEstatusServicio;
  }

  public Long getIdEstatusServicio() {
    return idEstatusServicio;
  }
	
	public static EEstatusServicios fromIdEstatusServicio(Long idEstatusServicio) {
    return lookup.get(idEstatusServicio);
  } 

}

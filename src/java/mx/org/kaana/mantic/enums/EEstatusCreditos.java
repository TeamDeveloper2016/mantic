package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusCreditos {

  ELABORADA   (1L),
  ELIMINADA   (2L),
  PARCIALIZADA(3L),
	TERMINADA   (4L);

  private Long idEstatusCredito;
	private static final Map<Long, EEstatusCreditos> lookup= new HashMap<>();

  static {
    for (EEstatusCreditos item: EnumSet.allOf(EEstatusCreditos.class)) 
      lookup.put(item.getIdEstatusCredito(), item);    
  }

  private EEstatusCreditos(Long idEstatusCredito) {
    this.idEstatusCredito = idEstatusCredito;
  }

  public Long getIdEstatusCredito() {
    return idEstatusCredito;
  }
	
	public static EEstatusCreditos fromIdEstatusCredito(Long idEstatusCredito) {
    return lookup.get(idEstatusCredito);
  } 
}
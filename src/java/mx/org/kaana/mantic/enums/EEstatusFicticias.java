package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusFicticias {

  ELABORADA  (1L),
  ABIERTA    (2L),
  PAGADA     (3L),
  CREDITO    (4L),
  CANCELADA  (5L),
  TERMINADA  (6L);  

  private Long idEstatusFicticia;
	private static final Map<Long, EEstatusFicticias> lookup= new HashMap<>();

  static {
    for (EEstatusFicticias item: EnumSet.allOf(EEstatusFicticias.class)) 
      lookup.put(item.getIdEstatusFicticia(), item);    
  }

  private EEstatusFicticias(Long idEstatusFicticia) {
    this.idEstatusFicticia = idEstatusFicticia;
  }

  public Long getIdEstatusFicticia() {
    return idEstatusFicticia;
  }
	
	public static EEstatusFicticias fromIdFicticia(Long idEstatusFicticia) {
    return lookup.get(idEstatusFicticia);
  } 
}
package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/02/2018
 *@time 08:10:23 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


public enum EEstatusFicticias {
  
  ABIERTA   (11L),
  TIMBRADA  (12L),
  ELIMINADA (13L),
  CANCELADA (14L);

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
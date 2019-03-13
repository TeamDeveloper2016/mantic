package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusEmpresas {

  INICIADA    (1L),
  PROGRAMADA  (2L),
  PARCIALIZADA(3L),
  LIQUIDADA   (4L);

  private Long idEstatusEmpresa;
	private static final Map<Long, EEstatusEmpresas> lookup= new HashMap<>();

  static {
    for (EEstatusEmpresas item: EnumSet.allOf(EEstatusEmpresas.class)) 
      lookup.put(item.getIdEstatusEmpresa(), item);    
  }

  private EEstatusEmpresas(Long idEstatusEmpresa) {
    this.idEstatusEmpresa = idEstatusEmpresa;
  }

  public Long getIdEstatusEmpresa() {
    return idEstatusEmpresa;
  }
	
	public static EEstatusEmpresas fromIdEstatusEmpresa(Long idEstatusEmpresa) {
    return lookup.get(idEstatusEmpresa);
  } 
}
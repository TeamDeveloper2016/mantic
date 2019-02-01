package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EEstatusEgresos {

	REGISTRADO,
	INCOMPLETO,
	COMPLETO;
	
	private static final Map<Long, EEstatusEgresos> lookup= new HashMap<>();
	
	static {
    for (EEstatusEgresos item: EnumSet.allOf(EEstatusEgresos.class)) 
      lookup.put(item.getKey(), item);    
  } // static
	
	public Long getKey(){
		return this.ordinal() + 1L;
	} // getKey
	
	public static EEstatusEgresos fromIdEstatusEgreso(Long idEstatusEgreso) {
    return lookup.get(idEstatusEgreso);
  } // fromIdEstatusEgreso
}

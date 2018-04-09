package mx.org.kaana.kajool.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EAmbitos {

	CENTRAL   (new ArrayList()),
	REGIONAL  (new ArrayList()),
	ESTATAL   (new ArrayList(){{add("Municipio"); add("Localidad"); add("Oficina");}}),
	OFICINA   (new ArrayList()),
  MUNICIPIO (new ArrayList(){{add("Localidad");}}),
  LOCALIDAD (new ArrayList());
	
	private static final Map<Integer, EAmbitos> lookup = new HashMap<Integer, EAmbitos>();	
	private List<String> cortesAplican;	
	
	private EAmbitos(List<String>cortesAplican){		
		this.cortesAplican= cortesAplican;
	}	
	
	static {
    for (EAmbitos item: EnumSet.allOf(EAmbitos.class)) {
      lookup.put(item.ordinal()+ 1, item);
    } // for
  }
	
	public Integer getKey() {
    return ordinal()+1;
  }
	
	public Long getIdAmbito() {						
		return ((Integer)(ordinal()+ 1)).longValue();
	}

	public List<String> getCortesAplican() {
		return cortesAplican;
	}
	
	public static EAmbitos fromOrdinal(int ordinal) {
    return lookup.get(ordinal);
  }		
}

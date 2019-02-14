package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ENivelUbicacion {

  EMPRESA    (1L),
  ALMACEN    (2L),
  UBICACION  (3L);  

  private Long idNivelUbicacion;
	private static final Map<Long, ENivelUbicacion> lookup= new HashMap<>();

  static {
    for (ENivelUbicacion item: EnumSet.allOf(ENivelUbicacion.class)) 
      lookup.put(item.getIdNivelUbicacion(), item);    
  }

  private ENivelUbicacion(Long idNivelUbicacion) {
    this.idNivelUbicacion = idNivelUbicacion;
  }

	public Long getIdNivelUbicacion() {
		return idNivelUbicacion;
	}  
	
	public static ENivelUbicacion fromIdNivel(Long idNivel) {
    return lookup.get(idNivel);
  } 
}
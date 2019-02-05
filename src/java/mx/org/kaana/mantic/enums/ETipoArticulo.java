package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ETipoArticulo {

  PRODUCTO  (1L),
  REFACCION (2L),
  SERVICIO  (3L);

  private Long idTipoArticulo;
	private static final Map<Long, ETipoArticulo> lookup= new HashMap<>();

  static {
    for (ETipoArticulo item: EnumSet.allOf(ETipoArticulo.class)) 
      lookup.put(item.getIdTipoArticulo(), item);    
  }

  private ETipoArticulo(Long idTipoArticulo) {
    this.idTipoArticulo = idTipoArticulo;
  }

	public Long getIdTipoArticulo() {
		return idTipoArticulo;
	}  
	
	public static ETipoArticulo fromIdTipoArticulo(Long idTipoArticulo) {
    return lookup.get(idTipoArticulo);
  } 
}
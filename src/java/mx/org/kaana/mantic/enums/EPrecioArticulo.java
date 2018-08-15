package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EPrecioArticulo {

  MENUDEO       ("MENUDEO"),
  MEDIO_MAYOREO ("MEDIOMAYOREO"),
  MAYOREO       ("MAYOREO");

  private String nombre;
	private static final Map<String, EPrecioArticulo> lookup= new HashMap<>();

  static {
    for (EPrecioArticulo item: EnumSet.allOf(EPrecioArticulo.class)) 
      lookup.put(item.getNombre(), item);    
  }

  private EPrecioArticulo(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }
	
	public static EPrecioArticulo fromNombre(String nombre) {
    return lookup.get(nombre);
  } 
}
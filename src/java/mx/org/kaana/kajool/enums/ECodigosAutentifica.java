package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ECodigosAutentifica {
  
  CORRECTO   ("01", "la autentificación se realizó de forma correcta"),
  INCORRECTO ("02", "La autentificación es incorrecta, verificar usuario y password"),
  ERROR      ("99", "Ocurrió un error inesperado al intentar autentificarse");

  private static final Map<Integer, ECodigosAutentifica> lookup = new HashMap<>();
  private String codigo;
  private String descripcion;

  private ECodigosAutentifica(String codigo, String descripcion) {
    this.codigo     = codigo;
    this.descripcion= descripcion;
  } // ECodigosAutentifica
  
  static {
    for (ECodigosAutentifica item: EnumSet.allOf(ECodigosAutentifica.class)) 
      lookup.put(item.ordinal()+ 1, item);    
  } // static

  public Long getKey(){
		return new Long(this.ordinal() + 1);
	} // getKey

  public String getCodigo() {
    return codigo;
  } // getCodigo

  public String getDescripcion() {
    return descripcion;
  } // getDescripcion
  
  public static ECodigosAutentifica fromOrdinal(Long ordinal) {
    return lookup.get(ordinal.intValue());
  } // fromOrdinal
}

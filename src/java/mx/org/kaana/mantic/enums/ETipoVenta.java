package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 19/05/2018
 * @time 08:43:43 PM
 * @author One Developer 2016 <one.developer@kaana.org.mx>
 */

public enum ETipoVenta {

  MENUDEO      (1L, "menudeo"),
  MEDIO_MAYOREO(2L, "medioMayoreo"),
	MAYOREO      (3L, "mayoreo");

  private Long idTipoVenta;
	private String nombreCampo;
	private static final Map<String, ETipoVenta> lookup= new HashMap<>();
	
	static {
    for (ETipoVenta item: EnumSet.allOf(ETipoVenta.class)) 
      lookup.put(item.getNombreCampo(), item);    
  } // static
	
  private ETipoVenta(Long idTipoVenta, String nombreCampo) {
    this.idTipoVenta= idTipoVenta;
		this.nombreCampo= nombreCampo;
  } // ETipoVenta

  public Long getIdTipoVenta() {
    return idTipoVenta;
  }

	public String getNombreCampo() {
		return nombreCampo;
	}
	
	public static ETipoVenta fromNombreCampo(String nombreCampo) {
    return lookup.get(nombreCampo);
  } 
}
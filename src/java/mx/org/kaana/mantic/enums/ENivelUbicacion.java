package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ENivelUbicacion {

  EMPRESA(1L, "ubicacionEmpresa", "empresa"),
  ALMACEN(2L, "ubicacionAlmacen", "almacen"),
  PISO   (3L, "ubicacionPiso", "piso"),
  CUARTO (4L, "ubicacionCuarto", "cuarto"),
  ANAQUEL(5L, "ubicacionAnaquel", "anaquel"),
  CHAROLA(6L, "ubicacionCharola", "charola");

  private Long idNivelUbicacion;
  private String idXml;
  private String type;
	private static final Map<Long, ENivelUbicacion> lookup= new HashMap<>();

  static {
    for (ENivelUbicacion item: EnumSet.allOf(ENivelUbicacion.class)) 
      lookup.put(item.getIdNivelUbicacion(), item);    
  } // static

  private ENivelUbicacion(Long idNivelUbicacion, String idXml, String type) {
    this.idNivelUbicacion= idNivelUbicacion;
		this.idXml           = idXml;
		this.type            = type;
  }

	public Long getIdNivelUbicacion() {
		return idNivelUbicacion;
	}  

	public String getIdXml() {
		return idXml;
	}	

	public String getType() {
		return type;
	}		
	
	public static ENivelUbicacion fromIdNivel(Long idNivel) {
    return lookup.get(idNivel);
  } 
}

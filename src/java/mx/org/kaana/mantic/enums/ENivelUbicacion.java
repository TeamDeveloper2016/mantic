package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ENivelUbicacion {

  EMPRESA(1L, "ubicacionEmpresa", "empresa", 0L),
  ALMACEN(2L, "ubicacionAlmacen", "almacen", 0L),
  PISO   (3L, "ubicacionPiso", "piso", 1L),
  CUARTO (4L, "ubicacionCuarto", "cuarto", 2L),
  ANAQUEL(5L, "ubicacionAnaquel", "anaquel", 3L),
  CHAROLA(6L, "ubicacionCharola", "charola", 4L);

  private Long idNivelUbicacion;
  private String idXml;
  private String type;
	private Long nivel;
	private static final Map<Long, ENivelUbicacion> lookup= new HashMap<>();

  static {
    for (ENivelUbicacion item: EnumSet.allOf(ENivelUbicacion.class)) 
      lookup.put(item.getIdNivelUbicacion(), item);    
  } // static

  private ENivelUbicacion(Long idNivelUbicacion, String idXml, String type, Long nivel) {
    this.idNivelUbicacion= idNivelUbicacion;
		this.idXml           = idXml;
		this.type            = type;
		this.nivel           = nivel;
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

	public Long getNivel() {
		return nivel;
	}	
	
	public static ENivelUbicacion fromIdNivel(Long idNivel) {
    return lookup.get(idNivel);
  } 
}

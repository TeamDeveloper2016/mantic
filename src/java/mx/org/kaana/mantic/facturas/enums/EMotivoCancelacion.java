package mx.org.kaana.mantic.facturas.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/02/2023
 *@time 08:10:23 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EMotivoCancelacion {
  
  CFDI_EMITIDO_CON_ERRORES_CON_RELACION("01", "Comprobante emitido con errores con relación"),
  CFDI_EMITIDO_CON_ERRORES_SIN_RELACION("02", "Comprobante emitido con errores sin relación"),
  CFDI_NO_SE_LLEVO_ACABO("03", "No se llevó a cabo la operación"),
  CFDI_FACTURA_NOMINATIVA_GLOBAL("04", "Operación nominativa relacionada con una factura global");

  private String idMotivoCancelacion;
  private String descripcion;
	private static final Map<String, EMotivoCancelacion> lookup= new HashMap<>();

  static {
    for (EMotivoCancelacion item: EnumSet.allOf(EMotivoCancelacion.class)) 
      lookup.put(item.getIdMotivoCancelacion(), item);    
  }

 private EMotivoCancelacion(String idMotivoCancelacion, String descripcion) {
    this.idMotivoCancelacion = idMotivoCancelacion;
    this.descripcion = descripcion;
  }

  public String getIdMotivoCancelacion() {
    return idMotivoCancelacion;
  }
	
  public String getDescripcion() {
    return descripcion;
  }
	
	public static EMotivoCancelacion fromIdFicticia(String idMotivoCancelacion) {
    return lookup.get(idMotivoCancelacion);
  }   
  
}

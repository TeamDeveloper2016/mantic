package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 09:51:48 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EPerfiles {
  
  ADMINISTRADOR_ENCUESTA ("Administrador de encuesta", "VistaIndicadoresPerfilesDto", "avanceCapturistas", "Porcentaje de avance", "nacional", "nacional"),
  ADMINISTRADOR          ("Administrador", "VistaIndicadoresPerfilesDto", "avanceCapturistas", "Porcentaje de avance", "nacional", "nacional"),
  RESPONSABLE_ESTATAL    ("Responsable estatal", "VistaIndicadoresPerfilesDto", "avanceCapturistas", "Porcentaje de avance", "estatal", "entidad"),
  CAPTURISTA             ("Capturista", "VistaIndicadoresPerfilesDto", "foliosSinCaptura", "Folios sin captura", "estatal", "usuario"), 
  CONSULTOR              ("Consultor", "VistaIndicadoresPerfilesDto", "avanceCapturistas", "Porcentaje de avance", "nacional", "nacional");

  private static final Map<Integer, EPerfiles> lookup = new HashMap<>();
  private String descripcion;
  private String vista;
  private String idVista;
  private String tituloTabla;
  private String idVistaContadores;
  private String idVistaContadoresMes;

  private EPerfiles(String descripcion, String vista, String idVista, String tituloTabla, String idVistaContadores, String idVistaContadoresMes) {
    this.descripcion         = descripcion;
    this.vista               = vista;
    this.idVista             = idVista;
    this.tituloTabla         = tituloTabla;
    this.idVistaContadores   = idVistaContadores;
    this.idVistaContadoresMes= idVistaContadoresMes;
  }  
    
  static {
    for (EPerfiles item: EnumSet.allOf(EPerfiles.class)) 
      lookup.put(item.ordinal()+ 1, item);    
  } // static

  public Long getKey(){
		return new Long(this.ordinal() + 1);
	}

  public static EPerfiles fromOrdinal(Long ordinal) {
    return lookup.get(ordinal.intValue());
  }

  public String getDescripcion() {
    return descripcion;
  }  

  public String getVista() {
    return vista;
  }

  public String getIdVista() {
    return idVista;
  }  

  public String getTituloTabla() {
    return tituloTabla;
  }  

  public String getIdVistaContadores() {
    return idVistaContadores;
  }

  public String getIdVistaContadoresMes() {
    return idVistaContadoresMes;
  }    
}

package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 30/05/2014
 * @time 03:29:43 PM
 * @author One Developer 2016 <one.developer@kaana.org.mx>
 */

public enum EPeriodosTableros {

  DIA      (1L, "Día"),
  SEMANA   (2L, "Semana"),
  QUINCENA (3L, "Quincena"),
  MES      (4L, "Mes"),
  TRIMESTRE(5L, "Trimestre"),
  SEMESTRE (6L, "Semestre"),
  ANIO     (7L, "Año");

  private Long idTipoPeriodo;
	private String nombre;

	private static final Map<Long, EPeriodosTableros> lookup= new HashMap<>();

  static {
    for (EPeriodosTableros item: EnumSet.allOf(EPeriodosTableros.class)) 
      lookup.put(item.getIdTipoPeriodo(), item);    
  } // static   

	private EPeriodosTableros(Long idTipoPeriodo, String nombre) {
		this.idTipoPeriodo= idTipoPeriodo;
		this.nombre       = nombre;
	}  

  public Long getIdTipoPeriodo() {
    return idTipoPeriodo;
  }

	public String getNombre() {
		return nombre;
	}	
	
	public static EPeriodosTableros fromIdPeriodo(Long idTipoPeriodo) {
    return lookup.get(idTipoPeriodo);
  } // fromNameTablero
}
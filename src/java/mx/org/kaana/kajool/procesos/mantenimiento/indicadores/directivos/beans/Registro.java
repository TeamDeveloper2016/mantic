package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.directivos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.enums.EAmbitos;


/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 13, 2013
 *@time 7:03:50 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Registro implements Serializable{

  private static final long serialVersionUID=-2425358542445737151L;

  private Long idKey;
  private String descripcion;
  private EAmbitos ambito;
  private Long total;
  private List<Columna> columnas;
  private Double avance;

  public Registro(String descripcion, EAmbitos ambito){
    this(-1L, descripcion, ambito, 0L, new ArrayList<Columna>(), 0.0D);
  }

  public Registro(Long idKey, String descripcion, EAmbitos ambito, Long total, List<Columna> columnas, Double avance) {
    this.idKey= idKey;
    this.descripcion= descripcion;
    this.ambito= ambito;
    this.total= total;
    this.columnas= columnas;
    this.avance= avance;
  }

  public Long getIdKey() {
    return idKey;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public EAmbitos getAmbito() {
    return ambito;
  }

  public Long getTotal() {
    return total;
  }

  public List<Columna> getColumnas() {
    return columnas;
  }

  public Double getAvance() {
    return avance;
  }

}

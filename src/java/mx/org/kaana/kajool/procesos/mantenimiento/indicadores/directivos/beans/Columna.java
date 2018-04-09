package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.directivos.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 13, 2013
 *@time 7:03:26 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Columna implements Serializable {

  private static final long serialVersionUID=703431313413104627L;

  private String etiqueta;
  private Long total;
  private Double porcentaje;

  public Columna(String etiqueta) {
    this(etiqueta, 0L, 0.0D);
  }

  public Columna(String etiqueta, Long total, Double porcentaje) {
    this.etiqueta=etiqueta;
    this.total=total;
    this.porcentaje=porcentaje;
  }

  public String getEtiqueta() {
    return etiqueta;
  }

  public Long getTotal() {
    return total;
  }

  public Double getPorcentaje() {
    return porcentaje;
  }

  public void setPorcentaje(Double porcentaje) {
    this.porcentaje= porcentaje;
  }

  @Override
  public int hashCode() {
    int hash=7;
    hash=97*hash+(this.etiqueta!=null ? this.etiqueta.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj==null) {
      return false;
    }
    if (getClass()!=obj.getClass()) {
      return false;
    }
    final Columna other=(Columna) obj;
    if ((this.etiqueta==null) ? (other.etiqueta!=null) : !this.etiqueta.equals(other.etiqueta)) {
      return false;
    }
    return true;

  }

}

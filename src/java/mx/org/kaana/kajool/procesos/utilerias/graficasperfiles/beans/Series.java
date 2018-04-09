package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 01:13:17 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Series implements Serializable{

  private static final long serialVersionUID= 1667123030644809739L;
  private Totales[] data;
  private DataLabels dataLabels;

  public Series(Totales[] data) {
    this.data = data;
  }

  public Series(DataLabels dataLabels) {
    this.dataLabels = dataLabels;
  } 
  
  public Totales[] getData() {
    return data;
  }

  public void setData(Totales[] data) {
    this.data = data;
  }

  public DataLabels getDataLabels() {
    return dataLabels;
  }

  public void setDataLabels(DataLabels dataLabels) {
    this.dataLabels = dataLabels;
  }
}

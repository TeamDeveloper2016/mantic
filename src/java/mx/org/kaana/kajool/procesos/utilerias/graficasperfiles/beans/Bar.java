package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 01:04:06 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Bar implements Serializable{

  private static final long serialVersionUID= 7398122805900870514L;
  private DataLabels dataLabels;

  public Bar(DataLabels dataLabels) {
    this.dataLabels = dataLabels;
  }

  public DataLabels getDataLabels() {
    return dataLabels;
  }

  public void setDataLabels(DataLabels dataLabels) {
    this.dataLabels = dataLabels;
  }
}

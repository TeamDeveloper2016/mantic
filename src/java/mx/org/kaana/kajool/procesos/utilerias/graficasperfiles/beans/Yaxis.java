package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 12:57:47 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Yaxis implements Serializable{

  private static final long serialVersionUID= 4751503994629015011L;
  private Integer min;
  private Title title;
  private Labels labels;

  public Yaxis(Integer min, Title title, Labels labels) {
    this.min   = min;
    this.title = title;
    this.labels= labels;
  }

  public Integer getMin() {
    return min;
  }

  public void setMin(Integer min) {
    this.min = min;
  }

  public Title getTitle() {
    return title;
  }

  public void setTitle(Title title) {
    this.title = title;
  }

  public Labels getLabels() {
    return labels;
  }

  public void setLabels(Labels labels) {
    this.labels = labels;
  }
}

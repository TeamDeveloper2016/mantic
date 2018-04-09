package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 01:03:57 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class PlotOptions implements Serializable{

  private static final long serialVersionUID= -8896159798715021914L;
  private Bar bar;
  private Pie pie;
  private Series series;

  public PlotOptions(Bar bar) {
    this.bar = bar;
  }
  
  public PlotOptions(Bar bar, Series series) {
    this.bar   = bar;
    this.series= series;
  }
  
  public PlotOptions(Pie pie) {
    this.pie = pie;
  }

  public Bar getBar() {
    return bar;
  }

  public void setBar(Bar bar) {
    this.bar= bar;
  }

  public Pie getPie() {
    return pie;
  }

  public void setPie(Pie pie) {
    this.pie  = pie;
  }  

  public Series getSeries() {
    return series;
  }

  public void setSeries(Series series) {
    this.series = series;
  }
}

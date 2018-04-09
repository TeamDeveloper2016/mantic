package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 09:51:48 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class HighchartsPie implements Serializable{
  
  private static final long serialVersionUID = -7580157770362862761L;
  private Chart chart;
  private Title title;  
  private Tooltip tooltip;
  private PlotOptions plotOptions;  
  private TotalesPie[] series;

  public HighchartsPie(Chart chart, Title title,  Tooltip tooltip, PlotOptions plotOptions, TotalesPie[] series) {        
    this.chart      = chart;
    this.title      = title;    
    this.tooltip    = tooltip;
    this.plotOptions= plotOptions;    
    this.series     = series;
  }

  public Chart getChart() {
    return chart;
  }

  public void setChart(Chart chart) {
    this.chart = chart;
  }

  public Title getTitle() {
    return title;
  }

  public void setTitle(Title title) {
    this.title = title;
  }  

  public Tooltip getTooltip() {
    return tooltip;
  }

  public void setTooltip(Tooltip tooltip) {
    this.tooltip = tooltip;
  }

  public PlotOptions getPlotOptions() {
    return plotOptions;
  }

  public void setPlotOptions(PlotOptions plotOptions) {
    this.plotOptions = plotOptions;
  }
  
  public TotalesPie[] getSeries() {
    return series;
  }

  public void setSeries(TotalesPie[] series) {
    this.series= series;
  }    
}

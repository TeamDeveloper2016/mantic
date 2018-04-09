package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;
import mx.org.kaana.libs.json.Decoder;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 01:18:14 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Highcharts implements Serializable{

  private static final long serialVersionUID= 2319721400367222634L;  
  private Chart chart;
  private Title title;
  private Xaxis xAxis;
  private Yaxis yAxis;
  private Tooltip tooltip;
  private PlotOptions plotOptions;
  private Legend legend;
  private Credits credits;
  private Totales[] series;
  
  public Highcharts(Chart chart, Title title, Xaxis xAxis, Yaxis yAxis, Tooltip tooltip, PlotOptions plotOptions, Legend legend, Credits credits, Totales[] series) {    
    this.chart      = chart;
    this.title      = title;
    this.xAxis      = xAxis;
    this.yAxis      = yAxis;
    this.tooltip    = tooltip;
    this.plotOptions= plotOptions;
    this.legend     = legend;
    this.credits    = credits;
    this.series     = series;
  } // Highcharts
  
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

  public Xaxis getxAxis() {
    return xAxis;
  }

  public void setxAxis(Xaxis xAxis) {
    this.xAxis = xAxis;
  }

  public Yaxis getyAxis() {
    return yAxis;
  }

  public void setyAxis(Yaxis yAxis) {
    this.yAxis = yAxis;
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

  public Legend getLegend() {
    return legend;
  }

  public void setLegend(Legend legend) {
    this.legend = legend;
  }

  public Credits getCredits() {
    return credits;
  }

  public void setCredits(Credits credits) {
    this.credits = credits;
  }

  public Totales[] getSeries() {
    return series;
  }

  public void setSeries(Totales[] series) {
    this.series = series;
  }
  
  public static void main (String ... args){
    try {
      String[] categories= {"DRN", "DROC", "DRNO", "DRSE", "DRNE", "DRS", "DROR", "DRCN", "DRCS", "DRC"};
      Long[] data= {893L, 470L, 365L, 797L, 600L, 750L, 330L, 657L, 874L, 650L};
      Totales[] totales= {new Totales("Con información", data)};
      Highcharts high= new Highcharts(
              new Chart("column"),
              new Title("Con informacion"),
              new Xaxis(categories, new Title("Dir regionales")),
              new Yaxis(0, new Title("Con informacion"), new Labels("justify")),
              new Tooltip("viviendas"),
              new PlotOptions(new Bar(new DataLabels(true))),
              new Legend("vertical", "right", "top", -40, 80, true, 1, "#FFFFFF", true),
              new Credits(false),
              totales);
      System.out.println(Decoder.toJson(high));
    } // try
    catch (Exception e) {
      System.out.println(e);
    } // catch
  }
}

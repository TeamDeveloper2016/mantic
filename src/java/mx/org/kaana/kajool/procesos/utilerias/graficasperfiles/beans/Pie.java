package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 09:51:48 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Pie implements Serializable{
  
  private static final long serialVersionUID = 7651757117066968956L;
  private boolean allowPointSelect;
  private String cursor;
  private DataLabels dataLabels;
  private boolean showInLegend;

  public Pie(boolean allowPointSelect, String cursor, DataLabels dataLabels, boolean showInLegend) {
    this.allowPointSelect= allowPointSelect;
    this.cursor          = cursor;
    this.dataLabels      = dataLabels;
    this.showInLegend    = showInLegend;
  }    

  public boolean isAllowPointSelect() {
    return allowPointSelect;
  }

  public void setAllowPointSelect(boolean allowPointSelect) {
    this.allowPointSelect = allowPointSelect;
  }

  public String getCursor() {
    return cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }

  public DataLabels getDataLabels() {
    return dataLabels;
  }

  public void setDataLabels(DataLabels dataLabels) {
    this.dataLabels = dataLabels;
  } 

  public boolean isShowInLegend() {
    return showInLegend;
  }

  public void setShowInLegend(boolean showInLegend) {
    this.showInLegend= showInLegend;
  }
}

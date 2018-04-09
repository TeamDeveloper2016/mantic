package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 01:02:43 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Tooltip implements Serializable{

  private static final long serialVersionUID = 8284175561643910692L;
  private String valueSuffix;
  private String pointFormat;

  public Tooltip(String valueSuffix) {
    this.valueSuffix = valueSuffix;
  }

  public Tooltip(String valueSuffix, String pointFormat) {
    this.valueSuffix = valueSuffix;
    this.pointFormat = pointFormat;
  }
  
  public String getValueSuffix() {
    return valueSuffix;
  }

  public void setValueSuffix(String valueSuffix) {
    this.valueSuffix = valueSuffix;
  }

  public String getPointFormat() {
    return pointFormat;
  }

  public void setPointFormat(String pointFormat) {
    this.pointFormat = pointFormat;
  }    
}

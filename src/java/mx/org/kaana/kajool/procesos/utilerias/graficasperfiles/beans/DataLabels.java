package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 01:05:00 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DataLabels implements Serializable{

  private static final long serialVersionUID = -3566188810720869850L;
  private boolean enabled;
  private String format;

  public DataLabels(boolean enabled) {
    this.enabled = enabled;
  }
  
  public DataLabels(boolean enabled, String format) {
    this.enabled = enabled;
    this.format  = format;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}

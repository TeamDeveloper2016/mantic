package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 01:11:28 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Credits implements Serializable{

  private static final long serialVersionUID = 6444499413997990122L;
  private boolean enabled;

  public Credits(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}

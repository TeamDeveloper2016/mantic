package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 12:59:47 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Labels implements Serializable{

  private static final long serialVersionUID= -6897658823287866355L;
  private String overflow;

  public Labels(String overflow) {
    this.overflow = overflow;
  }

  public String getOverflow() {
    return overflow;
  }

  public void setOverflow(String overflow) {
    this.overflow = overflow;
  }
}

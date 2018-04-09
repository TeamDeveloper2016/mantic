package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 12:53:16 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Title implements Serializable{

  private static final long serialVersionUID = 2043146388499750340L;
  private String text;

  public Title(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}

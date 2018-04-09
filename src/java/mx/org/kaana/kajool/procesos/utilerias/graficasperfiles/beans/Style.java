package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 12:33:24 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Style implements Serializable{

  private static final long serialVersionUID= -1740009443217774127L;
  private String fontFamily;

  public Style(String fontFamily) {
    this.fontFamily = fontFamily;
  }

  public String getFontFamily() {
    return fontFamily;
  }

  public void setFontFamily(String fontFamily) {
    this.fontFamily = fontFamily;
  }
}

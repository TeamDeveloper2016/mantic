package mx.org.kaana.libs.wassenger;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/06/2021
 *@time 03:02:10 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Medias implements Serializable {

  private static final long serialVersionUID = 6657886952825235420L;

  private String format;

  public Medias() {
    this("native");
  }

  public Medias(String format) {
    this.format = format;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  @Override
  public String toString() {
    return "Medias{" + "format=" + format + '}';
  }
  
}

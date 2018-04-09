package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 09:51:48 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DetailData implements Serializable{
  
  private static final long serialVersionUID = -646730076856495422L;
  private String name;
  private Long y;

  public DetailData(String name, Long y) {
    this.name= name;
    this.y   = y;
  } // DetailData

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getY() {
    return y;
  }

  public void setY(Long y) {
    this.y = y;
  }    
}

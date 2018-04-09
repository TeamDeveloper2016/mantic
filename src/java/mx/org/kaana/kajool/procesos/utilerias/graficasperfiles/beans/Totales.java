package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 01:14:29 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Totales implements Serializable{

  private static final long serialVersionUID= -2063440993378017292L;
  private String name;
  private Long[] data;

  public Totales(String name, Long[] data) {
    this.name = name;
    this.data = data;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long[] getData() {
    return data;
  }

  public void setData(Long[] data) {
    this.data = data;
  }
}

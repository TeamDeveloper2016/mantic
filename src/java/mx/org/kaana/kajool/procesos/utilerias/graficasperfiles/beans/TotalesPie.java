package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 09:51:48 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class TotalesPie {

  private String name;
  private DetailData[] data;

  public TotalesPie(String name, DetailData[] data) {
    this.name= name;
    this.data= data;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name= name;
  }

  public DetailData[] getData() {
    return data;
  }

  public void setData(DetailData[] data) {
    this.data= data;
  }    
}

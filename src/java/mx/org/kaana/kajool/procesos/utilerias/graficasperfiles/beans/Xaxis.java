package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 12:55:52 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Xaxis implements Serializable{

  private static final long serialVersionUID= 6480262969582723492L;
  private String[] categories;
  private Title title;

  public Xaxis(String[] categories, Title title) {
    this.categories = categories;
    this.title = title;
  }

  public String[] getCategories() {
    return categories;
  }

  public void setCategories(String[] categories) {
    this.categories = categories;
  }

  public Title getTitle() {
    return title;
  }

  public void setTitle(Title title) {
    this.title = title;
  }
}

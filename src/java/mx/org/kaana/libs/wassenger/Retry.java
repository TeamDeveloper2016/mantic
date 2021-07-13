package mx.org.kaana.libs.wassenger;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/06/2021
 *@time 02:58:38 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Retry implements Serializable {

  private static final long serialVersionUID = 494127037061128142L;

  private Integer count;

  public Retry() {
    this(0);
  }

  public Retry(Integer count) {
    this.count = count;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return "Retry{" + "count=" + count + '}';
  }

}

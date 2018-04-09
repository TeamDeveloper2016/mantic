
package mx.org.kaana.kajool.enums;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 27/05/2015
 *@time 06:19:24 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EFiltersWith {

  START_WITH(" like '{value}%'"), END_WITH(" like '%{value}'"), CONTAINS(" like '%{value}%'");

  private final String format;

  private EFiltersWith(String format) {
    this.format= format;
  }

  public String getFormat() {
    return format;
  }

}

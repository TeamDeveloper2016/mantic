
package mx.org.kaana.kajool.enums;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 9/04/2014
 *@time 10:43:40 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EDevice {

  MOBILE("taphold"), PC(""), UNKNOWN("dblclick");

  private String event;

  private EDevice(String event) {
    this.event = event;
  }

  public String getEvent() {
    return event;
  }

}

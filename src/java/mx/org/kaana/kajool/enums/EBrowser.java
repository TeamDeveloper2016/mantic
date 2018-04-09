
package mx.org.kaana.kajool.enums;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 9/04/2014
 *@time 10:39:07 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EBrowser {

  ANDROID(".*\\b(Android)\\b.*", EDevice.MOBILE), BLACK_BERRY(".*\\b(BlackBerry)\\b.*", EDevice.MOBILE), IOS(".*\\b(iPhone|iPad|iPod)\\b.*", EDevice.MOBILE), OPERA_MINI(".*\\b(Opera Mini)\\b.*", EDevice.MOBILE), WINDOWS(".*\\b(IEMobile)\\b.*", EDevice.MOBILE),
  MSIE(".*\\b(MSIE)\\b.*", EDevice.PC), FIREFOX(".*\\b(Firefox)\\b.*", EDevice.PC), CHROME(".*\\b(Chrome)\\b.*", EDevice.PC), OPERA(".*\\b(Opera)\\b.*", EDevice.PC), SAFARI(".*\\b(Safari)\\b.*", EDevice.PC), UNKNOWN("Unknown", EDevice.UNKNOWN);

  private String pattern;
  private EDevice device;

  private EBrowser(String pattern, EDevice device) {
    this.pattern = pattern;
    this.device = device;
  }

  public String getPattern() {
    return pattern;
  }

  public EDevice getDevice() {
    return device;
  }

  public static Boolean isMobile(EBrowser browser) {
    return browser.getDevice().equals(EDevice.MOBILE);
  }

  public Boolean isMobile() {
    return isMobile(this);
  }

  public static Boolean isMobile(String name) {
    return isMobile(detect(name));
  }

  public static EBrowser detect(String name) {
    EBrowser regresar= EBrowser.UNKNOWN;
    for (EBrowser item: values()) {
      if(name.matches(item.getPattern())) {
        regresar= item;
        break;
      } // if
    } // for
    return regresar;
  }

}

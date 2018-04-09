package mx.org.kaana.kajool.servicios.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class Memory implements Serializable{
  
  private static Object mutex;
  private static Memory instance;
  private static final long serialVersionUID = -6860502299580308210L;
  private Map<String, String> map;
  
  static {
    mutex=new Object();
  }

  public Memory() {
    this.map = new HashMap();
  }
  
  public static Memory getInstance() {
    synchronized (mutex) {
      if (instance==null) {
        instance=new Memory();
      }
    } // if
    return instance;
  }

  public Map<String, String> getMap() {
    return map;
  }
    
}

package mx.org.kaana.libs.pagina;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.reflection.Methods;

/**
 *@company KAANA
 *@project  (Sistema de Seguimiento y Control de Proyectos Estadisticos)
 *@date 20-feb-2014
 *@time 15:20:21
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class IBaseAttribute implements Serializable {

  private static final long serialVersionUID = -5324353121363296378L;

  protected Map<String, Object> attrs;

  public IBaseAttribute() {
    this.attrs= new HashMap<>();
  }

  public Map<String, Object> getAttrs() {
    return attrs;
  }

  public void setAttrs(Map<String, Object> attrs) {
    this.attrs = attrs;
  }

  protected abstract void init();

  protected Object toAttr(String name, Object value) {
    if(this.attrs.containsKey(name))
      return this.attrs.get(name);
    else
      return value;
  }

  protected Object toAttr(String name) {
    return toAttr(name, "");
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      this.attrs.clear();
      for (Object item: this.attrs.values())
        Methods.clean(item);
      this.attrs= null;
    } // try
    finally {
      super.finalize();
    } // finally
  }

}

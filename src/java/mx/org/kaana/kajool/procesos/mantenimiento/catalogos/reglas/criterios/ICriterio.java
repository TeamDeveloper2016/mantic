package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios;

import java.util.List;
import javax.faces.component.UIComponentBase;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/09/2015
 *@time 04:24:18 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface ICriterio {
  public List<UIComponentBase> create(String expression);
  public String toSql();
}

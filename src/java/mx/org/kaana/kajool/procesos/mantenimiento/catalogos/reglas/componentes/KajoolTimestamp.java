package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import java.sql.Timestamp;
import javax.el.ValueExpression;
import javax.faces.convert.Converter;
import mx.org.kaana.libs.pagina.convertidores.Registro;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 10:40:44 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class KajoolTimestamp extends KajoolCalendar {

  public KajoolTimestamp(){
    super();
  }

  public KajoolTimestamp(String id) {
    super(id);
  }

  public KajoolTimestamp(String id, ValueExpression value) {
    this(id, value, "button", true, "es");
  }

  public KajoolTimestamp(String id, ValueExpression value, String showOn, boolean readOnlyInput, String locale) {
    this(id, value, showOn, "dd/MM/yyyy hh:mm:ss", readOnlyInput, locale, new Registro());
  }

  public KajoolTimestamp(String id, ValueExpression value, String showOn, String pattern, boolean readOnlyInput, String locale, Converter converter) {
    super(id, value, showOn, pattern, readOnlyInput, locale, converter);
  }
}

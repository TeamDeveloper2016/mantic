package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import java.sql.Time;
import javax.el.ValueExpression;
import javax.faces.convert.Converter;
import mx.org.kaana.libs.pagina.convertidores.Hora;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 10:33:31 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class KajoolTime extends KajoolCalendar {

  public KajoolTime(){
    super();
  }

  public KajoolTime(String id) {
    super(id);
  }

  public KajoolTime(String id, ValueExpression value) {
    this(id, value, "button", true, "es");
  }

  public KajoolTime(String id, ValueExpression value, String showOn, boolean readOnlyInput, String locale) {
    this(id, value, showOn, "HH:mm:ss", readOnlyInput, locale, new Hora());
  }

  public KajoolTime(String id, ValueExpression value, String showOn, String pattern, boolean readOnlyInput, String locale, Converter converter) {
    super(id, value, showOn, pattern, readOnlyInput, locale, converter);
  }
}

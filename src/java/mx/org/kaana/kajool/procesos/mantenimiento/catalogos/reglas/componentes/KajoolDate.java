package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.el.ValueExpression;
import javax.faces.convert.Converter;
import mx.org.kaana.libs.pagina.convertidores.Fecha;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 10:21:36 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class KajoolDate extends KajoolCalendar {

  public KajoolDate(){
    super();
  }

  public KajoolDate(String id) {
    super(id);
  }

  public KajoolDate(String id, ValueExpression value) {
    this(id, value, "button", true, "es");
  }

  public KajoolDate(String id, ValueExpression value, String showOn, boolean readOnlyInput, String locale) {
    this(id, value, showOn, "dd/MM/yyyy", readOnlyInput, locale, new Fecha());
  }

  public KajoolDate(String id, ValueExpression value, String showOn, String pattern, boolean readOnlyInput, String locale, Converter converter) {
    super(id, value, showOn, pattern, readOnlyInput, locale, converter);
  }

}

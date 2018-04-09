package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.UUID;
import javax.el.ValueExpression;
import javax.faces.convert.Converter;
import mx.org.kaana.libs.pagina.convertidores.Calendario;
import org.primefaces.component.calendar.Calendar;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 10:45:43 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolCalendar extends Calendar implements IComponent {

  private static String render = "org.primefaces.component.CalendarRenderer";
  private String id;
  private ValueExpression value;
  private String showOn;
  private String pattern;
  private boolean readOnlyInput;
  private String locale;
  private Converter converter;

  public KajoolCalendar() {
    this(UUID.randomUUID().toString());
  }

  public KajoolCalendar(String id) {
    this(id, null);
  }

  public KajoolCalendar(String id, ValueExpression value) {
    this(id, value, "button", "dd/MM/yyyy", true, "es", new Calendario());
  }

  public KajoolCalendar(String id, ValueExpression value, String showOn, String pattern, boolean readOnlyInput, String locale, Converter converter) {
    this.id = id;
    this.value = value;
    this.showOn = showOn;
    this.pattern = pattern;
    this.readOnlyInput = readOnlyInput;
    this.locale = locale;
    this.converter = converter;
  }


  @Override
  public UIComponentBase create() {
    Calendar regresar = (Calendar) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), Calendar.COMPONENT_TYPE, this.render);
    regresar.setId(this.id);
    regresar.setValueExpression("value", this.value);
    regresar.setShowOn(this.showOn);
    regresar.setPattern(this.pattern);
    regresar.setReadonlyInput(this.readOnlyInput);
    regresar.setLocale(this.locale);
    regresar.setConverter(this.converter);
    return regresar;
  }
}

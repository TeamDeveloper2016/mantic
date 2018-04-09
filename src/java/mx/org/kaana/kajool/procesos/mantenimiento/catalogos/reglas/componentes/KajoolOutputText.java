package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.UUID;
import javax.el.ValueExpression;
import javax.faces.component.html.HtmlOutputText;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 10:45:43 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolOutputText extends HtmlOutputText implements IComponent {

  private String id;
  private Object value;

  public KajoolOutputText() {
    this(UUID.randomUUID().toString());
  }

  public KajoolOutputText(String id) {
    this(id, "");
  }

  public KajoolOutputText(String id, Object value) {
    this.id   = id;
    this.value= value;
  }

  @Override
  public UIComponentBase create() {
    HtmlOutputText regresar = (HtmlOutputText) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
    regresar.setId(this.id);
    if(value instanceof ValueExpression)
      regresar.setValueExpression("value", (ValueExpression)this.value);
    else
      regresar.setValue(this.value);
    regresar.setStyleClass("janal-text-one-line");
    return regresar;
  }
}

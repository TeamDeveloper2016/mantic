package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.UUID;
import javax.el.ValueExpression;
import javax.faces.convert.Converter;
import org.primefaces.component.inputtext.InputText;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 10:45:43 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolInputText extends InputText implements IComponent {

  private static String render = "org.primefaces.component.InputTextRenderer";
  private String id;
  private ValueExpression value;
  private Converter converter;

  public KajoolInputText() {
    this(UUID.randomUUID().toString());
  }

  public KajoolInputText(String id) {
    this(id, null);
  }

  public KajoolInputText(String id, ValueExpression value) {
    this(id, value, null);
  }

  public KajoolInputText(String id, ValueExpression value, Converter converter) {
    this.id       = id;
    this.value    = value;
    this.converter= converter;
  }

  @Override
  public UIComponentBase create() {
    InputText regresar = (InputText) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), InputText.COMPONENT_TYPE, this.render);
    regresar.setId(this.id);
    regresar.setValueExpression("value", this.value);
    regresar.setConverter(this.converter);
    return regresar;
  }
}

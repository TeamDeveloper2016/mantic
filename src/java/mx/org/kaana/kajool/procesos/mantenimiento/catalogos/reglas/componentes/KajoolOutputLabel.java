package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.UUID;
import javax.el.ValueExpression;
import mx.org.kaana.libs.formato.Cadena;
import org.primefaces.component.outputlabel.OutputLabel;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 10:45:43 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolOutputLabel extends OutputLabel implements IComponent {

  private static String render = "org.primefaces.component.OutputLabelRenderer";
  private String id;
  private Object value;
  private String _for;

  public KajoolOutputLabel() {
    this(UUID.randomUUID().toString());
  }

  public KajoolOutputLabel(String id) {
    this(id, "");
  }

  public KajoolOutputLabel(String id, Object value) {
    this(id, value, "");
  }

  public KajoolOutputLabel(String id, Object value, String _for) {
    this.id   = id;
    this.value= value;
    this._for = _for;
  }

  @Override
  public UIComponentBase create() {
    OutputLabel regresar = (OutputLabel) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), OutputLabel.COMPONENT_TYPE, this.render);
    regresar.setId(this.id);
    if(value instanceof ValueExpression)
      regresar.setValueExpression("value", (ValueExpression)this.value);
    else
      regresar.setValue(this.value);
    if(!Cadena.isVacio(this._for))
      regresar.setFor(this._for);
    regresar.setStyleClass("janal-text-one-line");
    return regresar;
  }
}

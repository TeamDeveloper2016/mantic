package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.UUID;
import javax.el.MethodExpression;
import org.primefaces.component.commandbutton.CommandButton;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 11:50:43 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolCommandButton extends CommandButton implements IComponent {

  private static final String RENDERER = "org.primefaces.component.CommandButtonRenderer";
  private String id;
  private String value;
  private String icon;

  public KajoolCommandButton() {
    this(UUID.randomUUID().toString());
  }

  public KajoolCommandButton(String id) {
    this(id, "");
  }

  public KajoolCommandButton(String id, String value) {
    this(id, value, "fa fa-search");
  }

  public KajoolCommandButton(String id, String value, String icon) {
    this.id   = id;
    this.value= value;
    this.icon = icon;
  }

  @Override
  public UIComponentBase create() {
    CommandButton regresar = (CommandButton) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), CommandButton.COMPONENT_TYPE, RENDERER);
    regresar.setId(this.id);
    regresar.setValue(this.value);
    regresar.setIcon(this.icon);
    return regresar;
  }

  public static MethodExpression createMethodExpression(String expression, Class<?> returnType, Class<?>... parameterTypes) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    return facesContext.getApplication().getExpressionFactory().createMethodExpression(facesContext.getELContext(), expression, returnType, parameterTypes);
  }

}

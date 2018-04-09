package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.UUID;
import javax.el.ValueExpression;
import javax.faces.component.html.HtmlOutputText;
import org.primefaces.component.spacer.Spacer;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 10:45:43 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolSpacer extends Spacer implements IComponent {

  private static String render = "org.primefaces.component.SpacerRenderer";

  private String width;
  private String height;

  public KajoolSpacer() {
    this("1px");
  }

  public KajoolSpacer(String width) {
    this(width, width);
  }

  public KajoolSpacer(String width, String height) {
    this.width = width;
    this.height= height;
  }

  @Override
  public UIComponentBase create() {
    Spacer regresar = (Spacer) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), Spacer.COMPONENT_TYPE, this.render);
    regresar.setWidth(this.width);
    regresar.setHeight(this.height);
    return regresar;
  }
}

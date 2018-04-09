package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolCommandButton;
import org.primefaces.component.commandbutton.CommandButton;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/10/2015
 * @time 08:08:40 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldForaneoDialogo extends FieldForaneo implements Serializable, ICriterio {

  private static final long serialVersionUID = -5574245914290725353L;

  public FieldForaneoDialogo(String nombre, String titulo, String foraneo, Map<String, Object> params) throws Exception {
    super(nombre, titulo, foraneo, params);
  }

  @Override
  public List<UIComponentBase> create(String value) {
    List<UIComponentBase> regresar= super.create(value, true);
    CommandButton component= (CommandButton) (new KajoolCommandButton("b".concat(this.getId()), "Buscar", "fa fa-history").create());
    component.setOncomplete("PF('wbuscar').show();");
    component.setProcess("@this ".concat(this.getId()));
    component.setUpdate("buscar");
    String method= value.substring(0, value.indexOf(".")).concat(".doBuscar(%s)}");
    MethodExpression expression =  KajoolCommandButton.createMethodExpression(String.format(method, value.replace("#{", "").replace(".value}", "")), null, Criterio.class);
    component.setActionExpression(expression);
    regresar.set(regresar.size()- 1, component);
    return regresar;
  }

}

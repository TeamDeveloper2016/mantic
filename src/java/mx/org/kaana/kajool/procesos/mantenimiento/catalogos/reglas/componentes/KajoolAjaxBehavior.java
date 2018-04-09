package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;



/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 15/09/2015
 *@time 10:18:30 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class KajoolAjaxBehavior extends AjaxBehavior {
  
  private AjaxBehavior comportamiento;
  private MethodExpression ex;
  private String update;
  private String process;

  public KajoolAjaxBehavior() {
    this(null,"@form","@this");
  }

  public KajoolAjaxBehavior(String metodo, String update, String process) {
    this.comportamiento= (AjaxBehavior) FacesContext.getCurrentInstance().getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
    this.ex = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), metodo, null, new Class[]{});
    this.update= update;
    this.process= process;
  }

  public AjaxBehavior create() {
    this.comportamiento.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(this.ex, this.ex));
    this.comportamiento.setUpdate(this.update);
    this.comportamiento.setProcess(this.process);
    return this.comportamiento;
  }
}

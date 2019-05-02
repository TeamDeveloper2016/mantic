package mx.org.kaana.mantic.evaluaciones.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.evaluaciones.beans.Current;
import mx.org.kaana.mantic.evaluaciones.reglas.ControlEvaluation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/05/2019
 *@time 09:56:30 AM 
 *@author Team Developer 2016 [team.developer@kaana.org.mx]
 */

@Named(value = "manticEvaluacionesControl")
@ViewScoped
public class Control extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID= -3494896842851695177L;
	private static final Log LOG              = LogFactory.getLog(Control.class);

	private ControlEvaluation instance;
	private Current current;

	public Current getCurrent() {
		return current;
	}

	public void setCurrent(Current current) {
		this.current=current;
	}

  @PostConstruct
  @Override
  protected void init() {
    try {    	      
			this.instance= (ControlEvaluation)JsfBase.getApplication().getAttribute("janalContolQuestion");
			if(this.instance== null) {
				this.instance= new ControlEvaluation();
				JsfBase.getApplication().setAttribute("janalContolQuestion", this.instance);
			} // if	
			this.current= this.instance.add("FEGEM", 10);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init		    


  public void doUpdateSwitch() {
	  this.current.setControl(Boolean.TRUE);
	}	
	
}

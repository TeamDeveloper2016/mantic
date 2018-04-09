package mx.org.kaana.kajool.procesos.plantillas.formularios.backing;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 5/06/2014
 *@time 05:28:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import mx.org.kaana.kajool.procesos.plantillas.beans.Persona;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolPlantillasFormulariosCaptura")
@ViewScoped
public class Captura implements Serializable {

  private static final Log LOG = LogFactory.getLog(Captura.class);

  private List<Persona> personas;

  public List<Persona> getPersonas() {
    return personas;
  }

  @PostConstruct
	private void init() {
    this.personas= new ArrayList<>();
    this.personas.add(new Persona()); this.personas.get(this.personas.size()-1).setNombres("Alex");
    this.personas.add(new Persona()); this.personas.get(this.personas.size()-1).setNombres("Yany");
    this.personas.add(new Persona()); this.personas.get(this.personas.size()-1).setNombres("Yare");
    this.personas.add(new Persona()); this.personas.get(this.personas.size()-1).setNombres("Axel");
	}

  public String getContext() {
		String root           = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
		StringBuilder regresar= new StringBuilder("");
		for (int x=0; x<root.length(); x++) {
		  regresar.append((int)root.charAt(x));
			if(x!= root.length()- 1)
    		regresar.append(",");
		} // for x
		return regresar.toString();
  }

}

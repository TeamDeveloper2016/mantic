package mx.org.kaana.mantic.evaluaciones.reglas;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.mantic.evaluaciones.beans.Current;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/05/2019
 *@time 02:04:43 PM 
 *@author Team Developer 2016 [team.developer@kaana.org.mx]
 */

public class ControlEvaluation {

	private static final Log LOG=LogFactory.getLog(ControlEvaluation.class);
	
	private Map<String, Current> control;

	public ControlEvaluation() {
		this.control= new HashMap<>();
	}

	public Map<String, Current> getControl() {
		return Collections.unmodifiableMap(control);
	}
	
	public Current add(String name, Integer top) {
		Current regresar= null;
		if(!Cadena.isVacio(name) && !this.control.containsKey(name)) {
			regresar= new Current(top);
		  this.control.put(name, regresar);
		} // if	
		else {
      LOG.warn("EL NOMBRE DE LA EVALUCACION YA FUE AGREGADO A LA LISTA ! ["+ name+ "]");
			regresar= this.control.get(name);
		} // if	
		return regresar;
	}
	
}

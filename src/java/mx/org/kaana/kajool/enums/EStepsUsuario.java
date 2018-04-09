package mx.org.kaana.kajool.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/09/2015
 *@time 11:07:47 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EStepsUsuario {

	BUSQUEDA,
  VALIDACION,
  VISTA_PREVIA,
  GUARDAR;
	
	private static final Map<Integer, EStepsUsuario>lookUp= new HashMap<>();
	
	static{
		for(EStepsUsuario step: EStepsUsuario.values())
			lookUp.put(step.getKey(), step);
	}
	
	public Integer getKey(){
		return new Integer(this.ordinal());
	}
	
	public static EStepsUsuario getStep(Integer step){
		return lookUp.get(step);
	}
}

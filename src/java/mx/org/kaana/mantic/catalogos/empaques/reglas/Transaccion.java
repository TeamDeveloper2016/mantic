package mx.org.kaana.mantic.catalogos.empaques.reglas;

import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {			
			switch(accion){
				case AGREGAR:			
					break;
				case MODIFICAR:				
					break;								
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(e.getMessage());
		} // catch		
		return regresar;
	} // ejecutar	
}

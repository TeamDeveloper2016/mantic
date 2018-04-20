
package mx.org.kaana.mantic.catalogos.articulos.reglas;

import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.catalogos.articulos.bean.RegistroArticulo;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private RegistroArticulo articulo;	

	public Transaccion(RegistroArticulo articulo) {
		this.articulo     = articulo;		
	} // Transaccion

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {
			switch(accion){
				case PROCESAR:
					regresar= procesarArticulo(sesion);
					break;
				case MODIFICAR:
					regresar= actualizarArticulo(sesion);
					break;				
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // ejecutar
	
	private boolean procesarArticulo(Session sesion){
		try {
			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return true;
	} // procesarArticulo
	
	private boolean actualizarArticulo(Session sesion){
		try {
			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return true;
	} // actualizarArticulo
}

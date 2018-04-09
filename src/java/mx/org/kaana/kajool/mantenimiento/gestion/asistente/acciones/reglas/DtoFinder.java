package mx.org.kaana.kajool.mantenimiento.gestion.asistente.acciones.reglas;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import org.reflections.Reflections;

/**
 * Clase para la obtencion de los Class de los Dto contenidos en la aplicacion
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 30/06/2015
 *@time 10:09:40 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DtoFinder implements Serializable{

  private static final long serialVersionUID = -5092117818142760662L;

  private static DtoFinder instance;
	private static Object mutex;	

	/**
	 * Inicialización de variable mutex
	 */
	
	static{
		mutex= new Object();
	}

  /**
	 * Devuelve la instancia de la clase.
	 * @return Instancia de la clase.
	 */
	
	public static DtoFinder getInstance(){
		synchronized(mutex) {
			if(instance== null)
				instance= new DtoFinder();
		} // synchronized
		return instance;
	}

  /**
   * Devuelve los Class de los dto contenidos en el paquete de la encuesta indicada
   * @param encuesta Contiene el nombre de la encuesta en la que se buscaran los dto.
   * @return Set<Class<?>> con las instancias Class de los dto de la encuesta
   * @throws Exception
   */
	public Set<Class<?>> loadDtos(String encuesta) throws Exception{
		Set<Class<?>> regresar = null;
		Reflections reflections= null;		
		try {									
			reflections= new Reflections(getPaquete(encuesta));			
			regresar   = reflections.getTypesAnnotatedWith(Table.class);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // findManagedBeans

  /**
	 * Devuelve la ruta del paquete pivote del proyecto asociado al  en base su nombre.
	 * @param encuesta Nombre de la proyecto asociado al .
	 * @return Ruta del paquete pivote del proyecto
	 * @throws Exception
	 */
  private String getPaquete(String encuesta) throws Exception {
		String regresar= null;				
		try {									
			regresar= Constantes.PAQUETE_MANAGED_BEAN_REGISTER.concat(encuesta).concat(".db.dto").toLowerCase();
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // getPaquete
}

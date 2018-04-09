package mx.org.kaana.kajool.seguridad.beanmanager;

import java.io.Serializable;
import java.util.Set;
import javax.faces.bean.*;
import mx.org.kaana.libs.Constantes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;
import org.reflections.Reflections;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Feb 18, 2014
 *@time 11:25:24 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class BackingFinder implements Serializable{

	private static final long serialVersionUID= -2874653677907187557L;
	private static final Log LOG							= LogFactory.getLog(BackingFinder.class);	
	private static final String VIEW					= "view";
	private static final String SESSION				= "session";
	private static final String REQUEST				= "request";
	private static final String NONE					= "none";
	private static final String CUSTOM				= "custom";
	private static final String APPLICATION		= "application";	
	private static BackingFinder instance;
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
	
	public static BackingFinder getInstance(){
		synchronized(mutex) {
			if(instance== null)
				instance= new BackingFinder();
		} // synchronized
		return instance;
	}
	
	/**
	 * Devuelve una collección de clases que utilizán la anotación @ManagedBean
	 * @param encuesta Nombre del proyecto asociado al
	 * @return Collección de clases que utilizán la anotación @ManagedBean
	 * @throws Exception
	 */
	
	public Set<Class<?>> findManagedBeans(String encuesta) throws Exception{
		Set<Class<?>> regresar = null;
		Reflections reflections= null;		
		try {									
			reflections= new Reflections(getPaquete(encuesta));			
			regresar   = reflections.getTypesAnnotatedWith(ManagedBean.class);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // findManagedBeans

	/**
	 * Devuelve el tipo de scope que utiliza la clase.
	 * @param clase Objeto de tipo Class que utiliza @Annotation
	 * @return Tipo de scope que utiliza la clase
	 * @throws Exception
	 */
	
	public String findScope(Class<?> clase) throws Exception{
		String regresar= null;
		try {
			if(clase.getAnnotation(ViewScoped.class)!= null)
				regresar= VIEW;
			else if(clase.getAnnotation(SessionScoped.class)!= null)
				regresar= SESSION;
			else if(clase.getAnnotation(ApplicationScoped.class)!= null)
				regresar= APPLICATION;
			else if(clase.getAnnotation(NoneScoped.class)!= null)
				regresar= NONE;
			else if(clase.getAnnotation(RequestScoped.class)!= null)
				regresar= REQUEST;
			else if(clase.getAnnotation(CustomScoped.class)!= null)
				regresar= CUSTOM;			
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // findScope
	
	/**
	 * Devuelve la ruta del paquete pivote del proyecto asociado al  en base su nombre.
	 * @param encuesta Nombre de la proyecto asociado al .
	 * @return Ruta del paquete pivote del proyecto
	 * @throws Exception
	 */
	
	private String getPaquete(String encuesta) throws Exception {
		String regresar= null;				
		try {									
			regresar= Constantes.PAQUETE_MANAGED_BEAN_REGISTER.concat(encuesta);
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // getPaquete
}

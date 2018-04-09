package mx.org.kaana.kajool.db.comun.josql;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.xml.Dml;
import org.apache.log4j.Logger;
import org.josql.Query;
import org.josql.QueryResults;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2014
 *@time 01:04:52 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class FactoryJoSql <T extends Object> {
	
	private static final Logger LOG= Logger.getLogger(FactoryJoSql.class);	
	private static FactoryJoSql instance;
	private static Object mutex;
	
	/**
	 * Inicialización de variable mutex
	 */
	
	static {
		mutex= new Object();
	} // static
	
	/**
	 * Devuelve la instancia de la clase.
	 * @return Instacia de la clase.
	 */
	
	public static FactoryJoSql getInstance(){
		synchronized(mutex){
			if(instance== null)
				instance= new FactoryJoSql();
		} // synchronized
		return instance;
	} // getInstance
	
	/**
	 * Devuelve una lista de objetos del tipo de la clase que se especifica mediante una consulta a una colleccion de objetos del mismo tipo.
	 * @param className Define el id de la unidad especificada en el archivo de consultas josql.xml en base al nombre de la clase.
	 * @param params Encapsula los parametros que seran remplazados en la consulta.
	 * @param collection Colección de objetos del tipo de clase definida a la que se aplicará la consulta.
	 * @return Devuelve lista de objetos del tipo de clase definida del resultado de la consulta aplicada a la colleccion.
	 * @throws Exception
	 */
	
	public List<T> toRecords(Class className, Map params, List<T> collection) throws Exception{
		return toRecords(className.getSimpleName(), Constantes.DML_SELECT, params, collection);		
	} // toRecords
	
	/**
	 * Devuelve una lista de objetos del tipo de la clase que se especifica mediante una consulta a una colleccion de objetos del mismo tipo.	
	 * @param className Define el id de la unidad especificada en el archivo de consultas josql.xml en base al nombre de la clase.
	 * @param id Define el id de la consulta especificada dentro de la unidad.
	 * @param params Encapsula los parametros que seran remplazados en la consulta.
   * @param collection Colección de objetos del tipo de clase definida a la que se aplicará la consulta.
	 * @return Devuelve lista de objetos del tipo de clase definida del resultado de la consulta aplicada a la colleccion.
	 * @throws Exception
	 */
	
	public List<T> toRecords(Class className, String id, Map params, List<T> collection) throws Exception{
		return toRecords(className.getSimpleName(), id, params, collection);
	}	// toRecords
	
	/**
	 * Devuelve una lista de objetos del tipo de la clase que se especifica mediante una consulta a una colleccion de objetos del mismo tipo.
	 * @param process Define el id de la unidad especificada en el archivo de consultas josql.xml
	 * @param id Define el id de la consulta especificada dentro de la unidad.
	 * @param params Encapsula los parametros que seran remplazados en la consulta.
	 * @param collection Colección de objetos del tipo de clase definida a la que se aplicará la consulta.
	 * @return Devuelve lista de objetos del tipo de clase definida del resultado de la consulta aplicada a la colleccion.
	 * @throws Exception
	 */
	
	public List<T> toRecords(String process, String id, Map params, List<T> collection) throws Exception{
		List<T> regresar    = null;
		QueryResults results= null;
		Query query         = null;				
		try {						
			LOG.info("Consulta joSql ".concat(process).concat("id").concat(id));
			query= new Query();			
			query.parse(Dml.getInstance().getSelect(process, id, params));
			results= query.execute(collection);			
			regresar= results!= null ? results.getResults() : new ArrayList<>();			
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	}	// toRecords

	/**
	 * Devuelve el primer objeto del tipo de la clase que se especifica mediante una consulta a una colleccion de objetos del mismo tipo.
	 * @param process Define el id de la unidad especificada en el archivo de consultas josql.xml
	 * @param id Define el id de la consulta especificada dentro de la unidad.
	 * @param params Encapsula los parametros que seran remplazados en la consulta.
	 * @param collection Colección de objetos del tipo de clase definida a la que se aplicará la consulta.
	 * @return Devuelve objeto del tipo de clase definida del resultado de la consulta aplicada a la colleccion.
	 * @throws Exception
	 */
	
	public T toRecord(String process, String id, Map params, List<T> collection) throws Exception{
		T regresar          = null;
		QueryResults results= null;
		Query query         = null;				
		try {						
			LOG.info("Consulta joSql ".concat(process).concat("id").concat(id));
			query= new Query();			
			query.parse(Dml.getInstance().getSelect(process, id, params));
			results= query.execute(collection);			
			if(results!= null)
				regresar= (T) results.getResults().get(0);						
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	}	// toRecords
}

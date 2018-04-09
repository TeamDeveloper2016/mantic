package mx.org.kaana.kajool.reglas.comun;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jun 11, 2012
 *@time 2:56:19 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import org.primefaces.model.SortOrder;


public class FormatLazyModel<T extends IBaseDto> extends EntityLazyModel<T> implements Serializable {
	private static final long serialVersionUID=8044282200365418064L;

	private List<Columna> fields;

	public FormatLazyModel(String proceso, List<Columna> fields) {
		this(proceso,Collections.EMPTY_MAP, fields);		
	}
	
	public FormatLazyModel(String proceso, Map<String, Object> params, List<Columna> fields) {
		this(proceso,Constantes.DML_SELECT,params,fields);		
	}
	
	public FormatLazyModel(String proceso, String idXml, List<Columna> fields) {
		this(proceso,idXml,Collections.EMPTY_MAP, fields,-1L);		
	}		
	
	public FormatLazyModel(String proceso, String idXml, Map<String, Object> params, List<Columna> fields) {		
		this(proceso, idXml, params,fields,-1L);		
	}	
	
	public FormatLazyModel(String proceso, Map<String, Object> params, List<Columna> fields, Long idFuenteDato) {
	  this(proceso,Constantes.DML_SELECT,params,fields,idFuenteDato);			
	}	
	
	public FormatLazyModel(Class<? extends IBaseDto> proceso, List<Columna> fields) {
		this(proceso,fields,-1L);
	}
	
	public FormatLazyModel(Class<? extends IBaseDto> proceso, List<Columna> fields, Long idFuenteDato) {
		this(proceso.getSimpleName(),Constantes.DML_SELECT,Collections.EMPTY_MAP, fields,idFuenteDato);		
	}
	
	public FormatLazyModel(String proceso, String idXml, Map<String, Object> params, List<Columna> fields, Long idFuenteDato) {
		super( proceso, idXml, params,idFuenteDato);	
		this.fields= new ArrayList<>(fields);
	}		

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<T> regresar= super.load(first, pageSize, sortField, sortOrder, filters);		
		if(regresar!= null) {
			regresar= (List<T>)((ArrayList<T>)regresar).clone();
			for(T entity: regresar) {
				for(Columna columna: this.fields) {
					if(!((Entity)entity).containsKey(columna.getName())) {
						throw new RuntimeException("No existe la columna "+ columna.getName()+ " en la proceso seleccionado.");
					} // if
					Value value= ((Entity)entity).get(columna.getName());
					value.setData(Global.format(columna.getFormat(), value.getData()));
				} // for
			} // for
		} // if	
		return (List<T>)regresar;
	}
}

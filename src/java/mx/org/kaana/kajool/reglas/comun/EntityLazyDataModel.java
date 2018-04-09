package mx.org.kaana.kajool.reglas.comun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *@company KAANA
 *@project  (Sistema de Seguimiento y Control de proyectos estad�sticos)
 *@date Feb 20, 2013
 *@time 1:46:16 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class EntityLazyDataModel<T extends IBaseDto> extends LazyDataModel<T>{
	private static final long serialVersionUID=4223638033114282149L;
	
	private List<T> dataSource;

	public EntityLazyDataModel(List<T> dataSource) {
		this.dataSource=dataSource;
	}
		
	@Override
	public T getRowData(String rowKey) {
		T regresar = null;
		for(IBaseDto dto: dataSource) {
			if(dto.getKey().toString().equals(rowKey))
				regresar =  (T)dto;
		} // for
		return regresar;
	} //getRowData
	
	@Override
	public Object getRowKey(IBaseDto dto){
		return dto.getKey();
	} // getRowKey
	
	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters){
		List<T> regresar= new ArrayList<T>();
		regresar = this.dataSource;
		//rowCount
		int dataSize= regresar.size();
		this.setRowCount(dataSize);
		// paginacion
		if (dataSize > pageSize) {
			try {
				return regresar.subList(first, first + pageSize);
			} // try
			catch (Exception e) {
				return regresar.subList(first, first + (dataSize % pageSize));
			} // catch
		} // if
		else
			return regresar;
	} // load

  public T getFirstRecord() {
    T regresar= null;
    if (this.dataSource!= null && !this.dataSource.isEmpty())
      regresar= this.dataSource.get(0);
    return regresar;
  }

}

package mx.org.kaana.kajool.reglas.comun;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 24, 2012
 *@time 1:30:13 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


public class DtoLazyModel<T extends IBaseDto> extends LazyDataModel<T> {
	
	private static final long serialVersionUID=-2386136605474281536L;
	
	private Class proceso;
	private Map<String, Object> params;

  public DtoLazyModel(Class<? extends IBaseDto> proceso) {
		this(proceso,new HashMap<String, String>());
		this.params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
	}

  public DtoLazyModel(Class<? extends IBaseDto> proceso, Map <String,String> params) {
    this.proceso = proceso;
    this.params  = (Map<String, Object>) ((HashMap) params).clone();
  }
	
	@Override
	public T getRowData(String rowKey) {
    T regresar      = null;		
    try {
      regresar=(T) DaoFactory.getInstance().findById(this.proceso, Numero.getLong(rowKey));
    }  // try
    catch (Exception e) {
      throw new RuntimeException(e);
    } // catch
    return regresar;
	} // getRowData

	@Override
	public Object getRowKey(IBaseDto dto) {
		return dto.getKey();
	} // getRowKey

  @Override
  public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
    List<T> regresar= null;
		PageRecords page= null;
    if(pageSize> 0) {
      try {
        page= DaoFactory.getInstance().findPage(proceso, this.params, first, pageSize);
        regresar= (List<T>)page.getList();
        this.setRowCount(page.getCount());
      } // try
      catch(Exception e) {
        Error.mensaje(e);
        regresar= new ArrayList<T>();
        this.setRowCount(0);
      } // catch
    } // if
		return regresar;	
  }
	
	@Override
	public void setRowIndex(final int rowIndex) {
		if (rowIndex== -1 || getPageSize()== 0)
			super.setRowIndex(-1);
		else
			super.setRowIndex(rowIndex % getPageSize());
	}	
	
}

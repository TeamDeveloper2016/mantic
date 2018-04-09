package mx.org.kaana.kajool.reglas.comun;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 10, 2012
 *@time 11:43:17 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import mx.org.kaana.xml.Dml;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public class DynamicEntityLazyModel<T extends IBaseDto> extends LazyDataModel<T> {

	private static final long serialVersionUID=4363382175414414122L;
	private String proceso;
	private String idXml;
	private Map<String, Object> params;
	private String keyName;
  private Long idFuenteDato;

	public DynamicEntityLazyModel(Class<? extends IBaseDto> proceso, Long idFuenteDato) {
		this(proceso.getSimpleName(), idFuenteDato);
	}

	public DynamicEntityLazyModel(String proceso, Long idFuenteDato) {
		this(proceso, Constantes.DML_SELECT, idFuenteDato);
	}

	public DynamicEntityLazyModel(String proceso, String idXml, Long idFuenteDato) {
		this(proceso, idXml, new HashMap<String, Object>(), idFuenteDato);
		this.params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
	}

	public DynamicEntityLazyModel(String proceso, Map<String, Object> params, Long idFuenteDato) {
		this(proceso, Constantes.DML_SELECT, params, idFuenteDato);
	}

	public DynamicEntityLazyModel(String proceso, String idXml, Map<String, Object> params, Long idFuenteDato) {
		this(proceso, idXml, params, idFuenteDato, "");
	}
	
	public DynamicEntityLazyModel(String proceso, String idXml, Map<String, Object> params, Long idFuenteDato, String keyName) {
		this.proceso     = proceso;
		this.idXml       = idXml;
		this.params      = (Map<String, Object>) ((HashMap) params).clone();
    this.idFuenteDato= idFuenteDato;
		this.keyName     = keyName;
	}

	@Override
	public T getRowData(String rowKey) {
		T regresar  = null;
		StringBuilder sb= new StringBuilder();
		try {
			if(!Cadena.isVacio(this.keyName)) {
				sb.append("select * from (");
        if(Dml.getInstance().exists(this.proceso, this.idXml.concat(Cadena.letraCapital(Constantes.DML_SELECT))))
  				sb.append(Dml.getInstance().getSelect(this.proceso, this.idXml.concat(Cadena.letraCapital(Constantes.DML_SELECT)), this.params));
        else
  				sb.append(Dml.getInstance().getSelect(this.proceso, this.idXml, this.params));
				sb.append(") datos where ");
				sb.append(Cadena.toSqlName(keyName)).append("=").append(rowKey);
				regresar=(T) DaoFactory.getInstance().toEntity(this.idFuenteDato, sb.toString());
			} // if	
			else
				if(regresar== null && rowKey!= null && !rowKey.equals("-1"))
  				throw new RuntimeException("La vista ["+this.proceso+"] no se le definio un campo llave 'id_key_<nombre>'.");
		} // try
		catch(Exception e) {
			throw new RuntimeException(e);
		} // catch
		return regresar;
	} // getRowData

	@Override
	public Object getRowKey(IBaseDto dto) {
		return dto.getKey();
	}//getRowKey

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<T> regresar=null;
		PageRecords page=null;
    if(pageSize> 0) {
      try {
        if(this.params!=null&&!this.params.isEmpty()) {
          page=DaoFactory.getInstance().toEntityPage(this.idFuenteDato, this.proceso, this.idXml, this.params, first, pageSize);
          regresar=(List<T>) page.getList();
          this.setRowCount(page.getCount());
        } // if
      } // try
      catch(Exception e) {
        throw new RuntimeException(e);
      } // catch
    } // if
		return regresar;
	}

	@Override
	public void setRowIndex(final int rowIndex) {
		if(rowIndex== -1||getPageSize()== 0)
			super.setRowIndex(-1);
		else
			super.setRowIndex(rowIndex % getPageSize());
	}

}

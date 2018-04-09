package mx.org.kaana.kajool.reglas.comun;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jun 11, 2012
 *@time 2:56:19 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.xml.Dml;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public class EntityLazyModel<T extends IBaseDto> extends LazyDataModel<T> {

	private static final long serialVersionUID=4363382175414414122L;
	private String proceso;
	private String idXml;
	private Map<String, Object> params;
	private String keyName;
	private Long idFuenteDato;

	public EntityLazyModel(Class<? extends IBaseDto> proceso) {
		this(proceso.getSimpleName());
	}

	public EntityLazyModel(String proceso) {
		this(proceso, Constantes.DML_SELECT);
	}

	public EntityLazyModel(String proceso, String idXml) {
		this(proceso, idXml, new HashMap<String, Object>());
		this.params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
	}

	public EntityLazyModel(String proceso, Map<String, Object> params) {
		this(proceso, Constantes.DML_SELECT, params,-1L);
	}
	
	public EntityLazyModel(String proceso, Map<String, Object> params,Long idFuenteDato) {
		this(proceso, Constantes.DML_SELECT, params);
	}
	
	public EntityLazyModel(String proceso, String idXml, Map<String, Object> params) {
		this(proceso,idXml,params, -1L);
	}
	
	public EntityLazyModel(String proceso, String idXml, Map<String, Object> params, Long idFuenteDato) {
		this.proceso= proceso;
		this.idXml  = idXml;
		this.params = (Map<String, Object>) ((HashMap) params).clone();
		this.idFuenteDato = idFuenteDato;
	}
	

	@Override
	public T getRowData(String rowKey) {
		T regresar      = null;
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
				regresar=(T) DaoFactory.getInstance().toEntity(sb.toString());
			} // if	
			else
				if(regresar==null)
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
	} // getRowKey

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		PageRecords page=null;
    if(pageSize> 0) {
      try {
        if(this.params!=null&&!this.params.isEmpty()) {
          page=DaoFactory.getInstance().toEntityPage(this.idFuenteDato,this.proceso, this.idXml, this.params, first, pageSize);
          this.setWrappedData((List<T>) page.getList());
          this.setRowCount(page.getCount());
          if(this.getRowCount()>0) {
            this.keyName=((Entity)((List<T>)this.getWrappedData()).get(0)).getKeyName();					
          } // if
        } // if
      } // try
      catch(Exception e) {
        throw new RuntimeException(e);
      } // catch
    } // if
		return ((List<T>)this.getWrappedData());
	}

	@Override
	public void setRowIndex(final int rowIndex) {
		if(rowIndex==-1||getPageSize()==0)
			super.setRowIndex(-1);
		else
			super.setRowIndex(rowIndex%getPageSize());
	}

  public String getKeyName() {
    return keyName;
  }

  public Map<String, Object> getParams() {
    return params;
  }

}

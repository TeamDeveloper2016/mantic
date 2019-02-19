package mx.org.kaana.mantic.facturas.reglas;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import org.primefaces.model.SortOrder;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 18/02/2019
 *@time 09:00:19 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FormatTicket extends FormatCustomLazy implements Serializable {

	private static final long serialVersionUID=422887930228813057L;

	public FormatTicket(String proceso, String idXml, Map<String, Object> params, List<Columna> columns) {
		super(proceso, idXml, params, columns);
	}

	@Override
	public List<IBaseDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<IBaseDto> items= super.load(first, pageSize, sortField, sortOrder, filters);
		if(items!= null) {
			for (IBaseDto item: items) 
				this.toLoad(item);
		} // if
		return items;
	}

	@Override
	public IBaseDto getRowData(String rowKey) {
		IBaseDto item= super.getRowData(rowKey);
		this.toLoad(item);
		return item;
	}

  private void toLoad(IBaseDto item) {
		try {
			if(item!= null) {
				Map<String, Object> params= item.toMap();
				Value cantidad= (Value)DaoFactory.getInstance().toField("VistaFicticiasDto", "concentrado", params, "cantidad");
				if(cantidad.toDouble()> 0D) {
					Value valor   = ((Entity)item).get("importe");
					valor.setData(Numero.toRedondearSat(valor.toDouble()- cantidad.toDouble()));
					Value total   = ((Entity)item).get("total");
					total.setData(Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, valor.toDouble()));
				} // if
				((Entity)item).put("tiene", new Value("tiene", cantidad.toDouble()> 0D? 1: 0));
				((Entity)item).put("devolucion", new Value("devolucion", cantidad.toDouble()> 0D));
				Value value= (Value)DaoFactory.getInstance().toField("VistaVentasDto", "tipoMedioPago", params, "medios");
				if(value!= null)
					((Entity)item).put("medio", new Value("medio", value.toString()));
				else
					((Entity)item).put("medio", new Value("medio", ""));
			} // if
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
	}	
	
}

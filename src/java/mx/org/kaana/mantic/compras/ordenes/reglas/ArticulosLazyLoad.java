package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.formato.Numero;
import org.primefaces.model.SortOrder;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 19/07/2018
 * @time 10:30:05 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class ArticulosLazyLoad extends FormatCustomLazy implements Serializable {

	private static final long serialVersionUID=8818366857483105741L;

	public ArticulosLazyLoad(String proceso, String idXml, Map<String, Object> params, List<Columna> columns) {
		super(proceso, idXml, params, columns);
	}

	@Override
	public List<IBaseDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<IBaseDto> regresar= (List<IBaseDto>)super.load(first, pageSize, sortField, sortOrder, filters);
		Value value= null;
		for (IBaseDto item: regresar) {
			Long idAplicar= new Long(((Entity)item).toString("idAplicar"));
			value= new Value("afectar", idAplicar.equals(1L));
			((Entity)item).put(value.getName(), value);
      
			Double costo= new Double(((Entity)item).toString("costo"));
			Double valor= new Double(((Entity)item).toString("valor"));
		  Descuentos descuentos= new Descuentos(costo, ((Entity)item).toString("descuento")+ ","+ ((Entity)item).toString("extras"));
		  double importe= descuentos.toImporte();
		  importe= Numero.toRedondearSat(valor- (importe== 0? costo: importe)); 
			value= new Value("diferencias", valor== 0? 0: Numero.toRedondearSat(importe* 100/ valor));
			((Entity)item).put(value.getName(), value);
			
		} // for 
		return regresar;
	}

	
}

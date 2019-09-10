package mx.org.kaana.mantic.consultas.reglas;

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
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 28/06/2019
 *@time 10:24:20 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class UtilidadArticulosLazy extends FormatCustomLazy implements Serializable {

	private static final long serialVersionUID=-1006491386246716097L;

	public UtilidadArticulosLazy(String proceso, String idXml, Map<String, Object> params, List<Columna> columns) {
		super(proceso, idXml, params, columns);
	}

	@Override
	public List<IBaseDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<IBaseDto> regresar= super.load(first, pageSize, sortField, sortOrder, filters);		
		if(regresar!= null) {
			for(IBaseDto item: regresar) {
				Entity row= (Entity)item;
    		double calculo= row.toDouble("precio")* (1+ (row.toDouble("iva")/ 100));
				row.put("utilidadMenudeo", new Value("utilidadMenudeo", Numero.toRedondearSat((row.toDouble("menudeo")- calculo)* 100/ calculo)));
				row.put("utilidadMedioMayoreo", new Value("utilidadMedioMayoreo", Numero.toRedondearSat((row.toDouble("medioMayoreo")- calculo)* 100/ calculo)));
				row.put("utilidadMayoreo", new Value("utilidadMayoreo", Numero.toRedondearSat((row.toDouble("mayoreo")- calculo)* 100/ calculo)));
			} // for
		} // if	
		return regresar;
	}
	
}

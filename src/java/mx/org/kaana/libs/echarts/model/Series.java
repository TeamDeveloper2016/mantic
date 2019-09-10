package mx.org.kaana.libs.echarts.model;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.echarts.beans.Serie;
import mx.org.kaana.libs.formato.Cadena;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 6/09/2019
 *@time 11:37:04 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Series implements Serializable {

	private static final long serialVersionUID=2040043938762402282L;
	private static final String FIELD_GROUP= "serie";	
	private static final String FIELD_TEXT = "text";	
	private static final String FIELD_VALUE= "value";	
	
	private List<Entity> data;

	public Series(List<Entity> data) {
		this.data=data;
	}
	
	public DataModel simple(String legend) {
		DataModel regresar= new DataModel();
		regresar.getLegend().add(legend);
		Serie serie       = new Serie(legend);
		serie.getData().clear();
		for (Entity item: this.data) {
			regresar.label(item.toString(FIELD_TEXT));
			serie.getData().add(item.toDouble(FIELD_VALUE));
		} // for
		regresar.add(serie);
		return regresar;
	}
	
	public DataModel multiple() {
		DataModel regresar= new DataModel();
		Serie serie = null;
		String group= null;
		int count   = 0;
		for (Entity item: this.data) {
			if(Cadena.isVacio(group) || !group.equals(item.toString(FIELD_GROUP))) {
				if(!Cadena.isVacio(group))
      		regresar.add(serie);
				group= item.toString(FIELD_GROUP);
				regresar.getLegend().add(group);
			  serie= new Serie(group);
  			serie.getData().clear();
				count++;
			}	// if
			if(count== 1)
			  regresar.label(item.toString(FIELD_TEXT));
			serie.getData().add(item.toDouble(FIELD_VALUE));
		} // for
 		regresar.add(serie);
		return regresar;
	}

}

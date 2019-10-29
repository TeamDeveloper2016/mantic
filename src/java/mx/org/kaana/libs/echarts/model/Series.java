package mx.org.kaana.libs.echarts.model;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.echarts.bar.Value;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.enums.EData;
import mx.org.kaana.libs.echarts.pie.Data;
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
	private static final String FIELD_GROUP= "category";	
	private static final String FIELD_TEXT = "serie";	
	private static final String FIELD_VALUE= "value";	
	
	private List<Entity> data;
	private DataModel model;

	public Series(EData kind, List<Entity> data) {
		this(null, kind, data);
	}
	
	public Series(String name, EData kind, List<Entity> data) {
		this.data=data;
		switch (kind) {
			case SIMPLE:
				this.model= this.simple(name);
				break;
			case MULTIPLE:
				this.model= this.multiple();
				break;
			case DATA:
				this.model= this.data(name);
				break;
			case STACK:
				this.model= this.group();
				break;
		} // switch
	}

	public DataModel getModel() {
		return model;
	}
	
	protected DataModel simple(String legend) {
		DataModel regresar= new DataModel();
		regresar.getLegend().add(legend);
		mx.org.kaana.libs.echarts.bar.Serie serie= new mx.org.kaana.libs.echarts.bar.Serie(legend);
		serie.getData().clear();
		for (Entity item: this.data) {
			regresar.label(item.toString(FIELD_TEXT));
			serie.getData().add(new Value(item.toString(FIELD_TEXT), item.toDouble(FIELD_VALUE)));
		} // for
		regresar.serie(serie);
		return regresar;
	}
	
	protected DataModel multiple() {
		DataModel regresar= new DataModel();
		mx.org.kaana.libs.echarts.bar.Serie serie = null;
		String group= null;
		int count   = 0;
		for (Entity item: this.data) {
			if(Cadena.isVacio(group) || !group.equals(item.toString(FIELD_GROUP))) {
				if(!Cadena.isVacio(group))
      		regresar.serie(serie);
				group= item.toString(FIELD_GROUP);
				regresar.getLegend().add(group);
			  serie= new mx.org.kaana.libs.echarts.bar.Serie(group);
  			serie.getData().clear();
				count++;
			}	// if
			if(count== 1)
			  regresar.label(item.toString(FIELD_TEXT));
			serie.getData().add(new Value(item.toString(FIELD_TEXT), item.toDouble(FIELD_VALUE)));
		} // for
 		regresar.serie(serie);
		return regresar;
	}

	protected DataModel data(String legend) {
		DataModel regresar= new DataModel();
		mx.org.kaana.libs.echarts.pie.Serie serie= new mx.org.kaana.libs.echarts.pie.Serie(legend);
		serie.getData().clear();
		for (Entity item: this.data) {
			regresar.getLegend().add(item.toString(FIELD_TEXT));
  		serie.getData().add(new Data(item.toString(FIELD_TEXT), item.toDouble(FIELD_VALUE)));
		} // for
 		regresar.data(serie);
		return regresar;
	}
	
	protected DataModel stack(String legend) {
		DataModel regresar= new DataModel();
		regresar.getLegend().add(legend);
		mx.org.kaana.libs.echarts.stack.Serie serie= new mx.org.kaana.libs.echarts.stack.Serie(legend);
		serie.getData().clear();
		for (Entity item: this.data) {
			regresar.label(item.toString(FIELD_TEXT));
			serie.getData().add(new Value(item.toString(FIELD_TEXT), item.toDouble(FIELD_VALUE)));
		} // for
		regresar.stack(serie);
		return regresar;
	}
	
	protected DataModel group() {
		DataModel regresar= new DataModel();
		mx.org.kaana.libs.echarts.stack.Serie serie = null;
		String group= null;
		String color= Colors.toColor();
		for (Entity item: this.data) {
			if(Cadena.isVacio(group) || !group.equals(item.toString(FIELD_GROUP))) {
				color= Colors.toColor();
				if(!Cadena.isVacio(group))
      		regresar.stack(serie);
				group= item.toString(FIELD_GROUP);
				regresar.getLegend().add(group);
			  serie= new mx.org.kaana.libs.echarts.stack.Serie(group, color);
  			serie.getData().clear();
			}	// if
		  regresar.label(item.toString(FIELD_TEXT));
			serie.getData().add(new Value(item.toString(FIELD_TEXT), item.toDouble(FIELD_VALUE), color));
		} // for
 		regresar.stack(serie);
		return regresar;
	}
	
}

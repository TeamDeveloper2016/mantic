package mx.org.kaana.libs.echarts.model;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Xaxis;
import mx.org.kaana.libs.echarts.enums.EData;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/09/2019
 *@time 04:33:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Multiple extends Series implements IDataSet, Serializable {

	private static final long serialVersionUID=-941459668404769397L;

	public Multiple(List<Entity> data) {
		super(EData.MULTIPLE, data);
	}

	@Override
	public List<mx.org.kaana.libs.echarts.bar.Serie> getSeries() {
		return this.getModel().getSeries();
	}

	@Override
	public List<mx.org.kaana.libs.echarts.pie.Serie> getDatas() {
		return this.getModel().getDatas();
	}

	@Override
	public Xaxis getXaxis() {
		return this.getModel().getXaxis();
	}

	@Override
	public Legend getLegend() {
		return this.getModel().getLegend();
	}
	
}

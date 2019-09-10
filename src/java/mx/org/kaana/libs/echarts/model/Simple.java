package mx.org.kaana.libs.echarts.model;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Serie;
import mx.org.kaana.libs.echarts.beans.Xaxis;
import mx.org.kaana.libs.echarts.beans.Yaxis;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/09/2019
 *@time 04:33:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Simple extends Series implements IDataSet, Serializable {

	private static final long serialVersionUID=-941459668404769397L;

	private String name;
	
	public Simple(String name, List<Entity> data) {
		super(data);
		this.name= name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	@Override
	public DataModel dataSet() {
		return this.simple(this.name);
	}

	@Override
	public List<Serie> getDataset() {
		return this.dataSet().getDataset();
	}

	@Override
	public Xaxis getXaxis() {
		return this.dataSet().getXaxis();
	}

	@Override
	public Legend getLegend() {
		return this.dataSet().getLegend();
	}
	
}

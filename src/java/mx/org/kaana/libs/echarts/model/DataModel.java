package mx.org.kaana.libs.echarts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.bar.Serie;
import mx.org.kaana.libs.echarts.beans.Xaxis;
import mx.org.kaana.libs.echarts.pie.Data;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/09/2019
 *@time 10:46:31 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DataModel implements Serializable {

	private static final long serialVersionUID=6955003730243424297L;

	private Xaxis xAxis;
	private List<mx.org.kaana.libs.echarts.bar.Serie> series;
	private List<mx.org.kaana.libs.echarts.pie.Serie> datas;
	private Legend legend;

	public DataModel() {
		this(new ArrayList<>(), new ArrayList<>(), new Legend());
	}

	public DataModel(List<mx.org.kaana.libs.echarts.pie.Serie> datas, List<mx.org.kaana.libs.echarts.bar.Serie> series, Legend legend) {
		this.datas=datas;
		this.xAxis=new Xaxis();
		this.series=series;
		this.legend=legend;
		this.xAxis.clear();
	}

	public List<mx.org.kaana.libs.echarts.pie.Serie> getDatas() {
		return datas;
	}

	public void setDatas(List<mx.org.kaana.libs.echarts.pie.Serie> datas) {
		this.datas=datas;
	}

	public Xaxis getXaxis() {
		return xAxis;
	}

	public void setXaxis(Xaxis xAxis) {
		this.xAxis=xAxis;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> Series) {
		this.series=series;
	}

	public Legend getLegend() {
		return legend;
	}

	public void setLegend(Legend legend) {
		this.legend=legend;
	}

	public void serie(mx.org.kaana.libs.echarts.bar.Serie serie) {
	  this.series.add(serie);
	}
	
	public void data(mx.org.kaana.libs.echarts.pie.Serie serie) {
	  this.datas.add(serie);
	}
	
	public void label(String label) {
	  this.xAxis.add(label);
	}
	
	public void legend(String legend) {
	  this.legend.add(legend);
	}

	@Override
	public String toString() {
		return "DataModel{"+"xAxis="+xAxis+", series="+series+", datas="+datas+", legend="+legend+'}';
	}
	
	
}

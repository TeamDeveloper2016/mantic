package mx.org.kaana.libs.echarts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.bar.Serie;
import mx.org.kaana.libs.echarts.beans.Xaxis;

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
	private List<mx.org.kaana.libs.echarts.stack.Serie> stacks;
	private Legend legend;

	public DataModel() {
		this(new ArrayList<mx.org.kaana.libs.echarts.pie.Serie>(), new ArrayList<mx.org.kaana.libs.echarts.bar.Serie>(), new ArrayList<mx.org.kaana.libs.echarts.stack.Serie>(), new Legend());
	}

	public DataModel(List<mx.org.kaana.libs.echarts.pie.Serie> datas, List<mx.org.kaana.libs.echarts.bar.Serie> series, List<mx.org.kaana.libs.echarts.stack.Serie> stacks, Legend legend) {
		this.series=series;
		this.datas=datas;
		this.stacks=stacks;
		this.legend=legend;
		this.xAxis=new Xaxis();
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

	public void setSeries(List<Serie> series) {
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
	
	public void stack(mx.org.kaana.libs.echarts.stack.Serie serie) {
	  this.stacks.add(serie);
	}

	public void label(String label) {
	  this.xAxis.add(label);
	}
	
	public void legend(String legend) {
	  this.legend.add(legend);
	}

	public List<mx.org.kaana.libs.echarts.stack.Serie> getStacks() {
		return stacks;
	}

	public void setStacks(List<mx.org.kaana.libs.echarts.stack.Serie> stacks) {
		this.stacks=stacks;
	}

	@Override
	public String toString() {
		return "DataModel{"+"xAxis="+xAxis+", series="+series+", datas="+datas+", stacks="+stacks+", legend="+legend+'}';
	}
	
}

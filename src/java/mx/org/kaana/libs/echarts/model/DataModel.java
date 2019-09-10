package mx.org.kaana.libs.echarts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Serie;
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
	private List<Serie> dataset;
	private Legend legend;

	public DataModel() {
		this(new ArrayList<>(), new Legend());
	}

	public DataModel(List<Serie> dataset, Legend legend) {
		this.xAxis= new Xaxis();
		this.dataset=dataset;
		this.legend=legend;
		this.xAxis.clear();
	}

	public Xaxis getXaxis() {
		return xAxis;
	}

	public void setXaxis(Xaxis xAxis) {
		this.xAxis=xAxis;
	}

	public List<Serie> getDataset() {
		return dataset;
	}

	public void setDataset(List<Serie> dataset) {
		this.dataset=dataset;
	}

	public Legend getLegend() {
		return legend;
	}

	public void setLegend(Legend legend) {
		this.legend=legend;
	}

	public void add(Serie serie) {
	  this.dataset.add(serie);
	}
	
	public void label(String label) {
	  this.xAxis.add(label);
	}
	
	public void legend(String legend) {
	  this.legend.add(legend);
	}
	
	@Override
	public String toString() {
		return "DataModel{"+"xAxis="+xAxis+", dataset="+dataset+'}';
	}
	
}

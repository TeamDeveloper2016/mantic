package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.pie.Serie;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.model.IDataSet;
import mx.org.kaana.libs.echarts.pie.Data;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:20:56 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DonutModel extends PieModel implements Serializable {

	private static final long serialVersionUID=9169328733926503563L;

	public DonutModel(String name) {
		this(name, "40%", "60%", new Title("CGOR", "subtitulo"));
	}

	public DonutModel(String name, String radius, String inside, Title title) {
		super(name, radius, title, new Legend("2019"), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new ArrayList<Serie>());
		this.getSeries().add(new Serie(name, new ArrayList(Arrays.asList(inside, radius))));
		this.prepare(radius, inside);
	}

	public DonutModel(String name, String radius, String inside, IDataSet data) {
		super(name, radius, data);
		this.prepare(radius, inside);
	}

	public DonutModel(String name, String radius, String inside, Title title, IDataSet data) {
		super(name, radius, title, data);
		this.prepare(radius, inside);
	}

	public DonutModel(String name, String radius, String inside, Title title, Legend legend, List<String> color, ToolTip tooltip, List<Serie> series) {
		super(name, radius, title, legend, color, tooltip, series);
		this.prepare(radius, inside);
	}

	private void prepare(String radius, String inside) {
		this.getTooltip().setTrigger("item");
		this.getTooltip().setAxisPointer(null);
		this.getLegend().getData().clear();
		for (Serie serie: this.getSeries()) {
			serie.setRadius(new ArrayList(Arrays.asList(inside, radius)));
		} // for
		for (Data data: this.getSeries().get(0).getData()) {
			this.getLegend().add(data.getName());
		} // for
	}
	
}

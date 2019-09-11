package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Axis;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.pie.Serie;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.model.IDataSet;
import mx.org.kaana.libs.echarts.pie.Data;
import mx.org.kaana.libs.json.Decoder;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:20:56 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class PieModel extends ChartModel implements Serializable {

	private static final long serialVersionUID=3783050684207305584L;
  private static final String[] SERIES_COLORS= {"#0080FF", "#80BFFF", "#3398AA", "#0059B3", "#001A33"};
	
	private Title title;
	private Legend legend;
	private List<String> color;
	private ToolTip tooltip;
	private List<Serie> series;

	public PieModel(String name) {
		this(name, new Title("CGOR", "subtitulo"));
	}

	public PieModel(String name, Title title) {
		this(title, new Legend("2019"), new ArrayList(Arrays.asList(SERIES_COLORS)), new ToolTip(), new ArrayList<Serie>());
		this.series.add(new Serie(name));
		this.getLegend().getData().clear();
		for (Data data : this.series.get(0).getData()) {
			this.getLegend().add(data.getName());
		} // for
	}
	
	public PieModel(String name, IDataSet data) {
		this(name, new Title("CGOR", "subtitulo"), data);
	}
	
	public PieModel(String name, Title title, IDataSet data) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList(SERIES_COLORS)), new ToolTip(), data.getDatas());
	}
	
	public PieModel(Title title, Legend legend, List<String> color, ToolTip tooltip, List<Serie> series) {
		super(Axis.COLOR_WHITE);
		this.title=title;
		this.legend=legend;
		this.color=color;
		this.tooltip=tooltip;
		this.series=series;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title=title;
	}

	public Legend getLegend() {
		return legend;
	}

	public void setLegend(Legend legend) {
		this.legend=legend;
	}

	public List<String> getColor() {
		return color;
	}

	public void setColor(List<String> color) {
		this.color=color;
	}

	public ToolTip getTooltip() {
		return tooltip;
	}

	public void setTooltip(ToolTip tooltip) {
		this.tooltip=tooltip;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series=series;
	}

  public String toJson() throws Exception {
	  return Decoder.toJson(this);	
	}	

	@Override
	public String toString() {
		return "PieModel{"+"title="+title+", legend="+legend+", color="+color+", tooltip="+tooltip+", series="+series+'}';
	}

}

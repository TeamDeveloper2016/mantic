package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Axis;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.pie.Serie;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.model.IDataSet;
import mx.org.kaana.libs.echarts.pie.Data;
import mx.org.kaana.libs.json.Decoder;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:20:56 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class PieModel extends ChartModel implements Serializable {

	private static final long serialVersionUID=3783050684207305584L;
	
	private Title title;
	private Legend legend;
	private List<String> color;
	private ToolTip tooltip;
	private List<Serie> series;
	private transient String radius;

	public PieModel(String name) {
		this(name, "55%", new Title("CGOR", "subtitulo"));
	}

	public PieModel(String name, String radius, Title title) {
		this(name, radius, title, new Legend("2019"), new ArrayList(), new ToolTip(), new ArrayList<Serie>());
		this.series.add(new Serie(name, radius));
		this.legend.getData().clear();
		for (Data data: this.series.get(0).getData()) {
			this.legend.add(data.getName());
			this.color.add(Colors.toColor());
		} // for
		this.radius= radius;
	}
	
	public PieModel(String name, String radius, IDataSet data) {
		this(name, radius, new Title("CGOR", "subtitulo"), data);
	}
	
	public PieModel(String name, String radius, Title title, IDataSet data) {
		this(name, radius, title, data.getLegend(), new ArrayList(), new ToolTip(), data.getDatas());
	}
	
	public PieModel(String name, String radius, Title title, Legend legend, List<String> color, ToolTip tooltip, List<Serie> series) {
		super(Axis.COLOR_WHITE);
		this.title=title;
		this.legend=legend;
		this.color=color;
		this.tooltip=tooltip;
		this.series=series;
		this.radius= radius;
		if(series!= null && !series.isEmpty()) {
			for (Serie item: series) {
				item.setName(name);
				item.setRadius(Arrays.asList("0%", this.radius));
				item.getLabel().getNormal().setFontSize(14);
			} // for
			for (Data item: this.series.get(0).getData()) {
				this.color.add(Colors.toColor());
			} // for
		} // if
		this.tooltip.setTrigger("item");
		this.tooltip.setAxisPointer(null);
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
	  return StringEscapeUtils.unescapeJava(Decoder.json(this));	
	}	

	@Override
	public String toString() {
		return "PieModel{"+"title="+title+", legend="+legend+", color="+color+", tooltip="+tooltip+", series="+series+'}';
	}
	
	public void toCustomFormatLabel(String format) {
		for (Serie item: this.series) {
			item.getLabel().getNormal().setFormatter(format);
		} // for
	}
	
}

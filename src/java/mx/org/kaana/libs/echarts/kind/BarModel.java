package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Axis;
import mx.org.kaana.libs.echarts.beans.Grid;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Serie;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.beans.Xaxis;
import mx.org.kaana.libs.echarts.beans.Yaxis;
import mx.org.kaana.libs.echarts.enums.EBarOritentation;
import mx.org.kaana.libs.echarts.model.IDataSet;
import mx.org.kaana.libs.json.Decoder;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:47:13 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class BarModel implements Serializable {

	private static final long serialVersionUID=-4271194453055348485L;

	private Title title;
	private Legend legend;
	private List<String> color;
	private ToolTip tooltip;
	private Grid grid;
	private Axis xAxis;
	private Axis yAxis;
	private List<Serie> series;
	private transient EBarOritentation orientation;

	public BarModel() {
		this(new Title("CGOR", "Subtitulo"), EBarOritentation.VERTICAL);
	}
	
	public BarModel(Title title) {
		this(title, EBarOritentation.VERTICAL);
	}

	public BarModel(Title title, IDataSet data) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList("#0080FF", "#80BFFF", "#3398AA", "#0059B3", "#001A33")), new ToolTip(), new Grid(), data.getXaxis(), new Yaxis(), data.getDataset(), EBarOritentation.VERTICAL);
	}

	public BarModel(Title title, EBarOritentation orientation) {
		this(title, new Legend(Arrays.asList("2019")), Arrays.asList("#3398AA"), new ToolTip(), new Grid(), new Xaxis(), new Yaxis(), Arrays.asList(new Serie()), orientation);
	}
	
	public BarModel(Title title, IDataSet data, EBarOritentation orientation) {
		this(title, data.getLegend(), Arrays.asList("#3398AA"), new ToolTip(), new Grid(), 
			EBarOritentation.VERTICAL.equals(orientation)? data.getXaxis(): new Yaxis(), 
			EBarOritentation.VERTICAL.equals(orientation)? new Yaxis(): data.getXaxis(), 
			data.getDataset(), orientation);
	}

	public BarModel(List<String> color, ToolTip tooltip, Axis xAxis, Axis yAxis, List<Serie> series) {
		this(color, tooltip, xAxis, yAxis, series, EBarOritentation.VERTICAL);
	}
	
	public BarModel(List<String> color, ToolTip tooltip, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation) {
		this(null, null, color, tooltip, new Grid(), xAxis, yAxis, series, orientation);
	}
	
	public BarModel(Title title, Legend legend, List<String> color, ToolTip tooltip, Grid grid, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation) {
		this.title=title;
		this.legend=legend;
		this.color=color;
		this.tooltip=tooltip;
		this.grid=grid;
		this.orientation= orientation;
	  if(EBarOritentation.VERTICAL.equals(this.orientation)) {
			this.xAxis=xAxis;
			this.yAxis=yAxis;
		} // if 
		else {
			this.xAxis=yAxis;
			this.yAxis=xAxis;
		} // else
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

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid=grid;
	}

	public Axis getxAxis() {
		return xAxis;
	}

	public void setxAxis(Xaxis xAxis) {
		this.xAxis=xAxis;
	}

	public Axis getyAxis() {
		return yAxis;
	}

	public void setyAxis(Yaxis yAxis) {
		this.yAxis=yAxis;
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
		return "BarModel{"+"color="+color+", tooltip="+tooltip+", grid="+grid+", xAxis="+xAxis+", yAxis="+yAxis+", series="+series+'}';
	}
	
}

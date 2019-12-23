package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.AxisPointer;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.Grid;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.enums.EBarOritentation;
import mx.org.kaana.libs.echarts.model.IDataSet;
import mx.org.kaana.libs.echarts.pic.Data;
import mx.org.kaana.libs.echarts.pic.Serie;
import mx.org.kaana.libs.echarts.pic.Xaxis;
import mx.org.kaana.libs.echarts.pic.Yaxis;
import mx.org.kaana.libs.json.Decoder;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 12:01:25 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class PicModel extends ChartModel implements Serializable {

	private static final long serialVersionUID=-4271194453055348482L;
	 
	private Title title;
	private Legend legend;
	private ToolTip tooltip;
	private List<String> color;
	private Grid grid;
	private Xaxis xAxis;
	private Yaxis yAxis;
	private List<Serie> series;
	private transient EBarOritentation orientation;
	
	public PicModel() {
		this(new Title("CGOR", "Sub titulo"), new ArrayList<Serie>(Arrays.asList(new Serie())));
	};

	public PicModel(Title title, IDataSet data) {
		this(title, data.getLegend(), data.getPictorial());
	}
	
	public PicModel(Title title, List<Serie> series) {
		this(title, new Legend(null, null, null, new ArrayList<>()), series);
	}
	
	public PicModel(Title title, Legend legend, List<Serie> series) {
		this.orientation= EBarOritentation.HORIZONTAL;
		this.title=title;
		this.series=series;
		this.legend= legend;
		this.getLegend().setOrient(null);
		this.getLegend().setLeft(null);
		this.tooltip= new ToolTip("axis", new AxisPointer("shadow"), "{a} <br/>{b}: {c}");
		this.grid= new Grid("30");
		this.color= new ArrayList();
		this.xAxis= new Xaxis();
		this.yAxis= new Yaxis();
		this.init();
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

	public ToolTip getTooltip() {
		return tooltip;
	}

	public void setTooltip(ToolTip tooltip) {
		this.tooltip=tooltip;
	}

	public List<String> getColor() {
		return color;
	}

	public void setColor(List<String> color) {
		this.color=color;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid=grid;
	}

	public Xaxis getxAxis() {
		return xAxis;
	}

	public void setxAxis(Xaxis xAxis) {
		this.xAxis=xAxis;
	}

	public Yaxis getyAxis() {
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

	public EBarOritentation getOrientation() {
		return orientation;
	}

	public void setOrientation(EBarOritentation orientation) {
		this.orientation=orientation;
	}
	
	private void init() {
		boolean umbral= false;
		int count= 0;
		for (Serie item: this.getSeries()) {
			String color= Colors.toColor();
			this.getColor().add(color);
			if(count> 0)
				item.setBarCategoryGap(null);
			for (Data data: item.getData()) {
				if(EBarOritentation.HORIZONTAL.equals(this.orientation))
					this.getyAxis().getData().add(data.getName());
				else
					this.getyAxis().getData().add(data.getName());
				data.setColor(color);
				if(data.getValue()> 100)
					umbral= Boolean.TRUE;
			} // for
			count++;
		} // for
		if(umbral)
			if(EBarOritentation.HORIZONTAL.equals(this.orientation))
				this.getxAxis().setMinMax(null, null);
			else
				this.getyAxis().setMinMax(null, null);
	}

	@Override
	public String toString() {
		return "PicModel{"+"title="+title+", legend="+legend+", tooltip="+tooltip+", color="+color+", grid="+grid+", xAxis="+xAxis+", yAxis="+yAxis+", series="+series+", orientation="+orientation+'}';
	}
	
	public String toJson() throws Exception {
	  return StringEscapeUtils.unescapeJava(Decoder.json(this));	
	}	

	public void toCustomFormatLabel(String format) {
		for (Serie item : this.series) {
			item.getLabel().getNormal().setFormatter(format);
		} // for
	}
	
	public void toCustomColorLabel(String color) {
		for (Serie item : this.series) {
			item.getLabel().getNormal().setColor(color);
		} // for
	}	
	
  public void toCustomLabel(String color, Integer size) {
		for(Serie item: this.series) {
			item.getLabel().getNormal().setColor(color);
			item.getLabel().getNormal().setFontSize(size);
		} // for
	}
	
}

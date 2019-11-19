package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Axis;
import mx.org.kaana.libs.echarts.beans.Grid;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.bar.Serie;
import mx.org.kaana.libs.echarts.bar.Value;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.IMarkLine;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.beans.Xaxis;
import mx.org.kaana.libs.echarts.beans.Yaxis;
import mx.org.kaana.libs.echarts.enums.EBarOritentation;
import mx.org.kaana.libs.echarts.model.IDataSet;
import mx.org.kaana.libs.echarts.model.SortNames;
import mx.org.kaana.libs.json.Decoder;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:47:13 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class BarModel extends BaseBarModel implements Serializable {

	private static final long serialVersionUID=-4271194453055348485L;

	private List<Serie> series;
	private List<String> sequence;

	public BarModel() {
		this(new Title("CGOR", "Subtitulo"), EBarOritentation.VERTICAL);
	}
	
	public BarModel(Title title) {
		this(title, EBarOritentation.VERTICAL);
	}

	public BarModel(Title title, IDataSet data) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), data.getXaxis(), new Yaxis(), data.getSeries(), EBarOritentation.VERTICAL);
	}

	public BarModel(Title title, IDataSet data, List<String> sequence) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), data.getXaxis(), new Yaxis(), data.getSeries(), EBarOritentation.VERTICAL, sequence);
	}

	public BarModel(Title title, EBarOritentation orientation) {
		this(title, new Legend("2019"), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), new Xaxis(), new Yaxis(), new ArrayList(Arrays.asList(new Serie())), orientation);
	}
	
	public BarModel(Title title, IDataSet data, EBarOritentation orientation) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), data.getXaxis(), new Yaxis(), data.getSeries(), orientation);
	}

	public BarModel(List<String> color, ToolTip tooltip, Axis xAxis, Axis yAxis, List<Serie> series) {
		this(color, tooltip, xAxis, yAxis, series, EBarOritentation.VERTICAL);
	}
	
	public BarModel(List<String> color, ToolTip tooltip, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation) {
		this(null, null, color, tooltip, new Grid(), xAxis, yAxis, series, orientation);
	}
	
	public BarModel(Title title, Legend legend, List<String> color, ToolTip tooltip, Grid grid, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation) {
		this(title, legend, color, tooltip, grid, xAxis, yAxis, series, orientation, xAxis.getData());
	}
	
	public BarModel(Title title, Legend legend, List<String> color, ToolTip tooltip, Grid grid, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation, List<String> sequence) {
		super(title, legend, color, tooltip, grid, xAxis, yAxis, orientation);
		this.series=series;
		this.sequence=sequence;
		this.loadColors();
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
	
	public void addLine(IMarkLine line) {
		if(this.series!= null && !this.series.isEmpty())
			if(this.series.get(0).getMarkLine().getData()!= null)
		    this.series.get(0).getMarkLine().getData().add(line);
	}
	
	@Override
	public String toString() {
		return "BarModel{series="+series+'}';
	}

	private void loadColors() {
	  super.getColor().clear();
		for (Serie item : this.series) {
			item.getLabel().getNormal().setFormatter("{a}\\n{c}");
			super.getColor().add(item.getData().get(0).getItemStyle().getColor());
		} // for
		this.ordered();
	}

	private void ordered() {
		if(EBarOritentation.VERTICAL.equals(this.getOrientation()))
		  this.getxAxis().setData(SortNames.toSort(this.getxAxis().getData(), this.sequence));
		else
		  this.getyAxis().setData(SortNames.toSort(this.getyAxis().getData(), this.sequence));
		this.sort(this.sequence);
	}
	
	public void sort(final List<String> labels) {
		for (Serie item: this.series) {
		  List<Value> values= new ArrayList<>();
			for (String name: labels) {
				int index= item.getData().indexOf(new Value(name));
				if(index>= 0)
					values.add(item.getData().get(index));
			} // for
			item.getData().clear();
			item.setData(values);
		} // for
	}
	
	public void sort() {
		this.sort(EBarOritentation.VERTICAL.equals(this.getOrientation())? this.getxAxis().getData(): this.getyAxis().getData());
	}

	public void sort(final String[] names) {
		this.sequence= Arrays.asList(names);
	  this.ordered();
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
	
	public void toCustomFontSize(Integer size) {
		for (Serie item : this.series) {
			item.getLabel().getNormal().setFontSize(size);
			if(item.getMarkLine()!= null)
			  item.getMarkLine().getLabel().getNormal().setFontSize(size);
		} // for
	}
	
	public void toCustomLabel(String color, Integer size) {
		for (Serie item: this.series) {
			item.getLabel().getNormal().setColor(color);
			item.getLabel().getNormal().setFontSize(size);
			if(item.getMarkLine()!= null)
			  item.getMarkLine().getLabel().getNormal().setFontSize(size);
		} // for
	}
	
	public void removeMarks() {
		for (Serie item : this.series) {
			item.setMarkPoint(null);
		} // for
	}
	
}

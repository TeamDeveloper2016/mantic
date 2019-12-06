package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.bar.Value;
import mx.org.kaana.libs.echarts.stack.Serie;
import mx.org.kaana.libs.echarts.beans.Axis;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.Grid;
import mx.org.kaana.libs.echarts.beans.IMarkLine;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Normal;
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

public class StackModel extends BaseBarModel implements Serializable {

	private static final long serialVersionUID=-2335254501339126952L;

	private List<Serie> series;
	private List<String> sequence;
	
	public StackModel() {
		this(new Title("CGOR", "Subtitulo"), EBarOritentation.VERTICAL);
	}
	
	public StackModel(Title title) {
		this(title, EBarOritentation.VERTICAL);
	}

	public StackModel(Title title, IDataSet data) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), data.getXaxis(), new Yaxis(), data.getStack(), EBarOritentation.VERTICAL);
	}

	public StackModel(Title title, IDataSet data, List<String> sequence) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), data.getXaxis(), new Yaxis(), data.getStack(), EBarOritentation.VERTICAL, sequence);
	}

	public StackModel(Title title, EBarOritentation orientation) {
		this(title, new Legend("2019"), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), new Xaxis(), new Yaxis(), new ArrayList(Arrays.asList(new Serie("2019", Colors.toColor()), new Serie("2020", Colors.toColor()))), orientation);
		this.getLegend().add("2020");
	}
	
	public StackModel(Title title, IDataSet data, EBarOritentation orientation) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), data.getXaxis(), new Yaxis(), data.getStack(), orientation);
	}

	public StackModel(List<String> color, ToolTip tooltip, Axis xAxis, Axis yAxis, List<Serie> series) {
		this(color, tooltip, xAxis, yAxis, series, EBarOritentation.VERTICAL);
	}
	
	public StackModel(List<String> color, ToolTip tooltip, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation) {
		this(null, null, color, tooltip, new Grid(), xAxis, yAxis, series, orientation);
	}
	
	public StackModel(Title title, Legend legend, List<String> color, ToolTip tooltip, Grid grid, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation) {
    this(title, legend, color, tooltip, grid, xAxis, yAxis, series, orientation, xAxis.getData());
	}

	public StackModel(Title title, Legend legend, List<String> color, ToolTip tooltip, Grid grid, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation, List<String> sequence) {
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
		return "StackModel{"+"series="+series+'}';
	}

	private void loadColors() {
	  super.getColor().clear();
		for (Serie item : this.series) {
			String color= item.getData().get(0).getItemStyle().getColor();
			item.getLabel().getNormal().setFormatter("{a}\\n{c}");
			item.getLabel().getNormal().setPosition("inside");
			super.getColor().add(color);
			for (String element: this.getxAxis().getData()) {
				if(!item.getData().contains(new Value(element)))
					item.getData().add(new Value(element, 0D, color));
			} // for
		} // for
		this.getxAxis().setData(SortNames.toSort(this.getxAxis().getData(), this.sequence));
		this.sort(this.sequence);
		if(this.series!= null && !this.series.isEmpty()) {
			try {
				Serie serie= this.series.get(this.series.size()- 1).clone();
				int count= 0;
				serie.setName("Total");
				serie.setData(new ArrayList<>());
				for (Value item: this.series.get(this.series.size()- 1).getData()) {
					serie.getData().add(new Value("CGOR:"+ this.calculate(count), 0.01D));
					count++;
				} // for
				serie.getLabel().getNormal().setPosition("top");
				this.series.add(serie);
			} // try
			catch(Exception e) {
				mx.org.kaana.libs.formato.Error.mensaje(e);
			} // catch
		} // if	
	}
	
	private Double calculate(int index) {
		Double regresar= 0D;
    for (Serie item: this.series) {
			regresar+= item.getData().get(index).getValue();
		}	// for	
		return regresar;
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
		this.sort(this.getxAxis().getData());
	}

	public void sort(final String[] names) {
	 this.getxAxis().setData(SortNames.toSort(this.getxAxis().getData(), names));	
	 this.sort();
	}

	public void toCustomFormatLabel(String format) {
		for (Serie item : this.series) {
			item.getLabel().getNormal().setFormatter(format);
		} // for
	}

	public void toCustomFontSize(Integer size) {
		for (Serie item: this.series) {
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
		for (mx.org.kaana.libs.echarts.bar.Serie item : this.series) {
			item.setMarkPoint(null);
		} // for
	}

}

package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.stack.Serie;
import mx.org.kaana.libs.echarts.beans.Axis;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.Grid;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.beans.Xaxis;
import mx.org.kaana.libs.echarts.beans.Yaxis;
import mx.org.kaana.libs.echarts.enums.EBarOritentation;
import mx.org.kaana.libs.echarts.model.IDataSet;
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
	
	public StackModel() {
		this(new Title("CGOR", "Subtitulo"), EBarOritentation.VERTICAL);
	}
	
	public StackModel(Title title) {
		this(title, EBarOritentation.VERTICAL);
	}

	public StackModel(Title title, IDataSet data) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), data.getXaxis(), new Yaxis(), data.getStack(), EBarOritentation.VERTICAL);
	}

	public StackModel(Title title, EBarOritentation orientation) {
		this(title, new Legend("2019"), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), new Xaxis(), new Yaxis(), new ArrayList(Arrays.asList(new Serie())), orientation);
	}
	
	public StackModel(Title title, IDataSet data, EBarOritentation orientation) {
		this(title, data.getLegend(), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new Grid(), 
			data.getXaxis(), new Yaxis(), data.getStack(), orientation);
	}

	public StackModel(List<String> color, ToolTip tooltip, Axis xAxis, Axis yAxis, List<Serie> series) {
		this(color, tooltip, xAxis, yAxis, series, EBarOritentation.VERTICAL);
	}
	
	public StackModel(List<String> color, ToolTip tooltip, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation) {
		this(null, null, color, tooltip, new Grid(), xAxis, yAxis, series, orientation);
	}
	
	public StackModel(Title title, Legend legend, List<String> color, ToolTip tooltip, Grid grid, Axis xAxis, Axis yAxis, List<Serie> series, EBarOritentation orientation) {
		super(title, legend, color, tooltip, grid, xAxis, yAxis, orientation);
		this.series=series;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series=series;
	}

  public String toJson() throws Exception {
	  return StringEscapeUtils.unescapeJava(Decoder.toJson(this));	
	}	
	
	@Override
	public String toString() {
		return "StackModel{"+"series="+series+'}';
	}

}
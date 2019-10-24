package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Axis;
import mx.org.kaana.libs.echarts.beans.Grid;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.beans.Xaxis;
import mx.org.kaana.libs.echarts.beans.Yaxis;
import mx.org.kaana.libs.echarts.enums.EBarOritentation;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:47:13 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class BaseBarModel extends ChartModel implements Serializable {

	private static final long serialVersionUID=1747946676353409638L;
	
	private Title title;
	private Legend legend;
	private List<String> color;
	private ToolTip tooltip;
	private Grid grid;
	private Axis xAxis;
	private Axis yAxis;
	private transient EBarOritentation orientation;

	public BaseBarModel(Title title, Legend legend, List<String> color, ToolTip tooltip, Grid grid, Axis xAxis, Axis yAxis, EBarOritentation orientation) {
		super(Axis.COLOR_WHITE);
		this.title=title;
		this.legend=legend;
		this.color=color;
		this.tooltip=tooltip;
		this.tooltip.setFormatter(null);
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

	public EBarOritentation getOrientation() {
		return orientation;
	}

	public void setOrientation(EBarOritentation orientation) {
		this.orientation=orientation;
	}

	@Override
	public String toString() {
		return "BaseBarModel{"+"title="+title+", legend="+legend+", color="+color+", tooltip="+tooltip+", grid="+grid+", xAxis="+xAxis+", yAxis="+yAxis+", orientation="+orientation+'}';
	}
	
}

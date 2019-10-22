package mx.org.kaana.libs.echarts.bar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Coordinate;
import mx.org.kaana.libs.echarts.beans.Label;
import mx.org.kaana.libs.echarts.beans.MarkLine;
import mx.org.kaana.libs.echarts.beans.MarkPoint;
import mx.org.kaana.libs.echarts.enums.EKinds;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:49:14 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Serie extends mx.org.kaana.libs.echarts.beans.Serie implements Serializable {

	private static final long serialVersionUID=71577914809278916L;

  private String barWidth;	
	private List<Value> data;
  private MarkPoint markPoint;
  private MarkLine markLine;
	private Integer barGap; 
	private Integer barMaxWidth;
	private Label label;

	public Serie() {
		this("Serie");
	}

	public Serie(String name) {
		this(name, null, new ArrayList(Arrays.asList(new Value(120D), new Value(200D), new Value(150D), new Value(80D), new Value(70D), new Value(110D), new Value(130D))), EKinds.BAR);
	}
	
	public Serie(String name, String barWidth, List<Value> data, EKinds type) {
		super(name, type.toString());
		this.barWidth=barWidth;
		this.data=data;
		this.markPoint=new MarkPoint();
		this.markLine=new MarkLine(name);
		this.barGap=0;
		this.barMaxWidth=60;
		this.label=new Label();
	}

	public String getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(String barWidth) {
		this.barWidth=barWidth;
	}

	public List<Value> getData() {
		return data;
	}

	public void setData(List<Value> data) {
		this.data=data;
	}

	public MarkPoint getMarkPoint() {
		return markPoint;
	}

	public void setMarkPoint(MarkPoint markPoint) {
		this.markPoint=markPoint;
	}

	public MarkLine getMarkLine() {
		return markLine;
	}

	public void setMarkLine(MarkLine markLine) {
		this.markLine=markLine;
	}

	public Integer getBarGap() {
		return barGap;
	}

	public void setBarGap(Integer barGap) {
		this.barGap=barGap;
	}

	public Integer getBarMaxWidth() {
		return barMaxWidth;
	}

	public void setBarMaxWidth(Integer barMaxWidth) {
		this.barMaxWidth=barMaxWidth;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label=label;
	}

	@Override
	public String toString() {
		return "Serie{"+"barWidth="+barWidth+", data="+data+", markPoint="+markPoint+", markLine="+markLine+", barGap="+barGap+", barMaxWidth="+barMaxWidth+", label="+label+'}';
	}

	public void addLine(Coordinate coordinate) {
		this.getMarkLine().getData().add(coordinate);
	}
		
}

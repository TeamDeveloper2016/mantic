package mx.org.kaana.libs.echarts.bar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.IMarkLine;
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
	private String barCategoryGap; 
	private Label label;

	public Serie() {
		this("Serie");
	}

	public Serie(String name) {
		this(name, Colors.toColor());
	}
	
	public Serie(String name, String color) {
		this(name, null, new ArrayList(Arrays.asList(new Value("Lun", toValue(), color), new Value("Mar", toValue(), color), new Value("Mie", toValue(), color), new Value("Jue", toValue(), color), new Value("Vie", toValue(), color), new Value("Sab", toValue(), color), new Value("Dom", toValue(), color))), EKinds.BAR);
	}
	
	public Serie(String name, String barWidth, List<Value> data, EKinds type) {
		super(name, type.toString());
		this.barWidth=barWidth;
		this.data=data;
		this.markPoint=new MarkPoint();
		this.markLine=new MarkLine(name);
		this.barCategoryGap="10%";
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

	public String getBarCategoryGap() {
		return barCategoryGap;
	}

	public void setBarCategoryGap(String barCategoryGap) {
		this.barCategoryGap=barCategoryGap;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label=label;
	}

	@Override
	public String toString() {
		return "Serie{"+"barWidth="+barWidth+", data="+data+", markPoint="+markPoint+", markLine="+markLine+", barCategoryGap="+barCategoryGap+", label="+label+'}';
	}

	public void addLine(IMarkLine coordinate) {
		this.getMarkLine().getData().add(coordinate);
	}
		
}

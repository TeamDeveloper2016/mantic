package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import mx.org.kaana.libs.echarts.enums.ETypeLine;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/10/2019
 *@time 01:30:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class CustomLine implements IMarkLine, Serializable {

	private static final long serialVersionUID=-8197395881452075986L;

	private String name;
	private Double yAxis;
	private LineStyle lineStyle;

	public CustomLine(String name, Double yAxis) {
		this(name, yAxis, Colors.COLOR_BLACK);
	}

	public CustomLine(String name, Double yAxis, String color) {
		this(name, yAxis, new LineStyle(color));
	}

	public CustomLine(String name, Double yAxis, String color, ETypeLine type) {
		this(name, yAxis, new LineStyle(color, type));
	}

	public CustomLine(String name, Double yAxis, LineStyle lineStyle) {
		this.name=name;
		this.yAxis=yAxis;
		this.lineStyle=lineStyle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public Double getyAxis() {
		return yAxis;
	}

	public void setyAxis(Double yAxis) {
		this.yAxis=yAxis;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle=lineStyle;
	}

	@Override
	public String toString() {
		return "CustomLine{"+"name="+name+", yAxis="+yAxis+", lineStyle="+lineStyle+'}';
	}
	
}

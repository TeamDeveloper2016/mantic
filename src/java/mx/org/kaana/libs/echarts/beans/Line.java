package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.echarts.enums.ETypeLine;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/10/2019
 *@time 02:14:23 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Line implements Serializable {

	private static final long serialVersionUID=-6283466476453309041L;

	private String name;
	private List<Integer> coord;
	private LineStyle lineStyle;

	public Line(String name, Integer x, Integer y) {
		this(name, x, y, Colors.COLOR_BLACK);
	}
	
	public Line(String name, Integer x, Integer y, String color) {
		this(name, x, y, new LineStyle(color, ETypeLine.DASHED));
	}
	
	public Line(String name, Integer x, Integer y, String color, ETypeLine type) {
		this(name, x, y, new LineStyle(color, type));
	}
	
	public Line(String name, Integer x, Integer y, LineStyle lineStyle) {
		this.name=name;
		this.coord= new ArrayList<>();
		this.coord.add(x);
		this.coord.add(y);
		this.lineStyle= lineStyle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public List<Integer> getCoord() {
		return coord;
	}

	public void setCoord(List<Integer> coord) {
		this.coord=coord;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle=lineStyle;
	}

	@Override
	public String toString() {
		return "Line{"+"name="+name+", coord="+coord+", lineStyle="+lineStyle+'}';
	}
	
}

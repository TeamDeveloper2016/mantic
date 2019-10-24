package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import mx.org.kaana.libs.echarts.enums.ETypeLine;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/10/2019
 *@time 02:14:23 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Coordinate extends ArrayList<Line> implements IMarkLine, Serializable {

	private static final long serialVersionUID=8411339112762406498L;

	public Coordinate(String name, Integer x, Integer y) {
	  this(name, x, y, Colors.COLOR_BLACK);
	}
	
	public Coordinate(String name, Integer x, Integer y, String color) {
		this.add(new Line(name, 0, y, color));
		this.add(new Line(name, x, y, color));
	}
		
	public Coordinate(String name, Integer x, Integer y, String color, ETypeLine type) {
		this.add(new Line(name, 0, y, color, type));
		this.add(new Line(name, x, y, color, type));
	}
		
}

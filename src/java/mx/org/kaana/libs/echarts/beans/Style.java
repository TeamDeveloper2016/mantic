package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import mx.org.kaana.libs.echarts.enums.ETypeLine;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/10/2019
 *@time 02:14:23 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Style implements Serializable {

	private static final long serialVersionUID=-6283466476453309041L;

	private String color;
	private String type;

	public Style() {
		this(Colors.COLOR_BLACK);
	}
	
	public Style(String color) {
		this(color, ETypeLine.DASHED.toName());
	}

	public Style(String color, String type) {
		this.color=color;
		this.type=type; 
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color=color;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	@Override
	public String toString() {
		return "Style{"+"color="+color+", type="+type+'}';
	}
		
}

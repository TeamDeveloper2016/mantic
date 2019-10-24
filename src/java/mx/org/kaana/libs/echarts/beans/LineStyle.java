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

public class LineStyle implements Serializable {

	private static final long serialVersionUID=-6283466476453309041L;

	private Style normal;

	public LineStyle(String color) {
		this(new Style(color));
	}
	
	public LineStyle(String color, ETypeLine type) {
		this(new Style(color, type.toName()));
	}

	public LineStyle(Style normal) {
		this.normal=normal;
	}

	public Style getNormal() {
		return normal;
	}

	public void setNormal(Style normal) {
		this.normal=normal;
	}

	@Override
	public String toString() {
		return "LineStyle{"+"normal="+normal+'}';
	}
		
}

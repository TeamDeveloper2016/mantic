package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 11:14:06 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AxisLabel implements Serializable {

	private static final long serialVersionUID=-3694091918311484602L;

	private Boolean inside; 
	private TextStyle textStyle;

	public AxisLabel() {
		this(false, new TextStyle());
	}

	public AxisLabel(Boolean inside, TextStyle textStyle) {
		this.inside=inside;
		this.textStyle=textStyle;
	}

	public Boolean getInside() {
		return inside;
	}

	public void setInside(Boolean inside) {
		this.inside=inside;
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle=textStyle;
	}

	@Override
	public String toString() {
		return "AxisLabel{"+"inside="+inside+", textStyle="+textStyle+'}';
	}
	
}

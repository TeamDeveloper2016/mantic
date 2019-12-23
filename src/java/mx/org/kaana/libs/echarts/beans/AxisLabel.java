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
	private String formatter;
	private Integer margin;

	public AxisLabel() {
		this(Boolean.FALSE, new TextStyle());
	}

	public AxisLabel(Boolean inside, TextStyle textStyle) {
		this(inside, textStyle, null);
	}
	
	public AxisLabel(Boolean inside, TextStyle textStyle, String formatter) {
		this(inside, textStyle, formatter, 30);
	}

	public AxisLabel(TextStyle textStyle) {
		this(Boolean.FALSE, textStyle, null, 30);
	}

	public AxisLabel(Boolean inside, TextStyle textStyle, String formatter, Integer margin) {
		this.inside=inside;
		this.textStyle=textStyle;
		this.formatter=formatter;
		this.margin=margin;
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

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter=formatter;
	}

	public void setMargin(Integer margin) {
		this.margin=margin;
	}

	public Integer getMargin() {
		return margin;
	}

	@Override
	public String toString() {
		return "AxisLabel{"+"inside="+inside+", textStyle="+textStyle+", formatter="+formatter+", margin="+margin+'}';
	}
	
}

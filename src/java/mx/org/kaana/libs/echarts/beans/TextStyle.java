package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 11:15:22 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class TextStyle implements Serializable {

	private static final long serialVersionUID=-6049969544415495534L;
	
	private String color;
	private Integer fontSize;

	public TextStyle() {
		this(Axis.COLOR_BLACK);
	}

	public TextStyle(Integer fontSize) {
		this.fontSize=fontSize;
	}
	
	public TextStyle(String color) {
		this.color=color;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color=color;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize=fontSize;
	}

	@Override
	public String toString() {
		return "TextStyle{"+"color="+color+", fontSize="+fontSize+'}';
	}
	
}

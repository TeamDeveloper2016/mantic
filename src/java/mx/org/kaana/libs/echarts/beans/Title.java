package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:33:37 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Title implements Serializable {

	private static final long serialVersionUID=-7676319841327166567L;

	private String text;
	private String subtext;
	private String left;
	private Integer top;
	private TextStyle textStyle;

	public Title() {
		this(null, null);
	}

	public Title(String text, String subtext) {
		this.text=text;
		this.subtext=subtext;
		this.top=10;
		this.left="right";
		this.textStyle= new TextStyle(Axis.COLOR_BLACK);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text=text;
	}

	public String getSubtext() {
		return subtext;
	}

	public void setSubtext(String subtext) {
		this.subtext=subtext;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left=left;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top=top;
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle=textStyle;
	}

	@Override
	public String toString() {
		return "Title{"+"text="+text+", subtext="+subtext+'}';
	}
	
}

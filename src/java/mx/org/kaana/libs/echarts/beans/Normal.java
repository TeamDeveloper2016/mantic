package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 18/09/2019
 *@time 17:11:48 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Normal implements Serializable, Cloneable {

	private static final long serialVersionUID=-7749442518672455859L;

	private String color;
	private String fontFamily;
	private Integer fontSize;
	private Boolean show;
	private String position; // insideRigth, insideLeft, inside
	private String formatter;

/*	formatter
{a}: series name.
{b}: the name of a data item.
{c}: the value of a data item.
{d}: the percent.
{@xxx}: the value of a dimension named'xxx', for example,{@product}refers the value of'product'` dimension.
{@[n]}: the value of a dimension at the index ofn, for example,{@[3]}` refers the value at dimensions[3].	
	
	*/
	public Normal() {
		this("outside"); // inside
	}

	public Normal(String position) {
		this(position, "{b}\\n{c}");
	}

	public Normal(String position, String formatter) {
		this.position=position;
		this.formatter=formatter;
		this.fontFamily="Roboto, sans-serif";
    this.fontSize=19;
		this.show=true;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color=color;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show=show;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position=position;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily=fontFamily;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize=fontSize;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter=formatter;
	}

	@Override
	public String toString() {
		return "Normal{"+"color="+color+", fontFamily="+fontFamily+", fontSize="+fontSize+", show="+show+", position="+position+", formatter="+formatter+'}';
	}

	@Override
	public Normal clone() {
		Object object=null;
		try {
			object= super.clone();
		} // try
		catch (CloneNotSupportedException e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} //catch
		return (Normal)object;
	}
	
}

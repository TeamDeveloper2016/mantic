package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:20:56 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Children implements Serializable {

	private static final long serialVersionUID=-6133631419950937122L;

	private String type;
	private Integer z;
	private String left;
	private String top;
	private Shape shape;
	private Design style;

	public Children() {
		this("rect", 100, "center", "middle", new Shape(), new Design());
	}

	public Children(String text) {
	  this(text, "14px Microsoft YaHei");
	}
	
	public Children(String text, String font) {
		this("text", 100, "center", "middle", null, new Design(text, font));
	}
	
	public Children(String type, Integer z, String left, String top, Shape shape, Design style) {
		this.type=type;
		this.z=z;
		this.left=left;
		this.top=top;
		this.shape=shape;
		this.style=style;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	public Integer getZ() {
		return z;
	}

	public void setZ(Integer z) {
		this.z=z;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left=left;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top=top;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape=shape;
	}

	public Design getStyle() {
		return style;
	}

	public void setStyle(Design style) {
		this.style=style;
	}

	@Override
	public String toString() {
		return "Children{"+"type="+type+", z="+z+", left="+left+", top="+top+", shape="+shape+", style="+style+'}';
	}
	
}

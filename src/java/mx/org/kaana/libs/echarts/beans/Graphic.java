package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:20:56 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Graphic implements Serializable {

	private static final long serialVersionUID=6373327602231029589L;

	private String type;
	private String left;
	private String top;
	private List<Children> children;

	public Graphic() {
		this("");
	}

	public Graphic(String text) {
		this("50%", "50%", text);
	}
	
	public Graphic(String left, String top, String text) {
		this(left, top, text, null);
	}
	
	public Graphic(String left, String top, String text, String font) {
		this.type="group";
		this.left=left;
		this.top=top;
		this.children= new ArrayList<>();
		this.children.add(new Children(text));
    this.children.get(0).getStyle().setFont(font);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
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

	public List<Children> getChildren() {
		return children;
	}

	public void setChildren(List<Children> children) {
		this.children=children;
	}

	@Override
	public String toString() {
		return "Graphic{"+"type="+type+", left="+left+", top="+top+", children="+children+'}';
	}
	
}

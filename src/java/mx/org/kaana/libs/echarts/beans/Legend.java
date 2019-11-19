package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:36:05 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Legend implements Serializable {
 
	private static final long serialVersionUID=7980019871750859771L;
 
	private String type;
	private transient String orient;
	private String x;
	private String y;
	private String left;
	private List<String> data;
	private String formatter;

	public Legend() {
		this("scroll", "center", "bottom", new ArrayList<>());
	}

	public Legend(String data) {
		this("scroll", "center", "bottom", new ArrayList(Arrays.asList(data)));
	}

	public Legend(String orient, String left, List<String> data) {
		this("scroll", "center", "bottom", new ArrayList(Arrays.asList(data)));
		this.orient=orient;
		this.left=left;
	}

	public Legend(String type, String x, String y, List<String> data) {
		this.orient="vertical";
		this.left="center";
		this.type=type;
		this.x=x;
		this.y=y;
		this.data=data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	public String getOrient() {
		return orient;
	}

	public void setOrient(String orient) {
		this.orient=orient;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x=x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y=y;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left=left;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter=formatter;
	}

	public List<String> getData() {
		return data;
	}

	public void add(String name) {
		this.data.add(name);
	}

	@Override
	public String toString() {
		return "Legend{"+"type="+type+", orient="+orient+", x="+x+", y="+y+", left="+left+", data="+data+", formatter="+formatter+'}';
	}

}

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
 
	private String orient;
	private String left;
	private List<String> data;

	public Legend() {
		this("vertical", "left", new ArrayList<>());
	}

	public Legend(String data) {
		this("vertical", "left", new ArrayList(Arrays.asList(data)));
	}

	public Legend(String orient, String left, List<String> data) {
		this.orient=orient;
		this.left=left;
		this.data=data;
	}

	public String getOrient() {
		return orient;
	}

	public void setOrient(String orient) {
		this.orient=orient;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left=left;
	}

	public List<String> getData() {
		return data;
	}

	public void add(String name) {
		this.data.add(name);
	}

	@Override
	public String toString() {
		return "Legend{"+"orient="+orient+", left="+left+", data="+data+'}';
	}
	
}

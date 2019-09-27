package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:43:48 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Point implements Serializable {

	private static final long serialVersionUID=314149241867360593L;

	private String type;
  private String name;

	public Point() {
		this("max", "máximo");
	}

	public Point(String type, String name) {
		this.type=type;
		this.name=name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	@Override
	public String toString() {
		return "Point{"+"type="+type+", name="+name+'}';
	}
	
}

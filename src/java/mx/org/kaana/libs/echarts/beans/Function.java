package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:43:48 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Function implements Serializable {

	private static final long serialVersionUID=7426814294366922507L;

  private String name;
  private String type;

	public Function(String name) {
		this(name, "average");
	}

	public Function(String name, String type) {
		this.name=name;
		this.type=type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	@Override
	public String toString() {
		return "Data{"+"name="+name+", type="+type+'}';
	}
	
}

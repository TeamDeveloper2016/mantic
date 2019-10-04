package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:24:23 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class AxisPointer implements Serializable {

	private static final long serialVersionUID=-9018362503070918037L;

	private String type;
	
	public AxisPointer() {
		this("shadow");  // line, cross
	}

	public AxisPointer(String type) {
		this.type=type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	@Override
	public String toString() {
		return "AxisPointer{"+"type="+type+'}';
	}
	
}

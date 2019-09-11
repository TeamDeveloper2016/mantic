package mx.org.kaana.libs.echarts.pie;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:49:03 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Data implements Serializable {

	private static final long serialVersionUID=7917129580896424223L;

	private String name;
	private Double value;

	public Data() {
		this("data-"+ Double.valueOf(Math.random()* 100).intValue(), Math.random()* 1000);
	}
	
	public Data(String name, Double value) {
		this.name=name;
		this.value=value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value=value;
	}

	@Override
	public String toString() {
		return "Data{"+"name="+name+", value="+value+'}';
	}
	
}

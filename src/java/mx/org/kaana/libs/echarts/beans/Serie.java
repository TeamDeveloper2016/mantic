package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import mx.org.kaana.libs.echarts.enums.EKinds;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:49:14 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Serie implements Serializable {

	private static final long serialVersionUID=71577914809278916L;

	private String name;
	private String type;

	public Serie() {
		this("Serie", EKinds.BAR.toString());
	}

	public Serie(String name, String type) {
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
		return "Serie{"+"name="+name+", type="+type+'}';
	}

	public static double toValue() {
	  return ThreadLocalRandom.current().nextDouble(50D, 300D);
	}
	
}

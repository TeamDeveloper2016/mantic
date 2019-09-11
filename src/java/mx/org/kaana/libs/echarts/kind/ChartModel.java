package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:40:28 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class ChartModel implements Serializable {

	private static final long serialVersionUID=-5282099427752743606L;

	private String backgroundColor;

	public ChartModel() {
	}

	public ChartModel(String backgroundColor) {
		this.backgroundColor=backgroundColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor=backgroundColor;
	}

	@Override
	public String toString() {
		return "ChartModel{"+"backgroundColor="+backgroundColor+'}';
	}
	
}

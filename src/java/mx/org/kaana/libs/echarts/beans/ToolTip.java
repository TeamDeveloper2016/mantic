package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:22:46 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class ToolTip implements Serializable {

	private static final long serialVersionUID=6209057606818339102L;

	private String trigger;
	private AxisPointer axisPointer;

	public ToolTip() {
		this("axis", new AxisPointer());
	}

	public ToolTip(String trigger, AxisPointer axisPointer) {
		this.trigger=trigger;
		this.axisPointer=axisPointer;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger=trigger;
	}

	public AxisPointer getAxisPointer() {
		return axisPointer;
	}

	public void setAxisPointer(AxisPointer axisPointer) {
		this.axisPointer=axisPointer;
	}

	@Override
	public String toString() {
		return "ToolTip{"+"trigger="+trigger+", axisPointer="+axisPointer+'}';
	}
 
}

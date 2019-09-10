package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 11:23:17 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AxisLine implements Serializable {

	private static final long serialVersionUID=8374721689937381426L;

  private Boolean show;

	public AxisLine() {
		this(true);
	}

	public AxisLine(Boolean show) {
		this.show=show;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show=show;
	}

	@Override
	public String toString() {
		return "AxisLine{"+"show="+show+'}';
	}
	
}

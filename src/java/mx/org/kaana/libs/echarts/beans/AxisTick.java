package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:11:21 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AxisTick implements Serializable {

	private static final long serialVersionUID=5546495108668710393L;
 
	private Boolean show;
	private Boolean alignWithLabel;

	public AxisTick() {
		this(true, true);
	}

	public AxisTick(Boolean show, Boolean alignWithLabel) {
		this.show=show;
		this.alignWithLabel=alignWithLabel;
	}

	public Boolean isShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show=show;
	}

	public Boolean isAlignWithLabel() {
		return alignWithLabel;
	}

	public void setAlignWithLabel(Boolean alignWithLabel) {
		this.alignWithLabel=alignWithLabel;
	}

	@Override
	public String toString() {
		return "AxisTick{"+"show="+show+", alignWithLabel="+alignWithLabel+'}';
	}

}

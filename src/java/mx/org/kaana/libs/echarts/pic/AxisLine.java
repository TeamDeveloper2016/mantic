package mx.org.kaana.libs.echarts.pic;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 12:20:25 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class AxisLine extends BaseAttr implements Serializable {

	private static final long serialVersionUID=-4271194453055348486L;

	public AxisLine() {
		this(Boolean.TRUE);
	}

	public AxisLine(boolean show) {
		super(show);
	}

}

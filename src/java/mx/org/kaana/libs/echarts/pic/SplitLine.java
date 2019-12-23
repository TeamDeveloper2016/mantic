package mx.org.kaana.libs.echarts.pic;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 12:20:25 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class SplitLine extends BaseAttr implements Serializable {

	private static final long serialVersionUID=-4271194453055348488L;

	public SplitLine() {
		this(Boolean.TRUE);
	}

	public SplitLine(boolean show) {
		super(show);
	}

}

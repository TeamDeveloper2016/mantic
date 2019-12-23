package mx.org.kaana.libs.echarts.pic;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 12:20:25 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Label extends BaseAttr implements Serializable {

	private static final long serialVersionUID=-4271194453055348481L;

	private String margin;

	public Label() {
		this(Boolean.TRUE, "30");
	}

	public Label(boolean show, String margin) {
		super(show);
		this.margin=margin;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin=margin;
	}

	@Override
	public String toString() {
		return "Label{"+"margin="+margin+'}';
	}
	
}

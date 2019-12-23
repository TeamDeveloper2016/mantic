package mx.org.kaana.libs.echarts.pic;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 12:20:25 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class AxisPointer implements Serializable {

	private static final long serialVersionUID=-4271194453055348416L;

	private Label label;

	public AxisPointer() {
		this(new Label(Boolean.TRUE, "30"));
	}

	public AxisPointer(Label label) {
		this.label=label;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label=label;
	}

	@Override
	public String toString() {
		return "AxisLPointer{"+"label="+label+'}';
	}
	
}

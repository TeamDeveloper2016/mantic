package mx.org.kaana.libs.echarts.pic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.AxisLabel;
import mx.org.kaana.libs.echarts.pic.AxisPointer;
import mx.org.kaana.libs.echarts.beans.TextStyle;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 12:04:58 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Xaxis extends Axis implements Serializable {

	private static final long serialVersionUID=-4271194453055348489L;

	public Xaxis() {
		this(new AxisLine(), new AxisTick(Boolean.FALSE), new SplitArea(false), null, null);
		this.setMinMax("0", "100");
	}

	public Xaxis(AxisLine axisLine, AxisTick axisTick, SplitArea splitArea, AxisLabel axisLabel, AxisPointer axisPointer) {
		super(axisLine, axisTick, splitArea, axisLabel, axisPointer);
	}

	public Xaxis(AxisLine axisLine, AxisTick axisTick, SplitLine splitLine, AxisLabel axisLabel, AxisPointer axisPointer) {
		super(axisLine, axisTick, splitLine, axisLabel, axisPointer);
	}

	public Xaxis(List<String> data, AxisLine axisLine, AxisTick axisTick, SplitArea splitArea, AxisLabel axisLabel, AxisPointer axisPointer) {
		super(data, axisLine, axisTick, splitArea, axisLabel, axisPointer);
	}

	public Xaxis(List<String> data, AxisLine axisLine, AxisTick axisTick, SplitLine splitLine, AxisLabel axisLabel, AxisPointer axisPointer) {
		super(data, axisLine, axisTick, splitLine, axisLabel, axisPointer);
	}
	
}

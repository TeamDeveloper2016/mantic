package mx.org.kaana.libs.echarts.pic;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.AxisLabel;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 12:04:58 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Axis implements Serializable {

	private static final long serialVersionUID=-4271194453055348489L;
	
	private List<String> data;
	private AxisLine axisLine;
	private AxisTick axisTick;
	private AxisLabel axisLabel;
	private AxisPointer axisPointer;
	private SplitArea splitArea;
	private SplitLine splitLine;
	private String min;
	private String max;

	public Axis(AxisLine axisLine, AxisTick axisTick, SplitLine splitLine, AxisLabel axisLabel, AxisPointer axisPointer) {
		this(null, axisLine, axisTick, splitLine, axisLabel, axisPointer);
	}
	
	public Axis(List<String> data, AxisLine axisLine, AxisTick axisTick, SplitLine splitLine, AxisLabel axisLabel, AxisPointer axisPointer) {
	  this.splitLine= splitLine;
		this.init(data, axisLine, axisTick, axisLabel, axisPointer);
	}
	
	public Axis(AxisLine axisLine, AxisTick axisTick, SplitArea splitArea, AxisLabel axisLabel, AxisPointer axisPointer) {
	  this(null, axisLine, axisTick, splitArea, axisLabel, axisPointer);
	}
	
	public Axis(List<String> data, AxisLine axisLine, AxisTick axisTick, SplitArea splitArea, AxisLabel axisLabel, AxisPointer axisPointer) {
	  this.splitArea= splitArea;
		this.init(data, axisLine, axisTick, axisLabel, axisPointer);
	}

	private void init(List<String> data, AxisLine axisLine, AxisTick axisTick, AxisLabel axisLabel, AxisPointer axisPointer) {
		this.data=data;
		this.axisLine=axisLine;
		this.axisTick=axisTick;
		this.axisLabel=axisLabel;
		this.axisPointer=axisPointer;
	}
	
	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data=data;
	}

	public AxisLine getAxisLine() {
		return axisLine;
	}

	public void setAxisLine(AxisLine axisLine) {
		this.axisLine=axisLine;
	}

	public AxisTick getAxisTick() {
		return axisTick;
	}

	public void setAxisTick(AxisTick axisTick) {
		this.axisTick=axisTick;
	}

	public SplitArea getSplitArea() {
		return splitArea;
	}

	public void setSplitArea(SplitArea splitArea) {
		this.splitArea=splitArea;
	}

	public AxisLabel getAxisLabel() {
		return axisLabel;
	}

	public void setAxisLabel(AxisLabel axisLabel) {
		this.axisLabel=axisLabel;
	}

	public AxisPointer getAxisPointer() {
		return axisPointer;
	}

	public void setAxisPointer(AxisPointer axisPointer) {
		this.axisPointer=axisPointer;
	}

	public SplitLine getSplitLine() {
		return splitLine;
	}

	public void setSplitLine(SplitLine splitLine) {
		this.splitLine=splitLine;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min=min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max=max;
	}

	public void setMinMax(String min, String max) {
		this.min=min;
		this.max=max;
	}
	
	@Override
	public String toString() {
		return "Axis{"+"data="+data+", axisLine="+axisLine+", axisTick="+axisTick+", axisLabel="+axisLabel+", axisPointer="+axisPointer+", splitArea="+splitArea+", splitLine="+splitLine+", min="+min+", max="+max+'}';
	}

}

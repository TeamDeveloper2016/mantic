package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.libs.reflection.Methods;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:43:48 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Axis implements Serializable {

	private static final long serialVersionUID=4270159570077052025L;
  public static final String COLOR_WHITE= "#FFF";
  public static final String COLOR_BLACK= "#000";
  public static final String COLOR_GRAY = "#999";

	private String type;
	private AxisLabel axisLabel;
	private AxisTick axisTick;
	private AxisLine axisLine;
	private List<String> data;
	private List<Double> boundaryGap;
	private Integer z;

	public Axis() {
		this("value");
	}

	public Axis(String type) {
		this(type, new AxisLabel(), new AxisTick(), new AxisLine(), null);
	}

	public Axis(String type, AxisLabel axisLabel, AxisTick axisTick, AxisLine axisLine, List<String> data) {
		this(type, axisLabel, axisTick, axisLine, data, 0);
	}
	
	public Axis(String type, AxisLabel axisLabel, AxisTick axisTick, AxisLine axisLine, List<String> data, Integer z) {
		this.type=type;
		this.axisLabel=axisLabel;
		this.axisTick=axisTick;
		this.axisLine=axisLine;
		this.data=data;
		// this.boundaryGap=Arrays.asList(0D, 0.1D);
		this.z= 0;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	public AxisLabel getAxisLabel() {
		return axisLabel;
	}

	public void setAxisLabel(AxisLabel axisLabel) {
		this.axisLabel=axisLabel;
	}

	public AxisTick getAxisTick() {
		return axisTick;
	}

	public void setAxisTick(AxisTick axisTick) {
		this.axisTick=axisTick;
	}

	public AxisLine getAxisLine() {
		return axisLine;
	}

	public void setAxisLine(AxisLine axisLine) {
		this.axisLine=axisLine;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data=data;
	}

	public List<Double> getBoundaryGap() {
		return boundaryGap;
	}

	public void setBoundaryGap(List<Double> boundaryGap) {
		this.boundaryGap=boundaryGap;
	}

	public Integer getZ() {
		return z;
	}

	public void setZ(Integer z) {
		this.z=z;
	}

	public void add(String name) {
		name= name!= null? name.toUpperCase(): null;
		if(name!= null && this.data.indexOf(name)< 0)
		  this.data.add(name);
	}
	
	@Override
	protected void finalize() throws Throwable {
		Methods.clean(this.data);
	}

	@Override
	public String toString() {
		return "Axis{"+"type="+type+", axisLabel="+axisLabel+", axisTick="+axisTick+", axisLine="+axisLine+", data="+data+", boundaryGap="+boundaryGap+", z="+z+'}';
	}

  public void clear() {
		this.data.clear();
	}	

}

package mx.org.kaana.libs.echarts.pic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Label;
import mx.org.kaana.libs.echarts.beans.Normal;
import mx.org.kaana.libs.echarts.beans.TextStyle;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 12:06:47 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Serie implements Serializable {

	private String name;
	private String type;
	private Label label;
	private Boolean symbolRepeat;
	private List<String> symbolSize;
	private String barCategoryGap;
	private String barGap;
	private List<Data> data;

	public Serie() {
		this("CGOR");
	}

	public Serie(String name) {
		this.name=name;
		this.type="pictorialBar";
		this.label= new Label(new Normal("right"));
		this.label.getNormal().setFontSize(14);
		this.label.getNormal().setOffset(new ArrayList<>(Arrays.asList(10, 0)));
		this.label.getNormal().setTextStyle(new TextStyle(14));
		this.symbolRepeat= Boolean.TRUE;
		this.symbolSize= new ArrayList<>(Arrays.asList("90%", "80%"));
		this.barCategoryGap="25%";
		this.barGap="5%";
		this.data= new ArrayList<>(Arrays.asList(new Data("pic-1"), new Data("pic-2"), new Data("pic-3"), new Data("pic-4"), new Data("pic-5")));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label=label;
	}

	public Boolean getSymbolRepeat() {
		return symbolRepeat;
	}

	public void setSymbolRepeat(Boolean symbolRepeat) {
		this.symbolRepeat=symbolRepeat;
	}

	public List<String> getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(List<String> symbolSize) {
		this.symbolSize=symbolSize;
	}

	public String getBarCategoryGap() {
		return barCategoryGap;
	}

	public void setBarCategoryGap(String barCategoryGap) {
		this.barCategoryGap=barCategoryGap;
	}

	public String getBarGap() {
		return barGap;
	}

	public void setBarGap(String barGap) {
		this.barGap=barGap;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data=data;
	}

	@Override
	public String toString() {
		return "Serie{"+"name="+name+", type="+type+", label="+label+", symbolRepeat="+symbolRepeat+", symbolSize="+symbolSize+", barCategoryGap="+barCategoryGap+", data="+data+'}';
	}
	
}

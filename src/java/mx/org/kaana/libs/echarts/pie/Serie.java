package mx.org.kaana.libs.echarts.pie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Label;
import mx.org.kaana.libs.echarts.beans.Normal;
import mx.org.kaana.libs.echarts.enums.EKinds;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:49:03 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Serie extends mx.org.kaana.libs.echarts.beans.Serie implements Serializable {

	private static final long serialVersionUID=5709519080391393460L;
	
	private List<String> radius;
	private List<String> center;
	private ItemStyle itemStyle;
	private List<Data> data;
	private Label label;

	public Serie(String name) {
		this(name, "55%");
	}
	
	public Serie(String name, String radius) {
		this(name, "55%", new ArrayList(Arrays.asList(new Data(), new Data(), new Data(), new Data(), new Data(), new Data(), new Data(), new Data())));
	}

	public Serie(String name, String radius, List<Data> data) {
		super(name, EKinds.PIE.toString());
		this.radius=new ArrayList(Arrays.asList("0%", radius));
		this.center=new ArrayList(Arrays.asList("50%", "50%"));
		this.itemStyle=new ItemStyle();
		this.label=new Label(new Normal("outside", "{b}\\n({d}%)"));
		this.data= data;
	}
	
	public Serie(String name, List<String> radius) {
		this(name, radius, new ArrayList(Arrays.asList(new Data(), new Data(), new Data(), new Data(), new Data(), new Data(), new Data(), new Data())));
	}

	public Serie(String name, List<String> radius, List<Data> data) {
		this(name, new ArrayList(Arrays.asList("50%", "50%")), radius, data);
	}

	public Serie(String name, List<String> center, List<String> radius, List<Data> data) {
		super(name, EKinds.PIE.toString());
		this.radius=radius;
		this.center=center;
		this.itemStyle=new ItemStyle();
		this.label=new Label(new Normal("outside", "{b}\\n({d}%)"));
		this.data=data;
	}

	public List<String> getRadius() {
		return radius;
	}

	public void setRadius(List<String> radius) {
		this.radius=radius;
	}

	public List<String> getCenter() {
		return center;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label=label;
	}

	public void setCenter(List<String> center) {
		this.center=center;
	}

	public ItemStyle getItemStyle() {
		return itemStyle;
	}

	public void setItemStyle(ItemStyle itemStyle) {
		this.itemStyle=itemStyle;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data=data;
	}

	@Override
	public String toString() {
		return "Serie{radius="+radius+", center="+center+", itemStyle="+itemStyle+", data="+data+'}';
	}
	
}

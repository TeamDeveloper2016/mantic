package mx.org.kaana.libs.echarts.bar;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 11:15:22 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Value implements Serializable {

	private static final long serialVersionUID=2531614052112824504L;

	private Double value;
	private ItemStyle itemStyle;

	public Value(Double value) {
		this(value, new ItemStyle());
	}

	public Value(Double value, String color) {
		this(value, new ItemStyle(color));
	}

	public Value(Double value, ItemStyle itemStyle) {
		this.value=value;
		this.itemStyle=itemStyle;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value=value;
	}

	public ItemStyle getItemStyle() {
		return itemStyle;
	}

	public void setItemStyle(ItemStyle itemStyle) {
		this.itemStyle=itemStyle;
	}

	@Override
	public String toString() {
		return "Value{"+"value="+value+", itemStyle="+itemStyle+'}';
	}
	
}

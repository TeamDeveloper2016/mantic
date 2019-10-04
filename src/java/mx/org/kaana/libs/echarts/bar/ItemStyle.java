package mx.org.kaana.libs.echarts.bar;

import java.io.Serializable;
import mx.org.kaana.libs.echarts.beans.Colors;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 11:15:22 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class ItemStyle implements Serializable {

	private static final long serialVersionUID=1L;

	private String color;
	private Double opacity;
	private String shadowColor;
	private Double shadowBlur;

	public ItemStyle() {
		this(Colors.toColor(), 0.8D, "rgba(0, 0, 0, 0.5)", 10D);
	}

	public ItemStyle(String color, Double opacity, String shadowColor, Double shadowBlur) {
		this.color=color;
		this.opacity=opacity;
		this.shadowColor=shadowColor;
		this.shadowBlur=shadowBlur;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color=color;
	}

	public Double getOpacity() {
		return opacity;
	}

	public void setOpacity(Double opacity) {
		this.opacity=opacity;
	}

	public String getShadowColor() {
		return shadowColor;
	}

	public void setShadowColor(String shadowColor) {
		this.shadowColor=shadowColor;
	}

	public Double getShadowBlur() {
		return shadowBlur;
	}

	public void setShadowBlur(Double shadowBlur) {
		this.shadowBlur=shadowBlur;
	}

	@Override
	public String toString() {
		return "ItemStyle{"+"color="+color+", opacity="+opacity+", shadowColor="+shadowColor+", shadowBlur="+shadowBlur+'}';
	}
	
}

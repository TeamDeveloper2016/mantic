package mx.org.kaana.libs.echarts.pie;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:49:03 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Emphasis implements Serializable {

	private static final long serialVersionUID=-5836808391197813457L;

	private Integer shadowBlur;
	private Integer shadowOffsetX;
	private String shadowColor;

	public Emphasis() {
		this(10, 0, "rgba(0, 0, 0, 0.5)");
	}

	public Emphasis(Integer shadowBlur, Integer shadowOffsetX, String shadowColor) {
		this.shadowBlur=shadowBlur;
		this.shadowOffsetX=shadowOffsetX;
		this.shadowColor=shadowColor;
	}

	public Integer getShadowBlur() {
		return shadowBlur;
	}

	public void setShadowBlur(Integer shadowBlur) {
		this.shadowBlur=shadowBlur;
	}

	public Integer getShadowOffsetX() {
		return shadowOffsetX;
	}

	public void setShadowOffsetX(Integer shadowOffsetX) {
		this.shadowOffsetX=shadowOffsetX;
	}

	public String getShadowColor() {
		return shadowColor;
	}

	public void setShadowColor(String shadowColor) {
		this.shadowColor=shadowColor;
	}

	@Override
	public String toString() {
		return "Emphasis{"+"shadowBlur="+shadowBlur+", shadowOffsetX="+shadowOffsetX+", shadowColor="+shadowColor+'}';
	}
	
}

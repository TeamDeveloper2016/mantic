package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:20:56 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Design implements Serializable {

	private static final long serialVersionUID=6463173481570505838L;

	private String fill;
	private String stroke;
	private Integer lineWidth;
	private Integer shadowBlur;
	private Integer shadowOffsetX;
	private Integer shadowOffsetY;
	private String shadowColor;
	private String text;
	private String font;

	public Design() {
		this("#fff", "#555", 1, 6, 3, 3, "rgba(0,0,0,0.3)");
	}

	public Design(String text, String font) {
		this.text=text;
		this.font=font;
	}
	
	public Design(String fill, String stroke, Integer lineWidth, Integer shadowBlur, Integer shadowOffsetX, Integer shadowOffsetY, String shadowColor) {
		this.fill=fill;
		this.stroke=stroke;
		this.lineWidth=lineWidth;
		this.shadowBlur=shadowBlur;
		this.shadowOffsetX=shadowOffsetX;
		this.shadowOffsetY=shadowOffsetY;
		this.shadowColor=shadowColor;
	}

	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill=fill;
	}

	public String getStroke() {
		return stroke;
	}

	public void setStroke(String stroke) {
		this.stroke=stroke;
	}

	public Integer getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(Integer lineWidth) {
		this.lineWidth=lineWidth;
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

	public Integer getShadowOffsetY() {
		return shadowOffsetY;
	}

	public void setShadowOffsetY(Integer shadowOffsetY) {
		this.shadowOffsetY=shadowOffsetY;
	}

	public String getShadowColor() {
		return shadowColor;
	}

	public void setShadowColor(String shadowColor) {
		this.shadowColor=shadowColor;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text=text;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font=font;
	}

	@Override
	public String toString() {
		return "Design{"+"fill="+fill+", stroke="+stroke+", lineWidth="+lineWidth+", shadowBlur="+shadowBlur+", shadowOffsetX="+shadowOffsetX+", shadowOffsetY="+shadowOffsetY+", shadowColor="+shadowColor+", text="+text+", font="+font+'}';
	}

	
}

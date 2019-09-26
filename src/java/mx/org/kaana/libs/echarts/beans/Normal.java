package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 18/09/2019
 *@time 17:11:48 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Normal implements Serializable {

	private static final long serialVersionUID=-7749442518672455859L;

	private String fontFamily;
	private Integer fontSize;
	private Boolean show;
	private String position; // insideRigth, insideLeft, inside

	public Normal() {
		this(true, "inside");
	}

	public Normal(Boolean show, String position) {
		this.fontFamily= "Roboto, sans-serif";
    this.fontSize= 11;
		this.show=show;
		this.position=position;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show=show;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position=position;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily=fontFamily;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize=fontSize;
	}

	@Override
	public String toString() {
		return "Normal{"+"fontFamily="+fontFamily+", fontSize="+fontSize+", show="+show+", position="+position+'}';
	}

}

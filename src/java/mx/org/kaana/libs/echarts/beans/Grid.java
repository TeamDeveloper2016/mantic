package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:13:38 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Grid implements Serializable {

	private static final long serialVersionUID=7471629472718955308L;

	private String left;
  private String right;
  private String top;
  private String bottom;
  private Boolean containLabel;

	public Grid() {
		this("60", "60", "10%", "10%", true);
	}

	public Grid(String left, String right, String top, String bottom, Boolean containLabel) {
		this.left=left;
		this.right=right;
		this.top=top;
		this.bottom=bottom;
		this.containLabel=containLabel;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left=left;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right=right;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top=top;
	}

	public String getBottom() {
		return bottom;
	}

	public void setBottom(String bottom) {
		this.bottom=bottom;
	}

	public Boolean getContainLabel() {
		return containLabel;
	}

	public void setContainLabel(Boolean containLabel) {
		this.containLabel=containLabel;
	}

	@Override
	public String toString() {
		return "Grid{"+"left="+left+", right="+right+", top="+top+", bottom="+bottom+", containLabel="+containLabel+'}';
	}
	
}

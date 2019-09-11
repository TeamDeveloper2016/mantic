package mx.org.kaana.libs.echarts.pie;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:49:03 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class ItemStyle implements Serializable {

	private static final long serialVersionUID=-2304795769442168321L;

  private Emphasis emphasis;

	public ItemStyle() {
		this(new Emphasis());
	}

	public ItemStyle(Emphasis emphasis) {
		this.emphasis=emphasis;
	}

	public Emphasis getEmphasis() {
		return emphasis;
	}

	public void setEmphasis(Emphasis emphasis) {
		this.emphasis=emphasis;
	}

	@Override
	public String toString() {
		return "ItemStyle{"+"emphasis="+emphasis+'}';
	}
	
}

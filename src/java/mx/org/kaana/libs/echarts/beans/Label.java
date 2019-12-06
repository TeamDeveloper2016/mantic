package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 18/09/2019
 *@time 17:12:18 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Label implements Serializable, Cloneable {

	private static final long serialVersionUID=-2619425225091818326L;

	private Normal normal;

	public Label() {
		this(new Normal());
	}

	public Label(Normal normal) {
		this.normal=normal;
	}

	public Normal getNormal() {
		return normal;
	}

	public void setNormal(Normal normal) {
		this.normal=normal;
	}

	@Override
	public String toString() {
		return "Label{"+"normal="+normal+'}';
	}

	@Override
	public Label clone() {
		Object object=null;
		try {
			object= super.clone();
		} // try
		catch (CloneNotSupportedException e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} //catch
		return (Label)object;
	}
	
}

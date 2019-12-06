package mx.org.kaana.libs.echarts.stack;

import java.io.Serializable;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.Label;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 5/09/2019
 * @time 09:49:14 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Serie extends mx.org.kaana.libs.echarts.bar.Serie implements Serializable, Cloneable {

	private static final long serialVersionUID=1120245308527989974L;

	private String stack;

	public Serie() {
		this("Serie");
	}

	public Serie(String legend) {
		this(legend, Colors.toColor());
	}

	public Serie(String legend, String color) {
		super(legend, color);
		super.setMarkPoint(null);
		this.stack="Stack";
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack=stack;
	}

	@Override
	public String toString() {
		return "Serie{"+"stack="+stack+'}';
	}

	@Override
	public Serie clone() {
		Object object=null;
		try {
			object= super.clone();
			Label label= ((Serie)object).getLabel().clone();
			label.setNormal(label.getNormal().clone());
			((Serie)object).setLabel(label);
		} // try
		catch (CloneNotSupportedException e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} //catch
		return (Serie)object;
	}
	
}

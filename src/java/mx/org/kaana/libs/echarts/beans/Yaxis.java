package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:42:43 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Yaxis extends Axis implements Serializable {

	private static final long serialVersionUID=8076111667381046848L;

	public Yaxis() {
		this("value");
	}

	public Yaxis(String type) {
		super(type);
	}
	
}

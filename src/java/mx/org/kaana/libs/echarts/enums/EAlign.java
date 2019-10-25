package mx.org.kaana.libs.echarts.enums;

import mx.org.kaana.libs.formato.Cadena;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/10/2019
 *@time 09:33:27 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EAlign {
	
	LEFT, CENTER, RIGHT, TOP, BOTTOM, MIDDLE, INSIDE_RIGHT, INSIDE_LEFT, INSIDE;

	public String toName() {
		return Cadena.toBeanName(this.name().toLowerCase());
	}
	
}

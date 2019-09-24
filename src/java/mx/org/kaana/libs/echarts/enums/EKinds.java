package mx.org.kaana.libs.echarts.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 12:24:15 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EKinds {
 
	BAR, LINE, PIE, STACK;
	
	public String toString() {
		return this.name().toLowerCase();
	}
	
}

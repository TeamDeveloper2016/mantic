package mx.org.kaana.libs.echarts.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/10/2019
 *@time 01:48:07 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ETypeLine {
	
   SOLID, DASHED, DOTTED;
	 
	 public String toName() {
		 return this.name().toLowerCase();
	 }
	 
}

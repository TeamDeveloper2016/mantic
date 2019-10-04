package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.Random;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 3/10/2019
 *@time 16:18:22 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Colors implements Serializable {

	public static final String[] SERIES_COLORS= {"#1A4150", "#70AEC6", "#245D74", "#A7D0E0", "#41839C", "#3A728E", "#OC3756", "#5EBEBD", "#061C36", "#7CD9CE", "#2E7588", "#4499A3"};
	private static final long serialVersionUID=4966824514724029183L;
	
	public static String toColor() {
		return SERIES_COLORS[new Random().nextInt(SERIES_COLORS.length)];
	}
	
}

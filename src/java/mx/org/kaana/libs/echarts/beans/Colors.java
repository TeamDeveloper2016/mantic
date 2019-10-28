package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 3/10/2019
 *@time 16:18:22 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Colors implements Serializable {

	public static final String[] SERIES_COLORS= {"#66CCFF", "#63C5FC", "#5FBFF8", "#5CB8F5", "#59B2F2", "#56ABEF", "#52A5EB", "#4F9EE8", "#4C97E5", "#4891E1", "#458ADE", "#4284DB", "#3F7DD8", "#3B76D4", "#3870D1", "#3569CE", "#3163CA", "#2E5CC7", "#2B56C4", "#274FC0", "#2448BD", "#2142BA", "#1E3BB7", "#1A35B3", "#172EB0", "#1427AD", "#1021A9", "#0D1AA6", "#0A14A3", "#070DA0", "#03079C", "#000099", "#1A4150", "#70AEC6", "#245D74", "#A7D0E0", "#41839C", "#3A728E", "#OC3756", "#5EBEBD", "#061C36", "#7CD9CE", "#2E7588", "#4499A3"};
	public static final String COLOR_BLACK= "#000000";
	public static final String COLOR_WHITE= "#FFFFFF";
	public static final String COLOR_BLUE = "#0000FF";
	public static final String COLOR_RED  = "#FF0000";
	public static final String COLOR_GREEN= "#248823";
	private static final long serialVersionUID=4966824514724029183L;
	private static final int TOP_LIST_COLORS= 10;
	
	private static List<String> colors;
	
	static {
		colors= new ArrayList<>();
	}
	
	private static String lookForNewColor() {
		String regresar= SERIES_COLORS[new Random().nextInt(SERIES_COLORS.length)];
		if(colors.indexOf(regresar)>= 0)
			regresar= lookForNewColor();
		return regresar;
	}
	
	public static String toColor(int topColors) {
		String color= SERIES_COLORS[new Random().nextInt(SERIES_COLORS.length)];
		if(topColors>= TOP_LIST_COLORS)
			topColors= TOP_LIST_COLORS- 2;
    if(colors.size()>= topColors) 
			colors.remove(0);
  	if(colors.indexOf(color)>= 0)
			color= lookForNewColor();
		colors.add(color);
		return color;
	}
	
	public static String toColor() {
		return toColor(TOP_LIST_COLORS);
	}
	
}

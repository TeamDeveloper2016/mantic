package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:35:44 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Xaxis extends Axis implements Serializable {
  
	private static final long serialVersionUID=-3050337562767707428L;
  
	public Xaxis() {
		this(new ArrayList(Arrays.asList("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom")));
	}
	
	public Xaxis(List<String> data) {
		super("category", new AxisLabel(), new AxisTick(), new AxisLine(), data);
	}

	public Xaxis(String type, List<String> data, AxisLabel axisLabel, Integer z) {
		super(type, axisLabel, new AxisTick(), new AxisLine(), data, z);
	}

}

package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.pie.Serie;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.model.IDataSet;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:20:56 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DonutModel extends PieModel implements Serializable {

	private static final long serialVersionUID=9169328733926503563L;
	private String inside;

	public DonutModel(String name) {
		super(name);
	}

	// Arrays.asList(inside, radius)
	public DonutModel(String name, String radius, String inside, Title title) {
		super(name, radius, title);
	}

	public DonutModel(String name, String radius, String inside, IDataSet data) {
		super(name, radius, data);
	}

	public DonutModel(String name, String radius, String inside, Title title, IDataSet data) {
		super(name, radius, title, data);
	}

	public DonutModel(String radius, String inside, Title title, Legend legend, List<String> color, ToolTip tooltip, List<Serie> series) {
		super(radius, title, legend, color, tooltip, series);
	}


}

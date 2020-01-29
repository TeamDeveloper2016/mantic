package mx.org.kaana.libs.echarts.kind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.Graphic;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.pie.Serie;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.beans.ToolTip;
import mx.org.kaana.libs.echarts.model.IDataSet;
import mx.org.kaana.libs.echarts.pie.Data;
import mx.org.kaana.libs.formato.Numero;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:20:56 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DonutModel extends PieModel implements Serializable {

	private static final long serialVersionUID=9169328733926503563L;

	private List<Graphic> graphic;
	
	public DonutModel(String name) {
		this(name, "40%", "60%", new Title("CGOR", "subtitulo"));
	}

	public DonutModel(String name, String radius, String inside, Title title) {
		super(name, radius, title, new Legend("2019"), new ArrayList(Arrays.asList(Colors.SERIES_COLORS)), new ToolTip(), new ArrayList<Serie>());
		this.getSeries().add(new Serie(name, new ArrayList(Arrays.asList(inside, radius))));
		this.prepare(radius, inside);
	}

	public DonutModel(String name, String radius, String inside, IDataSet data) {
		super(name, radius, data);
		this.prepare(radius, inside);
	}

	public DonutModel(String name, String radius, String inside, Title title, IDataSet data) {
		super(name, radius, title, data);
		this.prepare(radius, inside);
	}

	public DonutModel(String name, String radius, String inside, Title title, Legend legend, List<String> color, ToolTip tooltip, List<Serie> series) {
		super(name, radius, title, legend, color, tooltip, series);
		this.prepare(radius, inside);
	}

	private void prepare(String radius, String inside) {
		this.getTooltip().setTrigger("item");
		this.getTooltip().setAxisPointer(null);
		this.getLegend().getData().clear();
		for (Serie serie: this.getSeries()) {
			serie.setRadius(new ArrayList(Arrays.asList(inside, radius)));
		} // for
		for (Data data: this.getSeries().get(0).getData()) {
			this.getLegend().add(data.getName());
		} // for
	}

	private void toInnerDisplay(boolean clean) {
		for (Serie item: this.getSeries()) {
		  item.getLabel().getNormal().setPosition("inner");
			if(clean)
				item.getLabel().getNormal().setFormatter("function(params) { return ''; }");
			else 
			  item.getLabel().getNormal().setFormatter("function(params) { return params.percent+ ' %'; }");
		} // for	
	}
		
  public void toCustomDisplay(String elapsed, String percent, String total) {
		StringBuilder sb= new StringBuilder();
		sb.append("Transcurridos: ").append(elapsed).append("\\n").append("Porcentaje: ").append(percent);
		this.graphic= new ArrayList<>();
		this.graphic.add(new Graphic(sb.toString()));
		this.graphic.add(new Graphic("center", total, "40px Microsoft YaHei"));
		this.setTooltip(null);
		this.toInnerDisplay(true);
		this.setLegend(null);
	}

  public void toCustomDisplay(Double elapsed, Double percent, Double total) {
		this.toCustomDisplay(Numero.formatear(Numero.MILES_SIN_DECIMALES, elapsed), Numero.formatear(Numero.MONEDA_CON_DECIMALES, percent)+ " %", Numero.formatear(Numero.MILES_SIN_DECIMALES, total));
	}
	
  public void toCustomDonut(String total) {
		this.graphic= new ArrayList<>();
		this.graphic.add(new Graphic("center", total, "40px Microsoft YaHei"));
		this.toInnerDisplay(false);
	}

  public void toCustomDonut(Double total) {
		this.toCustomDonut(Numero.formatear(Numero.MILES_SIN_DECIMALES, total));
	}
	
}

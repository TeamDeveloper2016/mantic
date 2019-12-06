package mx.org.kaana.kajool.plantillas.graficas.backing;


import java.io.Serializable;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.echarts.bar.Serie;
import mx.org.kaana.libs.echarts.beans.Colors;
import mx.org.kaana.libs.echarts.beans.Coordinate;
import mx.org.kaana.libs.echarts.beans.CustomLine;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.enums.EBarOritentation;
import mx.org.kaana.libs.echarts.enums.ETypeLine;
import mx.org.kaana.libs.echarts.json.ItemSelected;
import mx.org.kaana.libs.echarts.kind.BarModel;
import mx.org.kaana.libs.echarts.kind.DonutModel;
import mx.org.kaana.libs.echarts.kind.PieModel;
import mx.org.kaana.libs.echarts.kind.StackModel;
import mx.org.kaana.libs.echarts.model.Datas;
import mx.org.kaana.libs.echarts.model.Multiple;
import mx.org.kaana.libs.echarts.model.Simple;
import mx.org.kaana.libs.echarts.model.Stacked;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.mantic.test.depurar.Clean;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 02:26:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "kajoolPlantillasGraficasFiltro")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

  private static final Log LOG=LogFactory.getLog(Filtro.class);
	private static final long serialVersionUID=-636605048480511716L;

  @Override
  @PostConstruct
	protected void init() {
		try {
			Simple simple    = new Simple("Ventas", DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "simple", attrs));
  		BarModel modelSimple  = new BarModel(new Title(), simple, EBarOritentation.HORIZONTAL);
			modelSimple.addLine(new CustomLine("2019", 50000D, Colors.COLOR_RED));
			modelSimple.toCustomFormatLabel("function (params) {return jsEcharts.format(params, 'double');}");
  		this.attrs.put("simple", modelSimple.toJson());
			
			Multiple multiple= new Multiple(DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "multiple", attrs));
  		BarModel modelMultiple= new BarModel(new Title(), multiple);
			modelMultiple.toCustomFormatLabel("function (params) {return jsEcharts.format(params, 'integer');}");
  		this.attrs.put("multiple", modelMultiple.toJson());
			
			Datas datas = new Datas("ventas", DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "simple", Collections.EMPTY_MAP));
  		PieModel pie= new PieModel("ventas", "55%", new Title(), datas);
			pie.toCustomFormatLabel("function (params) {return jsEcharts.format(params, 'percent');}");
			this.attrs.put("pie", pie.toJson());
			
  		DonutModel donut= new DonutModel("ventas", "55%", "30%", new Title(), datas);
  		this.attrs.put("donut", donut.toJson());
			
			Stacked stacked= new Stacked(DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "multiple", attrs));
  		StackModel stack= new StackModel(new Title(), stacked);
			stack.toCustomFormatLabel("function (params) {return jsEcharts.format(params, 'money', false);}");
			stack.getTooltip().setFormatter("function (params) {return jsEcharts.tooltip(params, 'money');}");
			
  		this.attrs.put("stack", stack.toJson());
			
  		modelSimple  = new BarModel(new Title(), simple);
			modelSimple.getGrid().setRight("10%");
  		this.attrs.put("vertical", modelSimple.toJson());
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}

	public void doCleanDB() {
		try {
  		Clean.main(null);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}

	public void doRefreshEChartWith(ItemSelected itemSelected) {
		LOG.info(itemSelected);
		try {
      BarModel model= new BarModel(new Title("CGOR", null), EBarOritentation.VERTICAL);
			model.addLine(new Coordinate("Hola", 6, 150, Colors.COLOR_RED, ETypeLine.SOLID));
			model.addLine(new CustomLine("Como", Serie.toValue(), Colors.COLOR_GREEN, ETypeLine.DASHED));
			model.addLine(new CustomLine("Estas", Serie.toValue(), Colors.COLOR_BLUE, ETypeLine.DOTTED));
			model.toCustomFormatLabel("function (params) {return jsEcharts.format(params, 'double');}");
			UIBackingUtilities.execute("jsEcharts.update('"+ itemSelected.getChart()+ "', {group:'00', json:"+ model.toJson()+ "});");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}

	public void doRefreshEChartSingle(String id, String group) {
		LOG.info(id);
		try {
      BarModel model= new BarModel(new Title("CGOR", null), EBarOritentation.VERTICAL);
			model.addLine(new Coordinate("Hola", 6, 150, Colors.COLOR_RED, ETypeLine.SOLID));
			//model.addLine(new CustomLine("Como", Serie.toValue(), Colors.COLOR_GREEN, ETypeLine.DASHED));
			//model.addLine(new CustomLine("Estas", Serie.toValue(), Colors.COLOR_BLUE, ETypeLine.DOTTED));
			model.toCustomFontSize(16);
			model.removeMarks();
			model.toCustomFormatLabel("function (params) {return jsEcharts.format(params, 'double');}");
			StringBuilder sb= new StringBuilder();
			sb.append("jsEcharts.add({");
			sb.append(id).append(": {group: '").append(group).append("', json:").append(model.toJson()).append(", title: '").append("Titulo demo "+ id.toUpperCase()).append("'}");
			sb.append("});");
			UIBackingUtilities.execute(sb.toString());
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}	
	
}

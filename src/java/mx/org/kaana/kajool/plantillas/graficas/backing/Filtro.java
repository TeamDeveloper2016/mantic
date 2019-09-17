package mx.org.kaana.kajool.plantillas.graficas.backing;


import java.io.Serializable;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.enums.EBarOritentation;
import mx.org.kaana.libs.echarts.kind.BarModel;
import mx.org.kaana.libs.echarts.kind.DonutModel;
import mx.org.kaana.libs.echarts.kind.PieModel;
import mx.org.kaana.libs.echarts.model.Datas;
import mx.org.kaana.libs.echarts.model.Multiple;
import mx.org.kaana.libs.echarts.model.Simple;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
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
  		this.attrs.put("simple", modelSimple.toJson());
			
			Multiple multiple= new Multiple(DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "multiple", attrs));
  		BarModel modelMultiple= new BarModel(new Title(), multiple);
  		this.attrs.put("multiple", modelMultiple.toJson());
			
			Datas datas = new Datas("ventas", DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "simple", Collections.EMPTY_MAP));
  		PieModel pie= new PieModel("ventas", "55%", datas);
			this.attrs.put("pie", pie.toJson());
			
  		DonutModel donut= new DonutModel("ventas", "55%", "40%", datas);
  		this.attrs.put("donut", donut.toJson());
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
		
}

package mx.org.kaana.libs.echarts.test;

import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.kind.PicModel;
import mx.org.kaana.libs.echarts.model.Pictorial;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:01:14 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class PicDemo {

	  private static final Log LOG=LogFactory.getLog(PicDemo.class);
		
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
      PicModel model= new PicModel();
			model.getyAxis().getAxisLabel().setFormatter("function(value) {return jsEcharts.label(value);}");
			model.getTooltip().setFormatter("function (params) {return jsEcharts.tooltip(params, 'percent');}");
			model.getLegend().setFormatter("function (params) {return jsEcharts.legend(params);}");
			model.toCustomFormatLabel("function (params) {return jsEcharts.format(params, 'percent');}");
			LOG.info(model.toJson());
			
			Pictorial multiple    = new Pictorial(DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "multiple", Collections.EMPTY_MAP));
  		PicModel modelMultiple= new PicModel(new Title(), multiple);
//			modelMultiple.getyAxis().getAxisLabel().setFormatter("function(value) {return jsEcharts.label(value);}");
//			modelMultiple.getTooltip().setFormatter("function (params) {return jsEcharts.tooltip(params, 'percent');}");
//			modelMultiple.getLegend().setFormatter("function (params) {return jsEcharts.legend(params);}");
//			modelMultiple.toCustomFormatLabel("function (params) {return jsEcharts.format(params, 'percent');}");
			LOG.info(modelMultiple.toJson());
    }

}

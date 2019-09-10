package mx.org.kaana.libs.echarts.test;

import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.enums.EBarOritentation;
import mx.org.kaana.libs.echarts.kind.BarModel;
import mx.org.kaana.libs.echarts.model.Multiple;
import mx.org.kaana.libs.echarts.model.Simple;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:01:14 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Demo {

	  private static final Log LOG=LogFactory.getLog(Demo.class);
		
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
      BarModel model= new BarModel(new Title("CGOR", null), EBarOritentation.HORIZONTAL);
			LOG.info(model.toJson());
			
			Simple simple       = new Simple("ventas", DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "simple", Collections.EMPTY_MAP));
  		BarModel modelSimple= new BarModel(new Title(), simple);
			LOG.info(modelSimple.toJson());
			
			Multiple multiple     = new Multiple(DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "multiple", Collections.EMPTY_MAP));
  		BarModel modelMultiple= new BarModel(new Title(), multiple);
			LOG.info(modelMultiple.toJson());
    }

}

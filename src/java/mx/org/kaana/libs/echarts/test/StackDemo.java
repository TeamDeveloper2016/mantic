package mx.org.kaana.libs.echarts.test;

import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.enums.EBarOritentation;
import mx.org.kaana.libs.echarts.kind.StackModel;
import mx.org.kaana.libs.echarts.model.Stacked;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:01:14 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class StackDemo {

	  private static final Log LOG=LogFactory.getLog(StackDemo.class);
		
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
      StackModel model= new StackModel(new Title("CGOR", null), EBarOritentation.VERTICAL);
			LOG.info(model.toJson());
			
			Stacked multiple     = new Stacked(DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "multiple", Collections.EMPTY_MAP));
  		StackModel modelMultiple= new StackModel(new Title(), multiple);
			LOG.info(modelMultiple.toJson());
    }

}
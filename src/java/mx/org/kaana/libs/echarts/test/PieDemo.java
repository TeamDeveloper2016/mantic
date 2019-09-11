package mx.org.kaana.libs.echarts.test;

import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.echarts.beans.Title;
import mx.org.kaana.libs.echarts.kind.PieModel;
import mx.org.kaana.libs.echarts.model.Datas;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:01:14 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class PieDemo {

	  private static final Log LOG=LogFactory.getLog(PieDemo.class);
		
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
      PieModel model= new PieModel("serie", new Title("CGOR", null));
			LOG.info(model.toJson());
			
			Datas datas       = new Datas("ventas", DaoFactory.getInstance().toEntitySet("VistaEchartsDemostracionDto", "simple", Collections.EMPTY_MAP));
  		PieModel data= new PieModel("ventas", datas);
			LOG.info(data.toJson());
    }

}

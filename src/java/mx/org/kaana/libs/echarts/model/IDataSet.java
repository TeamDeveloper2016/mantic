package mx.org.kaana.libs.echarts.model;

import java.util.List;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Serie;
import mx.org.kaana.libs.echarts.beans.Xaxis;
import mx.org.kaana.libs.echarts.beans.Yaxis;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/09/2019
 *@time 04:35:44 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public interface IDataSet {

	public DataModel dataSet();
	public List<Serie> getDataset();
	public Xaxis getXaxis();
	public Legend getLegend();
	
}

package mx.org.kaana.libs.echarts.model;

import java.util.List;
import mx.org.kaana.libs.echarts.beans.Legend;
import mx.org.kaana.libs.echarts.beans.Xaxis;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/09/2019
 *@time 04:35:44 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public interface IDataSet {

	public List<mx.org.kaana.libs.echarts.bar.Serie> getSeries();
	public List<mx.org.kaana.libs.echarts.pie.Serie> getDatas();
	public List<mx.org.kaana.libs.echarts.stack.Serie> getStack();
	public Xaxis getXaxis();
	public Legend getLegend();
	
}

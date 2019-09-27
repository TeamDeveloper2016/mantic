package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:43:48 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class MarkPoint implements Serializable {

	private static final long serialVersionUID=314149241867360593L;

	private List<Point> data;

	public MarkPoint() {
		this.data= new ArrayList<>();
		this.data.add(new Point("min", "mínimo"));
		this.data.add(new Point("max", "máximo"));
	}
	
	public MarkPoint(List<Point> data) {
		this.data=data;
	}

	public List<Point> getData() {
		return data;
	}

	public void setData(List<Point> data) {
		this.data=data;
	}

	@Override
	public String toString() {
		return "MarkPoint{"+"data="+data+'}';
	}

}

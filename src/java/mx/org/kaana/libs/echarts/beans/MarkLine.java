package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:43:48 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class MarkLine implements Serializable {

	private static final long serialVersionUID=912847999224677836L;

  private List<Data> data;

	public MarkLine(String name) {
		this(new ArrayList(Arrays.asList(new Data(name))));
	}

	public MarkLine(List<Data> data) {
		this.data=data;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data=data;
	}

	@Override
	public String toString() {
		return "MarkLine{"+"data="+data+'}';
	}
	
}

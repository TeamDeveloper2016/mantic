package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 10:36:05 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Legend implements Serializable {
 
	private static final long serialVersionUID=7980019871750859771L;
 
	private List<String> data;

	public Legend() {
		this(new ArrayList<>());
	}

	public Legend(List<String> data) {
		this.data=data;
	}

	public List<String> getData() {
		return data;
	}

	public void add(String name) {
		this.data.add(name);
	}
	
	@Override
	public String toString() {
		return "Legend{"+"data="+data+'}';
	}
	
}

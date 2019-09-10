package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.echarts.enums.EKinds;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 09:49:14 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Serie implements Serializable {

	private static final long serialVersionUID=71577914809278916L;

	private String name;
	private String type;
  private String barWidth;	
	private List<Double> data;

	public Serie() {
		this("Cuestionarios");
	}

	public Serie(String name) {
		this(name, "60%", new ArrayList(Arrays.asList(120D, 200D, 150D, 80D, 70D, 110D, 130D)), EKinds.BAR);
	}
	
	public Serie(String name, List<Entity> data) {
		this(name, "70%",  null, EKinds.BAR);
		this.load(data);
	}
	
	public Serie(String name, List<Entity> data, EKinds type) {
		this(name, "70%",  null, type);
		this.load(data);
	}
	
	public Serie(String name, String barWidth, List<Double> data, EKinds type) {
		this.name=name;
		this.barWidth=barWidth;
		this.data=data;
		this.type= type.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(String barWidth) {
		this.barWidth=barWidth;
	}

	public List<Double> getData() {
		return data;
	}

	public void setData(List<Double> data) {
		this.data=data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

	@Override
	public String toString() {
		return "Serie{"+"name="+name+", type="+type+", barWidth="+barWidth+", data="+data+'}';
	}

	private void load(List<Entity> data) {
		this.data= new ArrayList<>();
		for (Entity item: data) {
			this.data.add(item.toDouble("value"));
		} // for
	}
	
}

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

public class MarkLine implements  Serializable {

	private static final long serialVersionUID=912847999224677836L;

  private List<IMarkLine> data;
	private Label label;

	public MarkLine(String name) {
		this(new ArrayList(Arrays.asList(new Function(name))), "end");
	}

	public MarkLine(String name, Integer x, Integer y) {
		this(new ArrayList(new Coordinate(name, x, y)), "end");
	}

	public MarkLine(String name, Integer x, Integer y, String position) {
		this(new ArrayList(new Coordinate(name, x, y)), position);
	}

	public MarkLine(List<IMarkLine> data, String position) {
		this.data=data;
		this.label= new Label(new Normal(position));
	}

	public List<IMarkLine> getData() {
		return data;
	}

	public void setData(List<IMarkLine> data) {
		this.data=data;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label=label;
	}

	@Override
	public String toString() {
		return "MarkLine{"+"data="+data+", label="+label+'}';
	}
	
}

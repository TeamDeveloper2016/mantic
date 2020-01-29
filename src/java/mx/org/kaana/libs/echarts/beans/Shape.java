package mx.org.kaana.libs.echarts.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/09/2019
 *@time 10:20:56 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Shape implements Serializable {

	private static final long serialVersionUID=1621843510786808976L;

	private String width;
	private String height;
	private List<Integer> r;

	public Shape() {
		this("190", "50");
	}

	public Shape(String width, String height) {
		this.width=width;
		this.height=height;
		this.r= new ArrayList<>(Arrays.asList(5, 5, 5, 5));
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width=width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height=height;
	}

	public List<Integer> getR() {
		return r;
	}

	public void setR(List<Integer> r) {
		this.r=r;
	}

	@Override
	public String toString() {
		return "Shape{"+"width="+width+", height="+height+", r="+r+'}';
	}
	
}

package mx.org.kaana.libs.echarts.pic;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2019
 *@time 03:23:54 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Data implements Serializable {

	private Double value;
	private String symbol;
	private Integer symbolMargin;
	private transient String name;
	private transient String color;
	

	public Data() {
		this(toValue(), "rect");
	}

	public Data(String name) {
		this(name, toValue(), "#000000");
	}
	
	public Data(String name, Double value, String color) {
		this(value, "rect");
		this.name= name;
		this.color= color;
	}

	public Data(Double value, String symbol) {
		this(value, symbol, 2);
	}

	public Data(Double value, String symbol, Integer symbolMargin) {
		this.value=value;
		this.symbol=symbol;
		this.symbolMargin=symbolMargin;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value=value;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol=symbol;
	}

	public Integer getSymbolMargin() {
		return symbolMargin;
	}

	public void setSymbolMargin(Integer symbolMargin) {
		this.symbolMargin=symbolMargin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color=color;
	}

	public static double toValue() {
	  return ThreadLocalRandom.current().nextDouble(10D, 100D);
	}

	@Override
	public String toString() {
		return "Data{"+"value="+value+", symbol="+symbol+", symbolMargin="+symbolMargin+", name="+name+", color="+color+'}';
	}
	
}

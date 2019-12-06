package mx.org.kaana.libs.echarts.bar;

import java.io.Serializable;
import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/09/2019
 *@time 11:15:22 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Value implements Serializable, Cloneable {

	private static final long serialVersionUID=2531614052112824504L;

	private String name;
	private Double value;
	private ItemStyle itemStyle;

	public Value(String name) {
    this(name, 0D);		
	}
	
	public Value(String name, Double value) {
		this(name, value, new ItemStyle());
	}

	public Value(String name, Double value, String color) {
		this(name, value, new ItemStyle(color));
	}

	public Value(String name, Double value, ItemStyle itemStyle) {
		this.name= name!= null? name.toUpperCase(): null;
		this.value=value;
		this.itemStyle=itemStyle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value=value;
	}

	public ItemStyle getItemStyle() {
		return itemStyle;
	}

	public void setItemStyle(ItemStyle itemStyle) {
		this.itemStyle=itemStyle;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=11*hash+Objects.hashCode(this.name);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final Value other=(Value) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Value{"+"name="+name+", value="+value+", itemStyle="+itemStyle+'}';
	}

	@Override
	public Value clone() {
		Object object=null;
		try {
			object= super.clone();
		} // try
		catch (CloneNotSupportedException e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} //catch
		return (Value)object;
	}

}

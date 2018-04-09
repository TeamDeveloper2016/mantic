package mx.org.kaana.libs.archivo;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class MetaField implements Serializable{
  
  private static final long serialVersionUID = 246947512889949453L;
  private String field;
  private String name;	
	private int length;
	private int scale;
  private int type; 
  
  public MetaField(String name, int type, int length, int scale, String field) {
		this.name  = name;
		this.type  = type;
		this.length= length;
		this.scale = scale;
		this.field = field;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public int getLength() {
		return length;
	}

	public int getScale() {
		return scale;
	}

	public String getField() {
		return field;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=17*hash+(this.name!=null ? this.name.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final MetaField other=(MetaField) obj;
		if ((this.name==null) ? (other.name!=null) : !this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MetaField{"+"name="+this.name+", type="+this.type+", length="+this.length+", scale="+this.scale+", field="+this.field+'}';
	}
}

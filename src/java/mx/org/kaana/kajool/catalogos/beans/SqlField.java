package mx.org.kaana.kajool.catalogos.beans;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Cadena;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Dec 20, 2012
 *@time 11:26:29 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class SqlField implements Comparable<SqlField>, Serializable {
	
	private static final long serialVersionUID=5642487726920899727L;

	private String name;
	private int type;
	private String alias;

	public SqlField(String name) {
		this(name, 0, name);
	}

	public SqlField(String name, int type, String alias) {
		this.name=name;
		this.type=type;
		this.alias=alias;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias=alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type=type;
	}

  public String getBeanName() {
    return Cadena.toBeanName(this.getName().contains(".")?this.getName().substring(this.getName().indexOf(".")+1):this.getName());
  }

	@Override
	public boolean equals(Object obj) {
		if(obj==null) {
			return false;
		}
		if(getClass()!=obj.getClass()) {
			return false;
		}
		final SqlField other=(SqlField) obj;
		if((this.name==null) ? (other.name!=null) : !this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=37*hash+(this.name!=null ? this.name.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "SqlField{"+"name="+name+", type="+type+", alias="+alias+'}';
	}

	@Override
	public int compareTo(SqlField other) {
		return other.getName().compareTo(this.name);
	}
	
}

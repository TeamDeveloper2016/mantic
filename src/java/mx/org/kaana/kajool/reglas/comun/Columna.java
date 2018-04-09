package mx.org.kaana.kajool.reglas.comun;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jun 11, 2012
 * @time 2:58:36 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;

public class Columna implements Serializable {
	
	private static final long serialVersionUID=-4413506520777665897L;

	private String name;
	private EFormatoDinamicos format;

	public Columna(String name, EFormatoDinamicos format) {
		this.name=name;
		this.format=format;
	}

	public EFormatoDinamicos getFormat() {
		return format;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null) {
			return false;
		}
		if(getClass()!=obj.getClass()) {
			return false;
		}
		final Columna other=(Columna) obj;
		if((this.name==null) ? (other.name!=null) : !this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=97*hash+(this.name!=null ? this.name.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "Columna{"+"name="+name+", format="+format+'}';
	}
	
}

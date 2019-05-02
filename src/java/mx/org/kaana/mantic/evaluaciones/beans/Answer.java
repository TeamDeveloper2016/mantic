package mx.org.kaana.mantic.evaluaciones.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/05/2019
 *@time 10:01:35 AM 
 *@author Team Developer 2016 [team.developer@kaana.org.mx]
 */

public class Answer implements Serializable {

	private static final long serialVersionUID=2544775570173282261L;

	private Integer index;
	private String text;
	private String value;
	private Integer ok;

	public Answer() {
		this(-1, "", "", 2);
	}

	public Answer(Integer index, String text, String value, Integer ok) {
		this.index=index;
		this.text=text;
		this.value=value;
		this.ok=ok;
	}

	public Integer getIndex() {
		return index;
	}

	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}

	public Integer getOk() {
		return ok;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=71*hash+Objects.hashCode(this.value);
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
		final Answer other=(Answer) obj;
		if (!Objects.equals(this.value, other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Answer{"+"index="+index+", text="+text+", value="+value+", ok="+ok+ '}';
	}

}

package mx.org.kaana.mantic.evaluaciones.beans;

import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/05/2019
 *@time 02:11:55 PM 
 *@author Team Developer 2016 [team.developer@kaana.org.mx]
 */

public class Current {

	private Integer question;
	private Boolean control;
	private Integer top;

	public Current(Integer top) {
		this(0, Boolean.TRUE, top);
	}

	public Current(Integer question, Boolean control, Integer top) {
		this.question= question;
		this.control = control;
		this.top     = top;
	}

	public Integer getQuestion() {
		return question;
	}

	public void setQuestion(Integer question) {
		this.question=question;
	}

	public Boolean getControl() {
		return control;
	}

	public void setControl(Boolean control) {
		this.control=control;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top=top;
	}

	@Override
	public int hashCode() {
		int hash=3;
		hash=73*hash+Objects.hashCode(this.question);
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
		final Current other=(Current) obj;
		if (!Objects.equals(this.question, other.question)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Current{"+"question="+question+", control="+control+", top="+top+'}';
	}
		
}

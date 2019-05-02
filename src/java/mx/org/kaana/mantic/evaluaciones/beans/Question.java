package mx.org.kaana.mantic.evaluaciones.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import mx.org.kaana.libs.reflection.Methods;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/05/2019
 *@time 10:01:27 AM 
 *@author Team Developer 2016 [team.developer@kaana.org.mx]
 */

public class Question implements Serializable {

	private static final long serialVersionUID=9177240519164488595L;

	private Integer index;
	private String text;
	private List<Answer> answers;
	private String answer;

	public Question() {
		this(-1, "", new ArrayList<Answer>());
	}

	public Question(Integer index, String text, List<Answer> answers) {
		this.index  = index;
		this.text   = text;
		this.answers= answers;
		this.answer = "";
	}

	public Integer getIndex() {
		return index;
	}

	public String getText() {
		return text;
	}

	public List<Answer> getAnswers() {
		return Collections.unmodifiableList(this.answers);
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer=answer;
	}

	@Override
	public int hashCode() {
		int hash=3;
		hash=53*hash+Objects.hashCode(this.index);
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
		final Question other=(Question) obj;
		if (!Objects.equals(this.index, other.index)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Question{"+"index="+index+", text="+text+", answers="+answers+", answer="+answer+'}';
	}

	@Override
	protected void finalize() throws Throwable {
		Methods.clean(this.answers);
	}
	
}

package mx.org.kaana.mantic.evaluaciones.reglas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mx.org.kaana.mantic.evaluaciones.beans.Question;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/05/2019
 *@time 09:56:30 AM 
 *@author Team Developer 2016 [team.developer@kaana.org.mx]
 */

public class Evaluation {
	
	private Integer index;
	private Integer top;
	private List<Question> questions;

	public Evaluation() {
		this(new ArrayList<Question>());
	}

	public Evaluation(List<Question> questions) {
	  this.index    = 0;	
	  this.top      = questions.size();	
		this.questions= questions;
	}

	public Integer getIndex() {
		return index;
	}

	public Integer getTop() {
		return top;
	}

	public List<Question> getQuestions() {
		return Collections.unmodifiableList(this.questions);
	}
	
	public Integer next() {
		if(this.index< this.top- 1)
  		this.index++;
		return this.index;
	}

	public Integer previous() {
		if(this.index> 0)
		  this.index--;
		return this.index;
	}

	public Question getQuestion() {
		return getQuestion(this.index);
	}
	
	public Question getQuestion(Integer index) {
		return index>= 0 && index< this.top? this.questions.get(index): new Question();
	}

  public Boolean isLast() {
		return this.index== this.top- 1;
	}	
	
  public Boolean isFirst() {
		return this.index== 0;
	}	
	
	public void moveIndex(Integer index) {
		this.index= index>= 0 && index< this.top? index: 0;
	}
	
}

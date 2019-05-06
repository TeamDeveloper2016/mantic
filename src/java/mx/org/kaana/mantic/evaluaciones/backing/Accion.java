package mx.org.kaana.mantic.evaluaciones.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.evaluaciones.beans.Answer;
import mx.org.kaana.mantic.evaluaciones.beans.Current;
import mx.org.kaana.mantic.evaluaciones.beans.Question;
import mx.org.kaana.mantic.evaluaciones.enums.EPhaseEvaluation;
import mx.org.kaana.mantic.evaluaciones.reglas.ControlEvaluation;
import mx.org.kaana.mantic.evaluaciones.reglas.Evaluation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/05/2019
 *@time 09:56:30 AM 
 *@author Team Developer 2016 [team.developer@kaana.org.mx]
 */

@Named(value = "manticEvaluacionesAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID= -3494896842851695177L;
	private static final Log LOG              = LogFactory.getLog(Accion.class);

	private Evaluation evaluation;
	private BarChartModel model;
	private Boolean control;
	private EPhaseEvaluation stage;

	public Evaluation getEvaluation() {
		return evaluation;
	}
	
	public Question getQuestion() {
		return this.evaluation.getQuestion();
	}

	public BarChartModel getModel() {
		return model;
	}

	public Boolean getControl() {
		return control;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {    	      
			List<Answer> answers  = new ArrayList<>();
			answers.add(new Answer(1, "Mucho", "A", 2));
			answers.add(new Answer(2, "Algo", "B", 2));
			answers.add(new Answer(3, "Poco", "C", 1));
			answers.add(new Answer(4, "Nada", "D", 2));
			List<Question> questions= new ArrayList<>();
			questions.add(new Question(1, "En su opinión, ¿ Cuánto se respetan en el país los derechos de las personas indígenas ?", answers));
			answers  = new ArrayList<>();
			answers.add(new Answer(1, "texto opcion 1", "a", 2));
			answers.add(new Answer(2, "texto opcion 2", "b", 1));
			answers.add(new Answer(3, "texto opcion 3", "c", 2));
			answers.add(new Answer(4, "texto opcion 4", "d", 2));
			questions.add(new Question(2, "Texto de la pregunta ?", answers));
			answers  = new ArrayList<>();
			answers.add(new Answer(1, "opcion 1", "W", 2));
			answers.add(new Answer(2, "opcion 2", "X", 2));
			answers.add(new Answer(3, "opcion 3", "Y", 2));
			answers.add(new Answer(4, "opcion 4", "Z", 1));
			questions.add(new Question(3, "Texto de la pregunta ?", answers));
			answers  = new ArrayList<>();
			answers.add(new Answer(1, "opcion 1", "W", 2));
			answers.add(new Answer(2, "opcion 2", "X", 2));
			answers.add(new Answer(3, "opcion 3", "Y", 2));
			answers.add(new Answer(4, "opcion 4", "Z", 1));
			questions.add(new Question(4, "Texto de la pregunta ?", answers));
			answers  = new ArrayList<>();
			answers.add(new Answer(1, "opcion 1", "d", 2));
			answers.add(new Answer(2, "opcion 2", "e", 2));
			answers.add(new Answer(3, "opcion 3", "f", 2));
			answers.add(new Answer(4, "opcion 4", "g", 1));
			questions.add(new Question(5, "Texto de la pregunta ?", answers));
			answers  = new ArrayList<>();
			answers.add(new Answer(1, "opcion 1", "H", 2));
			answers.add(new Answer(2, "opcion 2", "I", 2));
			answers.add(new Answer(3, "opcion 3", "J", 2));
			answers.add(new Answer(4, "opcion 4", "K", 1));
			questions.add(new Question(6, "Texto de la pregunta ?", answers));
			answers  = new ArrayList<>();
			answers.add(new Answer(1, "opcion 1", "W", 2));
			answers.add(new Answer(2, "opcion 2", "X", 2));
			answers.add(new Answer(3, "opcion 3", "Y", 2));
			answers.add(new Answer(4, "opcion 4", "Z", 1));
			questions.add(new Question(7, "Texto de la pregunta ?", answers));
			answers  = new ArrayList<>();
			answers.add(new Answer(1, "opcion 1", "1", 2));
			answers.add(new Answer(2, "opcion 2", "2", 2));
			answers.add(new Answer(3, "opcion 3", "3", 2));
			answers.add(new Answer(4, "opcion 4", "4", 1));
			questions.add(new Question(8, "Texto de la pregunta ?", answers));
			answers  = new ArrayList<>();
			answers.add(new Answer(1, "opcion 1", "j", 2));
			answers.add(new Answer(2, "opcion 2", "k", 2));
			answers.add(new Answer(3, "opcion 3", "l", 2));
			answers.add(new Answer(4, "opcion 4", "m", 1));
			questions.add(new Question(9, "Texto de la pregunta ?", answers));
			answers  = new ArrayList<>();
			answers.add(new Answer(1, "opcion 1", "W", 2));
			answers.add(new Answer(2, "opcion 2", "X", 2));
			answers.add(new Answer(3, "opcion 3", "Y", 2));
			answers.add(new Answer(4, "opcion 4", "Z", 1));
			questions.add(new Question(10, "Texto de la pregunta ?", answers));
			this.evaluation= new Evaluation(questions);
			this.model     = this.initBarModel();
			this.control   = Boolean.FALSE;
			this.doCheckStateQuestion();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init		    

	public void doNext() {
		if(!this.control) 
 		  this.evaluation.next();
 		this.control= !this.control;
	}
	
	public void doPrevious() {
		if(this.control) 
  		this.evaluation.previous();
   	this.control= !this.control;
	}
	
	public Boolean getLast() {
		return this.evaluation.isLast();
	}
		
	public Boolean getFirst() {
		return this.evaluation.isFirst();
	}

	private BarChartModel initBarModel() {
		HorizontalBarChartModel barmodel=new HorizontalBarChartModel();
		ChartSeries boys=new ChartSeries();
		boys.setLabel("Respuestas");
		boys.set("A", 120);
		boys.set("B", 100);
		boys.set("C", 44);
		boys.set("D", 150);
		boys.set("E", 25);
		barmodel.addSeries(boys);
		barmodel.setShadow(true);
		barmodel.setSeriesColors("F52F2F, F52F2F, 00FF00, F52F2F, F52F2F");
		// model.setSeriesColors("58BA27, FFCC33, 00FF00, F52F2F, A30303");
		//barmodel.setBarWidth(70);
		barmodel.setExtender("chartExtender");
		return barmodel;
	}

	public void doCheckStateQuestion() {
		LOG.info("VERIFICAR LA VARIABLE A NIVEL DE APLICACION SOBRE QUE PREGUNTA MOSTRAR");
		ControlEvaluation instance= (ControlEvaluation)JsfBase.getApplication().getAttribute("janalContolQuestion");
		if(instance!= null) {
			Current current= instance.getControl().get("FEGEM");
		  this.evaluation.moveIndex(current.getQuestion());
			this.control= current.getControl();
			LOG.info("Actual: "+ current);
		} // if	
	}

  public void doSendTo() {
		LOG.info("ENVIAR INFORMACION PARA CALCULAR GRAFICO");
	}	
	
}

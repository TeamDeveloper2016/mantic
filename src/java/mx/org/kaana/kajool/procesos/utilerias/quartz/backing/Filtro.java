package mx.org.kaana.kajool.procesos.utilerias.quartz.backing;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContextEvent;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.beans.Jobs;
import mx.org.kaana.kajool.procesos.enums.EAccionJob;
import mx.org.kaana.kajool.procesos.enums.ESemaforos;
import mx.org.kaana.kajool.seguridad.init.Loader;
import mx.org.kaana.kajool.seguridad.quartz.Especial;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;

@Named(value = "kajoolMantenimientoUtileriasQuartzFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final long serialVersionUID= 5801122739416502243L;
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("expresion", "");
			this.attrs.put("jobName", "");
			this.attrs.put("size", 0);
			this.attrs.put("paginator", false);			
      doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
	
	public List<Jobs> doLoadInit() throws Exception {
		List<Jobs>regresar = null;
		Scheduler scheduler= null;
		String server      = null;
		try {
			scheduler= Especial.getInstance().getScheduler();			
			if(scheduler!= null){				
				regresar= new ArrayList<>();				
				for (String groupName : scheduler.getJobGroupNames()) { 
					for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) { 																							
						Trigger trigger = ((List<Trigger>) scheduler.getTriggersOfJob(jobKey)).get(0);						
						server= scheduler.getJobDetail(jobKey).getJobDataMap().getString("server");
						regresar.add(new Jobs(jobKey.getName(), jobKey.getGroup().toUpperCase(), trigger.getNextFireTime(), trigger.getPreviousFireTime(), jobKey, toSemaforo(scheduler.getTriggerState(trigger.getKey())), ((CronTrigger) trigger).getCronExpression(), ((CronTrigger) trigger).getExpressionSummary(), server));
					} // for
				} // for			
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		return regresar;
	} // doLoadInit
	
  @Override
  public void doLoad() {
    List<Jobs>search= null;		
    try {			
			if(Especial.getInstance().getScheduler()!= null){				
				search= doLoadInit();
				this.attrs.put("size", search.size());
				this.attrs.put("paginator", search.size() > Constantes.REGISTROS_POR_PAGINA);
				this.attrs.put("listJobs", search);
				UIBackingUtilities.resetDataTable();
			} // if			
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
  } // doLoad
	
	public void doProcesarJob(String proceso){
		Jobs seleccionado  = null;
		EAccionJob accion  = null;
		Scheduler scheduler= null;
		JobKey jobKey      = null;		
		try {			
			seleccionado= (Jobs) this.attrs.get("seleccionado");			
			accion= EAccionJob.valueOf(proceso);
			scheduler= Especial.getInstance().getScheduler();
			jobKey= seleccionado.getJobKey();			
			switch(accion){
				case ACTIVAR:
					scheduler.resumeJob(jobKey);
					break;
				case PAUSAR:
					scheduler.pauseJob(jobKey);
					break;
				case EJECUTAR:
					scheduler.triggerJob(jobKey);
					break;
			} // switch
			doLoad();
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doPausar 
	
	private String toSemaforo(Trigger.TriggerState state){
		String regresar= ESemaforos.VERDE.getNombre();
		try {
			switch(state){				
				case BLOCKED:
				case ERROR:
					regresar= ESemaforos.ROJO.getNombre();
					break;
				case PAUSED:
					regresar= ESemaforos.AMARILLO.getNombre();
					break;
			} // switch		
		} // try
		catch (Exception e) {						
			throw e;
		} // catch				
		return regresar;
	} // toSemaforo
	
	public boolean isMostrarCaptura(){
		boolean regresar          = false;	
		Jobs seleccionado         = null;
		Scheduler scheduler       = null;
		Trigger.TriggerState state= null;
		try {
			seleccionado= (Jobs) this.attrs.get("seleccionado");
			if(seleccionado!= null){
				scheduler= Especial.getInstance().getScheduler();
				state= scheduler.getTriggerState(scheduler.getTriggersOfJob(seleccionado.getJobKey()).get(0).getKey());
				regresar= state.equals(Trigger.TriggerState.PAUSED) || state.equals(Trigger.TriggerState.BLOCKED) || state.equals(Trigger.TriggerState.ERROR);
				this.attrs.put("jobName", seleccionado.getJobName());
				this.attrs.put("expresion", seleccionado.getExpresion());				
				this.attrs.put("summary", seleccionado.getSummary());				
			} // if
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // isMostrarCaptura
	
	public void doAceptar(){
		Jobs seleccionado            = null;
		Scheduler scheduler          = null;
		Trigger trigger              = null;
		TriggerBuilder triggerBuilder= null;
		try {			
			seleccionado= (Jobs) this.attrs.get("seleccionado");
			if(validarExpresion()){				
				scheduler= Especial.getInstance().getScheduler();
				trigger= scheduler.getTriggersOfJob(seleccionado.getJobKey()).get(0);
				triggerBuilder = TriggerBuilder.newTrigger().withIdentity(trigger.getKey()).withSchedule(CronScheduleBuilder.cronSchedule(this.attrs.get("expresion").toString()));
				scheduler.rescheduleJob(trigger.getKey(), triggerBuilder.build());				
				JsfBase.addMessage("Modificar expresión", "Se modifico la expresión de forma correcta", ETipoMensaje.INFORMACION);				
			} // if 
			else
				JsfBase.addMessage("Modificar expresión", "La expresión es incorrecta", ETipoMensaje.ERROR);
			UIBackingUtilities.execute("PF('dlgExpresion').hide();");
			doLoad();
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // doAceptar
	
  public void doActivar() {
    ServletContextEvent event= null;
    Scheduler scheduler      = null;    
    try {      
			scheduler= Especial.getInstance().getScheduler();
			if(scheduler== null){
				event= new ServletContextEvent(JsfBase.getApplication());
				Loader.getInstance(event).loadScheduler(event);
				doLoad();
			} // if      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  } // doActivar
  
	private boolean validarExpresion() throws ParseException{		
		return CronExpression.isValidExpression(this.attrs.get("expresion").toString());
	} // validarExpresion
  
  public boolean isMostrarActivar(){
    boolean regresar= false;
    try {
      regresar=JsfBase.isAdminEncuestaOrAdmin();
    } // try 
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  } // isMostrarActivar
  
  public boolean doAplicarEstilo(String pathJob){
    return Especial.getInstance().getPath().equals(pathJob) || pathJob.equals("*");
  } // doAplicarEstilo	
}
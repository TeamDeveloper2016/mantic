package mx.org.kaana.jobs.comun;

import java.io.Serializable;
import mx.org.kaana.libs.recurso.Configuracion;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class IBaseJob implements Job, Serializable{

	private static final long serialVersionUID = -4102220313702665405L;

	public abstract void procesar(JobExecutionContext jec) throws JobExecutionException;
	
	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		if(!Configuracion.getInstance().getPropiedad("sistema.corre.local").equals("si")){
			procesar(jec);
		} // if		
	}	// execute
}

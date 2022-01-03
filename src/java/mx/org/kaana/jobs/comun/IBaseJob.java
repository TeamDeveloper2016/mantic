package mx.org.kaana.jobs.comun;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class IBaseJob implements Job, Serializable {

  private static final Log LOG = LogFactory.getLog(IBaseJob.class);
	private static final long serialVersionUID = -4102220313702665405L;

	public abstract void procesar(JobExecutionContext jec) throws JobExecutionException;
	
	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
    LOG.error("Time ["+ Fecha.formatear(Fecha.FECHA_HORA)+ "] Class: "+ this.getClass().getName());
		if(!Configuracion.getInstance().getPropiedad("sistema.corre.local").equalsIgnoreCase("si")) 
			procesar(jec);
	}	// execute
  
}

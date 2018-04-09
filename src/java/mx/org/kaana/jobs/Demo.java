package mx.org.kaana.jobs;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22-sep-2015
 *@time 9:11:42
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


import java.io.Serializable;
import java.util.List;

import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.beans.DetalleConfiguracion;
import mx.org.kaana.kajool.beans.TareaServidor;
import mx.org.kaana.kajool.seguridad.quartz.Especial;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Demo implements Job, Serializable {

	private static final Log LOG              =LogFactory.getLog(Demo.class);
	private static final long serialVersionUID=7505746848602636876L;

	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		DetalleConfiguracion detalleConfiguracion = null;
		List<TareaServidor> tareaServidor         = null;		
		try {
		  tareaServidor=Especial.getInstance().getTareaServidor();
			LOG.info("Ejecutó la tarea Demo");
		  for (TareaServidor recordTarea : tareaServidor) {
        detalleConfiguracion=recordTarea.toDetalleConfiguracion();
        LOG.info("Entidades a procesar en el servidor ["+detalleConfiguracion.getEntidades()+"]");
		  }// for					
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch	
	}
}


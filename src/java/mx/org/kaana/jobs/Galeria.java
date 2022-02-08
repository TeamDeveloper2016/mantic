package mx.org.kaana.jobs;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 05-feb-2022
 *@time 9:11:42
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import mx.org.kaana.jobs.comun.IBaseJob;
import mx.org.kaana.kajool.control.bean.Portal;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Galeria extends IBaseJob {

	private static final Log LOG              =LogFactory.getLog(Galeria.class);
	private static final long serialVersionUID=7505746848602636876L;

	@Override
	public void procesar(JobExecutionContext jec) throws JobExecutionException {
		try {
			LOG.error("Cargando galería de las imagenes del portal");
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaCapacitacion()) 
        Portal.getInstance().reload();
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch	
	} // execute
  
}


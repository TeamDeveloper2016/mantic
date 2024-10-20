package mx.org.kaana.jobs;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19-oct-2024
 *@time 9:11:42
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Reiniciar implements Job, Serializable {

	private static final Log LOG              = LogFactory.getLog(Reiniciar.class);
	private static final long serialVersionUID= 7960714038594054567L;

	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		try {
			if(!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion()) {
        LOG.error("REINICIANDO EL SERVIDOR ...");
        Process runtimeProcess= Runtime.getRuntime().exec("jr");
        int processComplete   = runtimeProcess.waitFor();
        /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
        if (processComplete== 0)
          LOG.error("SE RE-INICIO CON EXITO");
        else
          LOG.error("FALLO EL RE-INICIO SERVIDOR");
			} // if
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
      LOG.error("Ocurrio un error al reiniciar el servidor");
		} // catch	
	} // execute
  
}


package mx.org.kaana.jobs;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22-sep-2015
 *@time 9:11:42
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.respaldos.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Respaldos implements Job, Serializable {

	private static final Log LOG              = LogFactory.getLog(Respaldos.class);
	private static final long serialVersionUID= 7960794038594054567L;

	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		Transaccion transaccion = null;
		try {
			if(!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion()) {
				transaccion= new Transaccion();
				if(transaccion.ejecutar(EAccion.AGREGAR))
					LOG.info("Se realizo el respaldo de la BD de forma correcta");
				else
					LOG.error("Ocurrio un error al realizar el respaldo de la BD");				
			} // if
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
      LOG.error("Ocurrio un error al realizar el respaldo de la BD");
		} // catch	
	} // execute
}


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
import mx.org.kaana.mantic.facturas.reglas.Transferir;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Facturas implements Job, Serializable {

	private static final Log LOG              = LogFactory.getLog(Facturas.class);
	private static final long serialVersionUID= 1809037806413388478L;

	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		Transferir transferir= null;
		try {
			if(!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion()) {
				transferir = new Transferir();
				if (transferir.ejecutar(EAccion.GENERAR)) 
					LOG.info("Se realizo la sincronización de las facturas de forma correcta");
				else
					LOG.error("Ocurrio un error al realizar la sincronización de las facturas");				
			} // if
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
      LOG.error("Ocurrio un error al realizar el respaldo de la BD");
		} // catch	
	} // execute
}


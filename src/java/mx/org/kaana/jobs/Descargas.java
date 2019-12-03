package mx.org.kaana.jobs;


import java.io.Serializable;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.seguridad.quartz.Especial;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.respaldos.descargas.reglas.Manager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Descargas implements Job, Serializable {

	private static final Log LOG              = LogFactory.getLog(Descargas.class);
	private static final long serialVersionUID= 7960794038594054567L;
	private static final String BUILD         = "build";	

	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		Manager manager= null;
		try {
			if(Configuracion.getInstance().isEtapaDesarrollo() && (Especial.getInstance().getPath()== null ? JsfBase.getRealPath() : Especial.getInstance().getPath()).contains(BUILD)) {
				manager= new Manager();
				if(manager.execute())
					LOG.info("Se realizo el proceso de descarga de respaldo de forma correcta.");
				else
					LOG.info("Ocurrio un error al realizar la descarga del respaldo.");				
			} // if
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
      LOG.error("Ocurrio un error al realizar la descarga del respaldo.");
		} // catch	
	} // execute
}


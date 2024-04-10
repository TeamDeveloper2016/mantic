package mx.org.kaana.jobs;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22-sep-2015
 *@time 9:11:42
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.jobs.comun.IBaseJob;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Periodo;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.respaldos.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Respaldos extends IBaseJob {

	private static final Log LOG              = LogFactory.getLog(Respaldos.class);
	private static final long serialVersionUID= 7960794038594054567L;

	@Override
	public void procesar(JobExecutionContext jec) throws JobExecutionException {
		Transaccion transaccion   = null;
		Map<String, Object> params= new HashMap<>();
		try {
			if(!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion()) {
        LOG.error("----------------ENTRO A REALIZAR UN RESPALDO A LA BD -----------------------------");
				transaccion= new Transaccion();
				if(transaccion.ejecutar(EAccion.AGREGAR))
					LOG.error("Se realizo el respaldo de la BD de forma correcta");
				else
					LOG.error("Ocurrio un error al realizar el respaldo de la BD");			
				Periodo periodo= new Periodo();
				periodo.addDias(-20);
				params.put("registro", periodo.toString());
  			LOG.error("Iniciando el proceso de limpieza de ventas perdidas con fecha de "+ periodo.toString());
				Long actualizados= DaoFactory.getInstance().updateAll(TcManticFaltantesDto.class, params, "olds");
  			LOG.error("Se actulizarón "+ actualizados+ " registros del catalogo de ventas perdidas.");
			} // if
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
      LOG.error("Ocurrio un error al realizar el respaldo de la BD");
		} // catch	
		finally {
			Methods.clean(params);
		} // finally
	} // execute
  
}


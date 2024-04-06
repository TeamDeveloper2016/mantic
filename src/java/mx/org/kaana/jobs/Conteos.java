package mx.org.kaana.jobs;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 04-febrero-2024
 *@time 22:01:12
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.jobs.comun.IBaseJob;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.conteos.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticConteosDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Conteos extends IBaseJob implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Conteos.class);
	private static final long serialVersionUID= 7960794038594054163L;

	@Override
	public void procesar(JobExecutionContext jec) throws JobExecutionException {
		Map<String, Object> params= new HashMap<>();
    Transaccion transaccion   = null;
		try {
			if(Configuracion.getInstance().isEtapaProduccion()) {
        params.put("sucursales", "1,2,3");
        params.put("fecha", Fecha.getMinutosEstandar(-5));
        List<Entity> conteos= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaConteosDto", "remoto", params, 500L);
        if(conteos!= null && !conteos.isEmpty()) {
          LOG.error("----------------ENTRO A CONTEOS REMOTOS------------------");
          for (Entity item: conteos) {
            LOG.error("Conteo: "+ item.toString("nombre")+ " - "+ item.toString("fecha")+ " de "+ item.toString("usuario"));  
            TcManticConteosDto conteo= (TcManticConteosDto)DaoFactory.getInstance().findById(TcManticConteosDto.class, item.toLong("idConteo"));
            if(!Objects.equals(conteo, null)) {
              transaccion= new Transaccion(conteo);
              try {
                transaccion.ejecutar(EAccion.PROCESAR);
              } // try
              catch(Exception e) {
                Error.mensaje(e); 
                transaccion.ejecutar(EAccion.DESACTIVAR);
              } // catch
              finally {
                transaccion= null;
              } // finally
            } // if
          } // for
          LOG.error("---------------------------------------------------------");
        } // if
      } // if
	  } // try
		catch(Exception e) {
			Error.mensaje(e);
      LOG.error("Ocurrio un error al realizar el registro de conteos remotos");
		} // catch	
		finally {
			Methods.clean(params);
		} // finally
	} 
  
}


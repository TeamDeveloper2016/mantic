package mx.org.kaana.jobs;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 27-agosto-2025
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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.contadores.reglas.Transaccion;
import mx.org.kaana.mantic.contadores.beans.Contador;
import mx.org.kaana.mantic.catalogos.conteos.reglas.Operacion;
import mx.org.kaana.mantic.db.dto.TcManticContadoresDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Contadores extends IBaseJob implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Contadores.class);
	private static final long serialVersionUID= 7960794038594054163L;

	@Override
	public void procesar(JobExecutionContext jec) throws JobExecutionException {
		Map<String, Object> params= new HashMap<>();
    Transaccion transaccion   = null;
//    Operacion operacion       = null;
		try {
			if(Configuracion.getInstance().isEtapaProduccion()) {
        params.put("sucursales", "1,2,3");
        params.put("fecha", Fecha.getMinutosEstandar(-7));
        List<Entity> conteos= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaContadoresDto", "remoto", params, 500L);
        if(conteos!= null && !conteos.isEmpty()) {
          LOG.error("------------------ENTRO A CONTEOS DIRIGIDOS------------------");
          for (Entity item: conteos) {
            LOG.error("Conteo: "+ item.toString("nombre")+ " - "+ item.toString("fecha")+ " de "+ item.toString("usuario"));  
            params.put(Constantes.SQL_CONDICION, "id_contador= "+ item.toLong("idContador"));
            Contador conteo= (Contador)DaoFactory.getInstance().toEntity(Contador.class, "TcManticContadoresDto", "igual", params);
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
          LOG.error("---------------------------------------------------------------");
        } // if
//        List<Entity> transferencias= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaConteosDto", "transferencias", params, 500L);
//        if(transferencias!= null && !transferencias.isEmpty()) {
//          LOG.error("----------------ENTRO A SOLICITUDES DE PRODUCTOS------------------");
//          for (Entity item: transferencias) {
//            LOG.error("Transferencia: "+ item.toString("nombre")+ " - "+ item.toString("fecha")+ " de "+ item.toString("usuario"));  
//            TcManticTransferenciasDto transferencia= (TcManticTransferenciasDto)DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, item.toLong("idTransferencia"));
//            if(!Objects.equals(transferencia, null)) {
//              operacion= new Operacion(transferencia);
//              try {
//                operacion.ejecutar(EAccion.PROCESAR);
//              } // try
//              catch(Exception e) {
//                Error.mensaje(e); 
//                operacion.ejecutar(EAccion.DESACTIVAR);
//              } // catch
//              finally {
//                operacion= null;
//              } // finally
//            } // if 
//          } // for
//        } // if  
      } // if
	  } // try
		catch(Exception e) {
			Error.mensaje(e);
      LOG.error("Ocurrio un error en conteos dirigidos / solicitudes de articulos");
		} // catch	
		finally {
			Methods.clean(params);
		} // finally
	} 
  
}


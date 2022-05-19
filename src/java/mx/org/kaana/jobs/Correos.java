package mx.org.kaana.jobs;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17-mayo-2022
 *@time 9:11:42
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.jobs.comun.IBaseJob;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Bonanza;
import mx.org.kaana.libs.wassenger.Email;
import mx.org.kaana.libs.wassenger.Token;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Correos extends IBaseJob implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Correos.class);
	private static final long serialVersionUID= 7960794038594054563L;

	@Override
	public void procesar(JobExecutionContext jec) throws JobExecutionException {
		Map<String, Object> params = new HashMap<>();
    Map<String, Object> actores= new HashMap<>();
    Encriptar encriptar        = new Encriptar();
		try {
      StringBuilder sb= new StringBuilder();
			if(Configuracion.getInstance().isEtapaProduccion()) {
          LOG.error("----------------ENTRO A CHECK EMAILS-----------------------------");
          Email email= new Email();
          Token token= null;
          List<Entity> correos= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticClientesDto", "correos", params, 500L);
          int count= 1;
          for (Entity item : correos) {
            email.setNombre(item.toString("razonSocial"));
            email.setCorreo(item.toString("valor"));
            token= email.doValidate();
            item.put("valido", new Value("valido", token.getValid()));
            item.put("mensaje", new Value("mensaje", token.getMessage()));
            if(token.getValid()) {
              params.put("idClienteTipoContacto", item.toLong("idClienteTipoContacto"));
              params.put("idValidado", 1L);
              DaoFactory.getInstance().updateAll(TrManticClienteTipoContactoDto.class, params);
            }  // if
            else { 
              params.put("idClienteTipoContacto", item.toLong("idClienteTipoContacto"));
              params.put("idValidado", 3L);
              DaoFactory.getInstance().updateAll(TrManticClienteTipoContactoDto.class, params);
//              DaoFactory.getInstance().delete(TrManticClienteTipoContactoDto.class, item.toLong("idClienteTipoContacto"));
              sb.append(count).append(".- *Cliente:* ").append(item.toString("razonSocial")).append("\\n_Correo:_ ").append(item.toString("valor")).append("\\n_Error:_ ").append(item.toString("mensaje")).append("\\n\\n");
              count++;
            } // else 
          } // for
          LOG.error(sb.toString());
          LOG.error("---------------------------------------------------------");
          actores.put("Alejandro Jiménez García", encriptar.desencriptar("cd4b3e3924191b057b8187"));
          actores.put("Daniel Davalos Gutiérrez", encriptar.desencriptar("443124130375ec53c7c5cd"));
          actores.put("Sandy Martínez Montoya", encriptar.desencriptar("2b160b0a71ea69d54a4cb4"));
          Bonanza notificar= new Bonanza();
          for (String item: actores.keySet()) {
            notificar.setNombre(Cadena.nombrePersona(item));
            notificar.setCelular((String)actores.get(item));
            notificar.setReporte(sb.toString());
            notificar.setFecha(Fecha.formatear(Fecha.DIA_FECHA_HORA_CORTA));
            LOG.info("Enviando mensaje de whatsapp al celular: "+ notificar.getCelular());
            notificar.doSendCorreo();
          } // for
      } // if
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
      LOG.error("Ocurrio un error al realizar la verificación de correos");
		} // catch	
		finally {
			Methods.clean(params);
			Methods.clean(actores);
		} // finally
	} // execute
  
  public static void main(String ... args) throws JobExecutionException {
    Correos correo= new Correos();
    correo.procesar(null);
  }
  
}


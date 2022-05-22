package mx.org.kaana.jobs;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20-mayo-2022
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
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Bonanza;
import mx.org.kaana.libs.wassenger.Rfc;
import mx.org.kaana.libs.wassenger.Sat;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Rfcs extends IBaseJob implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Rfcs.class);
	private static final long serialVersionUID= 7960794038594054563L;

	@Override
	public void procesar(JobExecutionContext jec) throws JobExecutionException {
		Map<String, Object> params = new HashMap<>();
    Map<String, Object> actores= new HashMap<>();
    Encriptar encriptar        = new Encriptar();
		try {
      StringBuilder sb= new StringBuilder();
			if(Configuracion.getInstance().isEtapaProduccion()) {
          LOG.error("----------------ENTRO A CHECK RFC's-----------------------------");
          Rfc rfc  = new Rfc();
          Sat token= null;
          List<Entity> correos= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticClientesDto", "rfcs", params, 500L);
          int count= 1;
          for (Entity item : correos) {
            String valor= item.toString("valor");
            if(valor!= null && valor.trim().length()> 0) {
              valor= valor.replaceAll(" ", "");
              rfc.setRfc(valor);
              token= rfc.doValidate();
              item.put("rfc", new Value("rfc", valor));
              item.put("formato", new Value("formato", token.getFormatoCorrecto()));
              item.put("activo", new Value("activo", token.getActivo()));
              item.put("localizado", new Value("localizado", token.getLocalizado()));
              if(token.getFormatoCorrecto() && token.getActivo() && token.getLocalizado()) {
                params.put("idCliente", item.toLong("idCliente"));
                params.put("idValidado", 1L);
                params.put("rfc", valor);
                DaoFactory.getInstance().updateAll(TcManticClientesDto.class, params);
              }  // if
              else { 
                params.put("idCliente", item.toLong("idCliente"));
                params.put("idValidado", 3L);
                params.put("rfc", valor);
                DaoFactory.getInstance().updateAll(TcManticClientesDto.class, params);
  //              DaoFactory.getInstance().delete(TrManticClienteTipoContactoDto.class, item.toLong("idClienteTipoContacto"));
                sb.append(count).append(".- *Cliente:* ").append(item.toString("razonSocial")).append("\\n_RFC:_ ").append(valor).append("\\n_Formato correcto:_ ").append(item.toString("formato")).append("\\n_Activo:_ ").append(item.toString("activo")).append("\\n_Localizado:_ ").append(item.toString("localizado")).append("\\n\\n");
                count++;
              } // else 
            } // if
            if(sb.length()< 5500)
              break;
          } // for
          LOG.error(sb.toString());
          LOG.error("---------------------------------------------------------");
          if(sb.length()<= 0) 
            sb.append("*Todos los RFC's son correctos por el momento*\\n\\n");
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
            notificar.doSendRfc();
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
    Rfcs correo= new Rfcs();
    correo.procesar(null);
  }
  
}


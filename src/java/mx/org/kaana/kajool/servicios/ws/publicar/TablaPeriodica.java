package mx.org.kaana.kajool.servicios.ws.publicar;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Error;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.servicios.ws.publicar.beans.RespuestaMovil;
import mx.org.kaana.kajool.servicios.ws.publicar.reglas.Transaccion;
import mx.org.kaana.libs.json.Decoder;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Path("/TablaPeriodica")
public class TablaPeriodica implements Serializable {
  
  private static final long serialVersionUID = -7067112384914290020L;

	//Orden de parametros para servicios @Path("/hidrogeno/{nombre}/{simbolo}/{numero}/{serie}/{grupo}/{periodo}/{bloque}/{masa}/{configuracion}/{electrones}")
	// nombre = cuestionario
	// simbolo= visitas
	// numero = idMuestra
	// serie  = cuenta
  @GET
  @Path("/hidrogeno/{nombre}/{simbolo}/{numero}/{serie}")
  @Produces("application/json;charset=ISO-8859-1")
  public String hidrogeno(@PathParam("nombre") String nombre, @PathParam("simbolo") String simbolo, @PathParam("numero") String numero, @PathParam("serie") String serie){
    String regresar               = null;        		
		Transaccion transaccion       = null;		
		Transaccion transaccionIntegra= null;		
    try {			
			transaccion= new Transaccion(numero, serie, nombre, simbolo);
			if(transaccion.ejecutar(EAccion.AGREGAR)) 			
				if(false) { // VALIDACION PARA INTEGRACION AUTOMATICA
				  transaccionIntegra= new Transaccion(transaccion.getBitacora());
					transaccionIntegra.ejecutar(EAccion.PROCESAR);
				} // else				
			regresar= Decoder.toJson(transaccion.getRespuestaMovil());
    } // try
    catch (Exception e) {
      Error.mensaje(e);            
    } // catch
    return regresar;
  } // bajaCaliforniaNorte
  
	public static void main(String[] args) throws Exception {
		// nombre = cuestionario
		// simbolo= visitas
		// numero = idMuestra
		// serie  = cuenta
    String nombre = "{1}"; 
		String simbolo= "{1}"; 
		String numero = "227"; 
		String serie  = "kajool.admin"; 						
		TablaPeriodica tablaPeriodica= new TablaPeriodica();
		tablaPeriodica.hidrogeno(nombre, simbolo, numero, serie);
  }
	
}

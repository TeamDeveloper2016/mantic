package mx.org.kaana.mantic.ws.consumible.reglas;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.mantic.ws.publicar.beans.Respuesta;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Peticion implements Serializable{

	private static final Log LOG              = LogFactory.getLog(Peticion.class);  
	private static final long serialVersionUID= 5441425208790235051L;		
	private static final String SERVIDOR      = "servidor.ws.";		
	private String servicio;
	private Object[] parametros;

	public Peticion(String servicio, Object[] parametros) {
		this.servicio  = servicio;
		this.parametros= parametros;
	}
		
	public String getCadenaRespuesta() throws Exception {
		String regresar= null;
		try {
			regresar= consumir();
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // regresar
	
	public Respuesta getBeanRespuesta() throws Exception {
		Respuesta regresar= null;
		try {
			regresar= (Respuesta) Decoder.toSerializar(Respuesta.class, consumir());
		} // try
		catch (Exception e) {
			throw e; 
		} // catch		
		return regresar;
	} // regresar
	
	public String consumir() throws Exception {    
    Service service= null;
    Call call      = null;
    String regresar= null;    		
    try {     
			LOG.debug("Preparando consumir ws [".concat(this.servicio).concat("]"));
			service= new Service();
      call= (Call) service.createCall();
      call.setTargetEndpointAddress(toEndPoint());
      call.setOperationName(this.servicio);
      regresar = (String) call.invoke(this.parametros);
			LOG.debug("Result ws [".concat(regresar).concat("]"));			
    } // try
    catch (Exception e) {       			
			Error.mensaje(e);
			throw e;			
    } // catch   
    return regresar;
  } // consumir
	
	public String toEndPoint(){
		String regresar= null;
		try {
			regresar= TcConfiguraciones.getInstance().getPropiedad(SERVIDOR.concat(Configuracion.getInstance().getEtapaServidor().toLowerCase()));
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // toEndPoint
}

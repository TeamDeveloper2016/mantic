package mx.org.kaana.mantic.ws.consumible.reglas;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.mantic.ws.publicar.beans.Respuesta;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Conteos implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Conteos.class);  
	private static final long serialVersionUID= 5441415208790235051L;		
	private static final String SERVIDOR      = "51f024d60836392614489d20a75e9b40fc29c7639c4a993882d656f021c0a059f724db77ea60e645dc0827da0dc86496";		
	private String servicio;
	private Object[] parametros;

	public Conteos(String servicio, Object[] parametros) {
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
    Encriptar code = new Encriptar();
    try {     
			LOG.debug("Preparando consumir ws [".concat(this.servicio).concat("]"));
			service= new Service();
      call= (Call) service.createCall();
      call.setTargetEndpointAddress(code.desencriptar(SERVIDOR));
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
	
}

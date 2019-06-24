package mx.org.kaana.mantic.ws.publicar;

import java.io.Serializable;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.mantic.enums.ERespuesta;
import mx.org.kaana.mantic.ws.publicar.beans.Respuesta;
import mx.org.kaana.mantic.ws.publicar.reglas.Manager;

public class Paises implements Serializable{
	
	private static final long serialVersionUID = -6756978815958154700L;
	
	public String argentina() throws Exception{
		String regresar= null;
		Manager manager= null;
		try {
			manager= new Manager();
			regresar= manager.verificaConexion();
		} // try
		catch (Exception e) {			
			regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));			
		} // catch		
		return regresar;
	} // afganistan
	
	public String mexico() throws Exception{
		String regresar= null;
		Manager manager= null;
		try {
			manager= new Manager();
			regresar= manager.ultimoRespaldo();
		} // try
		catch (Exception e) {			
			regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));			
		} // catch		
		return regresar;
	} // albania
}

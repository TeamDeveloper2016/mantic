package mx.org.kaana.mantic.ws.publicar.reglas;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.mantic.enums.ERespuesta;
import mx.org.kaana.mantic.ws.publicar.beans.Respuesta;

public class Manager implements Serializable{

	private static final long serialVersionUID = 3136961119537818420L;

	public String verificaConexion() throws Exception{
		String regresar= null;
		try {
			regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Conexión exitosa"));
		} // try
		catch (Exception e) {						
			Error.mensaje(e);			
			throw e;
		} // catch		
		return regresar;
	} // verificaConexion
	
	public String ultimoRespaldo() throws Exception{
		String regresar = null;
		GestorSQL gestor= null;
		try {
			gestor= new GestorSQL();
			regresar= gestor.ultimoRespaldo();
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		return regresar;
	} // ultimoRespaldo
}

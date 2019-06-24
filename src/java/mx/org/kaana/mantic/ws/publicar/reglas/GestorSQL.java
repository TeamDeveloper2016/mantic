package mx.org.kaana.mantic.ws.publicar.reglas;

import java.io.Serializable;
import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.mantic.enums.ERespuesta;
import mx.org.kaana.mantic.ws.publicar.beans.Respuesta;

public class GestorSQL implements Serializable{
	
	private static final long serialVersionUID = -617931697742735505L;
	
	public String ultimoRespaldo() throws Exception{
		String regresar    = null;
		Entity respaldo    = null;
		Respuesta respuesta= null;
		try {
			respaldo= (Entity) DaoFactory.getInstance().toEntity("TcManticRespaldosDto", "ultimo", Collections.EMPTY_MAP);
			if(respaldo!= null){
				respuesta= new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Se obtuvo el último respaldo correctamente.");
				respuesta.setNombre(respaldo.toString("nombre"));
				respuesta.setRuta(respaldo.toString("ruta"));
				regresar= Decoder.toJson(respuesta);
			} // if
			else
				regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_RESPALDO.getCodigo(), ERespuesta.SIN_RESPALDO.getDescripcion()));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // ultimoRespaldo
}

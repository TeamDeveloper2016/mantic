package mx.org.kaana.mantic.inventarios.entradas.reglas;

import java.io.Serializable;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.mantic.db.dto.TcManticNotasArchivosDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Importados extends IBaseTnx implements Serializable {

  private static final Logger LOG = Logger.getLogger(Importados.class);
	private static final long serialVersionUID=-6063204157451117549L;
 
	private TcManticNotasArchivosDto file;
	private String messageError;

	public Importados(TcManticNotasArchivosDto file) {
		this.file= file;
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" al importar el archivo.");
			switch(accion) {
				case AGREGAR:
					regresar= DaoFactory.getInstance().updateAll(sesion, TcManticNotasArchivosDto.class, this.file.toMap())>= 1L;
					regresar= DaoFactory.getInstance().insert(sesion, this.file)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		LOG.info("Se agrego el archivo de forma correcta: "+ this.file.getNombre());
		return regresar;
	}	// ejecutar

} 
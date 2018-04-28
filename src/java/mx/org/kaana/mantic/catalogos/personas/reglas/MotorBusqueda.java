
package mx.org.kaana.mantic.catalogos.personas.reglas;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

public class MotorBusqueda implements Serializable{

	private static final long serialVersionUID= 5366287658013154045L;	
	private Long idPersona;

	public MotorBusqueda(Long idPersona) {
		this.idPersona = idPersona;
	}

	public TcManticPersonasDto toPersona() throws Exception {
		TcManticPersonasDto regresar= null;
		try {
			regresar= (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, this.idPersona);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPersona	
}

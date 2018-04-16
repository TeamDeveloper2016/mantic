
package mx.org.kaana.mantic.catalogos.articulos.reglas;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcManticArticulosDto;

public class MotorBusqueda implements Serializable{
	
	private Long idArticulo;

	public MotorBusqueda(Long idArticulo) {
		this.idArticulo = idArticulo;
	}

	public TcManticArticulosDto toArticulo() throws Exception {
		TcManticArticulosDto regresar= null;
		try {
			regresar= (TcManticArticulosDto) DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idArticulo);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toArticulo
}

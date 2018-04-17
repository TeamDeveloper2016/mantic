
package mx.org.kaana.mantic.catalogos.articulos.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private TcManticArticulosDto articulo;
	private Long codigo;
	private String observaciones;

	public Transaccion(TcManticArticulosDto articulo, Long codigo, String observaciones) {
		this.articulo     = articulo;
		this.codigo       = codigo;
		this.observaciones= observaciones;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {
			switch(accion){
				case AGREGAR:
					regresar= DaoFactory.getInstance().insert(sesion, this.articulo)>= 1L;
					break;
				case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.articulo)>= 1L;
					break;
				case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, TcManticArticulosDto.class, this.articulo.getKey())>= 1L;
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // ejecutar
}

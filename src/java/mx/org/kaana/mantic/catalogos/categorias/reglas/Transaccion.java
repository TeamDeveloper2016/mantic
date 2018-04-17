
package mx.org.kaana.mantic.catalogos.categorias.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.mantic.db.dto.TcManticCategoriasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{
	
	private TcManticCategoriasDto dto;

	public Transaccion(TcManticCategoriasDto dto) {
		this.dto = dto;
	} // Transaccion

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {
			switch(accion){
				case AGREGAR:
					regresar= DaoFactory.getInstance().insert(sesion, this.dto)>= 1L;
					break;
				case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.dto)>= 1L;
					break;
				case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, TcManticCategoriasDto.class, this.dto.getKey())>= 1L;
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // ejecutar
}

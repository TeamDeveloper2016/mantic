package mx.org.kaana.mantic.catalogos.trabajos.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticTrabajosDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{
	
  private TcManticTrabajosDto trabajo;
  private String messageError;

	public Transaccion(TcManticTrabajosDto trabajo) {
		this.trabajo= trabajo;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		 boolean regresar= false;
    try {			
      switch (accion) {
        case AGREGAR:
          regresar= DaoFactory.getInstance().insert(sesion, this.trabajo)>= 1L;
          break;
        case MODIFICAR:
          regresar= DaoFactory.getInstance().update(sesion, this.trabajo)>= 1L;
          break;
        case ELIMINAR:
          regresar= DaoFactory.getInstance().delete(sesion, this.trabajo)>= 1L;;
          break;				
      } // switch
      if (!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
	} // ejecutar	
}
package mx.org.kaana.mantic.catalogos.cajas.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{
	
  private TcManticCajasDto caja;
  private String messageError;

	public Transaccion(TcManticCajasDto caja) {
		this.caja= caja;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		 boolean regresar= false;
    try {			
      switch (accion) {
        case AGREGAR:
          regresar= DaoFactory.getInstance().insert(sesion, this.caja)>= 1L;
          break;
        case MODIFICAR:
          regresar= DaoFactory.getInstance().update(sesion, this.caja)>= 1L;
          break;
        case ELIMINAR:
          regresar= DaoFactory.getInstance().delete(sesion, this.caja)>= 1L;;
          break;				
      } // switch
      if (!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
    } // catch		
    return regresar;
	} // ejecutar	
}
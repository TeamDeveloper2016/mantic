package mx.org.kaana.mantic.catalogos.portales.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticPortalesDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {
	
	private TcManticPortalesDto portal;
  private String messageError;

	public Transaccion(TcManticPortalesDto portal) {
		this.portal = portal;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
    try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el portal.");
      switch (accion) {
        case AGREGAR:
					regresar= DaoFactory.getInstance().insert(sesion, this.portal)>= 1L;
          break;
        case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.portal)>= 1L;
          break;
        case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.portal)>= 1L;
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

package mx.org.kaana.mantic.catalogos.listasprecios.reglas;

import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private static final Log LOG = LogFactory.getLog(Transaccion.class);
  private IBaseDto dto;
	private String messageError;
	
	public Transaccion(IBaseDto dto) {
		this.dto= dto;
	}
  
  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {
			switch (accion) {
        case AGREGAR:
          regresar = procesarListaProveedor(sesion);
          break;
        case ELIMINAR:
          regresar = procesarListaProveedor(sesion);
          break;
      } // switch
      if (!regresar) {
        throw new Exception(this.messageError);
      } // if
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
    } // catch		
    return regresar;
  } // ejecutar
	
	private boolean procesarListaProveedor(Session sesion) throws Exception {
    boolean regresar = false;
    try {
      regresar = DaoFactory.getInstance().insert(sesion, (TcManticListasPreciosDto)this.dto)>0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarListaProveedor
}

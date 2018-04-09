package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.notificacion.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/09/2015
 *@time 10:51:59 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesUsuariosDto;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);

  private TrJanalMensajesUsuariosDto dto;
  private Map<String, Object> params;

	public Transaccion (TrJanalMensajesUsuariosDto dto) {
		this.dto= dto;
  }
	
  public Transaccion (TrJanalMensajesUsuariosDto dto, Map<String, Object> params) {
		this.dto   = dto;
    this.params= params;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar	= false;
    try {
      switch(accion) {
        case AGREGAR :
          regresar	= DaoFactory.getInstance().insert(sesion, this.dto) >= 1;
        break;
        case MODIFICAR :
          regresar	= DaoFactory.getInstance().update(sesion, TrJanalMensajesUsuariosDto.class, this.dto.getKey(), this.params) >= 1;
        break;
        case ELIMINAR :
          regresar	= DaoFactory.getInstance().delete(sesion, this.dto) >= 1;
        break;
      } // switch
			if(!regresar)
				throw new RuntimeException("No se modifico ningun registro");
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }
}

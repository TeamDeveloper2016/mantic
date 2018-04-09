package mx.org.kaana.kajool.procesos.menu.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/09/2015
 *@time 03:33:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import org.hibernate.Session;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.pagina.UIMessage;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TrJanalBuzonDto;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);

  private TrJanalBuzonDto dto;

	public Transaccion (TrJanalBuzonDto dto) {
		this.dto= dto;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    try {
      switch(accion) {
        case AGREGAR :
          if(DaoFactory.getInstance().insert(sesion, this.dto)< 1)
            throw new KajoolBaseException(UIMessage.toMessage("error_agregar"));
        break;
      } // switch
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? [buzón]."));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return true;
  }
}

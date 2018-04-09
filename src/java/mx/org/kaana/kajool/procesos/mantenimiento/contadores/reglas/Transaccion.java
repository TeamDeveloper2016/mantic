package mx.org.kaana.kajool.procesos.mantenimiento.contadores.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 10/09/2015
 * @time 06:36:38 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TcJanalContadorAyudasDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private TcJanalContadorAyudasDto dto;

  public Transaccion (TcJanalContadorAyudasDto dto){
    this.dto = dto;
  }

  @Override
  public boolean ejecutar(Session session, EAccion accion) throws Exception, RuntimeException {
    boolean regresar= false;
	  switch (accion) {
      case AGREGAR:
        regresar= DaoFactory.getInstance().insert(session, this.dto).intValue()> 0;
        break;
    } // switch
    return regresar;
  } // ejecutar

}

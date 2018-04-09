package mx.org.kaana.kajool.mantenimiento.contadores.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TcJanalContadorAyudasDto;
import org.hibernate.Session;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 2/12/2014
 * @time 05:22:50 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

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
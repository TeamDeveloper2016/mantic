package mx.org.kaana.kajool.procesos.mantenimiento.temas.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 10/09/2015
 * @time 13:10:55 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalEmpleadosDto;

public class Transaccion extends IBaseTnx {

  private TcJanalEmpleadosDto dto;

  public Transaccion(TcJanalEmpleadosDto dto){
    this.dto= dto;
  }

  @Override
  public boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar =false;
    switch (accion) {
      case MODIFICAR:
        regresar= DaoFactory.getInstance().update(sesion, (IBaseDto) this.dto).intValue()> 0;
        break;
    } // switch
    return regresar;
  } // ejecutar

} // Transaccion

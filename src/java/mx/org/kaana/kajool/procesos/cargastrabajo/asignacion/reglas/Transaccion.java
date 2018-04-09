package mx.org.kaana.kajool.procesos.cargastrabajo.asignacion.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 07:11:14 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TcJanalMuestrasDto;
import mx.org.kaana.kajool.db.dto.TrJanalMovimientosDto;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
  private List<TrJanalMovimientosDto> movimientos;

  public Transaccion(List<TrJanalMovimientosDto> movimientos) {
    this.movimientos = movimientos;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar          = false;
		Long idMovimiento         = null;
    Map<String, Object> params= null;
    try {
			for (TrJanalMovimientosDto dtos: this.movimientos) {
        switch (accion) {
          case AGREGAR:
            LOG.info("Agregando movimientos...");
            idMovimiento= DaoFactory.getInstance().insert(sesion, dtos);
            updateMovimiento(dtos.getIdMuestra(), sesion, idMovimiento);
            break;
          case MODIFICAR:
            if(DaoFactory.getInstance().update(sesion, dtos)<= 0)
              throw new RuntimeException("No se pudo actualizar la muestra correctamente");
            break;
          case ELIMINAR:
            if(DaoFactory.getInstance().delete(sesion, dtos)<= 0)
              throw new RuntimeException("No se pudo actualizar la muestra correctamente");
            break;
				} // switch
      } // for
      regresar= true;
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch
    finally{
     Methods.clean(params);
    } // finally
    return regresar;
  } // ejecutar

  private void updateMovimiento(Long idMuestra, Session sesion, Long idMovimiento) throws Exception {
    Map<String, Object> params= null;
    try {
      LOG.info("Actualizando id_movimiento de tc_muestra..." + idMuestra);
      params= new HashMap<>();
      params.put("idMovimiento", idMovimiento);
			if(DaoFactory.getInstance().update(sesion, TcJanalMuestrasDto.class, idMuestra, params) <= 0)
				throw new RuntimeException("No se pudo actualizar la muestra correctamente");
    } // try // try
		catch (Exception e) {
			throw e;
		} // catch
  } // updateMovimiento
}

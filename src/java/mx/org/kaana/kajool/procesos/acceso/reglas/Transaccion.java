package mx.org.kaana.kajool.procesos.acceso.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TrJanalSesionesDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.kajool.seguridad.quartz.Especial;
import org.hibernate.Session;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 31/08/2015
 * @time 05:58:35 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx {

  private String session;

  public Transaccion() {
    this("");
  } // Transaccion

  public Transaccion(String session) {
    this.session= session;
  } // Transaccion

	@Override
  protected boolean ejecutar(Session sn, EAccion accion) throws Exception {
    boolean regresar= false;
    switch (accion) {
			case AGREGAR:
				regresar= insert(sn);
        break;
      case COMPLEMENTAR:
        regresar= update(sn);
        break;
    } // switch accion
    return regresar;
  } // ejecutar
	
  private boolean insert(Session sn) throws Exception {
		TrJanalSesionesDto dto = new TrJanalSesionesDto();
		Autentifica autentifica= JsfBase.getAutentifica();
    dto.setSesion(JsfBase.getSessionId());
    dto.setPath(Especial.getInstance().getPath());
    dto.setInicio(Especial.getInstance().getRegistro());
    dto.setRegistroFin(null);
    dto.setCuenta(autentifica.getCredenciales().getCuenta());
    dto.setIdUsuario(autentifica.getEmpleado().getIdUsuario());		
		return DaoFactory.getInstance().insert(sn, dto)>= 1;
  } // insert

  private boolean update(Session sn) throws Exception {
		boolean regresar          = false;
		Long afectados            = -1L;
		Map<String, Object> params= null;
		try {
			params= new HashMap<>();
			params.put("path", Especial.getInstance().getPath().endsWith("\\")? Especial.getInstance().getPath().concat("\\"): Especial.getInstance().getPath());
			params.put("sesion", this.session);
			params.put("inicio", Especial.getInstance().getRegistro());
			regresar= DaoFactory.getInstance().execute(ESql.UPDATE, sn, "TrJanalSesionesDto", "updateRegistroFin", params)> afectados;			
		} // try
		catch (Exception e) {
			throw e;
		}// catch	
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // update
}

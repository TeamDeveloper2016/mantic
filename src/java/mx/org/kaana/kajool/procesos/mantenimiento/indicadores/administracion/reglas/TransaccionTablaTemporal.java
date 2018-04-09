package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 23, 2015
 *@time 2:45:51 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TcJanalTablasTempAvaDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransaccionTablaTemporal extends IBaseTnx {

	private static final Log LOG = LogFactory.getLog(TransaccionTablaTemporal.class);
	private TcJanalTablasTempAvaDto temporalDto;
	private Long idKey;
	
	public TransaccionTablaTemporal(Long idKey){
		this(null, idKey);
	}

	public TransaccionTablaTemporal(TcJanalTablasTempAvaDto temporalDto){
		this(temporalDto, -1L);
	}
	
	public TransaccionTablaTemporal(TcJanalTablasTempAvaDto temporalDto, Long idKey){
		this.temporalDto= temporalDto;
		this.idKey= idKey;
	}
		
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar	= false;
    try {
      switch(accion) {
        case AGREGAR :
          regresar= DaoFactory.getInstance().insert(sesion, this.temporalDto)>= 1L;
        break;
        case MODIFICAR :
					regresar= DaoFactory.getInstance().update(sesion, this.temporalDto)>= 1L;
        break;
        case ELIMINAR :
					regresar= DaoFactory.getInstance().delete(sesion, TcJanalTablasTempAvaDto.class, this.idKey)>= 1L;
        break;
      } // switch
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
	}

}

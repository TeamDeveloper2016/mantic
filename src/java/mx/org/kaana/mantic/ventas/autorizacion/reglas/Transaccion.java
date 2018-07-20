package mx.org.kaana.mantic.ventas.autorizacion.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private static final Long AUTORIZAR= 1L;
	private Long idVenta;

	public Transaccion(Long idVenta) {
		this.idVenta= idVenta;
	}	// Transaccion
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar         = false;
		Map<String, Object>params= null;
		try {
			switch(accion){
				case MODIFICAR:
					params= new HashMap<>();
					params.put("idAutorizar", AUTORIZAR);
					regresar= DaoFactory.getInstance().update(sesion, TcManticVentasDto.class, this.idVenta, params)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(e.getMessage());
		} // catch				
		return regresar;
	} // ejecutar
}

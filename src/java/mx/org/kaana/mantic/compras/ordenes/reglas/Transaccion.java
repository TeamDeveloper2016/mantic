package mx.org.kaana.mantic.compras.ordenes.reglas;

/**
 *@company INEGI
 *@project IKTAN (Sistema de seguimiento y control de proyectos)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Alejandro Jimenez Garcia <alejandro.jimenez@inegi.org.mx>
 */

import java.util.HashMap;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
 
	private TcManticOrdenesComprasDto dto;
	private String messageError;
	
	public Transaccion(TcManticOrdenesComprasDto dto) {
	  this.dto= dto;
	}
	
  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar          = false;
		Map<String, Object> params= new HashMap<>();
		try {
			//params.put("iva", this.dto.getImporte());
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" registrar la orden de compra.");
			switch(accion){
				case AGREGAR:
					regresar= DaoFactory.getInstance().insert(sesion, this.dto)>= 1L;
					break;
				case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.dto)>= 1L;
					break;				
				case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception(this.messageError);
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError);
		} // catch		
		return regresar;
  }
	
} 
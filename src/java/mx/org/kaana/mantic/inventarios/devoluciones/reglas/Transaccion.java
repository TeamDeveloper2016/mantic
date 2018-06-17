package mx.org.kaana.mantic.inventarios.devoluciones.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
	private static final long serialVersionUID=-6069204157451117543L;
 
	private TcManticDevolucionesDto orden;	
	private List<Articulo> articulos;
	private String messageError;
	private TcManticDevolucionesBitacoraDto bitacora;

	public Transaccion(TcManticDevolucionesDto orden, TcManticDevolucionesBitacoraDto bitacora) {
		this(orden);
		this.bitacora= bitacora;
	}
	
	public Transaccion(TcManticDevolucionesDto orden) {
		this(orden, new ArrayList<Articulo>());
	}

	public Transaccion(TcManticDevolucionesDto orden, List<Articulo> articulos) {
		this.orden    = orden;		
		this.articulos= articulos;
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		Map<String, Object> params= null;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la devolución de entrada.");
			switch(accion) {
				case AGREGAR:
					break;				
				case ELIMINAR:
					break;
				case JUSTIFICAR:
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		LOG.info("Se genero de forma correcta la devolucion de entrada: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar
	
} 
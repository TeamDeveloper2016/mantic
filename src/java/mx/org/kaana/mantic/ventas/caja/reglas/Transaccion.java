package mx.org.kaana.mantic.ventas.caja.reglas;

import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import org.apache.log4j.Logger;
import org.hibernate.Session;

public class Transaccion extends mx.org.kaana.mantic.ventas.reglas.Transaccion{

	private static final Logger LOG  = Logger.getLogger(mx.org.kaana.mantic.ventas.reglas.Transaccion.class);
	private VentaFinalizada ventaFinalizada;

	public Transaccion(VentaFinalizada ventaFinalizada) {
		super(ventaFinalizada.getTicketVenta());
		this.ventaFinalizada = ventaFinalizada;
	}	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar          = false;
		try {						
			switch(accion) {					
				case REPROCESAR:				
					regresar= super.ejecutar(sesion, accion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(getMessageError().concat("\n\n")+ e.getMessage());
		} // catch		
		if(this.ventaFinalizada.getTicketVenta()!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.ventaFinalizada.getTicketVenta().getConsecutivo());
		return regresar;
	} // ejecutar
}

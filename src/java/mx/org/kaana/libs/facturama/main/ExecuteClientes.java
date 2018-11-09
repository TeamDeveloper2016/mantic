package mx.org.kaana.libs.facturama.main;

import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Error;

public class ExecuteClientes {

	public static void main(String[] args) {
		TransaccionFactura transaccion= null;
		try {
			transaccion= new TransaccionFactura();
			transaccion.ejecutar(EAccion.PROCESAR);				
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
	}	// main
}
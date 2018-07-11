package mx.org.kaana.mantic.catalogos.clientes.cuentas.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private String messageError;
	private TcManticClientesPagosDto pago;

	public Transaccion(TcManticClientesPagosDto pago) {
		this.pago = pago;
	} // Transaccion
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
    try {			
      switch (accion) {
        case AGREGAR:
          regresar = procesarPago(sesion);
          break;       
      } // switch
      if (!regresar) 
        throw new Exception("");      
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
    } // catch		
    return regresar;
	} // ejecutar
	
	private boolean procesarPago(Session sesion) throws Exception{
		boolean regresar               = false;
		TcManticClientesDeudasDto deuda= null;
		Double saldo                   = 0D;
		try {
			if(DaoFactory.getInstance().insert(sesion, this.pago)>= 1L){
				deuda= (TcManticClientesDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDeudasDto.class, this.pago.getIdClienteDeuda());
				saldo= deuda.getSaldo() - this.pago.getPago();
				deuda.setSaldo(saldo);
				deuda.setIdClienteEstatus(saldo.equals(0L) ? 3L : 2L);
				regresar= DaoFactory.getInstance().update(sesion, deuda)>= 1L;
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarPago
}

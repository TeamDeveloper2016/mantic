package mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private String messageError;
	private TcManticEmpresasPagosDto pago;

	public Transaccion(TcManticEmpresasPagosDto pago) {
		this.pago= pago;
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
		TcManticEmpresasDeudasDto deuda= null;
		Double saldo                   = 0D;
		try {
			if(DaoFactory.getInstance().insert(sesion, this.pago)>= 1L){
				deuda= (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.pago.getIdEmpresaDeuda());
				saldo= deuda.getSaldo() - this.pago.getPago();
				deuda.setSaldo(saldo);
				deuda.setIdEmpresaEstatus(saldo.equals(0L) ? 3L : 2L);
				regresar= DaoFactory.getInstance().update(sesion, deuda)>= 1L;
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally{
			this.messageError= "Error al registrar el pago";
		} // finally
		return regresar;
	} // procesarPago
}

package mx.org.kaana.mantic.catalogos.clientes.cuentas.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosDto;
import mx.org.kaana.mantic.enums.EEstatusClientes;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private String messageError;
	private TcManticClientesPagosDto pago;
	private Long idCliente;

	public Transaccion(TcManticClientesPagosDto pago) {
		this(pago, -1L);
	} // Transaccion
	
	public Transaccion(TcManticClientesPagosDto pago, Long idCliente) {
		this.pago     = pago;
		this.idCliente= idCliente;
	} // Transaccion
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
    try {			
      switch (accion) {
        case AGREGAR:
          regresar = procesarPago(sesion);
          break;       
        case PROCESAR:
          regresar = procesarPagoGeneral(sesion);
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
		finally{
			this.messageError= "Error al registrar el pago";
		} // finally
		return regresar;
	} // procesarPago
	
	private boolean procesarPagoGeneral(Session sesion) throws Exception{		
		boolean regresar         = true;
		List<Entity> deudas      = null;		
		Map<String, Object>params= null;
		Double saldo             = 1D;
		Double saldoDeuda        = 0D;				
		Double pagoParcial       = 0D;				
		Double abono             = 0D;		
		Long idEstatus           = -1L;
		try {
			deudas= toDeudas(sesion);
			for(Entity deuda: deudas){
				if(saldo > 0){					
					saldoDeuda= Double.valueOf(deuda.toString("saldo"));
					if(saldoDeuda < this.pago.getPago()){
						pagoParcial= saldoDeuda;
						saldo= this.pago.getPago() - saldoDeuda;						
						this.pago.setPago(saldo);
						abono= 0D;
						idEstatus= EEstatusClientes.FINALIZADA.getIdEstatus();
					} // if
					else{						
						pagoParcial= this.pago.getPago();
						saldo= 0D;
						abono= saldoDeuda - this.pago.getPago();
						idEstatus= saldoDeuda.equals(this.pago.getPago()) ? EEstatusClientes.FINALIZADA.getIdEstatus() : EEstatusClientes.PARCIALIZADA.getIdEstatus();
					} /// else
					if(registrarPago(sesion, deuda.getKey(), pagoParcial)){
						params= new HashMap<>();
						params.put("saldo", abono);
						params.put("idClienteEstatus", idEstatus);
						DaoFactory.getInstance().update(sesion, TcManticClientesDeudasDto.class, deuda.getKey(), params);
					}	// if				
				} // if
			} // for
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			this.messageError= "Error al registrar el pago";
			Methods.clean(params);			
		} // finally
		return regresar;
	} // procesarPagoGeneral
	
	private boolean registrarPago(Session sesion, Long idClienteDeuda, Double pagoParcial) throws Exception{
		TcManticClientesPagosDto registroPago= null;
		boolean regresar                     = false;
		try {
			registroPago= new TcManticClientesPagosDto();
			registroPago.setIdClienteDeuda(idClienteDeuda);
			registroPago.setIdUsuario(JsfBase.getIdUsuario());
			registroPago.setObservaciones("Pago aplicado a la deuda general del cliente. ".concat(this.pago.getObservaciones()));
			registroPago.setPago(pagoParcial);
			registroPago.setIdTipoMedioPago(this.pago.getIdTipoMedioPago());
			regresar= DaoFactory.getInstance().insert(sesion, registroPago)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarPago
	
	private List<Entity> toDeudas(Session sesion) throws Exception{
		List<Entity> regresar    = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.idCliente);
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			params.put("sortOrder", "order by tc_mantic_clientes_deudas.registro");
			regresar= DaoFactory.getInstance().toEntitySet(sesion, "VistaClientesDto", "cuentas", params);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDeudas	
}

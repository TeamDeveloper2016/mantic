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
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private List<Entity> cuentas;
	private String messageError;
	private TcManticClientesPagosDto pago;
	private Long idCliente;
	private Long idCaja;
	private Long idEmpresa;
	private Long idCierreActivo;
	private Long idBanco;
	private String referencia;
	private Double pagoGeneral;
	private boolean saldar;

	public Transaccion(TcManticClientesPagosDto pago, Long idCaja, Long idEmpresa, Long idBanco, String referencia, boolean saldar) {
		this(pago, idCaja, -1L, idEmpresa, idBanco, referencia, saldar);
	} // Transaccion
	
	public Transaccion(TcManticClientesPagosDto pago, Long idCaja, Long idCliente, Long idEmpresa, Long idBanco, String referencia, boolean saldar) {
		this(pago, idCaja, idCliente, idEmpresa, idBanco, referencia, null, saldar);
	}
	
	public Transaccion(TcManticClientesPagosDto pago, Long idCaja, Long idCliente, Long idEmpresa, Long idBanco, String referencia, List<Entity> cuentas, boolean saldar) {
		this.pago      = pago;
		this.idCliente = idCliente;
		this.idCaja    = idCaja;
		this.idEmpresa = idEmpresa;
		this.idBanco   = idBanco;
		this.referencia= referencia;
		this.cuentas   = cuentas;
		this.saldar    = saldar;
	} // Transaccion
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
    try {			
			this.pagoGeneral= this.pago.getPago();
      switch (accion) {
        case AGREGAR:					
						regresar = procesarPago(sesion);
          break;       
        case PROCESAR:					
						regresar = procesarPagoGeneral(sesion);
          break;       
				case COMPLEMENTAR: 
					regresar = procesarPagoSegmento(sesion);
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
			if(toCierreCaja(sesion, this.pago.getPago())){
				this.pago.setIdCierre(this.idCierreActivo);				
				if(!this.pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
					this.pago.setReferencia(this.referencia);
					this.pago.setIdBanco(this.idBanco);
				} // if
				if(DaoFactory.getInstance().insert(sesion, this.pago)>= 1L){
					deuda= (TcManticClientesDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDeudasDto.class, this.pago.getIdClienteDeuda());
					saldo= deuda.getSaldo() - this.pago.getPago();
					deuda.setSaldo(saldo);
					deuda.setIdClienteEstatus(saldo.equals(0L) || this.saldar ? 3L : 2L);
					regresar= DaoFactory.getInstance().update(sesion, deuda)>= 1L;
				} // if
			}
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
						idEstatus= this.saldar ? EEstatusClientes.FINALIZADA.getIdEstatus() : (saldoDeuda.equals(this.pago.getPago()) ? EEstatusClientes.FINALIZADA.getIdEstatus() : EEstatusClientes.PARCIALIZADA.getIdEstatus());
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
			if(toCierreCaja(sesion, pagoParcial)){
				registroPago= new TcManticClientesPagosDto();
				registroPago.setIdClienteDeuda(idClienteDeuda);
				registroPago.setIdUsuario(JsfBase.getIdUsuario());
				registroPago.setObservaciones("Pago aplicado a la deuda general del cliente. ".concat(this.pago.getObservaciones()).concat(". Pago general por $").concat(this.pagoGeneral.toString()));
				registroPago.setPago(pagoParcial);
				registroPago.setIdTipoMedioPago(this.pago.getIdTipoMedioPago());
				registroPago.setIdCierre(this.idCierreActivo);
				if(!this.pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
					registroPago.setIdBanco(this.idBanco);
					registroPago.setReferencia(this.referencia);
				} // if
				regresar= DaoFactory.getInstance().insert(sesion, registroPago)>= 1L;
			} // if
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
			params.put(Constantes.SQL_CONDICION, " tc_mantic_clientes_deudas.saldo > 0 and tc_mantic_clientes_deudas.id_cliente_estatus not in(".concat(EEstatusClientes.FINALIZADA.getIdEstatus().toString()).concat(")"));			
			params.put("sortOrder", "order by tc_mantic_clientes_deudas.registro");
			regresar= DaoFactory.getInstance().toEntitySet(sesion, "VistaClientesDto", "cuentas", params);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDeudas	
	
	private boolean toCierreCaja(Session sesion, Double pago) throws Exception{
		mx.org.kaana.mantic.ventas.caja.reglas.Transaccion cierre= null;
		VentaFinalizada datosCierre= null;
		boolean regresar= false;
		try {
			datosCierre= new VentaFinalizada();
			datosCierre.getTicketVenta().setIdEmpresa(this.idEmpresa);
			datosCierre.setIdCaja(this.idCaja);
			datosCierre.getTotales().setEfectivo(pago);
			cierre= new mx.org.kaana.mantic.ventas.caja.reglas.Transaccion(datosCierre);
			if(cierre.verificarCierreCaja(sesion)){
				this.idCierreActivo= cierre.getIdCierreVigente();
				regresar= cierre.alterarCierreCaja(sesion, this.pago.getIdTipoMedioPago());
			} // if
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // toCierreCaja
	
	private boolean procesarPagoSegmento(Session sesion) throws Exception{		
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
				for(Entity cuenta: this.cuentas){
					if(deuda.getKey().equals(cuenta.getKey())){
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
								idEstatus= this.saldar ? EEstatusClientes.FINALIZADA.getIdEstatus() : (saldoDeuda.equals(this.pago.getPago()) ? EEstatusClientes.FINALIZADA.getIdEstatus() : EEstatusClientes.PARCIALIZADA.getIdEstatus());
							} /// else
							if(registrarPago(sesion, deuda.getKey(), pagoParcial)){
								params= new HashMap<>();
								params.put("saldo", abono);
								params.put("idClienteEstatus", idEstatus);
								DaoFactory.getInstance().update(sesion, TcManticClientesDeudasDto.class, deuda.getKey(), params);
							}	// if				
						} // if
						else if (this.saldar){
							if(registrarPago(sesion, deuda.getKey(), 0D)){
								params= new HashMap<>();
								params.put("saldo", 0);
								params.put("idClienteEstatus", EEstatusClientes.FINALIZADA.getIdEstatus());
								DaoFactory.getInstance().update(sesion, TcManticClientesDeudasDto.class, deuda.getKey(), params);
							}	// if				
						}
					} // if
				} // for
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
}

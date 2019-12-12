package mx.org.kaana.mantic.ventas.garantias.reglas;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import mx.org.kaana.mantic.db.dto.TrManticGarantiaMedioPagoDto;
import mx.org.kaana.mantic.enums.EEstatusClientes;
import mx.org.kaana.mantic.enums.EEstatusGarantias;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Cierre;
import mx.org.kaana.mantic.ventas.garantias.beans.DetalleGarantia;
import mx.org.kaana.mantic.ventas.garantias.beans.Garantia;
import org.apache.log4j.Logger;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private static final Logger LOG          = Logger.getLogger(Transaccion.class);
	private static final String GENERAL      = "GENERAL";
	private static final String CIERRE_ACTIVO= "1,2";	
	private TcManticGarantiasDto garantiaDto;	
	private DetalleGarantia detalleGarantia;	
	private Garantia garantia;	
	private IBaseDto dto;
	private String justificacion;
	private String messageError;		
	private Long idCierreVigente;
	private Double pagoPivote;	
	List<String> tickets;
	
	public Transaccion(IBaseDto dto) {
		this(null, dto);
	} // Transaccion	
	
	public Transaccion(Garantia garantia) {
		this(garantia, null);
	} // Transaccion

	public Transaccion(Garantia garantia, IBaseDto dto) {
		this.garantia  = garantia;		
		this.dto       = dto;
		this.pagoPivote= 0D;
	}	// Transaccion	

	public Transaccion(TcManticGarantiasDto garantiaDto, String justificacion) {
		this.garantiaDto  = garantiaDto;
		this.justificacion= justificacion;
		this.pagoPivote   = 0D;
	} // Transaccion

	public Transaccion(DetalleGarantia detalleGarantia) {
		this.detalleGarantia= detalleGarantia;
	} // Transaccion	
	
	public Long getIdCierreVigente() {
		return idCierreVigente;
	}	

	public String getMessageError() {
		return messageError;
	}

	public TcManticGarantiasDto getGarantiaDto() {
		return garantiaDto;
	}		

	public List<String> getTickets() {
		return tickets;
	}
				
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar      = false;
		Long idEstatusGarantia= 1L;
		try {									
			switch(accion) {					
				case REPROCESAR:				
					this.tickets= new ArrayList<>();
					regresar= procesarGarantia(sesion);
					break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;		
				case ELIMINAR:
					idEstatusGarantia= EEstatusGarantias.ELIMINADA.getIdEstatusGarantia();
					this.garantiaDto= (TcManticGarantiasDto) DaoFactory.getInstance().findById(sesion, TcManticGarantiasDto.class, this.garantiaDto.getIdGarantia());
					this.garantiaDto.setIdGarantiaEstatus(idEstatusGarantia);					
					if(DaoFactory.getInstance().update(sesion, this.garantiaDto)>= 1L)
						regresar= registraBitacora(sesion, this.garantiaDto.getIdGarantia(), idEstatusGarantia, this.justificacion);					
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.dto)>= 1L){
						this.garantiaDto= (TcManticGarantiasDto) DaoFactory.getInstance().findById(sesion, TcManticGarantiasDto.class, ((TcManticGarantiasBitacoraDto)this.dto).getIdGarantia());
						this.garantiaDto.setIdGarantiaEstatus(((TcManticGarantiasBitacoraDto)this.dto).getIdGarantiaEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.garantiaDto)>= 1L;
					} // if
					break;				
			} // switch
			if(!regresar)
        throw new Exception(getMessageError());
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(getMessageError().concat("\n\n")+ e.getMessage());
		} // catch		
		if(this.garantia!= null && this.garantia.getTicketVenta()!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.garantia.getTicketVenta().getConsecutivo());
		return regresar;
	} // ejecutar		
	
	private boolean procesarGarantia(Session sesion) throws Exception {
		boolean regresar= false;
		Long idEstatus  = -1L;				
		try {
			for(Garantia newGarantia: this.detalleGarantia.getGarantias()){
				if(newGarantia.getArticulosGarantia().size()> 0){
					this.garantia= newGarantia;
					idEstatus= this.detalleGarantia.getPagoGarantia().getIdTipoVenta().equals(1L) ? EEstatusGarantias.TERMINADA.getIdEstatusGarantia() : this.garantia.getIdEfectivo().equals(Constantes.SI) ? EEstatusGarantias.TERMINADA.getIdEstatusGarantia() : EEstatusGarantias.RECIBIDA.getIdEstatusGarantia();
					this.generarGarantia(sesion, idEstatus);					
					if(this.verificarCierreCaja(sesion)){
						if(this.detalleGarantia.getPagoGarantia().getIdTipoVenta().equals(1L)){
							executeAccionCredito(sesion);
							regresar= this.alterarStockArticulos(sesion, newGarantia.getArticulosGarantia());
						} // if
						else{
							if(this.registrarPagos(sesion, this.garantia.getTicketVenta().getTotal()))					
								regresar= this.alterarStockArticulos(sesion, newGarantia.getArticulosGarantia());
						} // else
					} // if						
				} // if
			} // for			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarVenta				
	
	public boolean verificarCierreCaja(Session sesion) throws Exception {
		boolean regresar         = true;
		Map<String, Object>params= null;
		TcManticCierresDto cierre= null;
		TcManticCierresDto nuevo = null;
		try {
			params= new HashMap<>();
			params.put("estatusAbierto", CIERRE_ACTIVO);
			params.put("idEmpresa", this.detalleGarantia.getTicketVenta().getIdEmpresa());
			params.put("idCaja", this.detalleGarantia.getIdCaja());			
			cierre= (TcManticCierresDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresDto.class, "VistaCierresCajasDto", "cierreVigente", params);
			if(!(cierre!= null && cierre.isValid())){
				nuevo= toCierreNuevo(sesion);
				this.idCierreVigente= nuevo.getIdCierre();				
			} // if
			else
				this.idCierreVigente= cierre.getIdCierre();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // verificarCierreCaja
	
	private void toCierreActivo(Session sesion, Long idTipoMedioPago, Double totalPago) throws Exception {
		Map<String, Object>params         = null;
		TcManticCierresCajasDto cierreCaja= null;
		try {
			params= new HashMap<>();
			params.put("idCierre", this.idCierreVigente);
			params.put("medioPago", idTipoMedioPago);
			cierreCaja= (TcManticCierresCajasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresCajasDto.class, "TcManticCierresCajasDto", "cajaMedioPago", params);			
			// LOG.error("Medio pago:" + idTipoMedioPago + ", Cierre:" + this.idCierreVigente+ ", Caja:" + cierreCaja.getIdCaja() + ", Acumulado anterior:" + cierreCaja.getAcumulado() + ", Saldo anterior:" + cierreCaja.getSaldo());						
			cierreCaja.setAcumulado(cierreCaja.getAcumulado()- totalPago);			
			cierreCaja.setSaldo(cierreCaja.getDisponible()+ cierreCaja.getAcumulado());
			// LOG.error("Medio pago:" + idTipoMedioPago + ", Cierre:" + cierreCaja.getIdCierre() + ", Caja:" + cierreCaja.getIdCaja() + ", Disponible:" + cierreCaja.getDisponible() + ", Devolucion:" + totalPago + ", Acumulado:" + cierreCaja.getAcumulado() + ", Saldo:" + cierreCaja.getSaldo());
			DaoFactory.getInstance().update(sesion, cierreCaja);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toCierreActivo
	
	private TcManticCierresDto toCierreNuevo(Session sesion) throws Exception {
		TcManticCierresDto regresar= null;
		TcManticCierresDto registro= null;
		Cierre cierreNuevo         = null;
		try {			
			registro= new TcManticCierresDto();			
			registro.setEjercicio(Long.valueOf(Fecha.getAnioActual()));
			registro.setIdCierreEstatus(1L);
			registro.setIdDiferencias(2L);
			registro.setIdUsuario(JsfBase.getIdUsuario());
			registro.setObservaciones("Apertura de cierre");								
			cierreNuevo= new Cierre(this.garantia.getIdCaja(), 0D, registro, new ArrayList<>(), new ArrayList<>());				
			if(cierreNuevo.toNewCierreCaja(sesion))
				regresar= cierreNuevo.getCierre();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCierreNuevo		
	
	private boolean generarGarantia(Session sesion, Long idEstatusGarantia) throws Exception {
		boolean regresar         = false;
		Map<String, Object>params= null;		
		try {							
			this.loadGarantia(sesion, idEstatusGarantia);			
			if(DaoFactory.getInstance().insert(sesion, this.garantiaDto)>= 1L){				
				regresar= this.registraBitacora(sesion, this.garantiaDto.getIdGarantia(), idEstatusGarantia, "Se generó la garantía de forma correcta.");
				this.toFillArticulos(sesion, this.garantia.getArticulosGarantia());
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
			this.messageError= "Error al registrar el pago de la venta.";
		} // finally			
		return regresar;
	} // pagarVenta
	
	private void loadGarantia(Session sesion, Long idEstatusGarantia) throws Exception {
		Siguiente consecutivo= null;
		try {
			this.garantiaDto= new TcManticGarantiasDto();
			consecutivo= this.toSiguiente(sesion);
			this.tickets.add(consecutivo.getConsecutivo());
			this.garantiaDto.setConsecutivo(consecutivo.getConsecutivo());			
			this.garantiaDto.setOrden(consecutivo.getOrden());			
			this.garantiaDto.setIdGarantiaEstatus(idEstatusGarantia);			
			this.garantiaDto.setEjercicio(Long.valueOf(Fecha.getAnioActual()));			
			this.garantiaDto.setIdUsuario(JsfBase.getIdUsuario());
			this.garantiaDto.setIdVenta(this.garantia.getGarantia().getIdVenta());
			this.garantiaDto.setDescuentos(this.garantia.getTicketVenta().getDescuentos());
			this.garantiaDto.setImpuestos(this.garantia.getTicketVenta().getImpuestos());
			this.garantiaDto.setSubTotal(this.garantia.getTicketVenta().getSubTotal());
			this.garantiaDto.setTotal(this.garantia.getTicketVenta().getTotal());
			this.garantiaDto.setUtilidad(this.garantia.getTicketVenta().getUtilidad());		
			this.garantiaDto.setIdEfectivo(Long.valueOf(this.garantia.getIdEfectivo()));
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
	} // loadGarantia
		
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticGarantiasDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente
	
	protected void toFillArticulos(Session sesion, List<ArticuloVenta> detalleArt) throws Exception {		
		for (ArticuloVenta articulo: detalleArt) {
			TcManticGarantiasDetallesDto item= articulo.toGarantiaDetalle();
			item.setIdGarantia(this.garantiaDto.getIdGarantia());			
			if(item.getIdProveedor()<= 0L)
				item.setIdProveedor(null);
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticGarantiasDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
			else
				DaoFactory.getInstance().update(sesion, item);
		} // for
	} // toFillArticulos
	
	private boolean registrarPagos(Session sesion, Double total) throws Exception {
		boolean regresar= true;		
		try {						
			toPagoGarantia(sesion, total, "Registro de pago por garantia de venta.", false);
			toCierreActivo(sesion, this.detalleGarantia.getPagoGarantia().getIdTipoPago(), total);				
		} // try 
		catch (Exception e) {			 
			throw e; 
		} // catch		
		finally {
			this.messageError= "Error al registrar los pagos.";
		} // finally
		return regresar; 
	} // registrarPagos	
	
	private void toPagoGarantia(Session sesion, Double total, String observaciones, boolean credito) throws Exception{
		TrManticGarantiaMedioPagoDto pago= null;
		try {						
			pago= new TrManticGarantiaMedioPagoDto();
			pago.setIdCierre(this.idCierreVigente);
			pago.setIdGarantia(this.garantiaDto.getIdGarantia());
			pago.setIdTipoMedioPago(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
			if(!credito){
				if(!this.detalleGarantia.getPagoGarantia().getIdTipoPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
					pago.setIdBanco(this.detalleGarantia.getPagoGarantia().getIdBanco());
					pago.setReferencia(this.detalleGarantia.getPagoGarantia().getTransferencia());
				} // if			
				pago.setIdTipoMedioPago(this.detalleGarantia.getPagoGarantia().getIdTipoPago());
			} // if
			pago.setIdUsuario(JsfBase.getIdUsuario());
			pago.setImporte(total);		
			pago.setObservaciones(observaciones);		
			DaoFactory.getInstance().insert(sesion, pago);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch					
	} // toPagoGarantia
	
	private boolean executeAccionCredito(Session sesion) throws Exception{
		boolean regresar= false;		
		try {
			switch(this.detalleGarantia.getAccionCredito()){
				case COMPLETO:
					regresar= caseOneCredito(sesion);
					break;
				case AGREGAR:
					regresar= caseTwoCredito(sesion);
					break;
				case ASIGNAR:
					regresar= caseThreeCredito(sesion);
					break;
				case MODIFICAR:
					regresar= caseFourCredito(sesion);
					break;
				case PROCESAR:
					regresar= caseFiveCredito(sesion);
					break;
				case ACTIVAR:
					regresar= caseSixCredito(sesion);					
					break;
				case JUSTIFICAR:
					regresar= caseSevenCredito(sesion);
					break;
				case CALCULAR:					
					regresar= caseEightCredito(sesion);
					break;								
			} // switch
			toPagoGarantia(sesion, this.garantia.getTicketVenta().getTotal(), "Registro de pago por garantia de venta a credito.", true);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // executeAccionCredito
	
	private boolean caseOneCredito(Session sesion) throws Exception{
		boolean regresar                      = false;
		TcManticClientesDeudasDto deudaCliente= null;
		Map<String, Object>params             = null;
		String observaciones                  = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_venta=" + this.detalleGarantia.getIdVenta());
			deudaCliente= (TcManticClientesDeudasDto) DaoFactory.getInstance().toEntity(sesion, TcManticClientesDeudasDto.class, params);			
			deudaCliente.setIdClienteEstatus(EEstatusClientes.FINALIZADA.getIdEstatus());
			observaciones= (Cadena.isVacio(deudaCliente.getObservaciones()) ? "" : deudaCliente.getObservaciones()).concat(". Deuda finalizada por devolución. Fecha ").concat(Fecha.formatear(Fecha.FECHA_HORA_CORTA));
			deudaCliente.setObservaciones(observaciones);
			regresar= DaoFactory.getInstance().update(sesion, deudaCliente)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // caseOneCredito
	
	private boolean caseTwoCredito(Session sesion) throws Exception{
		boolean regresar          = false;
		boolean devolucionEfectivo= false;
		try {
			caseOneCredito(sesion);
			devolucionEfectivo= this.detalleGarantia.getPagoGarantia().getTipoDevolucion().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
			procesarPagoGeneral(sesion, this.detalleGarantia.getPagoCredito(), devolucionEfectivo);			
			if(devolucionEfectivo)
				toCierreActivo(sesion, ETipoMediosPago.EFECTIVO.getIdTipoMedioPago(), this.detalleGarantia.getDevolucionCredito());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // caseTwoCredito
	
	private boolean caseThreeCredito(Session sesion) throws Exception{
		boolean regresar= false;
		try {
			caseOneCredito(sesion);
			procesarPagoGeneral(sesion, this.detalleGarantia.getPagoCredito(), false);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // caseThreeCredito
	
	private boolean caseFourCredito(Session sesion) throws Exception{
		boolean regresar                      = false;
		TcManticClientesDeudasDto deudaCliente= null;
		Map<String, Object>params             = null;
		String observaciones                  = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_venta=" + this.detalleGarantia.getIdVenta());
			deudaCliente= (TcManticClientesDeudasDto) DaoFactory.getInstance().toEntity(sesion, TcManticClientesDeudasDto.class, params);
			observaciones= (Cadena.isVacio(deudaCliente.getObservaciones()) ? "" : deudaCliente.getObservaciones()).concat(". Actualización del importe de la deuda por una devolución parcial de articulos. Fecha ").concat(Fecha.formatear(Fecha.FECHA_HORA_CORTA));
			deudaCliente.setObservaciones(observaciones);
			deudaCliente.setImporte(this.detalleGarantia.getPagoCredito());
			deudaCliente.setSaldo(this.detalleGarantia.getPagoCredito());
			regresar= DaoFactory.getInstance().update(sesion, deudaCliente)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // caseFourCredito
	
	private boolean caseFiveCredito(Session sesion) throws Exception{
		boolean regresar                      = false;
		TcManticClientesDeudasDto deudaCliente= null;
		Map<String, Object>params             = null;
		String observaciones                  = null;
		boolean devolucionEfectivo            = false;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_venta=" + this.detalleGarantia.getIdVenta());
			deudaCliente= (TcManticClientesDeudasDto) DaoFactory.getInstance().toEntity(sesion, TcManticClientesDeudasDto.class, params);			
			devolucionEfectivo= this.detalleGarantia.getPagoGarantia().getTipoDevolucion().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
			if(devolucionEfectivo){
				deudaCliente.setIdClienteEstatus(EEstatusClientes.FINALIZADA.getIdEstatus());
				observaciones= (Cadena.isVacio(deudaCliente.getObservaciones()) ? "" : deudaCliente.getObservaciones()).concat(". Deuda finalizada por devolución. Fecha ").concat(Fecha.formatear(Fecha.FECHA_HORA_CORTA));
			} // if
			else{
				deudaCliente.setIdClienteEstatus(EEstatusClientes.PARCIALIZADA.getIdEstatus());
				observaciones= (Cadena.isVacio(deudaCliente.getObservaciones()) ? "" : deudaCliente.getObservaciones()).concat(". Deuda parcializada con saldo a favor por devolución. Fecha ").concat(Fecha.formatear(Fecha.FECHA_HORA_CORTA));
			} // else
			deudaCliente.setObservaciones(observaciones);
			deudaCliente.setSaldo(this.detalleGarantia.getDevolucionCredito());
			regresar= DaoFactory.getInstance().update(sesion, deudaCliente)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // caseFiveCredito
	
	private boolean caseSixCredito(Session sesion) throws Exception{
		boolean regresar          = false;
		boolean devolucionEfectivo= false;
		try {
			caseFiveCredito(sesion);
			devolucionEfectivo= this.detalleGarantia.getPagoGarantia().getTipoDevolucion().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
			procesarPagoGeneral(sesion, this.detalleGarantia.getPagoCredito(), devolucionEfectivo);			
			if(devolucionEfectivo)
				toCierreActivo(sesion, ETipoMediosPago.EFECTIVO.getIdTipoMedioPago(), this.detalleGarantia.getDevolucionCredito());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // caseSixCredito
	
	private boolean caseSevenCredito(Session sesion) throws Exception{
		boolean regresar= false;
		try {
			caseOneCredito(sesion);
			procesarPagoGeneral(sesion, this.detalleGarantia.getPagoCredito(), false);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // caseSevenCredito
	
	private boolean caseEightCredito(Session sesion) throws Exception{
		boolean regresar                      = false;
		TcManticClientesDeudasDto deudaCliente= null;
		Map<String, Object>params             = null;
		String observaciones                  = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_venta=" + this.detalleGarantia.getIdVenta());
			deudaCliente= (TcManticClientesDeudasDto) DaoFactory.getInstance().toEntity(sesion, TcManticClientesDeudasDto.class, params);			
			deudaCliente.setIdClienteEstatus(EEstatusClientes.FINALIZADA.getIdEstatus());
			observaciones= (Cadena.isVacio(deudaCliente.getObservaciones()) ? "" : deudaCliente.getObservaciones()).concat(". Deuda finalizada por devolución. Fecha ").concat(Fecha.formatear(Fecha.FECHA_HORA_CORTA));
			deudaCliente.setObservaciones(observaciones);
			deudaCliente.setSaldo(deudaCliente.getSaldo() - this.detalleGarantia.getPagoCredito());
			regresar= DaoFactory.getInstance().update(sesion, deudaCliente)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // caseEightCredito	
	
	private boolean procesarPagoGeneral(Session sesion, Double pago, boolean efectivo) throws Exception{		
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
			this.pagoPivote= pago;
			for(Entity deuda: deudas){
				if(saldo > 0){					
					saldoDeuda= Double.valueOf(deuda.toString("saldo"));
					if(saldoDeuda < this.pagoPivote){
						pagoParcial= saldoDeuda;
						saldo= this.pagoPivote - saldoDeuda;						
						this.pagoPivote= saldo;
						abono= 0D;
						idEstatus= EEstatusClientes.FINALIZADA.getIdEstatus();
					} // if
					else{						
						pagoParcial= this.pagoPivote;
						saldo= 0D;
						abono= saldoDeuda - this.pagoPivote;
						idEstatus= saldoDeuda<= this.pagoPivote ? EEstatusClientes.FINALIZADA.getIdEstatus() : EEstatusClientes.PARCIALIZADA.getIdEstatus();
					} /// else
					if(!efectivo){
						idEstatus= (abono * -1) > 0 ? EEstatusClientes.PARCIALIZADA.getIdEstatus() : idEstatus;
					} // if
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
	
	protected void registrarDeuda(Session sesion, Double importe) throws Exception{
		TcManticClientesDeudasDto deuda= null;		
		deuda= new TcManticClientesDeudasDto();
		deuda.setIdVenta(this.detalleGarantia.getIdVenta());
		deuda.setIdCliente(this.detalleGarantia.getIdCliente());
		deuda.setIdUsuario(JsfBase.getIdUsuario());
		deuda.setImporte(importe);
		deuda.setSaldo(importe);
		deuda.setLimite(toLimiteCredito(sesion));
		deuda.setIdClienteEstatus(1L);
		DaoFactory.getInstance().insert(sesion, deuda);		
	} // registrarDeuda
	
	public Date toLimiteCredito(Session sesion) throws Exception{
		TcManticClientesDto cliente= (TcManticClientesDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, this.detalleGarantia.getIdCliente());
		Long addDias= cliente.getPlazoDias();			
		Calendar calendar= Calendar.getInstance();
		Date regresar= new Date(calendar.getTimeInMillis());			
		calendar.setTime(regresar);
		calendar.add(Calendar.DAY_OF_YEAR, addDias.intValue());
		regresar= new Date(calendar.getTimeInMillis());		
		return regresar;
	} // toLimiteCredito
	
		private List<Entity> toDeudas(Session sesion) throws Exception{
		List<Entity> regresar    = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.detalleGarantia.getIdCliente());
			params.put("idVenta", this.detalleGarantia.getIdVenta());			
			params.put("estatus", EEstatusClientes.FINALIZADA.getIdEstatus());			
			params.put("sortOrder", "order by tc_mantic_clientes_deudas.registro desc");
			params.put(Constantes.SQL_CONDICION, " tc_mantic_clientes_deudas.saldo > 0 ");			
			regresar= DaoFactory.getInstance().toEntitySet(sesion, "VistaClientesDto", "cuentasExcluye", params);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDeudas	
	
	private boolean registrarPago(Session sesion, Long idClienteDeuda, Double pagoParcial) throws Exception{
		TcManticClientesPagosDto registroPago= null;
		boolean regresar                     = false;
		Siguiente orden                      = null;
		try {
			registroPago= new TcManticClientesPagosDto();
			registroPago.setIdClienteDeuda(idClienteDeuda);
			registroPago.setIdUsuario(JsfBase.getIdUsuario());
			registroPago.setObservaciones("Pago aplicado a la deuda general del cliente como saldo a favor de una devolucion. Pago general por $".concat(pagoParcial.toString()));
			registroPago.setPago(pagoParcial);
			registroPago.setIdTipoMedioPago(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
			registroPago.setIdCierre(this.idCierreVigente);				
			orden= this.toSiguiente(sesion, this.detalleGarantia.getIdCliente());
			registroPago.setOrden(orden.getOrden());
			registroPago.setConsecutivo(orden.getConsecutivo());
			registroPago.setEjercicio(new Long(Fecha.getAnioActual()));
			regresar= DaoFactory.getInstance().insert(sesion, registroPago)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarPago
	
	private Siguiente toSiguiente(Session sesion, Long idCliente) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", ((TcManticClientesDto)DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, idCliente)).getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "VistaTcManticClientesPagosDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente	
	
	private boolean alterarStockArticulos(Session sesion, List<ArticuloVenta> arts) throws Exception {
		TcManticAlmacenesArticulosDto almacenArticulo= null;
		TcManticArticulosDto articuloVenta           = null;		
		Map<String, Object>params                    = null;
		boolean regresar                             = false;
		int count                                    = 0; 
		Double stock                                 = 0D;
		try {			
			params= new HashMap<>();
			for(ArticuloVenta articulo: arts){
				stock= 0D;
				params.put(Constantes.SQL_CONDICION, "id_articulo="+ articulo.getIdArticulo()+ " and id_almacen="+ this.garantia.getTicketVenta().getIdAlmacen());
				almacenArticulo= (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().toEntity(sesion, TcManticAlmacenesArticulosDto.class, "TcManticAlmacenesArticulosDto", "row", params);
				if(almacenArticulo!= null){
					stock= almacenArticulo.getStock();
					almacenArticulo.setStock(almacenArticulo.getStock() + articulo.getCantidad());
					regresar= DaoFactory.getInstance().update(sesion, almacenArticulo)>= 1L;
				} // if
				else{
					stock= 0D;
					regresar= generarAlmacenArticulo(sesion, articulo.getIdArticulo(), articulo.getCantidad());
				} // else				
				registrarMovimiento(sesion, this.garantia.getTicketVenta().getIdAlmacen(), articulo.getCantidad(), articulo.getIdArticulo(), stock, this.garantia.getTicketVenta().getIdUsuario());
				if(regresar) {
					articuloVenta= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, articulo.getIdArticulo());
					articuloVenta.setStock(articuloVenta.getStock() + articulo.getCantidad());
					if(DaoFactory.getInstance().update(sesion, articuloVenta)>= 1L)
						regresar= actualizaInventario(sesion, articulo.getIdArticulo(), articulo.getCantidad());
				} // if
				if(regresar)
					count++;
			} // for		
			regresar= count== arts.size();			
		} // try
		catch (Exception e) {			
			throw e;		
		} // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // alterarStockArticulos
	
	private void registrarMovimiento(Session sesion, Long idAlmacen, Double cantidad, Long idArticulo, Double stock, Long idUsuario) throws Exception{
		Double calculo= Numero.toRedondearSat(stock + cantidad) ;
		TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
			  this.garantiaDto.getConsecutivo(), // String consecutivo, 
				5L,        // Long idTipoMovimiento, 
				idUsuario, // Long idUsuario, 
				idAlmacen, // Long idAlmacen, 
				-1L,       // Long idMovimiento, 
				cantidad,  // Double cantidad, 
				idArticulo,// Long idArticulo, 
				stock,     // Double stock, 
				calculo,   // Double calculo
				null
		  );
			DaoFactory.getInstance().insert(sesion, movimiento); 
	} // registrarMovimiento
	
	private boolean generarAlmacenArticulo(Session sesion, Long idArticulo, Double cantidad) throws Exception {
		boolean regresar                             = false;
		TcManticAlmacenesArticulosDto almacenArticulo= null;
		try {
			almacenArticulo= new TcManticAlmacenesArticulosDto();
			almacenArticulo.setIdAlmacen(this.garantia.getTicketVenta().getIdAlmacen());
			almacenArticulo.setIdArticulo(idArticulo);
			almacenArticulo.setIdUsuario(JsfBase.getIdUsuario());
			almacenArticulo.setMaximo(0D);
			almacenArticulo.setMinimo(0D);
			almacenArticulo.setStock(0 + cantidad);
			almacenArticulo.setIdAlmacenUbicacion(toIdAlmacenUbicacion(sesion));
			regresar= DaoFactory.getInstance().insert(sesion, almacenArticulo)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // generarAlmacenArticulo
	
	private Long toIdAlmacenUbicacion(Session sesion) throws Exception {
		Long regresar                            = -1L;
		TcManticAlmacenesUbicacionesDto ubicacion= null;
		Map<String, Object>params                = null;		
		try {
			params= new HashMap<>();
			params.put("idAlmacen", this.garantia.getTicketVenta().getIdAlmacen());
			ubicacion= (TcManticAlmacenesUbicacionesDto) DaoFactory.getInstance().toEntity(sesion, TcManticAlmacenesUbicacionesDto.class, "TcManticAlmacenesUbicacionesDto", "general", params);
			if(ubicacion!= null)
				regresar= ubicacion.getKey();
			else{
				ubicacion= new TcManticAlmacenesUbicacionesDto();
				ubicacion.setPiso(GENERAL);
				ubicacion.setDescripcion(GENERAL);
				ubicacion.setIdUsuario(JsfBase.getIdUsuario());				
				ubicacion.setIdAlmacen(this.garantia.getTicketVenta().getIdAlmacen());
				regresar= DaoFactory.getInstance().insert(sesion, ubicacion);
			} // 
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toIdAlmacenUbicacion
	
	private boolean actualizaInventario(Session sesion, Long idArticulo, Double cantidad) throws Exception {
		boolean regresar                 = false;
		TcManticInventariosDto inventario= null;
		Map<String, Object>params        = null;
		try {
			params= new HashMap<>();
			params.put("idAlmacen", this.garantia.getTicketVenta().getIdAlmacen());
			params.put("idArticulo", idArticulo);
			inventario= (TcManticInventariosDto) DaoFactory.getInstance().toEntity(sesion, TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario!= null){
				inventario.setEntradas(inventario.getEntradas()+ cantidad);
				inventario.setStock(inventario.getStock()+ cantidad);
				regresar= DaoFactory.getInstance().update(sesion, inventario)>= 1L;
			} // if
			else{
				inventario= new TcManticInventariosDto();
				inventario.setEjercicio(Long.valueOf(Fecha.getAnioActual()));
				inventario.setSalidas(0D);
				inventario.setIdAlmacen(this.garantia.getTicketVenta().getIdAlmacen());
				inventario.setIdArticulo(idArticulo);
				inventario.setIdUsuario(JsfBase.getIdUsuario());
				inventario.setInicial(0D);
				inventario.setEntradas(cantidad);
				inventario.setStock(cantidad);
				inventario.setIdAutomatico(1L);
				regresar= DaoFactory.getInstance().insert(sesion, inventario)>= 1L;
			} // else				
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizaInventario
	
	protected boolean registraBitacora(Session sesion, Long idGarantia, Long idGarantiaEstatus, String justificacion) throws Exception {
		boolean regresar                     = false;
		TcManticGarantiasBitacoraDto bitacora= null;
		try {
			bitacora= new TcManticGarantiasBitacoraDto(idGarantia, this.garantiaDto.getConsecutivo(), justificacion, JsfBase.getIdUsuario(), idGarantiaEstatus, -1L, this.garantiaDto.getTotal());
			regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarBitacora	
}
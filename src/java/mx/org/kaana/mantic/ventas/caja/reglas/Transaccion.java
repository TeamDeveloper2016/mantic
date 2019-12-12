package mx.org.kaana.mantic.ventas.caja.reglas;

import java.sql.Date;
import java.sql.Timestamp;
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
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresAlertasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticVentaMedioPagoDto;
import mx.org.kaana.mantic.enums.EEstatusFacturas;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETipoPago;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import mx.org.kaana.mantic.ventas.beans.ClienteVenta;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Facturacion;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Cierre;
import org.apache.log4j.Logger;
import org.hibernate.Session;

public class Transaccion extends mx.org.kaana.mantic.ventas.reglas.Transaccion {

	private static final Logger LOG          = Logger.getLogger(Transaccion.class);		
	private static final String GENERAL      = "GENERAL";
	private static final String CIERRE_ACTIVO= "1,2";
	private static final Long PAGADO         = 10L;
	private static final Long SI             = 1L;
	private static final Long NO             = 2L;	
	private VentaFinalizada ventaFinalizada;
	private IBaseDto dto;
	private boolean clienteDeault;
	private boolean isNuevoCierre;
	private Double cierreCaja;
	private Long idCierreVigente;
	private String cotizacion;
	private Long idFacturaGeneral;
	private Long idVenta;
	private Long idCliente;
	private String correosFactura;
	private TcManticFacturasDto facturaPrincipal;
	private Facturacion facturacion;
	
	public Transaccion(IBaseDto orden, List<Articulo> articulos) {
		super((TcManticVentasDto)orden, articulos);		
	} // Transaccion
	
	public Transaccion(IBaseDto dto) {
		super(new TicketVenta());
		this.dto= dto;
	} // Transaccion
	
	public Transaccion(VentaFinalizada ventaFinalizada) {
		this(ventaFinalizada, null);
	} // Transaccion

	public Transaccion(VentaFinalizada ventaFinalizada, IBaseDto dto) {
		super(ventaFinalizada.getTicketVenta());
		this.ventaFinalizada = ventaFinalizada;		
	}	// Transaccion	

	public Transaccion(Long idVenta, Long idCliente){
		super(new TicketVenta());
		this.idVenta  = idVenta;
		this.idCliente= idCliente;		
	} // Transaccion	

	public Transaccion(Facturacion facturacion) {
		super(new TicketVenta());
		this.facturacion = facturacion;
	}
		
	public Long getIdCierreVigente() {
		return idCierreVigente;
	}	

	public String getCotizacion() {
		return cotizacion;
	}

	public String getCorreosFactura() {
		return correosFactura;
	}

	public TcManticFacturasDto getFacturaPrincipal() {
		return facturaPrincipal;
	}	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar   = false;
		this.isNuevoCierre = false;
		this.cierreCaja    = 0D;		
		Long idEstatusVenta= null;
		try {						
			switch(accion) {					
				case REPROCESAR:				
					regresar= procesarVenta(sesion);
					break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
				case MODIFICAR:
					regresar= procesaCotizacion(sesion);					
					break;
				case ASIGNAR:
					regresar= actualizarClienteVenta(sesion);
					break;
				case AGREGAR:
					idEstatusVenta= EEstatusVentas.ABIERTA.getIdEstatusVenta();
					regresar= actualizarVenta(sesion, idEstatusVenta);					
					break;
				case MOVIMIENTOS:					
					super.ejecutar(sesion, accion);
					this.dto= getOrden();
					regresar= procesaCotizacion(sesion);					
					break;
				case DESACTIVAR:
					regresar= true;
					TcManticVentasDto venta= null;
					if(this.idVenta > 0){
						venta= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.idVenta);
						venta.setCandado(EBooleanos.SI.getIdBooleano());
						regresar= DaoFactory.getInstance().update(sesion, venta)>= 1L;
					} // if
					if(this.idCliente > 0){
						venta= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.idCliente);
						venta.setCandado(EBooleanos.NO.getIdBooleano());
						regresar= DaoFactory.getInstance().update(sesion, venta)>= 1L;
					} // if
					break;
				case GENERAR:							
					regresar= generarTimbradoFactura(sesion);
					break;
				case TRANSFORMACION:
					regresar= assignStatusAutomatico(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception(getMessageError());
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(getMessageError().concat("\n\n")+ e.getMessage());
		} // catch		
		if(this.ventaFinalizada!= null && this.ventaFinalizada.getTicketVenta()!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.ventaFinalizada.getTicketVenta().getConsecutivo());
		return regresar;
	} // ejecutar
	
	private boolean actualizarClienteVenta(Session sesion) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.idCliente);
			regresar= DaoFactory.getInstance().update(sesion, TcManticVentasDto.class, this.idVenta, params)>= 1L;
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // actualizarClienteVenta
	
	private boolean procesaCotizacion(Session sesion) throws Exception {
		boolean regresar            = false;
		Calendar calendar           = null;
		TcManticVentasDto cotizacion= null;
		Siguiente consecutivo       = null;		
		cotizacion= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.dto.getKey());
		if(Cadena.isVacio(cotizacion.getCotizacion())) {
			consecutivo= this.toSiguienteCotizacion(sesion, cotizacion.getIdEmpresa());
			cotizacion.setCcotizacion(consecutivo.getOrden());
			cotizacion.setCotizacion(consecutivo.getConsecutivo());				
		} // if
		this.cotizacion= cotizacion.getCotizacion();
		calendar= Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 15);
		cotizacion.setVigencia(new Date(calendar.getTimeInMillis()));
		cotizacion.setIdVentaEstatus(EEstatusVentas.COTIZACION.getIdEstatusVenta());					
		cotizacion.setCandado(EBooleanos.NO.getIdBooleano());
		regresar= DaoFactory.getInstance().update(sesion, cotizacion)>= 1L;		
		return regresar;
	} // procesaCotizacion
	
	private Siguiente toSiguienteCotizacion(Session sesion, Long idEmpresa) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", idEmpresa);
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "siguienteCotizacion", params, "siguiente");
			if(next!= null && next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguienteCotizacion
	
	private boolean procesarVenta(Session sesion) throws Exception{
		boolean regresar= false;		
		regresar= pagarVenta(sesion, this.ventaFinalizada.getApartado() ? EEstatusVentas.APARTADOS.getIdEstatusVenta() : (this.ventaFinalizada.isCredito() ? EEstatusVentas.CREDITO.getIdEstatusVenta() : EEstatusVentas.PAGADA.getIdEstatusVenta()));
		if(regresar){
			if(this.ventaFinalizada.isFacturar() && !this.ventaFinalizada.getApartado())
				regresar= registrarFactura(sesion);
			if(verificarCierreCaja(sesion)){
				if(registrarPagos(sesion)){					
					if(!this.ventaFinalizada.getTipoCuenta().equals(EEstatusVentas.APARTADOS.name()))
						regresar= alterarStockArticulos(sesion);
				} // if
			} // if
			if(this.ventaFinalizada.getApartado())
				regresar= registrarApartado(sesion);
			else if(this.ventaFinalizada.getTipoCuenta().equals(EEstatusVentas.APARTADOS.name()))
				regresar= liquidarApartado(sesion);			
			if(this.ventaFinalizada.getTicketVenta().getIdServicio() > 0L)
				actualizarServicio(sesion);
		} // if		
		return regresar;
	} // procesarVenta
	
	private void actualizarServicio(Session sesion) throws Exception{
		TcManticServiciosDto servicio        = null;
		TcManticServiciosBitacoraDto bitacora= null;
		try {
			servicio= (TcManticServiciosDto) DaoFactory.getInstance().findById(sesion, TcManticServiciosDto.class, this.ventaFinalizada.getTicketVenta().getIdServicio());
			if(this.ventaFinalizada.isFacturar())
				servicio.setIdFactura(this.idFacturaGeneral);
			servicio.setIdTipoMedioPago(getOrden().getIdTipoMedioPago());
			servicio.setIdServicioEstatus(PAGADO);			
			if(DaoFactory.getInstance().update(sesion, servicio)>= 1){
				bitacora= new TcManticServiciosBitacoraDto("Pago de apartado", -1L, JsfBase.getIdUsuario(), PAGADO, this.ventaFinalizada.getTicketVenta().getIdServicio(), Cadena.rellenar(toSiguienteServicio(sesion, this.ventaFinalizada.getTicketVenta().getIdServicio()).toString(), 5, '0', true), servicio.getTotal());
				DaoFactory.getInstance().insert(sesion, bitacora);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // actualizarServicio
	
	private Siguiente toSiguienteServicio(Session sesion, Long idServicio) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idServicio", idServicio);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticServiciosBitacoraDto", "siguiente", params, "siguiente");
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
	
	private boolean registrarApartado(Session sesion) throws Exception{
		boolean regresar                     = false;		
		Siguiente consecutivo                = null;
		Long idApartado                      = -1L;
		TcManticApartadosBitacoraDto bitacora= null;
		Calendar calendar                    = null;
		try {
			this.ventaFinalizada.getDetailApartado().setIdVenta(this.ventaFinalizada.getTicketVenta().getIdVenta());
			consecutivo= this.toSiguienteApartado(sesion);
			this.ventaFinalizada.getDetailApartado().setConsecutivo(consecutivo.getConsecutivo());
			this.ventaFinalizada.getDetailApartado().setOrden(consecutivo.getOrden());
			this.ventaFinalizada.getDetailApartado().setEjercicio(Long.valueOf(Fecha.getAnioActual()));
			this.ventaFinalizada.getDetailApartado().setImporte(this.ventaFinalizada.getTotales().getTotales().getTotal());
			this.ventaFinalizada.getDetailApartado().setAbonado(this.ventaFinalizada.getTotales().getPago());
			this.ventaFinalizada.getDetailApartado().setSaldo(this.ventaFinalizada.getTotales().getTotales().getTotal() - this.ventaFinalizada.getTotales().getPago());
			this.ventaFinalizada.getDetailApartado().setIdApartadoEstatus(1L);
			this.ventaFinalizada.getDetailApartado().setIdUsuario(JsfBase.getIdUsuario());
			if(!getOrden().getIdCliente().equals(toClienteDefault(sesion))){
				this.ventaFinalizada.getDetailApartado().setNombre(this.ventaFinalizada.getCliente().getRazonSocial());
				this.ventaFinalizada.getDetailApartado().setTelefono(toTelefonoCliente(sesion, this.ventaFinalizada.getCliente().getIdCliente()));
			} // if
			calendar= Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 30);
			this.ventaFinalizada.getDetailApartado().setVencimiento(new Date(calendar.getTimeInMillis()));
			idApartado= DaoFactory.getInstance().insert(sesion, this.ventaFinalizada.getDetailApartado());
			if(idApartado >= 1){
				bitacora= new TcManticApartadosBitacoraDto();
				bitacora.setIdApartado(idApartado);
				bitacora.setIdApartadoEstatus(1L);
				bitacora.setJustificacion(this.ventaFinalizada.getDetailApartado().getObservaciones());
				bitacora.setIdUsuario(JsfBase.getIdUsuario());
				regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
				if(regresar)
					regresar= registrarPagosApartado(sesion, idApartado);
			} // if
		} // try 		
		finally{
			setMessageError("Error al registrar el apartado.");
		} // finally
		return regresar;
	} // registrarApartado			
	
	private String toTelefonoCliente(Session sesion, Long idCliente) throws Exception{
		String regresar= null;
		Boolean inicio = false;
		MotorBusqueda motor= new MotorBusqueda(idCliente);
		List<ClienteTipoContacto>contactos= motor.toClientesTipoContacto(sesion);
		if(!contactos.isEmpty()){
			for(ClienteTipoContacto contacto: contactos){
				if(contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO_CASA.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO_NEGOCIO.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO_PERSONAL.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO_TRABAJO.getKey())){
					if(!inicio)
						contacto.getValor();
				} // if
			} // for			
		} // id
		return regresar;
	} // toTelefonoCliente
	
	public boolean verificarCierreCaja(Session sesion) throws Exception{
		boolean regresar         = true;
		Map<String, Object>params= null;
		TcManticCierresDto cierre= null;
		TcManticCierresDto nuevo = null;
		try {
			params= new HashMap<>();
			params.put("estatusAbierto", CIERRE_ACTIVO);
			params.put("idEmpresa", this.ventaFinalizada.getTicketVenta().getIdEmpresa());
			params.put("idCaja", this.ventaFinalizada.getIdCaja());			
			cierre= (TcManticCierresDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresDto.class, "VistaCierresCajasDto", "cierreVigente", params);
			if(!(cierre!= null && cierre.isValid())){
				nuevo= toCierreNuevo(sesion);
				this.idCierreVigente= nuevo.getIdCierre();				
			} // if
			else
				this.idCierreVigente= cierre.getIdCierre();
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // verificarCierreCaja
	
	public boolean alterarCierreCaja(Session sesion, Long idTipoMedioPago) throws Exception {
		boolean regresar     = true;
		TcManticCajasDto caja= (TcManticCajasDto) DaoFactory.getInstance().findById(sesion, TcManticCajasDto.class, this.ventaFinalizada.getIdCaja());
		Double limiteCaja    = caja.getLimite();
		this.toCierreActivo(sesion, idTipoMedioPago);
		if(idTipoMedioPago.equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())) {
			if(this.isNuevoCierre) {
				if(limiteCaja< this.cierreCaja)
					regresar= this.registraAlertaRetiro(sesion, this.idCierreVigente, limiteCaja-this.cierreCaja);				
			}	// if		
			else {
				this.cierreCaja= this.toAcumuladoCierreActivo(sesion, this.idCierreVigente, idTipoMedioPago);
				if(limiteCaja< this.cierreCaja)
					regresar= this.registraAlertaRetiro(sesion, this.idCierreVigente, limiteCaja-this.cierreCaja);
			} // else
		} // if		
		return regresar;
	} // alterarCierreCaja
	
	private boolean registraAlertaRetiro(Session sesion, Long idCierre, Double importe) throws Exception{
		boolean regresar                = true;
		TcManticCierresAlertasDto alerta= null;
		Map<String, Object>params       = null;		
		try{
			params= new HashMap<>();
			params.put("idCierre", idCierre);
			params.put(Constantes.SQL_CONDICION, "id_cierre="+idCierre);
			alerta= (TcManticCierresAlertasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresAlertasDto.class, "TcManticCierresAlertasDto", "row", params);
			if(!(alerta!= null && alerta.isValid())) {
				DaoFactory.getInstance().updateAll(sesion, TcManticCierresAlertasDto.class, params);
				alerta= new TcManticCierresAlertasDto();
				alerta.setIdCierre(idCierre);
				alerta.setIdNotifica(1L);
				alerta.setIdUsuario(JsfBase.getIdUsuario());
				alerta.setImporte(importe<= 0D ? 0D: importe);
				alerta.setMensaje("El total de caja a sobrepasado el limite permitido, favor de realizar un retiro.");
				regresar= DaoFactory.getInstance().insert(sesion, alerta)>= 1L;
			} // if				
		} // try
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // registraAlertaRetiro
	
	private Double toAcumuladoCierreActivo(Session sesion, Long idCierre, Long idTipoMedioPago) throws Exception {
		Double regresar          = 0D;
		Map<String, Object>params= null;
		try{
			params= new HashMap<>();		
			params.put(Constantes.SQL_CONDICION, "id_cierre="+idCierre+" and id_tipo_medio_pago=" + idTipoMedioPago);
			TcManticCierresCajasDto acumulaCierreCaja= (TcManticCierresCajasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresCajasDto.class, "TcManticCierresCajasDto", "row", params);
			regresar= acumulaCierreCaja.getAcumulado();		
		} // try
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toAcumuladoCierreActivo	
	
	private void toCierreActivo(Session sesion, Long idTipoMedioPago) throws Exception {
		Map<String, Object>params         = null;
		TcManticCierresCajasDto cierreCaja= null;
		ETipoMediosPago medioPago         = null;
		Double abono                      = 0D;		
		try{
			params= new HashMap<>();
			params.put("idCierre", this.idCierreVigente);
			params.put("medioPago", idTipoMedioPago);
			cierreCaja= (TcManticCierresCajasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresCajasDto.class, "TcManticCierresCajasDto", "cajaMedioPago", params);			
			medioPago= ETipoMediosPago.fromIdTipoPago(idTipoMedioPago);
			switch(medioPago){
				case CHEQUE:
					abono= this.ventaFinalizada.getTotales().getCheque();
					break;
				case EFECTIVO:					
					abono= this.ventaFinalizada.getTotales().getEfectivo() - this.ventaFinalizada.getTotales().getCambio();
					break;
				case TARJETA_CREDITO:
					abono= this.ventaFinalizada.getTotales().getCredito();
					break;
				case TRANSFERENCIA:
					abono= this.ventaFinalizada.getTotales().getTransferencia();
					break;
				case TARJETA_DEBITO:
					abono= this.ventaFinalizada.getTotales().getDebito();
					break;
				case VALES_DESPENSA:
					abono= this.ventaFinalizada.getTotales().getVales();
					break;
				case INTERMEDIARIO_PAGOS:
					abono= this.ventaFinalizada.getTotales().getTotales().getTotal() - (this.ventaFinalizada.getTotales().getPago() - this.ventaFinalizada.getTotales().getCambio());
					break;
			} // switch					
			// LOG.error("Medio pago:" + idTipoMedioPago + ", Cierre:" + cierreCaja.getIdCierre() + ", Caja:" + cierreCaja.getIdCaja() + ", Acumulado anterior:" + cierreCaja.getAcumulado() + ", Saldo anterior:" + cierreCaja.getSaldo());						
			cierreCaja.setAcumulado(cierreCaja.getAcumulado()+ abono);			
			cierreCaja.setSaldo(cierreCaja.getDisponible()+ cierreCaja.getAcumulado());
			// LOG.error("Medio pago:" + idTipoMedioPago + ", Cierre:" + cierreCaja.getIdCierre() + ", Caja:" + cierreCaja.getIdCaja() + ", Disponible:" + cierreCaja.getDisponible() + ", Abono:" + abono + ", Acumulado:" + cierreCaja.getAcumulado() + ", Saldo:" + cierreCaja.getSaldo());
			DaoFactory.getInstance().update(sesion, cierreCaja);		
		} // try
		finally{
			Methods.clean(params);
		} // finally
	} // toCierreActivo
	
	private TcManticCierresDto toCierreNuevo(Session sesion) throws Exception { 
		TcManticCierresDto regresar= null;
		TcManticCierresDto registro= null;
		Cierre cierreNuevo         = null;		
		registro= new TcManticCierresDto();			
		registro.setEjercicio(Long.valueOf(Fecha.getAnioActual()));
		registro.setIdCierreEstatus(1L);
		registro.setIdDiferencias(2L);
		registro.setIdUsuario(JsfBase.getIdUsuario());
		registro.setObservaciones("Apertura de cierre");								
		cierreNuevo= new Cierre(this.ventaFinalizada.getIdCaja(), 0D, registro, new ArrayList<>(), new ArrayList<>());				
		if(cierreNuevo.toNewCierreCaja(sesion)){
			this.isNuevoCierre= true;				
			this.cierreCaja= 0D;
			regresar= cierreNuevo.getCierre();
		} // if		
		return regresar;
	} // toCierreNuevo	
	
	private Siguiente toSiguienteApartado(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		Entity siguiente          = null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());			
			params.put("operador", this.getCurrentSign());
			siguiente= (Entity) DaoFactory.getInstance().toEntity(sesion, "TcManticApartadosDto", "siguiente", params);
			if(siguiente!= null && siguiente.get("siguiente")!= null && siguiente.get("siguiente").getData()!= null)
			  regresar= new Siguiente(siguiente.toLong("siguiente"));
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}	// toSiguienteApartado
	
	private boolean pagarVenta(Session sesion, Long idEstatusVenta) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		Siguiente consecutivo    = null;
		boolean validacionEstatus= false;
		try {									
			validacionEstatus= !idEstatusVenta.equals(EEstatusVentas.APARTADOS.getIdEstatusVenta());
			consecutivo= this.toSiguiente(sesion);			
			getOrden().setCticket(consecutivo.getOrden());			
			getOrden().setTicket(consecutivo.getConsecutivo());
			getOrden().setIdVentaEstatus(idEstatusVenta);			
			getOrden().setIdFacturar(this.ventaFinalizada.isFacturar() && validacionEstatus ? SI : NO);
			getOrden().setIdCredito(this.ventaFinalizada.isCredito() && validacionEstatus ? SI : NO);
			getOrden().setCobro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			if(this.ventaFinalizada.isFacturar() && validacionEstatus){				
				this.clienteDeault= getOrden().getIdCliente().equals(toClienteDefault(sesion));
				if(this.clienteDeault){
					this.ventaFinalizada.getCorreosContacto().add(this.ventaFinalizada.getCelular());
					this.ventaFinalizada.getCorreosContacto().add(this.ventaFinalizada.getTelefono());
					this.ventaFinalizada.getDomicilio().setIdTipoDomicilio(ETiposDomicilios.FISCAL.getKey());
					this.ventaFinalizada.getCliente().setIdUsoCfdi(getOrden().getIdUsoCfdi());
					this.ventaFinalizada.getCliente().setIdTipoVenta(1L);
					setClienteVenta(new ClienteVenta(this.ventaFinalizada.getCliente(), this.ventaFinalizada.getDomicilio(), null));
					procesarCliente(sesion);
					getOrden().setIdCliente(getIdClienteNuevo());
				} // if
				else
					registraClientesTipoContacto(sesion, getOrden().getIdCliente());				
			} // if						
			if(DaoFactory.getInstance().update(sesion, getOrden())>= 1L) {				
				regresar= registraBitacora(sesion, getOrden().getIdVenta(), idEstatusVenta, "La venta ha sido finalizada.");				
				params= new HashMap<>();
				params.put("idVenta", getOrden().getIdVenta());
				regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticVentasDetallesDto.class, params)>= 1;
				toFillArticulos(sesion, this.ventaFinalizada.getArticulos());
			} // if			
		} // try		
		finally{
			Methods.clean(params);
			setMessageError("Error al registrar el pago de la venta.");
		} // finally			
		return regresar;
	} // pagarVenta
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", getOrden().getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "siguienteTicket", params, "siguiente");
			if(next!= null && next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente
	
	@Override
	public boolean registraClientesTipoContacto(Session sesion, Long idCliente) throws Exception {
		TrManticClienteTipoContactoDto dto = null;
    ESql sqlAccion       = null;
    int count            = 0;
    boolean validate     = false;
    boolean regresar     = false;
		int orden            = 1;
    boolean validateOrden= true;
    try {
      for (ClienteTipoContacto clienteTipoContacto : this.ventaFinalizada.getCorreosContacto()) {
				if(clienteTipoContacto.getValor()!= null && !Cadena.isVacio(clienteTipoContacto.getValor())){
					if(clienteTipoContacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey()) && validateOrden){
						clienteTipoContacto.setOrden(1L);
						validateOrden= false;
					} // if
					else
						clienteTipoContacto.setOrden(orden + 1L);
					clienteTipoContacto.setIdCliente(idCliente);
					clienteTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
					dto= (TrManticClienteTipoContactoDto) clienteTipoContacto;
					sqlAccion= clienteTipoContacto.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdClienteTipoContacto(-1L);
							validate = DaoFactory.getInstance().insert(sesion, dto)>= 1L;
							break;
						case UPDATE:
							validate = DaoFactory.getInstance().update(sesion, dto)>= 1L;
							break;
					} // switch
					orden++;
				} // if
				else
					validate= true;
        if (validate) 
          count++;        
      } // for		
      regresar = count == this.ventaFinalizada.getCorreosContacto().size();
    } // try    
    finally {
      setMessageError("Error al registrar los tipos de contacto, verifique que no haya duplicados");
    } // finally
    return regresar;
	} // registrarContactosCliente
	
	private boolean registrarFactura(Session sesion) throws Exception{
		boolean regresar           = false;
		TcManticFacturasDto factura= null;
		StringBuilder correos      = null;
		try {			
			factura= new TcManticFacturasDto();
			factura.setIdUsuario(JsfBase.getIdUsuario());
			factura.setIntentos(0L);
			correos= new StringBuilder("");
			if(!this.ventaFinalizada.getCorreosContacto().isEmpty()){				
				for(ClienteTipoContacto correo: this.ventaFinalizada.getCorreosContacto())
					correos.append(correo.getValor()).append(", ");
			} // if						
			this.correosFactura= correos.length()> 0 ? correos.substring(0, correos.length()-2) : correos.toString();			
			factura.setCorreos(this.correosFactura);
			factura.setObservaciones(this.ventaFinalizada.getObservaciones());
			factura.setIdFacturaEstatus(EEstatusFacturas.REGISTRADA.getIdEstatusFactura());
			if(DaoFactory.getInstance().insert(sesion, factura)>= 1L){	
				registrarBitacoraFactura(sesion, factura.getIdFactura(), EEstatusFacturas.REGISTRADA.getIdEstatusFactura(), this.ventaFinalizada.getObservaciones());
				getOrden().setIdFactura(factura.getIdFactura());
				regresar= DaoFactory.getInstance().update(sesion, getOrden())>= 1L;				
			} // if
			this.idFacturaGeneral= factura.getIdFactura();
			this.facturaPrincipal= factura;
		} // try		
		finally{
			setMessageError("Error al registrar la factura.");
		} // finally
		return regresar;
	} // registrarFactura
	
	private boolean registrarPagos(Session sesion) throws Exception{
		List<TrManticVentaMedioPagoDto> pagos= null;
		boolean regresar= false;		
		int count       = 0;
		try {
			pagos= this.loadPagos(sesion);
			for(TrManticVentaMedioPagoDto pago: pagos){
				if(DaoFactory.getInstance().insert(sesion, pago)>= 1L)
					this.alterarCierreCaja(sesion, pago.getIdTipoMedioPago());
				count++;
			} // for
			regresar= count== pagos.size();
		} // try		
		finally {
			this.setMessageError("Error al registrar los pagos.");
		} // finally
		return regresar;
	} // registrarPagos
	
	private List<TrManticVentaMedioPagoDto> loadPagos(Session sesion) throws Exception{
		List<TrManticVentaMedioPagoDto> regresar= null;
		TrManticVentaMedioPagoDto pago          = null;		
		regresar= new ArrayList<>();
		pago= this.toPagoEfectivo();
		if(pago!= null)
			regresar.add(pago);			
		pago= this.toPagoTarjeta();
		if(pago!= null)
			regresar.add(pago);
		pago= this.toPagoTarjetaDebito();
		if(pago!= null)
			regresar.add(pago);
		pago= this.toPagoTransferencia();
		if(pago!= null)
			regresar.add(pago);
		pago= this.toPagoCheque();
		if(pago!= null)
			regresar.add(pago);
		pago= this.toPagoCredito(sesion);
		if(pago!= null)
			regresar.add(pago);		
		return regresar;
	} // loadPagos
	
	private TrManticVentaMedioPagoDto toPagoEfectivo(){
		TrManticVentaMedioPagoDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getEfectivo() > 0D){
			regresar= new TrManticVentaMedioPagoDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdVenta(getOrden().getIdVenta());
			regresar.setImporte(this.ventaFinalizada.getTotales().getEfectivo());	
			regresar.setIdCierre(this.idCierreVigente);
			regresar.setTotal(this.ventaFinalizada.getTotales().getEfectivo() - this.ventaFinalizada.getTotales().getCambio());
		} // if		
		return regresar;
	} // toPagoEfectivo
	
	private TrManticVentaMedioPagoDto toPagoTarjetaDebito() {
		TrManticVentaMedioPagoDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getDebito()> 0D){
			regresar= new TrManticVentaMedioPagoDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.TARJETA_DEBITO.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdVenta(getOrden().getIdVenta());
			regresar.setImporte(this.ventaFinalizada.getTotales().getDebito());				
			if(this.ventaFinalizada.getTotales().getBancoDebito()!= null)
			  regresar.setIdBanco(this.ventaFinalizada.getTotales().getBancoDebito().getKey());
			regresar.setReferencia(this.ventaFinalizada.getTotales().getReferenciaDebito());	
			regresar.setIdCierre(this.idCierreVigente);
			regresar.setTotal(this.ventaFinalizada.getTotales().getDebito());
		} // if		
		return regresar;
	} // toPagoTCredito
	
	private TrManticVentaMedioPagoDto toPagoTarjeta(){
		TrManticVentaMedioPagoDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getCredito()> 0D){
			regresar= new TrManticVentaMedioPagoDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.TARJETA_CREDITO.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdVenta(getOrden().getIdVenta());
			regresar.setImporte(this.ventaFinalizada.getTotales().getCredito());				
			if(this.ventaFinalizada.getTotales().getBancoCredito()!= null)
  			regresar.setIdBanco(this.ventaFinalizada.getTotales().getBancoCredito().getKey());
			regresar.setReferencia(this.ventaFinalizada.getTotales().getReferenciaCredito());	
			regresar.setIdCierre(this.idCierreVigente);
			regresar.setTotal(this.ventaFinalizada.getTotales().getCredito());
		} // if		
		return regresar;
	} // toPagoTCredito
	
	private TrManticVentaMedioPagoDto toPagoTransferencia(){
		TrManticVentaMedioPagoDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getTransferencia()> 0D){
			regresar= new TrManticVentaMedioPagoDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.TRANSFERENCIA.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdVenta(getOrden().getIdVenta());
			regresar.setImporte(this.ventaFinalizada.getTotales().getTransferencia());				
			if(this.ventaFinalizada.getTotales().getBancoTransferencia()!= null)
  			regresar.setIdBanco(this.ventaFinalizada.getTotales().getBancoTransferencia().getKey());
			regresar.setReferencia(this.ventaFinalizada.getTotales().getReferenciaTransferencia());			
			regresar.setIdCierre(this.idCierreVigente);
			regresar.setTotal(this.ventaFinalizada.getTotales().getTransferencia());
		} // if		
		return regresar;
	} // toPagoTransferencia
	
	private TrManticVentaMedioPagoDto toPagoCredito(Session sesion) throws Exception{
		TrManticVentaMedioPagoDto regresar= null;
		Double totalCredito               = 0D;		
		if(this.ventaFinalizada.isCredito()){
			totalCredito= this.ventaFinalizada.getTotales().getTotales().getTotal() - (this.ventaFinalizada.getTotales().getPago() - this.ventaFinalizada.getTotales().getCambio());
			if(totalCredito > 0D){					
				registrarDeuda(sesion, totalCredito);	
				actualizarSaldoCatalogoCliente(sesion, getOrden().getIdCliente(), totalCredito, true);
				regresar= new TrManticVentaMedioPagoDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.INTERMEDIARIO_PAGOS.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdVenta(getOrden().getIdVenta());
				regresar.setImporte(totalCredito);								
				regresar.setIdCierre(this.idCierreVigente);
				regresar.setTotal(totalCredito);
			} // if
		} // if		
		return regresar;
	} // toPagoCredito		
	
	private TrManticVentaMedioPagoDto toPagoCheque(){
		TrManticVentaMedioPagoDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getCheque()> 0D){
			regresar= new TrManticVentaMedioPagoDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.CHEQUE.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdVenta(getOrden().getIdVenta());
			regresar.setImporte(this.ventaFinalizada.getTotales().getCheque());				
			if(this.ventaFinalizada.getTotales().getBancoCheque()!= null)
  			regresar.setIdBanco(this.ventaFinalizada.getTotales().getBancoCheque().getKey());
			regresar.setReferencia(this.ventaFinalizada.getTotales().getReferenciaCheque());		
			regresar.setIdCierre(this.idCierreVigente);
			regresar.setTotal(this.ventaFinalizada.getTotales().getCheque());
		} // if		
		return regresar;
	} // toPagoCheque
	
	private boolean registrarPagosApartado(Session sesion, Long idApartado) throws Exception{
		List<TcManticApartadosPagosDto> pagos= null;
		boolean regresar= false;		
		int count       = 0;
		try {
			pagos= loadPagosApartado(idApartado);
			for(TcManticApartadosPagosDto pago: pagos){
				if(DaoFactory.getInstance().insert(sesion, pago)>= 1L)
					count++;
			} // for
			regresar= count== pagos.size();
		} // try		
		finally{
			setMessageError("Error al registrar los pagos del apartado.");
		} // finally
		return regresar;
	} // registrarPagos
	
	private List<TcManticApartadosPagosDto> loadPagosApartado(Long idApartado) throws Exception{
		List<TcManticApartadosPagosDto> regresar= null;
		TcManticApartadosPagosDto pago          = null;		
		regresar= new ArrayList<>();
		pago= toPagoEfectivoApartado(idApartado);
		if(pago!= null)
			regresar.add(pago);			
		pago= toPagoTarjetaApartado(idApartado);
		if(pago!= null)
			regresar.add(pago);
		pago= toPagoTransferenciaApartado(idApartado);
		if(pago!= null)
			regresar.add(pago);
		pago= toPagoChequeApartado(idApartado);
		if(pago!= null)
			regresar.add(pago);					
		return regresar;
	} // loadPagos
	
	private TcManticApartadosPagosDto toPagoEfectivoApartado(Long idApartado){
		TcManticApartadosPagosDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getEfectivo() > 0D){
			regresar= new TcManticApartadosPagosDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdApartado(idApartado);
			regresar.setPago(this.ventaFinalizada.getTotales().getEfectivo() - this.ventaFinalizada.getTotales().getCambio());
			regresar.setIdCierre(this.idCierreVigente);
		} // if		
		return regresar;
	} // toPagoEfectivo
	
	private TcManticApartadosPagosDto toPagoTarjetaApartado(Long idApartado){
		TcManticApartadosPagosDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getCredito()> 0D){
			regresar= new TcManticApartadosPagosDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.TARJETA_CREDITO.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdApartado(idApartado);
			regresar.setPago(this.ventaFinalizada.getTotales().getCredito());				
			regresar.setIdCierre(this.idCierreVigente);
		} // if		
		return regresar;
	} // toPagoTCredito
	
	private TcManticApartadosPagosDto toPagoTransferenciaApartado(Long idApartado){
		TcManticApartadosPagosDto regresar= null;
		if(this.ventaFinalizada.getTotales().getTransferencia()> 0D){
			regresar= new TcManticApartadosPagosDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.TRANSFERENCIA.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdApartado(idApartado);
			regresar.setPago(this.ventaFinalizada.getTotales().getTransferencia());			
			regresar.setIdCierre(this.idCierreVigente);
		} // if
		return regresar;
	} // toPagoTransferencia
	
	private TcManticApartadosPagosDto toPagoChequeApartado(Long idApartado){
		TcManticApartadosPagosDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getCheque()> 0D){
			regresar= new TcManticApartadosPagosDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.CHEQUE.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdApartado(idApartado);
			regresar.setPago(this.ventaFinalizada.getTotales().getCheque());					
			regresar.setIdCierre(this.idCierreVigente);
		} // if		
		return regresar;
	} // toPagoCheque	
	
	@Override
	public Date toLimiteCredito(Session sesion) throws Exception{
		Date regresar              = null;
		TcManticClientesDto cliente= null;
		Long addDias               = 15L;
		Calendar calendar          = null;		
		if(!this.ventaFinalizada.isFacturar() || (this.ventaFinalizada.isFacturar() && !this.clienteDeault)){
			cliente= (TcManticClientesDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, getOrden().getIdCliente());
			addDias= cliente.getPlazoDias();
		} // if
		calendar= Calendar.getInstance();
		regresar= new Date(calendar.getTimeInMillis());			
		calendar.setTime(regresar);
		calendar.add(Calendar.DAY_OF_YEAR, addDias.intValue());
		regresar= new Date(calendar.getTimeInMillis());		
		return regresar;
	} // toLimiteCredito
	
	private boolean alterarStockArticulos(Session sesion) throws Exception{
		TcManticAlmacenesArticulosDto almacenArticulo= null;
		TcManticArticulosDto articuloVenta           = null;		
		Map<String, Object>params                    = null;
		boolean regresar                             = false;
		int count                                    = 0; 
		Double stock                                 = 0D;
		try {			
			params= new HashMap<>();
			for(Articulo articulo: this.ventaFinalizada.getArticulos()){
				stock= 0D;
				if(articulo.isValid()){
					params.put(Constantes.SQL_CONDICION, "id_articulo="+ articulo.getIdArticulo()+ " and id_almacen="+ this.ventaFinalizada.getTicketVenta().getIdAlmacen());
					almacenArticulo= (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().toEntity(sesion, TcManticAlmacenesArticulosDto.class, "TcManticAlmacenesArticulosDto", "row", params);
					if(almacenArticulo!= null) {
						stock= almacenArticulo.getStock();
						almacenArticulo.setStock(almacenArticulo.getStock() - articulo.getCantidad());
						regresar= DaoFactory.getInstance().update(sesion, almacenArticulo)>= 1L;
					} // if
					else{
						stock= 0D;
						regresar= generarAlmacenArticulo(sesion, articulo.getIdArticulo(), articulo.getCantidad());
					} // else					
					registrarMovimiento(sesion, this.ventaFinalizada.getTicketVenta().getIdAlmacen(), articulo.getCantidad(), articulo.getIdArticulo(), stock, this.ventaFinalizada.getTicketVenta().getIdUsuario());
					if(regresar) {
						articuloVenta= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, articulo.getIdArticulo());
						articuloVenta.setStock(articuloVenta.getStock() - articulo.getCantidad());
						if(DaoFactory.getInstance().update(sesion, articuloVenta)>= 1L)
							regresar= actualizaInventario(sesion, articulo.getIdArticulo(), articulo.getCantidad());
					} // if
					if(regresar)
						count++;
				} // if
				else
					count++;
			} // for		
			regresar= count== this.ventaFinalizada.getArticulos().size();			
		} // try		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // alterarStockArticulos
	
	private void registrarMovimiento(Session sesion, Long idAlmacen, Double cantidad, Long idArticulo, Double stock, Long idUsuario) throws Exception{
		Double calculo= Numero.toRedondearSat(stock - cantidad) ;
		TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
			  getOrden().getTicket(), // String consecutivo, 
				2L,        // Long idTipoMovimiento, 
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
	
	private boolean generarAlmacenArticulo(Session sesion, Long idArticulo, Double cantidad) throws Exception{
		boolean regresar                             = false;
		TcManticAlmacenesArticulosDto almacenArticulo= null;		
		almacenArticulo= new TcManticAlmacenesArticulosDto();
		almacenArticulo.setIdAlmacen(getOrden().getIdAlmacen());
		almacenArticulo.setIdArticulo(idArticulo);
		almacenArticulo.setIdUsuario(JsfBase.getIdUsuario());
		almacenArticulo.setMaximo(0D);
		almacenArticulo.setMinimo(0D);
		almacenArticulo.setStock(0 - cantidad);
		almacenArticulo.setIdAlmacenUbicacion(toIdAlmacenUbicacion(sesion));
		regresar= DaoFactory.getInstance().insert(sesion, almacenArticulo)>= 1L;		
		return regresar;
	} // generarAlmacenArticulo
	
	private Long toIdAlmacenUbicacion(Session sesion) throws Exception{
		Long regresar                            = -1L;
		TcManticAlmacenesUbicacionesDto ubicacion= null;
		Map<String, Object>params                = null;		
		try {
			params= new HashMap<>();
			params.put("idAlmacen", getOrden().getIdAlmacen());
			ubicacion= (TcManticAlmacenesUbicacionesDto) DaoFactory.getInstance().toEntity(sesion, TcManticAlmacenesUbicacionesDto.class, "TcManticAlmacenesUbicacionesDto", "general", params);
			if(ubicacion!= null)
				regresar= ubicacion.getKey();
			else{
				ubicacion= new TcManticAlmacenesUbicacionesDto();
				ubicacion.setNivel(1L);
				ubicacion.setPiso(GENERAL);
				ubicacion.setDescripcion(GENERAL);
				ubicacion.setIdUsuario(JsfBase.getIdUsuario());				
				ubicacion.setIdAlmacen(getOrden().getIdAlmacen());
				regresar= DaoFactory.getInstance().insert(sesion, ubicacion);
			} // 
		} // try
		finally{
			Methods.clean(params);
		} // catch		
		return regresar;
	} // toIdAlmacenUbicacion
	
	private boolean actualizaInventario(Session sesion, Long idArticulo, Double cantidad) throws Exception{
		boolean regresar                 = false;
		TcManticInventariosDto inventario= null;
		Map<String, Object>params        = null;
		try {
			params= new HashMap<>();
			params.put("idAlmacen", getOrden().getIdAlmacen());
			params.put("idArticulo", idArticulo);
			inventario= (TcManticInventariosDto) DaoFactory.getInstance().toEntity(sesion, TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario!= null){
				inventario.setSalidas(inventario.getSalidas() + cantidad);
				inventario.setStock(inventario.getStock() - cantidad);
				regresar= DaoFactory.getInstance().update(sesion, inventario)>= 1L;
			} // if
			else{
				inventario= new TcManticInventariosDto();
				inventario.setEjercicio(Long.valueOf(Fecha.getAnioActual()));
				inventario.setEntradas(0D);
				inventario.setIdAlmacen(getOrden().getIdAlmacen());
				inventario.setIdArticulo(idArticulo);
				inventario.setIdUsuario(JsfBase.getIdUsuario());
				inventario.setInicial(0D);
				inventario.setSalidas(cantidad);
				inventario.setStock(cantidad);
				inventario.setIdAutomatico(1L);
				regresar= DaoFactory.getInstance().insert(sesion, inventario)>= 1L;
			} // else				
		} // try
		finally {			
			Methods.clean(params);
		} // catch		
		return regresar;
	} // actualizaInventario
	
	private boolean generarTimbradoFactura(Session sesion){
		boolean regresar             = true;
		TransaccionFactura factura   = null;
		CFDIGestor gestor            = null;
		ClienteFactura clienteFactura= null;				
		try {			
			sesion.flush();
			this.actualizarClienteFacturama(sesion, this.facturacion.getIdCliente(), this.facturacion.getIdClienteDomicilio());
			gestor= new CFDIGestor(this.facturacion.getIdVenta());			
			factura= new TransaccionFactura();			
			factura.actualizarFacturaAutomatico(this.facturacion.getIdFactura(), this.facturacion.getIdUsuario(), EEstatusFacturas.PROCESANDO.getIdEstatusFactura());
			factura.setArticulos(gestor.toDetalleCfdiVentas(sesion));
			clienteFactura= gestor.toClienteCfdiVenta(sesion);			
			clienteFactura.setMetodoPago(ETipoPago.fromIdTipoPago(this.facturacion.getIdTipoPago()).getClave());
			factura.setCliente(clienteFactura);
			factura.getCliente().setIdFactura(this.facturacion.getIdFactura());
			factura.generarCfdi(sesion, this.facturacion.getIdEmpresa().toString(), this.facturacion.getIdUsuario());						
		} // try
		catch (Exception e) {	
			Error.mensaje(e);
			try {
				if(factura!= null)
					factura.actualizarFacturaAutomatico(this.facturacion.getIdFactura(), this.facturacion.getIdUsuario(), EEstatusFacturas.AUTOMATICO.getIdEstatusFactura());
				regresar= false;
			} // try
			catch (Exception ex) {				
				Error.mensaje(ex);				
			} // catch									
		} // catch				
		return regresar;
	} // generarTimbradoFactura
	
	private boolean assignStatusAutomatico(Session sesion) throws Exception{
		boolean regresar          = false;		
		TransaccionFactura factura= null;
		try {
			factura= new TransaccionFactura();
			regresar= factura.actualizarFacturaAutomatico(sesion, this.facturacion.getIdFactura(), JsfBase.getIdUsuario());
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // assignStatusAutomatico
	
	private void actualizarClienteFacturama(Session sesion, Long idCliente, Long idClienteDomicilio) throws Exception {		
		CFDIGestor gestor= new CFDIGestor(idCliente);
		ClienteFactura cliente= gestor.toClienteFacturaUpdateVenta(sesion, idClienteDomicilio);
		setCliente(cliente);
		if(cliente.getIdFacturama()!= null)
			this.updateCliente(sesion);
		else
			this.registraClienteFacturama(sesion, idCliente);
	} // actualizarArticuloFacturama
	
	private boolean actualizarVenta(Session sesion, Long idEstatusVenta) throws Exception{
		boolean regresar             = false;
		Map<String, Object>params    = null;
		TcManticVentasDto ventaPivote= null;
		try {						
			ventaPivote= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.dto.getKey());
			ventaPivote.setIdVentaEstatus(idEstatusVenta);						
			ventaPivote.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			if(DaoFactory.getInstance().update(sesion, ventaPivote)>= 1L)
				regresar= registraBitacora(sesion, ventaPivote.getIdVenta(), idEstatusVenta, "Cambio de cotización a venta");				
		} // try		
		finally{
			Methods.clean(params);
		} // finally			
		return regresar;
	} // actualizarVenta		
	
	private boolean liquidarApartado(Session sesion) throws Exception{		
		List<TcManticApartadosPagosDto> pagos= null;
		Map<String, Object>params    = null;
		TcManticApartadosDto apartado= null;
		Long idApartado = null;
		Double saldo    = 0D;
		Double abonado  = 0D;
		boolean regresar= false;		
		int count       = 0;
		try {
			params= new HashMap<>();
			params.put("idVenta", getOrden().getIdVenta());
			apartado= (TcManticApartadosDto) DaoFactory.getInstance().findFirst(sesion, TcManticApartadosDto.class, "detalle", params);
			idApartado= apartado.getIdApartado();
			pagos= loadPagosApartado(idApartado);
			for(TcManticApartadosPagosDto pago: pagos){
				if(DaoFactory.getInstance().insert(sesion, pago)>= 1L){
					count++;
					apartado= (TcManticApartadosDto) DaoFactory.getInstance().findById(sesion, TcManticApartadosDto.class, idApartado);					
					saldo= apartado.getSaldo() - pago.getPago();
					abonado= apartado.getAbonado() + pago.getPago();
					apartado.setSaldo(saldo);
					apartado.setAbonado(abonado);
					apartado.setIdApartadoEstatus(saldo <= 0L ? 3L : 2L);
					if(DaoFactory.getInstance().update(sesion, apartado)>= 1L)          
						insertarBitacoraApartado(sesion, apartado, abonado);          
				} // if
			} // for
			regresar= count== pagos.size();
		} // try		
		finally{
			setMessageError("Error al registrar los pagos del apartado.");
		} // finally
		return regresar;
	} // registrarPagos
	
	private boolean insertarBitacoraApartado(Session sesion, TcManticApartadosDto apartado, Double abonado) throws Exception {
    boolean regresar= false;    
		TcManticApartadosBitacoraDto bitacora= new TcManticApartadosBitacoraDto();
		bitacora.setIdApartado(apartado.getIdApartado());
		bitacora.setIdApartadoEstatus(apartado.getIdApartadoEstatus());
		bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		bitacora.setIdUsuario(JsfBase.getIdUsuario());
		bitacora.setJustificacion("Pago realizado desde el modulo de cajas por la cantidad" + abonado);
		regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;		
		return regresar;
  } // insertarBitacora
}

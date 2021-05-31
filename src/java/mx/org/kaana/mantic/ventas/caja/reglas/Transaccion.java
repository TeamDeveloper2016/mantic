package mx.org.kaana.mantic.ventas.caja.reglas;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.enums.EEtapaServidor;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
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
import mx.org.kaana.mantic.db.dto.TcManticVentasDiferenciasDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticVentaMedioPagoDto;
import mx.org.kaana.mantic.enums.EEstatusFacturas;
import mx.org.kaana.mantic.enums.EEstatusFicticias;
import mx.org.kaana.mantic.enums.EEstatusServicios;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoDocumento;
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
	private Double efectivo;
	private Long idCierreVigente;
	private Long idCaja;
	private String cotizacion;
	private Long idFactura;
	private Long idVenta;
	private Long idCliente;
	private String correosFactura;
	private TcManticFacturasDto factura;
	private Facturacion facturacion;
	private Double totalDetalle;
	private String observaciones;
	
	public Transaccion(IBaseDto orden, List<Articulo> articulos) {
		super((TcManticVentasDto)orden, articulos);		
	} // Transaccion
	
	public Transaccion(IBaseDto dto) {
		super(new TicketVenta());
		this.dto= dto;
	} // Transaccion
	
	public Transaccion(VentaFinalizada ventaFinalizada) {
		this(ventaFinalizada, ventaFinalizada.getTicketVenta());
	} // Transaccion

	public Transaccion(VentaFinalizada ventaFinalizada, IBaseDto dto) {
		super(ventaFinalizada.getTicketVenta(), ventaFinalizada.getArticulos());
		this.ventaFinalizada = ventaFinalizada;		
	}	// Transaccion	

	public Transaccion(Long idVenta, Long idCliente) {
		this(idVenta, idCliente, "");
	}
	
	public Transaccion(Long idVenta, Long idCliente, String observaciones) {
		super(new TicketVenta());
		this.idVenta      = idVenta;
		this.idCliente    = idCliente;		
		this.observaciones= observaciones;
	} // Transaccion	

	public Transaccion(VentaFinalizada ventaFinalizada, Long idCliente, String observaciones) {
		super(ventaFinalizada.getTicketVenta(), ventaFinalizada.getArticulos());
		this.idVenta      = ventaFinalizada.getTicketVenta().getIdVenta();
		this.idCliente    = idCliente;		
		this.observaciones= observaciones;
	} // Transaccion	

	public Transaccion(Facturacion facturacion) {
		super(new TicketVenta());
		this.facturacion = facturacion;
	}
		
	public Transaccion(Long idVenta, TcManticFacturasDto factura, Long idCliente, IBaseDto orden, List<Articulo> articulos, Long idCierreVigente) {
		super((TcManticVentasDto)orden, articulos);		
    this.idVenta  = idVenta;
    this.idCliente= idCliente;
    this.factura  = factura;
    this.idCierreVigente= idCierreVigente;
	} // Transaccion
  
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
		return factura;
	}	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar   = false;
		this.isNuevoCierre = false;
		this.efectivo      = 0D;		
		Long idEstatusVenta= null;
		try {						
			this.totalDetalle= 0D;
			switch(accion) {					
				case RESTAURAR:				
					regresar= this.procesarCancela(sesion);
					break;
				case COPIAR:				
					regresar= this.procesarRefactura(sesion);
					break;
				case REPROCESAR:				
					regresar= this.procesarVenta(sesion);
					break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
				case MODIFICAR:
					regresar= this.procesaCotizacion(sesion);					
					break;
				case ASIGNAR:
          if(this.getOrden().getIdBanco()!= null && this.getOrden().getIdBanco()<= 0L)
            this.getOrden().setIdBanco(null);
          if(this.getOrden().getIdTipoMedioPago()!= null && this.getOrden().getIdTipoMedioPago()<= 0L)
            this.getOrden().setIdTipoMedioPago(null);
          if(this.getOrden().getIdTipoPago()!= null && this.getOrden().getIdTipoPago()<= 0L)
            this.getOrden().setIdTipoPago(null);
					regresar= this.actualizarClienteVenta(sesion);
					this.toFillArticulos(sesion, this.getArticulos());
					this.validarCabecera(sesion);
          if(this.getOrden().getIdBanco()== null)
            this.getOrden().setIdBanco(-1L);
          if(this.getOrden().getIdTipoMedioPago()== null)
            this.getOrden().setIdTipoMedioPago(-1L);
          if(this.getOrden().getIdTipoPago()== null)
            this.getOrden().setIdTipoPago(-1L);
					break;
				case AGREGAR:
					idEstatusVenta= EEstatusVentas.ABIERTA.getIdEstatusVenta();
					regresar= this.actualizarVenta(sesion, idEstatusVenta);					
					break;
				case MOVIMIENTOS:					
					super.ejecutar(sesion, accion);
					this.dto= getOrden();
					regresar= procesaCotizacion(sesion);					
					break;
				case DESACTIVAR:
					regresar= true;
					TcManticVentasDto venta= null;
					if(this.idVenta > 0) {
						venta= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.idVenta);
						venta.setCandado(EBooleanos.SI.getIdBooleano());
						regresar= DaoFactory.getInstance().update(sesion, venta)>= 1L;
					} // if
					if(this.idCliente > 0) {
						venta= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.idCliente);
						venta.setCandado(EBooleanos.NO.getIdBooleano());
						regresar= DaoFactory.getInstance().update(sesion, venta)>= 1L;
					} // if
					break;
				case GENERAR:							
					regresar= this.generarTimbradoFactura(sesion);
					break;
				case TRANSFORMACION:
					regresar= this.assignStatusAutomatico(sesion);
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
	
	private boolean actualizarClienteVenta(Session sesion) throws Exception {
		boolean regresar= false;
		try {
      this.getOrden().setIdCliente(this.idCliente);
      this.getOrden().setObservaciones(this.observaciones);
      if(this.getOrden().getIdBanco()!= null && this.getOrden().getIdBanco()<= -1L)
        this.getOrden().setIdBanco(null);
			regresar= DaoFactory.getInstance().update(sesion, this.getOrden())>= 1L;
		} // try		
		catch(Exception e) {
			throw e;
		} // try
		return regresar;
	} // actualizarClienteVenta
	
	private boolean procesaCotizacion(Session sesion) throws Exception {
		boolean regresar            = false;
		Calendar calendar           = null;
		TcManticVentasDto cotizacion= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.dto.getKey());
		Siguiente consecutivo       = null;		
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
	
	private boolean procesarVenta(Session sesion) throws Exception {
		boolean regresar= false;		
		regresar= this.pagarVenta(sesion, this.ventaFinalizada.getApartado() ? EEstatusVentas.APARTADOS.getIdEstatusVenta() : (this.ventaFinalizada.isCredito() ? EEstatusVentas.CREDITO.getIdEstatusVenta() : EEstatusVentas.PAGADA.getIdEstatusVenta()));
		if(regresar) {
			if(this.ventaFinalizada.isFacturar() && !this.ventaFinalizada.getApartado())
				regresar= this.registrarFactura(sesion);
			if(this.verificarCierreCaja(sesion)) {
				if(this.registrarPagos(sesion)) {					
					if(!this.ventaFinalizada.getTipoCuenta().equals(EEstatusVentas.APARTADOS.name())) {
           // VERIFICAR SI ES UNA VENTA GENERADA POR UNA ORDEN DE SERVICIO, CABIAR EL ESTATUS Y AFECTAR EL ALMACEN DE LAS REFACCIONES
           this.checkOrdenServicio(sesion);
          } // if  
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
	
	private void actualizarServicio(Session sesion) throws Exception {
		TcManticServiciosDto servicio        = null;
		TcManticServiciosBitacoraDto bitacora= null;
		try {
			servicio= (TcManticServiciosDto) DaoFactory.getInstance().findById(sesion, TcManticServiciosDto.class, this.ventaFinalizada.getTicketVenta().getIdServicio());
			if(this.ventaFinalizada.isFacturar())
				servicio.setIdFactura(this.idFactura);
			servicio.setIdTipoMedioPago(getOrden().getIdTipoMedioPago());
			servicio.setIdServicioEstatus(PAGADO);			
			if(DaoFactory.getInstance().update(sesion, servicio)>= 1) {
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
	
	private boolean registrarApartado(Session sesion) throws Exception {
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
			if(!getOrden().getIdCliente().equals(toClienteDefault(sesion))) {
				this.ventaFinalizada.getDetailApartado().setNombre(this.ventaFinalizada.getCliente().getRazonSocial());
				this.ventaFinalizada.getDetailApartado().setTelefono(toTelefonoCliente(sesion, this.ventaFinalizada.getCliente().getIdCliente()));
			} // if
			calendar= Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 30);
			this.ventaFinalizada.getDetailApartado().setVencimiento(new Date(calendar.getTimeInMillis()));
			idApartado= DaoFactory.getInstance().insert(sesion, this.ventaFinalizada.getDetailApartado());
			if(idApartado >= 1) {
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
	
	private String toTelefonoCliente(Session sesion, Long idCliente) throws Exception {
		String regresar= null;
		Boolean inicio = false;
		MotorBusqueda motor= new MotorBusqueda(idCliente);
		List<ClienteTipoContacto>contactos= motor.toClientesTipoContacto(sesion);
		if(!contactos.isEmpty()) {
			for(ClienteTipoContacto contacto: contactos) {
				if(contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO_CASA.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO_NEGOCIO.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO_PERSONAL.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO_TRABAJO.getKey())) {
					if(!inicio)
						contacto.getValor();
				} // if
			} // for			
		} // id
		return regresar;
	} // toTelefonoCliente
	
	public boolean verificarCierreCaja(Session sesion) throws Exception {
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
			if(!(cierre!= null && cierre.isValid())) {
				nuevo= this.toCierreNuevo(sesion);
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
				if(limiteCaja< this.efectivo)
					regresar= this.registraAlertaRetiro(sesion, this.idCierreVigente, this.efectivo, limiteCaja);				
			}	// if		
			else {
				this.efectivo= this.toAcumuladoCierreActivo(sesion, this.idCierreVigente);
				if(limiteCaja< this.efectivo)
					regresar= this.registraAlertaRetiro(sesion, this.idCierreVigente, this.efectivo, limiteCaja);
			} // else
		} // if		
		return regresar;
	} // alterarCierreCaja
	
	private boolean registraAlertaRetiro(Session sesion, Long idCierre, Double efectivo, Double limite) throws Exception {
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
				alerta.setImporte(efectivo<= 0D ? 0D: efectivo);
				alerta.setMensaje("EL SALDO EN EFECTIVO ["+ 
						 Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, efectivo)+ 
						 "] DE ESTA CAJA SUPERA AL LIMITE ["+ Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, limite)
						 + "] ESTABLECIDO PARA LA MISMA, POR FAVOR REALICE UN RETIRO DE CAJA.");
				regresar= DaoFactory.getInstance().insert(sesion, alerta)>= 1L;
			} // if				
		} // try
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // registraAlertaRetiro
	
	private Double toAcumuladoCierreActivo(Session sesion, Long idCierre) throws Exception {
		Double regresar          = 0D;
		Map<String, Object>params= null;
		try{
			params= new HashMap<>();		
			params.put("idCierre", idCierre);
			TcManticCierresCajasDto saldo= (TcManticCierresCajasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresCajasDto.class, "TcManticCierresCajasDto", "caja", params);
			if(saldo!= null)
			  regresar= saldo.getSaldo();		
		} // try
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toAcumuladoCierreActivo	
	
	private void toCierreActivo(Session sesion, Long idTipoMedioPago) throws Exception {
		Map<String, Object>params     = null;
		TcManticCierresCajasDto cierre= null;
		ETipoMediosPago medioPago     = null;
		Double abono                  = 0D;		
		try{
			params= new HashMap<>();
			params.put("idCierre", this.idCierreVigente);
			params.put("medioPago", idTipoMedioPago);
			cierre= (TcManticCierresCajasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresCajasDto.class, "TcManticCierresCajasDto", "cajaMedioPago", params);			
			medioPago= ETipoMediosPago.fromIdTipoPago(idTipoMedioPago);
			switch(medioPago) {
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
			cierre.setAcumulado(cierre.getAcumulado()+ abono);			
			cierre.setSaldo(cierre.getDisponible()+ cierre.getAcumulado());
			// LOG.error("Medio pago:" + idTipoMedioPago + ", Cierre:" + cierreCaja.getIdCierre() + ", Caja:" + cierreCaja.getIdCaja() + ", Disponible:" + cierreCaja.getDisponible() + ", Abono:" + abono + ", Acumulado:" + cierreCaja.getAcumulado() + ", Saldo:" + cierreCaja.getSaldo());
			DaoFactory.getInstance().update(sesion, cierre);		
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
		if(cierreNuevo.toNewCierreCaja(sesion)) {
			this.isNuevoCierre= true;				
			this.efectivo     = 0D;
			regresar          = cierreNuevo.getCierre();
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
	
	private boolean pagarVenta(Session sesion, Long idEstatusVenta) throws Exception {
		boolean regresar         = false;
		Map<String, Object>params= null;
		Siguiente consecutivo    = null;
		boolean validacionEstatus= false;
		try {									
			validacionEstatus= !idEstatusVenta.equals(EEstatusVentas.APARTADOS.getIdEstatusVenta());
			consecutivo= this.toSiguiente(sesion);			
			this.getOrden().setCticket(consecutivo.getOrden());			
			this.getOrden().setTicket(consecutivo.getConsecutivo());
			this.getOrden().setIdVentaEstatus(idEstatusVenta);			
			this.getOrden().setIdFacturar(this.ventaFinalizada.isFacturar() && validacionEstatus ? SI : NO);
			this.getOrden().setIdCredito(this.ventaFinalizada.isCredito() && validacionEstatus ? SI : NO);
			this.getOrden().setCobro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      if(this.getOrden().getIdBanco()!= null && this.getOrden().getIdBanco()<= -1L)
        this.getOrden().setIdBanco(null);
			if(this.ventaFinalizada.isFacturar() && validacionEstatus) {				
				this.clienteDeault= getOrden().getIdCliente().equals(toClienteDefault(sesion));
				if(this.clienteDeault) {
					this.ventaFinalizada.getCorreosContacto().add(this.ventaFinalizada.getCelular());
					this.ventaFinalizada.getCorreosContacto().add(this.ventaFinalizada.getTelefono());
					this.ventaFinalizada.getDomicilio().setIdTipoDomicilio(ETiposDomicilios.FISCAL.getKey());
					this.ventaFinalizada.getCliente().setIdUsoCfdi(getOrden().getIdUsoCfdi());
					this.ventaFinalizada.getCliente().setIdTipoVenta(1L);
					this.setClienteVenta(new ClienteVenta(this.ventaFinalizada.getCliente(), this.ventaFinalizada.getDomicilio(), null));
					this.procesarCliente(sesion);
					this.getOrden().setIdCliente(getIdClienteNuevo());
				} // if
				else
					this.registraClientesTipoContacto(sesion, this.getOrden().getIdCliente());				
			} // if						
			if(DaoFactory.getInstance().update(sesion, this.getOrden())>= 1L) {				
				if(this.registraBitacora(sesion, this.getOrden().getIdVenta(), idEstatusVenta, "LA VENTA HA SIDO FINALIZADA")) {
					params= new HashMap<>();
					params.put("idVenta", this.getOrden().getIdVenta());
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticVentasDetallesDto.class, params)>= 1;
					this.toFillArticulos(sesion, this.ventaFinalizada.getArticulos());
					this.validarCabecera(sesion);
				} // if
			} // if			
		} // try		
		finally{
			Methods.clean(params);
			setMessageError("ERROR AL REGISTRAR EL PAGO DE LA VENTA");
		} // finally			
		return regresar;
	} // pagarVenta
	
	private void validarCabecera(Session sesion) throws Exception {
		Double diferencia= 0D;
		try {
			if(getOrden().getTotal()> this.totalDetalle)
				diferencia= getOrden().getTotal() - this.totalDetalle;							
			else if(this.totalDetalle > getOrden().getTotal())
				diferencia= this.totalDetalle - getOrden().getTotal();			
			if(diferencia >= 1D)
				this.actualizarCabecera(sesion, diferencia);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // validarCabezera
	
	private void actualizarCabecera(Session sesion, Double diferencia) throws Exception {
		TcManticVentasDiferenciasDto inconsistente= null;
		Double total     = 0D;
		Double subtotal  = 0D;
		Double impuestos = 0D;
		Double descuentos= 0D;
		try {
			inconsistente= new TcManticVentasDiferenciasDto();
			inconsistente.setIdVenta(getOrden().getIdVenta());
			inconsistente.setDiferencia(diferencia);
			inconsistente.setIdUsuario(JsfBase.getIdUsuario());
			inconsistente.setImporte(getOrden().getTotal());
			inconsistente.setImporteDetalle(this.totalDetalle);
			if(DaoFactory.getInstance().insert(sesion, inconsistente)>= 1L) {
				LOG.info("Se registro una diferencia en la orden.");
				for (Articulo articulo: this.ventaFinalizada.getArticulos()) {
					if(articulo.isValid()) {
						total     = Numero.toRedondearSat(total     + articulo.getImporte());
						subtotal  = Numero.toRedondearSat(subtotal  + articulo.getSubTotal());
						impuestos = Numero.toRedondearSat(impuestos + articulo.getImpuestos());
						descuentos= Numero.toRedondearSat(descuentos+ articulo.getDescuentos());
					} // if
				} // for
				getOrden().setTotal(total);
				getOrden().setSubTotal(subtotal);
				getOrden().setImpuestos(impuestos);
				getOrden().setDescuentos(descuentos);
				DaoFactory.getInstance().update(sesion, this.getOrden());
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // actualizarCabecera
	
	@Override
	protected void toFillArticulos(Session sesion, List<Articulo> detalles) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticVentasDetallesDto", "detalle", getOrden().toMap());
		for (Articulo item: todos) 
			if(detalles.indexOf(item)< 0) {
        TcManticVentasDetallesDto erase= item.toVentaDetalle();
				DaoFactory.getInstance().delete(sesion, erase);
      } // if  
		for (Articulo articulo: detalles) {
			if(articulo.isValid()) {
				TcManticVentasDetallesDto item= articulo.toVentaDetalle();
				item.setIdVenta(getOrden().getIdVenta());
				if(DaoFactory.getInstance().findIdentically(sesion, TcManticVentasDetallesDto.class, item.toMap())== null) 
					DaoFactory.getInstance().insert(sesion, item);
				else
					DaoFactory.getInstance().update(sesion, item);
				this.totalDetalle= this.totalDetalle + articulo.getImporte();
			} // if
		} // for
	} // toFillArticulos
	
  @Override
	protected Siguiente toSiguiente(Session sesion) throws Exception {
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
				if(clienteTipoContacto.getValor()!= null && !Cadena.isVacio(clienteTipoContacto.getValor())) {
					if(clienteTipoContacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey()) && validateOrden) {
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
							if(DaoFactory.getInstance().findIdentically(sesion, TrManticClienteTipoContactoDto.class, dto.toMap())== null)
								validate = DaoFactory.getInstance().insert(sesion, dto)>= 1L;
							break;
						case UPDATE:
							if(DaoFactory.getInstance().findIdentically(sesion, TrManticClienteTipoContactoDto.class, dto.toMap())== null)
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
		catch(Exception e) {
			throw e;
		} // catch
    finally {
      setMessageError("Error al registrar los tipos de contacto, verifique que no haya duplicados");
    } // finally
    return regresar;
	} // registrarContactosCliente
	
	private boolean registrarFactura(Session sesion) throws Exception {
		boolean regresar             = false;
		TcManticFacturasDto documento= null;
		StringBuilder correos        = null;
		try {			
			documento= new TcManticFacturasDto();
			documento.setIdUsuario(JsfBase.getIdUsuario());
			documento.setIntentos(0L);
      if(Cadena.isVacio(this.correosFactura) && this.ventaFinalizada!= null && this.ventaFinalizada.getCorreosContacto()!= null && !this.ventaFinalizada.getCorreosContacto().isEmpty()) {
        correos= new StringBuilder("");
        if(!this.ventaFinalizada.getCorreosContacto().isEmpty()) {				
          for(ClienteTipoContacto correo: this.ventaFinalizada.getCorreosContacto())
            correos.append(correo.getValor()).append(", ");
        } // if						
  			this.correosFactura= correos.length()> 0 ? correos.substring(0, correos.length()-2) : correos.toString();			
      } // if  
			documento.setCorreos(this.correosFactura);
			documento.setObservaciones(this.ventaFinalizada.getObservaciones());
			documento.setIdFacturaEstatus(EEstatusFacturas.REGISTRADA.getIdEstatusFactura());
			if(DaoFactory.getInstance().insert(sesion, documento)>= 1L) {	
				registrarBitacoraFactura(sesion, documento.getIdFactura(), EEstatusFacturas.REGISTRADA.getIdEstatusFactura(), this.ventaFinalizada.getObservaciones());
				getOrden().setIdFactura(documento.getIdFactura());
				regresar= DaoFactory.getInstance().update(sesion, getOrden())>= 1L;				
			} // if
			this.idFactura= documento.getIdFactura();
			this.factura  = documento;
		} // try		 // try		
		finally{
			setMessageError("ERROR AL REGISTRAR LA FACTURA");
		} // finally
		return regresar;
	} // registrarFactura
	
	private boolean registrarPagos(Session sesion) throws Exception {
		List<TrManticVentaMedioPagoDto> pagos= null;
		boolean regresar= false;		
		int count       = 0;
		try {
			pagos= this.loadPagos(sesion);
			for(TrManticVentaMedioPagoDto pago: pagos) {
				if(DaoFactory.getInstance().insert(sesion, pago)>= 1L)
					this.alterarCierreCaja(sesion, pago.getIdTipoMedioPago());
				count++;
			} // for
			regresar= count== pagos.size();
		} // try		
		finally {
			this.setMessageError("ERROR AL REGISTRAR LOS PAGOS");
		} // finally
		return regresar;
	} // registrarPagos
	
	private List<TrManticVentaMedioPagoDto> loadPagos(Session sesion) throws Exception {
		List<TrManticVentaMedioPagoDto> regresar= null;
		TrManticVentaMedioPagoDto pago          = null;		
		regresar= new ArrayList<>();
		pago= this.toPagoEfectivo();
		if(pago!= null)
			regresar.add(pago);			
		pago= this.toPagoTarjetaCredito();
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
	
	private TrManticVentaMedioPagoDto toPagoEfectivo() {
		TrManticVentaMedioPagoDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getEfectivo() > 0D) {
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
		if(this.ventaFinalizada.getTotales().getDebito()> 0D) {
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
	
	private TrManticVentaMedioPagoDto toPagoTarjetaCredito() {
		TrManticVentaMedioPagoDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getCredito()> 0D) {
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
	
	private TrManticVentaMedioPagoDto toPagoTransferencia() {
		TrManticVentaMedioPagoDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getTransferencia()> 0D) {
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
	
	private TrManticVentaMedioPagoDto toPagoCredito(Session sesion) throws Exception {
		TrManticVentaMedioPagoDto regresar= null;
		Double totalCredito               = 0D;		
		if(this.ventaFinalizada.isCredito()) {
			totalCredito= this.ventaFinalizada.getTotales().getTotales().getTotal() - (this.ventaFinalizada.getTotales().getPago() - this.ventaFinalizada.getTotales().getCambio());
			if(totalCredito > 0D) {					
				this.registrarDeuda(sesion, totalCredito);	
				this.actualizarSaldoCatalogoCliente(sesion, getOrden().getIdCliente(), totalCredito, true);
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
	
	private TrManticVentaMedioPagoDto toPagoCheque() {
		TrManticVentaMedioPagoDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getCheque()> 0D) {
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
	
	private boolean registrarPagosApartado(Session sesion, Long idApartado) throws Exception {
		List<TcManticApartadosPagosDto> pagos= null;
		boolean regresar= false;		
		int count       = 0;
		try {
			pagos= this.loadPagosApartado(idApartado);
			for(TcManticApartadosPagosDto pago: pagos) {
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
	
	private List<TcManticApartadosPagosDto> loadPagosApartado(Long idApartado) throws Exception {
		List<TcManticApartadosPagosDto> regresar= null;
		TcManticApartadosPagosDto pago          = null;		
		regresar= new ArrayList<>();
		pago= this.toPagoEfectivoApartado(idApartado);
		if(pago!= null)
			regresar.add(pago);			
		pago= this.toPagoTarjetaDebitoApartado(idApartado);
		if(pago!= null)
			regresar.add(pago);
		pago= this.toPagoTarjetaCreditoApartado(idApartado);
		if(pago!= null)
			regresar.add(pago);
		pago= this.toPagoTransferenciaApartado(idApartado);
		if(pago!= null)
			regresar.add(pago);
		pago= this.toPagoChequeApartado(idApartado);
		if(pago!= null)
			regresar.add(pago);					
		return regresar;
	} // loadPagos
	
	private TcManticApartadosPagosDto toPagoEfectivoApartado(Long idApartado) {
		TcManticApartadosPagosDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getEfectivo() > 0D) {
			regresar= new TcManticApartadosPagosDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdApartado(idApartado);
			regresar.setPago(this.ventaFinalizada.getTotales().getEfectivo() - this.ventaFinalizada.getTotales().getCambio());
			regresar.setIdCierre(this.idCierreVigente);
		} // if		
		return regresar;
	} // toPagoEfectivo
	
	private TcManticApartadosPagosDto toPagoTarjetaDebitoApartado(Long idApartado) {
		TcManticApartadosPagosDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getDebito()> 0D) {
			regresar= new TcManticApartadosPagosDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.TARJETA_DEBITO.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdApartado(idApartado);
			regresar.setPago(this.ventaFinalizada.getTotales().getDebito());				
			regresar.setIdCierre(this.idCierreVigente);
		} // if		
		return regresar;
	} // toPagoTarjetaDebitoApartado
	
	private TcManticApartadosPagosDto toPagoTarjetaCreditoApartado(Long idApartado) {
		TcManticApartadosPagosDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getCredito()> 0D) {
			regresar= new TcManticApartadosPagosDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.TARJETA_CREDITO.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdApartado(idApartado);
			regresar.setPago(this.ventaFinalizada.getTotales().getCredito());				
			regresar.setIdCierre(this.idCierreVigente);
		} // if		
		return regresar;
	} // toPagoTarjetaCreditoApartado
	
	private TcManticApartadosPagosDto toPagoTransferenciaApartado(Long idApartado) {
		TcManticApartadosPagosDto regresar= null;
		if(this.ventaFinalizada.getTotales().getTransferencia()> 0D) {
			regresar= new TcManticApartadosPagosDto();
			regresar.setIdTipoMedioPago(ETipoMediosPago.TRANSFERENCIA.getIdTipoMedioPago());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdApartado(idApartado);
			regresar.setPago(this.ventaFinalizada.getTotales().getTransferencia());			
			regresar.setIdCierre(this.idCierreVigente);
		} // if
		return regresar;
	} // toPagoTransferencia
	
	private TcManticApartadosPagosDto toPagoChequeApartado(Long idApartado) {
		TcManticApartadosPagosDto regresar= null;		
		if(this.ventaFinalizada.getTotales().getCheque()> 0D) {
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
	public Date toLimiteCredito(Session sesion) throws Exception {
		Date regresar              = null;
		TcManticClientesDto cliente= null;
		Long addDias               = 15L;
		Calendar calendar          = null;		
		if(!this.ventaFinalizada.isFacturar() || (this.ventaFinalizada.isFacturar() && !this.clienteDeault)) {
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
	
	private boolean alterarStockArticulos(Session sesion, List<Articulo> articulos, Long idAlmacen, boolean movimiento) throws Exception {
		TcManticAlmacenesArticulosDto almacenArticulo= null;
		TcManticArticulosDto articuloVenta           = null;		
		Map<String, Object>params                    = null;
		boolean regresar                             = false;
		int count                                    = 0; 
		Double stock                                 = 0D;
		try {			
			params= new HashMap<>();
			for(Articulo articulo: articulos) {
				if(articulo.isValid()) {
					params.put(Constantes.SQL_CONDICION, "id_articulo="+ articulo.getIdArticulo()+ " and id_almacen="+ idAlmacen);
					almacenArticulo= (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().toEntity(sesion, TcManticAlmacenesArticulosDto.class, "TcManticAlmacenesArticulosDto", "row", params);
					if(almacenArticulo!= null) {
						stock= almacenArticulo.getStock();
						almacenArticulo.setStock(almacenArticulo.getStock() - articulo.getCantidad());
						regresar= DaoFactory.getInstance().update(sesion, almacenArticulo)>= 1L;
					} // if
					else{
						stock= 0D;
						regresar= this.generarAlmacenArticulo(sesion, idAlmacen, articulo.getIdArticulo(), articulo.getCantidad());
					} // else					      
          if(movimiento)
					  this.registrarMovimiento(sesion, idAlmacen, articulo.getCantidad(), articulo.getIdArticulo(), stock, this.ventaFinalizada.getTicketVenta().getIdUsuario());
					if(regresar) {
						articuloVenta= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, articulo.getIdArticulo());
						articuloVenta.setStock(articuloVenta.getStock() - articulo.getCantidad());
						if(DaoFactory.getInstance().update(sesion, articuloVenta)>= 1L)
							regresar= this.actualizaInventario(sesion, idAlmacen, articulo.getIdArticulo(), articulo.getCantidad());
					} // if
					if(regresar)
						count++;
				} // if
				else
					count++;
			} // for		
			regresar= count== articulos.size();		
		} // try		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // alterarStockArticulos

	private boolean checkOrdenServicio(Session sesion) throws Exception {
    boolean regresar          = false;
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
			params.put("idVenta", this.ventaFinalizada.getTicketVenta().getIdVenta());
			TcManticServiciosDto servicio= (TcManticServiciosDto) DaoFactory.getInstance().toEntity(sesion, TcManticServiciosDto.class, "TcManticServiciosDto", "venta", params);
			if(servicio!= null) {
				servicio.setIdServicioEstatus(EEstatusServicios.PAGADO.getIdEstatusServicio());
        DaoFactory.getInstance().update(sesion, servicio);
			  params.put("idServicio", servicio.getIdServicio());
        // SON LOS ARTICULOS NORMALES DE LAS VENTAS
			  params.put("idArticuloTipo", 1L);
        List<Articulo> articulos= (List<Articulo>) DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticServiciosDetallesDto", "inventario", params);
        if(articulos!= null && !articulos.isEmpty())
          this.alterarStockArticulos(sesion, articulos, this.ventaFinalizada.getTicketVenta().getIdAlmacen(), true);
        // SON LAS REFACCIONES QUE SE USARON EN LA ORDEN DE REPARACIÓN 
			  params.put("idArticuloTipo", 2L);
        List<Articulo> refacciones= (List<Articulo>) DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticServiciosDetallesDto", "inventario", params);
        if(refacciones!= null && !refacciones.isEmpty())
          this.alterarStockArticulos(sesion, refacciones, servicio.getIdAlmacen(), articulos== null || articulos.isEmpty());
        regresar= true;
      } // if
      else
        regresar= this.alterarStockArticulos(sesion, this.ventaFinalizada.getArticulos(), this.ventaFinalizada.getTicketVenta().getIdAlmacen(), true);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
	private void registrarMovimiento(Session sesion, Long idAlmacen, Double cantidad, Long idArticulo, Double stock, Long idUsuario) throws Exception {
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
	
	private boolean generarAlmacenArticulo(Session sesion, Long idAlmacen, Long idArticulo, Double cantidad) throws Exception {
		boolean regresar                             = false;
		TcManticAlmacenesArticulosDto almacenArticulo= null;		
		almacenArticulo= new TcManticAlmacenesArticulosDto();
		almacenArticulo.setIdAlmacen(idAlmacen);
		almacenArticulo.setIdArticulo(idArticulo);
		almacenArticulo.setIdUsuario(JsfBase.getIdUsuario());
		almacenArticulo.setMaximo(0D);
		almacenArticulo.setMinimo(0D);
		almacenArticulo.setStock(0 - cantidad);
		almacenArticulo.setIdAlmacenUbicacion(toIdAlmacenUbicacion(sesion));
		regresar= DaoFactory.getInstance().insert(sesion, almacenArticulo)>= 1L;		
		return regresar;
	} // generarAlmacenArticulo
	
	private Long toIdAlmacenUbicacion(Session sesion) throws Exception {
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
	
	private boolean actualizaInventario(Session sesion, Long idAlmacen, Long idArticulo, Double cantidad) throws Exception {
		boolean regresar                 = false;
		TcManticInventariosDto inventario= null;
		Map<String, Object>params        = null;
		try {
			params= new HashMap<>();
			params.put("idAlmacen", idAlmacen);
			params.put("idArticulo", idArticulo);
			inventario= (TcManticInventariosDto) DaoFactory.getInstance().toEntity(sesion, TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario!= null) {
				inventario.setSalidas(inventario.getSalidas()+ cantidad);
				inventario.setStock(inventario.getStock()- cantidad);
				regresar= DaoFactory.getInstance().update(sesion, inventario)>= 1L;
			} // if
			else {
				inventario= new TcManticInventariosDto();
				inventario.setEjercicio(Long.valueOf(Fecha.getAnioActual()));
				inventario.setEntradas(0D);
				inventario.setIdAlmacen(idAlmacen);
				inventario.setIdArticulo(idArticulo);
				inventario.setIdUsuario(JsfBase.getIdUsuario());
				inventario.setInicial(0D);
				inventario.setSalidas(cantidad);
				inventario.setStock(cantidad* -1);
				inventario.setIdAutomatico(1L);
				regresar= DaoFactory.getInstance().insert(sesion, inventario)>= 1L;
			} // else				
		} // try
		finally {			
			Methods.clean(params);
		} // catch		
		return regresar;
	} // actualizaInventario
	
	private boolean generarTimbradoFactura(Session sesion) {
		boolean regresar             = true;
		TransaccionFactura factura   = null;
		CFDIGestor gestor            = null;
		ClienteFactura clienteFactura= null;
		TcManticVentasDto venta      = null;
		try {			
			sesion.flush();
			this.actualizarClienteFacturama(sesion, this.facturacion.getIdCliente(), this.facturacion.getIdClienteDomicilio());
			gestor= new CFDIGestor(this.facturacion.getIdVenta());			
			factura= new TransaccionFactura();			
			factura.actualizarFacturaAutomatico(sesion, this.facturacion.getIdFactura(), this.facturacion.getIdUsuario(), EEstatusFacturas.PROCESANDO.getIdEstatusFactura());
			factura.setArticulos(gestor.toDetalleCfdiVentas(sesion));
			clienteFactura= this.facturacion.getIdTipoDocumento().equals(ETipoDocumento.VENTAS_NORMALES.getIdTipoDocumento()) ? gestor.toClienteCfdiVenta(sesion) : gestor.toClienteCfdiFicticia(sesion);			
			clienteFactura.setMetodoPago(ETipoPago.fromIdTipoPago(this.facturacion.getIdTipoPago()).getClave());
			factura.setCliente(clienteFactura);
			factura.getCliente().setIdFactura(this.facturacion.getIdFactura());
			factura.generarCfdi(sesion, this.facturacion.getIdEmpresa().toString(), this.facturacion.getIdUsuario());						
			venta= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.facturacion.getIdVenta());
			venta.setIdVentaEstatus(this.facturacion.getIdTipoDocumento().equals(ETipoDocumento.VENTAS_NORMALES.getIdTipoDocumento()) ? EEstatusVentas.TIMBRADA.getIdEstatusVenta() : EEstatusFicticias.TIMBRADA.getIdEstatusFicticia());
			DaoFactory.getInstance().update(sesion, venta);
		} // try
		catch (Exception e) {	
			Error.mensaje(e);
			try {
				if(factura!= null)
					factura.actualizarFacturaAutomatico(sesion, this.facturacion.getIdFactura(), this.facturacion.getIdUsuario(), EEstatusFacturas.AUTOMATICO.getIdEstatusFactura());
				regresar= false;
			} // try
			catch (Exception ex) {				
				Error.mensaje(ex);				
			} // catch									
		} // catch				
		return regresar;
	} // generarTimbradoFactura
	
	private boolean assignStatusAutomatico(Session sesion) throws Exception {
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
	
	private boolean actualizarVenta(Session sesion, Long idEstatusVenta) throws Exception {
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
	
	private boolean liquidarApartado(Session sesion) throws Exception {		
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
			for(TcManticApartadosPagosDto pago: pagos) {
				if(DaoFactory.getInstance().insert(sesion, pago)>= 1L) {
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
    try {
      TcManticApartadosBitacoraDto bitacora= new TcManticApartadosBitacoraDto();
      bitacora.setIdApartado(apartado.getIdApartado());
      bitacora.setIdApartadoEstatus(apartado.getIdApartadoEstatus());
      bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      bitacora.setIdUsuario(JsfBase.getIdUsuario());
      bitacora.setJustificacion("PAGO REALIZADO DESDE EL MÓDULO DE CAJA POR CANTIDAD " + abonado);
      regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;		
    } // try
    catch(Exception e) {
      throw e;
    } // catch
		return regresar;
  } // insertarBitacora
  
  public boolean procesarCancela(Session sesion) throws Exception {
    boolean regresar          = false;
    Map<String, Object> params= null;
		List<TrManticVentaMedioPagoDto> pagos= null;
		try {									
      params = new HashMap<>();      
      TcManticVentasDto venta= (TcManticVentasDto)DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.idVenta);
        // CANCELAR LA FACTURA ACTUAL PARA GENERAR LA NUEVA FACTURA
      if(this.factura.isValid()) {
        if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.DESARROLLO)) 
          CFDIFactory.getInstance().cfdiRemove(this.factura.getIdFacturama());
        this.factura.setCancelada(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        this.factura.setIdFacturaEstatus(EEstatusFacturas.CANCELADA.getIdEstatusFactura());
        DaoFactory.getInstance().update(sesion, factura);
        this.registrarBitacoraFactura(sesion, factura.getIdFactura(), EEstatusFacturas.CANCELADA.getIdEstatusFactura(), "TICKET CANCELADO");
      } // if  
      // CON ESTO SE ALTERA EL CIERRE DE CAJA DE ESE DIA Y SE AJUSTA A QUE EL TICKET AHORA ES EN EFECTIVO
      params.put("idVenta", this.idVenta);
      pagos= (List<TrManticVentaMedioPagoDto>)DaoFactory.getInstance().toEntitySet(sesion, TrManticVentaMedioPagoDto.class, "TrManticVentaMedioPagoDto", "detalle", params);
      if(pagos!= null && !pagos.isEmpty()) {
        TrManticVentaMedioPagoDto item= pagos.get(0);
        // REGISTRAR EN CAJA EL PAGO EN NEGATIVO DEL IMPORTE DEL TICKET ORIGINAL
        TrManticVentaMedioPagoDto clon= (TrManticVentaMedioPagoDto)item.clone();
        item.setKey(-1L);
        clon.setIdCierre(this.idCierreVigente);
        clon.setIdVentaMedioPago(-1L);
        clon.setIdVenta(venta.getIdVenta());
        clon.setImporte(clon.getTotal()* -1D);
        clon.setTotal(clon.getTotal()* -1D);
        clon.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        clon.setObservaciones("SE CANCELO EL TICKET ["+ this.getOrden().getTicket()+ "]");
        DaoFactory.getInstance().insert(sesion, clon);
      } // if
      // CANCELAR EL TICKET ANTERIOR PARA QUE NO SE PUEDA HACER OTRA DEVOLUCION 
      venta.setIdVentaEstatus(EEstatusVentas.CANCELADA.getIdEstatusVenta());
      venta.setObservaciones((this.getOrden().getObservaciones()!= null? "": this.getOrden().getObservaciones().concat(", ")).concat("TICKET CANCELADO"));
      DaoFactory.getInstance().update(sesion, venta);
      this.registraBitacora(sesion, venta.getIdVenta(), venta.getIdVentaEstatus(), "TICKET CANCELADO");
      regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;    
  }
  
  public boolean procesarRefactura(Session sesion) throws Exception {
    boolean regresar     = false;
    Siguiente consecutivo= null;
    Siguiente cuenta     = null;
		List<TrManticVentaMedioPagoDto> pagos= null;
    Map<String, Object> params           = null;
		try {									
      params = new HashMap<>();      
      TcManticVentasDto venta= (TcManticVentasDto)DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.idVenta);
      cuenta= super.toSiguiente(sesion);			
      this.getOrden().setConsecutivo(cuenta.getOrden());			
      this.getOrden().setOrden(cuenta.getOrden());
      this.getOrden().setIdUsuario(JsfBase.getIdUsuario());
			consecutivo= this.toSiguiente(sesion);			
			this.getOrden().setCticket(consecutivo.getOrden());			
			this.getOrden().setTicket(consecutivo.getConsecutivo());
      if(DaoFactory.getInstance().insert(sesion, this.getOrden())> 0L) {
        // CANCELAR LA FACTURA ACTUAL PARA GENERAR LA NUEVA FACTURA
        if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.DESARROLLO))
          CFDIFactory.getInstance().cfdiRemove(this.factura.getIdFacturama());
        this.factura.setCancelada(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        this.factura.setIdFacturaEstatus(EEstatusFacturas.CANCELADA.getIdEstatusFactura());
        DaoFactory.getInstance().update(sesion, factura);
        this.registrarBitacoraFactura(sesion, factura.getIdFactura(), EEstatusFacturas.CANCELADA.getIdEstatusFactura(), "SE CANCELÓ POR REFACTURACIÓN");
        this.ventaFinalizada= new VentaFinalizada();
        this.ventaFinalizada.setObservaciones("SE REFACTURO, TICKET ORIGINAL ["+ venta.getTicket()+ "]");
        this.ventaFinalizada.setArticulos(this.getArticulos());
        this.correosFactura= this.factura.getCorreos();
        this.toFillArticulos(sesion, this.getArticulos());
        this.validarCabecera(sesion);
        this.registraBitacora(sesion, this.getOrden().getIdVenta(), venta.getIdVentaEstatus(), "SE REFACTURO, TICKET ORIGINAL ["+ venta.getTicket()+ "]");
        // CON ESTO SE ALTERA EL CIERRE DE CAJA DE ESE DIA Y SE AJUSTA A QUE EL TICKET AHORA ES EN EFECTIVO
        params.put("idVenta", this.idVenta);
        pagos= (List<TrManticVentaMedioPagoDto>)DaoFactory.getInstance().toEntitySet(sesion, TrManticVentaMedioPagoDto.class, "TrManticVentaMedioPagoDto", "detalle", params);
        if(pagos!= null && !pagos.isEmpty()) {
          // REGISTRAR EN CAJA EL PAGO EN POSITIVO TICKET NUEVO
  			  TrManticVentaMedioPagoDto item= pagos.get(0);
          item.setKey(-1L);
          item.setIdVenta(this.getOrden().getIdVenta());
          item.setIdCierre(this.idCierreVigente);
          // SI TIENE MAS DE UN METODO DE PAGO SE VA A DEJAR EN EFECTIVO
          if(pagos.size()> 1) {
            item.setIdTipoMedioPago(1L); 
            item.setIdBanco(null);
            item.setReferencia(null);
          } // if  
          item.setImporte(this.getOrden().getTotal());
          item.setTotal(this.getOrden().getTotal());
          item.setObservaciones("SE REFACTURO, TICKET ORIGINAL ["+ venta.getTicket()+ "]");
          item.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
          item.setIdUsuario(JsfBase.getIdUsuario());
	  			DaoFactory.getInstance().insert(sesion, item);
          // REGISTRAR EN CAJA EL PAGO EN NEGATIVO DEL IMPORTE DEL TICKET ORIGINAL
          TrManticVentaMedioPagoDto clon= (TrManticVentaMedioPagoDto)item.clone();
          clon.setIdVentaMedioPago(-1L);
          clon.setIdVenta(venta.getIdVenta());
          clon.setImporte(clon.getTotal()* -1D);
          clon.setTotal(clon.getTotal()* -1D);
          clon.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
          clon.setObservaciones("SE REFACTURO, TICKET NUEVO ["+ this.getOrden().getTicket()+ "]");
	  			DaoFactory.getInstance().insert(sesion, clon);
        } // if
        // CANCELAR EL TICKET ANTERIOR PARA QUE NO SE PUEDA HACER OTRA DEVOLUCION 
        venta.setIdVentaEstatus(EEstatusVentas.CANCELADA.getIdEstatusVenta());
        venta.setObservaciones((venta.getObservaciones()!= null? "": venta.getObservaciones().concat(", ")).concat("SE REFACTURO, TICKET NUEVO ["+ this.getOrden().getTicket()+ "]"));
        DaoFactory.getInstance().update(sesion, venta);
        this.registraBitacora(sesion, venta.getIdVenta(), venta.getIdVentaEstatus(), "CANCELADA POR REFACTURACION, TICKET NUEVO["+ consecutivo.getConsecutivo()+ "]");
        // GENERAR LA NUEVA FACTURA PARTIENDO DEL NUEVO TICKET
        if(this.registrarFactura(sesion)) {
		      TransaccionFactura facturama= new TransaccionFactura();
   			  facturama.actualizarFacturaAutomatico(sesion, this.idFactura, JsfBase.getIdUsuario());
        } // if  
      } // if  
      regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
}

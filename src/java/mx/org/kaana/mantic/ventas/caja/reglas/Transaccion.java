package mx.org.kaana.mantic.ventas.caja.reglas;

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
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresAlertasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticVentaMedioPagoDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETiposDomicilios;
import mx.org.kaana.mantic.ventas.beans.ClienteVenta;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Cierre;
import org.apache.log4j.Logger;
import org.hibernate.Session;

public class Transaccion extends mx.org.kaana.mantic.ventas.reglas.Transaccion{

	private static final Logger LOG          = Logger.getLogger(Transaccion.class);
	private static final String GENERAL      = "GENERAL";
	private static final String CIERRE_ACTIVO= "1,2";
	private static final Long SI             = 1L;
	private static final Long NO             = 2L;
	private VentaFinalizada ventaFinalizada;
	private IBaseDto dto;
	private boolean clienteDeault;
	private boolean isNuevoCierre;
	private Double cierreCaja;
	private Long idCierreVigente;
	
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
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar  = false;
		this.isNuevoCierre= false;
		this.cierreCaja   = 0D;		
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
	
	private boolean procesaCotizacion(Session sesion) throws Exception{
		boolean regresar            = false;
		Calendar calendar           = null;
		TcManticVentasDto cotizacion= null;
		Long consecutivoCotizacion  = -1L;
		try {
			cotizacion= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.dto.getKey());
			if(Cadena.isVacio(cotizacion.getCotizacion())){
				consecutivoCotizacion= this.toSiguienteCotizacion(sesion, cotizacion.getIdEmpresa());
				cotizacion.setCcotizacion(consecutivoCotizacion);
				cotizacion.setCotizacion(Fecha.getAnioActual() + Cadena.rellenar(consecutivoCotizacion.toString(), 5, '0', true));
			} // if
			calendar= Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 8);
			cotizacion.setVigencia(new Date(calendar.getTimeInMillis()));
			cotizacion.setIdVentaEstatus(EEstatusVentas.COTIZACION.getIdEstatusVenta());					
			regresar= DaoFactory.getInstance().update(sesion, cotizacion)>= 1L;
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // procesaCotizacion
	
	private Long toSiguienteCotizacion(Session sesion, Long idEmpresa) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", idEmpresa);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "siguienteCotizacion", params, "siguiente");
			if(next.getData()!= null)
				regresar= next.toLong();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguienteCotizacion
	
	private boolean procesarVenta(Session sesion) throws Exception{
		boolean regresar= false;
		try {
			regresar= pagarVenta(sesion, this.ventaFinalizada.getApartado() ? EEstatusVentas.APARTADO.getIdEstatusVenta() : (this.ventaFinalizada.isCredito() ? EEstatusVentas.CREDITO.getIdEstatusVenta() : EEstatusVentas.PAGADA.getIdEstatusVenta()));
			if(regresar){
				if(this.ventaFinalizada.isFacturar() && !this.ventaFinalizada.getApartado())
					regresar= registrarFactura(sesion);
				if(verificarCierreCaja(sesion)){
					if(registrarPagos(sesion))					
						regresar= alterarStockArticulos(sesion);
				} // if
				if(this.ventaFinalizada.getApartado()){
					regresar= registrarApartado(sesion);
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarVenta
	
	private boolean registrarApartado(Session sesion) throws Exception{
		boolean regresar= false;		
		Long consecutivo= 1L;
		Long idApartado = -1L;
		TcManticApartadosBitacoraDto bitacora= null;
		try {
			this.ventaFinalizada.getDetailApartado().setIdVenta(this.ventaFinalizada.getTicketVenta().getIdVenta());
			consecutivo= toSiguienteApartado(sesion);
			this.ventaFinalizada.getDetailApartado().setConsecutivo(Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
			this.ventaFinalizada.getDetailApartado().setOrden(consecutivo);
			this.ventaFinalizada.getDetailApartado().setEjercicio(Long.valueOf(Fecha.getAnioActual()));
			this.ventaFinalizada.getDetailApartado().setImporte(this.ventaFinalizada.getTotales().getTotales().getTotal());
			this.ventaFinalizada.getDetailApartado().setAbonado(this.ventaFinalizada.getTotales().getPago());
			this.ventaFinalizada.getDetailApartado().setSaldo(this.ventaFinalizada.getTotales().getTotales().getTotal() - this.ventaFinalizada.getTotales().getPago());
			this.ventaFinalizada.getDetailApartado().setIdApartadoEstatus(1L);
			this.ventaFinalizada.getDetailApartado().setIdUsuario(JsfBase.getIdUsuario());
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
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			setMessageError("Error al registrar el apartado.");
		} // 
		return regresar;
	} // registrarApartado			
	
	private boolean verificarCierreCaja(Session sesion) throws Exception{
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
	
	private boolean alterarCierreCaja(Session sesion, Long idTipoMedioPago) throws Exception{
		boolean regresar               = true;
		TcManticCajasDto caja          = null;
		Double limiteCaja              = 0D;		
		try {
			caja= (TcManticCajasDto) DaoFactory.getInstance().findById(sesion, TcManticCajasDto.class, this.ventaFinalizada.getIdCaja());
			limiteCaja= caja.getLimite();
			toCierreActivo(sesion, idTipoMedioPago);
			if(idTipoMedioPago.equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){				
				if(this.isNuevoCierre){
					if(limiteCaja< this.cierreCaja)
						regresar= registraAlertaRetiro(sesion, this.idCierreVigente, limiteCaja-this.cierreCaja);				
				}	// if		
				else{
					this.cierreCaja= toAcumuladoCierreActivo(sesion, this.idCierreVigente, idTipoMedioPago);
					if(limiteCaja< this.cierreCaja)
						regresar= registraAlertaRetiro(sesion, this.idCierreVigente, limiteCaja-this.cierreCaja);
				} // else
			}
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // alterarCierreCaja
	
	private boolean registraAlertaRetiro(Session sesion, Long idCierre, Double importe) throws Exception{
		boolean regresar= true;
		TcManticCierresAlertasDto alerta= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cierre="+idCierre);
			alerta= (TcManticCierresAlertasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresAlertasDto.class, "TcManticCierresAlertasDto", "row", params);
			if(!(alerta!= null && alerta.isValid())){
				alerta= new TcManticCierresAlertasDto();
				alerta.setIdCierre(idCierre);
				alerta.setIdNotifica(1L);
				alerta.setIdUsuario(JsfBase.getIdUsuario());
				alerta.setImporte(importe<= 0D ? 0D : importe);
				alerta.setMensaje("El total de caja a sobrepasado el limite permitido, favor de realizar un retiro.");
				regresar= DaoFactory.getInstance().insert(sesion, alerta)>= 1L;
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registraAlertaRetiro
	
	private Double toAcumuladoCierreActivo(Session sesion, Long idCierre, Long idTipoMedioPago) throws Exception{
		Double regresar                          = 0D;
		TcManticCierresCajasDto acumulaCierreCaja= null;
		Map<String, Object>params                = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cierre="+idCierre+" and id_tipo_medio_pago=" + idTipoMedioPago);
			acumulaCierreCaja= (TcManticCierresCajasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresCajasDto.class, "TcManticCierresCajasDto", "row", params);
			regresar= acumulaCierreCaja.getAcumulado();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toAcumuladoCierreActivo	
	
	private void toCierreActivo(Session sesion, Long idTipoMedioPago) throws Exception{
		Map<String, Object>params         = null;
		TcManticCierresCajasDto cierreCaja= null;
		try {
			params= new HashMap<>();
			params.put("idCierre", this.idCierreVigente);
			params.put("medioPago", idTipoMedioPago);
			cierreCaja= (TcManticCierresCajasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresCajasDto.class, "TcManticCierresCajasDto", "cajaMedioPago", params);
			cierreCaja.setAcumulado(cierreCaja.getAcumulado() + this.ventaFinalizada.getTotales().getPago());
			cierreCaja.setSaldo(cierreCaja.getDisponible() + cierreCaja.getAcumulado());
			DaoFactory.getInstance().update(sesion, cierreCaja);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toCierreActivo
	
	private TcManticCierresDto toCierreNuevo(Session sesion) throws Exception{
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
			cierreNuevo= new Cierre(this.ventaFinalizada.getIdCaja(), 0D, registro, new ArrayList<>(), new ArrayList<>());				
			if(cierreNuevo.toNewCierreCaja(sesion)){
				this.isNuevoCierre= true;				
				this.cierreCaja= 0D;
				regresar= cierreNuevo.getCierre();
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCierreNuevo	
	
	private Long toSiguienteApartado(Session sesion) {
		Long regresar             = 1L;
		Map<String, Object> params=null;
		Entity siguiente          = null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());			
			siguiente= (Entity) DaoFactory.getInstance().toEntity(sesion, "TcManticApartadosDto", "siguiente", params);
			if(siguiente!= null)
			  regresar= siguiente.get("siguiente")!= null ? siguiente.toLong("siguiente") : 1L;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}	// toSiguienteApartado
	
	private boolean pagarVenta(Session sesion, Long idEstatusVenta) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		Long consecutivo          = -1L;
		boolean validacionEstatus = false;
		try {									
			validacionEstatus= !idEstatusVenta.equals(EEstatusVentas.APARTADO.getIdEstatusVenta());
			consecutivo= toSiguiente(sesion);			
			getOrden().setCticket(consecutivo);			
			getOrden().setTicket(Fecha.getAnioActual() + Cadena.rellenar(consecutivo.toString(), 5, '0', true));
			getOrden().setIdVentaEstatus(idEstatusVenta);			
			getOrden().setIdFactura(this.ventaFinalizada.isFacturar() && validacionEstatus ? SI : NO);
			getOrden().setIdCredito(this.ventaFinalizada.isCredito() && validacionEstatus ? SI : NO);
			if(this.ventaFinalizada.isFacturar() && validacionEstatus){				
				this.clienteDeault= getOrden().getIdCliente().equals(toClienteDefault(sesion));
				if(this.clienteDeault){
					this.ventaFinalizada.getCorreosContacto().add(this.ventaFinalizada.getCelular());
					this.ventaFinalizada.getCorreosContacto().add(this.ventaFinalizada.getTelefono());
					this.ventaFinalizada.getDomicilio().setIdTipoDomicilio(ETiposDomicilios.FISCAL.getKey());
					this.ventaFinalizada.getCliente().setIdUsoCfdi(getOrden().getIdUsoCfdi());
					setClienteVenta(new ClienteVenta(this.ventaFinalizada.getCliente(), this.ventaFinalizada.getDomicilio(), null));
					procesarCliente(sesion);
					getOrden().setIdCliente(getIdClienteNuevo());
				} // if
				else
					registraClientesTipoContacto(sesion, getOrden().getIdCliente());				
			} // if						
			if(DaoFactory.getInstance().update(sesion, getOrden())>= 1L)
				regresar= registraBitacora(sesion, getOrden().getIdVenta(), idEstatusVenta, "La venta ha sido finalizada.");				
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
			setMessageError("Error al registrar el pago de la venta.");
		} // finally			
		return regresar;
	} // pagarVenta
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", getOrden().getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "siguienteTicket", params, "siguiente");
			if(next.getData()!= null)
				regresar= next.toLong();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente
	
	@Override
	public boolean registraClientesTipoContacto(Session sesion, Long idCliente) throws Exception {
		TrManticClienteTipoContactoDto dto = null;
    ESql sqlAccion  = null;
    int count       = 0;
    boolean validate= false;
    boolean regresar= false;
    try {
      for (ClienteTipoContacto clienteTipoContacto : this.ventaFinalizada.getCorreosContacto()) {
				if(clienteTipoContacto.getValor()!= null && !Cadena.isVacio(clienteTipoContacto.getValor())){
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
				} // if
				else
					validate= true;
        if (validate) 
          count++;        
      } // for		
      regresar = count == this.ventaFinalizada.getCorreosContacto().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      setMessageError("Error al registrar los tipos de contacto, verifique que no haya duplicados");
    } // finally
    return regresar;
	} // registrarContactosCliente
	
	private boolean registrarFactura(Session sesion) throws Exception{
		boolean regresar           = false;
		TcManticFacturasDto factura= null;
		try {			
			factura= new TcManticFacturasDto();
			factura.setFechaEmision(new Date(Calendar.getInstance().getTimeInMillis()));
			factura.setIdVenta(getOrden().getIdVenta());
			factura.setIdUsuario(JsfBase.getIdUsuario());
			factura.setIntentos(0L);
			regresar= DaoFactory.getInstance().insert(sesion, factura)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
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
			pagos= loadPagos(sesion);
			for(TrManticVentaMedioPagoDto pago: pagos){
				if(DaoFactory.getInstance().insert(sesion, pago)>= 1L)
					alterarCierreCaja(sesion, pago.getIdTipoMedioPago());
					count++;
			} // for
			regresar= count== pagos.size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			setMessageError("Error al registrar los pagos.");
		} // finally
		return regresar;
	} // registrarPagos
	
	private List<TrManticVentaMedioPagoDto> loadPagos(Session sesion) throws Exception{
		List<TrManticVentaMedioPagoDto> regresar= null;
		TrManticVentaMedioPagoDto pago          = null;
		try {
			regresar= new ArrayList<>();
			pago= toPagoEfectivo();
			if(pago!= null)
				regresar.add(pago);
			pago= toPagoTDebito();
			if(pago!= null)
				regresar.add(pago);
			pago= toPagoTCredito();
			if(pago!= null)
				regresar.add(pago);
			pago= toPagoTransferencia();
			if(pago!= null)
				regresar.add(pago);
			pago= toPagoCheque();
			if(pago!= null)
				regresar.add(pago);
			pago= toPagoCredito(sesion);
			if(pago!= null)
				regresar.add(pago);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadPagos
	
	private TrManticVentaMedioPagoDto toPagoEfectivo(){
		TrManticVentaMedioPagoDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getEfectivo() > 0D){
				regresar= new TrManticVentaMedioPagoDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdVenta(getOrden().getIdVenta());
				regresar.setImporte(this.ventaFinalizada.getTotales().getEfectivo());	
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoEfectivo
	
	private TrManticVentaMedioPagoDto toPagoTDebito(){
		TrManticVentaMedioPagoDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getDebito()> 0D){
				regresar= new TrManticVentaMedioPagoDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.TARJETA_DEBITO.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdVenta(getOrden().getIdVenta());
				regresar.setImporte(this.ventaFinalizada.getTotales().getDebito());				
				regresar.setIdBanco(this.ventaFinalizada.getTotales().getBancoDebito().getKey());
				regresar.setReferencia(this.ventaFinalizada.getTotales().getReferenciaDebito());	
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoTDebito
	
	private TrManticVentaMedioPagoDto toPagoTCredito(){
		TrManticVentaMedioPagoDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getCredito()> 0D){
				regresar= new TrManticVentaMedioPagoDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.TARJETA_CREDITO.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdVenta(getOrden().getIdVenta());
				regresar.setImporte(this.ventaFinalizada.getTotales().getCredito());				
				regresar.setIdBanco(this.ventaFinalizada.getTotales().getBancoCredito().getKey());
				regresar.setReferencia(this.ventaFinalizada.getTotales().getReferenciaCredito());	
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoTCredito
	
	private TrManticVentaMedioPagoDto toPagoTransferencia(){
		TrManticVentaMedioPagoDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getTransferencia()> 0D){
				regresar= new TrManticVentaMedioPagoDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.TRANSFERENCIA.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdVenta(getOrden().getIdVenta());
				regresar.setImporte(this.ventaFinalizada.getTotales().getTransferencia());				
				regresar.setIdBanco(this.ventaFinalizada.getTotales().getBancoTransferencia().getKey());
				regresar.setReferencia(this.ventaFinalizada.getTotales().getReferenciaTransferencia());			
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoTransferencia
	
	private TrManticVentaMedioPagoDto toPagoCredito(Session sesion) throws Exception{
		TrManticVentaMedioPagoDto regresar= null;
		Double totalCredito               = 0D;
		try {
			if(this.ventaFinalizada.isCredito()){
				totalCredito= this.ventaFinalizada.getTotales().getTotales().getTotal() - (this.ventaFinalizada.getTotales().getPago() - this.ventaFinalizada.getTotales().getCambio());
				if(totalCredito > 0D){
					regresar= new TrManticVentaMedioPagoDto();
					regresar.setIdTipoMedioPago(ETipoMediosPago.POR_DEFINIR.getIdTipoMedioPago());
					regresar.setIdUsuario(JsfBase.getIdUsuario());
					regresar.setIdVenta(getOrden().getIdVenta());
					regresar.setImporte(totalCredito);	
					regresar.setIdCierre(this.idCierreVigente);
					registrarDeuda(sesion, totalCredito);					
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoCredito
	
	private TrManticVentaMedioPagoDto toPagoCheque(){
		TrManticVentaMedioPagoDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getCheque()> 0D){
				regresar= new TrManticVentaMedioPagoDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.CHEQUE.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdVenta(getOrden().getIdVenta());
				regresar.setImporte(this.ventaFinalizada.getTotales().getCheque());				
				regresar.setIdBanco(this.ventaFinalizada.getTotales().getBancoCheque().getKey());
				regresar.setReferencia(this.ventaFinalizada.getTotales().getReferenciaCheque());		
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoCheque
	
	private boolean registrarPagosApartado(Session sesion, Long idApartado) throws Exception{
		List<TcManticApartadosPagosDto> pagos= null;
		boolean regresar= false;		
		int count       = 0;
		try {
			pagos= loadPagosApartado(sesion, idApartado);
			for(TcManticApartadosPagosDto pago: pagos){
				if(DaoFactory.getInstance().insert(sesion, pago)>= 1L)
					count++;
			} // for
			regresar= count== pagos.size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			setMessageError("Error al registrar los pagos.");
		} // finally
		return regresar;
	} // registrarPagos
	
	private List<TcManticApartadosPagosDto> loadPagosApartado(Session sesion, Long idApartado) throws Exception{
		List<TcManticApartadosPagosDto> regresar= null;
		TcManticApartadosPagosDto pago          = null;
		try {
			regresar= new ArrayList<>();
			pago= toPagoEfectivoApartado(idApartado);
			if(pago!= null)
				regresar.add(pago);
			pago= toPagoTDebitoApartado(idApartado);
			if(pago!= null)
				regresar.add(pago);
			pago= toPagoTCreditoApartado(idApartado);
			if(pago!= null)
				regresar.add(pago);
			pago= toPagoTransferenciaApartado(idApartado);
			if(pago!= null)
				regresar.add(pago);
			pago= toPagoChequeApartado(idApartado);
			if(pago!= null)
				regresar.add(pago);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadPagos
	
	private TcManticApartadosPagosDto toPagoEfectivoApartado(Long idApartado){
		TcManticApartadosPagosDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getEfectivo() > 0D){
				regresar= new TcManticApartadosPagosDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdApartado(idApartado);
				regresar.setPago(this.ventaFinalizada.getTotales().getEfectivo());
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoEfectivo
	
	private TcManticApartadosPagosDto toPagoTDebitoApartado(Long idApartado){
		TcManticApartadosPagosDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getDebito()> 0D){
				regresar= new TcManticApartadosPagosDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.TARJETA_DEBITO.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdApartado(idApartado);
				regresar.setPago(this.ventaFinalizada.getTotales().getDebito());	
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoTDebito
	
	private TcManticApartadosPagosDto toPagoTCreditoApartado(Long idApartado){
		TcManticApartadosPagosDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getCredito()> 0D){
				regresar= new TcManticApartadosPagosDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.TARJETA_CREDITO.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdApartado(idApartado);
				regresar.setPago(this.ventaFinalizada.getTotales().getCredito());				
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoTCredito
	
	private TcManticApartadosPagosDto toPagoTransferenciaApartado(Long idApartado){
		TcManticApartadosPagosDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getTransferencia()> 0D){
				regresar= new TcManticApartadosPagosDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.TRANSFERENCIA.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdApartado(idApartado);
				regresar.setPago(this.ventaFinalizada.getTotales().getTransferencia());			
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoTransferencia
	
	private TcManticApartadosPagosDto toPagoChequeApartado(Long idApartado){
		TcManticApartadosPagosDto regresar= null;
		try {
			if(this.ventaFinalizada.getTotales().getCheque()> 0D){
				regresar= new TcManticApartadosPagosDto();
				regresar.setIdTipoMedioPago(ETipoMediosPago.CHEQUE.getIdTipoMedioPago());
				regresar.setIdUsuario(JsfBase.getIdUsuario());
				regresar.setIdApartado(idApartado);
				regresar.setPago(this.ventaFinalizada.getTotales().getCheque());					
				regresar.setIdCierre(this.idCierreVigente);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoCheque
	
	private void registrarDeuda(Session sesion, Double importe) throws Exception{
		TcManticClientesDeudasDto deuda= null;
		try {
			deuda= new TcManticClientesDeudasDto();
			deuda.setIdVenta(getOrden().getIdVenta());
			deuda.setIdCliente(getOrden().getIdCliente());
			deuda.setIdUsuario(JsfBase.getIdUsuario());
			deuda.setImporte(importe);
			deuda.setSaldo(importe);
			deuda.setLimite(toLimiteCredito(sesion));
			deuda.setIdClienteEstatus(1L);
			DaoFactory.getInstance().insert(sesion, deuda);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // registrarDeuda
	
	private Date toLimiteCredito(Session sesion) throws Exception{
		Date regresar              = null;
		TcManticClientesDto cliente= null;
		Long addDias               = 15L;
		Calendar calendar          = null;
		try {
			if(!this.ventaFinalizada.isFacturar() || (this.ventaFinalizada.isFacturar() && !this.clienteDeault)){
				cliente= (TcManticClientesDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, getOrden().getIdCliente());
				addDias= cliente.getPlazoDias();
			} // if
			calendar= Calendar.getInstance();
			regresar= new Date(calendar.getTimeInMillis());			
			calendar.setTime(regresar);
			calendar.add(Calendar.DAY_OF_YEAR, addDias.intValue());
			regresar= new Date(calendar.getTimeInMillis());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toLimiteCredito
	
	private boolean alterarStockArticulos(Session sesion) throws Exception{
		TcManticAlmacenesArticulosDto almacenArticulo= null;
		TcManticArticulosDto articuloVenta           = null;		
		Map<String, Object>params                    = null;
		boolean regresar                             = false;
		int count                                    = 0; 
		try {			
			params= new HashMap<>();
			for(Articulo articulo: this.ventaFinalizada.getArticulos()){
				params.put(Constantes.SQL_CONDICION, "id_articulo=".concat(articulo.getIdArticulo().toString()));
				almacenArticulo= (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().toEntity(sesion, TcManticAlmacenesArticulosDto.class, "TcManticAlmacenesArticulosDto", "row", params);
				if(almacenArticulo!= null){
					almacenArticulo.setStock(almacenArticulo.getStock() - articulo.getCantidad());
					regresar= DaoFactory.getInstance().update(sesion, almacenArticulo)>= 1L;
				} // if
				else
					regresar= generarAlmacenArticulo(sesion, articulo.getIdArticulo(), articulo.getCantidad());
				if(regresar){
					articuloVenta= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, articulo.getIdArticulo());
					articuloVenta.setStock(articuloVenta.getStock() - articulo.getCantidad());
					if(DaoFactory.getInstance().update(sesion, articuloVenta)>= 1L)
						regresar= actualizaInventario(sesion, articulo.getIdArticulo(), articulo.getCantidad());
				} // if
				if(regresar)
					count++;
			} // for		
			regresar= count== this.ventaFinalizada.getArticulos().size();			
		} // try
		catch (Exception e) {			
			throw e;		
		} // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // alterarStockArticulos
	
	private boolean generarAlmacenArticulo(Session sesion, Long idArticulo, Double cantidad) throws Exception{
		boolean regresar                             = false;
		TcManticAlmacenesArticulosDto almacenArticulo= null;
		try {
			almacenArticulo= new TcManticAlmacenesArticulosDto();
			almacenArticulo.setIdAlmacen(getOrden().getIdAlmacen());
			almacenArticulo.setIdArticulo(idArticulo);
			almacenArticulo.setIdUsuario(JsfBase.getIdUsuario());
			almacenArticulo.setMaximo(0L);
			almacenArticulo.setMinimo(0L);
			almacenArticulo.setStock(0 - cantidad);
			almacenArticulo.setIdAlmacenUbicacion(toIdAlmacenUbicacion(sesion));
			regresar= DaoFactory.getInstance().insert(sesion, almacenArticulo)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
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
				ubicacion.setPiso(GENERAL);
				ubicacion.setDescripcion(GENERAL);
				ubicacion.setIdUsuario(JsfBase.getIdUsuario());				
				ubicacion.setIdAlmacen(getOrden().getIdAlmacen());
				regresar= DaoFactory.getInstance().insert(sesion, ubicacion);
			} // 
		} // try
		catch (Exception e) {			
			throw e;
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
				inventario.setSalidas(0 - cantidad);
				inventario.setStock(0 - cantidad);
				regresar= DaoFactory.getInstance().insert(sesion, inventario)>= 1L;
			} // else				
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizaInventario
}

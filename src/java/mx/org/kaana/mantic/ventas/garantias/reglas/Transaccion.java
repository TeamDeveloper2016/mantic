package mx.org.kaana.mantic.ventas.garantias.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresAlertasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.enums.EEstatusGarantias;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Cierre;
import org.apache.log4j.Logger;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private static final Logger LOG          = Logger.getLogger(Transaccion.class);
	private static final String GENERAL      = "GENERAL";
	private static final String CIERRE_ACTIVO= "1,2";
	private static final Long SI             = 1L;
	private static final Long NO             = 2L;
	private VentaFinalizada ventaFinalizada;
	private TcManticGarantiasDto garantia;
	private String justificacion;
	private String messageError;
	private IBaseDto dto;	
	private boolean isNuevoCierre;
	private Double cierreCaja;
	private Long idCierreVigente;
	
	public Transaccion(IBaseDto dto) {
		this(null, dto);
	} // Transaccion	
	
	public Transaccion(VentaFinalizada ventaFinalizada) {
		this(ventaFinalizada, null);
	} // Transaccion

	public Transaccion(VentaFinalizada ventaFinalizada, IBaseDto dto) {
		this.ventaFinalizada = ventaFinalizada;		
		this.dto= dto;
	}	// Transaccion	

	public Transaccion(TcManticGarantiasDto garantia, String justificacion) {
		this.garantia     = garantia;
		this.justificacion= justificacion;
	} // Transaccion
	
	public Long getIdCierreVigente() {
		return idCierreVigente;
	}	

	public String getMessageError() {
		return messageError;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar      = false;
		this.isNuevoCierre    = false;
		this.cierreCaja       = 0D;		
		Long idEstatusGarantia= 1L;
		try {						
			switch(accion) {					
				case REPROCESAR:				
					regresar= procesarVenta(sesion);
					break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;		
				case ELIMINAR:
					idEstatusGarantia= EEstatusGarantias.ELIMINADA.getIdEstatusGarantia();
					this.garantia= (TcManticGarantiasDto) DaoFactory.getInstance().findById(sesion, TcManticGarantiasDto.class, this.garantia.getIdGarantia());
					this.garantia.setIdGarantiaEstatus(idEstatusGarantia);					
					if(DaoFactory.getInstance().update(sesion, this.garantia)>= 1L)
						regresar= registraBitacora(sesion, this.garantia.getIdGarantia(), idEstatusGarantia, this.justificacion);					
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
	
	private boolean procesarVenta(Session sesion) throws Exception{
		boolean regresar= false;
		try {
			regresar= pagarVenta(sesion, this.ventaFinalizada.getApartado() ? EEstatusVentas.APARTADO.getIdEstatusVenta() : (this.ventaFinalizada.isCredito() ? EEstatusVentas.CREDITO.getIdEstatusVenta() : EEstatusVentas.PAGADA.getIdEstatusVenta()));
			if(regresar){				
				if(verificarCierreCaja(sesion)){
					if(registrarPagos(sesion))					
						regresar= alterarStockArticulos(sesion);
				} // if				
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarVenta				
	
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
	
	public boolean alterarCierreCaja(Session sesion, Long idTipoMedioPago) throws Exception{
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
	
	private boolean pagarVenta(Session sesion, Long idEstatusGarantia) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		Long consecutivo          = -1L;
		try {									
			consecutivo= toSiguiente(sesion);			
			this.garantia.setConsecutivo(consecutivo);			
			this.garantia.setOrden(consecutivo);			
			this.garantia.setFolio(Fecha.getAnioActual() + Cadena.rellenar(consecutivo.toString(), 5, '0', true));
			this.garantia.setIdGarantiaEstatus(idEstatusGarantia);
			if(DaoFactory.getInstance().update(sesion, this.garantia)>= 1L){
				regresar= registraBitacora(sesion, this.garantia.getIdGarantia(), idEstatusGarantia, "La venta ha sido finalizada.");				
				//toFillArticulos(sesion, this.ventaFinalizada.getArticulos());
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
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", this.ventaFinalizada.getTicketVenta().getIdEmpresa());
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
	
	private boolean registrarPagos(Session sesion) throws Exception{
		boolean regresar= true;		
		try {			
			alterarCierreCaja(sesion, ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());				
		} // try 
		catch (Exception e) {			 
			throw e; 
		} // catch		
		finally {
			this.messageError= "Error al registrar los pagos.";
		} // finally
		return regresar; 
	} // registrarPagos
	
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
			almacenArticulo.setIdAlmacen(this.ventaFinalizada.getTicketVenta().getIdAlmacen());
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
			params.put("idAlmacen", this.ventaFinalizada.getTicketVenta().getIdAlmacen());
			ubicacion= (TcManticAlmacenesUbicacionesDto) DaoFactory.getInstance().toEntity(sesion, TcManticAlmacenesUbicacionesDto.class, "TcManticAlmacenesUbicacionesDto", "general", params);
			if(ubicacion!= null)
				regresar= ubicacion.getKey();
			else{
				ubicacion= new TcManticAlmacenesUbicacionesDto();
				ubicacion.setPiso(GENERAL);
				ubicacion.setDescripcion(GENERAL);
				ubicacion.setIdUsuario(JsfBase.getIdUsuario());				
				ubicacion.setIdAlmacen(this.ventaFinalizada.getTicketVenta().getIdAlmacen());
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
			params.put("idAlmacen", this.ventaFinalizada.getTicketVenta().getIdAlmacen());
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
				inventario.setIdAlmacen(this.ventaFinalizada.getTicketVenta().getIdAlmacen());
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
	
	protected boolean registraBitacora(Session sesion, Long idGarantia, Long idGarantiaEstatus, String justificacion) throws Exception{
		boolean regresar                     = false;
		TcManticGarantiasBitacoraDto bitacora= null;
		try {
			bitacora= new TcManticGarantiasBitacoraDto(idGarantia, this.garantia.getConsecutivo(),justificacion, JsfBase.getIdUsuario(), idGarantiaEstatus, -1L, this.garantia.getTotal());
			regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarBitacora
}

package mx.org.kaana.mantic.ventas.garantias.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TrManticGarantiaMedioPagoDto;
import mx.org.kaana.mantic.enums.EEstatusGarantias;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Cierre;
import mx.org.kaana.mantic.ventas.garantias.beans.Garantia;
import org.apache.log4j.Logger;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private static final Logger LOG          = Logger.getLogger(Transaccion.class);
	private static final String GENERAL      = "GENERAL";
	private static final String CIERRE_ACTIVO= "1,2";
	private Garantia garantia;
	private TcManticGarantiasDto garantiaDto;
	private String justificacion;
	private String messageError;
	private IBaseDto dto;	
	private boolean isNuevoCierre;
	private Double cierreCaja;
	private Long idCierreVigente;
	
	public Transaccion(IBaseDto dto) {
		this(null, dto);
	} // Transaccion	
	
	public Transaccion(Garantia garantia) {
		this(garantia, null);
	} // Transaccion

	public Transaccion(Garantia garantia, IBaseDto dto) {
		this.garantia= garantia;		
		this.dto= dto;
	}	// Transaccion	

	public Transaccion(TcManticGarantiasDto garantiaDto, String justificacion) {
		this.garantiaDto  = garantiaDto;
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
	
	private boolean procesarGarantia(Session sesion) throws Exception{
		boolean regresar= false;
		try {
			regresar= generarGarantia(sesion, EEstatusGarantias.ELABORADA.getIdEstatusGarantia());
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
			params.put("idEmpresa", this.garantia.getTicketVenta().getIdEmpresa());
			params.put("idCaja", this.garantia.getIdCaja());			
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
			caja= (TcManticCajasDto) DaoFactory.getInstance().findById(sesion, TcManticCajasDto.class, this.garantia.getIdCaja());
			limiteCaja= caja.getLimite();
			toCierreActivo(sesion, idTipoMedioPago);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // alterarCierreCaja	
	
	private void toCierreActivo(Session sesion, Long idTipoMedioPago) throws Exception{
		Map<String, Object>params         = null;
		TcManticCierresCajasDto cierreCaja= null;
		try {
			params= new HashMap<>();
			params.put("idCierre", this.idCierreVigente);
			params.put("medioPago", idTipoMedioPago);
			cierreCaja= (TcManticCierresCajasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresCajasDto.class, "TcManticCierresCajasDto", "cajaMedioPago", params);			
			cierreCaja.setAcumulado(cierreCaja.getAcumulado() - this.garantia.getTotales().getPago());			
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
			cierreNuevo= new Cierre(this.garantia.getIdCaja(), 0D, registro, new ArrayList<>(), new ArrayList<>());				
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
	
	private boolean generarGarantia(Session sesion, Long idEstatusGarantia) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;		
		try {							
			loadGarantia(sesion, idEstatusGarantia);			
			if(DaoFactory.getInstance().insert(sesion, this.garantiaDto)>= 1L){
				regresar= registraBitacora(sesion, this.garantiaDto.getIdGarantia(), idEstatusGarantia, "Se generó la garantía de forma correcta.");				
				toFillArticulos(sesion, this.garantia.getArticulos());
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
	
	private void loadGarantia(Session sesion, Long idEstatusGarantia) throws Exception{
		Long consecutivo= -1L;
		try {
			this.garantiaDto= new TcManticGarantiasDto();
			consecutivo= toSiguiente(sesion);			
			this.garantiaDto.setConsecutivo(consecutivo);			
			this.garantiaDto.setOrden(consecutivo);			
			this.garantiaDto.setIdGarantiaEstatus(idEstatusGarantia);
			this.garantiaDto.setIdVenta(this.garantia.getTicketVenta().getIdVenta());
			this.garantiaDto.setEjercicio(Long.valueOf(Fecha.getAnioActual()));
			this.garantiaDto.setDescuentos(this.garantia.getTicketVenta().getDescuentos());
			this.garantiaDto.setIdUsuario(JsfBase.getIdUsuario());
			this.garantiaDto.setImpuestos(this.garantia.getTicketVenta().getImpuestos());
			this.garantiaDto.setSubTotal(this.garantia.getTicketVenta().getSubTotal());
			this.garantiaDto.setTotal(this.garantia.getTicketVenta().getTotal());
			this.garantiaDto.setUtilidad(this.garantia.getTicketVenta().getUtilidad());			
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
	} // loadGarantia
		
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticGarantiasDto", "siguiente", params, "siguiente");
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
	
	protected void toFillArticulos(Session sesion, List<Articulo> detalleArt) throws Exception {		
		for (Articulo articulo: detalleArt) {
			TcManticGarantiasDetallesDto item= articulo.toGarantiaDetalle();
			item.setIdGarantia(this.garantiaDto.getIdGarantia());			
			if(item.getIdProveedor().equals(0L) || item.getIdProveedor().equals(-1L))
				item.setIdProveedor(null);
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticGarantiasDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
			else
				DaoFactory.getInstance().update(sesion, item);
		} // for
	} // toFillArticulos
	
	private boolean registrarPagos(Session sesion) throws Exception{
		boolean regresar                 = true;		
		TrManticGarantiaMedioPagoDto pago= null;
		try {						
			pago= new TrManticGarantiaMedioPagoDto();
			if(this.garantia.getPagoGarantia().getIdTipoPago().equals(ETipoMediosPago.TRANSFERENCIA.getIdTipoMedioPago())){
				pago.setIdBanco(this.garantia.getPagoGarantia().getIdBanco());
				pago.setReferencia(this.garantia.getPagoGarantia().getTransferencia());
			} // if
			pago.setIdCierre(this.idCierreVigente);
			pago.setIdGarantia(this.garantiaDto.getIdGarantia());
			pago.setIdTipoMedioPago(this.garantia.getPagoGarantia().getIdTipoPago());
			pago.setIdUsuario(JsfBase.getIdUsuario());
			pago.setImporte(this.garantia.getGarantia().getTotal());		
			DaoFactory.getInstance().insert(sesion, pago);
			alterarCierreCaja(sesion, this.garantia.getPagoGarantia().getIdTipoPago());				
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
			for(Articulo articulo: this.garantia.getArticulos()){
				params.put(Constantes.SQL_CONDICION, "id_articulo="+ articulo.getIdArticulo()+ " and id_almacen="+ this.garantia.getTicketVenta().getIdAlmacen());
				almacenArticulo= (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().toEntity(sesion, TcManticAlmacenesArticulosDto.class, "TcManticAlmacenesArticulosDto", "row", params);
				if(almacenArticulo!= null){
					almacenArticulo.setStock(almacenArticulo.getStock() + articulo.getCantidad());
					regresar= DaoFactory.getInstance().update(sesion, almacenArticulo)>= 1L;
				} // if
				else
					regresar= generarAlmacenArticulo(sesion, articulo.getIdArticulo(), articulo.getCantidad());
				if(regresar) {
					articuloVenta= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, articulo.getIdArticulo());
					articuloVenta.setStock(articuloVenta.getStock() + articulo.getCantidad());
					if(DaoFactory.getInstance().update(sesion, articuloVenta)>= 1L)
						regresar= actualizaInventario(sesion, articulo.getIdArticulo(), articulo.getCantidad());
				} // if
				if(regresar)
					count++;
			} // for		
			regresar= count== this.garantia.getArticulos().size();			
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
	
	private Long toIdAlmacenUbicacion(Session sesion) throws Exception{
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
	
	private boolean actualizaInventario(Session sesion, Long idArticulo, Double cantidad) throws Exception{
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
	
	protected boolean registraBitacora(Session sesion, Long idGarantia, Long idGarantiaEstatus, String justificacion) throws Exception{
		boolean regresar                     = false;
		TcManticGarantiasBitacoraDto bitacora= null;
		try {
			bitacora= new TcManticGarantiasBitacoraDto(idGarantia, this.garantiaDto.getConsecutivo(),justificacion, JsfBase.getIdUsuario(), idGarantiaEstatus, -1L, this.garantiaDto.getTotal());
			regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarBitacora
	
}

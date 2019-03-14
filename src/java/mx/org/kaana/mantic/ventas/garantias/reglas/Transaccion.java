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
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import mx.org.kaana.mantic.db.dto.TrManticGarantiaMedioPagoDto;
import mx.org.kaana.mantic.enums.EEstatusGarantias;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETiposGarantias;
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

	public Transaccion(DetalleGarantia detalleGarantia) {
		this.detalleGarantia= detalleGarantia;
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
	
	private boolean procesarGarantia(Session sesion) throws Exception {
		boolean regresar= false;
		try {
			for(Garantia newGarantia: this.detalleGarantia.getGarantias()){
				if(newGarantia.getArticulosGarantia().size()> 0){
					this.garantia= newGarantia;
					this.generarGarantia(sesion, this.garantia.getTipoGarantia().equals(ETiposGarantias.RECIBIDA) ? EEstatusGarantias.RECIBIDA.getIdEstatusGarantia() : EEstatusGarantias.TERMINADA.getIdEstatusGarantia());																
					if(this.verificarCierreCaja(sesion)){
						if(this.registrarPagos(sesion, this.garantia.getTicketVenta().getTotal()))					
							regresar= this.alterarStockArticulos(sesion, newGarantia.getArticulosGarantia());
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
	
	private void toCierreActivo(Session sesion, Long idTipoMedioPago) throws Exception {
		Map<String, Object>params         = null;
		TcManticCierresCajasDto cierreCaja= null;
		try {
			params= new HashMap<>();
			params.put("idCierre", this.idCierreVigente);
			params.put("medioPago", idTipoMedioPago);
			cierreCaja= (TcManticCierresCajasDto) DaoFactory.getInstance().toEntity(sesion, TcManticCierresCajasDto.class, "TcManticCierresCajasDto", "cajaMedioPago", params);			
			cierreCaja.setAcumulado(cierreCaja.getAcumulado()- this.garantia.getTotales().getPago());			
			cierreCaja.setSaldo(cierreCaja.getDisponible()+ cierreCaja.getAcumulado());
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
		Long consecutivo= 1L;
		try {
			this.garantiaDto= new TcManticGarantiasDto();
			consecutivo= toSiguiente(sesion);			
			this.garantiaDto.setConsecutivo(Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));			
			this.garantiaDto.setOrden(consecutivo);			
			this.garantiaDto.setIdGarantiaEstatus(idEstatusGarantia);			
			this.garantiaDto.setEjercicio(Long.valueOf(Fecha.getAnioActual()));			
			this.garantiaDto.setIdUsuario(JsfBase.getIdUsuario());
			this.garantiaDto.setIdVenta(this.garantia.getGarantia().getIdVenta());
			this.garantiaDto.setDescuentos(this.garantia.getTicketVenta().getDescuentos());
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
		boolean regresar                 = true;		
		TrManticGarantiaMedioPagoDto pago= null;
		try {						
			pago= new TrManticGarantiaMedioPagoDto();
			pago.setIdCierre(this.idCierreVigente);
			pago.setIdGarantia(this.garantiaDto.getIdGarantia());
			if(this.detalleGarantia.getPagoGarantia().getIdTipoPago().equals(ETipoMediosPago.TRANSFERENCIA.getIdTipoMedioPago())){
				pago.setIdBanco(this.detalleGarantia.getPagoGarantia().getIdBanco());
				pago.setReferencia(this.detalleGarantia.getPagoGarantia().getTransferencia());
			} // if						
			pago.setIdTipoMedioPago(this.detalleGarantia.getPagoGarantia().getIdTipoPago());
			pago.setIdUsuario(JsfBase.getIdUsuario());
			pago.setImporte(total);		
			DaoFactory.getInstance().insert(sesion, pago);
			toCierreActivo(sesion, this.detalleGarantia.getPagoGarantia().getIdTipoPago());				
		} // try 
		catch (Exception e) {			 
			throw e; 
		} // catch		
		finally {
			this.messageError= "Error al registrar los pagos.";
		} // finally
		return regresar; 
	} // registrarPagos	
	
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
				registrarMovimiento(sesion, this.garantia.getTicketVenta().getIdAlmacen(), articulo.getCantidad(), articulo.getIdArticulo(), stock);
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
	
	private void registrarMovimiento(Session sesion, Long idAlmacen, Double cantidad, Long idArticulo, Double stock) throws Exception{
		Double calculo= Numero.toRedondearSat(stock + cantidad) ;
		TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
			  this.garantiaDto.getConsecutivo(), // String consecutivo, 
				5L,                     // Long idTipoMovimiento, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				idAlmacen,              // Long idAlmacen, 
				-1L,                    // Long idMovimiento, 
				cantidad,               // Double cantidad, 
				idArticulo,             // Long idArticulo, 
				stock,                  // Double stock, 
				calculo,                // Double calculo
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
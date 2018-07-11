package mx.org.kaana.mantic.ventas.caja.reglas;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
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
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticVentaMedioPagoDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETiposDomicilios;
import mx.org.kaana.mantic.ventas.beans.ClienteVenta;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import org.apache.log4j.Logger;
import org.hibernate.Session;

public class Transaccion extends mx.org.kaana.mantic.ventas.reglas.Transaccion{

	private static final Logger LOG    = Logger.getLogger(Transaccion.class);
	private static final String GENERAL= "GENERAL";
	private static final Long SI       = 1L;
	private static final Long NO       = 2L;
	private VentaFinalizada ventaFinalizada;
	private IBaseDto dto;	
	private boolean clienteDeault;
	
	public Transaccion(IBaseDto dto) {
		this(null, dto);
	} // Transaccion
	
	public Transaccion(VentaFinalizada ventaFinalizada) {
		this(ventaFinalizada, null);
	} // Transaccion

	public Transaccion(VentaFinalizada ventaFinalizada, IBaseDto dto) {
		super(ventaFinalizada.getTicketVenta());
		this.ventaFinalizada = ventaFinalizada;
		this.dto             = dto;
	}	// Transaccion
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {						
			switch(accion) {					
				case REPROCESAR:				
					regresar= procesarVenta(sesion);
					break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception(getMessageError());
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(getMessageError().concat("\n\n")+ e.getMessage());
		} // catch		
		if(this.ventaFinalizada.getTicketVenta()!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.ventaFinalizada.getTicketVenta().getConsecutivo());
		return regresar;
	} // ejecutar
	
	private boolean procesarVenta(Session sesion) throws Exception{
		boolean regresar= false;
		try {
			regresar= pagarVenta(sesion, this.ventaFinalizada.isCredito() ? EEstatusVentas.CREDITO.getIdEstatusVenta() : EEstatusVentas.PAGADA.getIdEstatusVenta());
			if(regresar){
				if(this.ventaFinalizada.isFacturar())
					regresar= registrarFactura(sesion);
				if(registrarPagos(sesion))
					regresar= alterarStockArticulos(sesion);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarVenta
	
	private boolean pagarVenta(Session sesion, Long idEstatusVenta) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		try {						
			getOrden().setIdVentaEstatus(idEstatusVenta);			
			getOrden().setIdFactura(this.ventaFinalizada.isFacturar() ? SI : NO);
			getOrden().setIdCredito(this.ventaFinalizada.isCredito() ? SI : NO);
			if(this.ventaFinalizada.isFacturar()){				
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

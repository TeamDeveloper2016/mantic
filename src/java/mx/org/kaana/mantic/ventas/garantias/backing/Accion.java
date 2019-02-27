package mx.org.kaana.mantic.ventas.garantias.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.garantias.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETiposGarantias;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.comun.IBaseVenta;
import mx.org.kaana.mantic.ventas.garantias.beans.DetalleGarantia;
import mx.org.kaana.mantic.ventas.garantias.beans.Garantia;
import mx.org.kaana.mantic.ventas.garantias.beans.PagoGarantia;
import mx.org.kaana.mantic.ventas.garantias.reglas.AdminGarantia;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value= "manticVentasGarantiasAccion")
@ViewScoped
public class Accion extends IBaseVenta implements Serializable {

  private static final long serialVersionUID  = 327393488565639367L;
	private static final String CLAVE_VENTA_GRAL= "VENTA";	
	private TicketVenta ticketOriginal;
	private StreamedContent image;
	
	public Accion() {
		super("menudeo");
	}

	public StreamedContent getImage() {
		return image;
	}
	
	@PostConstruct
  @Override
  protected void init() {	
		EAccion accion= null;
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null ? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null ? -1L: JsfBase.getFlashAttribute("idVenta"));
      this.attrs.put("idGarantia", JsfBase.getFlashAttribute("idGarantia")== null ? -1L: JsfBase.getFlashAttribute("idGarantia"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null ? null : JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("tipoPago", 1L);
			this.attrs.put("buscaPorCodigo", false);
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("facturarVenta", false);
			this.attrs.put("pagarVenta", false);
			this.attrs.put("cobroVenta", false);
			this.attrs.put("clienteAsignado", false);
			this.attrs.put("tabIndex", 0);
			this.attrs.put("fecha", new Date(Calendar.getInstance().getTimeInMillis()));
			this.attrs.put("contador", 0L);
			this.attrs.put("creditoVenta", false);
			this.attrs.put("activeApartado", false);
			this.attrs.put("disabledFacturar", false);			
			this.attrs.put("apartado", false);			
			this.attrs.put("isEfectivo", true);			
			this.image= LoadImages.getImage(-1L);
			if(JsfBase.isAdminEncuestaOrAdmin())
				loadSucursales();						
			accion= (EAccion) this.attrs.get("accion");
			if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))
				doLoad();
			loadCajas();
			doLoadTicketAbiertos();						
			if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))
				doAsignaTicketAbierto();
			loadBancos();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));			
      switch (eaccion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminGarantia(new TicketVenta(-1L), eaccion));
          break;
        case MODIFICAR:			
        case CONSULTAR:			
          this.setAdminOrden(new AdminGarantia((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "VistaTcManticGarantiasArticulosDto", "garantia", this.attrs), eaccion, Long.valueOf(this.attrs.get("idGarantia").toString())));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
    			this.attrs.put("idEmpresa", ((TicketVenta)this.getAdminOrden().getOrden()).getIdEmpresa());
          break;
      } // switch
			this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			loadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad
	
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {				
			transaccion= new Transaccion(loadDetalleGarantia());			
			if (transaccion.ejecutar(EAccion.REPROCESAR)) {
				regresar = this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR) : null;
				JsfBase.addMessage("Se finalizo la garantia.", ETipoMensaje.INFORMACION);
				this.setAdminOrden(new AdminGarantia(new TicketVenta()));
				this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
				init();
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la garantia.", ETipoMensaje.ERROR);			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion  

	private DetalleGarantia loadDetalleGarantia() throws Exception{
		DetalleGarantia regresar = null;
		List<Garantia> garantias = null;		
		Garantia garantia        = null;		
		TicketVenta ticketVenta  = null;		
		PagoGarantia pagoGarantia= null;
		List<Articulo>articulos  = null;
		try {
			regresar= new DetalleGarantia();
			garantias= new ArrayList<>();
			articulos= this.getAdminOrden().getArticulos();
			for(ETiposGarantias tipoGarantia: ETiposGarantias.values()){				
				this.setAdminOrden(new AdminGarantia(new TicketVenta(), true));
				this.getAdminOrden().setArticulos(articulos);
				this.getAdminOrden().toCalculateGarantia(tipoGarantia.equals(ETiposGarantias.RECIBIDA));
				ticketVenta= (TicketVenta)this.getAdminOrden().getOrden();
				ticketVenta.setTotal(this.getAdminOrden().getTotales().getTotal());
				ticketVenta.setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
				ticketVenta.setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
				ticketVenta.setImpuestos(this.getAdminOrden().getTotales().getIva());
				ticketVenta.setUtilidad(this.getAdminOrden().getTotales().getUtilidad());			
				garantia= new Garantia();
				garantia.setTicketVenta(ticketVenta);							
				garantia.setArticulosGarantia(tipoGarantia.equals(ETiposGarantias.RECIBIDA) ? ((AdminGarantia)this.getAdminOrden()).getArticulosRecibida() : ((AdminGarantia)this.getAdminOrden()).getArticulosTerminada());				
				garantia.setGarantia(this.ticketOriginal);							
				garantias.add(garantia);
			} // for			
			pagoGarantia= new PagoGarantia();
			if(Long.valueOf(this.attrs.get("tipoPago").toString()).equals(1L))
				pagoGarantia.setIdTipoPago(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());		
			else{
				pagoGarantia.setIdTipoPago(ETipoMediosPago.TRANSFERENCIA.getIdTipoMedioPago());
				pagoGarantia.setTransferencia(this.attrs.get("referencia").toString());
				pagoGarantia.setIdBanco(Long.valueOf(this.attrs.get("banco").toString()));
			} // else
			regresar.setGarantias(garantias);
			regresar.setPagoGarantia(pagoGarantia);
			regresar.setTotales((Pago) this.attrs.get("pago"));			
			regresar.setIdCaja(Long.valueOf(this.attrs.get("caja").toString()));			
			regresar.getTicketVenta().setIdEmpresa(Long.valueOf(this.attrs.get("idEmpresa").toString()));			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadVentaFinalizada
	
	private void loadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) 
				((TicketVenta)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
      columns.remove(0);
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("clientes", UIEntity.build("TcManticClientesDto", "sucursales", params, columns));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // loadCatalog
	
	@Override
	public void doAsignaCliente(SelectEvent event){
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientes         = null;
		List<UISelectEntity> clientesSeleccion= null;
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
			doReCalculatePreciosArticulos(seleccion.getKey());			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	@Override
	public void doLoadTicketAbiertos() {
		List<UISelectEntity> ticketsAbiertos= null;
		Map<String, Object>params           = null;
		List<Columna> campos                = null;
		EAccion accion                      = null;
		try {
			loadCajas();
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			campos= new ArrayList<>();
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, toCondicion());
			ticketsAbiertos= UIEntity.build("VistaVentasDto", "lazy", params, campos, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("ticketsAbiertos", ticketsAbiertos);			
			accion= (EAccion) this.attrs.get("accion");
			if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR)){
				this.attrs.put("ticketAbierto", ticketsAbiertos.get(0));
				this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			} // if
			else{
				this.attrs.put("ticketAbierto", new UISelectEntity("-1"));
				this.setAdminOrden(new AdminGarantia(new TicketVenta()));
				this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			} // else
			this.attrs.put("pagarVenta", false);
			this.attrs.put("facturarVenta", false);
			this.attrs.put("cobroVenta", false);
			this.attrs.put("clienteAsignado", false);
			this.attrs.put("tabIndex", 0);		
			this.attrs.put("registroCliente", new TcManticClientesDto());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doLoadTicketAbiertos
	
	private String toCondicion() {
		StringBuilder regresar= null;
		Date fecha            = null;
		EAccion accion        = null;
		try {
			fecha= (Date) this.attrs.get("fecha");
			regresar= new StringBuilder();													
			accion= (EAccion) this.attrs.get("accion");
			if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR)){
				regresar.append(" tc_mantic_ventas.id_venta=");
				regresar.append(this.getAdminOrden().getOrden().getKey());
			} // if
			else {
				regresar.append(" DATE_FORMAT(tc_mantic_ventas.registro, '%Y%m%d')=".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, fecha)));
				regresar.append(" and tc_mantic_ventas.id_venta_estatus=");
				regresar.append(EEstatusVentas.PAGADA.getIdEstatusVenta());						
			} // else
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicion
	
	@Override
	public void doAsignaTicketAbierto(){
		Map<String, Object>params           = null;
		UISelectEntity ticketAbierto        = null;
		UISelectEntity ticketAbiertoPivote  = null;
		List<UISelectEntity> ticketsAbiertos= null;
		EAccion accion                      = null;
		try {
			accion= (EAccion) this.attrs.get("accion");
			if(!(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))){
				ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");
				params= new HashMap<>();
				params.put("idVenta", ticketAbierto.getKey());
				setDomicilio(new Domicilio());
				this.attrs.put("registroCliente", new TcManticClientesDto());
				if(!ticketAbierto.getKey().equals(-1L)){
					ticketsAbiertos= (List<UISelectEntity>) this.attrs.get("ticketsAbiertos");
					ticketAbiertoPivote= ticketsAbiertos.get(ticketsAbiertos.indexOf(ticketAbierto));
					this.setAdminOrden(new AdminGarantia((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params), false, EAccion.LISTAR, -1L));
					loadMediosPago(ticketAbierto.getKey());
					this.ticketOriginal= (TicketVenta) getAdminOrden().getOrden();
					this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
					loadCatalog();
					doAsignaClienteTicketAbierto();
					this.attrs.put("pagarVenta", true);
					this.attrs.put("cobroVenta", true);				
					this.attrs.put("tabIndex", 0);
					this.attrs.put("creditoCliente", ticketAbiertoPivote.toLong("idCredito").equals(1L));
					if(getAdminOrden().getArticulos().isEmpty())
						JsfBase.addMessage("Garantia de ticket.", "No hay articulos disponibles para el ticket seleccionado.", ETipoMensaje.INFORMACION);
				} // if
				else{				
					this.setAdminOrden(new AdminGarantia(new TicketVenta()));
					this.attrs.put("pagarVenta", false);
					this.attrs.put("facturarVenta", false);
					this.attrs.put("cobroVenta", false);
					this.attrs.put("clienteAsignado", false);
					this.attrs.put("tabIndex", 0);
					this.attrs.put("creditoCliente", false);				
				} // else			
			} // if
			else
				doAsignaClienteTicketAbierto();
			UIBackingUtilities.execute("jsArticulos.initArrayArt(" + String.valueOf(getAdminOrden().getArticulos().size()-1) + ");");
			this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doAsignaTicketAbierto
	
	private void loadMediosPago(Long idVenta){
		MotorBusqueda motor     = null;
		List<Entity> pagosVentas= null;
		try {
			motor= new MotorBusqueda(idVenta);
			pagosVentas= motor.pagosVenta();
			this.attrs.put("pagosVenta", pagosVentas);
			this.attrs.put("tipoPago", pagosVentas.get(0).toLong("idTipoMedioPago"));
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // loadMediosPago
	
	private void doAsignaClienteTicketAbierto() throws Exception{		
		MotorBusqueda motorBusqueda           = null;
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		Entity cliente                        = null;
		try {
			motorBusqueda= new MotorBusqueda(-1L, ((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente());
			cliente= motorBusqueda.toCliente();			
			if(cliente!= null){
				seleccion= new UISelectEntity(cliente);
				this.attrs.put("clienteAsignado", !seleccion.toString("clave").equals(CLAVE_VENTA_GRAL));
				this.attrs.put("nombreCliente", seleccion.toString("razonSocial"));
				clientesSeleccion= new ArrayList<>();
				clientesSeleccion.add(seleccion);
				this.attrs.put("clientesSeleccion", clientesSeleccion);
				this.attrs.put("clienteSeleccion", seleccion);
				setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
				doReCalculatePreciosArticulos(seleccion.getKey());			
			} // if
		} // try
		catch (Exception e) {	
			throw e;
		} // catch		
	} // doAsignaClienteTicketAbierto
	
	private void loadCajas() {
		List<UISelectEntity> cajas= null;
		Map<String, Object>params = null;
		List<Columna> columns     = null;
		EAccion accion            = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			accion= (EAccion) this.attrs.get("accion");
			if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))
				params.put("idEmpresa", ((TicketVenta)this.getAdminOrden().getOrden()).getIdEmpresa());
			else	
				params.put("idEmpresa", this.attrs.get("idEmpresa"));
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			cajas=(List<UISelectEntity>) UIEntity.build("TcManticCajasDto", "cajas", params, columns);
			this.attrs.put("cajas", cajas);
			this.attrs.put("caja", cajas.get(0));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
	} // loadCajas		
	
	public void doOpenCobro(){
		try {
			if(!this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L)))))
				this.attrs.put("tabIndex", 1);
			else
				JsfBase.addMessage("Cobrar venta", "No se ha seleccionado ningun ticket de venta", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doOpenCobro	
	
	public void doVerificaCantidadArticulos(Integer index) {
		Double cantidad         = 0D;
		Double cantidadGarantia = 0D;
		Articulo articuloAltered= null;
		try {
			articuloAltered= this.getAdminOrden().getArticulos().get(index);
			cantidad= articuloAltered.getCantidad();
			cantidadGarantia= articuloAltered.getCantidadGarantia();			
			if(!(cantidad<= cantidadGarantia)) {
				this.getAdminOrden().getArticulos().get(index).setCantidad(cantidadGarantia);
				JsfBase.addMessage("Cantidad de articulos", "La cantidad de articulos capturada no es valida, el maximo es de ".concat(String.valueOf(cantidadGarantia)), ETipoMensaje.ERROR);
			} // if
			this.image= LoadImages.getImage(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString(), String.valueOf(articuloAltered.getIdArticulo()));
			this.attrs.put("idArticulo", articuloAltered.getIdArticulo());
			this.attrs.put("descripcion", articuloAltered.getNombre());
			super.doCalculate(index);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch				
	} // doVerificaCantidadArticulos
	
	public void doActivarPago() {
		String tipoPago= null;				
		try {					
			tipoPago= this.attrs.get("tipoPago").toString();
			this.attrs.put("isEfectivo", tipoPago.equals(INDIVIDUAL));			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActivarPago
	
	private void loadBancos(){
		List<UISelectEntity> bancos= null;
		Map<String, Object> params = null;
		List<Columna> campos       = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos= new ArrayList<>();
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			bancos= UIEntity.build("TcManticBancosDto", "row", params, campos, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("bancos", bancos);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadBancos
}
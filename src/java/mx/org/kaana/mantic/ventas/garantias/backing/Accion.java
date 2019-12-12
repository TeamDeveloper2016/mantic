package mx.org.kaana.mantic.ventas.garantias.backing;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
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
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.garantias.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.enums.EEstatusClientes;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETiposGarantias;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.comun.IBaseVenta;
import mx.org.kaana.mantic.ventas.garantias.beans.DetalleGarantia;
import mx.org.kaana.mantic.ventas.garantias.beans.Garantia;
import mx.org.kaana.mantic.ventas.garantias.beans.PagoGarantia;
import mx.org.kaana.mantic.ventas.garantias.reglas.AdminGarantia;
import mx.org.kaana.mantic.ventas.garantias.reglas.CreateTicketGarantia;
import mx.org.kaana.mantic.ventas.garantias.reglas.GestorSQL;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value= "manticVentasGarantiasAccion")
@ViewScoped
public class Accion extends IBaseVenta implements Serializable {

  private static final long serialVersionUID  = 327393488565639367L;
	private static final String CLAVE_VENTA_GRAL= "VENTA";	
	private FormatLazyModel detalleDeudaCliente;
	private TicketVenta ticketOriginal;
	private StreamedContent image;
	
	public Accion() {
		super("menudeo");
	}

	public StreamedContent getImage() {
		return image;
	}

	public FormatLazyModel getDetalleDeudaCliente() {
		return detalleDeudaCliente;
	}	
	
	@PostConstruct
  @Override
  protected void init() {	
		EAccion accion              = null;
		List<UISelectEntity> tickets= null;
		Entity entity               = null;
    try {
			this.attrs.put("xcodigo", JsfBase.getFlashAttribute("xcodigo"));	
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null ? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null ? -1L: JsfBase.getFlashAttribute("idVenta"));
      this.attrs.put("idGarantia", JsfBase.getFlashAttribute("idGarantia")== null ? -1L: JsfBase.getFlashAttribute("idGarantia"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null ? null : JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("devolucionTicket", JsfBase.getFlashAttribute("devolucionTicket")== null ? null : JsfBase.getFlashAttribute("devolucionTicket"));
      this.attrs.put("isFactura", true);
      this.attrs.put("factura", null);
      this.attrs.put("isPesos", false);
      this.attrs.put("mostrarGarantia", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("tipoPago", 1L);
			this.attrs.put("mostrarDevolucion", false);
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
			this.attrs.put("tipoVenta", 1L);			
			this.attrs.put("tipoDevolucion", 1L);	
			this.attrs.put("ticketLock", -1L);
			this.image= LoadImages.getImage(-1L);
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				loadSucursales();	
			else
				loadSucursalesPerfil();			
			if(JsfBase.getFlashAttribute("accionVenta")!= null){								
				this.attrs.put("mostrarGarantia", true);
				this.attrs.put("accion", EAccion.AGREGAR);				
				this.attrs.put("retorno", JsfBase.getFlashAttribute("retornoVenta"));
				this.attrs.put("fecha", new Date(((Timestamp)JsfBase.getFlashAttribute("registroVenta")).getTime()));
				doLoad();								
				this.attrs.put("accion", EAccion.ASIGNAR);				
				doLoadTicketAbiertos();				
				doAsignaTicketAbierto();				
				loadBancos();								
				this.attrs.put("nombreAccion", Cadena.letraCapital(EAccion.ASIGNAR.name()));							
			} // if
			else {
				accion= (EAccion) this.attrs.get("accion");
				if(accion.equals(EAccion.CONSULTAR))
					this.attrs.put("mostrarGarantia", true);
				if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))
					doLoad();
				loadCajas();
				doLoadTicketAbiertos();						
				if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))
					doAsignaTicketAbierto();
				loadBancos();
			} // else
			if(this.attrs.get("devolucionTicket")!= null){				
				doUpdateOpenTickets(true);
				tickets= (List<UISelectEntity>) this.attrs.get("openTickets");
				toFindOpenTicket(tickets.get(0));
				entity= new Entity(-1L);
				entity.put("ticket", new Value("ticket", ((TicketVenta)this.getAdminOrden().getOrden()).getTicket()));
				this.attrs.put("cliente", entity);
			} // if
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
					loadDatosCliente(((TicketVenta)getAdminOrden().getOrden()).getIdVenta());
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
	
	protected void loadDatosCliente(Long idVenta) throws Exception{
		Map<String, Object>params = null;
		Entity descripcionGarantia= null;
		Entity entity            = null;
		try {
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", ((TicketVenta)getAdminOrden().getOrden()).getIdEmpresa());
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ventas.id_venta=" + idVenta);
			descripcionGarantia= (Entity) DaoFactory.getInstance().toEntity("VistaVentasDto", "lazy", params);
			this.attrs.put("ticket", descripcionGarantia);
			entity= new Entity(-1L);
			entity.put("ticket", new Value("ticket", ((TicketVenta)this.getAdminOrden().getOrden()).getTicket()));
			this.attrs.put("cliente", entity);
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
	} // loadDatosCliente
	
	public void doValidarDevolucion(){
		RequestContext rc     = null;
		String tipoVenta      = null;
		boolean lanzadoDialogo= true;
		try {
			tipoVenta= this.attrs.get("tipoVenta").toString();
			if(tipoVenta.equals("1"))
				lanzadoDialogo= verificarDevolucionCredito();
			if(lanzadoDialogo){
				rc= RequestContext.getCurrentInstance();
				rc.execute("ventaFinished(".concat(tipoVenta).concat(");"));
				rc.update("dialogoCerrarVenta");
			} // else
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doValidarDevolucion
	
	private boolean verificarDevolucionCredito() throws Exception{
		boolean regresar          = true;
		GestorSQL gestor          = null;
		Entity deuda              = null;
		boolean devolucionCompleta= false;
		boolean importeEquals     = false;
		Double saldoCliente       = 0D;
		Double saldoVenta         = 0D;
		Double diferencia         = 0D;
		Double diferenciaVenta    = 0D;
		try {
			this.attrs.put("mostrarDevolucion", false);
			gestor= new GestorSQL(((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta(), ((UISelectEntity) this.attrs.get("clienteSeleccion")).getKey());
			deuda= gestor.toDeudaVenta();
			saldoVenta= deuda.toDouble("saldo");
			devolucionCompleta= toDevolucionCompleta();
			importeEquals= deuda.toDouble("importe").equals(saldoVenta);
			this.attrs.put("pagoCredito", 0D);
			this.attrs.put("devolucionCredito", 0D);
			if(importeEquals && devolucionCompleta){
				this.attrs.put("messageDialog", "No hay saldo a favor. La deuda sera saldada.");
				this.attrs.put("accionCredito", EAccion.COMPLETO);
			} // if
			else if(!importeEquals && devolucionCompleta){				
				saldoCliente= gestor.toSaldoCliente();
				diferenciaVenta= deuda.toDouble("importe") - saldoVenta;
				if(saldoCliente < diferenciaVenta){
					this.attrs.put("mostrarDevolucion", true);
					this.attrs.put("messageDialog", "La devolución es por la cantidad de: $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, (diferenciaVenta-saldoCliente)) + ", el resto se abonara a las cuentas pendientes. Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta) + ".");
					this.attrs.put("accionCredito", EAccion.AGREGAR);
					this.attrs.put("pagoCredito", diferenciaVenta);
					this.attrs.put("devolucionCredito", (diferenciaVenta-saldoCliente));					
				} // if
				else{
					this.attrs.put("messageDialog", "No hay saldo a favor. La deuda sera saldada y los pagos realizados seran abonados a las cuentas pendientes. Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta) + ".");
					this.attrs.put("accionCredito", EAccion.ASIGNAR);
					this.attrs.put("pagoCredito", diferenciaVenta);
				} // else
			} // else if
			else if(importeEquals && !devolucionCompleta){
				this.attrs.put("messageDialog", "Se actualizara el monto del saldo de la deuda.");
				this.attrs.put("accionCredito", EAccion.MODIFICAR);
				this.attrs.put("pagoCredito", deuda.toDouble("importe") - getAdminOrden().getTotales().getTotal());				
			} // else if
			else if(!importeEquals && !devolucionCompleta){
				diferencia= saldoVenta - getAdminOrden().getTotales().getTotal();				
				saldoCliente= gestor.toSaldoCliente();
				diferenciaVenta= deuda.toDouble("importe") - saldoVenta;
				if(diferencia < 0 && saldoCliente <= 0){
					this.attrs.put("mostrarDevolucion", true);
					this.attrs.put("messageDialog", "La devolución es por la cantidad de: $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, (diferencia*-1)) + ". Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta) + ".");
					this.attrs.put("accionCredito", EAccion.PROCESAR);											
					this.attrs.put("devolucionCredito", diferencia);
				} // if					
				else if(diferencia < 0  && saldoCliente > 0 && saldoCliente < (diferencia*-1)){
					this.attrs.put("mostrarDevolucion", true);
					this.attrs.put("messageDialog", "La devolución es por la cantidad de: $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, ((diferencia*-1) - saldoCliente)) + ", el resto se abonara a las cuentas pendientes." + " Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta) + ".");
					this.attrs.put("accionCredito", EAccion.ACTIVAR);
					this.attrs.put("pagoCredito", saldoCliente);
					this.attrs.put("devolucionCredito", ((diferencia*-1) - saldoCliente));
				} // else					
				else if(diferencia < 0  && saldoCliente > 0 && saldoCliente > (diferencia*-1)){
					this.attrs.put("messageDialog", "No hay saldo a favor. La deuda sera saldada y los pagos realizados seran abonados a las cuentas pendientes. Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta) + ".");
					this.attrs.put("accionCredito", EAccion.JUSTIFICAR);
					this.attrs.put("pagoCredito", (diferencia*-1));
				} // else					
				else if (diferencia > 0 ){
					this.attrs.put("messageDialog", "No hay saldo a favor. Los abonos se utilizaran para cubrir la deuda de los articulos vigentes. Saldo pendiente $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferencia) + ". Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta) + ".");
					this.attrs.put("accionCredito", EAccion.CALCULAR);
					this.attrs.put("pagoCredito", diferenciaVenta);
				} // else				
			} // else if
			toDetalleDeudaCliente();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // verificarDevolucionCredito
	
	public void toDetalleDeudaCliente(){
		Map<String, Object>params= null;
		List<Columna>columns     = null;
		try {
			params= new HashMap<>();
			params.put("idCliente", ((UISelectEntity) this.attrs.get("clienteSeleccion")).getKey());									
			params.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");			
			params.put(Constantes.SQL_CONDICION, "tc_mantic_clientes_deudas.id_cliente_estatus in (".concat(EEstatusClientes.INICIADA.getIdEstatus().toString()).concat(",").concat(EEstatusClientes.PARCIALIZADA.getIdEstatus().toString()).concat(")"));			
			columns= new ArrayList<>();
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			this.detalleDeudaCliente= new FormatLazyModel("VistaClientesDto", "cuentas", params, columns);
			UIBackingUtilities.resetDataTable("detalleDeudaCliente");
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	}	// toDetalleDeudaCliente
	
	private boolean toDevolucionCompleta(){
		boolean regresar= false;
		int count       = 0;
		try {
			for(Articulo art: this.getAdminOrden().getArticulos()){
				if(!art.getCantidad().equals(0D) && art.getCantidad().equals(art.getCantidadGarantia()))
					count++;
			} // for
			regresar= count== this.getAdminOrden().getArticulos().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDevolucionCompleta
	
  public void doAceptar() {  
    Transaccion transaccion    = null;    
		CreateTicketGarantia ticket= null;		
		Pago pagoPivote            = null;
		DetalleGarantia detalle    = null;
		String claves              = "";
		String tickets             = "";
		String numeroTicket        = "";
		int count                  = 0;
    try {
			detalle= loadDetalleGarantia();			
			transaccion= new Transaccion(detalle);			
			if (transaccion.ejecutar(EAccion.REPROCESAR)) {						
				for(Garantia newGarantia: detalle.getGarantias()){
					if(!newGarantia.getArticulosGarantia().isEmpty()){
						numeroTicket= transaccion.getTickets().get(count);
						count++;
						pagoPivote= loadPagoPivote(newGarantia);
						ticket= new CreateTicketGarantia(pagoPivote, "DEVOLUCIÓN", newGarantia.getIdEfectivo().equals(ETiposGarantias.RECIBIDA.getIdTipoGarantia().intValue()));
						newGarantia.getGarantia().setTicket(numeroTicket);
						newGarantia.setTotales(pagoPivote);
						ticket.setGarantia(newGarantia);
						claves= claves.concat(ticket.getPrincipal().getClave().concat("-").concat(numeroTicket).concat("~"));
						tickets= tickets.concat(ticket.toHtml().concat("~")); 						
					} // if
				} // for
				claves= claves.substring(0, claves.length()-1);
				tickets= tickets.substring(0, tickets.length()-1);
				if(count== 1) {
					UIBackingUtilities.execute("jsTicket.imprimirTicket('" + claves + "','" + tickets + "');");
					UIBackingUtilities.execute("jsTicket.process('"+ JsfBase.getContext()+ "/Paginas/Mantic/Ventas/Caja/accion.jsf');");
				} // if
				else
					UIBackingUtilities.execute("jsTicket.imprimirMoreTicket('" + claves + "','" + tickets + "');");				
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
  } // doAccion  	
	
	private Pago loadPagoPivote(Garantia garantia){
		Pago pago                = null;
		ETipoMediosPago medioPago= null;
		Double total             = 0D;
		Double subTotal          = 0D;
		Double iva               = 0D;
		try {
			for(ArticuloVenta articulo: garantia.getArticulosGarantia()){
				total= total + articulo.getTotal();
				subTotal= subTotal + articulo.getSubTotal();
				iva= iva + articulo.getImpuestos();
			} // for
			pago= new Pago(new Totales());
			pago.getTotales().setTotal(total);
			pago.getTotales().setSubTotal(subTotal);
			pago.getTotales().setIva(iva);
			medioPago= ETipoMediosPago.fromIdTipoPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
			switch(medioPago){
				case EFECTIVO:
					pago.setEfectivo(pago.getTotales().getTotalDosDecimales());
					break;
				case CHEQUE:
					pago.setCheque(pago.getTotales().getTotalDosDecimales());
					pago.setBancoCheque((UISelectEntity) this.attrs.get("banco"));
					pago.setReferenciaCheque(this.attrs.get("referencia").toString());
					break;
				case TARJETA_CREDITO:
					pago.setCredito(pago.getTotales().getTotalDosDecimales());
					pago.setBancoCredito((UISelectEntity) this.attrs.get("banco"));
					pago.setReferenciaCredito(this.attrs.get("referencia").toString());
					break;
				case TARJETA_DEBITO:
					pago.setDebito(pago.getTotales().getTotalDosDecimales());
					pago.setBancoDebito((UISelectEntity) this.attrs.get("banco"));
					pago.setReferenciaTransferencia(this.attrs.get("referencia").toString());
					break;
				case TRANSFERENCIA:
					pago.setTransferencia(pago.getTotales().getTotalDosDecimales());
					pago.setBancoTransferencia((UISelectEntity) this.attrs.get("banco"));
					pago.setReferenciaTransferencia(this.attrs.get("referencia").toString());
					break;				
			} // switch			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return pago;
	} // loadPagoPivote
	
	private DetalleGarantia loadDetalleGarantia() throws Exception{
		DetalleGarantia regresar = null;
		List<Garantia> garantias = null;		
		Garantia garantia        = null;		
		TicketVenta ticketVenta  = null;		
		PagoGarantia pagoGarantia= null;
		List<Articulo>articulos  = null;
		UISelectEntity cliente   = null;
		try {
			regresar= new DetalleGarantia();
			regresar.setIdVenta(((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
			cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");
			regresar.setIdCliente(cliente.getKey());
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
				garantia.setIdEfectivo(tipoGarantia.getIdTipoGarantia().intValue());
				List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
				if(!almacenes.isEmpty()) 
					garantia.getTicketVenta().setIdAlmacen(almacenes.get(0).getKey());			
				garantias.add(garantia);
			} // for			
			pagoGarantia= new PagoGarantia();
			pagoGarantia.setIdTipoVenta(Long.valueOf(this.attrs.get("tipoVenta").toString()));
			if(Long.valueOf(this.attrs.get("tipoVenta").toString()).equals(2L)){				
				if(Long.valueOf(this.attrs.get("tipoPago").toString()).equals(1L))
					pagoGarantia.setIdTipoPago(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());		
				else{
					pagoGarantia.setIdTipoPago(ETipoMediosPago.fromIdTipoPago(Long.valueOf(this.attrs.get("tipoPago").toString())).getIdTipoMedioPago());
					pagoGarantia.setTransferencia(this.attrs.get("referencia").toString());
					pagoGarantia.setIdBanco(Long.valueOf(this.attrs.get("banco").toString()));
				} // else
			} // if
			else
				pagoGarantia.setTipoDevolucion(Long.valueOf(this.attrs.get("tipoDevolucion").toString()));
			regresar.setGarantias(garantias);
			regresar.setPagoGarantia(pagoGarantia);
			regresar.setTotales((Pago) this.attrs.get("pago"));			
			regresar.setIdCaja(Long.valueOf(this.attrs.get("caja").toString()));			
			regresar.getTicketVenta().setIdEmpresa(Long.valueOf(this.attrs.get("idEmpresa").toString()));			
			regresar.setAccionCredito((EAccion) this.attrs.get("accionCredito"));
			regresar.setPagoCredito((Double) this.attrs.get("pagoCredito"));
			regresar.setDevolucionCredito((Double) this.attrs.get("devolucionCredito"));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadVentaFinalizada
	
	protected void loadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", this.attrs.get("idEmpresa"));
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenPrincipal", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) 
				((TicketVenta)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
      columns.remove(0);
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
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
		List<Columna> columns               = null;
		EAccion accion                      = null;
		Entity entity                       = null;
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
			if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR)||accion.equals(EAccion.ASIGNAR)){
				this.attrs.put("ticketAbierto", ticketsAbiertos.get(0));
				this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			} // if
			else{
				this.attrs.put("ticketAbierto", new UISelectEntity("-1"));
				this.setAdminOrden(new AdminGarantia(new TicketVenta()));
				this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
				toFindOpenTicket(new UISelectEntity("-1"));
				entity= new Entity(-1L);
				entity.put("ticket", new Value("ticket", ""));
				this.attrs.put("cliente", entity);
				this.attrs.put("ticket", new Entity(-1L));
			} // else
			this.attrs.put("pagarVenta", false);
			this.attrs.put("facturarVenta", false);
			this.attrs.put("cobroVenta", false);
			this.attrs.put("clienteAsignado", false);
			this.attrs.put("tabIndex", 0);		
			this.attrs.put("registroCliente", new TcManticClientesDto());
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.clear();
			params.put("sucursales", this.attrs.get("idEmpresa"));			
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenPrincipal", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) 
				((TicketVenta)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
			params.clear();
			unlockVentaExtends(-1L, (Long)this.attrs.get("ticketLock"));			
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
			if(accion.equals(EAccion.ASIGNAR)){
				regresar.append(" tc_mantic_ventas.id_venta=");
				regresar.append(this.attrs.get("idVenta"));
			} // if
			else if(accion.equals(EAccion.CONSULTAR) || accion.equals(EAccion.MODIFICAR)){
				regresar.append(" tc_mantic_ventas.id_venta=");
				regresar.append(this.getAdminOrden().getOrden().getKey());
			} // if
			else {
				regresar.append(" DATE_FORMAT(tc_mantic_ventas.registro, '%Y%m%d')=");
				regresar.append(Fecha.formatear(Fecha.FECHA_ESTANDAR, fecha));
				regresar.append(" and tc_mantic_ventas.id_venta_estatus=");
				regresar.append(EEstatusVentas.PAGADA.getIdEstatusVenta());						
			} // else
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicion
	
	protected String toCondicionOpenTicket() {
		return toCondicionOpenTicket(false);
	}
	
	protected String toCondicionOpenTicket(boolean cobroCaja) {
		StringBuilder regresar= null;
		Entity ticket         = null;
		try {
			regresar= new StringBuilder();
			if(this.attrs.get("openTicket")!= null){
				regresar.append(" tc_mantic_ventas.ticket like '%");
				regresar.append(this.attrs.get("openTicket"));	
				regresar.append("%' and ");			
			} // if
			regresar.append(" (tc_mantic_ventas.id_venta_estatus=");			
			regresar.append(EEstatusVentas.PAGADA.getIdEstatusVenta());						
			regresar.append(" or tc_mantic_ventas.id_venta_estatus=");
			regresar.append(EEstatusVentas.TERMINADA.getIdEstatusVenta());									
			regresar.append(" or tc_mantic_ventas.id_venta_estatus=");
			regresar.append(EEstatusVentas.CREDITO.getIdEstatusVenta());									
			regresar.append(") ");
			if(cobroCaja){
				ticket= (Entity) this.attrs.get("devolucionTicket");
				regresar.append(" and tc_mantic_ventas.ticket= '");
				regresar.append(ticket.toString("ticket"));	
				regresar.append("'");
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicionOpenTicket
	
	@Override
	public void doAsignaTicketAbierto(){
		Map<String, Object>params           = null;
		UISelectEntity ticketAbierto        = null;
		UISelectEntity ticketAbiertoPivote  = null;
		List<UISelectEntity> ticketsAbiertos= null;
		EAccion accion                      = null;
		MotorBusqueda motor                 = null;
		Entity factura                      = null;
		Entity entity                       = null;
		try {
			accion= (EAccion) this.attrs.get("accion");
			if(!(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))){
				ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");
				params= new HashMap<>();
				params.put("idVenta", ticketAbierto.getKey());
				setDomicilio(new Domicilio());
				this.attrs.put("registroCliente", new TcManticClientesDto());
				if(!ticketAbierto.getKey().equals(-1L)){
					unlockVentaExtends(ticketAbierto.getKey(), (Long)this.attrs.get("ticketLock"));
					this.attrs.put("ticketLock", ticketAbierto.getKey());
					ticketsAbiertos= (List<UISelectEntity>) this.attrs.get("ticketsAbiertos");
					ticketAbiertoPivote= ticketsAbiertos.get(ticketsAbiertos.indexOf(ticketAbierto));
					this.attrs.put("ticket", (Entity)ticketAbiertoPivote);
					this.setAdminOrden(new AdminGarantia((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params), false, EAccion.ASIGNAR, -1L));
					loadMediosPago(ticketAbierto.getKey());
					entity= new Entity(-1L);
					entity.put("ticket", new Value("ticket", ((TicketVenta)this.getAdminOrden().getOrden()).getTicket()));
					this.attrs.put("cliente", entity);
					this.attrs.put("idEmpresa", ((TicketVenta)this.getAdminOrden().getOrden()).getIdEmpresa());
					loadCajas();
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
					if(ticketAbiertoPivote.get("idFactura").getData()!= null){
						motor= new MotorBusqueda(ticketAbiertoPivote.toLong("idFactura"));
						factura= motor.toFactura();
						if(factura!= null){
							this.attrs.put("factura", factura);
							this.attrs.put("isFactura", false);						
						} // if										
					} // if										
					else
						this.attrs.put("isFactura", true);						
				} // if
				else{			
					unlockVentaExtends(-1L, (Long)this.attrs.get("ticketLock"));
					this.attrs.put("ticketLock", -1L);
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
	
	public void doAsignaOpenTicket(SelectEvent event) {
		UISelectEntity seleccion    = null;
		List<UISelectEntity> tickets= null;
		try {
			tickets= (List<UISelectEntity>) this.attrs.get("openTickets");
			seleccion= tickets.get(tickets.indexOf((UISelectEntity)event.getObject()));
			this.toFindOpenTicket(seleccion);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
  private void toFindOpenTicket(UISelectEntity seleccion) {
		Map<String, Object>params= null;
		EAccion accion           = null;
		MotorBusqueda motor      = null;
		Entity factura           = null;
		try {
			accion= (EAccion) this.attrs.get("accion");
			if(!(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))){
				params= new HashMap<>();
				params.put("idVenta", seleccion.getKey());
				setDomicilio(new Domicilio());
				this.attrs.put("registroCliente", new TcManticClientesDto());
				if(!seleccion.getKey().equals(-1L)){	
					unlockVentaExtends(seleccion.getKey(), (Long)this.attrs.get("ticketLock"));
					this.attrs.put("ticketLock", seleccion.getKey());
					this.attrs.put("ticket", (Entity)seleccion);
					this.setAdminOrden(new AdminGarantia((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params), false, EAccion.LISTAR, -1L));
					loadMediosPago(seleccion.getKey());
					this.ticketOriginal= (TicketVenta) getAdminOrden().getOrden();
					this.attrs.put("tipoVenta", this.ticketOriginal.getIdVentaEstatus().equals(EEstatusVentas.CREDITO.getIdEstatusVenta()) ? 1L : 2L);
					this.attrs.put("mostrarGarantia", this.ticketOriginal.getIdVentaEstatus().equals(EEstatusVentas.CREDITO.getIdEstatusVenta()));
					this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
					loadCatalog();
					doAsignaClienteTicketAbierto();
					this.attrs.put("pagarVenta", true);
					this.attrs.put("cobroVenta", true);				
					this.attrs.put("tabIndex", 0);
					this.attrs.put("creditoCliente", seleccion.toLong("idCredito").equals(1L));
					if(getAdminOrden().getArticulos().isEmpty())
						JsfBase.addMessage("Garantia de ticket.", "No hay articulos disponibles para el ticket seleccionado.", ETipoMensaje.INFORMACION);
					if(seleccion.get("idFactura").getData()!= null){
						motor= new MotorBusqueda(seleccion.toLong("idFactura"));
						factura= motor.toFactura();
						if(factura!= null){
							this.attrs.put("factura", factura);
							this.attrs.put("isFactura", false);						
						} // if										
					} // if										
					else
						this.attrs.put("isFactura", true);						
				} // if
				else{				
					unlockVentaExtends(-1L, (Long)this.attrs.get("ticketLock"));
					this.attrs.put("ticketLock", -1L);
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
	} // toFindOpenTicket
	
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
	
	public List<UISelectEntity> doCompleteTicket(String query) {
		this.attrs.put("openTicket", query);
    this.doUpdateOpenTickets();
		return (List<UISelectEntity>)this.attrs.get("openTickets");
	}	// doCompleteCliente
	
	public void doUpdateOpenTickets() {
		doUpdateOpenTickets(false);
	} // doUpdateOpenTickets
	
	public void doUpdateOpenTickets(Boolean cobroCaja) {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put(Constantes.SQL_CONDICION, toCondicionOpenTicket(cobroCaja));
			columns= new ArrayList<>();
			columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));			
      this.attrs.put("openTickets", (List<UISelectEntity>) UIEntity.build("VistaVentasDto", "lazy", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}	// doUpdateClientes
	
	@Override
	public String doCancelar() {     	
    return (this.attrs.get("retorno") != null ? (String)this.attrs.get("retorno") : "filtro").concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public void doReturnAllItems() {
		try {
			int index= 0;
			for(Articulo item: this.getAdminOrden().getArticulos()) {
				item.setCantidad(item.getCantidadGarantia());
				super.doCalculate(index++);
			} // for
		} // try
		catch (Exception e) {			
      Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	}	
}
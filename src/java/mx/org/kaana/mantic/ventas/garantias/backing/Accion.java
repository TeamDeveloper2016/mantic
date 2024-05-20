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
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
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
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
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
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;

@Named(value= "manticVentasGarantiasAccion")
@ViewScoped
public class Accion extends IBaseVenta implements Serializable {

  private static final long serialVersionUID  = 327393488565639367L;
	private static final String CLAVE_VENTA_GRAL= "VENTA";	
	private FormatLazyModel detalleDeudaCliente;
	private TicketVenta ticketOriginal;
	private StreamedContent image;
  protected FormatLazyModel lazyModelDetalle;
	
	public Accion() {
		super("menudeo");
	}

	public StreamedContent getImage() {
		return image;
	}

	public FormatLazyModel getDetalleDeudaCliente() {
		return detalleDeudaCliente;
	}	
	
  public FormatLazyModel getLazyModelDetalle() {
    return this.lazyModelDetalle;
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
      this.attrs.put("refacturar", false);
  		this.image= LoadImages.getImage(-1L);
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				this.loadSucursales();	
			else
				this.loadSucursalesPerfil();			
			if(JsfBase.getFlashAttribute("accionVenta")!= null) {								
				this.attrs.put("mostrarGarantia", true);
				this.attrs.put("accion", EAccion.AGREGAR);				
				this.attrs.put("retorno", JsfBase.getFlashAttribute("retornoVenta"));
				this.attrs.put("fecha", new Date(((Timestamp)JsfBase.getFlashAttribute("registroVenta")).getTime()));
				this.doLoad();								
				this.attrs.put("accion", EAccion.ASIGNAR);				
				this.doLoadTicketAbiertos();				
				this.doAsignaTicketAbierto();				
				this.loadBancos();								
				this.attrs.put("nombreAccion", Cadena.letraCapital(EAccion.ASIGNAR.name()));							
			} // if
			else {
				accion= (EAccion) this.attrs.get("accion");
				if(accion.equals(EAccion.CONSULTAR))
					this.attrs.put("mostrarGarantia", true);
				if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))
					this.doLoad();
				this.loadCajas();
				this.doLoadTicketAbiertos();						
				if(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))
					this.doAsignaTicketAbierto();
				this.loadBancos();
			} // else
			if(this.attrs.get("devolucionTicket")!= null) {
				this.doUpdateOpenTickets(true);
				tickets= (List<UISelectEntity>) this.attrs.get("openTickets");
				this.toFindOpenTicket(tickets.get(0));
				entity= new Entity(-1L);
				entity.put("ticket", new Value("ticket", ((TicketVenta)this.getAdminOrden().getOrden()).getTicket()));
				this.attrs.put("cliente", entity);
			} // if
      this.lookForGarantia();
      this.doLoadGarantia();
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
          this.setAdminOrden(new AdminGarantia((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "VistaGarantiasDto", "garantia", this.attrs), eaccion, Long.valueOf(this.attrs.get("idGarantia").toString())));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
    			this.attrs.put("idEmpresa", ((TicketVenta)this.getAdminOrden().getOrden()).getIdEmpresa());
					this.loadDatosCliente(((TicketVenta)getAdminOrden().getOrden()).getIdVenta());
          break;
      } // switch
			this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			this.attrs.put("idVenta", ((TicketVenta)getAdminOrden().getOrden()).getIdVenta());
			this.loadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad
	
	protected void loadDatosCliente(Long idVenta) throws Exception{
		Map<String, Object>params = new HashMap<>();
		Entity descripcionGarantia= null;
		Entity entity             = null;
		try {
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
	
	public void doValidarDevolucion() {
		RequestContext rc     = null;
		String tipoVenta      = null;
		boolean lanzadoDialogo= true;
		try {
			tipoVenta= this.attrs.get("tipoVenta").toString();
			if(tipoVenta.equals("1"))
				lanzadoDialogo= this.verificarDevolucionCredito();
			if(lanzadoDialogo) {
				rc= RequestContext.getCurrentInstance();
				rc.execute("ventaFinished(".concat(tipoVenta).concat(");"));
				rc.update("dialogoCerrarVenta");
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doValidarDevolucion
	
	private boolean verificarDevolucionCredito() throws Exception {
		boolean regresar  = true;
		GestorSQL gestor  = null;
		Entity deuda      = null;
		boolean completa  = false;
		boolean iguales   = false;
		Double saldoGlobal= 0D;
		Double saldoVenta = 0D;
		Double diferencia = 0D;
		Double diferenciaVenta= 0D;
		try {
			this.attrs.put("mostrarDevolucion", false);
			gestor= new GestorSQL(((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta(), ((UISelectEntity) this.attrs.get("clienteSeleccion")).getKey());
			deuda= gestor.toDeudaVenta();
			saldoVenta= deuda.toDouble("saldo");
			completa= toDevolucionCompleta();
			iguales= deuda.toDouble("importe").equals(saldoVenta);
			this.attrs.put("pagoCredito", 0D);
			this.attrs.put("devolucionCredito", 0D);
			if(iguales && completa){
				this.attrs.put("messageDialog", "No hay saldo a favor. La deuda será saldada");
				this.attrs.put("accionCredito", EAccion.COMPLETO);
			} // if
			else if(!iguales && completa){				
				saldoGlobal= gestor.toSaldoCliente();
				diferenciaVenta= deuda.toDouble("importe") - saldoVenta;
				if(saldoGlobal < diferenciaVenta){
					this.attrs.put("mostrarDevolucion", true);
					this.attrs.put("messageDialog", "La devolución es por la cantidad de: $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, (diferenciaVenta-saldoGlobal)) + ", el resto se abonara a las cuentas pendientes. Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta));
					this.attrs.put("accionCredito", EAccion.AGREGAR);
					this.attrs.put("pagoCredito", diferenciaVenta);
					this.attrs.put("devolucionCredito", (diferenciaVenta-saldoGlobal));					
				} // if
				else{
					this.attrs.put("messageDialog", "No hay saldo a favor. La deuda será saldada y los pagos realizados seran abonados a las cuentas pendientes. Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta));
					this.attrs.put("accionCredito", EAccion.ASIGNAR);
					this.attrs.put("pagoCredito", diferenciaVenta);
				} // else
			} // else if
			else if(iguales && !completa){
				this.attrs.put("messageDialog", "Se actualizará el monto del saldo de la deuda");
				this.attrs.put("accionCredito", EAccion.MODIFICAR);
				this.attrs.put("pagoCredito", deuda.toDouble("importe") - getAdminOrden().getTotales().getTotal());				
			} // else if
			else if(!iguales && !completa){
				diferencia= saldoVenta - getAdminOrden().getTotales().getTotal();				
				saldoGlobal= gestor.toSaldoCliente();
				diferenciaVenta= deuda.toDouble("importe") - saldoVenta;
				if(diferencia < 0 && saldoGlobal <= 0){
					this.attrs.put("mostrarDevolucion", true);
					this.attrs.put("messageDialog", "La devolución es por la cantidad de: $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, (diferencia*-1)) + ". Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta));
					this.attrs.put("accionCredito", EAccion.PROCESAR);											
					this.attrs.put("devolucionCredito", diferencia);
				} // if					
				else if(diferencia < 0  && saldoGlobal > 0 && saldoGlobal < (diferencia*-1)){
					this.attrs.put("mostrarDevolucion", true);
					this.attrs.put("messageDialog", "La devolución es por la cantidad de: $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, ((diferencia*-1) - saldoGlobal)) + ", el resto se abonara a las cuentas pendientes." + " Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta));
					this.attrs.put("accionCredito", EAccion.ACTIVAR);
					this.attrs.put("pagoCredito", saldoGlobal);
					this.attrs.put("devolucionCredito", ((diferencia*-1) - saldoGlobal));
				} // else					
				else if(diferencia < 0  && saldoGlobal > 0 && saldoGlobal > (diferencia*-1)){
					this.attrs.put("messageDialog", "No hay saldo a favor. La deuda sera saldada y los pagos realizados seran abonados a las cuentas pendientes. Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta));
					this.attrs.put("accionCredito", EAccion.JUSTIFICAR);
					this.attrs.put("pagoCredito", (diferencia*-1));
				} // else					
				else if (diferencia > 0 ){
					this.attrs.put("messageDialog", "No hay saldo a favor. Los abonos se utilizaran para cubrir la deuda de los articulos vigentes. Saldo pendiente $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferencia) + ". Pagos por $" + Numero.formatear(Numero.NUMERO_SAT_DECIMALES, diferenciaVenta));
					this.attrs.put("accionCredito", EAccion.CALCULAR);
					this.attrs.put("pagoCredito", diferenciaVenta);
				} // else				
			} // else if
			this.toDetalleDeudaCliente();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // verificarDevolucionCredito
	
	public void toDetalleDeudaCliente(){
		Map<String, Object>params= new HashMap<>();
		List<Columna>columns     = new ArrayList<>();
		try {
			params.put("idCliente", ((UISelectEntity) this.attrs.get("clienteSeleccion")).getKey());									
			params.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");			
			params.put(Constantes.SQL_CONDICION, "tc_mantic_clientes_deudas.id_cliente_estatus in (".concat(EEstatusClientes.INICIADA.getIdEstatus().toString()).concat(",").concat(EEstatusClientes.PARCIALIZADA.getIdEstatus().toString()).concat(")"));			
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MILES_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MILES_SAT_DECIMALES));
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
      if(Cadena.isVacio(this.attrs.get("refacturar")) || !(Boolean)this.attrs.get("refacturar")) {
        detalle= this.loadDetalleGarantia();			
        transaccion= new Transaccion(detalle);			
        if (transaccion.ejecutar(EAccion.REPROCESAR)) {						
          for(Garantia newGarantia: detalle.getGarantias()){
            if(!newGarantia.getArticulosGarantia().isEmpty()){
              numeroTicket= transaccion.getTickets().get(count);
              count++;
              pagoPivote= this.loadPagoPivote(newGarantia);
              ticket= new CreateTicketGarantia(pagoPivote, "DEVOLUCIÓN", newGarantia.getIdEfectivo().equals(ETiposGarantias.RECIBIDA.getIdTipoGarantia().intValue()));
              newGarantia.getGarantia().setTicket(numeroTicket);
              newGarantia.setTotales(pagoPivote);
              ticket.setGarantia(newGarantia);
              claves = claves.concat(ticket.getPrincipal().getClave().concat("-").concat(numeroTicket).concat("~"));
              tickets= tickets.concat(ticket.toHtml().concat("~")); 						
            } // if
          } // for
          claves = claves.substring(0, claves.length()-1);
          tickets= tickets.substring(0, tickets.length()-1);
          if(count== 1) {
            UIBackingUtilities.execute("jsTicket.imprimirTicket('" + claves + "','" + tickets + "');");
            if(ticket!= null && ticket.getTicket()!= null && !Cadena.isVacio(ticket.getTicket().getIdFactura()))
              UIBackingUtilities.execute("jsTicket.process('"+ JsfBase.getContext()+ "/Paginas/Mantic/Ventas/Garantias/cancela.jsf?faces-redirect=true&xyz="+ Cifrar.cifrar(String.valueOf((Long)this.attrs.get("idVenta")))+ "&zyx="+ Cifrar.cifrar(String.valueOf(this.toCierreCaja()))+ "');");
            else
              UIBackingUtilities.execute("jsTicket.process('"+ JsfBase.getContext()+ "/Paginas/Mantic/Ventas/Caja/accion.jsf');");
          } // if
          else
            UIBackingUtilities.execute("jsTicket.imprimirMoreTicket('" + claves + "','" + tickets + "');");				
          JsfBase.addMessage("Se finalizó con éxito la garantia", ETipoMensaje.INFORMACION);
          this.setAdminOrden(new AdminGarantia(new TicketVenta()));
          this.attrs.put("pago", new Pago(this.getAdminOrden().getTotales()));
          this.init();
        } // if
        else 
  				JsfBase.addMessage("Ocurrió un error al registrar la garantia", ETipoMensaje.ERROR);			
			} // if
			else 
        UIBackingUtilities.execute("refacturar('"+ JsfBase.getContext()+ "/Paginas/Mantic/Ventas/Garantias/cancela.jsf?faces-redirect=true&xyz="+ Cifrar.cifrar(String.valueOf((Long)this.attrs.get("idVenta")))+ "&zyx="+ Cifrar.cifrar(String.valueOf(this.toCierreCaja()))+ "');");
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
				total   = total+ articulo.getTotal();
				subTotal= subTotal+ articulo.getSubTotal();
				iva     = iva+ articulo.getImpuestos();
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
					pagoGarantia.setIdBanco(((UISelectEntity)this.attrs.get("banco")).getKey());
				} // else
			} // if
			else
				pagoGarantia.setTipoDevolucion(Long.valueOf(this.attrs.get("tipoDevolucion").toString()));
			regresar.setGarantias(garantias);
			regresar.setPagoGarantia(pagoGarantia);
			regresar.setTotales((Pago) this.attrs.get("pago"));			
			regresar.setIdCaja(((UISelectEntity)this.attrs.get("caja")).getKey());			
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
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
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
		List<UISelectEntity> clientesSeleccion= new ArrayList<>();
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
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
		Map<String, Object>params           = new HashMap<>();
		List<Columna> campos                = new ArrayList<>();
		List<Columna> columns               = new ArrayList<>();
		EAccion accion                      = null;
		Entity entity                       = null;
		try {
			this.loadCajas();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
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
			regresar.append(" or tc_mantic_ventas.id_venta_estatus=");
			regresar.append(EEstatusVentas.TIMBRADA.getIdEstatusVenta());									
			regresar.append(") ");
			if(cobroCaja) {
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
				this.setDomicilio(new Domicilio());
				this.attrs.put("registroCliente", new TcManticClientesDto());
				if(!ticketAbierto.getKey().equals(-1L)){
					this.unlockVentaExtends(ticketAbierto.getKey(), (Long)this.attrs.get("ticketLock"));
					this.attrs.put("ticketLock", ticketAbierto.getKey());
					ticketsAbiertos    = (List<UISelectEntity>) this.attrs.get("ticketsAbiertos");
					ticketAbiertoPivote= ticketsAbiertos.get(ticketsAbiertos.indexOf(ticketAbierto));
					this.attrs.put("ticket", (Entity)ticketAbiertoPivote);
					this.setAdminOrden(new AdminGarantia((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params), false, EAccion.ASIGNAR, -1L));
					this.loadMediosPago(ticketAbierto.getKey());
					entity= new Entity(-1L);
					entity.put("ticket", new Value("ticket", ((TicketVenta)this.getAdminOrden().getOrden()).getTicket()));
					this.attrs.put("cliente", entity);
					this.attrs.put("idEmpresa", ((TicketVenta)this.getAdminOrden().getOrden()).getIdEmpresa());
					this.loadCajas();
					this.ticketOriginal= (TicketVenta) getAdminOrden().getOrden();
					this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
					this.loadCatalog();
					this.doAsignaClienteTicketAbierto();
					this.attrs.put("pagarVenta", true);
					this.attrs.put("cobroVenta", true);				
					this.attrs.put("tabIndex", 0);
					this.attrs.put("creditoCliente", ticketAbiertoPivote.toLong("idCredito").equals(1L));
					if(getAdminOrden().getArticulos().isEmpty())
						JsfBase.addMessage("Garantia de ticket.", "No hay articulos disponibles para el ticket seleccionado.", ETipoMensaje.INFORMACION);
					if(ticketAbiertoPivote.get("idFactura").getData()!= null) {
						motor= new MotorBusqueda(ticketAbiertoPivote.toLong("idFactura"));
						factura= motor.toFactura();
						if(factura!= null) {
							this.attrs.put("factura", factura);
							this.attrs.put("isFactura", false);						
						} // if										
					} // if										
					else
						this.attrs.put("isFactura", true);						
				} // if
				else{			
					this.unlockVentaExtends(-1L, (Long)this.attrs.get("ticketLock"));
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
		Map<String, Object>params= new HashMap<>();
		EAccion accion           = null;
		MotorBusqueda motor      = null;
		Entity factura           = null;
		try {
			accion= (EAccion) this.attrs.get("accion");
			if(!(accion.equals(EAccion.CONSULTAR)||accion.equals(EAccion.MODIFICAR))){
				params.put("idVenta", seleccion.getKey());
				this.setDomicilio(new Domicilio());
				this.attrs.put("registroCliente", new TcManticClientesDto());
				if(!seleccion.getKey().equals(-1L)){	
					this.unlockVentaExtends(seleccion.getKey(), (Long)this.attrs.get("ticketLock"));
					this.attrs.put("ticketLock", seleccion.getKey());
					this.attrs.put("ticket", (Entity)seleccion);
					this.setAdminOrden(new AdminGarantia((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params), false, EAccion.LISTAR, -1L));
					this.loadMediosPago(seleccion.getKey());
					this.ticketOriginal= (TicketVenta) getAdminOrden().getOrden();
					this.attrs.put("tipoVenta", this.ticketOriginal.getIdVentaEstatus().equals(EEstatusVentas.CREDITO.getIdEstatusVenta()) ? 1L : 2L);
					this.attrs.put("mostrarGarantia", this.ticketOriginal.getIdVentaEstatus().equals(EEstatusVentas.CREDITO.getIdEstatusVenta()));
					this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
					this.loadCatalog();
					this.doAsignaClienteTicketAbierto();
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
					this.unlockVentaExtends(-1L, (Long)this.attrs.get("ticketLock"));
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
				this.doAsignaClienteTicketAbierto();
			UIBackingUtilities.execute("jsArticulos.initArrayArt(" + String.valueOf(getAdminOrden().getArticulos().size()-1) + ");");
			this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // toFindOpenTicket
	
	private void loadMediosPago(Long idVenta) {
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
	
  @Override
	protected void doAsignaClienteTicketAbierto() throws Exception {		
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
		Map<String, Object>params = new HashMap<>();
		List<Columna> columns     = new ArrayList<>();
		EAccion accion            = null;
		try {
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
      
      // POR ALGUNA RAZÓN ESTE IMPORTE NO SE ASIGNA Y ESTA EN CEROS CUANDO DEBERIA DE TENER EL VALOR TOTAL DEL ARTICULO (09/01/2023)
      // SOLO APLICA PARA LAS DEVOLUCIONES, POR EL MOMENTO NO HE DETERMINADO LA CAUSA PERO NO MODIFICO LA INTERFAZ PARA NO AFECTAR
      // A OTRO PROCESO 
      this.getAdminOrden().getArticulos().get(index).setTotal(Numero.toRedondearSat(this.getAdminOrden().getArticulos().get(index).getImporte()));	
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
		Map<String, Object> params = new HashMap<>();
		List<Columna> columns      = new ArrayList<>();
		try {
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			bancos= UIEntity.build("TcManticBancosDto", "row", params, columns, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("bancos", bancos);
			this.attrs.put("banco", bancos.get(0));
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
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put(Constantes.SQL_CONDICION, this.toCondicionOpenTicket(cobroCaja));
			columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));			
      List<UISelectEntity> opens= (List<UISelectEntity>) UIEntity.build("VistaVentasDto", "lazy", params, columns, 20L);
      this.attrs.put("openTickets", opens);
      if(opens!= null && !opens.isEmpty())
        this.attrs.put("idVenta", opens.get(0).toLong("idVenta"));
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
 
  public void doRefacturar() {
    this.attrs.put("refacturar", true);
    this.doValidarDevolucion();
  }
 
  private Long toCierreCaja() {
    Long regresar             = -1L;
    Map<String, Object> params= new HashMap<>();
    try {      
			List<UISelectEntity> cajas= (List<UISelectEntity>)this.attrs.get("cajas");
      int index= cajas.indexOf((UISelectEntity)this.attrs.get("caja"));
      if(index>= 0)
			  this.attrs.put("caja", cajas.get(index));
      params.put("estatusAbierto", "1, 2");
      params.put("idEmpresa", ((UISelectEntity)this.attrs.get("caja")).toLong("idEmpresa"));
      params.put("idCaja", ((UISelectEntity)this.attrs.get("caja")).getKey());			
      TcManticCierresDto cierre= (TcManticCierresDto) DaoFactory.getInstance().toEntity(TcManticCierresDto.class, "VistaCierresCajasDto", "cierreVigente", params);
      if(cierre!= null)
        regresar= cierre.getIdCierre();
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

  private void lookForGarantia() {
    Map<String, Object> params = new HashMap<>();
    try {      
      params.put("idVenta", (Long)this.attrs.get("idVenta"));
      Entity entity = (Entity)DaoFactory.getInstance().toEntity("TcManticGarantiasDto", "venta", params);
      this.attrs.put("error", entity!= null && !entity.isEmpty());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
 
  public void doLoadGarantia() {
    List<Columna> columns     = new ArrayList<>();
	  Map<String, Object> params= new HashMap();	
    try {
			params.put("sortOrder", "order by tc_mantic_garantias.registro desc");
			params.put("idVenta", this.attrs.get("idVenta"));
      columns.add(new Columna("impuestos", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("subTotal", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("total", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));    
			this.lazyModel = new FormatCustomLazy("VistaTcManticVentasDetallesDto", "garantia", params, columns);
      UIBackingUtilities.resetDataTable();		
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally		   
  }
  
	public void doRowToggle(ToggleEvent event) {
		try {
			this.attrs.put("garantia", (Entity) event.getData());
			if (!event.getVisibility().equals(Visibility.HIDDEN)) 
				this.doLoadDetalle();
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doRowToggle
  
  public void doLoadDetalle() {
    List<Columna> columns     = new ArrayList<>();
	  Map<String, Object> params= new HashMap();	
    try {
			Entity entity= (Entity)this.attrs.get("garantia");
			params.put("sortOrder", "order by tc_mantic_garantias_detalles.registro");
			params.put("idGarantia", entity.toLong("idGarantia"));
      columns.add(new Columna("cantidad", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("impuestos", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("subTotal", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("importe", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));    
			this.lazyModelDetalle = new FormatCustomLazy("VistaTcManticVentasDetallesDto", "garantiaDetalle", params, columns);
      UIBackingUtilities.resetDataTable("detalle");		
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally		   
  }  
  
}
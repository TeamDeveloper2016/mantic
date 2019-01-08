package mx.org.kaana.mantic.ventas.caja.backing;

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
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.beans.ContadoresListas;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.db.dto.TcManticApartadosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import mx.org.kaana.mantic.ventas.caja.reglas.CreateTicket;
import mx.org.kaana.mantic.ventas.comun.IBaseVenta;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@Named(value= "manticVentasCajaAccion")
@ViewScoped
public class Accion extends IBaseVenta implements Serializable {

	private static final Logger LOG                 = Logger.getLogger(Accion.class);
  private static final long serialVersionUID      = 327393488565639367L;
	private static final String CLAVE_VENTA_GRAL    = "VENTA";
	private static final String GASTOS_GENERAL_CLAVE= "G03";	
	private List<ClienteTipoContacto> clientesTiposContacto;
	private ClienteTipoContacto clienteTipoContactoSeleccion;
	private FormatCustomLazy lazyModelTicket;
	private EOrdenes tipoOrden;	
	private TcManticApartadosDto apartado;
	private boolean pagar;
	
	public Accion() {
		super("menudeo");
	}
	
	public String getTitulo() {
		return "(".concat(tipoOrden.name()).concat(")");
	}

	public EOrdenes getTipoOrden() {
		return tipoOrden;
	}
	
	public List<ClienteTipoContacto> getClientesTiposContacto() {
		return clientesTiposContacto;
	}

	public void setClientesTiposContacto(List<ClienteTipoContacto> clientesTiposContacto) {
		this.clientesTiposContacto = clientesTiposContacto;
	}

	public TcManticApartadosDto getApartado() {
		return apartado;
	}

	public void setApartado(TcManticApartadosDto apartado) {
		this.apartado = apartado;
	}	

  public boolean getPagar() {
		return pagar;
	}	

	public FormatCustomLazy getLazyModelTicket() {
		return lazyModelTicket;
	}	
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null ? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null ? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null ? -1L: JsfBase.getFlashAttribute("idVenta"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null ? null : JsfBase.getFlashAttribute("retorno"));
			LOG.warn("Flash atributes [accion[" + this.attrs.get("accion") + "] idVenta [" + this.attrs.get("idVenta") + "] retorno [" + this.attrs.get("retorno") + "]]");
			this.attrs.put("sortOrder", "order by tc_mantic_ventas.registro desc");
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
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
			this.attrs.put("busquedaTicketAbierto", "");
			this.pagar= false;
			this.attrs.put("activeApartado", false);			
			this.attrs.put("apartado", false);			
			this.attrs.put("observaciones", "");		
			this.attrs.put("disabledFacturar", false);
			this.attrs.put("ajustePreciosCliente", true);			
			this.apartado= new TcManticApartadosDto();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				loadSucursales();							
			loadCajas();
			doLoadTicketAbiertos();			
			loadBancos();
			loadCfdis();
			verificaLimiteCaja();
			doActivarCliente();
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
			LOG.warn("Inicializando admin orden.");
			LOG.warn("Accion:" + eaccion.name());
      switch (eaccion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminTickets(new TicketVenta(-1L)));
          break;
        case MODIFICAR:			
        case CONSULTAR:			
					LOG.warn("Atributes:" + this.attrs.toString());
          this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", this.attrs)));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
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
    Transaccion transaccion        = null;
    String regresar                = null;
		Boolean validarCredito         = true;
		Boolean creditoVenta           = null;
		CreateTicket ticket            = null;
		VentaFinalizada ventaFinalizada= null;
    try {	
			creditoVenta= (Boolean) this.attrs.get("creditoVenta");
			if(creditoVenta)
				validarCredito= doValidaCreditoVenta();
			if(validarCredito){
				ventaFinalizada= loadVentaFinalizada();
				transaccion = new Transaccion(ventaFinalizada);
				if (transaccion.ejecutar(EAccion.REPROCESAR)) {
					ticket= new CreateTicket(((AdminTickets)getAdminOrden()), (Pago) this.attrs.get("pago"), ventaFinalizada.getApartado() ? "APARTADO" : "VENTA DE MOSTRADOR");
					RequestContext.getCurrentInstance().execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + ((TicketVenta)(((AdminTickets)getAdminOrden()).getOrden())).getTicket() + "','" + ticket.toHtml() + "');");
					RequestContext.getCurrentInstance().execute("jsTicket.clicTicket();");
					JsfBase.addMessage("Se finalizo el pago del ticket de venta.", ETipoMensaje.INFORMACION);
					this.setAdminOrden(new AdminTickets(new TicketVenta()));
					this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
					this.attrs.put("clienteSeleccion", null);
					init();
				} // if
				else 
					JsfBase.addMessage("Ocurrió un error al registrar el ticket de venta.", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage(this.attrs.get("mensajeErrorCredito").toString(), ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion
	
	public void doVerificaArticulosCotizacion(){
		try {
			if(!getAdminOrden().getArticulos().isEmpty() && getAdminOrden().getArticulos().size()> 0){
				RequestContext.getCurrentInstance().execute("janal.bloquear();");
				RequestContext.getCurrentInstance().execute("PF('dlgCotizacion').show();");
			} // if
			else{
				JsfBase.addMessage("Cotización", "No es posible generar una cotización sin articulos", ETipoMensaje.ERROR);
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doVerificaArticulosCotizacion
 	
  public String doAceptarCotizacion() {	  
    Transaccion transaccion= null;
    String regresar        = null;
		CreateTicket ticket    = null;
    try {							
			transaccion = new Transaccion((TicketVenta)this.getAdminOrden().getOrden());
			if (transaccion.ejecutar(EAccion.MODIFICAR)) {
				((TicketVenta)(((AdminTickets)getAdminOrden()).getOrden())).setCotizacion(transaccion.getCotizacion());
				ticket= new CreateTicket(((AdminTickets)getAdminOrden()), (Pago) this.attrs.get("pago"), "COTIZACIÓN");				
				RequestContext.getCurrentInstance().execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + transaccion.getCotizacion() + "','" + ticket.toHtml() + "');");
				RequestContext.getCurrentInstance().execute("jsTicket.clicTicket();");
				JsfBase.addMessage("Se finalizo la cotización del ticket.", ETipoMensaje.INFORMACION);								
				this.setAdminOrden(new AdminTickets(new TicketVenta()));
				this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
				this.attrs.put("clienteSeleccion", null);
				init();
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al generar la cotización.", ETipoMensaje.ERROR);			     			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

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
		UISelectEntity ticketAbierto          = null;
		List<UISelectEntity> clientes         = null;
		List<UISelectEntity> clientesSeleccion= null;
		Transaccion transaccion               = null;		
		try {
			ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			if(seleccion!= null && ((TicketVenta)this.getAdminOrden().getOrden()).isValid()){
				transaccion= new Transaccion(((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta(), seleccion.getKey());
				if(transaccion.ejecutar(EAccion.ASIGNAR)){
					doLoadTicketAbiertos();
					this.attrs.put("ticketAbierto", ticketAbierto);
					doAsignaTicketAbiertoCambioCliente();					
					this.attrs.put("tabIndex", 1);
				} // if
				else
					JsfBase.addMessage("no fue posible modificar el cliente a la venta", ETipoMensaje.ERROR);				
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
		
	public void doLoadTicketAbiertosPrincipal(){		
		Map<String, Object>params= null;
		List<Columna> campos     = null;
		try {
			campos= new ArrayList<>();
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));			
			campos.add(new Columna("cuenta", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, toCondicion());
			this.lazyCuentasAbiertas= new FormatLazyModel("VistaVentasDto", "lazy", params, campos);			
			RequestContext.getCurrentInstance().execute("PF('dlgOpenTickets').show();");			
			UIBackingUtilities.resetDataTable("tablaTicketsAbiertos");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally		
	} // doLoadTicketAbiertosPrincipal
	
	@Override
	public void doLoadTicketAbiertos(){
		List<UISelectEntity> ticketsAbiertos= null;
		Map<String, Object>params           = null;
		List<Columna> campos                = null;
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
			this.attrs.put("ticketAbierto", new UISelectEntity("-1"));
			this.setAdminOrden(new AdminTickets(new TicketVenta()));
			this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			this.attrs.put("pagarVenta", false);
			this.attrs.put("facturarVenta", false);
			this.attrs.put("cobroVenta", false);
			this.attrs.put("clienteAsignado", false);
			this.attrs.put("tabIndex", 0);
			setDomicilio(new Domicilio());
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
	
	private String toCondicion(){
		StringBuilder regresar= null;
		Date fecha            = null;
		try {
			fecha= (Date) this.attrs.get("fecha");
			regresar= new StringBuilder();
			regresar.append(" date_format (tc_mantic_ventas.registro, '%Y%m%d')=".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, fecha)));
			regresar.append(" and tc_mantic_ventas.id_venta_estatus in (");
			regresar.append(EEstatusVentas.ELABORADA.getIdEstatusVenta());									
			regresar.append(" , ");
			regresar.append(EEstatusVentas.ABIERTA.getIdEstatusVenta());									
			regresar.append(")");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicion
	
	public void doAsignaTicketAbiertoDlg(){
		List<UISelectItem> ticketsAbiertos= null;
		Map<String, Object>params         = null;
		List<String> fields               = null;
		try {
			fields= new ArrayList<>();
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			fields.add("consecutivo");
			fields.add("cuenta");
			fields.add("precioTotal");
			fields.add("cliente");
			params.put(Constantes.SQL_CONDICION, toCondicion());
			ticketsAbiertos= UISelect.build("VistaVentasDto", "lazy", params, fields, " - ", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
			if(!ticketsAbiertos.isEmpty()){
				this.attrs.put("ticketsAbiertosDlg", ticketsAbiertos);
				if(!ticketsAbiertos.isEmpty())
					this.attrs.put("ticketAbiertoDlg", UIBackingUtilities.toFirstKeySelectItem(ticketsAbiertos));
				RequestContext.getCurrentInstance().execute("PF('dlgOpenTickets').show();");
			} // if
			else{
				JsfBase.addMessage("Cuentas", "Actualmente no hay cuentas abiertas", ETipoMensaje.INFORMACION);
				RequestContext.getCurrentInstance().execute("janal.desbloquear();");
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doAsignaTicketAbiertoDlg
	
	public void doAsignaTicketAbiertoDirecto(){
		try {
			this.attrs.put("ajustePreciosCliente", true);			
			this.attrs.put("ticketAbierto", new UISelectEntity((Entity)this.attrs.get("selectedCuentaAbierta")));
			doAsignaTicketAbierto();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAsignaTicketAbiertoDirecto
	
	public void doAsignaTicketAbiertoCambioCliente(){
		try {
			this.attrs.put("ajustePreciosCliente", false);
			doAsignaTicketAbierto();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAsignaTicketAbiertoCambioCliente
	
	@Override
	public void doAsignaTicketAbierto() {
		Map<String, Object>params           = null;		
		UISelectEntity ticketAbierto        = null;
		UISelectEntity ticketAbiertoPivote  = null;
		List<UISelectEntity> ticketsAbiertos= null;
		try {			
			ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");
			params= new HashMap<>();
			params.put("idVenta", ticketAbierto!= null? ticketAbierto.getKey(): -1L);
			setDomicilio(new Domicilio());
			this.attrs.put("registroCliente", new TcManticClientesDto());
			if(ticketAbierto!= null && !ticketAbierto.getKey().equals(-1L)){				
				ticketsAbiertos= (List<UISelectEntity>) this.attrs.get("ticketsAbiertos");
				ticketAbiertoPivote= ticketsAbiertos.get(ticketsAbiertos.indexOf(ticketAbierto));
				this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params), true));
				this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
				loadCatalog();
				doAsignaClienteTicketAbierto();
				this.attrs.put("pagarVenta", true);
				this.attrs.put("cobroVenta", true);				
				this.attrs.put("tabIndex", 0);
				this.attrs.put("creditoCliente", ticketAbiertoPivote.toLong("idCredito").equals(1L));
			} // if
			else{				
				this.setAdminOrden(new AdminTickets(new TicketVenta()));
				this.attrs.put("pagarVenta", false);
				this.attrs.put("facturarVenta", false);
				this.attrs.put("cobroVenta", false);
				this.attrs.put("clienteAsignado", false);
				this.attrs.put("tabIndex", 0);
				this.attrs.put("creditoCliente", false);				
			} // else			
			validaFacturacion();
			RequestContext.getCurrentInstance().execute("jsArticulos.initArrayArt(" + String.valueOf(getAdminOrden().getArticulos().size()-1) + ");");
			this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			doActivarCliente();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doAsignaTicketAbiertoGeneral
	
	private void doAsignaClienteTicketAbierto() throws Exception{		
		MotorBusqueda motorBusqueda           = null;
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		try {
			motorBusqueda= new MotorBusqueda(-1L, ((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente());
			seleccion= new UISelectEntity(motorBusqueda.toCliente());
			this.attrs.put("clienteAsignado", !seleccion.toString("clave").equals(CLAVE_VENTA_GRAL));
			this.attrs.put("nombreCliente", seleccion.toString("razonSocial"));
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			if((Boolean)this.attrs.get("ajustePreciosCliente")){
				setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
				doReCalculatePreciosArticulos(seleccion.getKey());			
			} // if
		} // try
		catch (Exception e) {	
			throw e;
		} // catch		
	} // doAsignaClienteTicketAbierto
	
	private void loadCajas(){
		List<UISelectEntity> cajas= null;
		Map<String, Object>params = null;
		List<Columna> columns     = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
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
	
	public void doActivarCliente(){		
		UISelectEntity cliente                = null;
		UISelectEntity seleccionado           = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motor                   = null;
		ClienteTipoContacto contactoNuevo     = null;
		try {			
			cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");	
			if(cliente!= null){
				clientesSeleccion= (List<UISelectEntity>) this.attrs.get("clientesSeleccion");
				seleccionado= clientesSeleccion.get(clientesSeleccion.indexOf(cliente));
				if(!seleccionado.toString("clave").equals(CLAVE_VENTA_GRAL)){
					doAsignaDomicilioClienteInicial(seleccionado.getKey());
					motor= new MotorBusqueda(-1L, seleccionado.getKey());
					this.clientesTiposContacto= motor.toCorreosCliente();
					this.attrs.put("telefono", motor.toTelefonoCliente());
					this.attrs.put("celular", motor.toCelularCliente());
				} // if
				else{
					setDomicilio(new Domicilio());
					loadDefaultCollections();					
					this.attrs.put("registroCliente", new TcManticClientesDto());
					this.clientesTiposContacto= new ArrayList<>();
					contactoNuevo= new ClienteTipoContacto();
					contactoNuevo.setSqlAccion(ESql.INSERT);
					this.attrs.put("telefono", contactoNuevo);
					this.attrs.put("celular", contactoNuevo);
				} // else
			} // if
			else{
				setDomicilio(new Domicilio());
				loadDefaultCollections();					
				this.attrs.put("registroCliente", new TcManticClientesDto());
				this.clientesTiposContacto= new ArrayList<>();
				contactoNuevo= new ClienteTipoContacto();
				contactoNuevo.setSqlAccion(ESql.INSERT);
				this.attrs.put("telefono", contactoNuevo);
				this.attrs.put("celular", contactoNuevo);
			} // else				
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActivarCliente
	
	public void doActiveApartado(){
		boolean apartado= true;
		try {
			apartado= (boolean) this.attrs.get("apartado");			
			this.attrs.put("facturarVenta", !apartado);
			this.attrs.put("disabledFacturar", apartado);			
			this.attrs.put("tabIndex", 1);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doActiveApartado
	
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
	
	public void doValidaCreditoCliente(){
		Boolean credito   = false;
		Boolean apartado  = false;
		RequestContext rc = null;
		Double minApartado= null;
		try {
			credito= (Boolean) this.attrs.get("creditoVenta");
			apartado= (Boolean) this.attrs.get("apartado");
			rc= RequestContext.getCurrentInstance();
			if(apartado){
				minApartado= (getAdminOrden().getTotales().getTotal() * Constantes.ANTICIPO)/100;
				rc.execute("jsArticulos.validateApartado(" + minApartado + ");");
			} // if
			else if(credito)
				rc.execute("jsArticulos.validateCredito();");
			else
				rc.execute("jsArticulos.refreshCobroValidate();");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doValidaCreditoCliente
	
	public void doAgregarClienteTipoContacto(){
		ClienteTipoContacto clienteTipoContacto= null;
		ContadoresListas contadores            = null;
		Long contador                          = 0L;
		try {					
			contador= (Long) this.attrs.get("contador");
			contadores= new ContadoresListas();
			clienteTipoContacto= new ClienteTipoContacto(contadores.getTotalClientesTipoContacto() + contador, ESql.INSERT, true);				
			clienteTipoContacto.setOrden(this.clientesTiposContacto.size() + 1L);
			clienteTipoContacto.setIdTipoContacto(ETiposContactos.CORREO.getKey());
			this.clientesTiposContacto.add(clienteTipoContacto);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
		finally{
			contador++;
			this.attrs.put("contador", contador);
		} // finally
	} // doAgregarClienteTipoContacto
	
	public void doEliminarClienteTipoContacto(){
		try {			
			if(this.clientesTiposContacto.remove(this.clienteTipoContactoSeleccion)){
				if(!this.clienteTipoContactoSeleccion.getNuevo())
					addDeleteList(this.clienteTipoContactoSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el tipo de contacto", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el tipo de contacto", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarClienteTipoContacto
	
	private void addDeleteList(IBaseDto dto) throws Exception{
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(dto);
			transaccion.ejecutar(EAccion.DEPURAR);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // addDeleteList
	
	private VentaFinalizada loadVentaFinalizada(){
		VentaFinalizada regresar  = null;
		TicketVenta ticketVenta   = null;
		List<UISelectEntity> cfdis= null;
		UISelectEntity cfdi       = null;
		Boolean facturarVenta     = false;
		try {
			this.getAdminOrden().toCalculate();
			ticketVenta= (TicketVenta)this.getAdminOrden().getOrden();
			ticketVenta.setTotal(this.getAdminOrden().getTotales().getTotal());
			ticketVenta.setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			ticketVenta.setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
			ticketVenta.setImpuestos(this.getAdminOrden().getTotales().getIva());
			ticketVenta.setUtilidad(this.getAdminOrden().getTotales().getUtilidad());
			facturarVenta= (Boolean) this.attrs.get("facturarVenta");
			if(facturarVenta){
				cfdis= (List<UISelectEntity>) this.attrs.get("cfdis");
				cfdi= (UISelectEntity) this.attrs.get("cfdi");
				ticketVenta.setIdUsoCfdi(cfdis.get(cfdis.indexOf(cfdi)).getKey());
			} // if
			regresar= new VentaFinalizada();
			regresar.setTicketVenta(ticketVenta);
			regresar.setCorreosContacto(this.clientesTiposContacto);
			regresar.setCelular((ClienteTipoContacto) this.attrs.get("celular"));
			regresar.setTelefono((ClienteTipoContacto) this.attrs.get("telefono"));
			regresar.setTotales((Pago) this.attrs.get("pago"));
			regresar.setCliente((TcManticClientesDto) this.attrs.get("registroCliente"));
			regresar.setDomicilio(getDomicilio());
			regresar.setFacturar(facturarVenta);
			regresar.setCredito((Boolean) this.attrs.get("creditoVenta"));			
			regresar.setArticulos(getAdminOrden().getArticulos());
			regresar.setIdCaja(Long.valueOf(this.attrs.get("caja").toString()));
			regresar.setApartado((Boolean) this.attrs.get("apartado"));
			regresar.setDetailApartado(this.apartado);
			regresar.setObservaciones(this.attrs.get("observaciones").toString());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadVentaFinalizada
	
	private void loadCfdis(){
		List<UISelectEntity> cfdis  = null;
		UISelectEntity cfdiSeleccion= null;
		List<Columna> campos        = null;
		Map<String, Object>params   = null;
		try {
			params= new HashMap<>();
			campos= new ArrayList<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			cfdis= UIEntity.build("TcManticUsosCfdiDto", "row", params, campos, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("cfdis", cfdis);
			for(UISelectEntity record: cfdis){
				if(record.toString("clave").equals(GASTOS_GENERAL_CLAVE))
					cfdiSeleccion= record;
			} // for
			this.attrs.put("cfdi", cfdiSeleccion);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadCfdis
	
	private boolean doValidaCreditoVenta() throws Exception, Exception{
		boolean regresar               = true;
		TcManticVentasDto venta        = null;
		MotorBusqueda motor            = null;
		Double totalCredito            = null;
		Pago pago                      = null;
		List<UISelectEntity>clientes   = null;
		UISelectEntity clienteSeleccion= null;
		UISelectEntity cliente         = null;
		try {
			motor= new MotorBusqueda(null);
			venta= motor.toVenta(((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
			if(!EBooleanos.SI.getIdBooleano().equals(venta.getIdAutorizar())){
				pago= (Pago) this.attrs.get("pago");				
				totalCredito= getAdminOrden().getTotales().getTotal() - (pago.getPago() - pago.getCambio());
				clientes= (List<UISelectEntity>) this.attrs.get("clientesSeleccion");
				cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");
				clienteSeleccion= clientes.get(clientes.indexOf(cliente));
				regresar= totalCredito <= (clienteSeleccion.toDouble("limiteCredito") - clienteSeleccion.toDouble("saldo"));
				if(!regresar)
					this.attrs.put("mensajeErrorCredito", "El saldo de tu credito es insuficiente para cubrir la venta.");
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // doValidaCreditoVenta
	
	private void verificaLimiteCaja() throws Exception{
		Entity alerta            = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put("idCaja", this.attrs.get("caja"));
			alerta= (Entity) DaoFactory.getInstance().toEntity("VistaCierresCajasDto", "alerta", params);
			if(alerta!= null){
				RequestContext.getCurrentInstance().execute("janal.bloquear();");
				RequestContext.getCurrentInstance().execute("PF('dlgLimiteCaja').show();");
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		}	// finally	
	} // verificaLimiteCaja
	
	public void doTabChange(TabChangeEvent event) {
		this.pagar= event.getTab().getTitle().equals("Pagar");
		if(event.getTab().getTitle().equals("Tickets"))
			doLoadTickets();
	}

	public void doAplicarCambioPrecio(){
		doAplicarCambioPrecio(-1);
	}
	
	public void doAplicarCambioPrecio(Integer index){
		CambioUsuario cambioUsuario= null;
		String cuenta              = null;
		String contrasenia         = null;
		try {
			if(!getAdminOrden().getArticulos().isEmpty()){
				cuenta= this.attrs.get("cambioPrecioUsr").toString();
				contrasenia= this.attrs.get("passwordCambioPrecio").toString();
				cambioUsuario= new CambioUsuario(cuenta, contrasenia);
				if(cambioUsuario.validaPrivilegiosDescuentos()){					
						getAdminOrden().getArticulos().get(index).setCosto(Double.valueOf(this.attrs.get("cambioPrecio").toString()));												
				} // if
				else
					JsfBase.addMessage("El usuario no tiene privilegios o el usuario y la contraseña son incorrectos", ETipoMensaje.ERROR);
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{			
			this.attrs.put("cambioPrecioUsr", "");
			this.attrs.put("passwordCambioPrecio", "");
		} // finally
	} // doAplicarCambioPrecio
	
	public void doLoadTickets(){
		List<Columna> columns     = null;
		Map<String, Object> params= null;
		try {
			params= toPrepare();
      columns = new ArrayList<>();
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      columns.add(new Columna("hora", EFormatoDinamicos.HORA_CORTA));      			
      this.lazyModelTicket = new FormatCustomLazy("VistaConsultasDto", params, columns);
      UIBackingUtilities.resetDataTable("tablaTicket");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch				
	} // doLoadTickets
	
	private Map<String, Object> toPrepare(){
		Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();				
		if(!Cadena.isVacio(this.attrs.get("busquedaTicket")) && !this.attrs.get("busquedaTicket").toString().equals("-1")){
			sb.append("(upper(concat(tc_mantic_personas.nombres, ' ', tc_mantic_personas.paterno, ' ', tc_mantic_personas.materno)) like upper('%").append(this.attrs.get("busquedaTicket")).append("%') or ");					
			sb.append("upper(tc_mantic_ventas_detalles.nombre) like upper('%").append(this.attrs.get("busquedaTicket")).append("%') or ");					
			sb.append("upper(tc_mantic_clientes.razon_social) like upper('%").append(this.attrs.get("busquedaTicket")).append("%')").append(" or upper(tc_mantic_clientes.razon_social) like upper('%").append(this.attrs.get("busquedaTicket")).append("%') or ");
			sb.append("(tc_mantic_ventas.consecutivo like '%").append(this.attrs.get("busquedaTicket")).append("%'))");
			sb.append(" and tc_mantic_ventas.id_venta_estatus in (").append(EEstatusVentas.PAGADA.getIdEstatusVenta()).append(",").append(EEstatusVentas.TERMINADA.getIdEstatusVenta()).append(")");
		} // if
		regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb);
		return regresar;		
	} // toPrepare
	
	public void doPrintTicket(){
		Entity seleccionado      = null;
		Map<String, Object>params= null;
		CreateTicket ticket      = null;
		AdminTickets adminTicket = null;
		try {			
			seleccionado= (Entity) this.attrs.get("seleccionTicket");
			params= new HashMap<>();
			params.put("idVenta", seleccionado.toLong("idVenta"));
			adminTicket= new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params));			
			ticket= new CreateTicket(adminTicket, toPago(adminTicket, seleccionado.getKey()), toTipoTransaccion(seleccionado.toLong("idVentaEstatus")));
			RequestContext.getCurrentInstance().execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + toConsecutivoTicket(seleccionado.toLong("idVentaEstatus"), adminTicket) + "','" + ticket.toHtml() + "');");
			RequestContext.getCurrentInstance().execute("jsTicket.clicTicket();");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch		
	} // doPrintTicket
	
	private String toTipoTransaccion(Long idEstatus){
		String regresar       = null;
		EEstatusVentas estatus= null;
		try {
			estatus= EEstatusVentas.fromIdTipoPago(idEstatus);
			switch(estatus){
				case PAGADA:
				case TERMINADA:
					regresar= "VENTA DE MOSTRADOR";
				break;
				case COTIZACION:
					regresar= "COTIZACIÓN";
					break;
				case APARTADO:
					regresar= "APARTADO";
					break;
			} // switch			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toTipoTransaccion
	
	private String toConsecutivoTicket(Long idEstatus, AdminTickets ticket){
		String regresar       = null;
		EEstatusVentas estatus= null;
		try {
			estatus= EEstatusVentas.fromIdTipoPago(idEstatus);
			if(estatus.equals(EEstatusVentas.TERMINADA) || estatus.equals(EEstatusVentas.APARTADO))
				regresar= ((TicketVenta)(ticket.getOrden())).getTicket();
			else
				regresar= ((TicketVenta)(ticket.getOrden())).getCotizacion();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toTipoTransaccion
	
	private Pago toPago(AdminTickets adminTicket, Long idVenta) throws Exception{
		Pago regresar            = null;
		List<Entity> detallePago = null;
		Map<String, Object>params= null;
		ETipoMediosPago medioPago= null;
		try {
			regresar= new Pago(adminTicket.getTotales());
			params= new HashMap<>();
			params.put("idVenta", idVenta);
			detallePago= DaoFactory.getInstance().toEntitySet("TrManticVentaMedioPagoDto", "ticket", params, Constantes.SQL_TODOS_REGISTROS);
			if(!detallePago.isEmpty()){
				for(Entity pago: detallePago){
					medioPago= ETipoMediosPago.fromIdTipoPago(pago.toLong("idTipoMedioPago"));
					switch(medioPago){
						case EFECTIVO:
							regresar.setEfectivo(pago.toDouble("importe"));							
							break;
						case TARJETA_CREDITO:
							regresar.setCredito(pago.toDouble("importe"));							
							regresar.setReferenciaCredito(pago.toString("referencia"));							
							break;
						case CHEQUE:
							regresar.setCheque(pago.toDouble("importe"));							
							regresar.setReferenciaCheque(pago.toString("referencia"));							
							break;
						case TRANSFERENCIA:
							regresar.setTransferencia(pago.toDouble("importe"));							
							regresar.setReferenciaTransferencia(pago.toString("referencia"));							
							break;
					} // switch
				} // for
			} // if
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // toPago
	
	private void validaFacturacion() throws Exception{
		List<Entity> ticketsAbiertos= null;
		Entity ticketAbierto        = null;
		MotorBusqueda motor         = null;
		Entity cliente              = null;		
		try {
			motor= new MotorBusqueda(-1L);
			cliente= motor.toClienteDefault();
			ticketsAbiertos= (List<Entity>) this.attrs.get("ticketsAbiertos");
			this.attrs.put("disabledFacturar", false);
			if(!ticketsAbiertos.isEmpty()){
				ticketAbierto= (Entity) this.attrs.get("ticketAbierto");
				if(ticketAbierto!= null)
					this.attrs.put("disabledFacturar", cliente.getKey().equals(ticketsAbiertos.get(ticketsAbiertos.indexOf(ticketAbierto)).toLong("idCliente")));
			} // if			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // validaFacturacion
	
	@Override
	public void doAsignaCotizacion(){		
		Transaccion transaccion= null;
		try {			
			transaccion= new Transaccion(new TcManticVentasDto(Long.valueOf(this.attrs.get("cotizacion").toString())));			
			if (transaccion.ejecutar(EAccion.AGREGAR)){
    		super.doAsignaCotizacion();						
				doLoadTicketAbiertos();
				this.attrs.put("ajustePreciosCliente", true);			
				this.attrs.put("ticketAbierto", new UISelectEntity(new Entity(Long.valueOf(this.attrs.get("cotizacion").toString()))));
				doAsignaTicketAbierto();
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la cuenta de venta.", ETipoMensaje.ERROR);      			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAsignaCotizacion		        
}
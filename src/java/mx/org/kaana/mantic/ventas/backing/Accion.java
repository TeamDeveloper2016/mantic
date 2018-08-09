package mx.org.kaana.mantic.ventas.backing;

import java.io.Serializable;
import java.util.ArrayList;
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
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.ventas.beans.SaldoCliente;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

@Named(value= "manticVentasAccion")
@ViewScoped
public class Accion extends IBaseArticulos implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private static final String VENDEDOR_PERFIL= "VENDEDOR DE PISO";
	private static final String INDIVIDUAL= "1";
	private static final String GLOBAL= "0";
	private EOrdenes tipoOrden;
	private SaldoCliente saldoCliente;
	private StreamedContent image;

	public Accion() {
		super("menudeo");
	}
	
	public String getTitulo() {
		return "(".concat(tipoOrden.name()).concat(")");
	}

	public EOrdenes getTipoOrden() {
		return tipoOrden;
	}

	public SaldoCliente getSaldoCliente() {
		return saldoCliente;
	}	

	public void setSaldoCliente(SaldoCliente saldoCliente) {
		this.saldoCliente = saldoCliente;
	}

	public StreamedContent getImage() {
		return image;
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null? -1L: JsfBase.getFlashAttribute("idVenta"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? null: JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("buscaPorCodigo", false);
			this.attrs.put("activeLogin", false);
			this.attrs.put("isIndividual", true);
			this.attrs.put("descuentoIndividual", 0);
			this.attrs.put("descuentoGlobal", 0);
			this.attrs.put("tipoDescuento", INDIVIDUAL);
			this.image= LoadImages.getImage("-1");
			loadClienteDefault();
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("isMatriz", JsfBase.isAdminEncuestaOrAdmin());
			if(JsfBase.isAdminEncuestaOrAdmin())
				loadSucursales();
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion= null;
		Long idCliente = 3515L;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminTickets(new TicketVenta(-1L)));
					this.saldoCliente= new SaldoCliente();
          break;
        case MODIFICAR:			
        case CONSULTAR:			
          this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", this.attrs)));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
					idCliente= ((TicketVenta)getAdminOrden().getOrden()).getIdCliente();
					if(idCliente!= null && !idCliente.equals(-1L)){
						doAsignaClienteInicial(idCliente);
						doLoadSaldos(idCliente);
					} // if
          break;
      } // switch
			this.attrs.put("consecutivo", "");
			toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;		
    try {			
			loadOrdenVenta();
			eaccion= (EAccion) this.attrs.get("accion");						
			transaccion = new Transaccion(((TicketVenta)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
			this.getAdminOrden().toAdjustArticulos();
			if (transaccion.ejecutar(eaccion)) {
				if(eaccion.equals(EAccion.AGREGAR)) {
 				  regresar = this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR) : null;
    			RequestContext.getCurrentInstance().execute("jsArticulos.back('gener\\u00F3 ticket de venta', '"+ ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
					this.init();
				} // if	
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" el ticket de venta."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
				RequestContext.getCurrentInstance().execute("userUpdate();");
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el ticket de venta.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
    return this.attrs.get("retorno") != null ? (String)this.attrs.get("retorno") : "filtro";
  } // doCancelar

	private void toLoadCatalog() {
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
	}

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Articulos")) {
			switch(this.tipoOrden) {
				case NORMAL:
					break;
				case ALMACEN: 
					break;
				case PROVEEDOR:
					break;
			} // switch
		} // if	
	}
  
	public List<UISelectEntity> doCompleteCliente(String query) {
		this.attrs.put("codigoCliente", query);
    this.doUpdateClientes();		
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	// doCompleteCliente

	public void doUpdateClientes() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idEmpresa", this.attrs.get("idEmpresa"));
			String search= (String) this.attrs.get("codigoCliente"); 
			search= !Cadena.isVacio(search) ? search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*") : "WXYZ";
  		params.put(Constantes.SQL_CONDICION, "upper(tc_mantic_clientes.razon_social) regexp '.*".concat(search).concat(".*'").concat(" or upper(tc_mantic_clientes.rfc) regexp '.*".concat(search).concat(".*'")));			
      this.attrs.put("clientes", (List<UISelectEntity>) UIEntity.build("VistaClientesDto", "findRazonSocial", params, columns, 20L));
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
	
	public void doAsignaCliente(SelectEvent event){
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientes         = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null;
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			motorBusqueda= new MotorBusqueda(-1L);
			clientesSeleccion.add(0, new UISelectEntity(motorBusqueda.toClienteDefault()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
			doReCalculatePreciosArticulos(seleccion.getKey());		
			doLoadSaldos(seleccion.getKey());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	public void doAsignaClienteInicial(Long idCliente){
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null; 
		try {
			motorBusqueda= new MotorBusqueda(null, idCliente);
			seleccion= new UISelectEntity(motorBusqueda.toCliente());
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			clientesSeleccion.add(0, new UISelectEntity(motorBusqueda.toClienteDefault()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	public void doReCalculatePreciosArticulos(Long idCliente){
		doReCalculatePreciosArticulos(true, idCliente);
	} // doReCalculatePreciosArticulos
	
	public void doReCalculatePreciosArticulos(boolean descuentoVigente, Long idCliente){
		MotorBusqueda motor          = null;
		TcManticArticulosDto articulo= null;
		String descuento             = null;
		String sinDescuento          = "0";
		try {
			if(!getAdminOrden().getArticulos().isEmpty()){
				for(Articulo beanArticulo: getAdminOrden().getArticulos()){
					if(beanArticulo.getIdArticulo()!= null && !beanArticulo.getIdArticulo().equals(-1L)){
						motor= new MotorBusqueda(beanArticulo.getIdArticulo());
						articulo= motor.toArticulo();
						beanArticulo.setValor((Double) articulo.toValue(getPrecio()));
						beanArticulo.setCosto((Double) articulo.toValue(getPrecio()));
						if(descuentoVigente){
							descuento= toDescuentoVigente(beanArticulo.getIdArticulo(), idCliente);
							beanArticulo.setDescuento(descuento!= null ? descuento : sinDescuento);							
						} // if
						else
							beanArticulo.setDescuento(sinDescuento);
					} // if
				} // for					
				if(getAdminOrden().getArticulos().size()>1){					
					getAdminOrden().toCalculate();
					RequestContext.getCurrentInstance().update("@(.filas) @(.recalculo) @(.informacion)");
				} // if
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doReCalculatePreciosArticulos
	
	public void doActualizaPrecioCliente(){
		List<UISelectEntity> clientesSeleccion= null;
		UISelectEntity clienteSeleccion       = null;
		boolean precioVigente                 = false;
		try {
			clienteSeleccion= (UISelectEntity) this.attrs.get("clienteSeleccion");
			precioVigente= clienteSeleccion!= null && !clienteSeleccion.getKey().equals(-1L);
			if(precioVigente){
				clientesSeleccion= (List<UISelectEntity>) this.attrs.get("clientesSeleccion");
				clienteSeleccion= clientesSeleccion.get(clientesSeleccion.indexOf(clienteSeleccion));
				setPrecio(Cadena.toBeanNameEspecial(clienteSeleccion.toString("tipoVenta")));				
			} // if
			else
				setPrecio("menudeo");
			doReCalculatePreciosArticulos(precioVigente, clienteSeleccion.getKey());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActualizaPrecioCliente

	@Override
	protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		UISelectEntity clienteSeleccion= null;		
		String descuentoPivote         = null;
		String descuentoVigente        = null;		
		try {
			clienteSeleccion= (UISelectEntity) this.attrs.get("clienteSeleccion");
			if(clienteSeleccion!= null && !clienteSeleccion.getKey().equals(-1L)){
				descuentoVigente= toDescuentoVigente(articulo.toLong("idArticulo"), clienteSeleccion.getKey());				
				if(descuentoVigente!= null){
					descuentoPivote= getAdminOrden().getDescuento();
					getAdminOrden().setDescuento(descuentoVigente);
					super.toMoveData(articulo, index);			
					getAdminOrden().setDescuento(descuentoPivote);
				} // if
				else
					super.toMoveData(articulo, index);				
			} // if
			else
				super.toMoveData(articulo, index);	
			this.image= LoadImages.getImage(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString(), articulo.toLong("idArticulo").toString());
			this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
			RequestContext.getCurrentInstance().update("deudor");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
	} // toMoveData
	
	private String toDescuentoVigente(Long idArticulo, Long idCliente) throws Exception{
		MotorBusqueda motorBusqueda= null;
		Entity descuentoVigente    = null;
		String regresar            = null;
		try {
			motorBusqueda= new MotorBusqueda(idArticulo, idCliente);
			descuentoVigente= motorBusqueda.toDescuentoGrupo();
			if(descuentoVigente!= null)
				regresar= descuentoVigente.toString("porcentaje");
		} // try
		catch (Exception e) {			
			throw e;			
		} // catch		
		return regresar;
	} // toDescuentoVigente
	
	private void loadOrdenVenta(){		
		UISelectEntity cliente = null;
		try {
			cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");			
			((TicketVenta)this.getAdminOrden().getOrden()).setIdEmpresa(Long.valueOf(this.attrs.get("idEmpresa").toString()));
			((TicketVenta)this.getAdminOrden().getOrden()).setIdCliente(cliente.getKey());
			((TicketVenta)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
			((TicketVenta)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
			((TicketVenta)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			((TicketVenta)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
	} // loadOrdenVenta
	
	public void doCerrarTicket(){		
		Transaccion transaccion= null;
    try {								
			if(!this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L))))){
				loadOrdenVenta();
				transaccion = new Transaccion(((TicketVenta)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
				this.getAdminOrden().toAdjustArticulos();
				if (transaccion.ejecutar(EAccion.REGISTRAR)) {				
					RequestContext.getCurrentInstance().execute("jsArticulos.back('cerr\\u00F3 ticket de venta', '"+ ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
					JsfBase.addMessage("Se guardo el ticket de venta.", ETipoMensaje.INFORMACION);	
					init();
				} // if
				else 
					JsfBase.addMessage("Ocurrió un error al registrar el ticket de venta.", ETipoMensaje.ERROR);      			
			} // if						
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	} // doCerrarTicket
	
	public void doLogin() {		
		CambioUsuario cambioUsuario= null;		
		String cuenta   = null;
		String password = null;
    try {					
			cuenta       = this.attrs.get("cuenta").toString();
			password     = this.attrs.get("password").toString();						
			cambioUsuario= new CambioUsuario(cuenta, password);
			if(cambioUsuario.validaUsuario()) {
				this.init();
			  RequestContext.getCurrentInstance().execute("jsVentas.disabledLogin();");
			}	// if
			else
				JsfBase.addMessage("Cambio de usuario", "Ocurrió un error al autenticar el usuario seleccionado", ETipoMensaje.ERROR);      																	
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	} // doLogin
	
	public void doLoadTicketAbiertos(){
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
			params.put(Constantes.SQL_CONDICION, toCondicion());
			ticketsAbiertos= UISelect.build("VistaVentasDto", "lazy", params, fields, " - ", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
			if(!ticketsAbiertos.isEmpty()){
				this.attrs.put("ticketsAbiertos", ticketsAbiertos);
				if(!ticketsAbiertos.isEmpty())
					this.attrs.put("ticketAbierto", UIBackingUtilities.toFirstKeySelectItem(ticketsAbiertos));
				RequestContext.getCurrentInstance().execute("PF('dlgOpenTickets').show();");
			} // if
			else{
				JsfBase.addMessage("Tickets", "Actualmente no hay tickets abiertos", ETipoMensaje.INFORMACION);
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
	} // doLoadTicketAbiertos
	
	private String toCondicion() {
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			regresar.append(" date_format(tc_mantic_ventas.registro, '%Y%m%d')= date_format(SYSDATE(), '%Y%m%d')");
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
	
	public void doAsignaTicketAbierto(){
		Map<String, Object>params = null;
		try {
			params= new HashMap<>();
			params.put("idVenta", this.attrs.get("ticketAbierto"));
			this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params)));
    	this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
			this.attrs.put("consecutivo", ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo());
			toLoadCatalog();
			doAsignaClienteTicketAbierto();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doAsignaTicketAbierto
	
	private void doAsignaClienteTicketAbierto() throws Exception{		
		MotorBusqueda motorBusqueda           = null;
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		try {
			motorBusqueda= new MotorBusqueda(-1L, ((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente());
			seleccion= new UISelectEntity(motorBusqueda.toCliente());
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			clientesSeleccion.add(0, new UISelectEntity(motorBusqueda.toClienteDefault()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
			doReCalculatePreciosArticulos(seleccion.getKey());	
			doLoadSaldos(seleccion.getKey());
		} // try
		catch (Exception e) {	
			throw e;
		} // catch		
	} // doAsignaClienteTicketAbierto
	
	public String doClientes(){
		String regresar= null;
		try {
			regresar= "cliente.jsf".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	} // doClientes
	
	public void doDetailArticulo(Long idArticulo, Integer index) {
		MotorBusqueda motor= null;
		Entity detailArt   = null;
		try {
			if(idArticulo!= null){
				motor= new MotorBusqueda(idArticulo);
				detailArt= motor.toDetalleArticulo();
				this.attrs.put("detailArticulo", detailArt);
				RequestContext.getCurrentInstance().execute("PF('dlgDetalleArt').show();");
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doDetailArticulo
	
	public void doLoadUsers(){
		List<UISelectEntity> vendedores= null;
		Map<String, Object>params      = null;
		List<Columna> campos           = null;
		RequestContext rc              = null;
		try {
			campos= new ArrayList<>();
			params= new HashMap<>();
			params.put("idGrupo", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("perfil", VENDEDOR_PERFIL);
			params.put("idUsuario", JsfBase.getIdUsuario());
			campos.add(new Columna("nombreCompleto", EFormatoDinamicos.MAYUSCULAS));
			vendedores= UIEntity.build("VistaTcJanalUsuariosDto", "cambioUsuario", params, campos, Constantes.SQL_TODOS_REGISTROS);
			rc= RequestContext.getCurrentInstance();
			if(!vendedores.isEmpty()){
				this.attrs.put("vendedores", vendedores);
				this.attrs.put("vendedor", UIBackingUtilities.toFirstKeySelectEntity(vendedores));
				rc.execute("PF('dlgCloseTicket').show();");
			} // if
			else{
				JsfBase.addMessage("Cambio de usuario", "No hay mas usuarios con el mismo perfil", ETipoMensaje.INFORMACION);
				rc.execute("janal.desbloquear();");
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
			Methods.clean(campos);
		} // finally
	} // doLoadUsers
	
	private void doLoadSaldos(Long idCliente) throws Exception{
		Entity cliente     = null;
		MotorBusqueda motor= null;
		this.saldoCliente  = null;
		try {
			motor= new MotorBusqueda(null, idCliente);
			cliente= motor.toCliente();
			this.saldoCliente= new SaldoCliente();
			this.saldoCliente.setIdCliente(idCliente);
			this.saldoCliente.setTotalCredito(cliente.toDouble("limiteCredito"));
			this.saldoCliente.setTotalDeuda(motor.toDeudaCliente());
			this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // doLoadSaldos
	
	public void doActualizaImage(String idImage) {
		String idEmpresa= null;
		try {
			idEmpresa= JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString();
			if(!idImage.equals("-1")){
				this.image= LoadImages.getImage(idEmpresa, idImage);
				this.attrs.put("imagePivote", idImage);
			} // if
			else if (getAdminOrden().getArticulos().isEmpty() || (getAdminOrden().getArticulos().size()== 1 && getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L)))
				this.image= LoadImages.getImage(idEmpresa, idImage);
			else
				this.image= LoadImages.getImage(idEmpresa, this.attrs.get("imagePivote").toString());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActualizaImage
	
	@Override
	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.buildImage("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
			else
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.buildImage("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // doUpdateArticulos
	
	private void loadClienteDefault(){
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null;
		try {
			motorBusqueda= new MotorBusqueda(-1L);
			seleccion= new UISelectEntity(motorBusqueda.toClienteDefault());
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);			
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // loadClienteDefault
	
	private void loadSucursales(){
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			sucursales=(List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
			this.attrs.put("sucursales", sucursales);
			this.attrs.put("idEmpresa", sucursales.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
	
	public void doUpdateForEmpresa(){
		try {
			loadClienteDefault();
			doActualizaPrecioCliente();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doUpdateForEmpresa
	
	public void doActivarDescuento(){
		String tipoDescuento= null;		
		try {
			tipoDescuento= this.attrs.get("tipoDescuento").toString();
			this.attrs.put("isIndividual", tipoDescuento.equals(INDIVIDUAL));
			this.attrs.put(tipoDescuento.equals(INDIVIDUAL) ? "descuentoGlobal" : "descuentoIndividual", 0);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActivarDescuento
	
	public void doAplicarDescuento(){
		Boolean isIndividual= false;
		try {
			isIndividual= Boolean.valueOf(this.attrs.get("isIndividual").toString());
			if(isIndividual)
				RequestContext.getCurrentInstance().execute("jsArticulos.divDiscount('".concat(this.attrs.get("descuentoIndividual").toString()).concat("');"));
			else
				getAdminOrden().getTotales().setGlobal(Double.valueOf(this.attrs.get("descuentoGlobal").toString()));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAplicarDescuento
}

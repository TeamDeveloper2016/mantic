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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.beans.ContadoresListas;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.comun.IBaseCliente;
import mx.org.kaana.mantic.db.dto.TcManticApartadosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@Named(value= "manticVentasCajaAccion")
@ViewScoped
public class Accion extends IBaseCliente implements Serializable {

  private static final long serialVersionUID  = 327393488565639367L;
	private static final String CLAVE_VENTA_GRAL= "VENTA";
	private List<ClienteTipoContacto> clientesTiposContacto;
	private ClienteTipoContacto clienteTipoContactoSeleccion;
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
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null ? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null ? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null ? -1L: JsfBase.getFlashAttribute("idVenta"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null ? null : JsfBase.getFlashAttribute("retorno"));
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
			this.pagar= false;
			this.attrs.put("activeApartado", false);
			this.attrs.put("disabledFacturar", false);			
			this.attrs.put("apartado", false);			
			this.apartado= new TcManticApartadosDto();
			if(JsfBase.isAdminEncuestaOrAdmin())
				loadSucursales();							
			loadCajas();
			doLoadTicketAbiertos();			
			loadBancos();
			loadCfdis();
			verificaLimiteCaja();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));			
      switch (eaccion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminTickets(new TicketVenta(-1L)));
          break;
        case MODIFICAR:			
        case CONSULTAR:			
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
    Transaccion transaccion= null;
    String regresar        = null;
		Boolean validarCredito = true;
		Boolean creditoVenta   = null;
    try {	
			creditoVenta= (Boolean) this.attrs.get("creditoVenta");
			if(creditoVenta)
				validarCredito= doValidaCreditoVenta();
			if(validarCredito){
				transaccion = new Transaccion(loadVentaFinalizada());
				if (transaccion.ejecutar(EAccion.REPROCESAR)) {
					regresar = this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR) : null;
					JsfBase.addMessage("Se finalizo el pago del ticket de venta.", ETipoMensaje.INFORMACION);
					this.setAdminOrden(new AdminTickets(new TicketVenta()));
					this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
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
    try {				
			transaccion = new Transaccion((TicketVenta)this.getAdminOrden().getOrden());
			if (transaccion.ejecutar(EAccion.MODIFICAR)) {
				regresar = this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR) : null;
				JsfBase.addMessage("Se genero la cotización del ticket de venta.", ETipoMensaje.INFORMACION);
				this.setAdminOrden(new AdminTickets(new TicketVenta()));
				this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
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

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

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
	
	public void doReCalculatePreciosArticulos(Long idCliente){
		doReCalculatePreciosArticulos(true, idCliente);
	}
	
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
							if(descuento!= null)
								beanArticulo.setDescuento(descuento);							
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
			regresar.append(" DATE_FORMAT(tc_mantic_ventas.registro, '%Y%m%d')=".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, fecha)));
			regresar.append(" and tc_mantic_ventas.id_venta_estatus=");
			regresar.append(EEstatusVentas.ABIERTA.getIdEstatusVenta());									
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicion
	
	public void doAsignaTicketAbierto(){
		Map<String, Object>params           = null;
		UISelectEntity ticketAbierto        = null;
		UISelectEntity ticketAbiertoPivote  = null;
		List<UISelectEntity> ticketsAbiertos= null;
		try {
			ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");
			params= new HashMap<>();
			params.put("idVenta", ticketAbierto.getKey());
			setDomicilio(new Domicilio());
			this.attrs.put("registroCliente", new TcManticClientesDto());
			if(!ticketAbierto.getKey().equals(-1L)){
				ticketsAbiertos= (List<UISelectEntity>) this.attrs.get("ticketsAbiertos");
				ticketAbiertoPivote= ticketsAbiertos.get(ticketsAbiertos.indexOf(ticketAbierto));
				this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params), false));
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
			setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
			doReCalculatePreciosArticulos(seleccion.getKey());			
		} // try
		catch (Exception e) {	
			throw e;
		} // catch		
	} // doAsignaClienteTicketAbierto
	
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
		boolean facturarVenta                 = true;
		UISelectEntity cliente                = null;
		UISelectEntity seleccionado           = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motor                   = null;
		ClienteTipoContacto contactoNuevo     = null;
		try {
			facturarVenta= (Boolean) this.attrs.get("facturarVenta");
			if(facturarVenta){
				cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");		
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
				this.attrs.put("tabIndex", 1);
			} // if
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
			ticketVenta= (TicketVenta)this.getAdminOrden().getOrden();
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
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadVentaFinalizada
	
	private void loadCfdis(){
		List<UISelectEntity> cfdis= null;
		List<Columna> campos      = null;
		Map<String, Object>params = null;
		try {
			params= new HashMap<>();
			campos= new ArrayList<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			cfdis= UIEntity.build("TcManticUsosCfdiDto", "row", params, campos, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("cfdis", cfdis);
			this.attrs.put("cfdi", new UISelectEntity("-1"));
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
	}
	
}
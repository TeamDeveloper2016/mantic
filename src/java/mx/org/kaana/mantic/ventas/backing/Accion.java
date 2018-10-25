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
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.ventas.beans.SaldoCliente;
import mx.org.kaana.mantic.ventas.comun.IBaseVenta;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

@Named(value= "manticVentasAccion")
@ViewScoped
public class Accion extends IBaseVenta implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private static final String VENDEDOR_PERFIL= "VENDEDOR DE PISO";	
	private EOrdenes tipoOrden;
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

	public StreamedContent getImage() {
		return image;
	}
	
	@PostConstruct
  @Override
  protected void init() {		
		boolean isMatriz= false;
    try {
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null? -1L: JsfBase.getFlashAttribute("idVenta"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? null: JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("buscaPorCodigo", true);
			this.attrs.put("activeLogin", false);
			this.attrs.put("autorized", false);
			this.attrs.put("expirada", false);
			this.attrs.put("isIndividual", true);
			this.attrs.put("descuentoIndividual", 0);
			this.attrs.put("descuentoGlobal", 0);
			this.attrs.put("tipoDescuento", INDIVIDUAL);
			this.attrs.put("descripcion", "Imagen no disponible");
			this.image= LoadImages.getImage("-1");
			loadClienteDefault();
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			isMatriz= JsfBase.getAutentifica().getEmpresa().isMatriz();
			this.attrs.put("isMatriz", isMatriz);
			if(isMatriz)
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
 				regresar = this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR) : null;
    		RequestContext.getCurrentInstance().execute("jsArticulos.back('gener\\u00F3 la cuenta ', '"+ ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");									
				JsfBase.setFlashAttribute("idVenta", null);
				JsfBase.setFlashAttribute("accion", null);
				this.init();
				//JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la cuenta."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
				RequestContext.getCurrentInstance().execute("userUpdate();");
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la cuenta de venta.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion  

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
	
	
	public void doAsignaClienteInicial(Long idCliente) {
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
			this.setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente	
	
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
			this.attrs.put("decripcion", articulo.toString("nombre"));
			this.image= LoadImages.getImage(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString(), articulo.toLong("idArticulo").toString());
			this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
			RequestContext.getCurrentInstance().update("deudor");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
	} // toMoveData
	
	@Override
	protected void toMoveDataArt(Articulo articulo, Integer index) throws Exception {
		UISelectEntity clienteSeleccion= null;		
		String descuentoPivote         = null;
		String descuentoVigente        = null;		
		try {
			clienteSeleccion= (UISelectEntity) this.attrs.get("clienteSeleccion");
			if(clienteSeleccion!= null && !clienteSeleccion.getKey().equals(-1L)){
				descuentoVigente= toDescuentoVigente(articulo.getIdArticulo(), clienteSeleccion.getKey());				
				if(descuentoVigente!= null){
					descuentoPivote= getAdminOrden().getDescuento();
					getAdminOrden().setDescuento(descuentoVigente);
					super.toMoveDataArt(articulo, index);			
					getAdminOrden().setDescuento(descuentoPivote);
				} // if
				else
					super.toMoveDataArt(articulo, index);				
			} // if
			else
				super.toMoveDataArt(articulo, index);	
			this.attrs.put("descripcion", articulo.getNombre());
			this.image= LoadImages.getImage(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString(), articulo.getIdArticulo().toString());
			this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
			RequestContext.getCurrentInstance().update("deudor");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
	} // toMoveDataArt
	
	private String toDescuentoVigente(Long idArticulo, Long idCliente) throws Exception{
		MotorBusqueda motorBusqueda= null;
		Entity descuentoVigente    = null;
		String regresar            = null;
		try {
			motorBusqueda= new MotorBusqueda(idArticulo, idCliente);
			descuentoVigente= motorBusqueda.toDescuentoGrupo();
			if(descuentoVigente!= null)
				regresar= descuentoVigente.toString("porcentaje");
			else{
				descuentoVigente= motorBusqueda.toDescuentoArticulo();
				if(descuentoVigente!= null)
					regresar= descuentoVigente.toString("porcentaje");
			} // else
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
					RequestContext.getCurrentInstance().execute("jsArticulos.back('cerr\\u00F3 la cuenta', '"+ ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
					JsfBase.addMessage("Se guardo la cuenta de venta.", ETipoMensaje.INFORMACION);	
					init();
				} // if
				else 
					JsfBase.addMessage("Ocurrió un error al registrar la cuenta de venta.", ETipoMensaje.ERROR);      			
			} // if	
			if(((TicketVenta)this.getAdminOrden().getOrden()).isValid()){
				transaccion= new Transaccion(((TicketVenta)this.getAdminOrden().getOrden()));
				transaccion.ejecutar(EAccion.NO_APLICA);
			} // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	} // doCerrarTicket
	
	public void doLogin() {		
		CambioUsuario cambioUsuario= null;		
		String cuenta    = null;
		String password  = null;
		RequestContext rc= null;
    try {					
			cuenta       = this.attrs.get("cuenta").toString();
			password     = this.attrs.get("password").toString();						
			cambioUsuario= new CambioUsuario(cuenta, password);
			rc= RequestContext.getCurrentInstance();
			if(cambioUsuario.validaUsuario()) {
				this.init();
			  rc.execute("jsArticulos.disabledLogin();");
			}	// if
			else{
				this.attrs.put("cuenta", "");
				this.attrs.put("password", "");
				rc.execute("jsArticulos.restoreAutenticate();");
				rc.update("cuenta password");
				JsfBase.addMessage("Cambio de usuario", "Error de autenticación <br><br> -Cuenta/contraseña son incorrectos<br> -Cuenta de directorio invalida", ETipoMensaje.ERROR);      																	
			} // esle
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	} // doLogin		
	
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
	
	public void doActualizaImage(String idImage, String descripcion) {
		String idEmpresa= null;
		try {
			if(!Cadena.isVacio(descripcion))
  			this.attrs.put("descripcion", descripcion);
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
	
	public void doActivatePage(){
		this.attrs.put("activated", true);
	} // doActivatePage
	
	public String doIrPage(){
		String regresar        = null;
		Transaccion transaccion= null;
		try {
			if(!this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L))))){
				((TicketVenta)this.getAdminOrden().getOrden()).setIdVentaEstatus(EEstatusVentas.EN_CAPTURA.getIdEstatusVenta());
				loadOrdenVenta();
				transaccion = new Transaccion(((TicketVenta)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
				this.getAdminOrden().toAdjustArticulos();
				transaccion.ejecutar(EAccion.REGISTRAR);
				JsfBase.setFlashAttribute("idVenta", transaccion.getOrden().getIdVenta());
				JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);
			} // if
			else{
				JsfBase.setFlashAttribute("idVenta", -1L);
				JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
			} // else
			regresar= "catalogos".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // doCatalogos
	
	public void doUpdateDialogClientes(String codigo) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
			} // if	
			else
				codigo= "WXYZ";
			if(buscaPorCodigo)
    		params.put(Constantes.SQL_CONDICION, "upper(tc_mantic_clientes.rfc) like '".concat(codigo.toUpperCase()).concat("%'"));			
			else
    		params.put(Constantes.SQL_CONDICION, "upper(tc_mantic_clientes.razon_social) like '".concat(codigo.toUpperCase()).concat("%'"));
      this.attrs.put("lazyModelClientes", new FormatCustomLazy("VistaClientesDto", "findRazonSocial", params, columns));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
}

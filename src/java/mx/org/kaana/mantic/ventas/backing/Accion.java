package mx.org.kaana.mantic.ventas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoVenta;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.ventas.beans.SaldoCliente;
import mx.org.kaana.mantic.ventas.comun.IBaseVenta;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value= "manticVentasAccion")
@ViewScoped
public class Accion extends IBaseVenta implements Serializable {

	private static final Logger LOG            = Logger.getLogger(Accion.class);
  private static final long serialVersionUID = 327393488565639367L;
	protected static final String VENDEDOR_PERFIL= "VENDEDOR DE PISO";	
	protected EOrdenes tipoOrden;
	protected StreamedContent image;

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

  @Override
  public int getRows() {
	  return this.attrs.get("paginator")== null || this.getAdminOrden()== null || !(boolean)this.attrs.get("paginator") || this.getAdminOrden().getTotales().getArticulos()<= Constantes.REGISTROS_PARTIDAS? Constantes.REGISTROS_MAX_TABLA: Constantes.REGISTROS_PARTIDAS;
  }
  
	@PostConstruct
  @Override
  protected void init() {		
		boolean isMatriz= false;
    try {
			this.setPrecio(ETipoVenta.MENUDEO.getNombreCampo());
			this.attrs.put("xcodigo", JsfBase.getFlashAttribute("xcodigo"));	
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
			this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null? -1L: JsfBase.getFlashAttribute("idVenta"));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null ? EAccion.AGREGAR : (this.attrs.get("idVenta") != null && !Long.valueOf(this.attrs.get("idVenta").toString()).equals(-1L) ? JsfBase.getFlashAttribute("accion") : EAccion.AGREGAR));      
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente")== null? -1L: JsfBase.getFlashAttribute("idCliente"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? null: JsfBase.getFlashAttribute("retorno"));
			LOG.warn("Flash atributes [accion[" + this.attrs.get("accion") + "] idVenta [" + this.attrs.get("idVenta") + "] retorno [" + this.attrs.get("retorno") + "]]");
			this.loadClienteDefault();
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", true);
			this.attrs.put("buscaPorCodigo", true);
			this.attrs.put("activeLogin", false);
			this.attrs.put("autorized", false);
			this.attrs.put("expirada", false);
			this.attrs.put("isIndividual", true);
			this.attrs.put("observaciones", "");
			this.attrs.put("descuentoIndividual", 0);
			this.attrs.put("descuentoGlobal", 0);
			this.attrs.put("tipoDescuento", MENUDEO);
			this.doActivarDescuento();
			this.attrs.put("descripcion", "Imagen no disponible");
			this.attrs.put("busquedaTicketAbierto", "");
			this.attrs.put("decuentoAutorizadoActivo", false);
			this.attrs.put("tipoDecuentoAutorizadoActivo", MENUDEO);
			this.attrs.put("ticketLock", -1L);
			this.image= LoadImages.getImage(-1L);
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			isMatriz= JsfBase.getAutentifica().getEmpresa().isMatriz();
			this.attrs.put("isMatriz", isMatriz);
			if(isMatriz)
				this.loadSucursales();
			this.doLoad();
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.pathImage= dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");
      this.toLoadRegimenesFiscales();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
  public void doLoad() {
    EAccion eaccion= null;
		Long idCliente = Constantes.VENTA_AL_PUBLICO_GENERAL_ID_KEY;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
			LOG.warn("Inicializando admin orden.");
			LOG.warn("Accion:" + eaccion.name());
      switch (eaccion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminTickets(new TicketVenta(-1L)));
					this.saldoCliente= new SaldoCliente();
					this.attrs.put("consecutivo", "");		
					idCliente= Long.valueOf(this.attrs.get("idCliente").toString());
					if(idCliente!= null && !idCliente.equals(-1L))
						this.doAsignaClienteInicial(idCliente);
					else
						this.attrs.put("mostrarCorreos", true);
          break;
        case MODIFICAR:			
        case CONSULTAR:			
					LOG.warn("Atributes:" + this.attrs.toString());
          this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", this.attrs)));					
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
					idCliente= ((TicketVenta)getAdminOrden().getOrden()).getIdCliente();
					if(idCliente!= null && !idCliente.equals(-1L)){
						this.doAsignaClienteInicial(idCliente);
						this.doLoadSaldos(idCliente);
            if(((TicketVenta)this.getAdminOrden().getOrden()).getIdManual()== 1L) {
              this.attrs.put("observaciones", ((TicketVenta)this.getAdminOrden().getOrden()).getObservaciones());
              this.getAdminOrden().getTotales().setIva(((TicketVenta)this.getAdminOrden().getOrden()).getImpuestos());
              this.getAdminOrden().getTotales().setSubTotal(((TicketVenta)this.getAdminOrden().getOrden()).getSubTotal());
              this.getAdminOrden().getTotales().setTotal(((TicketVenta)this.getAdminOrden().getOrden()).getTotal());
            } // if
					} // if
					this.toLoadSucursales();
          break;
      } // switch
			this.attrs.put("consecutivo", "");
      this.attrs.put("paginator", Boolean.FALSE);
      this.attrs.put("indexPaginator", 0);
			// this.doResetDataTable();      
			this.toLoadCatalogos();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

	private void toLoadSucursales() {
		List<UISelectEntity> sucursales= null;		
		try {
			if(this.attrs.get("sucursales")!= null){
				sucursales= (List<UISelectEntity>) this.attrs.get("sucursales");
				for(Entity sucursal: sucursales){
					if(sucursal.getKey().equals(((TicketVenta)getAdminOrden().getOrden()).getIdEmpresa()))
						this.attrs.put("idEmpresa", sucursal);
				} // for
			} // if									
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
			throw e;
		} // catch		
	}
	
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
		boolean bandera        = true;
    try {			
			if(!this.getAdminOrden().getArticulos().isEmpty() && getAdminOrden().getArticulos().size()>0 && getAdminOrden().getArticulos().get(0).isValid()){
				this.loadOrdenVenta();
				eaccion= (EAccion) this.attrs.get("accion");						
				transaccion = new Transaccion(((TicketVenta)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
				this.getAdminOrden().toAdjustArticulos();
				if (!transaccion.ejecutar(eaccion)) {
					JsfBase.addMessage("Ocurrió un error al registrar la cuenta de venta.", ETipoMensaje.ERROR);      			
					bandera= false;
				} // if
			} // if
			else if (((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta()> 0L) {
				transaccion= new Transaccion((TicketVenta)this.getAdminOrden().getOrden());
				transaccion.ejecutar(EAccion.ELIMINAR);
			} // else if			
			if(bandera) {
				JsfBase.setFlashAttribute("idVenta", null);
				JsfBase.setFlashAttribute("accion", null);				
				this.attrs.put("idEmpresaVenta", this.attrs.get("idEmpresa"));
				this.init();
				this.attrs.put("idEmpresa", this.attrs.get("idEmpresaVenta"));
				this.doAsignaClienteInicial(Constantes.VENTA_AL_PUBLICO_GENERAL_ID_KEY);
				UIBackingUtilities.execute("userUpdate();");			
			} // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    return regresar;
  } // doAccion  

	private void toLoadCatalogos() {
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
	}  			
	
	public void doAsignaClienteInicial(Long idCliente) {
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null; 
		Entity clienteDefault                 = null;
		try {			
			motorBusqueda= new MotorBusqueda(null, idCliente);
			clienteDefault= motorBusqueda.toClienteDefault();
			this.attrs.put("mostrarCorreos", idCliente.equals(-1L) || idCliente.equals(clienteDefault.getKey()));
			seleccion= new UISelectEntity(motorBusqueda.toCliente());
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			clientesSeleccion.add(0, new UISelectEntity(clienteDefault));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			this.setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaClienteInicial
	
	public void doActualizaPrecioCliente() {
		List<UISelectEntity> clientesSeleccion= null;
		UISelectEntity clienteSeleccion       = null;
		boolean precioVigente                 = false;
		try {			
			clienteSeleccion= (UISelectEntity) this.attrs.get("clienteSeleccion");
			precioVigente   = clienteSeleccion!= null && !clienteSeleccion.getKey().equals(-1L);
			this.attrs.put("mostrarCorreos", clienteSeleccion== null ||clienteSeleccion.getKey().equals(-1L) || clienteSeleccion.getKey().equals(((UISelectEntity)this.attrs.get("clienteDefault")).getKey()));			
			if(precioVigente) {
				clientesSeleccion= (List<UISelectEntity>) this.attrs.get("clientesSeleccion");
				clienteSeleccion = clientesSeleccion.get(clientesSeleccion.indexOf(clienteSeleccion));
        this.attrs.put("clienteSeleccion", clienteSeleccion);
				setPrecio(Cadena.toBeanNameEspecial(clienteSeleccion.toString("tipoVenta")));				
			} // if
			else
				this.setPrecio("menudeo");
			this.doReCalculatePreciosArticulos(precioVigente, clienteSeleccion.getKey());
      this.toLoadRegimenesFiscales();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActualizaPrecioCliente

	@Override
	public void doCalculate(Integer index) {
		super.validatePrecioAsignado(index);
		super.doCalculate(index);
		this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
		UIBackingUtilities.update("deudor");
	}	// doCalculate
	
	@Override
	protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		UISelectEntity clienteSeleccion= null;		
		String descuentoPivote         = null;
		String descuentoVigente        = null;		
		try {
			clienteSeleccion= (UISelectEntity) this.attrs.get("clienteSeleccion");
			if(articulo!= null && articulo.containsKey("idArticulo")) {
				if(clienteSeleccion!= null && !clienteSeleccion.getKey().equals(-1L)) {
					descuentoVigente= this.toDescuentoVigente(articulo.toLong("idArticulo"), clienteSeleccion.getKey());				
					if(descuentoVigente!= null) {
						descuentoPivote= getAdminOrden().getDescuento();
						this.getAdminOrden().setDescuento(descuentoVigente);
						super.toMoveData(articulo, index);			
						this.getAdminOrden().setDescuento(descuentoPivote);
					} // if
					else
						super.toMoveData(articulo, index);				
				} // if
				else
					super.toMoveData(articulo, index);	
				this.attrs.put("decripcion", articulo.toString("nombre"));
				this.image= LoadImages.getImage(articulo.toLong("idArticulo"));
				this.saldoCliente.setTotalVenta(this.getAdminOrden().getTotales().getTotal());
			} // if
			else
				LOG.warn("VERIFICAR PORQUE RAZON NO SE TIENE EL ID_ARTICULO "+ articulo);
			UIBackingUtilities.update("deudor");
			this.doResetDataTable(index);      
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
	} // toMoveData
	
	@Override
	protected void toMoveArticulo(Articulo articulo, Integer index) throws Exception {
		UISelectEntity clienteSeleccion= null;		
		String descuentoPivote         = null;
		String descuentoVigente        = null;		
		try {
			clienteSeleccion= (UISelectEntity) this.attrs.get("clienteSeleccion");
			if(clienteSeleccion!= null && !clienteSeleccion.getKey().equals(-1L)){
				descuentoVigente= toDescuentoVigente(articulo.getIdArticulo(), clienteSeleccion.getKey());				
				if(descuentoVigente!= null) {
					descuentoPivote= getAdminOrden().getDescuento();
					getAdminOrden().setDescuento(descuentoVigente);
					super.toMoveArticulo(articulo, index);			
					this.getAdminOrden().setDescuento(descuentoPivote);
				} // if
				else
					super.toMoveArticulo(articulo, index);				
			} // if
			else
				super.toMoveArticulo(articulo, index);	
			this.attrs.put("descripcion", articulo.getNombre());
			this.image= LoadImages.getImage(articulo.getIdArticulo());
			this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
			UIBackingUtilities.update("deudor");
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
	
	private void loadOrdenVenta() {		
		// this.getAdminOrden().toCheckTotales();
    try {
      UISelectEntity cliente = (UISelectEntity) this.attrs.get("clienteSeleccion");			
      // AQUI SE DEBE DE LANZAR UN METODO DE JAVASCRIPT CON EL DIALOGO DE SOLICITAR AUTORIZACION SI SE DA EL CASO QUE LOS PRECIOS SEAN DIFERENTES A MENUDEO
      if(cliente!= null) {
        if(!Objects.equals(cliente.getKey(), Constantes.VENTA_AL_PUBLICO_GENERAL_ID_KEY)) {
          TicketVenta ticketVenta= (TicketVenta)this.getAdminOrden().getOrden();
          // ACTUALIZAR EL REGIMEN FISCAL PARA ESTE CLIENTE EN CASO DE QUE NO TENGA O SEA DIFERENTE
          TcManticClientesDto customer= (TcManticClientesDto)DaoFactory.getInstance().findById(TcManticClientesDto.class, cliente.getKey());
          if(this.getIkRegimenFiscal()!= null && !Objects.equals(this.getIkRegimenFiscal().getKey(), -1L) && (customer.getIdRegimenFiscal()== null || !Objects.equals(customer.getIdRegimenFiscal(), this.getIkRegimenFiscal().getKey()))) {
            customer.setIdRegimenFiscal(this.getIkRegimenFiscal().getKey());
            DaoFactory.getInstance().update(customer);
          } // if  
          if(this.getIkRegimenFiscal()!= null && !Objects.equals(this.getIkRegimenFiscal().getKey(), -1L))
            ticketVenta.setIdRegimenFiscal(this.getIkRegimenFiscal().getKey());
        } // if  
        ((TicketVenta)this.getAdminOrden().getOrden()).setIdEmpresa(Long.valueOf(this.attrs.get("idEmpresa").toString()));
        ((TicketVenta)this.getAdminOrden().getOrden()).setIdCliente(cliente.getKey());
        ((TicketVenta)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
        ((TicketVenta)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
        ((TicketVenta)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
//        if(!Objects.equals(Numero.toRedondear(this.getAdminOrden().getTotales().getSubTotal()+ this.getAdminOrden().getTotales().getIva()), Numero.toRedondear(this.getAdminOrden().getTotales().getTotal())))
//          ((TicketVenta)this.getAdminOrden().getOrden()).setTotal(Numero.toRedondear(this.getAdminOrden().getTotales().getSubTotal()+ this.getAdminOrden().getTotales().getIva()));
//        else
        ((TicketVenta)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
        ((TicketVenta)this.getAdminOrden().getOrden()).setObservaciones((String)this.attrs.get("observaciones"));
      } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	} // loadOrdenVenta
	
	public void doCerrarTicket() {		
		Transaccion transaccion= null;
    try {								
			if(!this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L))))){
				this.loadOrdenVenta();
				transaccion = new Transaccion(((TicketVenta)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
				this.getAdminOrden().toAdjustArticulos();
				if (transaccion.ejecutar(EAccion.REGISTRAR)) {				
					UIBackingUtilities.execute("jsArticulos.back('cerr\\u00F3 la cuenta', '"+ ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
					JsfBase.addMessage("Se guardo la cuenta de venta.", ETipoMensaje.INFORMACION);	
					this.init();
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
		String cuenta              = null;
		String password            = null;
		RequestContext rc          = null;
    try {					
			cuenta  = this.attrs.get("cuenta").toString();
			password= this.attrs.get("password").toString();						
			cambioUsuario= new CambioUsuario(cuenta, password);
			rc= UIBackingUtilities.getCurrentInstance();
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
		String regresar        = null;
		Transaccion transaccion= null;
		try {
			if(this.attrs.get("clienteSeleccion")!= null && !((Entity)this.attrs.get("clienteSeleccion")).getKey().equals(-1L)){
				if(!this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L))))){
					((TicketVenta)this.getAdminOrden().getOrden()).setIdVentaEstatus(EEstatusVentas.EN_CAPTURA.getIdEstatusVenta());
					this.loadOrdenVenta();
					transaccion = new Transaccion(((TicketVenta)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
					this.getAdminOrden().toAdjustArticulos();
					transaccion.ejecutar(EAccion.DESACTIVAR);
					JsfBase.setFlashAttribute("idVenta", transaccion.getOrden().getIdVenta());
				}
				else
					JsfBase.setFlashAttribute("idVenta", -1L);																											
				JsfBase.setFlashAttribute("idCliente", ((Entity)this.attrs.get("clienteSeleccion")).getKey().equals(((UISelectEntity)this.attrs.get("clienteDefault")).getKey()) ? -1L : ((Entity)this.attrs.get("clienteSeleccion")).getKey() );					
				JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);
			} // if
			else{
				JsfBase.setFlashAttribute("idVenta", -1L);																							
				JsfBase.setFlashAttribute("idCliente", -1L);
				JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
			} // else
			JsfBase.setFlashAttribute("observaciones", "");								
			JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Ventas/accion");								
			regresar= "/Paginas/Mantic/Ventas/cliente".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // doCatalogos
	
	public void doLoadUsers() {
		List<UISelectEntity> vendedores= null;
		Map<String, Object>params      = new HashMap<>();
		List<Columna> columns          = new ArrayList<>();
		RequestContext rc              = null;
		try {
			params.put("idGrupo", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("perfil", VENDEDOR_PERFIL);
			params.put("idUsuario", JsfBase.getIdUsuario());
			columns.add(new Columna("nombreCompleto", EFormatoDinamicos.MAYUSCULAS));
			vendedores= UIEntity.build("VistaTcJanalUsuariosDto", "cambioUsuario", params, columns, Constantes.SQL_TODOS_REGISTROS);
			rc= UIBackingUtilities.getCurrentInstance();
			if(!vendedores.isEmpty()) {
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
			Methods.clean(columns);
		} // finally
	} // doLoadUsers	
	
	public void doActualizaImage(String idImage, String descripcion) {		
		try {
			if(!Cadena.isVacio(descripcion))
  			this.attrs.put("descripcion", descripcion);
			if(!idImage.equals("-1")){
				this.image= LoadImages.getImage(Long.valueOf(idImage));
				this.attrs.put("imagePivote", idImage);
			} // if
			else 
        if (getAdminOrden().getArticulos().isEmpty() || (getAdminOrden().getArticulos().size()== 1 && getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L)))
				  this.image= LoadImages.getImage(Long.valueOf(idImage));
			  else
          if(Cadena.isVacio(this.attrs.get("imagePivote")))
            LOG.error("VERIFICAR POR QUE ESTA VACIO [".concat(idImage).concat("] -> ").concat(descripcion));
          else  
				    this.image= LoadImages.getImage(Long.valueOf((String)this.attrs.get("imagePivote")));
			this.attrs.put("idArticulo", idImage);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActualizaImage
	
	private void loadClienteDefault() {
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
			this.attrs.put("clienteDefault", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // loadClienteDefault	
	
	public void doUpdateForEmpresa(){
		Map<String, Object>params = new HashMap<>();
		List<Columna> columns     = new ArrayList<>();    
		try {
			loadClienteDefault();
			doActualizaPrecioCliente();			    
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", this.attrs.get("idEmpresa"));
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenPrincipal", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) 
				((TicketVenta)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
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
				this.loadOrdenVenta();
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
	
	@Override
	public void doSearchArticulo(Long idArticulo, Integer index) {
		this.attrs.put("idAlmacen", ((TicketVenta)this.getAdminOrden().getOrden()).getIkAlmacen().getKey());
		super.doSearchArticulo(idArticulo, index);
	}
  
  public void doResetDataTable(int index) {
    if(Objects.equals(((index+ 1)% Constantes.REGISTROS_PARTIDAS), 0))
      this.attrs.put("indexPaginator", index+ 1);
    this.doResetDataTable();
  }
  
  @Override
	public void doResetDataTable() {
    DataTable dataTable= (DataTable)JsfUtilities.findComponent("tabla");
    if (dataTable!= null) {
      Integer index= (Integer)this.attrs.get("indexPaginator");
      if(!((boolean)this.attrs.get("paginator")) && this.getAdminOrden().getTotales().getArticulos()> Constantes.REGISTROS_PARTIDAS) {
			  dataTable.reset();
        dataTable.setFirst(index);		
        dataTable.setRows(Constantes.REGISTROS_PARTIDAS);		
        this.attrs.put("paginator", Boolean.TRUE);
		  }	// if
      else
        if((boolean)this.attrs.get("paginator") && this.getAdminOrden().getTotales().getArticulos()<= Constantes.REGISTROS_PARTIDAS) {
          dataTable.reset();
          dataTable.setFirst(index);		
          dataTable.setRows(Constantes.REGISTROS_MAX_TABLA);		
          this.attrs.put("paginator", Boolean.FALSE);
        } // if
    } // if
	}
  
  @Override  
  public void doDeleteArticulo(Integer index, Boolean isCantidad) {  
    Boolean temporal= (boolean)this.attrs.get("paginator");
    super.doDeleteArticulo(index, isCantidad);
    this.attrs.put("paginator", temporal);
    this.doResetDataTable();
  }
 
	private void toLoadRegimenesFiscales() {
		List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    List<UISelectEntity> regimenesFiscales= null;
    try {      
      UISelectEntity cliente= (UISelectEntity)this.attrs.get("clienteSeleccion");
      params.put("idTipoRegimenPersona", "1, 2");                  
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      regimenesFiscales= (List<UISelectEntity>) UIEntity.seleccione("TcManticRegimenesFiscalesDto", "tipo", params, columns, "codigo");
			this.attrs.put("regimenesFiscales", regimenesFiscales);
      if(cliente!= null && regimenesFiscales!= null && !regimenesFiscales.isEmpty()) {
        int index= regimenesFiscales.indexOf(new UISelectEntity(cliente.toLong("idRegimenFiscal")== null? -1L: cliente.toLong("idRegimenFiscal")));
        if(index< 0)
          this.setIkRegimenFiscal(regimenesFiscales.get(0));
        else
          this.setIkRegimenFiscal(regimenesFiscales.get(index));
      } // else
      else
        this.setIkRegimenFiscal(new UISelectEntity(-1L));
      
      if(cliente!= null&& !Objects.equals(cliente.getKey(), -1L) && !Objects.equals(cliente.getKey(), Constantes.VENTA_AL_PUBLICO_GENERAL_ID_KEY) && this.getIkRegimenFiscal()!= null && Objects.equals(this.getIkRegimenFiscal().getKey(), -1L)) 
        UIBackingUtilities.execute("janal.alert('¡ Por favor solicite el Regimen Fiscal al cliente !\\n\\n Y capture la información donde corresponde ...');");
    } // try
    catch (Exception e) {
			throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	} // toLoadRegimenesFiscales
 
  @Override
	public void doAsignaTicketAbierto() {  
    super.doAsignaTicketAbierto();
    this.toLoadRegimenesFiscales();
  }
  
  @Override
  public void doAsignaCliente(SelectEvent event) {
    super.doAsignaCliente(event);
    this.toLoadRegimenesFiscales();
  }
    
}
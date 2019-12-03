package mx.org.kaana.mantic.facturas.backing;

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
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.facturas.reglas.Transaccion;
import mx.org.kaana.mantic.comun.IBaseStorage;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.facturas.beans.FacturaFicticia;
import mx.org.kaana.mantic.facturas.reglas.AdminFacturas;
import mx.org.kaana.mantic.ventas.beans.SaldoCliente;
import mx.org.kaana.mantic.ventas.comun.IBaseVenta;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Named(value= "manticFacturasAccion")
@ViewScoped
public class Accion extends IBaseVenta implements IBaseStorage, Serializable {

	private static final Log LOG               = LogFactory.getLog(Accion.class);  
  private static final long serialVersionUID = 327393488565639367L;
	private static final String VENDEDOR_PERFIL= "VENDEDOR DE PISO";
	private static final String INDIVIDUAL     = "1";
	private static final Long ESTATUS_ELABORADA= 11L;
	
	private SaldoCliente saldoCliente;
	private StreamedContent image;
	private FormatLazyModel almacenes;

	public Accion() {
		super("menudeo", true);
	}
	
	@Override
	public SaldoCliente getSaldoCliente() {
		return saldoCliente;
	}	

	@Override
	public void setSaldoCliente(SaldoCliente saldoCliente) {
		this.saldoCliente = saldoCliente;
	}

	public StreamedContent getImage() {
		return image;
	}

	@Override
	public FormatLazyModel getAlmacenes() {
		return almacenes;
	}	
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("accionInicial", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idFicticia", JsfBase.getFlashAttribute("idFicticia")== null? -1L: JsfBase.getFlashAttribute("idFicticia"));
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente")== null? -1L: JsfBase.getFlashAttribute("idCliente"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? null: JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("buscaPorCodigo", false);
			this.attrs.put("activeLogin", false);
			this.attrs.put("autorized", false);
			this.attrs.put("expirada", false);
			this.attrs.put("isIndividual", true);
			this.attrs.put("descuentoIndividual", 0);
			this.attrs.put("descuentoGlobal", 0);
			this.attrs.put("tipoDescuento", INDIVIDUAL);
			doActivarDescuento();
			this.attrs.put("descripcion", "Imagen no disponible");
			this.attrs.put("mostrarBanco", false);
			this.attrs.put("decuentoAutorizadoActivo", false);
			this.attrs.put("tipoDecuentoAutorizadoActivo", MENUDEO);
			this.attrs.put("ticketLock", -1L);
			this.image= LoadImages.getImage(-1L);
			this.attrs.put("observaciones", JsfBase.getFlashAttribute("observaciones")== null? "" : JsfBase.getFlashAttribute("observaciones"));
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("isMatriz", JsfBase.isAdminEncuestaOrAdmin());
			loadClienteDefault();
			if(JsfBase.isAdminEncuestaOrAdmin())
				loadSucursales();
			loadBancos();
			loadCfdis();
			loadTiposMediosPagos();
			loadTiposPagos();
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
  public void doLoad() {
    EAccion eaccion            = null;
		Long idCliente             = -1L;
		Long idClienteDomicilio    = -1L;
		TcManticFacturasDto factura= null;
		this.saldoCliente          = new SaldoCliente();
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminFacturas(new FacturaFicticia(-1L)));
					this.attrs.put("consecutivo", "");		
					idCliente= Long.valueOf(this.attrs.get("idCliente").toString());
					if(idCliente!= null && !idCliente.equals(-1L))
						doAsignaClienteInicial(idCliente);
          break;
        case MODIFICAR:			
        case CONSULTAR:			
          this.setAdminOrden(new AdminFacturas((FacturaFicticia)DaoFactory.getInstance().toEntity(FacturaFicticia.class, "VistaFicticiasDto", "ficticia", this.attrs)));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));					
					this.attrs.put("consecutivo", ((FacturaFicticia)this.getAdminOrden().getOrden()).getConsecutivo());	
					factura= (TcManticFacturasDto) DaoFactory.getInstance().findById(TcManticFacturasDto.class, ((FacturaFicticia)this.getAdminOrden().getOrden()).getIdFactura());
					this.attrs.put("observaciones", factura.getObservaciones());					
					idCliente= ((FacturaFicticia)getAdminOrden().getOrden()).getIdCliente();
					if(idCliente!= null && !idCliente.equals(-1L)){
						doAsignaClienteInicial(idCliente);
						loadDomicilios(idCliente);
						idClienteDomicilio= ((FacturaFicticia)getAdminOrden().getOrden()).getIdClienteDomicilio();
						if(idClienteDomicilio!= null && !idClienteDomicilio.equals(-1L))
							this.attrs.put("domicilio", new UISelectEntity(idClienteDomicilio));
					} // if
					loadCatalogs();					
          break;
      } // switch			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

	private void loadCatalogs(){
		List<UISelectEntity> sucursales     = null;
		List<UISelectEntity> cfdis          = null;
		List<UISelectEntity> tiposMedioPagos= null;
		List<UISelectEntity> tiposPagos     = null;
		List<UISelectEntity> bancos         = null;
		try {
			if(this.attrs.get("sucursales")!= null){
				sucursales= (List<UISelectEntity>) this.attrs.get("sucursales");
				for(Entity sucursal: sucursales){
					if(sucursal.getKey().equals(((FacturaFicticia)getAdminOrden().getOrden()).getIdEmpresa()))
						this.attrs.put("idEmpresa", sucursal);
				} // for
			} // if
			cfdis= (List<UISelectEntity>) this.attrs.get("cfdis");
			for(Entity cfdi: cfdis){
				if(cfdi.getKey().equals(((FacturaFicticia)getAdminOrden().getOrden()).getIdUsoCfdi()))
					this.attrs.put("cfdi", cfdi);
			} // for
			tiposMedioPagos= (List<UISelectEntity>) this.attrs.get("tiposMedioPagos");
			for(Entity tiposMedioPago: tiposMedioPagos){
				if(tiposMedioPago.getKey().equals(((FacturaFicticia)getAdminOrden().getOrden()).getIdTipoMedioPago()))
					this.attrs.put("tipoMedioPago", tiposMedioPago);
			} // for			
			tiposPagos= (List<UISelectEntity>) this.attrs.get("tiposPagos");
			for(Entity tipoPago: tiposPagos){
				if(tipoPago.getKey().equals(((FacturaFicticia)getAdminOrden().getOrden()).getIdTipoPago()))
					this.attrs.put("tipoPago", tipoPago);
			} // for
			doValidaTipoPago();
			if(Boolean.valueOf(this.attrs.get("mostrarBanco").toString())){
				bancos= (List<UISelectEntity>) this.attrs.get("bancos");
				for(Entity banco: bancos){
					if(banco.getKey().equals(((FacturaFicticia)getAdminOrden().getOrden()).getIdBanco()))
						this.attrs.put("banco", banco);
				} // for
				this.attrs.put("referencia", ((FacturaFicticia)getAdminOrden().getOrden()).getReferencia());
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
			throw e;
		} // catch		
	}
	
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
	
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;		
    try {			
			this.loadOrdenVenta();
			eaccion= (EAccion) this.attrs.get("accion");						
			transaccion = new Transaccion(((FacturaFicticia)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos(), this.attrs.get("observaciones").toString());
			this.getAdminOrden().toAdjustArticulos();
			if (transaccion.ejecutar(eaccion)) {
				if(eaccion.equals(EAccion.AGREGAR)) { 				  
    			// UIBackingUtilities.execute("jsArticulos.back('gener\\u00F3 la factura ', '"+ ((FacturaFicticia)this.getAdminOrden().getOrden()).getTicket()+ "');");
    			JsfBase.setFlashAttribute("idFicticia", ((FacturaFicticia)this.getAdminOrden().getOrden()).getIdFicticia());				
					this.init();
				} // if	
				if(eaccion.equals(EAccion.MODIFICAR))
				  JsfBase.addMessage("Se modificó la factura con consecutivo ["+ ((FacturaFicticia)this.getAdminOrden().getOrden()).getTicket()+ "].", ETipoMensaje.INFORMACION);
				regresar= (this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString() : "filtro").concat(Constantes.REDIRECIONAR);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la factura.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

	@Override
  public String doCancelar() {   
		String regresar= null;
		try {			
			JsfBase.setFlashAttribute("idFicticia", ((FacturaFicticia)this.getAdminOrden().getOrden()).getIdFicticia());
			regresar= this.attrs.get("retorno") != null ? (String)this.attrs.get("retorno") : "filtro";
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  } // doCancelar
	
	@Override
	public void doReCalculatePreciosArticulos(Long idCliente){
		doReCalculatePreciosArticulos(true, idCliente);
	} // doReCalculatePreciosArticulos
	
	@Override
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
							descuento= toDescuentoVigente(beanArticulo.getIdArticulo());
							if(descuento!= null)
								beanArticulo.setDescuento(descuento);							
						} // if
						else
							beanArticulo.setDescuento(sinDescuento);
					} // if
				} // for					
				if(getAdminOrden().getArticulos().size()>1){					
					getAdminOrden().toCalculate();
					UIBackingUtilities.update("@(.filas) @(.recalculo) @(.informacion)");
				} // if
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doReCalculatePreciosArticulos
	
	@Override
	protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		String descuentoPivote = null;
		String descuentoVigente= null;		
		try {			
			descuentoPivote = this.getAdminOrden().getDescuento();
			descuentoVigente= this.toDescuentoVigente(articulo.toLong("idArticulo"));				
			if(descuentoVigente!= null)
				this.getAdminOrden().setDescuento(descuentoVigente);					
			super.toMoveData(articulo, index);	
			this.getAdminOrden().setDescuento(descuentoPivote);
			this.attrs.put("descripcion", articulo.toString("nombre"));
			this.image= LoadImages.getImage(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString(), articulo.toLong("idArticulo").toString());
			this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
			UIBackingUtilities.update("deudor");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
	} // toMoveData
	
	@Override
	protected void toMoveArticulo(Articulo articulo, Integer index) throws Exception {		
		String descuentoPivote = null;
		String descuentoVigente= null;		
		try {	
			descuentoPivote= getAdminOrden().getDescuento();
			descuentoVigente= toDescuentoVigente(articulo.getIdArticulo());				
			if(descuentoVigente!= null)					
				getAdminOrden().setDescuento(descuentoVigente);																	
			super.toMoveArticulo(articulo, index);	
			getAdminOrden().setDescuento(descuentoPivote);
			this.attrs.put("descripcion", articulo.getNombre());
			this.image= LoadImages.getImage(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString(), articulo.getIdArticulo().toString());
			this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
			UIBackingUtilities.update("deudor");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
	} // toMoveData
	
	private String toDescuentoVigente(Long idArticulo) throws Exception {
		MotorBusqueda motorBusqueda= null;
		Entity descuentoVigente    = null;
		String regresar            = null;
		try {
			motorBusqueda= new MotorBusqueda(idArticulo, -1L);			
			descuentoVigente= motorBusqueda.toDescuentoArticulo();
			if(descuentoVigente!= null)
				regresar= descuentoVigente.toString("porcentaje");
		} // try
		catch (Exception e) {			
			throw e;			
		} // catch		
		return regresar;
	} // toDescuentoVigente
	
	private void loadOrdenVenta() {
		// this.getAdminOrden().toCheckTotales();
		UISelectEntity cliente = (UISelectEntity) this.attrs.get("clienteSeleccion");			
		((FacturaFicticia)this.getAdminOrden().getOrden()).setIdEmpresa(Long.valueOf(this.attrs.get("idEmpresa").toString()));
		((FacturaFicticia)this.getAdminOrden().getOrden()).setIdCliente(cliente.getKey());
		((FacturaFicticia)this.getAdminOrden().getOrden()).setIdTipoPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
		((FacturaFicticia)this.getAdminOrden().getOrden()).setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoMedioPago").toString()));
		((FacturaFicticia)this.getAdminOrden().getOrden()).setIdUsoCfdi(Long.valueOf(this.attrs.get("cfdi").toString()));
		if(!Long.valueOf(this.attrs.get("tipoMedioPago").toString()).equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
			((FacturaFicticia)this.getAdminOrden().getOrden()).setIdBanco(Long.valueOf(this.attrs.get("banco").toString()));
			((FacturaFicticia)this.getAdminOrden().getOrden()).setReferencia(this.attrs.get("referencia").toString());
		} // if
		((FacturaFicticia)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
		((FacturaFicticia)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
		((FacturaFicticia)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
		((FacturaFicticia)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
		((FacturaFicticia)this.getAdminOrden().getOrden()).setUtilidad(this.getAdminOrden().getTotales().getUtilidad());
		((FacturaFicticia)this.getAdminOrden().getOrden()).setIdClienteDomicilio(((Entity)this.attrs.get("domicilio")).getKey());		
		if(((FacturaFicticia)this.getAdminOrden().getOrden()).getTipoDeCambio()< 1)
			((FacturaFicticia)this.getAdminOrden().getOrden()).setTipoDeCambio(1D);
	} // loadOrdenVenta
	
	public void doCerrarTicket() {		
		Transaccion transaccion= null;
    try {								
			if(!this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L))))){
				this.loadOrdenVenta();
				transaccion = new Transaccion(((FacturaFicticia)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
				this.getAdminOrden().toAdjustArticulos();
				if (transaccion.ejecutar(EAccion.REGISTRAR)) {				
					UIBackingUtilities.execute("jsArticulos.back('cerr\\u00F3 la cuenta', '"+ ((FacturaFicticia)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
					JsfBase.addMessage("Se guardo la cuenta de venta.", ETipoMensaje.INFORMACION);	
					init();
				} // if
				else 
					JsfBase.addMessage("Ocurrió un error al registrar la cuenta de venta.", ETipoMensaje.ERROR);      			
			} // if	
			if(((FacturaFicticia)this.getAdminOrden().getOrden()).isValid()){
				transaccion= new Transaccion(((FacturaFicticia)this.getAdminOrden().getOrden()));
				transaccion.ejecutar(EAccion.NO_APLICA);
			} // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	} // doCerrarTicket	
	
	@Override
	public void doAlmacenesArticulo(Long idArticulo, Integer index) {
		Map<String, Object>params= null;
		List<Columna>columns     = null;
		try {
			if(idArticulo!= null){
				params= new HashMap<>();
				params.put("idArticulo", idArticulo);
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
				columns= new ArrayList<>();
				columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
				this.almacenes= new FormatLazyModel("VistaKardexDto", "almacenesDetalle", params, columns);
				UIBackingUtilities.resetDataTable("almacenes");
				UIBackingUtilities.execute("PF('dlgAlmacenes').show();");				
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
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
			rc= UIBackingUtilities.getCurrentInstance();
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
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String) this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {				
				buscaPorCodigo= (((boolean)this.attrs.get("buscaPorCodigo")) && !search.startsWith(".")) || (!((boolean)this.attrs.get("buscaPorCodigo")) && search.startsWith("."));  			
				if(search.startsWith("."))
					search= search.trim().substring(1);				
				search= search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);			
			if(buscaPorCodigo)
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
	
	@Override
	protected void loadSucursales(){
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
	
	@Override
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
	
	@Override
	public void doAplicarDescuento(){
		doAplicarDescuento(-1);
	} // doAplicarDescuento
	
	@Override
	public void doAplicarDescuento(Integer index){
		Boolean isIndividual       = false;
		CambioUsuario cambioUsuario= null;
		String cuenta              = null;
		String contrasenia         = null;
		Double global              = 0D;
		try {
			if(!getAdminOrden().getArticulos().isEmpty()){
				cuenta= this.attrs.get("usuarioDescuento").toString();
				contrasenia= this.attrs.get("passwordDescuento").toString();
				cambioUsuario= new CambioUsuario(cuenta, contrasenia);
				if(cambioUsuario.validaPrivilegiosDescuentos()){
					isIndividual= Boolean.valueOf(this.attrs.get("isIndividual").toString());
					if(isIndividual){
						getAdminOrden().getArticulos().get(index).setDescuento(this.attrs.get("descuentoIndividual").toString());
						if(getAdminOrden().getArticulos().get(index).autorizedDiscount())
							UIBackingUtilities.execute("jsArticulos.divDiscount('".concat(this.attrs.get("descuentoIndividual").toString()).concat("');"));
						else
							JsfBase.addMessage("No es posble aplicar el descuento, el descuento es superior a la utilidad", ETipoMensaje.ERROR);
					} // if
					else{		
						global= Double.valueOf(this.attrs.get("descuentoGlobal").toString());
						getAdminOrden().toCalculate();
						if(global < getAdminOrden().getTotales().getUtilidad()) {
							getAdminOrden().getTotales().setGlobal(global);							
							getAdminOrden().toCalculate();
						} // if
						else
							JsfBase.addMessage("No es posble aplicar el descuento, el descuento es superior a la utilidad", ETipoMensaje.ERROR);
					} // else
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
			this.attrs.put("isIndividual", true);
			this.attrs.put("descuentoIndividual", 0);
			this.attrs.put("descuentoGlobal", 0);
			this.attrs.put("tipoDescuento", INDIVIDUAL);
			this.attrs.put("usuarioDescuento", "");
			this.attrs.put("passwordDescuento", "");
		} // finally
	} // doAplicarDescuento	
	
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
			this.attrs.put("clienteDefault", seleccion);			
			loadDomicilios(seleccion.getKey());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // loadClienteDefault	
	
	private void loadDomicilios(Long idCliente) throws Exception{
		Map<String, Object>params     = null;
		List<UISelectEntity>domicilios= null;
		List<Columna>campos           = null;
		try {
			params= new HashMap<>();					
			params.put("idCliente", idCliente);
			campos= new ArrayList<>();
			campos.add(new Columna("calle", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("asentamiento", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("localidad", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("municipio", EFormatoDinamicos.MAYUSCULAS));
			domicilios= UIEntity.build("VistaClientesDto", "domiciliosCliente", params, campos, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("domicilios", domicilios);
			if(!domicilios.isEmpty())
				this.attrs.put("domicilio", UIBackingUtilities.toFirstKeySelectEntity(domicilios));
		} // try		
		finally {
			Methods.clean(params);
		} // finally
	} // loadDomicilios
	
	@Override
	public void doAsignaCliente(SelectEvent event) {
		UISelectEntity seleccion     = null;
		List<UISelectEntity> clientes= null;
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			this.toFindCliente(seleccion);
			loadDomicilios(seleccion.getKey());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	private void toFindCliente(UISelectEntity seleccion) {
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null;
		try {
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			motorBusqueda= new MotorBusqueda(-1L);
			clientesSeleccion.add(0, new UISelectEntity(motorBusqueda.toClienteDefault()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // toFindCliente
	
	@Override
	public List<UISelectEntity> doCompleteCliente(String query) {
		this.attrs.put("codigoCliente", query);
    this.doUpdateClientes();		
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	// doCompleteCliente
	
	@Override
	public void doUpdateClientes() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
			String search= (String) this.attrs.get("codigoCliente"); 
			search= !Cadena.isVacio(search)? search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*") : "WXYZ";
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
	
	public void doActualizaPrecioCliente(){		
		UISelectEntity clienteSeleccion= null;		
		try {
			clienteSeleccion= (UISelectEntity) this.attrs.get("clienteSeleccion");			
			loadDomicilios(clienteSeleccion.getKey());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActualizaPrecioCliente
	
	public String doClientes() {
		String regresar        = null;
		Transaccion transaccion= null;
		try {
			if(this.attrs.get("clienteSeleccion")!= null && !((Entity)this.attrs.get("clienteSeleccion")).getKey().equals(-1L)){
				if(!this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L))))){
					((FacturaFicticia)this.getAdminOrden().getOrden()).setIdFicticiaEstatus(ESTATUS_ELABORADA);
					this.loadOrdenVenta();
					transaccion = new Transaccion(((FacturaFicticia)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos(), this.attrs.get("observaciones").toString());
					this.getAdminOrden().toAdjustArticulos();
					transaccion.ejecutar(EAccion.DESACTIVAR);
					JsfBase.setFlashAttribute("idFicticia", transaccion.getOrden().getIdFicticia());
				}
				else
					JsfBase.setFlashAttribute("idFicticia", -1L);																											
				JsfBase.setFlashAttribute("idCliente", ((Entity)this.attrs.get("clienteSeleccion")).getKey().equals(((UISelectEntity)this.attrs.get("clienteDefault")).getKey()) ? -1L : ((Entity)this.attrs.get("clienteSeleccion")).getKey() );					
				JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);
			} // if
			else{
				JsfBase.setFlashAttribute("idFicticia", -1L);																							
				JsfBase.setFlashAttribute("idCliente", -1L);
				JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
			} // else
			JsfBase.setFlashAttribute("observaciones", this.attrs.get("observaciones"));								
			JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Facturas/accion.jsf");								
			regresar= "/Paginas/Mantic/Ventas/cliente.jsf".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // doCatalogos
	
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
			for(Entity record: cfdis){
				if(record.getKey().equals(3L))
					this.attrs.put("cfdi", record);
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadCfdis
	
	private void loadTiposMediosPagos(){
		List<UISelectEntity> tiposPagos= null;
		Map<String, Object>params      = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cobro_caja=1");
			tiposPagos= UIEntity.build("TcManticTiposMediosPagosDto", "row", params);
			this.attrs.put("tiposMedioPagos", tiposPagos);
			this.attrs.put("tipoMedioPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadTiposPagos
	
	private void loadTiposPagos(){
		List<UISelectEntity> tiposPagos= null;
		Map<String, Object>params      = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			tiposPagos= UIEntity.build("TcManticTiposPagosDto", "row", params);
			this.attrs.put("tiposPagos", tiposPagos);
			this.attrs.put("tipoPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadTiposPagos
	
	public void doValidaTipoPago(){
		Long tipoPago= -1L;
		try {
			tipoPago= Long.valueOf(this.attrs.get("tipoMedioPago").toString());
			this.attrs.put("mostrarBanco", !ETipoMediosPago.EFECTIVO.getIdTipoMedioPago().equals(tipoPago));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doValidaTipoPago

	@Override
	public void toSaveRecord() {
		Transaccion transaccion= null;
		EAccion eaccion        = null;		
    try {			
			this.loadOrdenVenta();
			eaccion= ((FacturaFicticia)this.getAdminOrden().getOrden()).getIdFicticia().equals(-1L) ? EAccion.AGREGAR : EAccion.MODIFICAR;						
			transaccion = new Transaccion(((FacturaFicticia)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos(), this.attrs.get("observaciones").toString());			
			if (transaccion.ejecutar(eaccion)){ 
				if(eaccion.equals(EAccion.AGREGAR))
					UIBackingUtilities.execute("jsArticulos.back('gener\\u00F3 la factura ', '"+ ((FacturaFicticia)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
  			JsfBase.setFlashAttribute("idFicticia", ((FacturaFicticia)this.getAdminOrden().getOrden()).getIdFicticia());							
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la factura.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	} // toSaveRecord
	
	public void doGlobalEvent(Boolean isViewException) {
		LOG.error("ESTO ES UN MENSAJE GLOBAL INVOCADO POR UNA EXCEPCION QUE NO FUE ATRAPADA");
		if(isViewException && this.getAdminOrden().getArticulos().size()> 0)
		  this.toSaveRecord();
    //UIBackingUtilities.execute("alert('ESTO ES UN MENSAJE GLOBAL INVOCADO POR UNA EXCEPCION QUE NO FUE ATRAPADA');");
	} // doGlobalEvent
}

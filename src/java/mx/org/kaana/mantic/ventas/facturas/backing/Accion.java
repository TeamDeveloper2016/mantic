package mx.org.kaana.mantic.ventas.facturas.backing;

import java.io.File;
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
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.facturas.reglas.Transaccion;
import mx.org.kaana.mantic.comun.IBaseStorage;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.IBaseAttachment;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.enums.EEstatusFicticias;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.Correo;
import mx.org.kaana.mantic.facturas.reglas.Transferir;
import mx.org.kaana.mantic.ventas.beans.SaldoCliente;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.comun.IBaseVenta;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Named(value= "manticVentasFacturasAccion")
@ViewScoped
public class Accion extends IBaseVenta implements IBaseStorage, Serializable {

	private static final Log LOG               = LogFactory.getLog(Accion.class);  
  private static final long serialVersionUID = 327393488565639367L;
	private static final String VENDEDOR_PERFIL= "VENDEDOR DE PISO";
	
	private SaldoCliente saldoCliente;
	private StreamedContent image;
	private FormatLazyModel almacenes;
	private List<Correo> correos;
	private List<Correo> selectedCorreos;	
	private Correo correo;
	protected Reporte reporte;

	public Accion() {
		super("menudeo", true);
	}
	
	public Reporte getReporte() {
		return reporte;
	}	// getReporte
	
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
	
	public List<Correo> getCorreos() {
		return correos;
	}

	public List<Correo> getSelectedCorreos() {
		return selectedCorreos;
	}

	public void setSelectedCorreos(List<Correo> selectedCorreos) {
		this.selectedCorreos = selectedCorreos;
	}	

	public Correo getCorreo() {
		return correo;
	}

	public void setCorreo(Correo correo) {
		this.correo = correo;
	}	
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("accionInicial", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null? -1L: JsfBase.getFlashAttribute("idVenta"));
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
			this.attrs.put("decuentoAutorizadoActivo", false);
			this.attrs.put("tipoDecuentoAutorizadoActivo", MENUDEO);
			this.attrs.put("descripcion", "Imagen no disponible");
			this.attrs.put("mostrarBanco", false);
			this.attrs.put("ticketLock", -1L);
			this.image= LoadImages.getImage(-1L);
			this.attrs.put("observaciones", JsfBase.getFlashAttribute("observaciones")== null? "" : JsfBase.getFlashAttribute("observaciones"));
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("isMatriz", JsfBase.isAdminEncuestaOrAdmin());
			this.correos= new ArrayList<>();
			this.selectedCorreos= new ArrayList<>();
			this.loadClienteDefault();						
			this.loadBancos();
			this.loadCfdis();
			this.loadTiposMediosPagos();
			this.loadTiposPagos();
			this.doLoad();			
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
		MotorBusqueda motorBusqueda= null; 		
		this.saldoCliente          = new SaldoCliente();
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminTickets(new TicketVenta(-1L)));
					this.attrs.put("consecutivo", "");		
					idCliente= Long.valueOf(this.attrs.get("idCliente").toString());
					if(idCliente!= null && !idCliente.equals(-1L))
						doAsignaClienteInicial(idCliente);
          break;
        case MODIFICAR:			
        case CONSULTAR:			
          this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", this.attrs), false));					
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));					
					this.attrs.put("consecutivo", ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo());						
					idCliente= ((TicketVenta)getAdminOrden().getOrden()).getIdCliente();
					motorBusqueda= new MotorBusqueda(-1L);
					if(idCliente!= null && (idCliente.equals(-1L) || idCliente.equals(motorBusqueda.toClienteDefault().getKey())))
						((AdminTickets)this.getAdminOrden()).loadTipoMedioPago();
					if(idCliente!= null && !idCliente.equals(-1L)){
						doAsignaClienteInicial(idCliente);
						loadDomicilios(idCliente);	
						if(((TicketVenta)getAdminOrden().getOrden()).getIdClienteDomicilio()!= null && !((TicketVenta)getAdminOrden().getOrden()).getIdClienteDomicilio().equals(-1L))
							this.attrs.put("domicilio", new UISelectEntity(((TicketVenta)getAdminOrden().getOrden()).getIdClienteDomicilio()));
					} // if
					this.loadSucursales();
					this.loadCatalogs();		
					List<UISelectEntity> tiposPagos= (List<UISelectEntity>)this.attrs.get("tiposMedioPagos");
					int index= tiposPagos.indexOf(new UISelectEntity(((TicketVenta)this.getAdminOrden().getOrden()).getIdTipoMedioPago()));
					if(index>= 0) {
					  this.attrs.put("tipoMedioPago", tiposPagos.get(index));
       		  if(!((TicketVenta)this.getAdminOrden().getOrden()).getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())) {
    					List<UISelectEntity> bancos= (List<UISelectEntity>)this.attrs.get("bancos");
							int banco= bancos.indexOf(new UISelectEntity(((TicketVenta)this.getAdminOrden().getOrden()).getIdBanco()));
							if(banco>= 0)
    		        this.attrs.put("banco", bancos.get(banco));
	    	      this.attrs.put("referencia", ((TicketVenta)this.getAdminOrden().getOrden()).getReferencia());
							this.attrs.put("mostrarBanco", true);
						} // if	
					} // if	
          break;
      } // switch			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

	private void loadCatalogs(){
		List<UISelectEntity> sucursales= null;
		List<UISelectEntity> cfdis     = null;		
		try {
			if(this.attrs.get("sucursales")!= null){
				sucursales= (List<UISelectEntity>) this.attrs.get("sucursales");
				for(Entity sucursal: sucursales){
					if(sucursal.getKey().equals(((TicketVenta)getAdminOrden().getOrden()).getIdEmpresa()))
						this.attrs.put("idEmpresa", sucursal);
				} // for
			} // if
			cfdis= (List<UISelectEntity>) this.attrs.get("cfdis");
			for(Entity cfdi: cfdis){
				if(cfdi.getKey().equals(((TicketVenta)getAdminOrden().getOrden()).getIdUsoCfdi()))
					this.attrs.put("cfdi", cfdi);
			} // for			
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
	} // doAsignaCliente	
	
  public String doAceptar() {  
    Transaccion transaccion     = null;
    String regresar             = null;
		List<UISelectEntity>clientes= null;
    try {				
			this.loadOrdenVenta();
			UISelectEntity cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");
			clientes= (List<UISelectEntity>) this.attrs.get("clientesSeleccion");
			UISelectEntity seleccion= clientes.get(clientes.indexOf(cliente));			
			if(seleccion.toString("razonSocial")==null || seleccion.toString("razonSocial").equals(Constantes.VENTA_AL_PUBLICO_GENERAL))
				JsfBase.addMessage("No se puede generar una factura para VENTA AL PUBLICO EN GENERAL, por favor cambie el cliente.", ETipoMensaje.ALERTA);   
			else {
				transaccion = new Transaccion(((TicketVenta)this.getAdminOrden().getOrden()), EEstatusFicticias.TIMBRADA.getIdEstatusFicticia(), (String)this.attrs.get("observaciones"));
				if (transaccion.ejecutar(EAccion.MODIFICAR)) {								
					doSendMail(((TicketVenta)this.getAdminOrden().getOrden()).getCorreos(), seleccion.toString("razonSocial"), ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta(), transaccion.getFacturaPrincipal(), seleccion.getKey());
					regresar = this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR) : "/Paginas/Mantic/Facturas/filtro".concat(Constantes.REDIRECIONAR);
					JsfBase.setFlashAttribute("idFicticia", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
					JsfBase.setFlashAttribute("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());				
					JsfBase.addMessage("Se generó la factura de forma correcta.", ETipoMensaje.INFORMACION);      			
				} // if
				else 
					JsfBase.addMessage("Ocurrió un error al registrar la factura.", ETipoMensaje.ERROR);   
		  } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

	public void doSendMail(String correos, String razonSocial, Long idVenta, TcManticFacturasDto facturaPrincipal, Long idCliente) {
		File factura= null;		
		Map<String, Object> params= new HashMap<>();
		//String[] emails= {"jimenez76@yahoo.com", correos};
		String[] emails= {correos};
		List<Attachment> files= new ArrayList<>(); 
		try {
			params.put("header", "...");
			params.put("footer", "...");
			params.put("empresa", JsfBase.getAutentifica().getEmpresa().getNombre());
			params.put("tipo", "Factura");			
			params.put("razonSocial", razonSocial);
			params.put("correo", ECorreos.FACTURACION.getEmail());			
			factura= toXml(facturaPrincipal.getIdFactura());
			this.doReporte("FACTURAS_FICTICIAS_DETALLE", true, idVenta, facturaPrincipal.getIdFactura(), facturaPrincipal.getIdFacturama(), facturaPrincipal.getSelloSat(), idCliente);
			Attachment attachments= new Attachment(this.reporte.getNombre(), Boolean.FALSE);
			files.add(attachments);
			files.add(new Attachment(factura, Boolean.FALSE));
			files.add(new Attachment("logo", ECorreos.FACTURACION.getImages().concat("logo.png"), Boolean.TRUE));
			params.put("attach", attachments.getId());
			for (String item: emails) {
				try {
					if(!Cadena.isVacio(item)) {
					  IBaseAttachment notificar= new IBaseAttachment(ECorreos.FACTURACION, ECorreos.FACTURACION.getEmail(), item, "controlbonanza@gmail.com,jorge.alberto.vs.10@gmail.com", "Ferreteria Bonanza - Factura", params, files);
					  LOG.info("Enviando correo a la cuenta: "+ item);
					  notificar.send();
					} // if	
				} // try
				finally {
				  if(attachments.getFile().exists()) {
   	  	    LOG.info("Eliminando archivo temporal: "+ attachments.getAbsolute());
				    // user.getFile().delete();
				  } // if	
				} // finally	
			} // for
	  	LOG.info("Se envio el correo de forma exitosa");
			if(correos.length()> 0)
		    JsfBase.addMessage("Se envió el correo de forma exitosa.", ETipoMensaje.INFORMACION);
			else
		    JsfBase.addMessage("No se selecciono ningún correo, por favor verifiquelo e intente de nueva cuenta.", ETipoMensaje.ALERTA);
		} // try // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(files);
		} // finally
	} // doSendMail	
	
	protected File toXml(Long idFactura) throws Exception{
		File regresar            = null;
		List<Entity> facturas    = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idFactura", idFactura);
			facturas= DaoFactory.getInstance().toEntitySet("VistaFicticiasDto", "importados", params);
			for(Entity factura: facturas){
				if(factura.toLong("idTipoArchivo").equals(1L))
					regresar= new File(factura.toString("ruta").concat(factura.toString("nombre")));
				else
					this.attrs.put("nameFacturaPdf", factura.toString("nombre"));
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toXml
	
	protected void doReporte(String nombre, boolean email, Long idVenta, Long idFactura, String idFacturama, String selloSat, Long idCliente) throws Exception{
		Parametros comunes           = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
		try {		      
			//recuperar el sello digital en caso de que la factura ya fue timbrada para que salga de forma correcta el reporte
			if(idFacturama!= null && selloSat== null) {
				Transferir transferir= null;
				try {
          transferir= new Transferir(idFacturama);
				  transferir.ejecutar(EAccion.PROCESAR);
				} // try
        catch(Exception e) {
					LOG.warn("La factura ["+ idFactura+ "] presento un problema al recuperar el sello digital ["+ idFacturama +"]");
          Error.mensaje(e);
				} // catch
				finally {
					transferir= null;
				} // finally
			} // if
      //es importante este orden para los grupos en el reporte	
			params= new HashMap<>();
      params.put("sortOrder", "order by tc_mantic_ventas.id_empresa, tc_mantic_clientes.id_cliente, tc_mantic_ventas.ejercicio, tc_mantic_ventas.orden");
      reporteSeleccion= EReportes.valueOf(nombre);
      if(!reporteSeleccion.equals(EReportes.FACTURAS_FICTICIAS)) {
        params.put("idFicticia", idVenta);
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(),-1L, -1L, idCliente);
      } // if
      else
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			      			
			if(email){ 
				this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros), this.attrs.get("nameFacturaPdf").toString().replaceFirst(".pdf", ""));		
        this.reporte.doAceptarSimple();			
			} // if
			else {				
				this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
				this.doVerificarReporte();
				this.reporte.setPrevisualizar(true);
				this.reporte.doAceptar();			
			} // else
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // doReporte
	
	public boolean doVerificarReporte() {
    boolean regresar = false;
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L){
			rc.execute("start(" + this.reporte.getTotal() + ")");	
      regresar = true;
    }
		else{
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
      regresar = false;
		} // else
    return regresar;
	} // doVerificarReporte	
	
	@Override
  public String doCancelar() {   
		String regresar= null;
		try {			
			JsfBase.setFlashAttribute("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
			JsfBase.setFlashAttribute("idFicticia", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
			regresar= this.attrs.get("retorno") != null ? (String)this.attrs.get("retorno") : "/Paginas/Mantic/Facturas/filtro";
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
		UISelectEntity cliente = (UISelectEntity) this.attrs.get("clienteSeleccion");			
		((TicketVenta)this.getAdminOrden().getOrden()).setIdEmpresa(Long.valueOf(this.attrs.get("idEmpresa").toString()));
		((TicketVenta)this.getAdminOrden().getOrden()).setIdCliente(cliente.getKey());
		((TicketVenta)this.getAdminOrden().getOrden()).setIdTipoPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
		((TicketVenta)this.getAdminOrden().getOrden()).setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoMedioPago").toString()));
		((TicketVenta)this.getAdminOrden().getOrden()).setIdUsoCfdi(Long.valueOf(this.attrs.get("cfdi").toString()));
		if(!Long.valueOf(this.attrs.get("tipoMedioPago").toString()).equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())) {
			((TicketVenta)this.getAdminOrden().getOrden()).setIdBanco(Long.valueOf(this.attrs.get("banco").toString()));
			((TicketVenta)this.getAdminOrden().getOrden()).setReferencia(this.attrs.get("referencia").toString());
		} // if
		((TicketVenta)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
		((TicketVenta)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
		((TicketVenta)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
		((TicketVenta)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
		((TicketVenta)this.getAdminOrden().getOrden()).setIdClienteDomicilio(((Entity)this.attrs.get("domicilio")).getKey());		
		if(((TicketVenta)this.getAdminOrden().getOrden()).getTipoDeCambio()< 1)
			((TicketVenta)this.getAdminOrden().getOrden()).setTipoDeCambio(1D);
		((TicketVenta)this.getAdminOrden().getOrden()).setCorreos(loadCorreos());
	} // loadOrdenVenta
	
	private String loadCorreos(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder("");
			for(Correo mail: this.selectedCorreos){
				if(!Cadena.isVacio(mail.getDescripcion()))
						regresar.append(mail.getDescripcion()).append(", ");
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.substring(0, regresar.length()- 2);
	} // loadCorreos	
	
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
			params.put("sucursales", ((TicketVenta)this.getAdminOrden().getOrden()).getIdEmpresa());
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
						if(global < getAdminOrden().getTotales().getUtilidad()){
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
		MotorBusqueda motorBusqueda  = null;
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			((TicketVenta)this.getAdminOrden().getOrden()).setIdCliente(seleccion.getKey());
			motorBusqueda= new MotorBusqueda(-1L);
			this.attrs.put("mostrarCorreos", seleccion.getKey().equals(-1L) || seleccion.getKey().equals(motorBusqueda.toClienteDefault().getKey()));
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
		String regresar= null;
		try {
			if(this.attrs.get("clienteSeleccion")!= null && !((Entity)this.attrs.get("clienteSeleccion")).getKey().equals(-1L)){				
				JsfBase.setFlashAttribute("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());																											
				JsfBase.setFlashAttribute("idCliente", ((Entity)this.attrs.get("clienteSeleccion")).getKey().equals(((UISelectEntity)this.attrs.get("clienteDefault")).getKey()) ? -1L : ((Entity)this.attrs.get("clienteSeleccion")).getKey() );					
				JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);
			} // if
			else{
				JsfBase.setFlashAttribute("idVenta", -1L);																							
				JsfBase.setFlashAttribute("idCliente", -1L);
				JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
			} // else
			JsfBase.setFlashAttribute("observaciones", this.attrs.get("observaciones"));								
			JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Ventas/Facturas/accion.jsf");								
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
	
	private void loadTiposMediosPagos() throws Exception {
		List<UISelectEntity> tiposPagos= null;
		Map<String, Object>params      = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cobro_caja=1");
			tiposPagos= UIEntity.build("TcManticTiposMediosPagosDto", "row", params);
			this.attrs.put("tiposMedioPagos", tiposPagos);
			this.attrs.put("tipoMedioPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		finally {
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
	} // toSaveRecord
	
	public void doGlobalEvent(Boolean isViewException) {
		LOG.error("ESTO ES UN MENSAJE GLOBAL INVOCADO POR UNA EXCEPCION QUE NO FUE ATRAPADA");
		if(isViewException && this.getAdminOrden().getArticulos().size()> 0)
		  this.toSaveRecord();
	} // doGlobalEvent
	
	public void doLoadEstatus() {
		Map<String, Object>params         = null;
		MotorBusquedaCatalogos motor      = null; 
		List<ClienteTipoContacto>contactos= null;
		Correo correoAdd                  = null;
		try {			
			this.attrs.put("consultarCorreos", false);
			motor= new MotorBusqueda(-1L, ((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente());
			if(!motor.toClienteDefault().getKey().equals(((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente())){
				contactos= motor.toClientesTipoContacto();
				LOG.warn("Inicializando listas de correos y seleccionados");
				this.correos= new ArrayList<>();
				this.selectedCorreos= new ArrayList<>();
				LOG.warn("Total de contactos" + contactos.size());
				for(ClienteTipoContacto contacto: contactos){
					if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey())){
						correoAdd= new Correo(contacto.getIdClienteTipoContacto(), contacto.getValor());
						this.correos.add(correoAdd);		
						this.selectedCorreos.add(correoAdd);
					} // if
				} // for
				LOG.warn("Agregando correo default");
				this.correos.add(new Correo(-1L, ""));
				UIBackingUtilities.execute("PF('dlgCorreos').show();");
			} // if
			else{
				JsfBase.addMessage("Generar factura", "Es necesario asignar un cliente regitrado.", ETipoMensaje.ERROR);
				UIBackingUtilities.execute("janal.desbloquear();");
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doLoadEstatus
	
	public void doAgregarCorreo() {
		UISelectEntity cliente = null;
		Transaccion transaccion= null;
		try {
			if(!Cadena.isVacio(this.correo.getDescripcion())){
				cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");
				transaccion= new Transaccion(this.correo, cliente.getKey());
				if(transaccion.ejecutar(EAccion.COMPLEMENTAR))
					JsfBase.addMessage("Se agrego el correo electronico correctamente !");
				else
					JsfBase.addMessage("Ocurrió un error al agregar el correo electronico");
			} // if
			else
				JsfBase.addMessage("Es necesario capturar un correo electronico !");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAgregarCorreo
	
	public void doLoadCorreos() {
		MotorBusquedaCatalogos motor      = null; 
		List<ClienteTipoContacto>contactos= null;
		Correo correoAdd                  = null;
		try {					
			this.attrs.put("consultarCorreos", true);
			motor= new MotorBusqueda(-1L, ((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente());
			contactos= motor.toClientesTipoContacto();
			this.correos= new ArrayList<>();
				this.selectedCorreos= new ArrayList<>();
				LOG.warn("Total de contactos: " + contactos.size());
				for(ClienteTipoContacto contacto: contactos) {
					if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey())) {
						correoAdd= new Correo(contacto.getIdClienteTipoContacto(), contacto.getValor());
						this.correos.add(correoAdd);		
						this.selectedCorreos.add(correoAdd);
					} // if
				} // for
			LOG.warn("Agregando correo default");
			this.correos.add(new Correo(-1L, ""));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doLoadCorreos	
}

package mx.org.kaana.mantic.ventas.caja.backing;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.beans.ContadoresListas;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.IBaseAttachment;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.db.dto.TcManticApartadosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETipoVenta;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.reglas.Transferir;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Facturacion;
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
	private FormatLazyModel lazyDetalleTicket;
	private EOrdenes tipoOrden;	
	private TcManticApartadosDto apartado;
	private boolean pagar;
	protected Reporte reporte;
	
	public Accion() {
		super("menudeo");
	}
	
	public Reporte getReporte() {
		return reporte;
	}	// getReporte
	
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

	public FormatLazyModel getLazyDetalleTicket() {
		return lazyDetalleTicket;
	}		

  public String doTipoMedioPago(Entity row) {
		String regresar= null;
    Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idVenta", row.toLong("idVenta"));
			Value value= (Value)DaoFactory.getInstance().toField("VistaVentasDto", "tipoMedioPago", params, "medios");
			if(value!= null)
				regresar= value.toString();
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

	@PostConstruct
  @Override
  protected void init() {				
		MotorBusqueda motorBusqueda= null;
    try {
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null ? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null ? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null ? -1L: JsfBase.getFlashAttribute("idVenta"));                        
      this.attrs.put("fechaRegistro", JsfBase.getFlashAttribute("fechaRegistro")== null ? -1L: JsfBase.getFlashAttribute("fechaRegistro"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null ? null : JsfBase.getFlashAttribute("retorno"));
			LOG.warn("Flash atributes [accion[" + this.attrs.get("accion") + "] idVenta [" + this.attrs.get("idVenta") + "] retorno [" + this.attrs.get("retorno") + "]]");
			this.attrs.put("sortOrder", "order by tc_mantic_ventas.registro desc");	
			this.attrs.put("mostrarConfirmacionFactura", true);			
      this.doInitPage();
			motorBusqueda= new MotorBusqueda(Long.valueOf(this.attrs.get("idEmpresa").toString()));
			this.attrs.put("clienteGeneral", motorBusqueda.toClienteDefault());
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.pathImage= dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");
      this.attrs.put("ticketCotizacion", null);
      this.attrs.put("ok", Boolean.FALSE);
      this.toLoadRegimenesFiscales();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
	
	public void doInitPage() {
		Calendar fechaInicio= null;
		try {
			setPrecio(ETipoVenta.MENUDEO.getNombreCampo());
			this.attrs.put("facturacionSinCorreo", false);
			this.attrs.put("titleTab", "Articulos");
			this.attrs.put("ticketLock", -1L);
			this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("buscaPorCodigo", true);
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("facturarVenta", false);
			this.attrs.put("pagarVenta", false);
			this.attrs.put("cobroVenta", false);
			this.attrs.put("clienteAsignado", false);
			this.attrs.put("tabIndex", 0);
			this.loadRangoFechas(true);			
			fechaInicio= Calendar.getInstance();
			fechaInicio.set(Calendar.DAY_OF_YEAR, fechaInicio.get(Calendar.DAY_OF_YEAR)- 30);
			this.attrs.put("fechaApartirTicket", new Date(fechaInicio.getTimeInMillis()));
			this.attrs.put("fechaHastaTicket", new Date(Calendar.getInstance().getTimeInMillis()));
			this.attrs.put("folioTicket", "");
			this.attrs.put("importeTicket", "");
			this.attrs.put("productoTicket", new UISelectEntity(-1L));
			this.attrs.put("contador", 0L);
			this.attrs.put("creditoVenta", false);
			this.attrs.put("busquedaTicketAbierto", "");
			this.pagar= false;
			this.attrs.put("activeApartado", false);			
			this.attrs.put("mostrarApartado", false);			
			this.attrs.put("apartado", false);			
			this.attrs.put("tabApartado", false);			
			this.attrs.put("pagoMinimo", "0");			
			this.attrs.put("observaciones", "");		
			this.attrs.put("disabledFacturar", false);
			this.attrs.put("disabledFacturarSwitch", false);			
			this.attrs.put("ajustePreciosCliente", true);		
			this.attrs.put("isIndividual", true);
			this.attrs.put("descuentoIndividual", 0);
			this.attrs.put("descuentoGlobal", 0);
			this.attrs.put("tipoDescuento", MENUDEO);
			this.doActivarDescuento();
			this.attrs.put("decuentoAutorizadoActivo", false);
			this.attrs.put("tipoDecuentoAutorizadoActivo", MENUDEO);
			this.apartado= new TcManticApartadosDto();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				this.loadSucursales();							
			this.loadCajas();
      if(Long.valueOf(this.attrs.get("idVenta").toString()) != -1L)
        this.attrs.put("fecha", this.attrs.get("fechaRegistro"));      
			this.doLoadTicketAbiertos();						
			this.loadBancos();
			this.loadCfdis();			
			this.loadTiposPagos();
			this.verificaLimiteCaja();
			this.doActivarCliente();
			this.loadArt();
			this.doLoadSaldos(-1L);
      if(Long.valueOf(this.attrs.get("idVenta").toString()) != -1L) {
        this.attrs.put("ticketAbierto", new UISelectEntity(new Entity(Long.valueOf(this.attrs.get("idVenta").toString()))));
        this.doAsignaTicketAbierto();
				this.attrs.put("clienteAsignado", true);
      } // if
			this.loadUltimoTicket();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doInitPage
	
	public void doCleanCaptura() {
		mx.org.kaana.mantic.ventas.reglas.Transaccion transaccion= null;
		RequestContext rc= null;
		try {
			rc= RequestContext.getCurrentInstance();
			if(!this.getAdminOrden().getArticulos().isEmpty() && getAdminOrden().getArticulos().size()>0 && getAdminOrden().getArticulos().get(0).isValid()) {
				((TicketVenta)this.getAdminOrden().getOrden()).setObservaciones(!Cadena.isVacio(this.attrs.get("observaciones")) ? ((String)this.attrs.get("observaciones")).toUpperCase() : "");
				rc.execute("jsCaja.validaAccionCaptura();");
			} // if				
			else {
				if(((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta()> 0L) {
					transaccion= new mx.org.kaana.mantic.ventas.reglas.Transaccion((TicketVenta)this.getAdminOrden().getOrden());
					transaccion.ejecutar(EAccion.ELIMINAR);
				} // if
				this.doInitPage();
				rc.execute("janal.desbloquear();jsArticulos.refreshCobroValidate();");
			} // else
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doCleanCaptura
	
	private void loadRangoFechas(boolean init) throws Exception{
		List<Entity> fechas      = null;
		Map<String, Object>params= null;
		String days              = "";
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, toCondicionEstatus(true));
			params.put("idEmpresa", this.attrs.get("idEmpresa"));			
			fechas= DaoFactory.getInstance().toEntitySet("VistaVentasDto", "fechasTicketsAbiertos", params);			
			if(init) {
				this.attrs.put("fechaInicial", fechas.isEmpty() ? new Date(Calendar.getInstance().getTimeInMillis()) : new Date(fechas.get(0).toTimestamp("registro").getTime()));			
				this.attrs.put("fecha", new Date(Calendar.getInstance().getTimeInMillis()));			
			} // if
			if(!fechas.isEmpty()) {
				for(Entity day: fechas) {
					days= days.concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, day.toTimestamp("registro"))).concat(",");
				} // for				
			} // if
			this.attrs.put("days", days);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toRangoFechasTickets
	
	private void loadArt() {
		Entity articulo= null;
		try {
			articulo= new UISelectEntity(-1L);
			articulo.put("nombre", new Value("nombre", ""));
			this.attrs.put("productoTicket", articulo);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadArt
	
	@Override
  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));			
			LOG.warn("Inicializando admin orden.");
			LOG.warn("Accion:" + eaccion.name());
			this.attrs.put("mostrarCorreos", true);
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
			this.attrs.put("servicio", ((TicketVenta)getAdminOrden().getOrden()).getIdServicio() > 0L);
			this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			loadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad
	
	@Override
	public void doCalculate(Integer index) {
		super.validatePrecioAsignado(index);
		super.doCalculate(index);		
		this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
		UIBackingUtilities.update("deudor");
	}	// doCalculate

	@Override
	protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		super.toMoveData(articulo, index); 
		this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
		UIBackingUtilities.update("deudor");
	} // toMoveData

	@Override
	protected void toMoveArticulo(Articulo articulo, Integer index) throws Exception {
		super.toMoveArticulo(articulo, index);
		this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
		UIBackingUtilities.update("deudor");
	} // toMoveArticulo
	
  public void doAutorizar() {  
    try {
      // AQUI SE DEBE DE LANZAR UN METODO DE JAVASCRIPT CON EL DIALOGO DE SOLICITAR AUTORIZACION SI SE DA EL CASO QUE LOS PRECIOS SEAN DIFERENTES A MENUDEO
      TcManticClientesDto cliente= (TcManticClientesDto)this.attrs.get("registroCliente");
      if(cliente!= null && (!Objects.equals(cliente.getEspecial(), 0D) || (cliente.getIdTipoVenta()!= null && !Objects.equals(cliente.getIdTipoVenta(), 1L)) || !Objects.equals(this.getPrecio(), "menudeo"))) {
        UIBackingUtilities.execute("autorizacion();");
        if(!Objects.equals(cliente.getKey(), Constantes.VENTA_AL_PUBLICO_GENERAL_ID_KEY)) {
          // ACTUALIZAR EL REGIMEN FISCAL PARA ESTE CLIENTE EN CASO DE QUE NO TENGA O SEA DIFERENTE
          TcManticClientesDto customer= (TcManticClientesDto)DaoFactory.getInstance().findById(TcManticClientesDto.class, cliente.getKey());
          if(this.getIkRegimenFiscal()!= null && !Objects.equals(this.getIkRegimenFiscal().getKey(), -1L) && (customer.getIdRegimenFiscal()== null || !Objects.equals(customer.getIdRegimenFiscal(), this.getIkRegimenFiscal().getKey()))) {
            customer.setIdRegimenFiscal(this.getIkRegimenFiscal().getKey());
            DaoFactory.getInstance().update(customer);
          } // if  
          TicketVenta ticketVenta= (TicketVenta)this.getAdminOrden().getOrden();
          if(this.getIkRegimenFiscal()!= null && !Objects.equals(this.getIkRegimenFiscal().getKey(), -1L))
            ticketVenta.setIdRegimenFiscal(this.getIkRegimenFiscal().getKey());
        } // if  
      } // if  
      else
        this.doAceptar();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  }
  
  public void doAceptar() {  
    Transaccion transaccion               = null;
		Boolean validarCredito                = true;
		Boolean creditoVenta                  = null;
		CreateTicket ticket                   = null;
		VentaFinalizada ventaFinalizada       = null;
		UISelectEntity cliente                = null;
		UISelectEntity seleccionado           = null;
		List<UISelectEntity> clientesSeleccion= null;
		String tipoTicket                     = null;
    try {	
			creditoVenta     = (Boolean) this.attrs.get("creditoVenta");
			cliente          = (UISelectEntity) this.attrs.get("clienteSeleccion");	
			clientesSeleccion= (List<UISelectEntity>) this.attrs.get("clientesSeleccion");
			seleccionado     = clientesSeleccion.get(clientesSeleccion.indexOf(cliente));
			if(creditoVenta)
				validarCredito= this.doValidaCreditoVenta();
			if(validarCredito) {
				ventaFinalizada= this.loadVentaFinalizada();
				transaccion = new Transaccion(ventaFinalizada);
				if (transaccion.ejecutar(EAccion.REPROCESAR)) {
					if(ventaFinalizada.isFacturar() && !ventaFinalizada.getApartado()) {
						UIBackingUtilities.addCallbackParam("facturacionOk", true);
						UIBackingUtilities.addCallbackParam("facturacion", new Facturacion(((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta(), seleccionado.getKey(), transaccion.getCorreosFactura(), seleccionado.toString("razonSocial"), ventaFinalizada.getIdTipoPago(), transaccion.getFacturaPrincipal().getIdFactura(), transaccion.getFacturaPrincipal().getIdFacturama(), transaccion.getFacturaPrincipal().getSelloSat(), ventaFinalizada.getTicketVenta().getIdClienteDomicilio()));						
					} // if
					else
						UIBackingUtilities.addCallbackParam("facturacionOk", false);
					tipoTicket= ventaFinalizada.getApartado() ? "APARTADO" : (ventaFinalizada.isFacturar() ? "FACTURA" : (ventaFinalizada.isCredito() ? "CREDITO" : "VENTA DE MOSTRADOR"));
					if(tipoTicket.equals("FACTURA") || tipoTicket.equals("CREDITO"))						
						ticket= new CreateTicket(((AdminTickets)getAdminOrden()), (Pago) this.attrs.get("pago"), tipoTicket, seleccionado.toString("razonSocial"));
					else
						ticket= new CreateTicket(((AdminTickets)getAdminOrden()), (Pago) this.attrs.get("pago"), tipoTicket);
					UIBackingUtilities.execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + ((TicketVenta)(((AdminTickets)getAdminOrden()).getOrden())).getTicket() + "','" + ticket.toHtml() + "');");
					UIBackingUtilities.execute("jsTicket.clicTicket();");
					JsfBase.addMessage("Se finalizo el pago del ticket de venta", ETipoMensaje.INFORMACION);
					this.setAdminOrden(new AdminTickets(new TicketVenta()));
					this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
					this.attrs.put("clienteSeleccion", null);
					this.init();
				} // if
				else 
					JsfBase.addMessage("Ocurrió un error al registrar el ticket de venta.", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage(this.attrs.get("mensajeErrorCredito").toString(), ETipoMensaje.ERROR);      			
      UIBackingUtilities.update("contenedorGrupos");
      UIBackingUtilities.update("@(.filas)");
      UIBackingUtilities.update("@(.recalculo)");
      UIBackingUtilities.update("contenedorGrupos:deudorPago");
      UIBackingUtilities.update("contenedorGrupos:deudor");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doAccion
	
	public void doSendMail(Facturacion facturacion) {		
		Map<String, Object> params= null;
		List<Attachment> files    = null;
		String[] emails           = null;
		File factura              = null;		
		try {
			params= new HashMap<>();
			params.put("header", "...");
			params.put("footer", "...");
			params.put("empresa", JsfBase.getAutentifica().getEmpresa().getNombre());
			params.put("tipo", "Factura");			
			params.put("razonSocial", facturacion.getRazonSocial());
			params.put("correo", ECorreos.FACTURACION.getEmail());			
			factura= toXml(facturacion.getIdFactura());
			this.doReporte("FACTURAS_FICTICIAS_DETALLE", true, facturacion.getIdVenta(), facturacion.getIdFactura(), facturacion.getIdFacturama(), facturacion.getSelloSat(), facturacion.getIdCliente());
			Attachment attachments= new Attachment(this.reporte.getNombre(), Boolean.FALSE);
			files= new ArrayList<>(); 
			files.add(attachments);
			files.add(new Attachment(factura, Boolean.FALSE));
			files.add(new Attachment("logo", ECorreos.FACTURACION.getImages().concat("logo.png"), Boolean.TRUE));
			params.put("attach", attachments.getId());
			//emails= new String[]{"jimenez76@yahoo.com", facturacion.getCorreos()};		
			emails= new String[]{facturacion.getCorreos()};		
			for (String item: emails) {
				try {
					if(!Cadena.isVacio(item)) {
					  IBaseAttachment notificar= new IBaseAttachment(ECorreos.FACTURACION, ECorreos.FACTURACION.getEmail(), item, "controlbonanza@gmail.com", "Ferreteria Bonanza - Factura", params, files);
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
			if(facturacion.getCorreos().length()> 0)
		    JsfBase.addMessage("Se envió el correo de forma exitosa.", ETipoMensaje.INFORMACION);			
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
			for(Entity factura: facturas) {
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
			if(email) { 
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
		if(this.reporte.getTotal()> 0L) {
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
	
	public void doVerificaArticulosCotizacion() {
		UISelectEntity ticketAbierto= null;
		boolean confirmacion        = false;
		String mensaje              = null;
		try {
			mensaje= "No es posible generar una cotización sin articulos";
			ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");
			if(this.attrs.get("tipo")!= null) {				
				if(!(this.attrs.get("tipo").toString().equals(EEstatusVentas.APARTADOS.name()) || this.attrs.get("tipo").toString().equals(EEstatusVentas.COTIZACION.name())))
					confirmacion= !this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size()> 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L))));									
				else
					mensaje= "Se guardo con éxito el apartado o cotización";
			} // if
			else
				confirmacion= (ticketAbierto== null || ticketAbierto.getKey().equals(-1L)) && (!this.getAdminOrden().getArticulos().isEmpty() && (getAdminOrden().getArticulos().size()> 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L)))));			
			if(confirmacion) {
				UIBackingUtilities.execute("janal.bloquear();");
				UIBackingUtilities.execute("PF('dlgCotizacion').show();");
			} //if
      else {
				JsfBase.addMessage("Cotización", mensaje, ETipoMensaje.ERROR);					
        this.toUpdateCotizacion();
      } // else  
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doVerificaArticulosCotizacion
 	
  public String doAceptarCotizacion() {	  
		UISelectEntity ticketAbierto= null;
    Transaccion transaccion     = null;
    String regresar             = null;
		CreateTicket ticket         = null;
		boolean confirmacion        = false;
    try {				
			ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");			
			if(ticketAbierto== null && !this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L)))) && this.getAdminOrden().getTotales().getImporte()> 0D) {
				this.getAdminOrden().getTotales().setTotal(this.getAdminOrden().getTotales().getImporte());
				this.loadOrdenVenta();				
			} // if
      this.getAdminOrden().validatePrecioArticulo();
      this.getAdminOrden().toAdjustArticulos();
      this.getAdminOrden().cleanPrecioDescuentoArticulo();				
      TicketVenta ticketVenta= (TicketVenta)this.getAdminOrden().getOrden();
 			ticketVenta.setTotal(this.getAdminOrden().getTotales().getTotal());
			ticketVenta.setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			ticketVenta.setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
			ticketVenta.setImpuestos(this.getAdminOrden().getTotales().getIva());
			ticketVenta.setUtilidad(this.getAdminOrden().getTotales().getUtilidad());
      transaccion= new Transaccion(ticketVenta, this.getAdminOrden().getArticulos());
			confirmacion= transaccion.ejecutar(EAccion.MOVIMIENTOS);				
			if(confirmacion) {
				((TicketVenta)(((AdminTickets)getAdminOrden()).getOrden())).setCotizacion(transaccion.getCotizacion());
				ticket= new CreateTicket(((AdminTickets)getAdminOrden()), (Pago)this.attrs.get("pago"), "COTIZACIÓN");				
				UIBackingUtilities.execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + transaccion.getCotizacion() + "','" + ticket.toHtml() + "');");
				UIBackingUtilities.execute("jsTicket.clicTicket();");
				JsfBase.addMessage("Se finalizó la cotización del ticket.", ETipoMensaje.INFORMACION);								
				this.setAdminOrden(new AdminTickets(new TicketVenta()));
				this.attrs.put("pago", new Pago(this.getAdminOrden().getTotales()));
				this.attrs.put("clienteSeleccion", null);
				this.init();
			} // if
			else
				JsfBase.addMessage("Ocurrió un error al generar la cotización.", ETipoMensaje.ERROR);			  
      this.refreshTicketsAbiertos(); 
      UIBackingUtilities.update("ticketsAbiertos");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAceptarCotizacion

	private void loadCatalog() {
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
	public void doAsignaCliente(SelectEvent event) {
		UISelectEntity seleccion              = null;
		UISelectEntity ticketAbierto          = null;
		List<UISelectEntity> clientes         = null;
		List<UISelectEntity> clientesSeleccion= null;
		Transaccion transaccion               = null;		
		boolean facturarVenta                 = false;
		MotorBusqueda motor                   = null;
		try {
			ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");
			clientes = (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);			
			motor= new MotorBusqueda(-1L);
			this.attrs.put("mostrarCorreos", seleccion== null || seleccion.getKey().equals(-1L) || seleccion.getKey().equals(motor.toClienteDefault().getKey()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			facturarVenta= (Boolean) this.attrs.get("facturarVenta");
			if(seleccion!= null && ((TicketVenta)this.getAdminOrden().getOrden()).isValid()) {				
				this.setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
				this.doReCalculatePreciosArticulos(seleccion.getKey());	
        this.attrs.put("pago", new Pago(this.getAdminOrden().getTotales()));
        VentaFinalizada ventaFinalizada= this.loadVentaFinalizada();
				transaccion= new Transaccion(ventaFinalizada, seleccion.getKey(), (String)this.attrs.get("observaciones"));
				if(transaccion.ejecutar(EAccion.ASIGNAR)) {
					this.unlockVentaExtends(-1L, (Long)this.attrs.get("ticketLock"));			
					this.attrs.put("ticketLock", -1L);
					this.doLoadTicketAbiertos();
					this.attrs.put("ticketAbierto", ticketAbierto);					
          this.attrs.put("ajustePreciosCliente", !seleccion.isEmpty() && seleccion.toDouble("especial")!= 0D);
					this.doAsignaTicketAbierto();					
					this.attrs.put("tabIndex", 1);
					this.attrs.put("facturarVenta", facturarVenta);					
					this.attrs.put("disabledFacturar", !facturarVenta);					
          // this.checkAutorizacionVenta();
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
		
	public void doLoadTicketAbiertosPrincipal() {		
		Map<String, Object>params= null;
		List<Columna> campos     = null;
		try {			
			campos= new ArrayList<>();
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));			
			campos.add(new Columna("cuenta", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, toCondicion(false));
			this.lazyCuentasAbiertas= new FormatLazyModel("VistaVentasDto", "lazy", params, campos);			
			UIBackingUtilities.execute("PF('dlgOpenTickets').show();");			
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
	
	public void refreshTicketsAbiertos() {
		Map<String, Object>params= null;
		List<Columna>campos      = null;
		try {
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			campos= new ArrayList<>();
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, toCondicion(true));			
			this.attrs.put("ticketsAbiertos", UIEntity.build("VistaVentasDto", "lazy", params, campos, Constantes.SQL_TODOS_REGISTROS));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // refreshTicketsAbiertos
	
	@Override
	public void doLoadTicketAbiertos() {		
		List<UISelectEntity> ticketsAbiertos= null;
		Map<String, Object>params           = null;
		List<Columna> campos                = null;
		List<Columna> columns               = null;
		try {
			this.loadRangoFechas(false);
			this.loadCajas();			
			params= new HashMap<>();			
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			campos= new ArrayList<>();
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, toCondicion(true));
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
			this.setDomicilio(new Domicilio());
			this.attrs.put("registroCliente", new TcManticClientesDto());
      this.setIkRegimenFiscal(new UISelectEntity(-1L));
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
			this.unlockVentaExtends(-1L, (Long)this.attrs.get("ticketLock"));			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doLoadTicketAbiertos
	
	private String toCondicion(boolean all) {
		StringBuilder regresar= null;
		Date fecha            = null;
		try {
			fecha= this.attrs.get("fecha")!= null && ((Object)this.attrs.get("fecha")) instanceof Date ? (Date) this.attrs.get("fecha") : new Date(Calendar.getInstance().getTimeInMillis());
			regresar= new StringBuilder();
			regresar.append(" date_format (tc_mantic_ventas.registro, '%Y%m%d')=".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, fecha)));
			regresar.append(toCondicionEstatus(all));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicion	
	
	private String toCondicionEstatus(boolean all) {
		StringBuilder regresar= new StringBuilder("");
		regresar.append(" and tc_mantic_ventas.candado = 2");
		regresar.append(" and tc_mantic_ventas.id_venta_estatus in (");
		regresar.append(EEstatusVentas.ELABORADA.getIdEstatusVenta());									
		regresar.append(" , ");
		regresar.append(EEstatusVentas.ABIERTA.getIdEstatusVenta());									
		if(all) {
			regresar.append(" , ");
			regresar.append(EEstatusVentas.APARTADOS.getIdEstatusVenta());									
			regresar.append(" , ");
			regresar.append(EEstatusVentas.COTIZACION.getIdEstatusVenta());									
		} // if
		regresar.append(")");
		return regresar.toString();
	} // toCOndicionEstatus
	
	public void doActualizaTicketsAbiertos() {
		List<UISelectEntity> ticketsAbiertos= null;
		List<UISelectEntity> ticketsVigentes= null;
		Map<String, Object>params           = null;
		List<Columna> campos                = null;
		try {
			if(this.attrs.get("titleTab").toString().equals("Articulos")) {
				ticketsAbiertos= (List<UISelectEntity>) this.attrs.get("ticketsAbiertos");
				params= new HashMap<>();
				params.put("sortOrder", "");
				params.put("idEmpresa", this.attrs.get("idEmpresa"));
				campos= new ArrayList<>();
				campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
				campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				params.put(Constantes.SQL_CONDICION, toCondicion(true));
				ticketsVigentes= UIEntity.build("VistaVentasDto", "lazy", params, campos, Constantes.SQL_TODOS_REGISTROS);
				if(!ticketsVigentes.isEmpty()) {
					for(UISelectEntity vigente: ticketsVigentes) {
						if(!ticketsAbiertos.isEmpty()) {
							if(ticketsAbiertos.indexOf(vigente)< 0)
								ticketsAbiertos.add(vigente);
						} // if
						else
							ticketsAbiertos.add(vigente);
					} // for
				} // if
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doActualizaTicketsAbiertos
	
	public void doAsignaTicketAbiertoDirecto() {
		try {
			this.attrs.put("facturarVenta", false);			
			this.refreshTicketsAbiertos();
			this.attrs.put("ajustePreciosCliente", true);			
			this.attrs.put("ticketAbierto", new UISelectEntity((Entity)this.attrs.get("selectedCuentaAbierta")));
			this.doAsignaTicketAbierto();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAsignaTicketAbiertoDirecto
	
	public void doAsignaTicketAbiertoCambioCliente() {
		try {
			this.attrs.put("ajustePreciosCliente", false);
			this.doAsignaTicketAbierto();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAsignaTicketAbiertoCambioCliente
	
	public void doCleanInitPage() {
		this.attrs.put("ticketAbierto", null);
		this.attrs.put("ticketCotizacion", null);
		this.doAsignaTicketAbierto();
	} // doCleanInitPage
	
	@Override
	public void doAsignaTicketAbierto() {
		Map<String, Object>params           = null;		
		UISelectEntity ticketAbierto        = null;
		UISelectEntity ticketAbiertoPivote  = null;
		List<UISelectEntity> ticketsAbiertos= null;
		Date actual                         = null;
		String tipo                         = null;
    MotorBusqueda motorBusqueda         = null;
		try {			
			ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");
			params= new HashMap<>();
			params.put("idVenta", ticketAbierto!= null? ticketAbierto.getKey(): -1L);
			this.setDomicilio(new Domicilio());
			this.attrs.put("registroCliente", new TcManticClientesDto());
			if(ticketAbierto!= null && !ticketAbierto.getKey().equals(-1L)) {				
				this.unlockVentaExtends(ticketAbierto.getKey(), (Long)this.attrs.get("ticketLock"));									
				this.attrs.put("ticketLock", ticketAbierto.getKey());
				ticketsAbiertos= (List<UISelectEntity>) this.attrs.get("ticketsAbiertos");
        int index= ticketsAbiertos.indexOf(ticketAbierto);
				if(!ticketsAbiertos.isEmpty() && index>= 0) {
				  ticketAbiertoPivote= ticketsAbiertos.get(index);
					this.attrs.put("ticketAbierto", ticketAbiertoPivote); 
					this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params), true));
					tipo= ticketAbiertoPivote.toString("tipo");
					this.attrs.put("tipo", tipo);
					this.attrs.put("mostrarApartado", tipo.equals(EEstatusVentas.APARTADOS.name()));								
					if(tipo.equals(EEstatusVentas.COTIZACION.name()) || tipo.equals(EEstatusVentas.APARTADOS.name())) {					
						if(tipo.equals(EEstatusVentas.APARTADOS.name()))
							this.asignaFechaApartado();
						actual= new Date(Calendar.getInstance().getTimeInMillis());
						if(actual.after(((TicketVenta)getAdminOrden().getOrden()).getVigencia()))
							this.generateNewVenta();					
					} // if
					this.attrs.put("observaciones", ((TicketVenta)this.getAdminOrden().getOrden()).getObservaciones());
					this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
					this.attrs.put("consecutivo", ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo());
					this.loadCatalog();
					this.doAsignaClienteTicketAbierto();
					this.attrs.put("pagarVenta", true);
					this.attrs.put("cobroVenta", true);				
					this.attrs.put("tabIndex", 0);
					this.attrs.put("creditoCliente", ticketAbiertoPivote.toLong("idCredito").equals(1L));
				} // if
			} // if
			else {				
				this.unlockVentaExtends(-1L, (Long)this.attrs.get("ticketLock"));
				this.attrs.put("ticketLock", -1L);
				this.setAdminOrden(new AdminTickets(new TicketVenta()));
        motorBusqueda= new MotorBusqueda(Long.valueOf(this.attrs.get("idEmpresa").toString()));
        this.attrs.put("clienteGeneral", motorBusqueda.toClienteDefault());
   			this.attrs.put("clienteAsignado", false);
				this.attrs.put("clienteSeleccion", null);
				this.attrs.put("pagarVenta", false);
				this.attrs.put("facturarVenta", false);
				this.attrs.put("cobroVenta", false);
				this.attrs.put("clienteAsignado", false);
				this.attrs.put("tabIndex", 0);
				this.attrs.put("creditoCliente", false);		
				this.attrs.put("observaciones", "");
			} // else			
			this.validaFacturacion();
			this.attrs.put("pago", new Pago(getAdminOrden().getTotales()));
			this.doLoadSaldos(((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente());
			this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
			UIBackingUtilities.update("deudor");
			UIBackingUtilities.update("deudorPago");
			if(tipo!= null && tipo.equals(EEstatusVentas.APARTADOS.name()))
				this.asignaAbonoApartado();
			this.doActivarCliente();
      this.toLoadRegimenesFiscales();      
			UIBackingUtilities.execute("jsArticulos.initArrayArt(" + String.valueOf(getAdminOrden().getArticulos().size()-1) + ");");
      // this.checkAutorizacionVenta();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doAsignaTicketAbiertoGeneral
	
	private void doAsignaClienteTicketAbierto() throws Exception { 		
		MotorBusqueda motorBusqueda           = null;
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		try {
			motorBusqueda= new MotorBusqueda(-1L, ((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente());
			seleccion= new UISelectEntity(motorBusqueda.toCliente());
			this.attrs.put("mostrarCorreos", seleccion.getKey().equals(-1L) || seleccion.getKey().equals(motorBusqueda.toClienteDefault().getKey()));
			this.attrs.put("clienteAsignado", !seleccion.toString("clave").equals(CLAVE_VENTA_GRAL));
			this.attrs.put("clienteRegistrado", seleccion.toString("clave").equals(CLAVE_VENTA_GRAL));
			this.attrs.put("nombreCliente", seleccion.toString("razonSocial"));
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			if((Boolean)this.attrs.get("ajustePreciosCliente")) {
				this.setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
				this.doReCalculatePreciosArticulos(seleccion.getKey());			
			} // if
			this.doActiveApartado();
		} // try
		catch (Exception e) {	
			throw e;
		} // catch		
	} // doAsignaClienteTicketAbierto
	
	private void loadCajas() {
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
	
	public void doActivarCliente() {		
		UISelectEntity cliente                = null;
		UISelectEntity seleccionado           = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motor                   = null;
		ClienteTipoContacto telefono          = null;
		ClienteTipoContacto movil             = null;
		try {						
			this.attrs.put("facturacionSinCorreo", false);
			this.attrs.put("disabledFacturar", !((Boolean)this.attrs.get("facturarVenta")));						
			cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");	
			if(cliente!= null) {
				clientesSeleccion= (List<UISelectEntity>) this.attrs.get("clientesSeleccion");
				seleccionado= clientesSeleccion.get(clientesSeleccion.indexOf(cliente));
				if(!seleccionado.toString("clave").equals(CLAVE_VENTA_GRAL)) {
					this.doAsignaDomicilioClienteInicial(seleccionado.getKey());
					motor= new MotorBusqueda(-1L, seleccionado.getKey());
					this.clientesTiposContacto= motor.toCorreosCliente();
					this.attrs.put("telefono", motor.toTelefonoCliente());
					this.attrs.put("celular", motor.toCelularCliente());					
					this.attrs.put("clienteRegistrado", true);
					loadDomiciliosFactura(seleccionado.getKey());
					this.attrs.put("facturacionSinCorreo", !this.clientesTiposContacto.isEmpty());
				} // if
				else{
					setDomicilio(new Domicilio());
					loadDefaultCollections();					
					this.attrs.put("registroCliente", new TcManticClientesDto());
          this.setIkRegimenFiscal(new UISelectEntity(-1L));
					this.clientesTiposContacto= new ArrayList<>();
					telefono= new ClienteTipoContacto();
					telefono.setSqlAccion(ESql.INSERT);
					telefono.setIdTipoContacto(ETiposContactos.TELEFONO.getKey());
					this.attrs.put("telefono", telefono);
					movil= new ClienteTipoContacto();
					movil.setSqlAccion(ESql.INSERT);
					movil.setIdTipoContacto(ETiposContactos.CELULAR.getKey());
					this.attrs.put("celular", movil);
					this.attrs.put("clienteRegistrado", ((Boolean)this.attrs.get("facturarVenta")));
				} // else
			} // if
      else {
				setDomicilio(new Domicilio());
				loadDefaultCollections();					
				this.attrs.put("registroCliente", new TcManticClientesDto());
        this.setIkRegimenFiscal(new UISelectEntity(-1L));
				this.clientesTiposContacto= new ArrayList<>();
				telefono= new ClienteTipoContacto();
				telefono.setSqlAccion(ESql.INSERT);
				telefono.setIdTipoContacto(ETiposContactos.TELEFONO.getKey());
				this.attrs.put("telefono", telefono);
				movil= new ClienteTipoContacto();
				movil.setSqlAccion(ESql.INSERT);
				movil.setIdTipoContacto(ETiposContactos.CELULAR.getKey());
				this.attrs.put("celular", movil);
				this.attrs.put("clienteRegistrado", ((Boolean)this.attrs.get("facturarVenta")));
			} // else				
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActivarCliente
	
	public void doActiveApartado() {
		boolean apartado               = true;
		UISelectEntity clienteSeleccion= null;
		Entity cliente                 = null;
		MotorBusqueda motor            = null;
		try {
			clienteSeleccion= (UISelectEntity) this.attrs.get("clienteSeleccion");
			motor= new MotorBusqueda(-1L);
			cliente= motor.toClienteDefault();
			apartado= (boolean) this.attrs.get("apartado");	
			if(apartado) {
				this.attrs.put("facturarVenta", !apartado);
				this.attrs.put("disabledFacturar", apartado);			
				this.attrs.put("clienteRegistrado", ((Boolean)this.attrs.get("facturarVenta")));				
				this.attrs.put("tabApartado", cliente.getKey().equals(clienteSeleccion.getKey()));
				this.attrs.put("pagoMinimo", Numero.formatear(Numero.NUMERO_CON_DECIMALES, (getAdminOrden().getTotales().getTotal() * Constantes.ANTICIPO)/100));
			} // if			
			else {
				this.attrs.put("clienteRegistrado", ((Boolean)this.attrs.get("clienteAsignado")));
				this.attrs.put("tabApartado", false);
				this.attrs.put("pagoMinimo", "0");
			} // else
			this.attrs.put("disabledFacturarSwitch", apartado || cliente.getKey().equals(clienteSeleccion.getKey()));			
			this.attrs.put("tabIndex", 1);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doActiveApartado
	
	public void doOpenCobro() {
		mx.org.kaana.mantic.ventas.reglas.Transaccion transaccion= null;
		UISelectEntity ticketAbierto= null;
		try {
			ticketAbierto= this.attrs.get("ticketAbierto")!= null? (UISelectEntity)this.attrs.get("ticketAbierto"): (UISelectEntity)this.attrs.get("ticketCotizacion");
			if(ticketAbierto!= null && !ticketAbierto.getKey().equals(-1L) && !this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L))))) {
				this.loadOrdenVenta();				
				transaccion = new mx.org.kaana.mantic.ventas.reglas.Transaccion(((TicketVenta)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
				this.getAdminOrden().validatePrecioArticulo();
				this.getAdminOrden().toAdjustArticulos();
				this.getAdminOrden().cleanPrecioDescuentoArticulo();
				if (transaccion.ejecutar(EAccion.REGISTRAR)) {
					this.attrs.put("tabIndex", 1);
					this.pagar= true;
					this.getAdminOrden().getArticulos().add(new ArticuloVenta(-1L));
				} // if				
			} // if
			else if(!this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L))))) {
				this.loadOrdenVenta();				
				transaccion = new mx.org.kaana.mantic.ventas.reglas.Transaccion(((TicketVenta)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
				this.getAdminOrden().validatePrecioArticulo();
				this.getAdminOrden().toAdjustArticulos();
				this.getAdminOrden().cleanPrecioDescuentoArticulo();
				if (transaccion.ejecutar(EAccion.REGISTRAR)) {				
					//UIBackingUtilities.execute("jsArticulos.back('gener\\u00F3 la cuenta ', '"+ ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");									
					this.doLoadTicketAbiertos();
					this.attrs.put("ajustePreciosCliente", false);			
					this.attrs.put("ticketAbierto", new UISelectEntity(new Entity(transaccion.getOrden().getIdVenta())));
					this.doAsignaTicketAbierto();
					this.attrs.put("tabIndex", 1);
					this.pagar= true;
				} // if
				else
					JsfBase.addMessage("Ocurrió un error al registrar la cuenta de venta.", ETipoMensaje.ERROR);
			} // else if
			else
				JsfBase.addMessage("Cobrar venta", "No se ha seleccionado ningun ticket de venta", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doOpenCobro
	
	private void loadOrdenVenta() throws Exception {		
		// this.getAdminOrden().toCheckTotales();
		MotorBusqueda motor= new MotorBusqueda(-1L);
		((TicketVenta)this.getAdminOrden().getOrden()).setIdEmpresa(Long.valueOf(this.attrs.get("idEmpresa").toString()));
		if(((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente() < 1L)
			((TicketVenta)this.getAdminOrden().getOrden()).setIdCliente(motor.toClienteDefault().getKey());
		((TicketVenta)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
		((TicketVenta)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
		((TicketVenta)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
		((TicketVenta)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
		((TicketVenta)this.getAdminOrden().getOrden()).setObservaciones(!Cadena.isVacio(this.attrs.get("observaciones")) ? ((String)this.attrs.get("observaciones")).toUpperCase() : "");
	} // loadOrdenVenta
	
	private void loadBancos() {
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
	
	private void loadTiposPagos() {
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
	
	public void doValidaCreditoCliente() {
		Boolean credito   = false;
		Boolean apartado  = false;
		RequestContext rc = null;
		try {
			credito= (Boolean) this.attrs.get("creditoVenta");
			apartado= (Boolean) this.attrs.get("apartado");
			rc= UIBackingUtilities.getCurrentInstance();
			if(apartado)				
				rc.execute("jsArticulos.validateApartado(" + Numero.formatear(Numero.NUMERO_CON_DECIMALES, (getAdminOrden().getTotales().getTotal() * Constantes.ANTICIPO)/100) + ");");
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
	
	public void doAgregarClienteTipoContacto() {
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
	
	public void doEliminarClienteTipoContacto() {
		try {			
			if(this.clientesTiposContacto.remove(this.clienteTipoContactoSeleccion)) {
				if(!this.clienteTipoContactoSeleccion.getNuevo())
					this.addDeleteList(this.clienteTipoContactoSeleccion);
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
	
	private VentaFinalizada loadVentaFinalizada() throws Exception {
		VentaFinalizada regresar  = null;
		TicketVenta ticketVenta   = null;
		List<UISelectEntity> cfdis= null;
		UISelectEntity cfdi       = null;
		Boolean facturarVenta     = false;
		Calendar calendar         = null;
		try {
			this.getAdminOrden().validatePrecioArticulo();								
			this.getAdminOrden().toCalculate();
			this.getAdminOrden().cleanPrecioDescuentoArticulo();
			ticketVenta= (TicketVenta)this.getAdminOrden().getOrden();
			ticketVenta.setTotal(this.getAdminOrden().getTotales().getTotal());
			ticketVenta.setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			ticketVenta.setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
			ticketVenta.setImpuestos(this.getAdminOrden().getTotales().getIva());
			ticketVenta.setUtilidad(this.getAdminOrden().getTotales().getUtilidad());
			facturarVenta= (Boolean) this.attrs.get("facturarVenta");
			regresar= new VentaFinalizada();
			regresar.setTotales((Pago) this.attrs.get("pago"));
			if(facturarVenta) {
				cfdis= (List<UISelectEntity>) this.attrs.get("cfdis");
				cfdi= (UISelectEntity) this.attrs.get("cfdi");
				ticketVenta.setIdUsoCfdi(cfdis.get(cfdis.indexOf(cfdi)).getKey());
				ticketVenta.setIdTipoPago(Long.valueOf(this.attrs.get("tipoPago").toString()));				
				ticketVenta.setIdTipoMedioPago(regresar.getTotales().getIdTipoMedioPago());
				if(!ETipoMediosPago.EFECTIVO.getIdTipoMedioPago().equals(regresar.getTotales().getIdTipoMedioPago())) {
					ticketVenta.setIdBanco(regresar.getTotales().getIdBanco());
					ticketVenta.setReferencia(regresar.getTotales().getReferencia());
				} // if
				regresar.setIdTipoPago(Long.valueOf(this.attrs.get("tipoPago").toString()));		
				ticketVenta.setIdClienteDomicilio(((Entity)this.attrs.get("domicilioFactura")).getKey());		
			} // if	
			else if(!((Boolean) this.attrs.get("creditoVenta"))) {
				ticketVenta.setIdTipoPago(Long.valueOf(this.attrs.get("tipoPago").toString()));				
				ticketVenta.setIdTipoMedioPago(regresar.getTotales().getIdTipoMedioPago());
				if(!ETipoMediosPago.EFECTIVO.getIdTipoMedioPago().equals(regresar.getTotales().getIdTipoMedioPago())) {
					ticketVenta.setIdBanco(regresar.getTotales().getIdBanco());
					ticketVenta.setReferencia(regresar.getTotales().getReferencia());
				} // if
				regresar.setIdTipoPago(Long.valueOf(this.attrs.get("tipoPago").toString()));		
			} // else
			ticketVenta.setObservaciones((String)this.attrs.get("observaciones"));
			regresar.setTicketVenta(ticketVenta);
			for(ClienteTipoContacto record: this.clientesTiposContacto)
				record.setIdTipoContacto(ETiposContactos.CORREO.getKey());
			regresar.setCorreosContacto(this.clientesTiposContacto);
			regresar.setCelular((ClienteTipoContacto) this.attrs.get("celular"));
			regresar.setTelefono((ClienteTipoContacto) this.attrs.get("telefono"));			
			regresar.setCliente((TcManticClientesDto) this.attrs.get("registroCliente"));
			regresar.setDomicilio(this.getDomicilio());
			regresar.setFacturar(facturarVenta);
			regresar.setCredito((Boolean) this.attrs.get("creditoVenta"));			
			regresar.setArticulos(getAdminOrden().getArticulos());
			regresar.setIdCaja(Long.valueOf(this.attrs.get("caja").toString()));
			regresar.setApartado((Boolean) this.attrs.get("apartado"));
			if(regresar.getApartado()) {
				calendar= Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_YEAR, 30);
				regresar.getTicketVenta().setVigencia(new Date(calendar.getTimeInMillis()));
			} // if
			regresar.setDetailApartado(this.apartado);
			regresar.setObservaciones((String)this.attrs.get("observaciones"));
			regresar.setTipoCuenta(this.attrs.get("tipo").toString().toUpperCase());			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadVentaFinalizada
	
	private void loadCfdis() throws Exception {
		UISelectEntity cfdiSeleccion= null;
		Map<String, Object>params= new HashMap<>();
		List<Columna> campos     = new ArrayList<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
		campos.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
		List<UISelectEntity> cfdis= UIEntity.build("TcManticUsosCfdiDto", "row", params, campos, Constantes.SQL_TODOS_REGISTROS);
		this.attrs.put("cfdis", cfdis);
		for(UISelectEntity record: cfdis) {
			if(record.toString("clave").equals(GASTOS_GENERAL_CLAVE))
				cfdiSeleccion= record;
		} // for
		this.attrs.put("cfdi", cfdiSeleccion);
	} // loadCfdis
	
	private boolean doValidaCreditoVenta() throws Exception {
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
			pago= (Pago) this.attrs.get("pago");				
			if(pago.getCambio()> 0 || pago.getPago()>= getAdminOrden().getTotales().getTotal()) {
				this.attrs.put("mensajeErrorCredito", "La venta no puede efectuarse a crédito, el pago capturado cubre el total de la venta");
				regresar= false;
			} // if
			else 
        if(!EBooleanos.SI.getIdBooleano().equals(venta.getIdAutorizar())) {								
          totalCredito= getAdminOrden().getTotales().getTotal()- (pago.getPago()- pago.getCambio());
          clientes= (List<UISelectEntity>) this.attrs.get("clientesSeleccion");
          cliente = (UISelectEntity) this.attrs.get("clienteSeleccion");
          clienteSeleccion= clientes.get(clientes.indexOf(cliente));
          regresar= totalCredito <= (clienteSeleccion.toDouble("limiteCredito") - clienteSeleccion.toDouble("saldo"));
          if(!regresar)
            this.attrs.put("mensajeErrorCredito", "El saldo de tu crédito es insuficiente para cubrir la venta");
          else
            if(this.saldoCliente.isDeudor()) {
              this.attrs.put("mensajeErrorCredito", "Crédito superado y/o plazó vencido. Consultar con crédito y cobranza !");
              regresar= false;
            } // if  
        } // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // doValidaCreditoVenta
	
	private void verificaLimiteCaja() throws Exception {
		Entity alerta            = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put("idCaja", this.attrs.get("caja"));
			alerta= (Entity) DaoFactory.getInstance().toEntity("VistaCierresCajasDto", "alerta", params);
			if(alerta!= null) {
				UIBackingUtilities.execute("janal.bloquear();");
				UIBackingUtilities.execute("PF('dlgLimiteCaja').show();");
			} // if
		} // try
		finally {
			Methods.clean(params);
		}	// finally	
	} // verificaLimiteCaja
	
	public void doTabChange(TabChangeEvent event) {
		String title= event.getTab().getTitle();
		this.pagar= title.equals("Pagar") || title.equals("Apartado");
		if(title.equals("Cliente") || title.equals("Tickets") || title.equals("Pagar") || title.equals("Apartado")) 
			((TicketVenta)this.getAdminOrden().getOrden()).setObservaciones(!Cadena.isVacio(this.attrs.get("observaciones")) ? ((String)this.attrs.get("observaciones")).toUpperCase() : "");
    else
      if(title.equals("Tickets")) {
        this.doLoadTickets();			
        UIBackingUtilities.update("contenedorGrupos:tablaTicket");
      } // if
      else
        if(title.equals("Pagar")) 
          UIBackingUtilities.execute("jsArticulos.focusCobro();");			
        else
          if(title.equals("Articulos"))
            this.attrs.put("observaciones", ((TicketVenta)this.getAdminOrden().getOrden()).getObservaciones());
		this.attrs.put("titleTab", title);		
	} // doTabChange

	public void doAplicarCambioPrecio() {
		doAplicarCambioPrecio(-1);
	}
	
	public void doAplicarCambioPrecio(Integer index) {
		CambioUsuario cambioUsuario= null;
		String cuenta              = null;
		String contrasenia         = null;
		try {
			if(!getAdminOrden().getArticulos().isEmpty()) {
				cuenta= this.attrs.get("cambioPrecioUsr").toString();
				contrasenia= this.attrs.get("passwordCambioPrecio").toString();
				cambioUsuario= new CambioUsuario(cuenta, contrasenia);
				if(cambioUsuario.validaPrivilegiosDescuentos()) {					
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
	
	public void doLoadTickets() {
		List<Columna> columns     = null;
		Map<String, Object> params= null;
		try {
			params= this.toPrepare();
      columns = new ArrayList<>();
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("devuelto", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_HORA_CORTA));      
      columns.add(new Columna("hora", EFormatoDinamicos.HORA_CORTA));     
			params.put("sortOrder", "order by tc_mantic_ventas.registro desc");
      this.lazyModelTicket = new FormatCustomLazy("VistaConsultasDto", params, columns);
      UIBackingUtilities.resetDataTable("contenedorGrupos:tablaTicket");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch				
	} // doLoadTickets
	
	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();	
		Date inicio= (Date) this.attrs.get("fechaApartirTicket");			
		Date fin= (Date) this.attrs.get("fechaHastaTicket");			
		sb.append("date_format(tc_mantic_ventas.registro, '%Y%m%d') >= ").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, inicio)).append(" and ");
		sb.append("date_format(tc_mantic_ventas.registro, '%Y%m%d') <= ").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, fin)).append(" and ");
		if(this.attrs.get("noTicket")!= null && !Cadena.isVacio(this.attrs.get("noTicket"))) {
			sb.append("tc_mantic_ventas.ticket like '%").append(this.attrs.get("noTicket")).append("%'").append(" and ");
		} // if
		if(this.attrs.get("productoTicket")!= null && !((UISelectEntity)this.attrs.get("productoTicket")).getKey().equals(-1L)) {
      Long idArticulo= ((UISelectEntity)this.attrs.get("productoTicket")).getKey();
      List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulosProductoTicket");
      if(articulos!= null && articulos.indexOf((Entity)this.attrs.get("productoTicket"))>= 0) {
				UISelectEntity item= articulos.get(articulos.indexOf((Entity)this.attrs.get("productoTicket")));
        idArticulo= item.toLong("idArticulo");
      } // if   
			sb.append("tc_mantic_ventas_detalles.id_articulo=").append(idArticulo).append(" and ");					
		} // if
		else 
      if (JsfBase.getParametro("contenedorGrupos:productoTicket_input")!= null) {
			  sb.append("tc_mantic_ventas_detalles.nombre like '%").append(JsfBase.getParametro("contenedorGrupos:productoTicket_input")).append("%' and ");					
		  } // else if
		if(this.attrs.get("importeTicket")!= null && !Cadena.isVacio(this.attrs.get("importeTicket")) && !this.attrs.get("importeTicket").toString().equals("0.00")) {												
			sb.append("tc_mantic_ventas.total like '%").append(Cadena.eliminaCaracter(this.attrs.get("importeTicket").toString(), ',')).append("%' and ");			
		} // if
		sb.append("tc_mantic_ventas.id_venta_estatus in (").append(EEstatusVentas.CREDITO.getIdEstatusVenta()).append(",").append(EEstatusVentas.TIMBRADA.getIdEstatusVenta()).append(",").append(EEstatusVentas.PAGADA.getIdEstatusVenta()).append(",").append(EEstatusVentas.TERMINADA.getIdEstatusVenta()).append(")");
		regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb);
		return regresar;		
	} // toPrepare
	
	private void loadUltimoTicket() throws Exception{
		Entity ultimoTicket      = null;
		Map<String, Object>params= null;		
		try {
			params= new HashMap<>();
			params.put("sortOrder", "order by tc_mantic_ventas.cobro desc");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ventas.id_venta_estatus in (" + EEstatusVentas.CREDITO.getIdEstatusVenta() + "," + EEstatusVentas.PAGADA.getIdEstatusVenta() + "," + EEstatusVentas.TERMINADA.getIdEstatusVenta() + "," + EEstatusVentas.TIMBRADA.getIdEstatusVenta() + ")");
      ultimoTicket= (Entity) DaoFactory.getInstance().toEntity("VistaConsultasDto", "lazy", params);
			this.attrs.put("ultimoTicketEntity", ultimoTicket);
			this.attrs.put("ultimoTicket", ultimoTicket.toString("ticket"));
			this.attrs.put("descripcionUltimoTicket", "Ticket: ".concat(ultimoTicket.toString("ticket")).concat(", Total: $").concat(ultimoTicket.toString("total")));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadUltimoTicket
	
	public void doPrintUltimoTicket() {
		this.doPrint((Entity) this.attrs.get("ultimoTicketEntity"));		
	} // doPrintUltimoTicket
	
	public void doPrintTicket() {
		this.doPrint((Entity) this.attrs.get("seleccionTicket"));
	} // doPrintTicket
	
	public void doPrint(Entity seleccionado) {
		Map<String, Object>params= null;
		CreateTicket ticket      = null;
		AdminTickets adminTicket = null;		
		Entity cliente           = null;
		MotorBusqueda motor      = null;
		try {			
			params= new HashMap<>();
			params.put("idVenta", seleccionado.toLong("idVenta"));
			adminTicket= new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params));
			if(EEstatusVentas.fromIdTipoPago(seleccionado.toLong("idVentaEstatus")).equals(EEstatusVentas.TIMBRADA)) {
				motor= new MotorBusqueda(-1L, adminTicket.getIdCliente());
				cliente= motor.toCliente();
				ticket= new CreateTicket(adminTicket, toPago(adminTicket, seleccionado.getKey()), toTipoTransaccion(seleccionado.toLong("idVentaEstatus")), cliente.toString("razonSocial"));
			} // if
			else
				ticket= new CreateTicket(adminTicket, toPago(adminTicket, seleccionado.getKey()), toTipoTransaccion(seleccionado.toLong("idVentaEstatus")));
			UIBackingUtilities.execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + toConsecutivoTicket(seleccionado.toLong("idVentaEstatus"), adminTicket) + "','" + ticket.toHtml() + "');");
			UIBackingUtilities.execute("jsTicket.clicTicket();");
			doLoadTickets();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch		
	} // doPrintTicket
	
	private String toTipoTransaccion(Long idEstatus) {
		String regresar       = null;
		EEstatusVentas estatus= null;
		try {
			estatus= EEstatusVentas.fromIdTipoPago(idEstatus);
			switch(estatus) {
				case TIMBRADA:
					regresar= "FACTURA";
					break;
				case PAGADA:				
				case TERMINADA:
					regresar= "VENTA DE MOSTRADOR";
				break;
				case COTIZACION:
					regresar= "COTIZACIÓN";
					break;
				case APARTADOS:
					regresar= "APARTADO";
					break;
				case CREDITO:
					regresar= "VENTA A CREDITO";
					break;				
			} // switch			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toTipoTransaccion
	
	private String toConsecutivoTicket(Long idEstatus, AdminTickets ticket) {
		String regresar       = null;
		EEstatusVentas estatus= null;
		try {
			estatus= EEstatusVentas.fromIdTipoPago(idEstatus);
			if(estatus.equals(EEstatusVentas.TIMBRADA) || estatus.equals(EEstatusVentas.CREDITO) || estatus.equals(EEstatusVentas.PAGADA) || estatus.equals(EEstatusVentas.TERMINADA) || estatus.equals(EEstatusVentas.APARTADOS))
				regresar= ((TicketVenta)(ticket.getOrden())).getTicket();
			else
				regresar= ((TicketVenta)(ticket.getOrden())).getCotizacion();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toTipoTransaccion
	
	private Pago toPago(AdminTickets adminTicket, Long idVenta) throws Exception {
		Pago regresar            = null;
		List<Entity> detallePago = null;
		Map<String, Object>params= null;
		ETipoMediosPago medioPago= null;
		try {
			regresar= new Pago(adminTicket.getTotales());
			params= new HashMap<>();
			params.put("idVenta", idVenta);
			detallePago= DaoFactory.getInstance().toEntitySet("TrManticVentaMedioPagoDto", "ticket", params, Constantes.SQL_TODOS_REGISTROS);
			if(!detallePago.isEmpty()) {
				for(Entity pago: detallePago) {
					medioPago= ETipoMediosPago.fromIdTipoPago(pago.toLong("idTipoMedioPago"));
					switch(medioPago) {
						case EFECTIVO:
							regresar.setEfectivo(pago.toDouble("importe"));							
							break;
						case TARJETA_CREDITO:
							regresar.setCredito(pago.toDouble("importe"));							
							regresar.setReferenciaCredito(pago.toString("referencia"));							
							break;
						case TARJETA_DEBITO:
							regresar.setDebito(pago.toDouble("importe"));							
							regresar.setReferenciaDebito(pago.toString("referencia"));							
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
		
	private void validaFacturacion() throws Exception {
		List<Entity> ticketsAbiertos= null;
		Entity ticketAbierto        = null;
		MotorBusqueda motor         = null;
		Entity cliente              = null;		
		try {
			motor= new MotorBusqueda(-1L);
			cliente= motor.toClienteDefault();
			ticketsAbiertos= (List<Entity>) this.attrs.get("ticketsAbiertos");			
			this.attrs.put("disabledFacturar", false);
			if(!ticketsAbiertos.isEmpty()) {
				ticketAbierto= (Entity) this.attrs.get("ticketAbierto");
				if(ticketAbierto!= null) {
          int index= ticketsAbiertos.indexOf(ticketAbierto);
          if(index>= 0)
					  this.attrs.put("disabledFacturar", Objects.equals(cliente.getKey(), ticketsAbiertos.get(index).toLong("idCliente")));
          else
            this.attrs.put("disabledFacturar", Boolean.TRUE);
        } // if  
			} // if						
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // validaFacturacion	
	
	@Override
	public void doAsignaCotizacion() {		
		try {			
			super.doAsignaCotizacion();						
      UISelectEntity cotizacion= new UISelectEntity((Entity)this.attrs.get("cotizacion"));
			this.attrs.put("ajustePreciosCliente", true);			
			this.attrs.put("ticketAbierto", new UISelectEntity((Entity)this.attrs.get("cotizacion")));
			this.attrs.put("ticketCotizacion", cotizacion);
      List<UISelectEntity> ticketsAbiertos= (List<UISelectEntity>) this.attrs.get("ticketsAbiertos");      
      if(ticketsAbiertos!= null && ticketsAbiertos.indexOf(cotizacion)< 0)
        ticketsAbiertos.add(cotizacion);
			this.doAsignaTicketAbierto();			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAsignaCotizacion		 
	
	@Override
	public void doAsignaApartado() {
		try {			
			super.doAsignaApartado();			
			this.attrs.put("mostrarApartado", true);			
			this.attrs.put("apartado", false);
			this.attrs.put("creditoVenta", false);
			this.attrs.put("ajustePreciosCliente", true);			
			this.attrs.put("ticketAbierto", new UISelectEntity(new Entity(Long.valueOf(this.attrs.get("apartados").toString()))));
			this.doAsignaTicketAbierto();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAsignaApartado	
	
	public List<UISelectEntity> doCompleteProductoTicket(String query) {
		this.attrs.put("codigoProductoTicket", query);
    this.doUpdateTicketArticulos();		
		return (List<UISelectEntity>)this.attrs.get("articulosProductoTicket");
	}
	
	public void doUpdateTicketArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String)this.attrs.get("codigoProductoTicket"); 
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
        this.attrs.put("articulosProductoTicket", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
			else
        this.attrs.put("articulosProductoTicket", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
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

	public void doMostrarDetalleTicket() {
		Entity seleccionado      = null;
		Map<String, Object>params= null;
		List<Columna>campos      = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionDetalle");									
			params= new HashMap<>();
			if(seleccionado!= null && !seleccionado.isEmpty()) {
				params.put("idVenta", seleccionado.getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				this.lazyDetalleTicket= new FormatLazyModel("VistaTcManticVentasDetallesDto", "detalle", params, campos);
				UIBackingUtilities.resetDataTable("tablaDetalleTicket");
				//this.seleccionDetalleTicket= (Entity) DaoFactory.getInstance().toEntity("TcManticVentasDto", "detalle", params);
				this.attrs.put("medioPagoDetalleTicket", doTipoMedioPago(seleccionado));						
				this.attrs.put("seleccionDetalleTicket", seleccionado);			
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(campos);
		} // finally
	} // doMostrarDetalleTicket
	
	private void loadDomiciliosFactura(Long idCliente) throws Exception{
		Map<String, Object>params     = new HashMap<>();
		List<UISelectEntity>domicilios= null;
		List<Columna>columns          = new ArrayList<>();
		try {
			params.put("idCliente", idCliente);
			columns.add(new Columna("calle", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("asentamiento", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("localidad", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("municipio", EFormatoDinamicos.MAYUSCULAS));
			domicilios= UIEntity.build("VistaClientesDto", "domiciliosCliente", params, columns, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("domiciliosFactura", domicilios);
			if(!domicilios.isEmpty())
				this.attrs.put("domicilioFactura", UIBackingUtilities.toFirstKeySelectEntity(domicilios));
		} // try		
		finally {
			Methods.clean(params);
		} // finally
	} // loadDomicilios
	
	public void doFacturarPendiente(Facturacion facturacion) {
		Transaccion transaccion= null;
		try {
      facturacion.setTimbrado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			transaccion= new Transaccion(facturacion);
			transaccion.ejecutar(EAccion.TRANSFORMACION);
				//doSendMail(facturacion);							
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doFacturarPendiente
	
	public void doCleanOpenTicket() {
	  this.attrs.put("ticketAbierto", null);
		this.doAsignaTicketAbierto();
	}
	
	public String doDevolucion() {
		JsfBase.setFlashAttribute("devolucionTicket", this.attrs.get("devolucionTicket"));
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Ventas/Caja/accion");
	  return "/Paginas/Mantic/Ventas/Garantias/accion".concat(Constantes.REDIRECIONAR);	
	}

	public String doFacturar() {
		JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);
		JsfBase.setFlashAttribute("idVenta", ((Entity)this.attrs.get("facturarTicket")).getKey());
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Ventas/Caja/accion");
		this.attrs.put("idVenta", -1L);
	  return "/Paginas/Mantic/Ventas/Facturas/accion".concat(Constantes.REDIRECIONAR);	
	}

	public void doValidateConfirmacionFactura() {
		boolean mostrarConfirmacionFactura= true;
		Entity clienteGeneral             = null;
		UISelectEntity cliente            = null;
		try {
			this.attrs.put("confirmarFactura", false);			
			clienteGeneral= (Entity) this.attrs.get("clienteGeneral");
			cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");	
			mostrarConfirmacionFactura= !((boolean)this.attrs.get("apartado")) && !clienteGeneral.getKey().equals(cliente.getKey()) && !((boolean)this.attrs.get("facturarVenta")) && !((boolean)this.attrs.get("creditoVenta"));
			this.attrs.put("mostrarConfirmacionFactura", mostrarConfirmacionFactura);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch
	} // doValidateConfirmacionFactura
  
  @Override
  public void doUnLockCuenta() {
    super.doUnLockCuenta();
    this.doActualizaTicketsAbiertos();
  }

 	public String toColorGarantia(Entity row) {
		return Cadena.isVacio(row.toString("garantia"))? Cadena.isVacio(row.toString("cotizacion"))? "": "janal-tr-purple": "janal-tr-lime";
	} 

  private String toUpdateCotizacion() {	  
		UISelectEntity ticketAbierto= null;
    Transaccion transaccion     = null;
    String regresar             = null;
    try {				
			ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");			
			if(ticketAbierto== null && !this.getAdminOrden().getArticulos().isEmpty() && (this.getAdminOrden().getArticulos().size() > 1 || (this.getAdminOrden().getArticulos().size()== 1 && (this.getAdminOrden().getArticulos().get(0).getIdArticulo()!= null && !this.getAdminOrden().getArticulos().get(0).getIdArticulo().equals(-1L)))) && this.getAdminOrden().getTotales().getImporte()> 0D) {
				this.getAdminOrden().getTotales().setTotal(this.getAdminOrden().getTotales().getImporte());
				this.loadOrdenVenta();				
			} // if
      this.getAdminOrden().validatePrecioArticulo();
      this.getAdminOrden().toAdjustArticulos();
      this.getAdminOrden().cleanPrecioDescuentoArticulo();				
      TicketVenta ticketVenta= (TicketVenta)this.getAdminOrden().getOrden();
 			ticketVenta.setTotal(this.getAdminOrden().getTotales().getTotal());
			ticketVenta.setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			ticketVenta.setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
			ticketVenta.setImpuestos(this.getAdminOrden().getTotales().getIva());
			ticketVenta.setUtilidad(this.getAdminOrden().getTotales().getUtilidad());
      boolean imprimir= Objects.equals(ticketVenta.getIdVentaEstatus(), EEstatusVentas.ELABORADA.getIdEstatusVenta()) || Objects.equals(ticketVenta.getIdVentaEstatus(), EEstatusVentas.COTIZACION.getIdEstatusVenta());
      transaccion = new Transaccion(ticketVenta, this.getAdminOrden().getArticulos());
			if(!transaccion.ejecutar(EAccion.MOVIMIENTOS))
				JsfBase.addMessage("Ocurrió un error al generar la cotización", ETipoMensaje.ERROR);			  
      else
        if(imprimir) {
				  ((TicketVenta)(((AdminTickets)getAdminOrden()).getOrden())).setCotizacion(transaccion.getCotizacion());
          CreateTicket ticket= new CreateTicket(((AdminTickets)getAdminOrden()), (Pago)this.attrs.get("pago"), "COTIZACIÓN");				
          UIBackingUtilities.execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + transaccion.getCotizacion() + "','" + ticket.toHtml() + "');");
          UIBackingUtilities.execute("jsTicket.clicTicket();");
          this.setAdminOrden(new AdminTickets(new TicketVenta()));
          this.attrs.put("pago", new Pago(this.getAdminOrden().getTotales()));
          this.attrs.put("clienteSeleccion", null);
          this.init();
        } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // toUpdateCotizacion

	private void toLoadRegimenesFiscales() {
		List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    List<UISelectEntity> regimenesFiscales= null;
    String rfc                = null;
    try {      
      TcManticClientesDto cliente= (TcManticClientesDto)this.attrs.get("registroCliente");
      if(cliente!= null && cliente.getRfc()!= null)
        rfc= cliente.getRfc();
      if(rfc!= null && !Cadena.isVacio(rfc) && rfc.trim().length()== 13)
        params.put("idTipoRegimenPersona", "1");      
      else 
        if(rfc!= null && !Cadena.isVacio(rfc) && rfc.trim().length()== 12)
          params.put("idTipoRegimenPersona", "2");      
        else
          params.put("idTipoRegimenPersona", "1, 2");                  
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      regimenesFiscales= (List<UISelectEntity>) UIEntity.seleccione("TcManticRegimenesFiscalesDto", "tipo", params, columns, "codigo");
			this.attrs.put("regimenesFiscales", regimenesFiscales);
      if(cliente!= null && regimenesFiscales!= null && !regimenesFiscales.isEmpty()) {
        int index= regimenesFiscales.indexOf(new UISelectEntity(cliente.getIdRegimenFiscal()== null? -1L: cliente.getIdRegimenFiscal()));
        if(index< 0)
          this.setIkRegimenFiscal(regimenesFiscales.get(0));
        else
          this.setIkRegimenFiscal(regimenesFiscales.get(index));
      } // else
      else
        this.setIkRegimenFiscal(new UISelectEntity(-1L));
    } // try
    catch (Exception e) {
			throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	} // toLoadRegimenesFiscales

	public String doCheckUser() {
		String regresar   = null;
		String cuenta     = (String)this.attrs.get("cuenta");
		String contrasenia= (String)this.attrs.get("contrasenia");
		try {
			CambioUsuario	usuario= new CambioUsuario(cuenta, contrasenia);			
			if(usuario.validaPrivilegiosDescuentos()) {
				this.attrs.put("cuenta", "");
				this.attrs.put("contrasenia", "");
				this.attrs.put("ok", Boolean.FALSE);
				UIBackingUtilities.execute("PF('widgetDialogoAutorizacion').hide();");
        this.doAceptar();
			} // if
			else
				this.attrs.put("ok", Boolean.TRUE);
	  } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
		return regresar;
	}

  private void checkAutorizacionVenta() {
    
  }

}

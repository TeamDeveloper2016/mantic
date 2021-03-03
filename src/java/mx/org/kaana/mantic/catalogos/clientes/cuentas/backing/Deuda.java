package mx.org.kaana.mantic.catalogos.clientes.cuentas.backing;

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
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosDto;
import mx.org.kaana.mantic.enums.EEstatusClientes;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@Named(value = "manticCatalogosClientesCuentasDeuda")
@ViewScoped
public class Deuda extends IBaseFilter implements Serializable {

  private static final Log LOG = LogFactory.getLog(Deuda.class);
  
  private static final long serialVersionUID = 8793667741599428879L;	
	private FormatLazyModel detallePagos;
	private FormatLazyModel pagosSegmento;
	private List<Entity> seleccionadosSegmento;

	public FormatLazyModel getDetallePagos() {
		return detallePagos;
	}

	public FormatLazyModel getPagosSegmento() {
		return pagosSegmento;
	}	

	public List<Entity> getSeleccionadosSegmento() {
		return seleccionadosSegmento;
	}

	public void setSeleccionadosSegmento(List<Entity> seleccionadosSegmento) {
		this.seleccionadosSegmento = seleccionadosSegmento;
	}	
	
  @PostConstruct
  @Override
  protected void init() {
		Long idEmpresaInicial= -1L;
    try {			
			this.seleccionadosSegmento= new ArrayList<>();
      this.attrs.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));         
			idEmpresaInicial= JsfBase.getAutentifica().getEmpresa().getIdEmpresa();
			this.attrs.put("idEmpresa", idEmpresaInicial);
			this.attrs.put("idEmpresaGeneral", idEmpresaInicial);
			this.attrs.put("idEmpresaSegmento", idEmpresaInicial);
			this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("mostrarBancoGeneral", false);
			this.attrs.put("mostrarBanco", false);
			this.attrs.put("saldar", "2");
			this.attrs.put("saldarGeneral", "2");
			this.attrs.put("saldarSegmento", "2");
			this.attrs.put("fechaPago", new Date(Calendar.getInstance().getTimeInMillis()));
			this.attrs.put("fechaPagoGeneral", new Date(Calendar.getInstance().getTimeInMillis()));
			this.attrs.put("fechaPagoSegmento", new Date(Calendar.getInstance().getTimeInMillis()));
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				this.loadSucursales();							
			this.doLoadCajas();
			this.doLoadCajasGeneral();
			this.doLoadCajasSegmento();
			this.loadBancos();
			this.loadTiposPagos();
			this.loadClienteDeuda();			
      if((Boolean)this.attrs.get("activePagoGeneral")) {
        UIBackingUtilities.execute("janal.bloquear();PF('dlgPagoSegmento').show();");
        this.doLoadCuentas();
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

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
	
	private void loadSucursales() {
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
	
	public void doLoadCajas() {
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
	
	public void doLoadCajasGeneral() {
		List<UISelectEntity> cajas= null;
		Map<String, Object>params = null;
		List<Columna> columns     = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("idEmpresa", this.attrs.get("idEmpresaGeneral"));
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			cajas=(List<UISelectEntity>) UIEntity.build("TcManticCajasDto", "cajas", params, columns);
			this.attrs.put("cajasGenerales", cajas);
			this.attrs.put("cajaGeneral", cajas.get(0));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
	} // loadCajas
	
	public void doLoadCajasSegmento() {
		List<UISelectEntity> cajas= null;
		Map<String, Object>params = null;
		List<Columna> columns     = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("idEmpresa", this.attrs.get("idEmpresaSegmento"));
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			cajas=(List<UISelectEntity>) UIEntity.build("TcManticCajasDto", "cajas", params, columns);
			this.attrs.put("cajasSegmento", cajas);
			this.attrs.put("cajaSegmento", cajas.get(0));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
	} // loadCajas
	
	private void loadClienteDeuda() throws Exception {
		Entity deuda             = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.attrs.get("idCliente"));						
			deuda= (Entity) DaoFactory.getInstance().toEntity("VistaClientesDto", "deuda", params);
			this.attrs.put("deuda", deuda);
			this.attrs.put("pago", deuda.toDouble("saldo"));
			this.attrs.put("pagoGeneral", deuda.toDouble("saldo"));
			this.attrs.put("pagoSegmento", deuda.toDouble("saldo"));
			this.attrs.put("recuperarPagoSegmento", deuda.toDouble("saldo"));
			this.doLoad();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	} // loadClienteDeuda
	
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
	  Map<String, Object> params= null;
		List<Entity> cuentas      = null;
    try {  	  
			params= new HashMap<>();
			params.put("idCliente", this.attrs.get("idCliente"));						
			params.put("sortOrder", "order by tc_mantic_ventas.ticket desc");			
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MILES_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MILES_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			this.lazyModel = new FormatCustomLazy("VistaClientesDto", "cuentas", params, columns);			
			cuentas= DaoFactory.getInstance().toEntitySet("VistaClientesDto", "cuentas", params);
			this.validaPagoGeneral(cuentas);
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
  } // doLoad
	
	private void validaPagoGeneral(List<Entity> cuentas) {
		int count= 0;
		try {
			for(Entity cuenta: cuentas) {
				if(!(cuenta.toLong("idClienteEstatus").equals(EEstatusClientes.FINALIZADA.getIdEstatus())))
					count++;
			} // for
			this.attrs.put("activePagoGeneral", count>0);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
			throw e;
		} // catch
	} // validaPagogeneral
	
	public void doLoadCuentas() {
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
		try {
			this.seleccionadosSegmento= new ArrayList<>();
			params= new HashMap<>();
			params.put("idCliente", this.attrs.get("idCliente"));						
			params.put("sortOrder", "order by dias desc");
			params.put(Constantes.SQL_CONDICION, " tc_mantic_clientes_deudas.saldo > 0 and tc_mantic_clientes_deudas.id_cliente_estatus not in(".concat(EEstatusClientes.FINALIZADA.getIdEstatus().toString()).concat(")"));			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MILES_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MILES_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));						
			this.pagosSegmento= new FormatLazyModel("VistaClientesDto", "cuentas", params, columns);      
      UIBackingUtilities.resetDataTable("tablaSegmentos");		
		} // try 
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch 		
	} 
  
	public String doRegresar() {	  
    JsfBase.setFlashAttribute("idCliente", this.attrs.get("idCliente"));
		return "saldos".concat(Constantes.REDIRECIONAR);
	} // doRegresar
	
	public void doRegistrarPago() {
		Transaccion transaccion      = null;
		TcManticClientesPagosDto pago= null;
		boolean tipoPago             = false;
		boolean saldar               = false;
		try {
			if(this.validaPago()) {
				saldar= Long.valueOf(this.attrs.get("saldar").toString()).equals(1L);
				pago= new TcManticClientesPagosDto();
				pago.setIdClienteDeuda(((Entity)this.attrs.get("seleccionado")).getKey());
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observaciones").toString());
				pago.setPago((Double)this.attrs.get("pago"));
				pago.setFechaPago((Date)this.attrs.get("fechaPago"));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("caja").toString()), Long.valueOf((String)this.attrs.get("idCliente")), Long.valueOf(this.attrs.get("idEmpresa").toString()), tipoPago ? -1L : Long.valueOf(this.attrs.get("banco").toString()), tipoPago ? "" : this.attrs.get("referencia").toString(), saldar);
				if(transaccion.ejecutar(EAccion.AGREGAR)) {
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta.", ETipoMensaje.INFORMACION);
					loadClienteDeuda();					
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago.", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doRegistrarPago
	
	private boolean validaPago() {
		boolean regresar= false;
		Double pago     = 0D;
		Double saldo    = 0D;
		Entity deuda    = null;
		try {
			pago = (Double)this.attrs.get("pago");
			deuda= (Entity)this.attrs.get("seleccionado");
			if(pago> 0D && !deuda.toLong("idClienteEstatus").equals(EEstatusClientes.FINALIZADA.getIdEstatus())) {
				saldo   = Numero.toRedondearSat(Double.valueOf(deuda.toString("saldo")));
				regresar= pago<= saldo;
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch
		return regresar;
	} // validaPago
	
	public void onRowToggle(ToggleEvent event) {
		try {
			this.attrs.put("registroSeleccionado", (Entity) event.getData());
			if (!event.getVisibility().equals(Visibility.HIDDEN)) 
				loadHistorialPagos();			
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // onRowToggle
	
	private void loadHistorialPagos() throws Exception {
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {  	  
			params= new HashMap<>();
			params.put("idClienteDeuda", ((Entity)this.attrs.get("registroSeleccionado")).getKey());			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("pago", EFormatoDinamicos.MILES_CON_DECIMALES));
			columns.add(new Columna("saldo", EFormatoDinamicos.MILES_CON_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MILES_CON_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
			this.detallePagos = new FormatCustomLazy("VistaClientesDto", "pagosDeuda", params, columns);
      UIBackingUtilities.resetDataTable("tablaDetalle");		
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally			
	} // loadHistorialPagos
	
	public void doRegistrarPagoGeneral() {
		Transaccion transaccion      = null;
		TcManticClientesPagosDto pago= null;
		boolean tipoPago             = false;
		boolean saldar               = false;
		try {
			if(this.validaPagoGeneral()) {
				saldar= Long.valueOf((String)this.attrs.get("saldarGeneral")).equals(1L);
				pago= new TcManticClientesPagosDto();
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones((String)this.attrs.get("observacionesGeneral"));
				pago.setPago((Double)this.attrs.get("pagoGeneral"));
				pago.setFechaPago((Date)this.attrs.get("fechaPagoGeneral"));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("cajaGeneral").toString()), Long.valueOf((String)this.attrs.get("idCliente")), Long.valueOf(this.attrs.get("idEmpresaGeneral").toString()), tipoPago ? -1L : Long.valueOf(this.attrs.get("bancoGeneral").toString()), tipoPago ? "" : this.attrs.get("referenciaGeneral").toString(), saldar);
				if(transaccion.ejecutar(EAccion.PROCESAR)) {
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta.");
					this.loadClienteDeuda();					
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago.", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doRegistrarPago
	
	public void doRegistrarPagoSegmento() {
		Transaccion transaccion      = null;
		TcManticClientesPagosDto pago= null;
		boolean tipoPago             = false;
		boolean saldar               = false;
		try {
			if(this.validaPagoSegmento() && !this.seleccionadosSegmento.isEmpty()) { 
				saldar= Long.valueOf(this.attrs.get("saldarSegmento").toString()).equals(1L);
				pago= new TcManticClientesPagosDto();
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones((String)this.attrs.get("observacionesSegmento"));
				pago.setPago((Double)this.attrs.get("pagoSegmento"));
				pago.setFechaPago((Date)this.attrs.get("fechaPagoSegmento"));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("cajaSegmento").toString()), Long.valueOf((String)this.attrs.get("idCliente")), Long.valueOf(this.attrs.get("idEmpresaSegmento").toString()), tipoPago ? -1L : Long.valueOf(this.attrs.get("bancoSegmento").toString()), tipoPago ? "" : this.attrs.get("referenciaSegmento").toString(), this.seleccionadosSegmento, saldar);
				if(transaccion.ejecutar(EAccion.COMPLEMENTAR)) {
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta.");
					this.loadClienteDeuda();					
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago.", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante, ademas se debe de seleecionar una cuenta.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.seleccionadosSegmento= new ArrayList<>();
		} // finally
	} // doRegistrarPago
	
	private boolean validaPagoGeneral() {
		boolean regresar= false;
		Double pago     = 0D;
		Double saldo    = 0D;
		Entity deuda    = null;
		try {
			pago= (Double)this.attrs.get("pagoGeneral");
			if(pago> 0D) {
				deuda= (Entity)this.attrs.get("deuda");
				saldo= Numero.toRedondearSat(deuda.toDouble("saldo"));
				regresar= pago<= saldo;
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch
		return regresar;
	} // validaPagoGeneral
	
	private boolean validaPagoSegmento() {
		boolean regresar= false;
		Double pago     = 0D;
		Double saldo    = 0D;
		try {
			pago= (Double)this.attrs.get("pagoSegmento");
			if(pago > 0D) {
				for(Entity cuenta: this.seleccionadosSegmento)					
					saldo+= Numero.toRedondearSat(cuenta.toDouble("saldo"));
				regresar= pago<= Numero.toRedondearSat(saldo);
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch
		return regresar;
	} // validaPagoSegmento
	
	private void loadTiposPagos() {
		List<UISelectEntity> tiposPagos= null;
		Map<String, Object>params      = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cobro_caja=1");
			tiposPagos= UIEntity.build("TcManticTiposMediosPagosDto", "row", params);
			this.attrs.put("tiposPagos", tiposPagos);
			this.attrs.put("tipoPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadTiposPagos
	
	public void doValidaTipoPago() {
		Long tipoPago= -1L;
		try {
			tipoPago= Long.valueOf(this.attrs.get("tipoPago").toString());
			this.attrs.put("mostrarBanco", !ETipoMediosPago.EFECTIVO.getIdTipoMedioPago().equals(tipoPago));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doValidaTipoPago
	
	public void doValidaTipoPagoGeneral() {
		Long tipoPago= -1L;
		try {
			tipoPago= Long.valueOf(this.attrs.get("tipoPagoGeneral").toString());
			this.attrs.put("mostrarBancoGeneral", !ETipoMediosPago.EFECTIVO.getIdTipoMedioPago().equals(tipoPago));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doValidaTipoPagoGeneral
	
	public void doValidaTipoPagoSegmento() {
		Long tipoPago= -1L;
		try {
			tipoPago= Long.valueOf(this.attrs.get("tipoPagoSegmento").toString());
			this.attrs.put("mostrarBancoSegmento", !ETipoMediosPago.EFECTIVO.getIdTipoMedioPago().equals(tipoPago));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doValidaTipoPagoSegmento
	
	public void doActualizaPago() {
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
			this.attrs.put("pago", seleccionado.toDouble("saldo"));
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doActualizaPago
  
  public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Registro pago")) {
      // seleccionadosSegmento
      if(!this.seleccionadosSegmento.isEmpty()) { 
        Double saldo= 0D;
        for(Entity cuenta: this.seleccionadosSegmento)					
          saldo+= Double.valueOf(cuenta.toString("saldo"));
        this.attrs.put("pagoSegmento", saldo);
      } // if
      else
        this.attrs.put("pagoSegmento", this.attrs.get("recuperarPagoSegmento"));
    } // if
  }  
 
  public void doRowSeleccionado() {
    LOG.info(this.seleccionadosSegmento.size());
  }
  
}
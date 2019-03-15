package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.enums.EEstatusEmpresas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@Named(value = "manticCatalogosEmpresasCuentasDeuda")
@ViewScoped
public class Deuda extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;	
	private FormatLazyModel detallePagos;
	private FormatLazyModel pagosSegmento;
	private List<Entity> seleccionadosSegmento;
	private FormatLazyModel notasEntradaFavor;
	private FormatLazyModel notasCreditoFavor;
	private List<Entity> seleccionadosNotas;
	private List<Entity> seleccionadosCredito;

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
	
	public FormatLazyModel getNotasEntradaFavor() {
		return notasEntradaFavor;
	}

	public FormatLazyModel getNotasCreditoFavor() {
		return notasCreditoFavor;
	}

	public List<Entity> getSeleccionadosNotas() {
		return seleccionadosNotas;
	}

	public void setSeleccionadosNotas(List<Entity> seleccionadosNotas) {
		this.seleccionadosNotas = seleccionadosNotas;
	}

	public List<Entity> getSeleccionadosCredito() {
		return seleccionadosCredito;
	}

	public void setSeleccionadosCredito(List<Entity> seleccionadosCredito) {
		this.seleccionadosCredito = seleccionadosCredito;
	}		
	
  @PostConstruct
  @Override
  protected void init() {
		Long idEmpresaInicial= -1L;
    try {			
			if(JsfBase.getFlashAttribute("idProveedor")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
			this.seleccionadosSegmento= new ArrayList<>();
      this.attrs.put("sortOrder", "order by	tc_mantic_empresas_deudas.registro desc");
      this.attrs.put("idProveedor", JsfBase.getFlashAttribute("idProveedor"));         
			idEmpresaInicial= JsfBase.getAutentifica().getEmpresa().getIdEmpresa();
			this.attrs.put("idEmpresa", idEmpresaInicial);
			this.attrs.put("idEmpresaGeneral", idEmpresaInicial);
			this.attrs.put("idEmpresaSegmento", idEmpresaInicial);
			this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda"));     
			this.attrs.put("mostrarBancoSegmento", false);
			this.attrs.put("mostrarBancoGeneral", false);
			this.attrs.put("mostrarBanco", false);			
			this.attrs.put("pago", 1D);
			this.attrs.put("pagoGeneral", 1D);
			this.attrs.put("pagoSegmento", 1D);
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				loadSucursales();							
			doLoadCajas();
			doLoadCajasGeneral();
			doLoadCajasSegmento();
			loadBancos();
			loadTiposPagos();
			loadProvedorDeuda();			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

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
	
	public void doLoadCajas(){
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
	
	public void doLoadCajasGeneral(){
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
	
	public void doLoadCajasSegmento(){
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
	
	private void loadProvedorDeuda() throws Exception{
		Entity deuda             = null;
		Map<String, Object>params= null;
    List<Columna> columns    = null;
		try {
			params = new HashMap<>();
      columns= new ArrayList<>();  
			params.put("idProveedor", this.attrs.get("idProveedor"));						
			deuda= (Entity) DaoFactory.getInstance().toEntity("VistaEmpresasDto", "deuda", params);
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("debe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			UIBackingUtilities.toFormatEntity(deuda, columns);
			this.attrs.put("deuda", deuda);
			this.attrs.put("saldoPositivo", Double.valueOf(deuda.toString("saldo")) * -1);
			this.attrs.put("pago", deuda.toDouble("saldo")* -1);
			this.attrs.put("pagoGeneral", deuda.toDouble("saldo")* -1);
			this.attrs.put("pagoSegmento", deuda.toDouble("saldo")* -1);
			doLoad();
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
			params.put("idProveedor", this.attrs.get("idProveedor"));						
			params.put("sortOrder", this.attrs.get("sortOrder"));			
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));
			this.lazyModel = new FormatCustomLazy("VistaEmpresasDto", "cuentasProveedor", params, columns);			
			cuentas= DaoFactory.getInstance().toEntitySet("VistaEmpresasDto", "cuentasProveedor", params);
			validaPagoGeneral(cuentas);
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
	
	private void validaPagoGeneral(List<Entity> cuentas){
		int count= 0;
		try {
			for(Entity cuenta: cuentas){
				if(!(cuenta.toLong("idEmpresaEstatus").equals(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa())))
					count++;
			} // for
			this.attrs.put("activePagoGeneral", count > 0);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
			throw e;
		} // catch		
	} // validaPagogeneral
	
	public void doLoadCuentas(){
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
		try {
			this.seleccionadosSegmento= new ArrayList<>();
			params= new HashMap<>();
			params.put("idProveedor", this.attrs.get("idProveedor"));						
			params.put("sortOrder", this.attrs.get("sortOrder"));			
			params.put(Constantes.SQL_CONDICION, " tc_mantic_empresas_deudas.saldo < 0 and tc_mantic_empresas_deudas.id_empresa_estatus not in(".concat(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa().toString()).concat(")"));			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));						
			this.pagosSegmento= new FormatLazyModel("VistaEmpresasDto", "cuentasProveedor", params, columns);      
      UIBackingUtilities.resetDataTable("tablaSegmentos");		
		} // try 
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch 		
	}

	public String doRegresar() {	  
		JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
		return "saldos".concat(Constantes.REDIRECIONAR);
	} // doRegresar
	
	public void doRegistrarPago() {
		Transaccion transaccion      = null;
		TcManticEmpresasPagosDto pago= null;
		boolean tipoPago             = false;
		try {
			if(validaPago()){
				pago= new TcManticEmpresasPagosDto();
				pago.setIdEmpresaDeuda(((Entity)this.attrs.get("seleccionado")).getKey());
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observaciones").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pago").toString()));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("caja").toString()), -1L, Long.valueOf(this.attrs.get("idEmpresa").toString()), tipoPago ? -1L : Long.valueOf(this.attrs.get("banco").toString()), tipoPago ? "" : this.attrs.get("referencia").toString(), null, false, this.seleccionadosNotas, this.seleccionadosCredito);
				if(transaccion.ejecutar(EAccion.AGREGAR)){
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta", ETipoMensaje.INFORMACION);
					loadProvedorDeuda();					
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante y distinto a 0. o la cuenta ya se encuentra finalizada.", ETipoMensaje.ERROR);
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
			pago= Double.valueOf(this.attrs.get("pago").toString());
			deuda= (Entity) this.attrs.get("seleccionado");
			if(pago > 0D && !deuda.toLong("idEmpresaEstatus").equals(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa())){				
				saldo= Double.valueOf(deuda.toString("saldo"));
				regresar= pago<= (saldo * -1);
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
	
	private void loadHistorialPagos() throws Exception{
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {  	  
			params= new HashMap<>();
			params.put("idEmpresaDeuda", ((Entity)this.attrs.get("registroSeleccionado")).getKey());			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("pago", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
			this.detallePagos = new FormatCustomLazy("VistaEmpresasDto", "pagosDeuda", params, columns);
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
	
	public void doRegistrarPagoGeneral(){
		Transaccion transaccion      = null;
		TcManticEmpresasPagosDto pago= null;
		boolean tipoPago             = false;
		try {
			if(validaPagoGeneral()){
				pago= new TcManticEmpresasPagosDto();
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observacionesGeneral").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pagoGeneral").toString()));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("cajaGeneral").toString()), Long.valueOf(this.attrs.get("idProveedor").toString()), Long.valueOf(this.attrs.get("idEmpresaGeneral").toString()), tipoPago ? -1L : Long.valueOf(this.attrs.get("bancoGeneral").toString()), tipoPago ? "" : this.attrs.get("referenciaGeneral").toString(), false);
				if(transaccion.ejecutar(EAccion.PROCESAR)){
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta");
					loadProvedorDeuda();					
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante y distinto a 0.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doRegistrarPagoGeneral
	
	public void doRegistrarPagoSegmento(){
		Transaccion transaccion      = null;
		TcManticEmpresasPagosDto pago= null;
		boolean tipoPago             = false;		
		try {
			if(validaPagoSegmento() && !this.seleccionadosSegmento.isEmpty()){ 
				pago= new TcManticEmpresasPagosDto();
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observacionesSegmento").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pagoSegmento").toString()));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("cajaSegmento").toString()), Long.valueOf(this.attrs.get("idProveedor").toString()), Long.valueOf(this.attrs.get("idEmpresaSegmento").toString()), tipoPago ? -1L : Long.valueOf(this.attrs.get("bancoSegmento").toString()), tipoPago ? "" : this.attrs.get("referenciaSegmento").toString(), this.seleccionadosSegmento, false);
				if(transaccion.ejecutar(EAccion.COMPLEMENTAR)){
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta");
					loadProvedorDeuda();					
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante y distinto a 0. ó no se selecciono ninguna cuenta.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.seleccionadosSegmento= new ArrayList<>();
		} // finally
	} // doRegistrarPago
	
	private boolean validaPagoGeneral(){
		boolean regresar= false;
		Double pago     = 0D;
		Double saldo    = 0D;
		Entity deuda    = null;
		try {
			pago= Double.valueOf(this.attrs.get("pagoGeneral").toString());
			if(pago > 0D){
				deuda= (Entity) this.attrs.get("deuda");
				saldo= Double.valueOf(deuda.toString("saldo"));
				regresar= pago<= (saldo * -1);
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch
		return regresar;
	} // validaPagoGeneral
	
	private boolean validaPagoSegmento(){
		boolean regresar= false;
		Double pago     = 0D;
		Double saldo    = 0D;
		try {
			pago= Double.valueOf(this.attrs.get("pagoSegmento").toString());
			if(pago > 0D){
				for(Entity cuenta: this.seleccionadosSegmento)					
					saldo= saldo + Double.valueOf(cuenta.toString("saldo"));
				regresar= pago<= (saldo * -1);
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch
		return regresar;
	} // validaPagoGeneral
	
	private void loadTiposPagos(){
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
	
	public void doValidaTipoPago(){
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
	
	public void doValidaTipoPagoGeneral(){
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
	
	public void doValidaTipoPagoSegmento(){
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
	
	public void doLoadCuentasAFavor(){
		Entity seleccionado= null;		
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
			this.attrs.put("pago", (seleccionado.toDouble("saldo") * -1));
			doLoadNotasEntradas();
			doLoadNotasCredito();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doLoadCuentas
	
	private void doLoadNotasEntradas(){
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
		try {
			this.seleccionadosNotas= new ArrayList<>();
			params= new HashMap<>();
			params.put("idProveedor", this.attrs.get("idProveedor"));														
			params.put("idEmpresaEstatus", EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa());														
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));						
			this.notasEntradaFavor= new FormatLazyModel("VistaEmpresasDto", "saldoFavorEntradas", params, columns);      
      UIBackingUtilities.resetDataTable("tablaNotas");		
		} // try 
		catch (Exception e) {			
			throw e;
		} // catch		
	} // doLoadNotasEntradas
	
	private void doLoadNotasCredito(){
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
		try {
			this.seleccionadosCredito= new ArrayList<>();
			params= new HashMap<>();
			params.put("idProveedor", this.attrs.get("idProveedor"));						
			params.put("idCreditoEstatus", EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa() + "," + EEstatusEmpresas.PROGRAMADA.getIdEstatusEmpresa());																	
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));						
			this.notasCreditoFavor= new FormatLazyModel("VistaCreditosNotasDto", "saldoFavorCreditos", params, columns);      
      UIBackingUtilities.resetDataTable("tablaCreditos");		
		} // try 
		catch (Exception e) {			
			throw e;
		} // catch		
	} // doLoadNotasCredito
}
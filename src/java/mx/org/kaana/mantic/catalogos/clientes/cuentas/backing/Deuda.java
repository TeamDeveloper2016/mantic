package mx.org.kaana.mantic.catalogos.clientes.cuentas.backing;

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
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosDto;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@Named(value = "manticCatalogosClientesCuentasDeuda")
@ViewScoped
public class Deuda extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;	
	private FormatLazyModel detallePagos;

	public FormatLazyModel getDetallePagos() {
		return detallePagos;
	}
		
  @PostConstruct
  @Override
  protected void init() {
    try {			
      this.attrs.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));           
			loadClienteDeuda();
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadClienteDeuda() throws Exception{
		Entity deuda             = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.attrs.get("idCliente"));						
			deuda= (Entity) DaoFactory.getInstance().toEntity("VistaClientesDto", "deuda", params);
			this.attrs.put("deuda", deuda);
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
    try {  	  
			params= new HashMap<>();
			params.put("idCliente", this.attrs.get("idCliente"));						
			params.put("sortOrder", this.attrs.get("sortOrder"));			
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			this.lazyModel = new FormatCustomLazy("VistaClientesDto", "cuentas", params, columns);
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

	public String doRegresar() {	  
		return "saldos".concat(Constantes.REDIRECIONAR);
	} // doRegresar
	
	public void doRegistrarPago(){
		Transaccion transaccion      = null;
		TcManticClientesPagosDto pago= null;
		try {
			if(validaPago()){
				pago= new TcManticClientesPagosDto();
				pago.setIdClienteDeuda(((Entity)this.attrs.get("seleccionado")).getKey());
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observaciones").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pago").toString()));
				transaccion= new Transaccion(pago);
				if(transaccion.ejecutar(EAccion.AGREGAR)){
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta", ETipoMensaje.INFORMACION);
					loadClienteDeuda();
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
	} // doRegistrarPago
	
	private boolean validaPago(){
		boolean regresar= false;
		Double pago     = 0D;
		Double saldo    = 0D;
		Entity deuda    = null;
		try {
			pago= Double.valueOf(this.attrs.get("pago").toString());
			if(pago > 0D){
				deuda= (Entity) this.attrs.get("seleccionado");
				saldo= Double.valueOf(deuda.toString("saldo"));
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
	
	private void loadHistorialPagos() throws Exception{
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {  	  
			params= new HashMap<>();
			params.put("idClienteDeuda", ((Entity)this.attrs.get("registroSeleccionado")).getKey());			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("pago", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
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
	
	public void doRegistrarPagoGeneral(){
		Transaccion transaccion      = null;
		TcManticClientesPagosDto pago= null;
		try {
			if(validaPagoGeneral()){
				pago= new TcManticClientesPagosDto();
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observacionesGeneral").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pagoGeneral").toString()));
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("idCliente").toString()));
				if(transaccion.ejecutar(EAccion.PROCESAR)){
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta");
					loadClienteDeuda();
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
				regresar= pago<= saldo;
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch
		return regresar;
	} // validaPagoGeneral
}
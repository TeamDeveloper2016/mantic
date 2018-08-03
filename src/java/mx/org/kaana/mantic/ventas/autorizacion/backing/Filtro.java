package mx.org.kaana.mantic.ventas.autorizacion.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
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
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.ventas.autorizacion.reglas.Transaccion;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@Named(value = "manticVentasAutorizacionFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final long serialVersionUID = 5570593377763068163L;
	private FormatLazyModel detallePagos;
	private UISelectEntity encontrado;  
	
	public FormatLazyModel getDetallePagos() {
		return detallePagos;
	}	

	public UISelectEntity getEncontrado() {
		return encontrado;
	}

	public void setEncontrado(UISelectEntity encontrado) {
		this.encontrado=encontrado;
	}	
	
  @PostConstruct
  @Override
  protected void init() {
    try {			
      this.attrs.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");      
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {  	  
			params= new HashMap<>();
			params.put("idCliente", this.attrs.get("cliente"));						
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
	
	public void doClientes() {
		List<UISelectEntity> clientes= null;
    Map<String, Object> params   = null;
		List<Columna> columns        = null;
    try {
			columns= new ArrayList<>();
			if(this.attrs.get("busqueda")!= null && this.attrs.get("busqueda").toString().length()> 2) {
				params = new HashMap<>();      
				params.put(Constantes.SQL_CONDICION, "upper(razon_social) like upper('%".concat((String)this.attrs.get("busqueda")).concat("%')"));
				params.put("idEmpresa", this.attrs.get("idEmpresa"));
				columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));			
				columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));			
				clientes = UIEntity.build("VistaClientesDto", "findRazonSocial", params, columns, Constantes.SQL_TODOS_REGISTROS);      
				this.attrs.put("clientes", clientes);      
				this.attrs.put("resultados", clientes.size());      
			} // if
			else 
				JsfBase.addMessage("Cliente", "Favor de teclear por lo menos 3 caracteres.", ETipoMensaje.ALERTA);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	} // doClientes
	
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
	
	public void doSeleccionado() {
		List<UISelectEntity> listado= null;
		List<UISelectEntity> unico  = null;
		UISelectEntity cliente      = null;
		try {
			listado= (List<UISelectEntity>) this.attrs.get("clientes");
			cliente= listado.get(listado.indexOf(this.encontrado));
			this.attrs.put("cliente", cliente);						
			unico  = new ArrayList<>();
			unico.add(cliente);
			this.attrs.put("unico", unico);				
			doLoadTicketAbiertos();
			doLoad();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doSeleccionado		
	
	public void doLoadTicketAbiertos(){
		List<UISelectEntity> ticketsAbiertos= null;
		Map<String, Object>params           = null;
		List<Columna> campos                = null;
		try {
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			campos= new ArrayList<>();
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, toCondicion());
			ticketsAbiertos= UIEntity.build("VistaVentasDto", "lazy", params, campos, Constantes.SQL_TODOS_REGISTROS);
			if(!ticketsAbiertos.isEmpty()){
				this.attrs.put("ticketsAbiertos", ticketsAbiertos);			
				this.attrs.put("ticketAbierto", UIBackingUtilities.toFirstKeySelectEntity(ticketsAbiertos));			
				this.attrs.put("ventaDetalle", ticketsAbiertos.get(0));			
			} // if
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
		try {
			regresar= new StringBuilder();
			regresar.append(" tc_mantic_ventas.id_venta_estatus=");
			regresar.append(EEstatusVentas.ABIERTA.getIdEstatusVenta());
			regresar.append(" and tc_mantic_clientes.id_cliente=");
			regresar.append(this.attrs.get("cliente"));
			regresar.append(" and tc_mantic_ventas.id_autorizar=");
			regresar.append(EBooleanos.NO.getIdBooleano());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicion
	
	public void doAutorizar(){
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(((Entity)this.attrs.get("ventaDetalle")).getKey());
			if(transaccion.ejecutar(EAccion.MODIFICAR))
				JsfBase.addMessage("Autorizar venta", "Se autorizo la venta de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Autorizar venta", "Ocurrió un error al autorizar la venta", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doAutorizar
	
	public String doDetalleVenta(){
		String regresar= null;
		Entity venta   = null;
		try {
			if(this.attrs.get("ventaDetalle")!= null){
				venta= (Entity) this.attrs.get("ventaDetalle");
				JsfBase.setFlashAttribute("idVenta", venta.getKey());
				regresar= "detalle".concat(Constantes.REDIRECIONAR);
			} // if
			else
				JsfBase.addMessage("Detalle de la venta", "No se tiene ninguna venta pendiente por autorizar", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doDetalleArticulos
	
	public String doDetalleCredito(){
		String regresar= null;
		Entity venta   = null;
		try {
			venta= (Entity) this.attrs.get("seleccionado");
			JsfBase.setFlashAttribute("idVenta", venta.getKey());			
			regresar= "detalle".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doDetalleArticulos
	
	public void doLoadDetalle(){
		List<UISelectEntity> ticketsAbiertos= null;
		UISelectEntity ticketAbierto        = null;
		try {
			ticketsAbiertos= (List<UISelectEntity>) this.attrs.get("ticketsAbiertos");
			ticketAbierto= (UISelectEntity) this.attrs.get("ticketAbierto");
			this.attrs.put("ventaDetalle", ticketsAbiertos.get(ticketsAbiertos.indexOf(ticketAbierto)));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doLoadDetalle
}
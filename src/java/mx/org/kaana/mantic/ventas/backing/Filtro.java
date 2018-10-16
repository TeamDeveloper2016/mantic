package mx.org.kaana.mantic.ventas.backing;

import java.io.Serializable;
import java.sql.Date;
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
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETipoPago;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.caja.reglas.CreateTicket;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import org.primefaces.context.RequestContext;

@Named(value= "manticVentasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;
	private Reporte reporte;
	
	public Reporte getReporte() {
		return reporte;
	}	// getReporte
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta"));
      this.attrs.put("sortOrder", "order by tc_mantic_ventas.id_empresa, tc_mantic_ventas.ejercicio, tc_mantic_ventas.orden");
			toLoadCatalog();
      if(this.attrs.get("idVenta")!= null) 
			  this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= toPrepare();
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      this.lazyModel = new FormatCustomLazy("VistaVentasDto", params, columns);
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

  public String doAccion(String accion) {
    EAccion eaccion= null;
		String regresar= "/Paginas/Mantic/Ventas/accion".concat(Constantes.REDIRECIONAR); 
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Ventas/filtro");					
			JsfBase.setFlashAttribute("idVenta", eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
			if(eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)){
				if(((Entity)this.attrs.get("seleccionado")).toString("idManual").equals("1"))
					regresar= "/Paginas/Mantic/Ventas/express".concat(Constantes.REDIRECIONAR); 
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
  } // doAccion  
	
  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion(new TcManticVentasDto(seleccionado.getKey()), this.attrs.get("justificacionEliminar").toString());
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "El ticket de venta se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar el ticket de venta.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar

	protected Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		UISelectEntity estatus= (UISelectEntity) this.attrs.get("idVentaEstatus");
		if(!Cadena.isVacio(this.attrs.get("idVenta")) && !this.attrs.get("idVenta").toString().equals("-1"))
  		sb.append("(tc_mantic_ventas.id_venta=").append(this.attrs.get("idVenta")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_ventas.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_ventas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_ventas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("idCliente")) && !this.attrs.get("idCliente").toString().equals("-1"))
  		sb.append("(tc_mantic_clientes.id_cliente= ").append(this.attrs.get("idCliente")).append(") and ");
		if(estatus!= null && !estatus.getKey().equals(-1L))
  		sb.append("(tc_mantic_ventas.id_venta_estatus= ").append(estatus.getKey()).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	}
	
	protected void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("sucursales", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			this.attrs.put("idEmpresa", new UISelectEntity("-1"));
      columns.add(new Columna("limiteCredito", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      this.attrs.put("clientes", (List<UISelectEntity>) UIEntity.build("VistaVentasDto", "clientes", params, columns));
			this.attrs.put("idCliente", new UISelectEntity("-1"));
			columns.remove(0);
			columns.remove(1);
      this.attrs.put("estatusFiltro", (List<UISelectEntity>) UIEntity.build("TcManticVentasEstatusDto", "row", params, columns));
			this.attrs.put("idVentaEstatus", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public void doReporte() throws Exception {
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
		try{				
			reporteSeleccion= EReportes.ORDEN_COMPRA;
			this.reporte= JsfBase.toReporte();
			params= new HashMap<>();
			params.put("idVenta", ((Entity)this.attrs.get("seleccionado")).getKey());			
			parametros= new HashMap<>();
			parametros.put("REPORTE_EMPRESA", JsfBase.getAutentifica().getEmpresa().getNombreCorto());
		  parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
			parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
			parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
			this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
			doVerificarReporte();
			this.reporte.doAceptar();			
		} // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
    } // catch	
	} // doReporte
	
	public void doVerificarReporte() {
		RequestContext rc= RequestContext.getCurrentInstance();
		if(this.reporte.getTotal()> 0L)
			rc.execute("start(" + this.reporte.getTotal() + ")");		
		else{
			rc.execute("generalHide()");		
			JsfBase.addMessage("Generar reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
		} // else
	} // doVerificarReporte		
	
	public void doLoadEstatus(){
		Entity seleccionado          = null;
		Map<String, Object>params    = null;
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_venta_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticVentasEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
			this.attrs.put("allEstatus", allEstatus);
			this.attrs.put("estatus", allEstatus.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doLoadEstatus
	
	public void doActualizarEstatus() {
		Transaccion transaccion           = null;
		TcManticVentasBitacoraDto bitacora= null;
		Entity seleccionado               = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			TcManticVentasDto orden= (TcManticVentasDto)DaoFactory.getInstance().findById(TcManticVentasDto.class, seleccionado.getKey());
			bitacora= new TcManticVentasBitacoraDto(-1L, (String)this.attrs.get("justificacion"), JsfBase.getIdUsuario(), seleccionado.getKey(), Long.valueOf(this.attrs.get("estatus").toString()), orden.getConsecutivo(), orden.getTotal());
			transaccion= new Transaccion(bitacora);
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally {
			this.attrs.put("justificacion", "");
		} // finally
	}	// doActualizaEstatus
	
	public String doTicketExpress(){
		String regresar= null;
		try {
			regresar= "/Paginas/Mantic/Ventas/express".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doTicketExpress
	
	public void doTicket(){
		Entity seleccionado      = null;
		Map<String, Object>params= null;
		CreateTicket ticket      = null;
		AdminTickets adminTicket = null;
		try {			
			seleccionado= (Entity) this.attrs.get("seleccionado");
			params= new HashMap<>();
			params.put("idVenta", seleccionado.getKey());
			adminTicket= new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params));			
			ticket= new CreateTicket(adminTicket, toPago(adminTicket, seleccionado.getKey()), toTipoTransaccion(seleccionado.toLong("idVentaEstatus")));
			RequestContext.getCurrentInstance().execute("imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + toConsecutivoTicket(seleccionado.toLong("idVentaEstatus"), adminTicket) + "','" + ticket.toHtml() + "');");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // doTicket
	
	private String toTipoTransaccion(Long idEstatus){
		String regresar       = null;
		EEstatusVentas estatus= null;
		try {
			estatus= EEstatusVentas.fromIdTipoPago(idEstatus);
			switch(estatus){
				case PAGADA:
				case TERMINADA:
					regresar= "VENTA DE MOSTRADOR";
				break;
				case COTIZACION:
					regresar= "COTIZACIÓN";
					break;
				case APARTADO:
					regresar= "APARTADO";
					break;
			} // switch			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toTipoTransaccion
	
	private String toConsecutivoTicket(Long idEstatus, AdminTickets ticket){
		String regresar       = null;
		EEstatusVentas estatus= null;
		try {
			estatus= EEstatusVentas.fromIdTipoPago(idEstatus);
			if(estatus.equals(EEstatusVentas.TERMINADA) || estatus.equals(EEstatusVentas.APARTADO))
				regresar= ((TicketVenta)(ticket.getOrden())).getTicket();
			else
				regresar= ((TicketVenta)(ticket.getOrden())).getCotizacion();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toTipoTransaccion
	
	private Pago toPago(AdminTickets adminTicket, Long idVenta) throws Exception{
		Pago regresar            = null;
		List<Entity> detallePago = null;
		Map<String, Object>params= null;
		ETipoMediosPago medioPago= null;
		try {
			regresar= new Pago(adminTicket.getTotales());
			params= new HashMap<>();
			params.put("idVenta", idVenta);
			detallePago= DaoFactory.getInstance().toEntitySet("TrManticVentaMedioPagoDto", "ticket", params, Constantes.SQL_TODOS_REGISTROS);
			if(!detallePago.isEmpty()){
				for(Entity pago: detallePago){
					medioPago= ETipoMediosPago.fromIdTipoPago(pago.toLong("idTipoMedioPago"));
					switch(medioPago){
						case EFECTIVO:
							regresar.setEfectivo(pago.toDouble("importe"));							
							break;
						case TARJETA:
							regresar.setCredito(pago.toDouble("importe"));							
							regresar.setReferenciaCredito(pago.toString("referencia"));							
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
}

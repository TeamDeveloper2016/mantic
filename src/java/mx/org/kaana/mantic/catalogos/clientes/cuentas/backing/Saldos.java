package mx.org.kaana.mantic.catalogos.clientes.cuentas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Periodo;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.reglas.NotificaCliente;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.IBaseAttachment;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.Correo;
import mx.org.kaana.mantic.facturas.reglas.Transaccion;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@Named(value = "manticCatalogosClientesCuentasSaldos")
@ViewScoped
public class Saldos extends IBaseFilter implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Saldos.class);
  private static final long serialVersionUID= 8793667741599428879L;
  
  private Reporte reporte;
	private UISelectEntity encontrado;
  private Long idCliente;
	private boolean filtro;
	private List<Correo> correos;
	private List<Correo> selectedCorreos;	
	private Correo correo;
  protected FormatLazyModel lazyModelDetalle;
	private List<Entity> pagosRealizados;
  protected FormatLazyModel lazyPagosRealizados;
  private EAccion pivote;
	private FormatLazyModel pagosSegmento;
	private List<Entity> seleccionadosSegmento;

	public UISelectEntity getEncontrado() {
		return encontrado;
	}

	public void setEncontrado(UISelectEntity encontrado) {
		this.encontrado=encontrado;
	}

	public boolean isFiltro() {
		return filtro;
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

  public FormatLazyModel getLazyModelDetalle() {
    return lazyModelDetalle;
  }

  public List<Entity> getPagosRealizados() {
    return pagosRealizados;
  }

  public FormatLazyModel getLazyPagosRealizados() {
    return lazyPagosRealizados;
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
    try {
			this.idCliente= JsfBase.getFlashAttribute("idCliente")== null? -1L: Long.valueOf(JsfBase.getFlashAttribute("idCliente").toString());			
			this.filtro = this.idCliente.equals(-1L);
      this.pivote = EAccion.ELIMINAR;
  		this.attrs.put("ok", Boolean.FALSE);
      this.attrs.put("idCliente", this.idCliente);     
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("limitePago", "credenciales");
      this.attrs.put("tipoCorreoEspecial", "CREDITO");
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				this.loadSucursales();
      this.toLoadCatalog();
			if(!this.idCliente.equals(-1L)) {
				this.doLoad();
				this.idCliente= -1L;
			} // if
      this.attrs.put("plazo", 1);
      this.doCalcularPlazo();
			this.correos        = new ArrayList<>();
			this.selectedCorreos= new ArrayList<>();
      this.pagosRealizados= null;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns     = new ArrayList<>();
	  Map<String, Object> params= null;	
    try {
  	  params = this.toPrepare();	
      params.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");
			params.put("idCliente", this.idCliente);
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("importe", EFormatoDinamicos.MILES_CON_DECIMALES));      
      columns.add(new Columna("saldo", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("abonado", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("limiteCredito", EFormatoDinamicos.MILES_CON_DECIMALES));    
			this.lazyModel = new FormatCustomLazy("VistaClientesDto", "clientes", params, columns);
      UIBackingUtilities.resetDataTable();	
      this.lazyModelDetalle   = null;
      this.pagosRealizados    = null;
      this.lazyPagosRealizados= null;
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

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb            = new StringBuilder("");
	  UISelectEntity cliente      = (UISelectEntity)this.attrs.get("cliente");
		List<UISelectEntity>clientes= (List<UISelectEntity>)this.attrs.get("clientes");
    if(!this.idCliente.equals(-1L))
      sb.append("tc_mantic_clientes_deudas.id_cliente= ").append(this.idCliente).append(" and ");
		if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
			sb.append("tc_mantic_clientes.razon_social like '%").append(clientes.get(clientes.indexOf(cliente)).toString("razonSocial")).append("%' and ");			
		else if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input")))
  		sb.append("tc_mantic_clientes.razon_social like '%").append(JsfBase.getParametro("razonSocial_input")).append("%' and ");						
  	if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_ventas.ticket like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_clientes_deudas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_clientes_deudas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("vencidos")) && this.attrs.get("vencidos").toString().equals("1"))
  		sb.append("(now()> tc_mantic_clientes_deudas.limite) and ");
		if(!Cadena.isVacio(this.attrs.get("dias")))
  		sb.append("((datediff(tc_mantic_clientes_deudas.limite, now())* -1)>= ").append(this.attrs.get("dias")).append(") and ");
    UISelectEntity estatus= (UISelectEntity) this.attrs.get("idClienteEstatus");
    if(estatus!= null)
      if(Objects.equals(0L, estatus.getKey()))
        sb.append("tc_mantic_clientes_deudas.id_cliente_estatus in (1, 2) and ");
      else
        if(!Objects.equals(-1L, estatus.getKey()))
          sb.append("(tc_mantic_clientes_deudas.id_cliente_estatus= ").append(estatus.getKey()).append(") and ");
    if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))			
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	} // toPrepare	

 	protected void toLoadCatalog() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      List<UISelectEntity> estatus= (List<UISelectEntity>) UIEntity.seleccione("TcManticClientesEstatusDto", "row", params, columns, "nombre");
      if(estatus!= null) {
        Entity entity= new Entity(0L);
        entity.put("nombre", new Value("nombre", "PENDIENTES"));
        UISelectEntity pendientes= new UISelectEntity(entity);
        estatus.add(1, pendientes);
      } // if
      this.attrs.put("allEstatus", estatus);
			this.attrs.put("idClienteEstatus", new UISelectEntity("0"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	} // toLoadCatalog

	public String doRegresar() {
	  JsfBase.setFlashAttribute("idCliente", this.attrs.get("idCliente"));		
		return "filtro".concat(Constantes.REDIRECIONAR);
	} 
	
	public void doClientes() {
		List<UISelectEntity> clientes= null;
    Map<String, Object> params   = new HashMap<>();
		List<Columna> columns        = new ArrayList<>();
    try {
			if(this.attrs.get("busqueda")!= null && this.attrs.get("busqueda").toString().length()> 3) {
				params.put(Constantes.SQL_CONDICION, "upper(razon_social) like upper('%".concat((String)this.attrs.get("busqueda")).concat("%')"));
				// params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
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
	} 

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
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	}
	
	private void loadSucursales() {
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = new HashMap<>();
		List<Columna> columns          = new ArrayList<>();
		try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			sucursales=(List<UISelectEntity>) UIEntity.seleccione("TcManticEmpresasDto", "empresas", params, columns, "clave");
			this.attrs.put("sucursales", sucursales);			
			this.attrs.put("idEmpresa", new UISelectEntity(-1L));			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
	
	public String doPago() {
		String regresar    = null;
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionadoDetalle");
			JsfBase.setFlashAttribute("idClienteDeuda", seleccionado.getKey());
			JsfBase.setFlashAttribute("idCliente", seleccionado.toString("idCliente"));
			regresar= "abono".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doPago
	
	public String doDeuda() {
		String regresar    = null;
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionadoDetalle");
			JsfBase.setFlashAttribute("idCliente", seleccionado.toString("idCliente"));
			regresar= "deuda".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doPago
  
  public void doNotificar(String nombre, Entity pagoRealizado) throws Exception {
    Parametros comunes           = null;
		Map<String, Object>params    = new HashMap<>();
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
		try {		
      reporteSeleccion= EReportes.valueOf(nombre);
      this.idCliente= pagoRealizado.toLong("idCliente");
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
      params.put("idCliente", pagoRealizado.toLong("idCliente"));
      params.put("idClientePagoControl", pagoRealizado.toLong("idClientePagoControl"));
      params.put("sortOrder", "order by	tc_mantic_clientes_pagos.registro");
      comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
      this.doVerificarReporte();
      this.reporte.doAceptar();			
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  }
  
  public void doReporte(String nombre, Entity seleccionado) throws Exception {
    Parametros comunes           = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
		try {		
      reporteSeleccion= EReportes.valueOf(nombre);
      if(reporteSeleccion.equals(EReportes.CUENTAS_POR_COBRAR) && this.attrs.get("seleccionado")!= null) {
        this.idCliente= seleccionado.toLong("idCliente");
        params= this.toPrepare();
        this.idCliente= -1L;
      } // if  
      else
        params= this.toPrepare();
      params.put("idCliente", this.idCliente);
			params.put("cliente", this.attrs.get("cliente"));
      params.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");
      if(reporteSeleccion.equals(EReportes.CUENTA_COBRAR_DETALLE) || reporteSeleccion.equals(EReportes.DEUDAS_CLIENTES_DETALLE)) {
        params.put("idClienteDeuda", ((Entity)this.attrs.get("seleccionadoDetalle")).toLong("idKey"));
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), -1L, -1L , seleccionado.toLong("idCliente"));
      } // if
      else {
        params.put("sortOrder", "order by	tc_mantic_clientes.razon_social,tc_mantic_clientes_deudas.registro desc");
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      } // else
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
      this.doVerificarReporte();
      this.reporte.doAceptar();			
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // doReporte
  
  public void doDetallado(Entity seleccionado) throws Exception {
		List<Columna> columns     = new ArrayList<>();
	  Map<String, Object> params= new HashMap<>();	
		try {
			this.seleccionadosSegmento= new ArrayList<>();
			params.put("idCliente", seleccionado.toLong("idCliente"));						
			params.put("sortOrder", "order by dias desc");
      UISelectEntity estatus= (UISelectEntity) this.attrs.get("idClienteEstatus");
      if(estatus!= null)
        if(Objects.equals(0L, estatus.getKey()))
          params.put(Constantes.SQL_CONDICION, " tc_mantic_clientes_deudas.id_cliente_estatus in (1, 2)");			
        else
          if(!Objects.equals(-1L, estatus.getKey()))
            params.put(Constantes.SQL_CONDICION, " tc_mantic_clientes_deudas.id_cliente_estatus= ".concat(estatus.getKey().toString()).concat(")"));			
          else
            params.put(Constantes.SQL_CONDICION, " tc_mantic_clientes_deudas.saldo > 0 and tc_mantic_clientes_deudas.id_cliente_estatus in (1, 2)");			
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MILES_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MILES_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));						
			this.pagosSegmento= new FormatLazyModel("VistaClientesDto", "cuentas", params, columns);      
      UIBackingUtilities.resetDataTable("tablaSegmentos");		
      this.attrs.put("clientePivot", seleccionado);
		} // try 
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch 		
  } // doDetallado

  public void doReporteDetallado() throws Exception {
    Parametros comunes           = null;
		Map<String, Object>params    = new HashMap<>();
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = EReportes.CUENTA_COBRAR_TICKETS;
		try {		
      Entity seleccionado = (Entity)this.attrs.get("clientePivot");
      StringBuilder ventas= new StringBuilder();
      for (Entity item: this.seleccionadosSegmento) {
        ventas.append(item.toLong("idVenta")).append(", ");
      } // for
      ventas.delete(ventas.length()- 2, ventas.length());
      params.put("ventas", ventas.toString());
			params.put("cliente", this.attrs.get("cliente"));
      comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), -1L, -1L , seleccionado.toLong("idCliente"));
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
      this.doVerificarReporte();
      this.reporte.doAceptar();		
 		} // try 
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch 		
  } // doReporteDetallado

  public boolean doVerificarReporte() {
    boolean regresar = false;
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L) {
			rc.execute("start(" + this.reporte.getTotal() + ")");		
      regresar = true;
    } // if
		else{
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
      regresar = false;
		} // else
    return regresar;
	} // doVerificarReporte	
	
	public String doImportar() {
		JsfBase.setFlashAttribute("idVenta", ((Entity)this.attrs.get("seleccionadoDetalle")).toLong("idVenta"));
		JsfBase.setFlashAttribute("idCliente", ((Entity)this.attrs.get("seleccionadoDetalle")).toLong("idCliente"));
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos");		
		JsfBase.setFlashAttribute("idClienteDeuda",((Entity)this.attrs.get("seleccionadoDetalle")).getKey());
		return "importar".concat(Constantes.REDIRECIONAR);
	}
	
	public String doTicketExpress(Entity cliente) {
		String regresar= null;
		try {			
      JsfBase.setFlashAttribute("idCliente", cliente.toLong("idCliente"));		
      JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos");		
			regresar= "/Paginas/Mantic/Ventas/express".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doTicketExpress
	
	public String toColor(Entity row) {
		return row.toLong("idManual").equals(1L)? "janal-tr-orange": "";
	}
	
	public List<UISelectEntity> doCompleteCliente(String codigo) {
 		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= new String(codigo).replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put("codigo", codigo);
			if(buscaPorCodigo)
        this.attrs.put("clientes", UIEntity.build("VistaCuentasPorCobrarDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("clientes", UIEntity.build("VistaCuentasPorCobrarDto", "porNombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("clientes");
	} // doCompleteCliente			
	
	public String doAccion() {
		String regresar= "/Paginas/Mantic/Ventas/accion".concat(Constantes.REDIRECIONAR); 
		try {
			JsfBase.setFlashAttribute("accion", EAccion.CONSULTAR);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos");					
			JsfBase.setFlashAttribute("idVenta", ((Entity)this.attrs.get("seleccionadoDetalle")).toLong("idVenta"));			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
  } // doAccion 
  
  public void doCalcularPlazo() {
		Integer plazo  = null;				
    Calendar actual= null;
    Calendar inicio= null;
		try {					
			plazo= Integer.valueOf(this.attrs.get("plazo").toString());
      actual= Calendar.getInstance();
      actual.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(Fecha.getHoy()));
      this.attrs.put("vigenciaFin",new java.sql.Date(actual.getTimeInMillis()));
      inicio= Calendar.getInstance();
      inicio.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(Fecha.getHoy()));
      this.attrs.put("tipoReporteEspecial", "DEUDAS_CLIENTES");
			switch(plazo) {
        case 1:
          inicio.add(Calendar.DATE, -30);
        break;
        case 2:
          inicio.set(Calendar.DAY_OF_MONTH,1);
        break;
        case 3:
          inicio.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(Fecha.getHoy()));
        break;
        case 4:
          this.attrs.put("tipoReporteEspecial", "DEUDAS_CLIENTES_PENDIENTES");
        break;
      }//switch
      this.attrs.put("vigenciaIni", new java.sql.Date(inicio.getTimeInMillis()));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch		
	} // doCalcularPlazo
  
  public void doReporteEspecial() throws Exception{
		this.toReporteEspecial(false);
	}
	
  private void toReporteEspecial(boolean email) throws Exception {
    Parametros comunes = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado = ((Entity)this.attrs.get("seleccionadoDetalle"));
		try{		
      params= this.toPrepare();
      params.put("idCliente", seleccionado.toString("idCliente"));
      params.put("idClienteDeuda", seleccionado.getKey());
      params.put("fechaInicio", this.attrs.get("vigenciaIni"));
      params.put("fechaFin", this.attrs.get("vigenciaFin"));
      reporteSeleccion= EReportes.valueOf(this.attrs.get("tipoReporteEspecial").toString());
      comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), -1L, -1L , seleccionado.toLong("idCliente"));
      parametros= comunes.getComunes();
      this.reporte= JsfBase.toReporte();	
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
			if(email) 
        this.reporte.doAceptarSimple();			
			else 
				if(this.doVerificarReporte())
          this.reporte.doAceptar();			      
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // toReporteEspecial
	
	public void doLoadEstatus(String reporte, String correo, Entity seleccionado) {
		Map<String, Object>params         = null;
		MotorBusquedaCatalogos motor      = null; 
		Correo item                       = null;
		List<ClienteTipoContacto>contactos= null;
		try {
      this.attrs.put("seleccionado", seleccionado);
      this.attrs.put("tipoReporteEspecial", reporte);
      this.attrs.put("tipoCorreoEspecial", correo);
			this.correos.clear();
			this.selectedCorreos.clear();
			motor= new MotorBusqueda(-1L, seleccionado.toLong("idCliente"));
			contactos= motor.toClientesTipoContacto();
			LOG.warn("Total de contactos asociados al cliente [" + contactos.size()+ "]");
			for(ClienteTipoContacto contacto: contactos) {
				if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey())) {
					item= new Correo(contacto.getIdClienteTipoContacto(), contacto.getValor(), contacto.getIdPreferido());
					this.correos.add(item);		
					this.selectedCorreos.add(item);
				} // if
			} // for
			LOG.warn("Agregando un correo por defecto para el estado de cuenta");
			this.correos.add(new Correo(-1L, "", 2L));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doLoadEstatus
	
  private void toSendMailEspecial(StringBuilder sb, Entity seleccionado) {
		Map<String, Object> params= new HashMap<>();
		String[] emails= {(sb.length()> 0? sb.substring(0, sb.length()- 2): "")};
		List<Attachment> files= new ArrayList<>(); 
		try {
			params.put("header", "...");
			params.put("footer", "...");
			params.put("empresa", JsfBase.getAutentifica().getEmpresa().getNombre());
			params.put("tipo", "Estado de Cuenta");
			params.put("razonSocial", seleccionado.toString("razonSocial"));
			params.put("correo", ECorreos.CUENTAS.getEmail());
			params.put("url", Configuracion.getInstance().getPropiedadServidor("sistema.dns"));
			this.toReporteEspecial(true);
			Attachment attachments= new Attachment(this.reporte.getNombre(), Boolean.FALSE);
			files.add(attachments);
			files.add(new Attachment("logo", ECorreos.CUENTAS.getImages().concat("logo.png"), Boolean.TRUE));
			params.put("attach", attachments.getId());
			for (String item: emails) {
				try {
					if(!Cadena.isVacio(item)) {
					  IBaseAttachment notificar= new IBaseAttachment(ECorreos.CUENTAS, ECorreos.CUENTAS.getEmail(), item, "controlbonanza@gmail.com", "Ferreteria Bonanza - Estado de cuenta", params, files);
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
			if(sb.length()> 0)
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
  }
  
	public void doSendMail() {
		StringBuilder sb= new StringBuilder("");
		if(this.selectedCorreos!= null && !this.selectedCorreos.isEmpty()) {
			for(Correo mail: this.selectedCorreos) {
				if(!Cadena.isVacio(mail.getDescripcion()))
					sb.append(mail.getDescripcion()).append(", ");
			} // for
		} // if
    if(Objects.equals((String)this.attrs.get("tipoReporteEspecial"), "CUENTAS_POR_COBRAR") || 
       Objects.equals((String)this.attrs.get("tipoReporteEspecial"), "PAGOS_CUENTAS_POR_COBRAR")) {
      NotificaCliente notifica= new NotificaCliente(
        ((Entity)this.attrs.get("seleccionado")).toLong("idCliente"), // Long idCliente, 
        ((Entity)this.attrs.get("seleccionado")).toString("razonSocial"), // String razonSocial, 
        sb.toString(), // String correos, 
        EReportes.valueOf((String)this.attrs.get("tipoReporteEspecial")), // EReportes reportes, 
        ECorreos.valueOf((String)this.attrs.get("tipoCorreoEspecial")), // ECorreos correo, 
        true // boolean notifica      
      );
      notifica.doSendMail();
      notifica.doSendWhatsup();
    } // if  
    else
      this.toSendMailEspecial(sb, (Entity)this.attrs.get("seleccionadoDetalle"));
	} // doSendMail
	
	public void doAgregarCorreo() {
		Entity seleccionado    = null;
		Transaccion transaccion= null;
		try {
			if(!Cadena.isVacio(this.correo.getDescripcion())) {
				seleccionado= (Entity)this.attrs.get("seleccionadoDetalle");
				transaccion = new Transaccion(this.correo, seleccionado.toLong("idCliente"));
				if(transaccion.ejecutar(EAccion.COMPLEMENTAR))
					JsfBase.addMessage("Se agrego el correo electrónico correctamente !");
				else
					JsfBase.addMessage("Ocurrió un error al agregar el correo electronico");
			} // if
			else
				JsfBase.addMessage("Es necesario capturar un correo electrónico !");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAgregarCorreo
  
  public void doLoadDetalle() {
    List<Columna> columns     = new ArrayList<>();
	  Map<String, Object> params= null;	
    try {
  	  params = this.toPrepare();
			Entity entity= this.attrs.get("seleccionado")== null? (Entity)this.attrs.get("rowSeleccionado"): (Entity)this.attrs.get("seleccionado");
			params.put("sortOrder", "order by dias desc, tc_mantic_ventas.ticket desc");
			params.put("idCliente", entity.toLong("idCliente"));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("importe", EFormatoDinamicos.MILES_CON_DECIMALES));      
      columns.add(new Columna("saldo", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("abonado", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));    
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));    
			this.lazyModelDetalle = new FormatCustomLazy("VistaClientesDto", "detalle", params, columns);
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
  }
 
	public void onRowToggle(ToggleEvent event) {
		try {
			this.attrs.put("rowSeleccionado", (Entity) event.getData());
			if (!event.getVisibility().equals(Visibility.HIDDEN)) 
				this.doLoadDetalle();
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // onRowToggle
 
  public void toLoadPagosRealizados(Entity row) {
    List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    try {      
      this.attrs.put("seleccionado", row);
      params.put("idCliente", row.toLong("idCliente"));      
      Periodo periodo= new Periodo();
      periodo.addMeses(-12);
      params.put("inicio", periodo.toString());      
      columns.add(new Columna("pago", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
      this.pagosRealizados = (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaClientesDto", "pagosRealizados", params);
      if(this.pagosRealizados!= null && !this.pagosRealizados.isEmpty()) {
        UIBackingUtilities.toFormatEntitySet(this.pagosRealizados, columns);
        this.pagosRealizados.get(0).getValue("eliminar").setData(1L);
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
  }
 
  public void doUpdateFechaPago() {
    Entity row= (Entity)this.attrs.get("seleccionadoDetalle");
    this.attrs.put("eliminarPagoRealizado", row);
    this.attrs.put("limitePago", "limitePago");
    this.attrs.put("limite", row.toDate("limite"));
    this.pivote = EAccion.MODIFICAR;
    this.attrs.put("msgAutorizacion", " el cambio de fecha de vencimiento ".concat(row.toString("limite")));
    if(row.toLong("idClienteEstatus")== 1L || row.toLong("idClienteEstatus")== 2L)
      UIBackingUtilities.execute("PF('widgetDialogoAutorizacion').show();");
    else
      JsfBase.addMessage("Cuenta:", "La cuenta NO se encuentra abierta para ajustar la fecha de vencimiento !");
  }
  
  public void doDeletePago(Entity row) {
    this.attrs.put("eliminarPagoRealizado", row);
    this.attrs.put("limitePago", "credenciales");
    this.pivote = EAccion.ELIMINAR;
    this.attrs.put("msgAutorizacion", " la cancelación del pago con importe ".concat(row.toString("pago")));
  }
  
  public void doDeleteCuenta(Entity row) {
    this.attrs.put("seleccionadoDetalle", row);
    this.attrs.put("limitePago", "credenciales");
    this.pivote = EAccion.DEPURAR;
    this.attrs.put("msgAutorizacion", " la cancelación de la CxC con ticket ".concat(row.toString("ticket")));
  }
  
	public void onRowTogglePagosRealizados(ToggleEvent event) {
		try {
			this.attrs.put("rowPagoRealizado", (Entity) event.getData());
			if (!event.getVisibility().equals(Visibility.HIDDEN)) 
				this.doLoadDetallePagosRealizados();
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // onRowTogglePagosRealizados
 
  public void doLoadDetallePagosRealizados() {
    List<Columna> columns     = new ArrayList<>();
	  Map<String, Object> params= new HashMap<>();	
    try {
			Entity entity= this.attrs.get("pagoRealizado")== null? (Entity)this.attrs.get("rowPagoRealizado"): (Entity)this.attrs.get("pagoRealizado");
			params.put("sortOrder", "order by tc_mantic_clientes_pagos.registro desc");
			params.put("idClientePagoControl", entity.toLong("idClientePagoControl"));
      columns.add(new Columna("venta", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("abonado", EFormatoDinamicos.MILES_CON_DECIMALES));      
			this.lazyPagosRealizados= new FormatCustomLazy("VistaClientesDto", "detallePagosRealizados", params, columns);
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
  }  
  
	public String doCheckUser() {
		String regresar   = null;
    String texto      = "";
    String ticket     = null;
		String cuenta     = (String)this.attrs.get("cuenta");
		String contrasenia= (String)this.attrs.get("contrasenia");
		try {
			CambioUsuario	usuario= new CambioUsuario(cuenta, contrasenia);			
			if(usuario.autorizaCancelacion()) {
        String justificacion= (String)this.attrs.get("justificacion");
				this.attrs.put("cuenta", "");
				this.attrs.put("contrasenia", "");
				this.attrs.put("justificacion", "");
        // aqui se elimina el pago al que se hace referencia
        switch(pivote) {
          case ELIMINAR:
            ticket= " [Folio: ".concat(((Entity)this.attrs.get("eliminarPagoRealizado")).toString("consecutivo")).concat("]");
            break;
          case DEPURAR:
            ticket= " [Ticket: ".concat(((Entity)this.attrs.get("seleccionadoDetalle")).toString("ticket")).concat("]");
            break;
          case MODIFICAR:
            ticket= " [Ticket: ".concat(((Entity)this.attrs.get("seleccionadoDetalle")).toString("ticket")).concat("]");
            break;
        } // switch
        String proceso= EAccion.ELIMINAR.equals(pivote)? "eliminarPagoRealizado": "seleccionadoDetalle";
        Entity entity = (Entity)this.attrs.get(proceso);
        mx.org.kaana.mantic.catalogos.clientes.cuentas.reglas.Transaccion transaccion= new 
          mx.org.kaana.mantic.catalogos.clientes.cuentas.reglas.Transaccion(EAccion.ELIMINAR.equals(pivote)? entity.toLong("idClientePagoControl"): entity.toLong("idClienteDeuda"), justificacion, (Date)this.attrs.get("limite"));
        if(transaccion.ejecutar(pivote)) {
          switch(pivote) {
            case ELIMINAR:
              texto= "Se eliminó el pago con éxito ".concat(ticket);
              break;
            case DEPURAR:
              texto= "Se eliminó la cuenta por cobrar con éxito ".concat(ticket);
              break;
            case MODIFICAR:
              texto= "Se ajusto la fecha de vencimiento con éxito ".concat(ticket);
              break;
          } // switch
          this.idCliente= entity.toLong("idCliente");
          this.doLoad();
          this.idCliente= -1L;
          UIBackingUtilities.update("tabla");
          UIBackingUtilities.update("tablaPagosRealizados");
        } // if
        else 
          switch(pivote) {
            case ELIMINAR:
              texto= "No se puedo eliminar el pago, intente nuevamente !";
              break;
            case DEPURAR:
              texto= "No se puedo eliminar la cuenta por cobrar intente nuevamente !";
              break;
            case MODIFICAR:
              texto= "No se puedo modificar la fecha de vencimiento !";
              break;
          } // switch
				this.attrs.put("ok", Boolean.FALSE);
				UIBackingUtilities.execute("PF('widgetDialogoAutorizacion').hide();");
        UIBackingUtilities.execute("janal.show([{summary: 'Cuenta: ', detail: '"+ texto+ "'}], '"+ (texto.startsWith("Se")? "info": "warn")+ "');");
			} // if
			else
				this.attrs.put("ok", Boolean.TRUE);
	  } // try
    catch (Exception e) {
      Error.mensaje(e);
			UIBackingUtilities.execute("PF('widgetDialogoAutorizacion').hide();");
      JsfBase.addMessageError(e);
    } // catch
		return regresar;
	}
 
  public void doRowSeleccionado() {
    LOG.info(this.seleccionadosSegmento.size());
  }
  
}
package mx.org.kaana.mantic.catalogos.clientes.cuentas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
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
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.enums.EReportes;
import org.primefaces.context.RequestContext;

@Named(value = "manticCatalogosClientesCuentasSaldos")
@ViewScoped
public class Saldos extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  private Reporte reporte;
	private UISelectEntity encontrado;
  private Long idCliente;
	private boolean filtro;

	public UISelectEntity getEncontrado() {
		return encontrado;
	}

	public void setEncontrado(UISelectEntity encontrado) {
		this.encontrado=encontrado;
	}

	public boolean isFiltro() {
		return filtro;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			this.idCliente= JsfBase.getFlashAttribute("idCliente")== null? -1L: Long.valueOf(JsfBase.getFlashAttribute("idCliente").toString());			
			this.filtro = this.idCliente.equals(-1L);
      this.attrs.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");
      this.attrs.put("idCliente", this.idCliente);     
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				loadSucursales();
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
  	  params = toPrepare();	
			params.put("idCliente", this.idCliente);
      columns= new ArrayList<>();
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));      
      columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));    
      columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));    
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));    
			if(!this.idCliente.equals(-1L))
				this.lazyModel = new FormatCustomLazy("VistaClientesDto", "cuentas", params, columns);
			else
				this.lazyModel = new FormatCustomLazy("VistaClientesDto", "cuentasBusqueda", params, columns);
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

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb            = new StringBuilder("");
	  UISelectEntity cliente      = (UISelectEntity)this.attrs.get("cliente");
		List<UISelectEntity>clientes= (List<UISelectEntity>)this.attrs.get("clientes");
		if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
			sb.append("tc_mantic_clientes.razon_social like '%").append(clientes.get(clientes.indexOf(cliente)).toString("razonSocial")).append("%' and ");			
		else if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input")))
  		sb.append("tc_mantic_clientes.razon_social like '%").append(JsfBase.getParametro("razonSocial_input")).append("%' and ");						
  	if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_ventas.consecutivo= ").append(this.attrs.get("consecutivo")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_clientes_deudas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_clientes_deudas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("vencidos")) && this.attrs.get("vencidos").toString().equals("1"))
  		sb.append("(now()> tc_mantic_clientes_deudas.limite) and ");
		if(!Cadena.isVacio(this.attrs.get("dias")))
  		sb.append("(datediff(tc_mantic_clientes_deudas.limite, now())>= ").append(this.attrs.get("dias")).append(") and ");
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

	public String doRegresar() {
	  JsfBase.setFlashAttribute("idCliente", this.attrs.get("idCliente"));		
		return "filtro".concat(Constantes.REDIRECIONAR);
	} 
	
	public void doClientes() {
		List<UISelectEntity> clientes= null;
    Map<String, Object> params   = null;
		List<Columna> columns        = null;
    try {
			columns= new ArrayList<>();
			if(this.attrs.get("busqueda")!= null && this.attrs.get("busqueda").toString().length()> 3) {
				params = new HashMap<>();      
				params.put(Constantes.SQL_CONDICION, "upper(razon_social) like upper('%".concat((String)this.attrs.get("busqueda")).concat("%')"));
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
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
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
	
	public String doPago(){
		String regresar    = null;
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
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
	
	public String doDeuda(){
		String regresar    = null;
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
			JsfBase.setFlashAttribute("idCliente", seleccionado.toString("idCliente"));
			regresar= "deuda".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doPago
  
  public void doReporte(String nombre) throws Exception{
    Parametros comunes = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
		try{		
      params= toPrepare();
      params.put("idCliente", this.idCliente);
			params.put("cliente", this.attrs.get("cliente"));
      seleccionado = ((Entity)this.attrs.get("seleccionado"));
      params.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");
      reporteSeleccion= EReportes.valueOf(nombre);
      if(reporteSeleccion.equals(EReportes.CUENTA_COBRAR_DETALLE)){
        params.put("idClienteDeuda", seleccionado.toLong("idKey"));
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), -1L, -1L , seleccionado.toLong("idCliente"));
      }
      else
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
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
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
		} // else
	} // doVerificarReporte	
	
	public String doImportar() {
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos");		
		JsfBase.setFlashAttribute("idClienteDeuda",((Entity)this.attrs.get("seleccionado")).getKey());
		return "importar".concat(Constantes.REDIRECIONAR);
	}
	
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
        this.attrs.put("clientes", UIEntity.build("TcManticClientesDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("clientes", UIEntity.build("TcManticClientesDto", "porNombre", params, columns, 40L));
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
	
	public String doAccion(String accion) {
		String regresar= "/Paginas/Mantic/Ventas/accion".concat(Constantes.REDIRECIONAR); 
		try {
			JsfBase.setFlashAttribute("accion", EAccion.CONSULTAR);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos");					
			JsfBase.setFlashAttribute("idVenta", ((Entity)this.attrs.get("seleccionado")).toLong("idVenta"));			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
  } // doAccion 
}
package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
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
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.enums.EEstatusEmpresas;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

@Named(value = "manticCatalogosEmpresasCuentasSaldos")
@ViewScoped
public class Saldos extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;	
  private static final Log LOG = LogFactory.getLog(Saldos.class);
  
  private Reporte reporte;
  protected FormatLazyModel lazyModelDetalle;
	private FormatLazyModel pagosSegmento;
	private FormatLazyModel notasEntradaFavor;
	private FormatLazyModel notasCreditoFavor;
	private List<Entity> seleccionadosNotas;
	private List<Entity> seleccionadosCredito;
	private Entity[] seleccionadosSegmento;

  public FormatLazyModel getLazyModelDetalle() {
    return lazyModelDetalle;
  }

  public FormatLazyModel getPagosSegmento() {
    return pagosSegmento;
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

  public Entity[] getSeleccionadosSegmento() {
    return seleccionadosSegmento;
  }

  public void setSeleccionadosSegmento(Entity[] seleccionadosSegmento) {
    this.seleccionadosSegmento = seleccionadosSegmento;
  }

  @PostConstruct
  @Override
  protected void init() {
 		Long idEmpresaInicial= -1L;
    try {
			this.attrs.put("empresa", "");
			this.attrs.put("almacen", "");
			this.attrs.put("vencidos", new Long(3));
      this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa")== null? JsfBase.getAutentifica().getEmpresa().getIdEmpresa() : (Long)JsfBase.getFlashAttribute("idEmpresa"));     
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda"));     
			this.seleccionadosSegmento= new Entity[0];
			idEmpresaInicial= JsfBase.getAutentifica().getEmpresa().getIdEmpresa();
			this.attrs.put("idEmpresaPago", idEmpresaInicial);
			this.attrs.put("idEmpresaGeneral", idEmpresaInicial);
			this.attrs.put("idEmpresaSegmento", idEmpresaInicial);
			this.attrs.put("mostrarBancoSegmento", false);
			this.attrs.put("mostrarBancoGeneral", false);
			this.attrs.put("mostrarBanco", false);			
			this.attrs.put("pago", 1D);
			this.attrs.put("pagoGeneral", 1D);
			this.attrs.put("pagoSegmento", 1D);
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				this.loadSucursales();
      if(this.attrs.get("idEmpresaDeuda")!= null) 
			  this.doLoad();
      this.toLoadCatalog();
			this.doLoadCajas();
			this.doLoadCajasGeneral();
      this.doLoadCajasSegmento();
      this.loadBancos();
			this.loadTiposPagos();
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
  	  params = this.toPrepare();	
      columns= new ArrayList<>();
      columns.add(new Columna("pagar", EFormatoDinamicos.MILES_CON_DECIMALES));      
      columns.add(new Columna("saldo", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("abonado", EFormatoDinamicos.MILES_CON_DECIMALES));    
			params.put("sortOrder", "order by tc_mantic_proveedores.razon_social");
			this.lazyModel = new FormatCustomLazy("VistaEmpresasDto", "proveedores", params, columns);
      UIBackingUtilities.resetDataTable();		
			this.attrs.put("idEmpresaDeuda", null);
      this.lazyModelDetalle= null;
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
		StringBuilder sb= new StringBuilder();
	  UISelectEntity proveedor      = (UISelectEntity)this.attrs.get("proveedor");
		List<UISelectEntity>provedores= (List<UISelectEntity>)this.attrs.get("proveedores");
		if(!Cadena.isVacio(this.attrs.get("idEmpresaDeuda")) && !this.attrs.get("idEmpresaDeuda").toString().equals("-1"))
  		sb.append("(tc_mantic_empresas_deudas.id_empresa_deuda=").append(this.attrs.get("idEmpresaDeuda")).append(") and ");
		if(provedores!= null && proveedor!= null && provedores.indexOf(proveedor)>= 0) 
			sb.append("tc_mantic_proveedores.razon_social like '%").append(provedores.get(provedores.indexOf(proveedor)).toString("razonSocial")).append("%' and ");			
		else if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input")))
  		sb.append("tc_mantic_proveedores.razon_social like '%").append(JsfBase.getParametro("razonSocial_input")).append("%' and ");			
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_notas_entradas.consecutivo= ").append(this.attrs.get("consecutivo")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("factura")))
  		sb.append("(tc_mantic_notas_entradas.factura like '%").append(this.attrs.get("factura")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_empresas_deudas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_empresas_deudas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("vencidos")))
			switch(((Long)this.attrs.get("vencidos")).intValue()) {
				case 1: // SI
      		sb.append("(now()> tc_mantic_empresas_deudas.limite) and ");
				case 2: // NO
         	sb.append("(tc_mantic_empresas_deudas.saldo<> 0) and ");
					break;
				case 3: // TODOS
					break;
			} // switch
		if(!Cadena.isVacio(this.attrs.get("dias")))
  		sb.append("((datediff(tc_mantic_empresas_deudas.limite, now())* -1)>= ").append(this.attrs.get("dias")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("almacen")))
  		sb.append("tc_mantic_almacenes.nombre like '%").append(this.attrs.get("almacen")).append("%' and ");
    UISelectEntity estatus= (UISelectEntity) this.attrs.get("idEmpresaEstatus");
    if(estatus!= null)
      if(Objects.equals(0L, estatus.getKey()))
        sb.append("tc_mantic_empresas_deudas.id_empresa_estatus in (1, 2, 3) and ");
      else
        if(!Objects.equals(-1L, estatus.getKey()))
          sb.append("(tc_mantic_empresas_deudas.id_empresa_estatus= ").append(estatus.getKey()).append(") and ");
    regresar.put("idEmpresa", this.attrs.get("idEmpresa").toString().equals("-1") ? this.attrs.get("allEmpresa") : this.attrs.get("idEmpresa"));			
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
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      List<UISelectEntity> estatus= (List<UISelectEntity>) UIEntity.seleccione("TcManticEmpresasEstatusDto", "row", params, columns, "nombre");
      if(estatus!= null) {
        Entity entity= new Entity(0L);
        entity.put("nombre", new Value("nombre", "PENDIENTES"));
        UISelectEntity pendientes= new UISelectEntity(entity);
        estatus.add(1, pendientes);
      } // if
      this.attrs.put("allEstatus", estatus);
			this.attrs.put("idEmpresaEstatus", new UISelectEntity("0"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	} // toLoadCatalog
  
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
			params.put(Constantes.SQL_CONDICION, "id_cobro_caja=1");
			tiposPagos= UIEntity.build("TcManticTiposMediosPagosDto", "row", params);
			this.attrs.put("tiposPagos", tiposPagos);
			this.attrs.put("tipoPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadTiposPagos
	  
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
	} // doLoadCajasGeneral
  
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
	} // doLoadCajasSegmento
  
	public String doRegresar() {
		return "filtro".concat(Constantes.REDIRECIONAR);
	} // doRegresar 	 	
	
	private void loadSucursales() {
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		String allEmpresa              = "";
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			sucursales=(List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
			if(!sucursales.isEmpty()) {
				for(UISelectEntity sucursal: sucursales)
					allEmpresa= allEmpresa.concat(sucursal.getKey().toString()).concat(",");
				this.attrs.put("allEmpresa", allEmpresa.substring(0, allEmpresa.length()-1));
			} // 
			this.attrs.put("sucursales", sucursales);						
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
			JsfBase.setFlashAttribute("idEmpresaDeuda", seleccionado.getKey());
			JsfBase.setFlashAttribute("idEmpresa", seleccionado.toString("idEmpresa"));
			JsfBase.setFlashAttribute("idProveedor", seleccionado.toString("idProveedor"));
			regresar= "abono".concat(Constantes.REDIRECIONAR);
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
      params.put("empresa", this.attrs.get("empresa"));
			params.put("idEmpresa", this.attrs.get("idEmpresa").toString().equals("-1") ? this.attrs.get("allEmpresa") : this.attrs.get("idEmpresa"));			
			params.put("almacen", this.attrs.get("almacen"));			
      seleccionado = ((Entity)this.attrs.get("seleccionadoDetalle"));
      params.put("sortOrder", "order by	registro desc");
      reporteSeleccion= EReportes.valueOf(nombre);
      if(reporteSeleccion.equals(EReportes.CUENTA_PAGAR_DETALLE)) {
        params.put("idEmpresaDeuda", seleccionado.toLong("idKey"));
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), seleccionado.toLong("idAlmacen"), seleccionado.toLong("idProveedor"), -1L);
      }
      else{
        params.put("sortOrder", "order by	almacen,proveedor,registro desc");
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      }
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
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L)
			rc.execute("start(" + this.reporte.getTotal() + ")");		
		else{
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
		} // else
	} // doVerificarReporte		
	
	public String doImportar() {
		String regresar= null;
		try {
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Empresas/Cuentas/saldos");		
			JsfBase.setFlashAttribute("idEmpresaDeuda",((Entity)this.attrs.get("seleccionadoDetalle")).getKey());
			regresar= "importar".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doImportar
	
	public String doModificar() {
		String regresar= null;
		try {
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Empresas/Cuentas/saldos");		
			JsfBase.setFlashAttribute("idEmpresaDeuda",((Entity)this.attrs.get("seleccionadoDetalle")).getKey());
			JsfBase.setFlashAttribute("idEmpresa", ((Entity)this.attrs.get("seleccionadoDetalle")).toString("idEmpresa"));
			regresar= "prorroga".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doModificar
	
  public String doAccion(String accion) {
		String regresar= "accion";
    EAccion eaccion= EAccion.valueOf(accion.toUpperCase());
		try {
		  JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idEmpresaDeuda",((Entity)this.attrs.get("seleccionadoDetalle")).getKey());
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Inventarios/Entradas/filtro");		
			JsfBase.setFlashAttribute("idNotaEntrada", -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar.concat(Constantes.REDIRECIONAR);
  } // doAccion  

	public String doDeuda() {
		String regresar    = null;
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionadoDetalle");
			JsfBase.setFlashAttribute("idEmpresaDeuda",((Entity)this.attrs.get("seleccionadoDetalle")).getKey());
			JsfBase.setFlashAttribute("idProveedor", seleccionado.toString("idProveedor"));
			regresar= "deuda".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doPago
	
	public String doEstructura() {
		String regresar    = null;
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionadoDetalle");
			JsfBase.setFlashAttribute("idEmpresaDeuda", seleccionado.getKey());
			JsfBase.setFlashAttribute("idEmpresa", seleccionado.toString("idEmpresa"));
			regresar= "estructura".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doEstructura
	
	public List<UISelectEntity> doCompleteProveedor(String codigo) {
 		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
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
        this.attrs.put("proveedores", UIEntity.build("TcManticProveedoresDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("proveedores", UIEntity.build("TcManticProveedoresDto", "porNombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("proveedores");
	}		

	public String doNotaEntrada() {
		JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
		return "/Paginas/Mantic/Inventarios/Entradas/filtro".concat(Constantes.REDIRECIONAR);
	}

  public String toColor(Entity row) {
		Double original= row.toDouble("original");
		Double total   = row.toDouble("importe");
		return row.toLong("idNotaTipo").equals(3L)? "janal-tr-purple": (original!= 0D && original> total)? "janal-tr-yellow": (original!= 0D && original< total)? "janal-tr-green": "";
	} 
	
	public String doCostos(Entity row) {
		Double original= row.toDouble("original");
		Double total   = row.toDouble("importe");
		String regresar= "<i class='fa fa-fw fa-question-circle janal-color-green' style='float:right;' title='\n\nNota entrada: "+ Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, row.toDouble("importe"))+ 
			"\n\nImporte factura: " + Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, row.toString("original"))+ 
			"'\n\n'></i>";
		return (original!= 0D && original> total) || (original!= 0D && original< total)? regresar: "";
	}

  public void doLoadDetalle() {
    List<Columna> columns     = null;
	  Map<String, Object> params= null;	
		List<Entity> cuentas      = null;
    try {
  	  params = this.toPrepare();
			Entity entity= (Entity)this.attrs.get("seleccionado");
			params.put("sortOrder", "order by dias desc");
			params.put("idProveedor", entity.toLong("idProveedor"));
			this.attrs.put("idProveedor", entity.toLong("idProveedor"));
      columns= new ArrayList<>();
      columns.add(new Columna("saldo", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("abonado", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));    
      columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));    
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));    
			this.lazyModelDetalle = new FormatCustomLazy("VistaEmpresasDto", "detalle", params, columns);
			cuentas= DaoFactory.getInstance().toEntitySet("VistaEmpresasDto", "cuentasProveedor", params);
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
  }
 
	public void doLoadCuentasAFavor() {
		Entity seleccionado= null;		
		try {
			seleccionado= (Entity) this.attrs.get("seleccionadoDetalle");
			this.attrs.put("pago", seleccionado.toDouble("saldo"));
			this.doLoadNotasEntradas();
			this.doLoadNotasCredito();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doLoadCuentasAFavor
  
  	public void doLoadCuentas() {
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
		try {
			this.seleccionadosSegmento= new Entity[0];
			params= new HashMap<>();
			params.put("idProveedor", this.attrs.get("idProveedor"));      
			params.put("sortOrder", "order by	tc_mantic_empresas_deudas.registro desc");			
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
	} // doLoadCuentas
	
	private void doLoadNotasEntradas() {
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
	
	private void doLoadNotasCredito() {
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

	private boolean validaPagoGeneral() {
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
  
	private void validaPagoGeneral(List<Entity> cuentas) {
		int count= 0;
		try {
			for(Entity cuenta: cuentas) {
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
 
  private boolean validaPago() {
		boolean regresar= false;
		Double pago     = 0D;
		Double saldo    = 0D;
		Entity deuda    = null;
		try {
			pago = Double.valueOf(this.attrs.get("pago").toString());
			deuda= (Entity) this.attrs.get("seleccionadoDetalle");
			if(pago> 0D && !deuda.toLong("idEmpresaEstatus").equals(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa())) {			
				saldo= Double.valueOf(deuda.toString("saldo"));
				regresar= pago<= (saldo * -1);
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch
		return regresar;
	} // validaPago

	public void doRegistrarPago() {
		mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas.Transaccion transaccion= null;
		TcManticEmpresasPagosDto pago= null;
		boolean tipoPago             = false;
		try {
			if(validaPago()) {
				pago= new TcManticEmpresasPagosDto();
				pago.setIdEmpresaDeuda(((Entity)this.attrs.get("seleccionadoDetalle")).getKey());
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observaciones").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pago").toString()));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas.Transaccion(pago, ((UISelectEntity)this.attrs.get("caja")).getKey(), -1L, ((UISelectEntity)this.attrs.get("idEmpresaPago")).getKey(), tipoPago ? -1L : ((UISelectEntity)this.attrs.get("banco")).getKey(), tipoPago ? "" : this.attrs.get("referencia").toString(), null, false, this.seleccionadosNotas, this.seleccionadosCredito);
				if(transaccion.ejecutar(EAccion.AGREGAR)) {
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta", ETipoMensaje.INFORMACION);
					this.doLoad();					
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante y distinto a 0 o la cuenta ya se encuentra finalizada.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doRegistrarPago  
  
	public void doRegistrarPagoGeneral() {
		Transaccion transaccion      = null;
		TcManticEmpresasPagosDto pago= null;
		boolean tipoPago             = false;
		try {
			if(validaPagoGeneral()) {
				pago= new TcManticEmpresasPagosDto();
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observacionesGeneral").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pagoGeneral").toString()));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, ((UISelectEntity)this.attrs.get("cajaGeneral")).getKey(), ((UISelectEntity)this.attrs.get("idProveedor")).getKey(), ((UISelectEntity)this.attrs.get("idEmpresaGeneral")).getKey(), tipoPago ? -1L: ((UISelectEntity)this.attrs.get("bancoGeneral")).getKey(), tipoPago ? "" : this.attrs.get("referenciaGeneral").toString(), false);
				if(transaccion.ejecutar(EAccion.PROCESAR)) {
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta");
					this.doLoad();					
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
  
	public void onTabChangeSegmentos(TabChangeEvent event) {
    Double saldo= 0D;
		if(event.getTab().getTitle().equals("Registro pago")) {
      if(this.seleccionadosSegmento!= null && this.seleccionadosSegmento.length> 0) {
        for(Entity cuenta: this.seleccionadosSegmento)					
					saldo= saldo+ Double.valueOf(cuenta.toString("saldo"));
				this.attrs.put("pagoSegmento", saldo);
      } // if
    } // if
  }  
  
  public void doCountCuentas() {
    LOG.info(this.seleccionadosSegmento);
  }
  
}
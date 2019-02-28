package mx.org.kaana.mantic.inventarios.devoluciones.backing;

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
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.inventarios.devoluciones.reglas.Transaccion;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import org.primefaces.context.RequestContext;

@Named(value = "manticInventariosDevolucionesFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final long serialVersionUID=1368701967796774746L;
  private Reporte reporte;
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idDevolucion", JsfBase.getFlashAttribute("idDevolucion"));
      this.attrs.put("notaEntrada", JsfBase.getFlashAttribute("notaEntrada"));
			this.toLoadCatalog();
      if(this.attrs.get("idDevolucion")!= null || this.attrs.get("notaEntrada")!= null) 
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
      params.put("sortOrder", "order by tc_mantic_devoluciones.registro desc");
      columns = new ArrayList<>();
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("articulos", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      this.lazyModel = new FormatCustomLazy("VistaDevolucionesDto", params, columns);
      UIBackingUtilities.resetDataTable();
			this.attrs.put("idDevolucion", null);
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
		String regresar= "/Paginas/Mantic/Inventarios/Devoluciones/accion";
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			if(eaccion.equals(EAccion.COMPLETO)) 
			  JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
			else 
			  JsfBase.setFlashAttribute("accion", eaccion);		
			if(!eaccion.equals(EAccion.COMPLETO) || ((eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) && ((Entity)this.attrs.get("seleccionado")).toLong("idDirecta").equals(2L))) 
				regresar= regresar.concat("?zOyOxDwIvGuCt=zNyLxMwAvCuEtAs");
			else
				regresar= regresar.concat("?zOyOxDwIvGuCt=zLyOxRwMvAuNt");
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Inventarios/Devoluciones/filtro");		
			JsfBase.setFlashAttribute("idDevolucion", eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
			JsfBase.setFlashAttribute("idNotaEntrada", eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)? ((Entity)this.attrs.get("seleccionado")).toLong("idNotaEntrada") : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar.concat(Constantes.REDIRECIONAR_AMPERSON);
  } // doAccion  
	
  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion((TcManticDevolucionesDto)DaoFactory.getInstance().findById(TcManticDevolucionesDto.class, seleccionado.getKey()));
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "La devolución de entrada se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar la devolucion de entrada.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("idDevolucion")) && !this.attrs.get("idDevolucion").toString().equals("-1"))
  		sb.append("(tc_mantic_devoluciones.id_devolucion=").append(this.attrs.get("idDevolucion")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("notaEntrada")))
  		sb.append("(tc_mantic_notas_entradas.consecutivo like '%").append(this.attrs.get("notaEntrada")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_devoluciones.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("idProveedor")) && !this.attrs.get("idProveedor").toString().equals("-1"))
  		sb.append("(tc_mantic_proveedores.id_proveedor= ").append(this.attrs.get("idProveedor")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_devoluciones.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_devoluciones.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("idDevolucionEstatus")) && !this.attrs.get("idDevolucionEstatus").toString().equals("-1"))
  		sb.append("(tc_mantic_devoluciones.id_devolucion_estatus= ").append(this.attrs.get("idDevolucionEstatus")).append(") and ");
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
	
	private void toLoadCatalog() {
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
      this.attrs.put("empresas", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			this.attrs.put("idEmpresa", new UISelectEntity("-1"));
      this.attrs.put("proveedores", (List<UISelectEntity>) UIEntity.build("VistaDevolucionesDto", "proveedores", params, columns));
			this.attrs.put("idProveedor", new UISelectEntity("-1"));
			columns.remove(0);
      this.attrs.put("catalogo", (List<UISelectEntity>) UIEntity.build("TcManticDevolucionesEstatusDto", "row", params, columns));
			this.attrs.put("idDevolucionEstatus", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
  public void doReporte() throws Exception{
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
		try{				
			reporteSeleccion= EReportes.ORDEN_COMPRA;
			this.reporte= JsfBase.toReporte();
			params= new HashMap<>();
			params.put("idDevolucion", ((Entity)this.attrs.get("seleccionado")).getKey());			
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
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L)
			rc.execute("start(" + this.reporte.getTotal() + ")");		
		else{
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
		} // else
	} // doVerificarReporte		

	public void doLoadEstatus() {
		Entity seleccionado          = null;
		Map<String, Object>params    = null;
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_devolucion_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticDevolucionesEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);
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
	} 
	
	public void doActualizarEstatus() {
		TcManticDevolucionesBitacoraDto bitacora= null;
		Transaccion transaccion= null;
		Entity seleccionado    = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			TcManticDevolucionesDto orden= (TcManticDevolucionesDto)DaoFactory.getInstance().findById(TcManticDevolucionesDto.class, seleccionado.getKey());
			bitacora= new TcManticDevolucionesBitacoraDto(Long.valueOf(this.attrs.get("estatus").toString()), (String)this.attrs.get("justificacion"), JsfBase.getIdUsuario(), seleccionado.getKey(), -1L, orden.getConsecutivo(), orden.getTotal());
			transaccion= new Transaccion(orden, bitacora);
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
	}	
	
	public String doMovimientos() {
		JsfBase.setFlashAttribute("tipo", ETipoMovimiento.DEVOLUCIONES);
		JsfBase.setFlashAttribute(ETipoMovimiento.DEVOLUCIONES.getIdKey(), ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Inventarios/Devoluciones/filtro");
		return "/Paginas/Mantic/Compras/Ordenes/movimientos".concat(Constantes.REDIRECIONAR);
	}
	
	public String doCreditosNotas() {
		JsfBase.setFlashAttribute("idTipoCreditoNota", 1L);
		JsfBase.setFlashAttribute("idDevolucion", ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Inventarios/Creditos/filtro");
		JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
		return "/Paginas/Mantic/Inventarios/Creditos/accion".concat(Constantes.REDIRECIONAR);
	}

	public String doNotaEntrada() {
		JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
		return "/Paginas/Mantic/Inventarios/Entradas/filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doNotaCredito() {
		JsfBase.setFlashAttribute("devolucion", this.attrs.get("devolucion"));
		return "/Paginas/Mantic/Inventarios/Creditos/filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doAgregar() {
		JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
  	JsfBase.setFlashAttribute("idNotaEntrada", ((Value)this.attrs.get("idNotaEntrada")).toLong());
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Inventarios/Devoluciones/filtro");		
		return "/Paginas/Mantic/Inventarios/Devoluciones/accion".concat(Constantes.REDIRECIONAR);
	}

  public void doReporte(String nombre) throws Exception {
    Parametros comunes = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
		try{		
      params= toPrepare();
      seleccionado = ((Entity)this.attrs.get("seleccionado"));
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());	
      params.put("sortOrder", "order by tc_mantic_devoluciones.id_empresa, tc_mantic_devoluciones.ejercicio, tc_mantic_devoluciones.orden");
      reporteSeleccion= EReportes.valueOf(nombre);
      if(reporteSeleccion.equals(EReportes.DEVOLUCIONES_DETALLE)){
        params.put("idDevolucion", seleccionado.toLong("idKey"));
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), seleccionado.toLong("idAlmacen"), seleccionado.toLong("idProveedor"),-1L);
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
	

}

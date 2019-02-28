package mx.org.kaana.mantic.inventarios.creditos.backing;

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
import mx.org.kaana.mantic.inventarios.creditos.reglas.Transaccion;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.db.dto.TcManticCreditosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticCreditosNotasDto;
import mx.org.kaana.mantic.enums.EReportes;
import org.primefaces.context.RequestContext;

@Named(value = "manticInventariosCreditosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final long serialVersionUID=1368701967796774746L;
  private Reporte reporte;
	
	public void doReporte(String nombre) throws Exception {
    Parametros comunes           = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
		try{		
      params= toPrepare();
      seleccionado = ((Entity)this.attrs.get("seleccionado"));
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());	
      params.put("sortOrder", "order by tc_mantic_creditos_notas.id_empresa, tc_mantic_creditos_notas.ejercicio, tc_mantic_creditos_notas.orden");
      reporteSeleccion= EReportes.valueOf(nombre);
      if(reporteSeleccion.equals(EReportes.NOTA_CREDITO_DETALLE)){
        params.put("idCreditoNota", seleccionado.toLong("idCreditoNota"));
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), -1L, seleccionado.toLong("idProveedor"), -1L);
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
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idCreditoNota", JsfBase.getFlashAttribute("idCreditoNota"));
			this.toLoadCatalog();
      if(this.attrs.get("idCreditoNota")!= null) 
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
      params.put("sortOrder", "order by tc_mantic_creditos_notas.registro desc");
      columns = new ArrayList<>();
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("folio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      this.lazyModel = new FormatCustomLazy("VistaCreditosNotasDto", params, columns);
      UIBackingUtilities.resetDataTable();
			this.attrs.put("idCreditoNota", null);
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

  public String doAccion(String accion, Long idTipoCreditoNota) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
		  JsfBase.setFlashAttribute("accion", eaccion);		
			if(eaccion.equals(EAccion.AGREGAR))
				JsfBase.setFlashAttribute("idTipoCreditoNota", idTipoCreditoNota);	
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Inventarios/Creditos/filtro");		
			JsfBase.setFlashAttribute("idCreditoNota", eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Inventarios/Creditos/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion((TcManticCreditosNotasDto)DaoFactory.getInstance().findById(TcManticCreditosNotasDto.class, seleccionado.getKey()), seleccionado.toDouble("importe"), null, null);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "La nota de crédito se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar la nota de crédito.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } 

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("idCreditoNota")) && !this.attrs.get("idCreditoNota").toString().equals("-1"))
  		sb.append("(tc_mantic_creditos_notas.id_credito_nota=").append(this.attrs.get("idCreditoNota")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("devolucion")))
  		sb.append("(tc_mantic_devoluciones.consecutivo like '%").append(this.attrs.get("devolucion")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("folio")))
  		sb.append("(tc_mantic_creditos_notas.folio like '%").append(this.attrs.get("folio")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_creditos_notas.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_creditos_notas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_creditos_notas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("idCreditoEstatus")) && !this.attrs.get("idCreditoEstatus").toString().equals("-1"))
  		sb.append("(tc_mantic_creditos_notas.id_credito_estatus= ").append(this.attrs.get("idCreditoEstatus")).append(") and ");
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
			columns.remove(0);
      this.attrs.put("catalogo", (List<UISelectEntity>) UIEntity.build("TcManticCreditosEstatusDto", "row", params, columns));
			this.attrs.put("idCreditoEstatus", new UISelectEntity("-1"));
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
			params.put("idCreditoNota", ((Entity)this.attrs.get("seleccionado")).getKey());			
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
			params.put(Constantes.SQL_CONDICION, "id_credito_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticCreditosEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);
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
		TcManticCreditosBitacoraDto bitacora= null;
		Transaccion transaccion= null;
		Entity seleccionado    = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			TcManticCreditosNotasDto orden= (TcManticCreditosNotasDto)DaoFactory.getInstance().findById(TcManticCreditosNotasDto.class, seleccionado.getKey());
			bitacora= new TcManticCreditosBitacoraDto(orden.getConsecutivo(), (String)this.attrs.get("justificacion"), Long.valueOf((String)this.attrs.get("estatus")), -1L, JsfBase.getIdUsuario(), seleccionado.getKey(), orden.getImporte());
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
	
	public String doDevolucion() {
		JsfBase.setFlashAttribute("idDevolucion", this.attrs.get("idDevolucion"));
		return "/Paginas/Mantic/Inventarios/Devoluciones/filtro".concat(Constantes.REDIRECIONAR);
	}
	
	public String doNotaEntrada() {
		JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
		return "/Paginas/Mantic/Inventarios/Entradas/filtro".concat(Constantes.REDIRECIONAR);
	}
	
	public String doImportar() {
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Inventarios/Creditos/filtro");		
		JsfBase.setFlashAttribute("idCreditoNota",((Entity)this.attrs.get("seleccionado")).getKey());
		return "importar".concat(Constantes.REDIRECIONAR);
	}
	
	public String doAgregar() {
		JsfBase.setFlashAttribute("idTipoCreditoNota", 1L);
		JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
  	JsfBase.setFlashAttribute("idDevolucion", ((Value)this.attrs.get("idDevolucion")).toLong());
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Inventarios/Creditos/filtro");		
		return "/Paginas/Mantic/Inventarios/Creditos/accion".concat(Constantes.REDIRECIONAR);
	}
}

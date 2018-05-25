package mx.org.kaana.mantic.compras.entradas.backing;

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
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.reglas.Transaccion;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.enums.EReportes;
import org.primefaces.context.RequestContext;

@Named(value = "manticComprasEntradasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final long serialVersionUID=1368701967796774746L;
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
			this.attrs.put("sucursales", JsfBase.getAutentifica().getSucursales());
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
      this.attrs.put("sortOrder", "order by tc_mantic_notas_entradas.id_empresa, tc_mantic_notas_entradas.ejercicio, tc_mantic_notas_entradas.orden");
			this.toLoadCatalog();
			if(this.attrs.get("idNotaEntrada")!= null)
				this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns= null;
		Map<String, Object> params= toPrepare();
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      this.lazyModel = new FormatCustomLazy("VistaNotasEntradasDto", params, columns);
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
		String regresar= "/Paginas/Mantic/Compras/Entradas/accion";
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			if(eaccion.equals(EAccion.COMPLETO)) 
			  JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
			else 
			  JsfBase.setFlashAttribute("accion", eaccion);		
			if(!eaccion.equals(EAccion.COMPLETO) || (eaccion.equals(EAccion.MODIFICAR) && ((Entity)this.attrs.get("seleccionado")).toLong("idDirecta").equals(2L))) 
				regresar= regresar.concat("?zOyOxDwIvGuCt=zNyLxMwAvCuEtAs");
			else
				regresar= regresar.concat("?zOyOxDwIvGuCt=zLyOxRwMvAuNt");
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Compras/Entradas/filtro");		
			JsfBase.setFlashAttribute("idNotaCompra", eaccion.equals(EAccion.MODIFICAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
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
			transaccion= new Transaccion(new TcManticOrdenesComprasDto(seleccionado.getKey()));
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "La nota de entrada se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar la nota de entrada.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		regresar.put("idNotaEntrada", attrs.get("idNotaEntrada"));
		regresar.put("ordenCompra", attrs.get("ordenCompra"));
		regresar.put("notaEntrada", attrs.get("notaEntrada"));
		regresar.put("idEmpresa", attrs.get("idEmpresa"));
		regresar.put("idProveedor", attrs.get("idProveedor"));
		regresar.put("idOrdenEstatus", attrs.get("idOrdenEstatus"));
		if(Cadena.isVacio(attrs.get("fechaInicio")))
		  regresar.put("fechaInicio", "20160101");	
		else
			regresar.put("fechaInicio", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)attrs.get("fechaInicio")));	
		if(Cadena.isVacio(attrs.get("fechaTermino")))
		  regresar.put("fechaTermino", "29991231");	
		else
			regresar.put("fechaTermino", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)attrs.get("fechaTermino")));	
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
      this.attrs.put("sucursales", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
      this.attrs.put("proveedores", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "proveedores", params, columns));
			columns.remove(0);
      this.attrs.put("estatus", (List<UISelectEntity>) UIEntity.build("TcManticOrdenesEstatusDto", "row", params, columns));
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
			params.put("idNotaEntrada", ((Entity)this.attrs.get("seleccionado")).getKey());			
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
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
		} // else
	} // doVerificarReporte		
	
}

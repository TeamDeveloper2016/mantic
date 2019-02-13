package mx.org.kaana.mantic.inventarios.entradas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.libs.pagina.IFilterImportar;
import org.primefaces.context.RequestContext;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/06/2018
 *@time 07:51:53 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "manticNotasEntradasDiferencias")
@ViewScoped
public class Diferencias extends IFilterImportar implements Serializable {

  private static final long serialVersionUID = 8793667741599428311L;
  private Reporte reporte;
	
	protected FormatLazyModel lazyDevoluciones;

	public FormatLazyModel getLazyDevoluciones() {
		return lazyDevoluciones;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada"));
      this.attrs.put("idAlmacen", JsfBase.getFlashAttribute("idAlmacen"));
      this.attrs.put("idProveedor", JsfBase.getFlashAttribute("idProveedor"));
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
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_SIN_DECIMALES));      
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));      
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("cantidades", EFormatoDinamicos.NUMERO_SIN_DECIMALES));      
      this.attrs.put("sortOrder", "order by tc_mantic_notas_detalles.nombre");
      this.lazyModel = new FormatCustomLazy("VistaDevolucionesDto", "confronta", this.attrs, columns);
      UIBackingUtilities.resetDataTable();
			this.attrs.put("seleccionado", null);
			this.doRowSelectEvent();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad
	
	public String doRegresar() {
		JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
		return "filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doNotaEntrada() {
		JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
		return "/Paginas/Mantic/Inventarios/Entradas/filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doDevolucion() {
		JsfBase.setFlashAttribute("idDevolucion", this.attrs.get("idDevolucion"));
		return "/Paginas/Mantic/Inventarios/Devoluciones/filtro".concat(Constantes.REDIRECIONAR);
	}

	public void doRowSelectEvent() {
		Entity entity= (Entity)this.attrs.get("seleccionado");
    List<Columna> columns     = null;
	  Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_SIN_DECIMALES));      
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));      
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
		  params.put(Constantes.SQL_CONDICION, " ");
			if(entity!= null) {
			  params.put("idNotaEntrada", entity.toLong("idNotaEntrada"));
			  params.put(Constantes.SQL_CONDICION, " and tc_mantic_notas_detalles.id_articulo= "+ entity.toLong("idArticulo"));
			} // if
			else 
			  params.put("idNotaEntrada", this.attrs.get("idNotaEntrada"));
      params.put("sortOrder", "order by tc_mantic_devoluciones.consecutivo, tc_mantic_notas_detalles.nombre");
      this.lazyDevoluciones = new FormatCustomLazy("VistaDevolucionesDto", "consulta", params, columns);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally	
	}
	
	public String toColor(Entity row) {
		return !row.toDouble("unidades").equals(0D)? "janal-tr-orange": "";
	} 

  public String getSuma() {
		Double sum= 0D;
		for (IBaseDto item: (List<IBaseDto>)lazyModel.getWrappedData()) {
			Entity row= (Entity)item;
			sum+= new Double(row.toString("cantidad"));
		} // for
	  return Global.format(EFormatoDinamicos.NUMERO_SAT_DECIMALES, sum);
	}	
	
  public String getCantidades() {
		Double sum= 0D;
		for (IBaseDto item: (List<IBaseDto>)lazyDevoluciones.getWrappedData()) {
			Entity row= (Entity)item;
			sum+= new Double(row.toString("cantidad"));
		} // for
	  return Global.format(EFormatoDinamicos.NUMERO_SAT_DECIMALES, sum);
	}	
  
  public void doReporte(String nombre) throws Exception{
		Parametros comunes = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
		try{		
      reporteSeleccion= EReportes.valueOf(nombre);
      if(!reporteSeleccion.equals(EReportes.NOTAS_ENTRADA)){
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), Long.valueOf(this.attrs.get("idAlmacen").toString()), Long.valueOf(this.attrs.get("idProveedor").toString()), -1L);
      }
      else
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, this.attrs, parametros));		
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
			rc.execute("generalHide()");		
			JsfBase.addMessage("Generar reporte","No se encontraron registros para el reporte", ETipoMensaje.ALERTA);
		} // else
	} // doVerificarReporte		
	
}

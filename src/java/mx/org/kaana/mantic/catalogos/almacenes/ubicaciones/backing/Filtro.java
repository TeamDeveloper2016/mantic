package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.enums.EReportes;
import org.primefaces.context.RequestContext;

@Named(value = "manticCatalogosAlmacenesUbicacionesFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  private Reporte reporte;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("idPrincipal", 1L);
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());			
			toLoadEmpresas();
			doLoadAlmacenes(); 
			if(JsfBase.getFlashAttribute("idAlmacenUbicacion")!= null){
				this.attrs.put("idAlmacenUbicacion", JsfBase.getFlashAttribute("idAlmacenUbicacion"));
				doLoad();
				this.attrs.put("idAlmacenUbicacion", null);
			} // if				
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
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("piso", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cuarto", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("anaquel", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("charola", EFormatoDinamicos.MAYUSCULAS));
      params.put("sortOrder", "order by tc_mantic_almacenes_ubicaciones.id_almacen, tc_mantic_almacenes_ubicaciones.piso, tc_mantic_almacenes_ubicaciones.cuarto, tc_mantic_almacenes_ubicaciones.anaquel, tc_mantic_almacenes_ubicaciones.charola");
      this.lazyModel = new FormatCustomLazy("VistaAlmacenesUbicacionesDto", params, columns);
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
		StringBuilder sb= new StringBuilder();		
		UISelectEntity empresa= (UISelectEntity) this.attrs.get("empresa");
		UISelectEntity almacen= (UISelectEntity) this.attrs.get("almacen");
		if(!empresa.getKey().equals(-1L))
		  regresar.put("sucursales", empresa.getKey());
		else
		  regresar.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(!almacen.getKey().equals(-1L))
		  sb.append("tc_mantic_almacenes.id_almacen=").append(almacen.getKey()).append(" and ");
		if(this.attrs.get("idAlmacenUbicacion")!= null && !Cadena.isVacio(this.attrs.get("idAlmacenUbicacion")))
			sb.append("tc_mantic_almacenes_ubicaciones.id_almacen_ubicacion=").append(this.attrs.get("idAlmacenUbicacion")).append(" and ");			
		if(this.attrs.get("piso")!= null && !Cadena.isVacio(this.attrs.get("piso")))
			sb.append("tc_mantic_almacenes_ubicaciones.piso like '%").append(this.attrs.get("piso")).append("%' and ");			
		if(this.attrs.get("cuarto")!= null && !Cadena.isVacio(this.attrs.get("cuarto")))
			sb.append("tc_mantic_almacenes_ubicaciones.cuarto like '%").append(this.attrs.get("cuarto")).append("%' and ");			
		if(this.attrs.get("anaquel")!= null && !Cadena.isVacio(this.attrs.get("anaquel")))
			sb.append("tc_mantic_almacenes_ubicaciones.anaquel like '%").append(this.attrs.get("anaquel")).append("%' and ");			
		if(this.attrs.get("charola")!= null && !Cadena.isVacio(this.attrs.get("charola")))
			sb.append("tc_mantic_almacenes_ubicaciones.charola like '%").append(this.attrs.get("charola")).append("%' and ");			
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	} // toPrepare

	private void toLoadEmpresas() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			columns= new ArrayList<>();			
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("sucursales", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			this.attrs.put("empresa", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // toLoadEmpresas
	
	public void doLoadAlmacenes() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
		UISelectEntity empresa    = null; 
    try {
			empresa= (UISelectEntity) this.attrs.get("empresa");
			if(!empresa.getKey().equals(-1L)){
				columns= new ArrayList<>();			
				params= new HashMap<>();
				params.put("sucursales", empresa.getKey());				
				columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				this.attrs.put("almacenes", (List<UISelectEntity>) UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
			} // if
			else
				this.attrs.put("almacenes", new ArrayList<>());
			this.attrs.put("almacen", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // toLoadAlmacenes  
  
	public String doOrganigrama() {
    String regresar= null;
		try {			
			JsfBase.setFlashAttribute("retorno", "filtro");
			JsfBase.setFlashAttribute("empresaOrganigram", ((boolean)this.attrs.get("isMatriz"))? JsfBase.getAutentifica().getEmpresa().getDependencias(): this.attrs.get("idEmpresa").toString());
			regresar= "organigrama".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
  } // doOrganigrama
	
	public String doOrganigramaIndividual() {
    String regresar= null;
		try {			
			JsfBase.setFlashAttribute("retorno", "filtro");
			JsfBase.setFlashAttribute("empresaOrganigram", ((Entity)this.attrs.get("seleccionado")).toString("idEmpresa"));
			JsfBase.setFlashAttribute("idAlmacenUbicacion", ((Entity)this.attrs.get("seleccionado")).toString("idAlmacenUbicacion"));
			regresar= "organigrama".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
  } // doOrganigrama

  public String doArticulosIndividual() {
    String regresar= null;
		try {			
			JsfBase.setFlashAttribute("retorno", "filtro");
			JsfBase.setFlashAttribute("empresaOrganigram", ((Entity)this.attrs.get("seleccionado")).toString("idEmpresa"));
			JsfBase.setFlashAttribute("idAlmacen", ((Entity)this.attrs.get("seleccionado")).toString("idAlmacen"));
			JsfBase.setFlashAttribute("idAlmacenUbicacion", ((Entity)this.attrs.get("seleccionado")).toString("idAlmacenUbicacion"));
			regresar= "articulos".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
  } // doArticulos
  
  public void doReporte(String nombre) throws Exception {
    Parametros comunes = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
    Map<String, Object>params    = null;
		try {		
      params= toPrepare();
      seleccionado = ((Entity)this.attrs.get("seleccionado"));
      if(seleccionado != null){
        params.put("idKeyTransferencia", seleccionado.getKey());
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(),seleccionado.toLong("idAlmacen"),-1L,-1L);
      }
      else{
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      }
      params.put("sortOrder", "order by tc_mantic_almacenes_ubicaciones.id_almacen, tc_mantic_almacenes_ubicaciones.piso, tc_mantic_almacenes_ubicaciones.cuarto, tc_mantic_almacenes_ubicaciones.anaquel, tc_mantic_almacenes_ubicaciones.charola");
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());	
      reporteSeleccion= EReportes.valueOf(nombre);
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
      if(doVerificarReporte())
        this.reporte.doAceptar();			
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // doReporte
  
  public boolean doVerificarReporte() {
    boolean regresar = false;
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L){
			rc.execute("start(" + this.reporte.getTotal() + ")");		
      regresar = true;
    }
		else{
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
      regresar = false;
		} // else
    return regresar;
	} // doVerificarReporte	

}
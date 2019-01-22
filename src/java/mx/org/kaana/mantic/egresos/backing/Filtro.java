package mx.org.kaana.mantic.egresos.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.egresos.reglas.Transaccion;

@Named(value = "manticEgresosFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {    	      
      this.attrs.put("descripcion", "");
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());  
			loadEstatus();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadEstatus(){
		List<UISelectItem>estatus= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			estatus= UISelect.build("TcManticEgresosEstatusDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS);
			estatus.add(0, new UISelectItem(-1L, "TODOS"));
			this.attrs.put("estatus", estatus);
			this.attrs.put("idEstatus", UIBackingUtilities.toFirstKeySelectItem(estatus));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadEstatus
	
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= null;
    try {
      columns = new ArrayList<>();
			params= new HashMap<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));			
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));			
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));			
			params.put(Constantes.SQL_CONDICION, toCondicion());			
      this.lazyModel = new FormatCustomLazy("VistaEgresosDto", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad
	
	private String toCondicion(){
		String regresar        = null;
		String search          = null;
		StringBuilder condicion= null;
		try {			
			condicion= new StringBuilder("");			
			if(!Cadena.isVacio(this.attrs.get("idEstatus")) && !Long.valueOf(this.attrs.get("idEstatus").toString()).equals(-1L))
				condicion.append("tc_mantic_egresos.id_egreso_estatus=").append(this.attrs.get("idEstatus")).append(" and ");						
			if(!Cadena.isVacio(this.attrs.get("fecha")))
				condicion.append("(date_format(tc_mantic_egresos.fecha, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fecha"))).append("') and ");	
			search= (String) this.attrs.get("descripcion");
			if(!Cadena.isVacio(search)) {
				search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();				
				condicion.append("upper(tc_mantic_egresos.descripcion) regexp upper('.*").append(search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*")).append(".*') and ");						
			} // if									
			if(Cadena.isVacio(condicion))
				regresar= Constantes.SQL_VERDADERO;
			else
				regresar= condicion.substring(0, condicion.length()-4);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCondicion

  public String doAccion(String accion) {
    EAccion eaccion = null;
    try {
      eaccion = EAccion.valueOf(accion.toUpperCase());
      JsfBase.setFlashAttribute("accion", eaccion);
      JsfBase.setFlashAttribute("idEgreso", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) || eaccion.equals(EAccion.COPIAR) || eaccion.equals(EAccion.ACTIVAR)) ? ((Entity) this.attrs.get("seleccionado")).getKey() : -1L);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion	  		
	
  public String doMasivo() {
    JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Egresos/filtro");
    JsfBase.setFlashAttribute("idTipoMasivo", ECargaMasiva.EGRESOS.getId());
    return "/Paginas/Mantic/Catalogos/Masivos/importar".concat(Constantes.REDIRECIONAR);
	} // doMasivo	
	
	public void doRegistraNota(){
		Transaccion transaccion= null;
		Entity seleccionado    = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
			transaccion= new Transaccion(seleccionado.getKey(), this.attrs.get("nota").toString());
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
				JsfBase.addMessage("Registrar nota", "Se registro de forma correcta la nota.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Registrar nota", "Ocurrió un error al registrar la nota.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doRegistraNota
	
	public String doDetalle(){
		String regresar= null;		
		try {
			JsfBase.setFlashAttribute("idEgreso", ((Entity)this.attrs.get("seleccionado")).getKey());
			regresar= "detalle".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doDetalle
}
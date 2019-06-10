package mx.org.kaana.mantic.catalogos.empaques.backing;

import java.io.Serializable;
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
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "manticCatalogosEmpaquesFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

	private static final long serialVersionUID = 572331401280070748L;

  @PostConstruct
  @Override
  protected void init() {
    try {    	
      this.attrs.put("codigo", "");      
      this.attrs.put("idTipoArticulo", 1L);      
			this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= this.toPrepare();
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("claveEmpaque", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empaque", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("claveUnidad", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("unidad", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatCustomLazy("VistaEmpaquesUnidadesDto", "row", params, columns);
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
	
	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();			
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("empaques", (List<UISelectEntity>) UIEntity.build("TcManticEmpaquesDto", "row", params, columns));
			this.attrs.put("idEmpaque", new UISelectEntity("-1"));
			this.doUpdateEmpaque();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= null;
		StringBuilder sb            = null;
		try {
			sb= new StringBuilder();	
			regresar= new HashMap<>();								
			if(!Cadena.isVacio(this.attrs.get("idEmpaque")) && !this.attrs.get("idEmpaque").toString().equals("-1"))
  		  sb.append("tr_mantic_empaque_unidad_medida.id_empaque=").append(this.attrs.get("idEmpaque")).append(" and ");
			if(!Cadena.isVacio(this.attrs.get("idUnidad")) && !this.attrs.get("idUnidad").toString().equals("-1"))
  		  sb.append("tr_mantic_empaque_unidad_medida.id_unidad_medida=").append(this.attrs.get("idUnidad")).append(" and ");
			if(Cadena.isVacio(sb.toString()))
				regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			else
			  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));					  
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
      JsfBase.setFlashAttribute("idEmpaqueUnidadMedida", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity) this.attrs.get("seleccionado")).getKey() : -1L);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion
	
	public void doUpdateEmpaque() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();			
      columns.add(new Columna("claveUnidad", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("unidad", EFormatoDinamicos.MAYUSCULAS));
			if(!Cadena.isVacio(this.attrs.get("idEmpaque")) && !this.attrs.get("idEmpaque").toString().equals("-1")) {
  			params.put(Constantes.SQL_CONDICION, "tr_mantic_empaque_unidad_medida.id_empaque="+ this.attrs.get("idEmpaque"));
        this.attrs.put("unidades", (List<UISelectEntity>) UIEntity.build("VistaEmpaquesUnidadesDto", "row", params, columns));
			} // if	
			this.attrs.put("idUnidad", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
}
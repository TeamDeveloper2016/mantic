package mx.org.kaana.mantic.catalogos.listasprecios.backing;

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

@Named(value = "manticCatalogosListaPreciosArticulos")
@ViewScoped
public class Articulos extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("codigo", "");
      this.attrs.put("nombre", "");    
      this.attrs.put("auxiliar", "");    
      this.attrs.put("idProveedor", "");    
      this.attrs.put("sortOrder"," order by tc_mantic_listas_precios_detalles.descripcion, tc_mantic_proveedores.razon_social");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    try {
			this.attrs.put("idProveedor", new UISelectEntity(new Entity(-1L)));
			this.doLoadArticulos();
			this.doLoadProveedores();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoad
  
  public void doLoadArticulos() {
    List<Columna> campos     = null;
    Map<String, Object>params= toPrepare();
    try {
      campos= new ArrayList<>();
      campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      campos.add(new Columna("precio", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      this.lazyModel = new FormatCustomLazy("VistaListasArchivosDto", "lazyArticulos", params, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(campos);
      Methods.clean(params);
    } // finally		
  } // doLoad
  
  private void doLoadProveedores() {
    Map<String, Object> params= toPrepare();
	  try {
      this.attrs.put("proveedores", UIEntity.build("VistaListasArchivosDto", "proveedores", params));
      this.attrs.put("idProveedor", new UISelectEntity(new Entity(-1L)));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally		
  }
 
  private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("codigo")))
  		sb.append("upper(tc_mantic_listas_precios_detalles.codigo) like upper('%").append(this.attrs.get("codigo")).append("%') and ");
    if(!Cadena.isVacio(this.attrs.get("auxiliar")))
  		sb.append("upper(tc_mantic_listas_precios_detalles.auxiliar) like upper('%").append(this.attrs.get("auxiliar")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("nombre"))) {
		  String nombre= ((String)this.attrs.get("nombre")).toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
  		sb.append("(upper(tc_mantic_listas_precios_detalles.descripcion) like upper('%").append(this.attrs.get("nombre")).append("%') or ");
  		sb.append(" upper(tc_mantic_listas_precios_detalles.descripcion) regexp '.*").append(nombre).append(".*') and ");
	  } // if
		if(!Cadena.isVacio(this.attrs.get("idProveedor")) && (!this.attrs.get("idProveedor").toString().equals("-1")))
  		sb.append("tc_mantic_listas_precios.id_proveedor = ").append(((UISelectEntity)this.attrs.get("idProveedor")).getKey().toString()).append(" and ");
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;
	}
  
}

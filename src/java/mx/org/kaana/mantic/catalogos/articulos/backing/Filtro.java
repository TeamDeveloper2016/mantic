package mx.org.kaana.mantic.catalogos.articulos.backing;

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
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.RegistroArticulo;
import mx.org.kaana.mantic.catalogos.articulos.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.facturas.beans.ArticuloFactura;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@Named(value = "manticCatalogosArticulosFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
    	this.attrs.put("buscaPorCodigo", false);
      this.attrs.put("codigo", "");
      this.attrs.put("nombre", "");
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());      
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
      columns = new ArrayList<>();
			params= new HashMap<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
			params.put("condicion", toCondicion());			
      this.lazyModel = new FormatCustomLazy("VistaArticulosDto", "row", params, columns);
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
			condicion= new StringBuilder();
			if(!Cadena.isVacio(this.attrs.get("codigo")))
				condicion.append("upper(tc_mantic_articulos_codigos.codigo) like upper('%").append(this.attrs.get("codigo")).append("%') and ");			
			search= (String) this.attrs.get("nombre");
			if(!Cadena.isVacio(search)) {
				search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();				
				condicion.append("upper(tc_mantic_articulos.nombre) regexp upper('.*").append(search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*")).append(".*') and ");						
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
      JsfBase.setFlashAttribute("idArticulo", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) || eaccion.equals(EAccion.COPIAR) || eaccion.equals(EAccion.ACTIVAR)) ? ((Entity) this.attrs.get("seleccionado")).getKey() : -1L);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion
  
	public void doOpenDialog(EAccion accion, Long idArticulo){
		Map<String, List<String>> params= null;
		List<String> options            = null;		
		try {						
			params = new HashMap<>();		
			options= new ArrayList<>();		
			options.add(accion.name());
			options.add(idArticulo.toString());
			params.put("data", options);
			RequestContext.getCurrentInstance().openDialog("express");//, getConfigDialogo(), params);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // doOpenDialog
	
	public void onReturnValues(SelectEvent event) {
		Object object     = event.getObject();
		String[] respuesta= null;
		try {
			respuesta= ((String) object).split(Constantes.TILDE);			
			JsfBase.addMessage("Articulos", respuesta[0], ETipoMensaje.valueOf(respuesta[1]));			
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // onReturnValues
	
	private Map<String, Object> getConfigDialogo() throws Exception{
		Map<String, Object> regresar= null;
		try {
			regresar= new HashMap<>();
			regresar.put("modal", true);
			regresar.put("draggable", false);
			regresar.put("closable", false);
			regresar.put("resizable", false);
			regresar.put("contentHeight", 380);			
			regresar.put("contentWidth", 900);						
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // getConfigDialogo
	
  public void doEliminar() {
    Transaccion transaccion = null;
    Entity seleccionado = null;
    RegistroArticulo registro = null;
    try {
      seleccionado = (Entity) this.attrs.get("seleccionado");
      registro = new RegistroArticulo();
      registro.setIdArticulo(seleccionado.getKey());
      transaccion = new Transaccion(registro);
      if (transaccion.ejecutar(EAccion.ELIMINAR)) {
        JsfBase.addMessage("Eliminar articulo", "El art�culo se ha eliminado correctamente.", ETipoMensaje.ERROR);
      } else {
        JsfBase.addMessage("Eliminar articulo", "Ocurri� un error al eliminar la art�culo.", ETipoMensaje.ERROR);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminar
	
	public void doUpdateArticulos() {
		List<Columna> columns         = null;
    Map<String, Object> params    = new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo        = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);
			String search= new String((String)this.attrs.get("codigo")); 
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
        articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 40L);
			else
        articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 40L);
      this.attrs.put("articulos", articulos);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}	

	public List<UISelectEntity> doCompleteArticulo(String query) {
		this.attrs.put("existe", null);
		this.attrs.put("codigo", query);
    this.doUpdateArticulos();		
		return (List<UISelectEntity>)this.attrs.get("articulos");
	}	

  public void doFindArticulo() {
		try {
    	List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
	    UISelectEntity articulo= (UISelectEntity)this.attrs.get("custom");
			if(articulo== null)
			  articulo= new UISelectEntity(new Entity(-1L));
			else
				if(articulos.indexOf(articulo)>= 0) 
					articulo= articulos.get(articulos.indexOf(articulo));
			  else
			    articulo= articulos.get(0);
			this.attrs.put("seleccionado", new Entity(articulo.toLong("idArticulo")));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 
	
	public void doPublicarFacturama(){
		TransaccionFactura transaccion= null;
		CFDIGestor gestor             = null;
		ArticuloFactura articulo      = null;
		try {
			gestor= new CFDIGestor(((Entity)this.attrs.get("seleccionado")).getKey());
			articulo= gestor.toArticuloFactura();			
			transaccion= new TransaccionFactura(articulo);
			if(transaccion.ejecutar(EAccion.AGREGAR))
				JsfBase.addMessage("Registrar articulo en facturama", "Se registro de forma correcta.");
			else
				JsfBase.addMessage("Registrar articulo en facturama", "Ocurrio un error al registrar el articulo en facturama.");			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doPublicarFacturama
	
  public String doMasivo() {
    JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Articulos/filtro");
    JsfBase.setFlashAttribute("idTipoMasivo", ECargaMasiva.ARTICULOS.getId());
    return "/Paginas/Mantic/Catalogos/Masivos/importar".concat(Constantes.REDIRECIONAR);
	}
	
}
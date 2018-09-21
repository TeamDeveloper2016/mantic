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
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.RegistroArticulo;
import mx.org.kaana.mantic.catalogos.articulos.reglas.Transaccion;
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
    List<Columna> campos = null;
    try {
      campos = new ArrayList<>();
      campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			if(this.attrs.get("nombre")!= null)
				this.attrs.put("expresion", ((String)this.attrs.get("nombre")).toUpperCase().replaceAll("(,| |\\t)+", ".*.*"));
			else
				this.attrs.put("expresion", "A");
      this.lazyModel = new FormatCustomLazy("VistaArticulosDto", "row", this.attrs, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(campos);
    } // finally		
  } // doLoad

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
  
	public void doExpress() {
		try {
			JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
			JsfBase.setFlashAttribute("idArticulo", -1L);			
			RequestContext.getCurrentInstance().execute("prepareDialogExpressAdd();");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	} // doExpress

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
        JsfBase.addMessage("Eliminar articulo", "El artículo se ha eliminado correctamente.", ETipoMensaje.ERROR);
      } else {
        JsfBase.addMessage("Eliminar articulo", "Ocurrió un error al eliminar la artículo.", ETipoMensaje.ERROR);
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
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
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
			this.attrs.put("seleccionado", new Entity(articulo.getKey()));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 
}
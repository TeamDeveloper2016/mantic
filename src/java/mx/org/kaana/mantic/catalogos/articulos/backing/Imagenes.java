package mx.org.kaana.mantic.catalogos.articulos.backing;

import java.io.Serializable;
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
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.CodigoArticulo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/05/2018
 *@time 02:19:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticCatalogosArticulosImagenes")
@ViewScoped
public class Imagenes extends IBaseAttribute implements Serializable {

	private static final Log LOG=LogFactory.getLog(Imagenes.class);
	private static final long serialVersionUID=-6770709196941718368L;

	private Entity articulo;
	private StreamedContent image;
	private StreamedContent pivote;
	private List<CodigoArticulo> articulos;

	public Entity getArticulo() {
		return articulo;
	}

	public StreamedContent getImage() {
		return image;
	}
	
	public StreamedContent getPivote() {
		return pivote;
	}
	
	public List<CodigoArticulo> getArticulos() {
		return articulos;
	}

	@Override
	@PostConstruct
	protected void init() {
  	this.attrs.put("buscaPorCodigo", false);
    this.attrs.put("idPivote", JsfBase.getFlashAttribute("idPivote")== null? 10198L: JsfBase.getFlashAttribute("idPivote"));
		this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Mantic/Catalogos/Articulos/filtro": JsfBase.getFlashAttribute("retorno"));
    if(this.attrs.get("idPivote")!= null) 
			this.doLoad();
	  this.articulos= new ArrayList<>();
	}
	
	private void doLoad() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>(); 
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idArticulo", this.attrs.get("idPivote"));
			this.articulo= (Entity)DaoFactory.getInstance().toEntity("VistaArticulosDto", "imagen", params);
			this.image   = LoadImages.getImage(this.articulo.toString("idArticulo"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
      Methods.clean(columns);
			Methods.clean(params);
		} // finally	
	}
	
	private void updateArticulo(UISelectEntity articulo) throws Exception {
		this.attrs.put("idArticulo", null);
    Map<String, Object> params= new HashMap<>();
		try {
			if(articulo.size()> 1) {
				if(this.articulos.indexOf(new CodigoArticulo(articulo.toLong("idArticulo")))< 0 && !this.attrs.get("idPivote").equals(articulo.toLong("idArticulo"))) {
 					CodigoArticulo item= new CodigoArticulo(articulo.toLong("idArticulo"), articulo.toString("propio"), articulo.toString("nombre"));
    			params.put("idArticulo", articulo.toLong("idArticulo"));
					Value idImagen= (Value)DaoFactory.getInstance().toField("VistaArticulosDto", "imagen", params, "idImagen");
					if(idImagen!= null && idImagen.toLong()> 0L)
						item.setCantidad(idImagen.toLong());
					else
						item.setCantidad(10198L);
					this.attrs.put("idArticulo", articulo.toLong("idArticulo"));
					this.articulos.add(item);
					RequestContext.getCurrentInstance().execute("jsKardex.cursor.top= "+ (this.articulos.size()- 1)+"; jsKardex.callback("+ articulo +");");
				} // if
				else {
					this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO YA ESTA EN LA LISTA DE ARTICULOS</span>");
					this.attrs.put("articulo", null);
				}	// else 
			} // if
			else {
				this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
				this.attrs.put("articulo", null);
			} // if	
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally	
	}
	
	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
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
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 40L));
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

	public String doAceptar() {
		String regresar= null;
    try {			
			regresar= ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
		return regresar;
	}
	
  public void doFindArticulo() {
		try {
    	List<UISelectEntity> list= (List<UISelectEntity>)this.attrs.get("articulos");
	    UISelectEntity articulo  = (UISelectEntity)this.attrs.get("custom");
			if(articulo== null)
			  articulo= new UISelectEntity(new Entity(-1L));
			else
				if(list.indexOf(articulo)>= 0) 
					articulo= list.get(list.indexOf(articulo));
			  else
			    articulo= list.get(0);
			this.updateArticulo(articulo);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 

	public void doUpdateArticulo(String codigo) {
    try {
  		List<UISelectEntity> list= this.doCompleteArticulo(codigo);
			UISelectEntity articulo= UIBackingUtilities.toFirstKeySelectEntity(list);
			this.updateArticulo(articulo);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	
	
	public void doEliminar(CodigoArticulo seleccionado) {
		if(seleccionado!= null) 
	    this.articulos.remove(seleccionado);
	}

	public String doCancelar() {   
  	JsfBase.setFlashAttribute("idArticulo", this.attrs.get("idPivote"));
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

	public void doPrepareImage(Long idImagen, String nombre) {
		try {
			LOG.info("idImagen: "+ idImagen);
			this.attrs.put("nombre", nombre);
			this.pivote= LoadImages.getImage(String.valueOf(idImagen));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
}
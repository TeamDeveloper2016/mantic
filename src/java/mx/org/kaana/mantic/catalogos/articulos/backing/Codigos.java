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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.acceso.beans.Sucursal;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.CodigoArticulo;
import org.primefaces.context.RequestContext;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/05/2018
 *@time 02:19:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticCatalogosArticulosCodigos")
@ViewScoped
public class Codigos extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-6770709196941718368L;

	private List<CodigoArticulo> articulos;

	public List<CodigoArticulo> getArticulos() {
		return articulos;
	}

	@Override
	@PostConstruct
	protected void init() {
  	this.attrs.put("buscaPorCodigo", false);
    this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
		this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Mantic/Inventarios/Entradas/filtro": JsfBase.getFlashAttribute("retorno"));
    if(this.attrs.get("idNotaEntrada")!= null) 
			this.doLoad();
		else
		  this.articulos= new ArrayList<>();
	}
	
	private void doLoad() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>(); 
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params = new HashMap<>();
			params.put("idNotaEntrada", this.attrs.get("idNotaEntrada"));
			params.put("idBarras", 1L);
			this.articulos= (List<CodigoArticulo>)DaoFactory.getInstance().toEntitySet(CodigoArticulo.class, "VistaArticulosDto", "barras", params);
			if(this.articulos== null)
				this.articulos= new ArrayList<>();
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
		if(articulo.size()> 1) {
			if(this.articulos.indexOf(new CodigoArticulo(articulo.toLong("idArticulo")))< 0) {
  			// this.image= LoadImages.getImage(articulo.toLong("idArticulo"));
				this.attrs.put("idArticulo", articulo.toLong("idArticulo"));
				this.articulos.add(new CodigoArticulo(articulo.toLong("idArticulo"), articulo.toString("propio"), articulo.toString("nombre")));
				UIBackingUtilities.execute("jsKardex.cursor.top= "+ (this.articulos.size()- 1)+"; jsKardex.callback("+ articulo +");");
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
	}
	
	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
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
		String[] codes = null;
		String code    = "";
		Long size      = 0L;
		String title   = "";
    try {			
			if(!this.articulos.isEmpty()){
				for(CodigoArticulo codigo: this.articulos)
					size= size + codigo.getCantidad();
				codes= new String[size.intValue()];
				for(int count=0; count<this.articulos.size(); count ++){
					for(int countC=0; countC<this.articulos.get(count).getCantidad().intValue(); countC ++){
						codes[count]= this.articulos.get(count).getPropio();
						code= code.concat(this.articulos.get(count).getPropio()).concat("~");
					} // for
				} // for
				this.attrs.put("codes", codes);
				code= code.substring(0, code.length()-1);
				for(Sucursal sucursal: JsfBase.getAutentifica().getSucursales()){
					if(sucursal.isMatriz())
						title= sucursal.getTitulo();
				} // for					
				UIBackingUtilities.execute("printCode('"+code+"','"+title+"');");
				regresar= ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
			} // if
			else
				JsfBase.addMessage("No hay códigos por imprimir", ETipoMensaje.ERROR);
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
  	JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

}

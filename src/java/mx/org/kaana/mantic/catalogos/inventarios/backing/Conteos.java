package mx.org.kaana.mantic.catalogos.inventarios.backing;

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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.inventarios.beans.ArticuloInventario;
import mx.org.kaana.mantic.catalogos.inventarios.reglas.Transaccion;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;


@Named(value = "manticCatalogosInventariosConteos")
@ViewScoped
public class Conteos extends IBaseFilter implements Serializable {
	
	private static final long serialVersionUID = 5570593377763068163L;	
	
	private List<ArticuloInventario> inventarios;
	private StreamedContent image;
		
	public StreamedContent getImage() {
		return image;
	}
	
	@PostConstruct
  @Override
  protected void init() {
    try {			
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());	
    	this.attrs.put("buscaPorCodigo", false);
			loadAlmacenes();
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
			columns= new ArrayList<>();
      columns.add(new Columna("inicial", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("entradas", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("salidas", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			this.attrs.put("sortOrder", " order by registro desc");
			if(this.attrs.get("almacen")!= null)
			  this.attrs.put("idAlmacen", ((Entity)this.attrs.get("almacen")).getKey());			
			Entity vigente= (Entity)DaoFactory.getInstance().toEntity("TcManticInventariosDto", "inventario", this.attrs);
			UIBackingUtilities.toFormatEntity(vigente, columns);
      this.attrs.put("vigente", vigente);
      this.lazyModel = new FormatCustomLazy("VistaInventariosDto", this.attrs, columns);
      UIBackingUtilities.resetDataTable();
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally
  } // doLoad	
	
	private void loadAlmacenes() {
		List<UISelectEntity> almacenes= null;
		Map<String, Object> params    = null;
		List<Columna> columns         = null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			columns= new ArrayList<>();
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));							
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));							
			columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));							
			almacenes= UIEntity.build("VistaAlmacenesDto", "almacenesEmpresa", params, columns, Constantes.SQL_TODOS_REGISTROS);      
			this.attrs.put("almacenes", almacenes);
			if(almacenes!= null && !almacenes.isEmpty())
			  this.attrs.put("almacen", new Entity(UIBackingUtilities.toFirstKeySelectEntity(almacenes).getKey()));			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadAlmacenes
	
	private void updateArticulo(UISelectEntity articulo) throws Exception {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empaque", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("unidadMedida", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			this.attrs.put("idArticulo", null);
			if(articulo.size()> 1) {
				this.image= LoadImages.getImage(articulo.toString("idArticulo"));
  			this.attrs.put("idArticulo", articulo.toLong("idArticulo"));
				Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaKardexDto", "row", this.attrs);
				if(solicitado!= null) {
					UIBackingUtilities.toFormatEntity(solicitado, columns);
					this.attrs.put("articulo", solicitado);
					this.attrs.put("precio", solicitado.toDouble("precio"));
					Value ultimo= (Value)DaoFactory.getInstance().toField("TcManticArticulosBitacoraDto", "ultimo", this.attrs, "registro");
					if(ultimo!= null)
					  this.attrs.put("ultimo", Global.format(EFormatoDinamicos.FECHA_HORA, ultimo.toTimestamp()));
					RequestContext.getCurrentInstance().execute("jsKardex.callback("+ solicitado +");");
				} // if	
			} // if
			else {
				this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
				this.attrs.put("articulo", null);
			} // if	
			this.doLoad();
		} // try
		finally {
      Methods.clean(columns);
    } // finally
	}
	
	public void doUpdateArticulo(String codigo) {
    try {
  		List<UISelectEntity> articulos= this.doCompleteArticulo(codigo);
			UISelectEntity articulo= UIBackingUtilities.toFirstKeySelectEntity(articulos);
			this.updateArticulo(articulo);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	

	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		List<UISelectEntity> articulos= null;
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
	
	public void doArticulos() {
		List<UISelectEntity> articulos= null;
    Map<String, Object> params   = null;
		List<Columna> columns        = null;
    try {
			columns= new ArrayList<>();
			if(this.attrs.get("busqueda")!= null && this.attrs.get("busqueda").toString().length()> 2) {
				params = new HashMap<>();      
				params.put("nombre", this.attrs.get("busqueda"));
				params.put("codigo", this.attrs.get("busqueda"));
				params.put("idEmpresa", this.attrs.get("idEmpresa"));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));							
				articulos = UIEntity.build("VistaArticulosDto", "inventario", params, columns, Constantes.SQL_TODOS_REGISTROS);      
				this.attrs.put("articulos", articulos);      
				this.attrs.put("resultados", articulos.size());      
			} // if
			else 
				JsfBase.addMessage("Cliente", "Favor de teclear por lo menos 3 caracteres.", ETipoMensaje.ALERTA);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	}
	
	public void doAceptar() {
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(this.inventarios);
			if(transaccion.ejecutar(EAccion.AGREGAR))
				JsfBase.addMessage("Inventarios", "Se agrego de forma correcta el inventario", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Inventarios", "Ocurrió un error al agregar el inventario", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	}
	
	public void doCleanArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		try {
			columns= new ArrayList<>();
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			params.put("codigo", "WXYZ");
      this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porLikeNombre", params, columns));
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
	
	public void doChangeBuscado() {
		try {
			if(this.attrs.get("encontrado")== null) {
				FormatCustomLazy list= (FormatCustomLazy)this.attrs.get("lazyModel");
				if(list!= null) {
					List<Entity> items= (List<Entity>)list.getWrappedData();
					if(items.size()> 0)
						this.updateArticulo(new UISelectEntity(items.get(0)));
				} // if
			} // else
			else
				this.updateArticulo((UISelectEntity)this.attrs.get("encontrado"));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
  public void doUpdateDialogArticulos(String codigo) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("original", EFormatoDinamicos.MONEDA_CON_DECIMALES));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			if(!Cadena.isVacio(codigo)) {
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
			} // if	
			else
				codigo= "WXYZ";
			params.put("codigo", codigo.toUpperCase());
			if(buscaPorCodigo)
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porCodigo", params, columns));
			else
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porLikeNombre", params, columns));
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

  public void doRowDblselect(SelectEvent event) {
		try {
			this.attrs.put("encontrado", new UISelectEntity((Entity)event.getObject()));
			this.updateArticulo((UISelectEntity)this.attrs.get("encontrado"));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
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
			this.updateArticulo(articulo);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 
	
}
package mx.org.kaana.mantic.catalogos.inventarios.backing;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import mx.org.kaana.mantic.catalogos.inventarios.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;


@Named(value = "manticCatalogosInventariosConteos")
@ViewScoped
public class Conteos extends IBaseFilter implements Serializable {
	
	private static final long serialVersionUID = 5570593377763068163L;	
	
	private StreamedContent image;
	private TcManticAlmacenesArticulosDto articulo;
		
	public StreamedContent getImage() {
		return image;
	}

	public TcManticAlmacenesArticulosDto getArticulo() {
		return articulo;
	}

	public void setArticulo(TcManticAlmacenesArticulosDto articulo) {
		this.articulo=articulo;
	}
	
	public boolean getHabilitar() {
		return this.attrs.get("existe")!= null || this.attrs.get("vigente")== null;
	}
	
	@PostConstruct
  @Override
  protected void init() {
    try {			
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());	
    	this.attrs.put("buscaPorCodigo", false);
			this.articulo= new TcManticAlmacenesArticulosDto();
			this.loadAlmacenes();
			if(JsfBase.getFlashAttribute("codigo")!= null) {
				this.doCompleteArticulo((String)JsfBase.getFlashAttribute("codigo"));
				List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
				if(articulos!= null && !articulos.isEmpty()) {
				  this.attrs.put("custom", articulos.get(0));
				  this.doFindArticulo();
				} // if	
			} // if	
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
			TcManticInventariosDto vigente= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", this.attrs);
			if(vigente== null || !vigente.getEntradas().equals(0D) || !vigente.getSalidas().equals(0D)) {
				vigente= new TcManticInventariosDto(-1L);
				vigente.setIdAlmacen((Long)this.attrs.get("idAlmacen"));
				vigente.setIdArticulo((Long)this.attrs.get("idArticulo"));
				vigente.setEjercicio(new Long(Calendar.getInstance().get(Calendar.YEAR)));
				vigente.setInicial(0D);
				vigente.setEntradas(0D);
				vigente.setSalidas(0D);
				vigente.setStock(0D);
  			vigente.setIdUsuario(JsfBase.getIdUsuario());
				vigente.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			} // if
      this.attrs.put("vigente", vigente);
      this.lazyModel = new FormatCustomLazy("VistaInventariosDto", this.attrs, columns);
      UIBackingUtilities.resetDataTable();
			this.articulo= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().toEntity(TcManticAlmacenesArticulosDto.class, "TcManticAlmacenesArticulosDto", "almacenArticulo", this.attrs);
			if(this.articulo== null) {
				TcManticArticulosDto item= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.attrs.get("idArticulo")== null? -1L: (Long)this.attrs.get("idArticulo"));
				if(item!= null)
					this.articulo= new TcManticAlmacenesArticulosDto(
						item.getMinimo(), // Long minimo, 
						-1L, // Long idAlmacenArticulo, 
						JsfBase.getIdUsuario(), // Long idUsuario, 
						(Long)this.attrs.get("idAlmacen"), // Long idAlmacen,
						item.getMaximo(), // Long maximo, 
						-1L, // Long idAlmacenUbicacion, 
						(Long)this.attrs.get("idArticulo"), // Long idArticulo, 
						0D // Double stock
						);
				else
    			this.articulo= new TcManticAlmacenesArticulosDto();
			} // if	
			else
				if(!vigente.isValid())
					vigente.setInicial(articulo.getStock());
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    } // finally
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
				this.image= LoadImages.getImage(articulo.toLong("idArticulo"));
  			this.attrs.put("idArticulo", articulo.toLong("idArticulo"));
				Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaKardexDto", "row", this.attrs);
				if(solicitado!= null) {
					UIBackingUtilities.toFormatEntity(solicitado, columns);
					this.attrs.put("articulo", solicitado);
					Value ultimo= (Value)DaoFactory.getInstance().toField("TcManticArticulosBitacoraDto", "ultimo", this.attrs, "registro");
					if(ultimo!= null)
					  this.attrs.put("ultimo", Global.format(EFormatoDinamicos.FECHA_HORA, ultimo.toTimestamp()));
					UIBackingUtilities.execute("jsKardex.callback("+ solicitado +");");
				} // if	
			} // if
			else {
				this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
				this.attrs.put("articulo", null);
			} // else	
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
				buscaPorCodigo= search.startsWith(".");
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
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
			TcManticInventariosDto vigente= (TcManticInventariosDto)this.attrs.get("vigente");
			vigente.setIdUsuario(JsfBase.getIdUsuario());
			vigente.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			transaccion= new Transaccion(vigente, this.articulo);
			if(transaccion.ejecutar(vigente.isValid()? EAccion.MODIFICAR: EAccion.AGREGAR)) {
				JsfBase.addMessage("Inventarios", "Se agregó/modificó de forma correcta el inventario", ETipoMensaje.INFORMACION);
				this.doLoad();
			} // if	
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
	    UISelectEntity item= (UISelectEntity)this.attrs.get("custom");
			if(item== null)
			  item= new UISelectEntity(new Entity(-1L));
			else
				if(articulos.indexOf(item)>= 0) 
					item= articulos.get(articulos.indexOf(item));
			  else
			    item= articulos.get(0);
			this.updateArticulo(item);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 
	
}
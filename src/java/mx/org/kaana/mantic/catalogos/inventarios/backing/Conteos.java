package mx.org.kaana.mantic.catalogos.inventarios.backing;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Periodo;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.inventarios.beans.ArticuloAlmacen;
import mx.org.kaana.mantic.catalogos.inventarios.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;


@Named(value = "manticCatalogosInventariosConteos")
@ViewScoped
public class Conteos extends IBaseFilter implements Serializable {
	
	private static final long serialVersionUID = 5570593377763068163L;	
	
	private StreamedContent image;
	private ArticuloAlmacen articulo;
  protected FormatLazyModel lazyConteos; 		
  
	public StreamedContent getImage() {
		return image;
	}

	public ArticuloAlmacen getArticulo() {
		return articulo;
	}

	public void setArticulo(ArticuloAlmacen articulo) {
		this.articulo=articulo;
	}
	
	public boolean getHabilitar() {
		return this.attrs.get("existe")!= null || this.attrs.get("vigente")== null;
	}

  public FormatLazyModel getLazyConteos() {
    return lazyConteos;
  }
	
	@PostConstruct
  @Override
  protected void init() {
    try {			
      if(JsfBase.getBrowser().isMobile()) 
        UIBackingUtilities.execute("janal.isPostBack('movil');");
			this.attrs.put("xcodigo", JsfBase.getFlashAttribute("xcodigo"));	
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());	
    	this.attrs.put("buscaPorCodigo", false);
    	this.attrs.put("ultimo", "");
    	this.attrs.put("idArticuloTipo", 1L);
			this.articulo= new ArticuloAlmacen();
			this.toLoadAlmacenes();
			if(this.attrs.get("xcodigo")!= null) {
				this.doCompleteArticulo((String)this.attrs.get("xcodigo"));
				List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
				if(articulos!= null && !articulos.isEmpty()) {
				  this.attrs.put("custom", articulos.get(0));
				  this.doFindArticulo();
				} // if	
			} // if	
			this.toLoadUbicaciones();
      this.toLoadConteos();
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
			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(almacenes!= null && !almacenes.isEmpty()) {
        int index= almacenes.indexOf((UISelectEntity)this.attrs.get("almacen"));
        if(index>= 0)
          this.attrs.put("almacen", almacenes.get(index));
      } // if  
			if(this.attrs.get("almacen")!= null)
			  this.attrs.put("idAlmacen", ((UISelectEntity)this.attrs.get("almacen")).getKey());			
      params = new HashMap<>();      
			columns= new ArrayList<>();
      columns.add(new Columna("inicial", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("entradas", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("salidas", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			params.put("sortOrder", " order by registro desc");
      params.put("idAlmacen", this.attrs.get("idAlmacen"));      
      params.put("idArticulo", this.attrs.get("idArticulo"));      
			TcManticInventariosDto vigente= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if((vigente== null || !vigente.getEntradas().equals(0D) || !vigente.getSalidas().equals(0D)) && !Cadena.isVacio(this.attrs.get("idArticulo"))) {
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
      this.lazyModel = new FormatCustomLazy("VistaInventariosDto", params, columns);
      UIBackingUtilities.resetDataTable();
			this.toLoadAlmacenArticulo();
			this.toSearchUltimo();
			this.toLoadUbicaciones();
			this.toLoadMovimientos();
      this.toLoadConteos();
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
	
	private void toLoadAlmacenArticulo() throws Exception {
		List<UISelectEntity> ubicaciones= (List<UISelectEntity>)this.attrs.get("ubicaciones");
    TcManticInventariosDto vigente  = (TcManticInventariosDto)this.attrs.get("vigente");
    Map<String, Object> params      = null;
    Long idArticulo                 = this.attrs.get("idArticulo")== null? -1L: (Long)this.attrs.get("idArticulo");
    try {      
      params = new HashMap<>();      
      params.put("idArticulo", idArticulo);      
      params.put("idAlmacen", this.attrs.get("idAlmacen"));      
      this.articulo= (ArticuloAlmacen)DaoFactory.getInstance().toEntity(ArticuloAlmacen.class, "TcManticAlmacenesArticulosDto", "almacenArticulo", params);
      if(this.articulo== null) {
        TcManticArticulosDto item= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, idArticulo);
        if(item!= null)
          this.articulo= new ArticuloAlmacen(
            item.getMinimo(), // Long minimo, 
            -1L, // Long idAlmacenArticulo, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            (Long)this.attrs.get("idAlmacen"), // Long idAlmacen,
            item.getMaximo(), // Long maximo, 
            -1L, // Long idAlmacenUbicacion, 
            (Long)this.attrs.get("idArticulo"), // Long idArticulo, 
            0D, // Double stock
            item.getIdVerificado() // Long idVerificado
          );
        else
          this.articulo= new ArticuloAlmacen();
        if(ubicaciones!= null && !ubicaciones.isEmpty()) 
          this.attrs.put("idAlmacenUbicacion", ubicaciones.get(0));
      } // if	
      else {
        if(ubicaciones!= null && !ubicaciones.isEmpty()) {
          int index= ubicaciones.indexOf(new UISelectEntity(this.articulo.getIdAlmacenUbicacion()));
          if(index>= 0)
            this.attrs.put("idAlmacenUbicacion", ubicaciones.get(index));
          else
            this.attrs.put("idAlmacenUbicacion", ubicaciones.get(0));
        } // if
        if(!vigente.isValid())
          vigente.setInicial(this.articulo.getStock());
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
	
	private void toLoadAlmacenes() throws Exception {
		List<UISelectEntity> almacenes= null;
		Map<String, Object> params    = null;
		List<Columna> columns         = null;
		try {
			params= new HashMap<>();
      if(JsfBase.isAdmin())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
      else
			  params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			columns= new ArrayList<>();
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));							
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));							
			columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));							
			almacenes= UIEntity.build("VistaAlmacenesDto", "almacenesEmpresa", params, columns, Constantes.SQL_TODOS_REGISTROS);      
			this.attrs.put("almacenes", almacenes);
			if(almacenes!= null && !almacenes.isEmpty())
			  this.attrs.put("almacen", UIBackingUtilities.toFirstKeySelectEntity(almacenes));			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadAlmacenes

  public void toLoadUbicaciones() {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("almacen", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("piso", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cuarto", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("anaquel", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("charola", EFormatoDinamicos.MAYUSCULAS));
			if(this.attrs.get("almacen")!= null)
			  this.attrs.put("idAlmacen", ((Entity)this.attrs.get("almacen")).getKey());			
			List<UISelectEntity> ubicaciones= UIEntity.seleccione("VistaKardexDto", "ubicaciones", this.attrs, columns, "piso");
      // SI NO HAY UBICACIONES INSERTAR UNA UBICACION POR DEFECTO
      if(ubicaciones== null || ubicaciones.isEmpty()) {
        TcManticAlmacenesUbicacionesDto general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), ((Entity)this.attrs.get("almacen")).getKey(), -1L);
				DaoFactory.getInstance().insert(general);
        ubicaciones= UIEntity.seleccione("VistaKardexDto", "ubicaciones", this.attrs, columns, "piso");
      } // if
			if(ubicaciones!= null && !ubicaciones.isEmpty() && this.articulo!= null && this.articulo.getIdAlmacenUbicacion()!= null) {
				int index= ubicaciones.indexOf(new UISelectEntity(this.articulo.getIdAlmacenUbicacion()));
				if(index>= 0)
					this.attrs.put("idAlmacenUbicacion", ubicaciones.get(index));
			} // if
			this.attrs.put("ubicaciones", ubicaciones);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally		 
	}	

	private void updateArticulo(Entity articulo) throws Exception {
		List<Columna> columns= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empaque", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("unidadMedida", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			this.attrs.put("ultimo", "");
			this.attrs.put("idArticulo", null);
			if(articulo.size()> 1) {
				this.image= LoadImages.getImage(articulo.toLong("idArticulo"));
  			this.attrs.put("idArticulo", articulo.toLong("idArticulo"));
				Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaKardexDto", "row", this.attrs);
				if(solicitado!= null) {
				  UIBackingUtilities.toFormatEntity(solicitado, columns);
					this.attrs.put("articulo", solicitado);
					this.toSearchUltimo();
					UIBackingUtilities.execute("jsKardex.callback("+ solicitado +");");
				} // if	
			} // if
			else {
				this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
				this.attrs.put("articulo", null);
			} // else	
			this.doLoad();
			this.toLoadMovimientos();
		} // try
		finally {
      Methods.clean(columns);
    } // finally
	}
	
	private void toSearchUltimo() {
		Map<String, Object> params= null;
		List<Columna> columns     = null;
		try {
			params = new HashMap<>();
			columns= new ArrayList<>();
			this.attrs.put("ultimo", "");
	  	if(this.attrs.get("idArticulo")!= null) {
				Periodo periodo= new Periodo();
				periodo.addMeses(-240);
				params.put("idArticulo", this.attrs.get("idArticulo"));
				params.put("idAlmacen", this.attrs.get("idAlmacen"));
				params.put("registro", periodo.toString());
				columns.add(new Columna("concepto", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
				Entity ultimo= (Entity)DaoFactory.getInstance().toEntity("VistaKardexDto", "conteo", params);
				UIBackingUtilities.toFormatEntity(ultimo, columns);
				if(ultimo!= null)
					this.attrs.put("ultimo", ultimo.toString("concepto")+ " "+ ultimo.toString("registro"));
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
      Methods.clean(columns);
		} // finally
	}
	public void doUpdateArticulo(String codigo) {
    try {
  		List<UISelectEntity> articulos= this.doCompleteArticulo(codigo);
			UISelectEntity item= UIBackingUtilities.toFirstKeySelectEntity(articulos);
			this.updateArticulo(item);
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
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idArticuloTipo", this.attrs.get("idArticuloTipo"));
  		params.put("idProveedor", -1L);
			String search= (String)this.attrs.get("codigo"); 
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
        articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigoCompleto", params, columns, 40L);
			else
        articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombreCompleto", params, columns, 40L);
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
	
	public void doConteo() {
		Transaccion transaccion= null;
		try {
			TcManticInventariosDto vigente= (TcManticInventariosDto)this.attrs.get("vigente");
			vigente.setIdUsuario(JsfBase.getIdUsuario());
			vigente.setIdVerificado(this.articulo.getIdVerificado());
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
	
	public void doAceptar() {
		Transaccion transaccion= null;
		try {
			TcManticInventariosDto vigente= (TcManticInventariosDto)this.attrs.get("vigente");
			vigente.setIdUsuario(JsfBase.getIdUsuario());
      vigente.setIdVerificado(this.articulo.getIdVerificado());
			vigente.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			transaccion= new Transaccion(vigente, this.articulo);
			if(transaccion.ejecutar(EAccion.PROCESAR)) {
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
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
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
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
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

  public String doCancelar() {   
		JsfBase.setFlashAttribute("xcodigo", this.attrs.get("xcodigo"));	
    return this.attrs.get("retorno")!= null? ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR): null;
  } // doCancelar

  public void doChangeUbicacion() {
		if(this.articulo!= null && this.attrs.get("idAlmacenUbicacion")!= null && ((UISelectEntity)this.attrs.get("idAlmacenUbicacion")).getKey()> 0L) 
			this.articulo.setIdAlmacenUbicacion(((UISelectEntity)this.attrs.get("idAlmacenUbicacion")).getKey());
	} 

	public void doTabChange(TabChangeEvent event) {
		// if(event.getTab().getTitle().equals("Movimientos (60 días)") && this.attrs.get("idArticulo")!= null) 
		//	this.toLoadMovimientos();
	}
	
	private void toLoadMovimientos() {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("almacen", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombreEmpresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("calculo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			Periodo periodo= new Periodo();
			periodo.addMeses(-2);
			this.attrs.put("registro", periodo.toString());
			this.attrs.put("periodo", Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, periodo.toString()));
      this.attrs.put("movimientos", (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "movimientos", this.attrs, columns));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally
	}	

	private void toLoadItemAlmacen() {
		List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
		if(this.attrs.get("almacen")== null) {
			this.attrs.put("almacen", almacenes.get(0));
		} // if
		else {
			int index= almacenes.indexOf((UISelectEntity)this.attrs.get("almacen"));
			if(index>= 0) 
  			this.attrs.put("almacen", almacenes.get(index));
			else
				this.attrs.put("almacen", almacenes.get(0));
		} // if
	}
		
	private Long toFindIdKey(String consecutivo, String proceso, String idXml) {
		Long regresar             = -1L;
		Map<String, Object> params= null;
		try {
			this.toLoadItemAlmacen();
			params=new HashMap<>();
			params.put("consecutivo", consecutivo);
			params.put("idEmpresa", ((UISelectEntity)this.attrs.get("almacen")).toLong("idEmpresa"));
			Entity entity= (Entity)DaoFactory.getInstance().toEntity(proceso, idXml, params);
			if(entity!= null && !entity.isEmpty())
				regresar= entity.getKey();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	public String doMoveSection() {
		UISelectEntity consecutivo    = (UISelectEntity)this.attrs.get("consecutivo");
		List<Columna> columns         = null;
    Map<String, Object> params    = new HashMap<>();
		List<UISelectEntity> documento= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("impuestos", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("precio", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_HORA));
			switch(consecutivo.toLong("idTipoMovimiento").intValue()) {
				case 1: // ENTRADAS
					Long idNotaEntrada= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticNotasEntradasDto", "consecutivo");
      		params.put("idNotaEntrada", idNotaEntrada);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "notaEntrada", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "de la nota de entrada");
					break;
				case 2: // VENTAS
					Long idVenta= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticVentasDto", "ticket");
      		params.put("idVenta", idVenta);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "venta", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "de la venta");
					break;
				case 3: // DEVOLUCIONES
					Long idDevolucion= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticDevolucionesDto", "consecutivo");
      		params.put("idDevolucion", idDevolucion);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "devolucion", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "de la devolución");
					break;
				case 4: // TRASPASOS
          columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
          columns.add(new Columna("origen", EFormatoDinamicos.MAYUSCULAS));
          columns.add(new Columna("destino", EFormatoDinamicos.MAYUSCULAS));
					Long idTransferencia= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticTransferenciasDto", "consecutivo");
      		params.put("idTransferencia", idTransferencia);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "traspaso", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "del traspaso");
					break;
				case 5: // GARANTIAS
					Long idGarantia= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticGarantiasDto", "consecutivo");
      		params.put("idGarantia", idGarantia);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "garantia", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "de la garantía");
					break;
				case 6: // CONTEOS
          columns.clear();
          columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
          columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
          columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
          columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_HORA));
          columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      		params.put("idMovimiento", consecutivo.getKey());
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "ver", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "del conteo");
					break;
			} // switch
			if(documento!= null && !documento.isEmpty()) {
				documento.get(0).put("articulos", new Value("articulos", documento.size()));
        this.attrs.put("documento", documento.get(0));
			} // if	
      this.attrs.put("idTipoDocumento", consecutivo.toLong("idTipoMovimiento").intValue());
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
		return null;
	}
 
  public void toLoadConteos() {
		List<Columna> columns= null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();      
			columns= new ArrayList<>();
      columns.add(new Columna("inicial", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("entradas", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("salidas", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			params.put("sortOrder", " order by tc_mantic_inventarios.registro desc");
      params.put("idAlmacen", this.attrs.get("idAlmacen")== null? "-1": this.attrs.get("idAlmacen"));      
      params.put("idArticulo", this.attrs.get("idArticulo"));      
      this.lazyConteos = new FormatCustomLazy("VistaInventariosDto", "conteos", params, columns);
      UIBackingUtilities.resetDataTable("conteos");
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
  } // toLoadConteos	
 
	public String toColor(Entity row) {
		return Objects.equals(row.toLong("idVerificado"), 1L)? "janal-tr-orange": "";
	} 
 
  public void doRowConteoActivo(Entity row) {
    try {
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(almacenes!= null && !almacenes.isEmpty()) {
        int index= almacenes.indexOf(new UISelectEntity(row.toLong("idAlmacen")));
        if(index>= 0)
          this.attrs.put("almacen", almacenes.get(index));
      } // if  
      this.updateArticulo(row);
      TabView view= (TabView)JsfUtilities.findComponent("contenedorGrupos");
      if(view!= null) 
        view.setActiveIndex(0);
      this.attrs.put("idArticulo", row.toLong("idArticulo"));
      this.toLoadMovimientos();
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
  }
  
}
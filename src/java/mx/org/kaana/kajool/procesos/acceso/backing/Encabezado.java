package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.File;
import java.io.FileInputStream;
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
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.acceso.beans.Faltante;
import mx.org.kaana.kajool.procesos.acceso.beans.UsuarioMenu;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@Named(value = "kajoolEncabezado")
@ViewScoped
public class Encabezado extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 5323749709626263793L;
  private static final Log LOG = LogFactory.getLog(Encabezado.class);
	
  private List<Entity> lazyFaltantes;
  private FormatLazyModel lazyListaPrecios;
  private List<Entity> lazyCatalogoArticulos;
	private Faltante faltante;
	private StreamedContent image;

	public List<Entity> getLazyFaltantes() {
		return lazyFaltantes;
	}

	public FormatLazyModel getLazyListaPrecios() {
		return lazyListaPrecios;
	}

	public List<Entity> getLazyCatalogoArticulos() {
		return lazyCatalogoArticulos;
	}

	public Faltante getFaltante() {
		return faltante;
	}

	public void setFaltante(Faltante faltante) {
		this.faltante=faltante;
	}
	
	public StreamedContent getImage() {
		return image;
	}
	
	@Override
	@PostConstruct
	protected void init() {
		this.attrs.put("codigo", "");
		this.attrs.put("buscarPor", "");
		this.attrs.put("buscaPorCodigo", false);
	  this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
		this.faltante= new Faltante(JsfBase.getIdUsuario(), -1L, "", 1D, 1L, -1L, JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
	  this.toLoadCatalog();	
	}
	
	@Override
	public void doLoad() {
    List<Columna> columns= null;
    Map<String, Object> params= new HashMap<>();
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("menudeo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("medioMayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("mayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("limiteMedioMayoreo", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("limiteMayoreo", EFormatoDinamicos.NUMERO_CON_DECIMALES));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", this.attrs.get("sucursales"));
  		params.put("idProveedor", this.attrs.get("proveedor"));
  		params.put("codigo", this.attrs.get("codigo"));
  		params.put("idArticulo", this.attrs.get("idArticulo"));
      this.lazyModel = new FormatCustomLazy("VistaOrdenesComprasDto", (String)this.attrs.get("idXml"), params, columns);
      UIBackingUtilities.resetDataTable("verificadorTabla");
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally			
	}

	public void doChange() {
		String codigo= new String((String)this.attrs.get("buscarPor"));
		if(codigo== null || codigo.equals(".*.")) 
			this.lazyModel= null;
		else {
  		codigo= codigo.replaceAll(Constantes.CLEAN_SQL, "").trim();
			boolean buscaPorCodigo= !codigo.startsWith(".");
			if(!buscaPorCodigo)
				codigo= codigo.trim().substring(1);
			codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			this.attrs.put("codigo", codigo);
			this.attrs.put("idXml", "porNombre");
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
				this.attrs.put("idXml", "porCodigo");
			this.doLoad();
		} // else
	}

  public void doRowDblSelectEvent(SelectEvent event) {
		Entity entity= (Entity)event.getObject();
		LOG.info("doRowSelectEvent: "+ entity.getKey());
		List<Columna> columns     = null;
		Map<String, Object> params= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("ubicacion", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
			params=new HashMap<>();
			params.put("idArticulo", entity.toLong("idArticulo"));
			this.attrs.put("almacenes", UIEntity.build("VistaKardexDto", "localizado", params, columns));
			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty())
				this.attrs.put("idAlmacen", almacenes.get(0));
			this.image= LoadImages.getImage(entity.toLong("idArticulo"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
			Methods.clean(columns);
		} // finally
  }	

  public void doFaltanteArticulo() {
		try {
      TcManticFaltantesDto existe= (TcManticFaltantesDto)DaoFactory.getInstance().findFirst(TcManticFaltantesDto.class, "existe", this.faltante.toMap());
			if(existe== null) {
				if(DaoFactory.getInstance().insert(this.faltante)> 0L) {
	  			JsfBase.addMessage("Agregado:", "El articulo fue agregado a la relación de faltantes. !", ETipoMensaje.INFORMACION);
  				this.faltante= new Faltante(JsfBase.getIdUsuario(), -1L, "", 1D, 1L, -1L, this.faltante.getIdEmpresa());
				} // if	
		  }	// if
			else {
				existe.setCantidad(existe.getCantidad()+ this.faltante.getCantidad());
				if(DaoFactory.getInstance().update(existe)> 0L) {
	  			JsfBase.addMessage("Agregado:", "El articulo fue actualizado en la relación de faltantes. !", ETipoMensaje.INFORMACION);
  				this.faltante= new Faltante(JsfBase.getIdUsuario(), -1L, "", 1D, 1L, -1L, this.faltante.getIdEmpresa());
				} // if	
			} // else	
			UIBackingUtilities.update("@(.faltantes)");
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
  public void doReplaceFaltante() {
		try {
    	List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
	    UISelectEntity articulo= (UISelectEntity)this.attrs.get("articulo");
			if(articulo== null)
			  articulo= new UISelectEntity(new Entity(-1L));
			else
				if(articulos.indexOf(articulo)>= 0) 
					articulo= articulos.get(articulos.indexOf(articulo));
			  else
			    articulo= articulos.get(0);
			if(articulo.size()> 0) {
			  this.faltante.setIdArticulo(articulo.toLong("idArticulo"));
			  this.faltante.setCodigo(articulo.toString("propio"));
			  this.faltante.setNombre(articulo.toString("nombre"));
			} // if	
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
	public void doStartLoadFaltantes() {
		if(JsfBase.getAutentifica()!= null && JsfBase.getAutentifica().getEmpresa()!= null && JsfBase.getAutentifica().getEmpresa().getIdEmpresa()!= null && JsfBase.getAutentifica().getEmpresa().getIdEmpresa()> 0L) {
			this.faltante.setIdUsuario(JsfBase.getIdUsuario());
			this.faltante.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
		} // if
		this.doLoadFaltantes();
	}
	
	public void doLoadFaltantes() {
    List<Columna> columns= null;
    try {
			Long idSucursal= this.faltante.getIdEmpresa()== null? -1L: this.faltante.getIdEmpresa();
			this.attrs.put("idSucursal", idSucursal);
      columns = new ArrayList<>();
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			if(this.lazyFaltantes!= null)
				Methods.clean(this.lazyFaltantes);
      this.lazyFaltantes = (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaOrdenesComprasDto", "registrados", this.attrs, 50L);
      UIBackingUtilities.toFormatEntitySet(this.lazyFaltantes, columns);
      UIBackingUtilities.resetDataTable("faltantesTabla");
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
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
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= new String((String)this.attrs.get("codigoFaltantes")); 
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
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
			else
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
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
		this.attrs.put("codigoFaltantes", query);
    this.doUpdateArticulos();		
		return (List<UISelectEntity>)this.attrs.get("articulos");
	}	

  public void doEliminarFaltante() {
		try {
			Entity eliminado= (Entity)this.attrs.get("eliminado");
			if(DaoFactory.getInstance().delete(TcManticFaltantesDto.class, eliminado.getKey())> 0L)
				JsfBase.addMessage("Eliminado:", "El articulo fue eliminado de la relación de faltantes. !", ETipoMensaje.INFORMACION);
			UIBackingUtilities.execute("$('#codigosFaltantes').focus();");
			UIBackingUtilities.update("@(.faltantes)");
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	
	
	public String doCleanChars(String text) {
		return Cadena.reemplazarCaracter(text, (char)39, ' ');
	}
	
	public String doEjecutar() {
		String regresar= null;
		String opcion  = (String)this.attrs.get("opcion");
		LOG.info("Ejecutar: "+ opcion);
		if(!Cadena.isVacio(opcion))
			for (UsuarioMenu item: JsfBase.getAutentifica().getMenu()) {
				if(opcion.equals(item.getCodigo()) && !Cadena.isVacio(item.getRuta())) {
					regresar= item.getRuta();
					break;
				} // if	
			} // for
		if(regresar== null) {
			UIBackingUtilities.execute("janal.alert('La opci\\u00F3n ["+ opcion+ "] no existe, verifiquelo de favor !')");
		} // if
		return regresar== null? regresar: regresar.concat(Constantes.REDIRECIONAR);
	}
	
	public void doChangeListaPrecios() {
		String codigo= new String((String)this.attrs.get("buscarPor"));
		if(codigo== null || codigo.equals(".*.")) 
			this.lazyListaPrecios= null;
		else {
  		codigo= codigo.replaceAll(Constantes.CLEAN_SQL, "").trim();
			boolean buscaPorCodigo= codigo.startsWith(".");
			if(buscaPorCodigo)
				codigo= codigo.trim().substring(1);
			codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			this.attrs.put("codigo", codigo);
			this.attrs.put("idXml", "porNombre");
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
				this.attrs.put("idXml", "porCodigo");
			this.doLoadListaPrecios();
		} // else
	}

	public void doLoadListaPrecios() {
    List<Columna> columns= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("auxiliar", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("precio", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      this.lazyListaPrecios= new FormatCustomLazy("VistaListasArchivosDto", (String)this.attrs.get("idXml"), this.attrs, columns);
      UIBackingUtilities.resetDataTable("listaPreciosTabla");
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally			
	}
	
	public void doChangeCatalogoArticulos() {
		String codigo= new String((String)this.attrs.get("buscarPor"));
		if(codigo== null || codigo.equals(".*.")) 
			this.lazyCatalogoArticulos= null;
		else {
			codigo= codigo.replaceAll(Constantes.CLEAN_SQL, "").trim().toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			this.attrs.put("codigo", codigo);
			this.attrs.put("idXml", "porCatalogo");
			this.doLoadCatalogoArticulos();
		} // else
	}

	public void doLoadCatalogoArticulos() {
    List<Columna> columns= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
  		this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.lazyCatalogoArticulos= DaoFactory.getInstance().toEntitySet("VistaListasArchivosDto", (String)this.attrs.get("idXml"), this.attrs);
			UIBackingUtilities.toFormatEntitySet(this.lazyCatalogoArticulos, columns);
      UIBackingUtilities.resetDataTable("catalogoArticulosTabla");
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally			
	}

  public void doViewPdfDocument(Entity item) { 
		this.toCopyDocument(item.toString("alias"), item.toString("nombre"));
	}
  
  private void toCopyDocument(String alias, String name) {
		try {
  	  this.attrs.put("temporal", JsfBase.getContext().concat("/").concat(Constantes.RUTA_TEMPORALES).concat(name).concat("?pfdrid_c=true"));
  		File source= new File(JsfBase.getRealPath().concat(Constantes.RUTA_TEMPORALES).concat(name));
			if(!source.exists()) {
	  	  FileInputStream input= new FileInputStream(new File(alias));
        Archivo.toWriteFile(source, input);		
			} // if	
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
	}	// toCopyDocument

  public void doCerrar() {
		try {
//			String name= (String)this.attrs.get("temporal");
//			name= name.substring(0, name.lastIndexOf("?"));
//			File file= new File(JsfBase.getRealPath().concat(name));
//			file.delete();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
  } // doCerrar

  public String doMovePageStart() {
		String regresar= null;
		// 0201000000->Bienvenido
		// 0204000000->Tablero de control
		for (UsuarioMenu item: JsfBase.getAutentifica().getMenu()) {
			if("0201000000".equals(item.getClave()) || "0204000000".equals(item.getClave())) {
				regresar= item.getRuta().concat(Constantes.REDIRECIONAR);
				break;
			} // if	
		} // for
		return regresar;
	}

  private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("empresas", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

  public boolean isAdmin() {
    boolean regresar = false;
    try {
      regresar = JsfBase.isAdmin();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch		
    return regresar;
	}
	
	public void doCleanFlash() {
	  JsfBase.cleanFlashParams();
	}

}

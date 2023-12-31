package mx.org.kaana.mantic.clientes.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import org.primefaces.model.StreamedContent;

@Named(value = "manticClientesNormal")
@ViewScoped
public class Normal extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  private Entity cliente;

  public String getHoy() {
    return Fecha.getHoyCorreo().concat("; ").concat(Fecha.getHoraExtendida());
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
    	this.attrs.put("buscaPorCodigo", false);
      this.attrs.put("codigo", "");
      //this.attrs.put("nombre", "");
      this.attrs.put("idTipoArticulo", 1L);
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());      
			this.attrs.put("isGerente", JsfBase.isAdminEncuestaOrAdmin());
			this.attrs.put("idUsuario", JsfBase.getIdUsuario());
      this.cliente= (Entity)DaoFactory.getInstance().toEntity("VistaClientesRepresentantesDto", "cliente", this.attrs);
      if(this.cliente== null) {
        this.cliente= new Entity(JsfBase.getIdUsuario());
        this.cliente.put("especial", new Value("especial", -1D));
        this.cliente.put("idUsuario", new Value("idUsuario", -1L));
      } // if  
			this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= null;
    try {
  		params= this.toPrepare();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("menudeo", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("medioMayoreo", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("mayoreo", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("especial", EFormatoDinamicos.MILES_CON_DECIMALES));
      params.put("sortOrder", "order by tc_mantic_articulos.nombre, tc_mantic_articulos.actualizado");
      this.lazyModel = new FormatCustomLazy("VistaArticulosDto", params, columns);
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
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("empresas", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			this.attrs.put("idEmpresa", new UISelectEntity("-1"));      
      columns.clear();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("nombreCompleto", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("clientes", (List<UISelectEntity>) UIEntity.build("VistaClientesRepresentantesDto", "todos", params, columns));
			this.attrs.put("idCliente", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

	private Map<String, Object> toPrepare() throws Exception {
		Map<String, Object> regresar= new HashMap<>();
		StringBuilder sb            = null;
		try {
      if (!Cadena.isVacio(this.attrs.get("idCliente")) && !this.attrs.get("idCliente").toString().equals("-1") && !Objects.equals(this.cliente.toLong("idUsuario"), new Long(this.attrs.get("idCliente").toString()))) {
        regresar.put("idUsuario", this.attrs.get("idCliente"));
        this.cliente= (Entity)DaoFactory.getInstance().toEntity("VistaClientesRepresentantesDto", "cliente", regresar);
      } // if
      else 
        if (!Cadena.isVacio(this.attrs.get("idCliente")) && this.attrs.get("idCliente").toString().equals("-1") && !Objects.equals(this.cliente.toDouble("especial"), -1D)) {
          this.cliente.get("especial").setData(-1D);
          this.cliente.get("idUsuario").setData(-1L);
        } // if  
  		regresar.put("porcentaje", this.cliente.toDouble("especial"));
  		regresar.put("codigos", Constantes.SQL_VERDADERO);
			sb= new StringBuilder("tc_mantic_articulos.id_articulo_tipo=").append(this.attrs.get("idTipoArticulo")).append(" and ");			
      if((boolean) this.attrs.get("buscaPorCodigo")) {
        if(this.attrs.get("codigos")!= null && ((UISelectEntity)this.attrs.get("codigos")).getKey()> 0L) 
          regresar.put("codigos", "tc_mantic_articulos_codigos.id_articulo="+ ((UISelectEntity)this.attrs.get("codigos")).getKey());
        else 
          if(!Cadena.isVacio(JsfBase.getParametro("codigos_input"))) { 
            String codigo= JsfBase.getParametro("codigos_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*");
            regresar.put("codigos", "(tc_mantic_articulos_codigos.codigo regexp '.*".concat(codigo).concat(".*')"));				
          } // if	
      } // if  
      else {
        if(this.attrs.get("codigos")!= null && ((UISelectEntity)this.attrs.get("codigos")).getKey()> 0L) 
          sb.append("tc_mantic_articulos.id_articulo=").append(((UISelectEntity)this.attrs.get("codigos")).getKey()).append(" and ");						
        else 
          if(!Cadena.isVacio(JsfBase.getParametro("codigos_input"))) { 
            String nombre= JsfBase.getParametro("codigos_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*");
            sb.append("(tc_mantic_articulos.nombre regexp '.*").append(nombre).append(".*' or tc_mantic_articulos.descripcion regexp '.*").append(nombre).append(".*') and ");				
          } // if	
      } // else  
			if(Cadena.isVacio(sb.toString()))
				regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			else
			  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));			
		  if(Cadena.isVacio(this.attrs.get("idEmpresa")) || this.attrs.get("idEmpresa").toString().equals("-1"))
			  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
			else
			  regresar.put("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresa")).getKey());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCondicion

  public String doAccion(String accion) {
    EAccion eaccion = null;
    try {
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion
  
	public void doUpdateArticulos() {
		List<Columna> columns         = null;
    Map<String, Object> params    = new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo        = false;
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
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*");
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
	}	// doUpdateArticulos
	
	public void doUpdateArticulosFiltro() {
		List<Columna> columns         = null;
    Map<String, Object> params    = null;
		List<UISelectEntity> articulos= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params= new HashMap<>();
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);
			String search= (String) this.attrs.get("codigoFiltro"); 
			if(!Cadena.isVacio(search)) 
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim().toUpperCase().replaceAll("(,| |\\t)+", ".*");			
			else
				search= "WXYZ";
  		params.put("codigo", search);			        
      articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombreTipoArticulo", params, columns, 40L);
      this.attrs.put("articulosFiltro", articulos);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}	// doUpdateArticulos

	public List<UISelectEntity> doCompleteArticulo(String query) {
		this.attrs.put("existe", null);
		this.attrs.put("codigo", query);
    this.doUpdateArticulos();		
		return (List<UISelectEntity>)this.attrs.get("articulos");
	}	// doCompleteArticulo
	
	public List<UISelectEntity> doCompleteArticuloFiltro(String query) {
		this.attrs.put("existeFiltro", null);
		this.attrs.put("codigoFiltro", query);
    this.doUpdateArticulosFiltro();
		return (List<UISelectEntity>)this.attrs.get("articulosFiltro");
	}	// doCompleteArticulo

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
	} // doFindArticulo
	
	public StreamedContent doPrepareImage(Entity row) {
		StreamedContent regresar= null;
		try {
			regresar= LoadImages.getImage(row.toLong("idKey"));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		return regresar;
	}
	
	public void doUpdateCodigos() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			String search= (String)this.attrs.get("codigoCodigo"); 
			search= !Cadena.isVacio(search) ? search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim(): "WXYZ";
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("sucursales", this.attrs.get("idEmpresa"));
			else
				params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);			
  		params.put("codigo", search);			
      this.attrs.put("codigos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}	// doUpdateCodigos

	public List<UISelectEntity> doCompleteCodigo(String query) {
		this.attrs.put("codigoCodigo", query);
    this.doUpdateCodigos();		
		return (List<UISelectEntity>)this.attrs.get("codigos");
	}	// doCompleteCodigo

}
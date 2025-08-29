package mx.org.kaana.mantic.contadores.backing;

import java.io.Serializable;
import java.sql.Date;
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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.mantic.contadores.reglas.Transaccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.contadores.beans.Contador;
import mx.org.kaana.mantic.db.dto.TcManticContadoresBitacoraDto;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticContadoresFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  
  private EAccion accion;
  private FormatLazyModel lazyDetalle;

	public FormatLazyModel getLazyDetalle() {
		return lazyDetalle;
	}		
  
  public Boolean getAdmin() {
    Boolean regresar= Boolean.FALSE;
    try {
      regresar= JsfBase.isAdminEncuestaOrAdmin();
    } // try
    catch(Exception e) {
      regresar= Boolean.FALSE;
    } // catch
    return regresar;
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("buscaPorCodigo", false);
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());      
			this.attrs.put("isGerente", JsfBase.isAdminEncuestaOrAdmin());
			this.toLoadCatalogos();
      if(JsfBase.getFlashAttribute("idContadorProcess")!= null) {
        this.attrs.put("idContadorProcess", JsfBase.getFlashAttribute("idContadorProcess"));
        this.doLoad();
        this.attrs.put("idContadorProcess", null);
      } // if
      this.accion= EAccion.PROCESAR;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= this.toPrepare();
    try {
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("almacen", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("trabajo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("procesado", EFormatoDinamicos.FECHA_HORA_CORTA));
      params.put("sortOrder", "order by tc_mantic_contadores.registro desc");
      this.lazyModel = new FormatCustomLazy("VistaContadoresDto", params, columns);
      UIBackingUtilities.resetDataTable();
      this.lazyDetalle= null;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } 
  
  public String doAccion(String accion) {
    EAccion eaccion = null;
    try {
      eaccion = EAccion.valueOf(accion.toUpperCase());
      JsfBase.setFlashAttribute("accion", eaccion);
      JsfBase.setFlashAttribute("idContador", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity) this.attrs.get("seleccionado")).getKey() : -1L);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return "accion".concat(Constantes.REDIRECIONAR);
  } 
	
	private void toLoadCatalogos() {
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
      this.attrs.put("almacenes", (List<UISelectEntity>) UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
			this.attrs.put("idAlmacen", new UISelectEntity("-1"));
			columns.remove(0);
      this.attrs.put("estatus", (List<UISelectEntity>) UIEntity.build("TcManticContadoresEstatusDto", "row", params, columns));
			this.attrs.put("idContadorEstatus", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= new HashMap<>();
		StringBuilder sb            = new StringBuilder("");
		try {
      if(!Cadena.isVacio(this.attrs.get("idContadorProcess")) && !Objects.equals((Long)this.attrs.get("idContadorProcess"), -1L)) 
        sb.append("tc_mantic_contadores.id_contador=").append(this.attrs.get("idContadorProcess")).append(" and ");
			if(!Cadena.isVacio(this.attrs.get("consecutivo")))
				sb.append("(tc_mantic_contadores.consecutivo= '").append(this.attrs.get("consecutivo")).append("') and ");	
			if(!Cadena.isVacio(JsfBase.getParametro("codigo_input")))
				sb.append("upper(tc_mantic_articulos_codigos.codigo) like upper('%").append(JsfBase.getParametro("codigo_input")).append("%') and ");						
			if(this.attrs.get("nombre")!= null && ((UISelectEntity)this.attrs.get("nombre")).getKey()> 0L) 
				sb.append("tc_mantic_articulos.id_articulo=").append(((UISelectEntity)this.attrs.get("nombre")).getKey()).append(" and ");						
  		else 
	  		if(!Cadena.isVacio(JsfBase.getParametro("nombre_input"))) { 
					String nombre= JsfBase.getParametro("nombre_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*");
		  		sb.append("(tc_mantic_articulos.nombre regexp '").append(nombre).append(".*' or tc_mantic_articulos.descripcion regexp '").append(nombre).append(".*') and ");				
				} // if	
			if(this.attrs.get("usuario")!= null && ((UISelectEntity)this.attrs.get("usuario")).getKey()> 0L) 
				sb.append("tc_mantic_contadores.id_trabaja=").append(((UISelectEntity)this.attrs.get("usuario")).getKey()).append(" and ");						
  		else 
	  		if(!Cadena.isVacio(JsfBase.getParametro("usuario_input"))) { 
					String nombre= JsfBase.getParametro("usuario_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*");
		  		sb.append("(concat(tc_mantic_personas.nombres, ' ', tc_mantic_personas.paterno, ' ', tc_mantic_personas.materno) regexp '").append(nombre).append(".*' or tc_mantic_personas.cuenta regexp '").append(nombre).append(".*') and ");				
				} // if	
			if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
				sb.append("(substr(tc_mantic_contadores.fecha, 1, 8)>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
			if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
				sb.append("(substr(tc_mantic_contadores.fecha, 1, 8)<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
			if(!Cadena.isVacio(this.attrs.get("idAlmacen")) && !this.attrs.get("idAlmacen").toString().equals("-1"))
  		  regresar.put("almacen", " and (tc_mantic_contadores.id_almacen= "+ ((UISelectEntity)this.attrs.get("idAlmacen")).getKey()+ ")");
			else
  		  regresar.put("almacen", " ");
      if(!Cadena.isVacio(this.attrs.get("idAlmacen")) && !this.attrs.get("idAlmacen").toString().equals("-1"))
  		  sb.append("(tc_mantic_contadores.id_almacen= ").append(this.attrs.get("idAlmacen")).append(") and ");
  		regresar.put(Constantes.SQL_CONDICION, Cadena.isVacio(sb.toString())? Constantes.SQL_VERDADERO: sb.substring(0, sb.length()- 4));
		  if(Cadena.isVacio(this.attrs.get("idEmpresa")) || this.attrs.get("idEmpresa").toString().equals("-1"))
			  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
			else
			  regresar.put("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresa")).getKey());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} 

	public List<UISelectEntity> doCompleteArticulo(String search) {
		this.attrs.put("existe", null);
		List<Columna> columns         = new ArrayList<>();
    Map<String, Object> params    = new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo        = false;
    try {
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*");
        if(Cadena.isVacio(search))
          search= ".*";        
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
    } // finally
		return articulos;
	}	

	public void doAsignaArticulo(SelectEvent event) {
		UISelectEntity seleccion    = null;
		List<UISelectEntity> codigos= null;
		try {
			codigos= (List<UISelectEntity>) this.attrs.get("articulos");
			seleccion= codigos.get(codigos.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("articulo", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} 
  
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
	
	public void doAlmacenes() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("sucursales", this.attrs.get("idEmpresa"));
			else
				params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("almacenes", (List<UISelectEntity>) UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
			this.attrs.put("idAlmacen", new UISelectEntity("-1"));
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

	public List<UISelectEntity> doCompleteCodigo(String search) {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
      search= !Cadena.isVacio(search)? search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim(): "WXYZ";
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
		return (List<UISelectEntity>)this.attrs.get("codigos");
	}

	public void doAsignaCodigo(SelectEvent event) {
		UISelectEntity seleccion    = null;
		List<UISelectEntity> codigos= null;
		try {
			codigos= (List<UISelectEntity>) this.attrs.get("codigos");
			seleccion= codigos.get(codigos.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("codigo", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} 
  
	public List<UISelectEntity> doCompleteUsuario(String search) {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("cuenta", EFormatoDinamicos.MINUSCULAS));
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("sucursales", this.attrs.get("idEmpresa"));
			else
				params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("codigo", search.toUpperCase().replaceAll("(,| |\\t)+", ".*"));
      this.attrs.put("usuarios", (List<UISelectEntity>) UIEntity.build("VistaTcJanalUsuariosDto", "porCuenta", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
		return (List<UISelectEntity>)this.attrs.get("usuarios");
	}

	public void doAsignaUsuario(SelectEvent event) {
		UISelectEntity seleccion    = null;
		List<UISelectEntity> codigos= null;
		try {
			codigos= (List<UISelectEntity>) this.attrs.get("usuarios");
			seleccion= codigos.get(codigos.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("usuario", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} 
  
  public void doConsultar() {
    this.doDetalle((Entity)this.attrs.get("seleccionado"));
  }
  
  public void doDetalle(Entity row) {
		Map<String, Object>params= new HashMap<>();
		List<Columna>columns     = new ArrayList<>();
		try {
			if(row!= null && !row.isEmpty()) {
        this.attrs.put("seleccionado", row);
				params.put("idContador", row.toLong("idContador"));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("cantidad", EFormatoDinamicos.MILES_CON_DECIMALES));
				columns.add(new Columna("procesado", EFormatoDinamicos.FECHA_HORA_CORTA));
				this.lazyDetalle= new FormatLazyModel("VistaContadoresDto", "row", params, columns);
				UIBackingUtilities.resetDataTable("tablaDetalle");
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(columns);
		} // finally
  }
 
  public void doProcesar() {
    this.accion= EAccion.PROCESAR;
    this.attrs.put("titulo", "¿ Está seguro de procesar el conteo seleccionado ?");
  }
  
  public void doEliminar() {
    this.accion= EAccion.DEPURAR;
    this.attrs.put("titulo", "¿ Está seguro de eliminar el conteo seleccionado ?");
  }
  
  public void doEjecutar() {
    switch(this.accion) {
      case PROCESAR:
        this.toProcesar();
        break;
      case DEPURAR:
        this.toEliminar();
        break;
    } // switch
  }
  
  private void toProcesar() {
    Transaccion transaccion  = null;
    Entity seleccionado      = (Entity) this.attrs.get("seleccionado");
		Map<String, Object>params= new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, "id_contador= "+ seleccionado.getKey());
      Contador conteo= (Contador)DaoFactory.getInstance().toEntity(Contador.class, "TcManticContadoresDto", "igual", params);
      transaccion = new Transaccion(conteo);
      if (transaccion.ejecutar(this.accion)) 
        JsfBase.addMessage("Procesar conteo", "El conteo fue procesado correctamente", ETipoMensaje.ERROR);
      else
        JsfBase.addMessage("Procesar conteo", "Ocurrió un error al procesar el conteo", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } 
 
  private void toEliminar() {
    Transaccion transaccion  = null;
		Entity seleccionado      = (Entity)this.attrs.get("seleccionado");
		Map<String, Object>params= new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, "id_contador= "+ seleccionado.getKey());
      Contador conteo= (Contador)DaoFactory.getInstance().toEntity(Contador.class, "TcManticContadoresDto", "igual", params);
      transaccion = new Transaccion(conteo);
      if (transaccion.ejecutar(this.accion)) 
        JsfBase.addMessage("Eliminar conteo", "El conteo fue eliminado correctamente", ETipoMensaje.ERROR);
      else
        JsfBase.addMessage("Eliminar conteo", "Ocurrió un error al eliminar el conteo", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  }
 
	public void doActualizarEstatus() {
		Transaccion transaccion               = null;
		TcManticContadoresBitacoraDto bitacora= null;
		Entity seleccionado                   = (Entity)this.attrs.get("seleccionado");
		Map<String, Object>params             = new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, "id_contador= "+ seleccionado.getKey());
      Contador conteo= (Contador)DaoFactory.getInstance().toEntity(Contador.class, "TcManticContadoresDto", "igual", params);
			bitacora    = new TcManticContadoresBitacoraDto(
        (String)this.attrs.get("justificacion"), // String justificacion, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        seleccionado.getKey(), // Long idContador, 
        -1L, // Long idContadorBitacora, 
        Long.valueOf((String)this.attrs.get("idEstatus")) // Long idContadorEstatus
      );
			transaccion = new Transaccion(conteo, bitacora);
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			this.attrs.put("justificacion", "");
      Methods.clean(params);
		} // finally
	}	
 
	public void doLoadEstatus() {
		Entity seleccionado          = (Entity)this.attrs.get("seleccionado");
		Map<String, Object>params    = new HashMap<>();
		List<UISelectItem> allEstatus= null;
		try {
			params.put(Constantes.SQL_CONDICION, "id_contador_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticContadoresEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
			this.attrs.put("allEstatus", allEstatus);
			this.attrs.put("idEstatus", allEstatus.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} 
  
}
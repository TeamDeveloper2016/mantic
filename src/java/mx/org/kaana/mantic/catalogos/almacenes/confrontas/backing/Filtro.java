package mx.org.kaana.mantic.catalogos.almacenes.confrontas.backing;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Xls;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosAlmacenesConfrontasFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741398428879L;
  private Reporte reporte;

	public Boolean getIsAutorizar() {
		Boolean regresar= true;
		try {
			regresar= this.attrs.get("seleccionado")!= null && ((Entity)this.attrs.get("seleccionado")).toLong("estatus")== 7L && JsfBase.isAdminEncuestaOrAdmin();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	}

  public StreamedContent getArchivo() {
		StreamedContent regresar= null;
		Xls xls                 = null;
		String template         = "CONTEOS";
		Map<String, Object> params=null;
		try {
			String salida  = EFormatos.XLS.toPath().concat(Archivo.toFormatNameFile(template).concat(".")).concat(EFormatos.XLS.name().toLowerCase());
  		String fileName= JsfBase.getRealPath("").concat(salida);
			params         = new HashMap<>();
			params.put("idConfronta", this.attrs.get("seleccionado")!= null? ((Entity)this.attrs.get("seleccionado")).toLong("idConfronta"): -1L);
      xls= new Xls(fileName, new Modelo(params, "VistaConfrontasDto", "destino", template), "CODIGO,NOMBRE,FECHA,STOCK");	
			if(xls.procesar()) {
				Zip zip       = new Zip();
				String zipName= Archivo.toFormatNameFile(template).concat(".").concat(EFormatos.ZIP.name().toLowerCase());
				zip.setEliminar(true);
				zip.compactar(JsfBase.getRealPath("").concat(EFormatos.XLS.toPath()).concat(zipName), JsfBase.getRealPath("").concat(EFormatos.XLS.toPath()), "*".concat(template.concat(".").concat(EFormatos.XLS.name().toLowerCase())));
		    String contentType= EFormatos.ZIP.getContent();
        InputStream stream= ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(EFormatos.XLS.toPath().concat(zipName));  
		    // String contentType= EFormatos.XLS.getContent();
        // InputStream stream= ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(salida);  
		    regresar          = new DefaultStreamedContent(stream, contentType, Archivo.toFormatNameFile(template).concat(".").concat(EFormatos.ZIP.name().toLowerCase()));				
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
    return regresar;
  }	
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idTransferencia", JsfBase.getFlashAttribute("idTransferencia"));
      this.attrs.put("idConfronta", JsfBase.getFlashAttribute("idConfronta"));
			this.toLoadCatalog();
      if(this.attrs.get("idConfronta")!= null || this.attrs.get("idTransferencia")!= null) 
			  this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> campos      = null;
		Map<String, Object> params= this.toPrepare();
    try {
      params.put("sortOrder", "order by tc_mantic_transferencias.consecutivo desc");
      campos = new ArrayList<>();
      campos.add(new Columna("nombreOrigen", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("nombreDestino", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      campos.add(new Columna("solicito", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatCustomLazy("VistaConfrontasDto", params, campos);
      UIBackingUtilities.resetDataTable();
			this.attrs.put("idTransferencia", null);
			this.attrs.put("idConfronta", null);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(campos);
    } // finally		
  } // doLoad

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("idConfronta")) && !this.attrs.get("idConfronta").toString().equals("-1"))
  		sb.append("(tc_mantic_confrontas.id_confronta=").append(this.attrs.get("idConfronta")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idTransferencia")) && !this.attrs.get("idTransferencia").toString().equals("-1"))
  		sb.append("(tc_mantic_transferencias.id_transferencia=").append(this.attrs.get("idTransferencia")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_confrontas.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("transferencia")))
  		sb.append("(tc_mantic_transferencias.consecutivo like '%").append(this.attrs.get("transferencia")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("idAlmacen")) && !this.attrs.get("idAlmacen").toString().equals("-1"))
  		sb.append("(tc_mantic_transferencias.id_destino= ").append(this.attrs.get("idAlmacen")).append(") and ");
		UISelectEntity articulo      = (UISelectEntity)this.attrs.get("articulo");
		List<UISelectEntity>articulos= (List<UISelectEntity>)this.attrs.get("articulos");
		if(articulos!= null && articulo!= null && articulos.indexOf(articulo)>= 0) 
			sb.append("(tc_mantic_confrontas_detalles.id_articulo= ").append(articulos.get(articulos.indexOf(articulo)).getKey()).append(") and ");				
		else 
			if(!Cadena.isVacio(JsfBase.getParametro("codigos_input"))) 
				sb.append("(tc_mantic_confrontas_detalles.nombre regexp '.*").append(JsfBase.getParametro("codigos_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*")).append(".*' or tc_mantic_confrontas_detalles.codigo regexp '.*").append(JsfBase.getParametro("codigos_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*")).append(".*') and ");				
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_confrontas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_confrontas, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("idTransferenciaEstatus")) && !this.attrs.get("idTransferenciaEstatus").toString().equals("-1"))
  		sb.append("(tc_mantic_transferencias.id_transferencia_estatus= ").append(this.attrs.get("idTransferenciaEstatus")).append(") and ");
		else
			if(sb.length()== 0)
  		  sb.append("(tc_mantic_transferencias.id_transferencia_estatus>= 3) and ");
			else {
				sb.delete(sb.length()- 4, sb.length());
				sb.insert(0, "(").append(" or (tc_mantic_transferencias.id_destino= ").append(this.attrs.get("idAlmacen")).append(" and tc_mantic_transferencias.id_transferencia_estatus= 3)) and ");
			} // else	
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()!= 0)
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		// or tc_mantic_transferencias.id_transferencia_estatus= 3
		return regresar;
	}

	private void toLoadCatalog() throws Exception {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
	  List<UISelectEntity> empresas = null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			if(JsfBase.isAdminEncuestaOrAdmin()) {
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
  			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
        empresas = (List<UISelectEntity>) UIEntity.seleccione("TcManticEmpresasDto", "empresas", params, columns, "clave");
			} // if	
			else {
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
  			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
        empresas = (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
			} // else	
			this.attrs.put("empresas", empresas);
			this.attrs.put("idEmpresa", empresas.size()> 0? empresas.get(0): new UISelectEntity("-1"));
			List<UISelectEntity> almacenes= null;
			if(JsfBase.isAdminEncuestaOrAdmin()) 
				almacenes= (List<UISelectEntity>) UIEntity.seleccione("TcManticAlmacenesDto", "almacenes", params, columns, "clave");
			else {
        params.put("idEmpresa", empresas.size()> 0? empresas.get(0).getKey(): -1L);
  			params.put("sucursales", empresas.size()> 0? empresas.get(0).getKey(): -1L);
				almacenes= (List<UISelectEntity>) UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns);
			} // else
			this.attrs.put("almacenes", almacenes);
			this.attrs.put("idAlmacen", almacenes.size()> 0? almacenes.get(0): new UISelectEntity("-1"));
			columns.remove(0);
			params.put(Constantes.SQL_CONDICION, "id_transferencia_estatus>= 3");
      this.attrs.put("catalogo", (List<UISelectEntity>) UIEntity.build("TcManticTransferenciasEstatusDto", "row", params, columns));
			this.attrs.put("idTransferenciasEstatus", new UISelectEntity("-1"));
			this.loadPersonas();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

  private void loadPersonas() {
    List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
      columns= new ArrayList<>();
      params = new HashMap<>();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      List<UISelectEntity> personas= UIEntity.build("VistaAlmacenesTransferenciasDto", "solicito", params, columns);
      this.attrs.put("personas", personas);
			if(personas!= null && !personas.isEmpty())
			  this.attrs.put("idTransporto", personas.get(0));
		  else	
			  this.attrs.put("idTransporto", new UISelectEntity(-1L));
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally 
  }
	
  public String doAccion(String accion) {
		String regresar= "accion";
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
		  JsfBase.setFlashAttribute("retorno", "filtro");		
		  JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idConfronta", (eaccion.equals(EAccion.MODIFICAR)||eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).toLong("idConfronta"): -1L);
			JsfBase.setFlashAttribute("idTransferencia", (eaccion.equals(EAccion.MODIFICAR)||eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).toLong("idTransferencia"): -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar.concat(Constantes.REDIRECIONAR);
  } // doAccion
  
  public void doReporte(String nombre) throws Exception {
    Parametros comunes = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
    Map<String, Object>params    = null;
		try {		
      params= toPrepare();
      seleccionado = ((Entity)this.attrs.get("seleccionado")); 
      if(seleccionado != null){
        params.put("idKeyTransferencia", seleccionado.getKey());
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), seleccionado.toLong("idAlmacen"), seleccionado.toLong("idDestino"));
      }
      else
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      params.put("sortOrder", "order by tc_mantic_confrontas.registro asc");
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());	
      reporteSeleccion= EReportes.valueOf(nombre);
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
      if(this.doVerificarReporte())
        this.reporte.doAceptar();			
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // doReporte
  
  public boolean doVerificarReporte() {
    boolean regresar = false;
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L){
			rc.execute("start(" + this.reporte.getTotal() + ")");		
      regresar = true;
    } // if
    else {
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
		} // else
    return regresar;
	} // doVerificarReporte		
	
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
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*");
        if(Cadena.isVacio(search))
          search= "WXYZ";        
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
			if(buscaPorCodigo)
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

	public void doAlmacenes() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
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

	public String doTransferencias() {
		JsfBase.setFlashAttribute("idTransferencia", this.attrs.get("idTransferencia"));
		return "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/filtro".concat(Constantes.REDIRECIONAR);
	}

  public String doDiferencias() {
		JsfBase.setFlashAttribute("idTransferencia", ((Entity)this.attrs.get("seleccionado")).toLong("idTransferencia"));
		return "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/diferencias".concat(Constantes.REDIRECIONAR);
	}	
	
  public String doAutorizar() {
		JsfBase.setFlashAttribute("accion", EAccion.CALCULAR);		
		JsfBase.setFlashAttribute("idConfronta", ((Entity)this.attrs.get("seleccionado")).toLong("idConfronta"));
		JsfBase.setFlashAttribute("idTransferencia", ((Entity)this.attrs.get("seleccionado")).toLong("idTransferencia"));
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Catalogos/Almacenes/Confrontas/filtro");
		return "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/autorizar".concat(Constantes.REDIRECIONAR);
	}	
	
	public String doMovimientos() {
		JsfBase.setFlashAttribute("tipo", ETipoMovimiento.TRANSFERENCIAS);
		JsfBase.setFlashAttribute(ETipoMovimiento.TRANSFERENCIAS.getIdKey(), ((Entity)this.attrs.get("seleccionado")).toLong(ETipoMovimiento.TRANSFERENCIAS.getIdKey()));
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Catalogos/Almacenes/Confrontas/filtro");
		return "/Paginas/Mantic/Compras/Ordenes/movimientos".concat(Constantes.REDIRECIONAR);
	}

  public String doRecibir() {
		try {
			Entity seleccionado= (Entity)this.attrs.get("seleccionado");
  	  JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
		  JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Almacenes/Confrontas/filtro");		
			JsfBase.setFlashAttribute("idTransferencia", seleccionado.toLong("idTransferencia"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Catalogos/Almacenes/Confrontas/accion".concat(Constantes.REDIRECIONAR);
  } // doRecibir	

	public String toColor(Entity row) {
		return row.toDouble("perdidos")> 0D? "janal-tr-orange": "";
	} 
  
  public void doUpdatePerdidos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		List<Entity> documentos   = null;
    try {
      Entity seleccionado= (Entity)this.attrs.get("seleccionado");
			columns= new ArrayList<>();
      columns.add(new Columna("autorizo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("cantidades", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("declarados", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("diferencia", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("perdidos", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("articulos", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
   		params.put("idConfronta", seleccionado.toLong("idConfronta"));
      documentos= (List<Entity>) DaoFactory.getInstance().toEntitySet("VistaConfrontasDto", "perdidos", params, Constantes.SQL_TODOS_REGISTROS);
      if(documentos!= null && !documentos.isEmpty())
        UIBackingUtilities.toFormatEntitySet(documentos, columns);
      this.attrs.put("documentos", documentos);
      this.attrs.put("tipoDocumento", "de la transferencia con partida(s) desparecidas !");
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
  
}

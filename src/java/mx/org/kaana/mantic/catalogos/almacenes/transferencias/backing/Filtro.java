package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
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
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.conteos.reglas.Operacion;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosAlmacenesTransferenciasFiltro")
@ViewScoped
public class Filtro extends IBaseImportar implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  
  protected EAccion accion;
  protected Reporte reporte;
  private FormatLazyModel lazyDetalle;

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
		Map<String, Object> params=new HashMap<>();
		try {
			String salida  = EFormatos.XLS.toPath().concat(Archivo.toFormatNameFile(template).concat(".")).concat(EFormatos.XLS.name().toLowerCase());
  		String fileName= JsfBase.getRealPath("").concat(salida);
			params.put("idTransferencia", this.attrs.get("seleccionado")!= null? ((Entity)this.attrs.get("seleccionado")).toLong("idTransferencia"): -1L);
      xls= new Xls(fileName, new Modelo(params, "VistaConfrontasDto", "origen", template), "CODIGO,NOMBRE,FECHA,STOCK");	
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
  
	public FormatLazyModel getLazyDetalle() {
		return lazyDetalle;
	}		
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idTransferencia", JsfBase.getFlashAttribute("idTransferencia"));
      this.attrs.put("transito", false);
			this.toLoadCatalog();
      if(this.attrs.get("idTransferencia")!= null) 
			  this.doLoad();
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
      params.put("sortOrder", "order by tc_mantic_transferencias.registro desc");
      columns.add(new Columna("nombreOrigen", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombreDestino", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("solicito", EFormatoDinamicos.MAYUSCULAS));
  		columns.add(new Columna("articulos", EFormatoDinamicos.MILES_SIN_DECIMALES));
      this.lazyModel = new FormatCustomLazy("VistaAlmacenesTransferenciasDto", params, columns);
      UIBackingUtilities.resetDataTable();
			this.attrs.put("idTransferencia", null);
      this.lazyDetalle= null;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("idTransferencia")) && !this.attrs.get("idTransferencia").toString().equals("-1"))
  		sb.append("(tc_mantic_transferencias.id_transferencia=").append(this.attrs.get("idTransferencia")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_transferencias.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("idAlmacen")) && !this.attrs.get("idAlmacen").toString().equals("-1"))
  		sb.append("(tc_mantic_transferencias.id_almacen= ").append(this.attrs.get("idAlmacen")).append(" or tc_mantic_transferencias.id_destino= ").append(this.attrs.get("idAlmacen")).append(") and ");
		UISelectEntity articulo      = (UISelectEntity)this.attrs.get("articulo");
		List<UISelectEntity>articulos= (List<UISelectEntity>)this.attrs.get("articulos");
		if(articulos!= null && articulo!= null && articulos.indexOf(articulo)>= 0) 
			sb.append("(tc_mantic_transferencias_detalles.id_articulo= ").append(articulos.get(articulos.indexOf(articulo)).getKey()).append(") and ");				
		else 
			if(!Cadena.isVacio(JsfBase.getParametro("codigos_input"))) 
				sb.append("(tc_mantic_transferencias_detalles.nombre regexp '.*").append(JsfBase.getParametro("codigos_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*")).append(".*' or tc_mantic_transferencias_detalles.codigo regexp '.*").append(JsfBase.getParametro("codigos_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*")).append(".*') and ");				
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_transferencias.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_transferencias.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("idTransferenciaEstatus")) && !this.attrs.get("idTransferenciaEstatus").toString().equals("-1"))
  		sb.append("(tc_mantic_transferencias.id_transferencia_estatus= ").append(this.attrs.get("idTransferenciaEstatus")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idTransferenciaTipo")) && !this.attrs.get("idTransferenciaTipo").toString().equals("-1"))
  		sb.append("(tc_mantic_transferencias.id_transferencia_tipo= ").append(this.attrs.get("idTransferenciaTipo")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;
	}

	protected void toLoadCatalog() {
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
      this.attrs.put("tipos", (List<UISelectEntity>) UIEntity.build("TcManticTransferenciasTiposDto", "row", params, columns));
			this.attrs.put("idTransferenciaTipo", new UISelectEntity("-1"));
      this.toLoadTransferenciasEstatus();
      this.toLoadTransferenciasTipos();
			this.toLoadPersonas();
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

	protected void toLoadTransferenciasEstatus() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("catalogo", (List<UISelectEntity>) UIEntity.build("TcManticTransferenciasEstatusDto", "row", params, columns));
			this.attrs.put("idTransferenciasEstatus", new UISelectEntity("-1"));
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

	protected void toLoadTransferenciasTipos() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, "id_transferencia_tipo!= 4");
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("tipos", (List<UISelectEntity>) UIEntity.build("TcManticTransferenciasTiposDto", "row", params, columns));
			this.attrs.put("idTransferenciaTipo", new UISelectEntity("-1"));
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

  protected void toLoadPersonas() {
    List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      List<UISelectEntity> personas= UIEntity.build("VistaAlmacenesTransferenciasDto", "solicito", params, columns);
      this.attrs.put("personas", personas);
      this.attrs.put("idTransporto", UIBackingUtilities.toFirstKeySelectEntity(personas));
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
	
  public String doAccion(String accion) {
		String regresar    = "accion";
    EAccion eaccion    = null;
    Entity seleccionado= (Entity)this.attrs.get("seleccionado");
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
		  JsfBase.setFlashAttribute("retorno", "filtro");		
		  JsfBase.setFlashAttribute("accion", eaccion);		
			if(Objects.equals(seleccionado.toLong("idTransferenciaTipo"), 2L) || Objects.equals(seleccionado.toLong("idTransferenciaTipo"), 3L))
				regresar= "normal";
      else
  			if(Objects.equals(seleccionado.toLong("idTransferenciaTipo"), 4L))
  				regresar= "/Paginas/Mantic/Solicitudes/solicitud";
			JsfBase.setFlashAttribute("idTransferencia", (eaccion.equals(EAccion.MODIFICAR)||eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey(): -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar.concat(Constantes.REDIRECIONAR);
  } // doAccion
	
  public String doRecibir() {
		try {
			Entity seleccionado= (Entity)this.attrs.get("seleccionado");
			switch(seleccionado.toLong("estatus").intValue()) {
				case 3: // TRANSITO
				  JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
				  break;
				case 5: // RECEPCION
				case 6: // INCOMPLETA
				  JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);
				  break;
			} // switch	
		  JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/filtro");		
			JsfBase.setFlashAttribute("idTransferencia", seleccionado.getKey());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Catalogos/Almacenes/Confrontas/accion".concat(Constantes.REDIRECIONAR);
  } // doRecibir
  
  public void doLoadEstatus() {
		Entity seleccionado          = null;
		Map<String, Object>params    = new HashMap<>();
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params.put(Constantes.SQL_CONDICION, "id_transferencia_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticTransferenciasEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
			this.attrs.put("allEstatusAsigna", allEstatus);
			this.attrs.put("estatusAsigna", allEstatus.get(0));
			this.attrs.put("transito", allEstatus.get(0).getValue().equals("3"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doLoadEstatus
	
	public void doActualizarEstatus() {
    Long idEstatus= Long.valueOf((String)this.attrs.get("estatus"));
    Long idTransferenciaEstatus= ((Entity)this.attrs.get("seleccionado")).toLong("estatus");
  	if(Objects.equals(idTransferenciaEstatus, 5L) || Objects.equals(idTransferenciaEstatus, 10L))
      this.toOperacion(idEstatus);
    else
      this.toTransaccion(idEstatus);
  }
  
	private void toTransaccion(Long idEstatus) {
		Transaccion transaccion= null;
		Entity seleccionado    = (Entity)this.attrs.get("seleccionado");
		Long idTransporto      = null;
		try {
			idTransporto= this.attrs.get("idTransporto")!= null && ((Entity)this.attrs.get("idTransporto")).getKey()> 0L? ((Entity)this.attrs.get("idTransporto")).getKey(): JsfBase.getIdUsuario();
			TcManticTransferenciasBitacoraDto bitacora= new TcManticTransferenciasBitacoraDto(
        -1L, // Long idTransferenciaBitacora, 
        (String)this.attrs.get("justificacion"), // String justificacion, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        Objects.equals((String)this.attrs.get("estatus"), "3")? idTransporto: null, // Long idTransporto, 
        idEstatus, // Long idTransferenciaEstatus, 
        seleccionado.getKey() // Long idTransferencia
      );
			transaccion= new Transaccion((TcManticTransferenciasDto)DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, seleccionado.getKey()), bitacora);
			if(transaccion.ejecutar(EAccion.REGISTRAR)) 
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			this.attrs.put("justificacion", "");
		} // finally
	}	// doActualizaEstatus

	private void toOperacion(Long idEstatus) {
		Operacion operacion= null;
		Entity seleccionado= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			TcManticTransferenciasBitacoraDto bitacora= null;
  	  bitacora= new TcManticTransferenciasBitacoraDto(-1L, (String)this.attrs.get("justificacion"), JsfBase.getIdUsuario(), null, idEstatus, seleccionado.getKey());
			operacion = new Operacion((TcManticTransferenciasDto)DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, seleccionado.getKey()), bitacora);
			if(operacion.ejecutar(EAccion.JUSTIFICAR)) 
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			this.attrs.put("justificacion", "");
		} // finally
	}	

  public void doReporte(String nombre) throws Exception {
    Parametros comunes = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
    Map<String, Object>params    = null;
		try {		
      params= this.toPrepare();
      seleccionado = ((Entity)this.attrs.get("seleccionado"));
      if(seleccionado != null) {
        params.put("idKeyTransferencia", seleccionado.getKey());
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), seleccionado.toLong("idAlmacen"), seleccionado.toLong("idDestino"));
      }
      else
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      params.put("sortOrder", "order by tc_mantic_transferencias.id_almacen, tc_mantic_transferencias.id_destino desc");
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());	
      reporteSeleccion= EReportes.valueOf(nombre);
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
      if(doVerificarReporte())
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
    }
		else{
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
      regresar = false;
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
			String search= (String)this.attrs.get("codigo"); 
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

	public String doNormal(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idTransferencia", (eaccion.equals(EAccion.MODIFICAR)||eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "normal".concat(Constantes.REDIRECIONAR);
  } 

	public void doTransporta() {
    this.attrs.put("transito", this.attrs.get("estatus")!= null && ((String)this.attrs.get("estatus")).equals("3"));
	}
	
	public String doMovimientos() {
		JsfBase.setFlashAttribute("tipo", ETipoMovimiento.TRANSFERENCIAS);
		JsfBase.setFlashAttribute(ETipoMovimiento.TRANSFERENCIAS.getIdKey(), ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/filtro");
		return "/Paginas/Mantic/Compras/Ordenes/movimientos".concat(Constantes.REDIRECIONAR);
	}

  public String doDiferencias() {
		JsfBase.setFlashAttribute("idTransferencia", ((Entity)this.attrs.get("seleccionado")).getKey());
		return "diferencias".concat(Constantes.REDIRECIONAR);
	}	

  public String doAutorizar() {
		JsfBase.setFlashAttribute("accion", EAccion.CALCULAR);		
		JsfBase.setFlashAttribute("idTransferencia", ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/filtro");
		return "autorizar".concat(Constantes.REDIRECIONAR);
	}	
	
	public String toColor(Entity row) {
		return ""; // row.toDouble("perdidos")> 0D? "janal-tr-orange": "";
	} 

  public void doProcesar() {
    this.accion= EAccion.PROCESAR;
    this.attrs.put("titulo", "¿ Está seguro de procesar la transferencia seleccionado ?");
  }
  
  public void doEliminar() {
    this.accion= EAccion.DEPURAR;
    this.attrs.put("titulo", "¿ Está seguro de eliminar la transferencia seleccionado ?");
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
    Operacion operacion= null;
    Entity seleccionado= (Entity) this.attrs.get("seleccionado");
    try {
      TcManticTransferenciasDto transferencia= (TcManticTransferenciasDto)DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, seleccionado.getKey());
      operacion = new Operacion(transferencia);
      if (operacion.ejecutar(this.accion)) 
        JsfBase.addMessage("Procesar transferencia", "La transferencia fue procesada correctamente", ETipoMensaje.ERROR);
      else
        JsfBase.addMessage("Procesar transferencia", "Ocurrió un error al procesar la transferencia", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } 
 
  private void toEliminar() {
    Operacion operacion= null;
		Entity seleccionado= (Entity)this.attrs.get("seleccionado");
    try {
      TcManticTransferenciasDto transferencia= (TcManticTransferenciasDto)DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, seleccionado.getKey());
      operacion = new Operacion(transferencia);
      if (operacion.ejecutar(this.accion)) 
        JsfBase.addMessage("Eliminar transferencia", "La transferencia fue eliminada correctamente", ETipoMensaje.ERROR);
      else
        JsfBase.addMessage("Eliminar transferencia", "Ocurrió un error al eliminar la transferencia", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  }

  public String doUmbrales() {
		JsfBase.setFlashAttribute("idTransferencia", ((Entity)this.attrs.get("seleccionado")).getKey());
		return "umbrales".concat(Constantes.REDIRECIONAR);
	}	
  
  public String doSolicitudes(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idTransferencia", (eaccion.equals(EAccion.MODIFICAR)||eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Solicitudes/solicitud".concat(Constantes.REDIRECIONAR);
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
				params.put("idTransferencia", row.toLong("idTransferencia"));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("cantidad", EFormatoDinamicos.MILES_CON_DECIMALES));
				columns.add(new Columna("procesado", EFormatoDinamicos.FECHA_HORA_CORTA));
				columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
				this.lazyDetalle= new FormatLazyModel("VistaAlmacenesTransferenciasDto", "igual", params, columns);
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
  
}

package mx.org.kaana.mantic.egresos.backing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.db.dto.TcManticEgresosBitacoraDto;
import mx.org.kaana.mantic.egresos.beans.ZipEgreso;
import mx.org.kaana.mantic.egresos.reglas.Transaccion;
import mx.org.kaana.mantic.enums.ECuentasEgresos;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;

@Named(value = "manticEgresosFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  protected FormatLazyModel lazyModelDetalle;
  private String path;

  public FormatLazyModel getLazyModelDetalle() {
    return lazyModelDetalle;
  }
  
  public String getPath() {
    return path;
  }

  @PostConstruct
  @Override
  protected void init() {
		Long idEgreso= (Long)JsfBase.getFlashAttribute("idEgreso");
    try {    	      
      this.attrs.put("descripcion", "");
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());  
			this.loadEstatus();
			if(idEgreso!= null) {
				this.attrs.put("idEgreso", idEgreso);
				this.doLoad();
			} // if
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/archivos/");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadEstatus() {
		List<UISelectItem>estatus= null;
		Map<String, Object>params= new HashMap<>();
		try {
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			estatus= UISelect.build("TcManticEgresosEstatusDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS);
			estatus.add(0, new UISelectItem(-1L, "TODOS"));
			this.attrs.put("estatus", estatus);
			this.attrs.put("idEstatus", UIBackingUtilities.toFirstKeySelectItem(estatus));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} 
	
  @Override
  public void doLoad() {
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));			
      columns.add(new Columna("importe", EFormatoDinamicos.MILES_CON_DECIMALES));			
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));			
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));			
			params.put(Constantes.SQL_CONDICION, this.toCondicion());			
			params.put("sortOrder", "order by tc_mantic_egresos.registro desc, consecutivo desc");			
      this.lazyModel = new FormatCustomLazy("VistaEgresosDto", params, columns);
      UIBackingUtilities.resetDataTable();
      this.attrs.put("importados", null);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad
	
	private String toCondicion() {
		String regresar = null;
		String search   = null;
		StringBuilder sb= null;
		try {			
			sb= new StringBuilder("");			
			if(!Cadena.isVacio(this.attrs.get("idEgreso")) && !Long.valueOf(this.attrs.get("idEgreso").toString()).equals(-1L)){
				sb.append("tc_mantic_egresos.id_egreso=").append(this.attrs.get("idEgreso")).append(" and ");							
				this.attrs.put("idEgreso", "");
			} // if
			if(!Cadena.isVacio(this.attrs.get("consecutivo")))
				sb.append("(tc_mantic_egresos.consecutivo='").append(this.attrs.get("consecutivo")).append("') and ");
			if(!Cadena.isVacio(this.attrs.get("notaEntrada")))
				sb.append("(tc_mantic_notas_entradas.consecutivo='").append(this.attrs.get("notaEntrada")).append("') and ");
			if(!Cadena.isVacio(this.attrs.get("factura")))
				sb.append("(tc_mantic_notas_entradas.factura='").append(this.attrs.get("factura")).append("') and ");
      if(!Cadena.isVacio(this.attrs.get("idProveedor")) && !this.attrs.get("idProveedor").toString().equals("-1"))
        sb.append("(tc_mantic_notas_entradas.id_proveedor= ").append(this.attrs.get("idProveedor")).append(") and ");
			if(!Cadena.isVacio(this.attrs.get("idEstatus")) && !Long.valueOf(this.attrs.get("idEstatus").toString()).equals(-1L))
				sb.append("(tc_mantic_egresos.id_egreso_estatus=").append(this.attrs.get("idEstatus")).append(") and ");
			if(!Cadena.isVacio(this.attrs.get("fecha")))
				sb.append("(date_format(tc_mantic_egresos.fecha, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fecha"))).append("') and ");	
			if(!Cadena.isVacio(this.attrs.get("fechaFinal")))
				sb.append("(date_format(tc_mantic_egresos.fecha, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaFinal"))).append("') and ");	
			if(this.attrs.get("importeTicket")!= null && !Cadena.isVacio(this.attrs.get("importeTicket")) && !this.attrs.get("importeTicket").toString().equals("0.00"))
				sb.append("tc_mantic_egresos.importe like '%").append(Cadena.eliminaCaracter(this.attrs.get("importeTicket").toString(), ',')).append("%' and ");					
			search= (String) this.attrs.get("descripcion");
			if(!Cadena.isVacio(search)) {
				search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();				
				sb.append("upper(tc_mantic_egresos.descripcion) regexp upper('.*").append(search.toUpperCase().replaceAll("(,| |\\t)+", ".*")).append(".*') and ");						
			} // if									
			if(Cadena.isVacio(sb))
				regresar= Constantes.SQL_VERDADERO;
			else
				regresar= sb.substring(0, sb.length()-4);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCondicion

  public String doAccion(String accion) {
    EAccion eaccion = null;
    try {
      eaccion = EAccion.valueOf(accion.toUpperCase());
      JsfBase.setFlashAttribute("accion", eaccion);
      JsfBase.setFlashAttribute("retorno", "filtro");
      JsfBase.setFlashAttribute("idEgreso", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) || eaccion.equals(EAccion.ELIMINAR)) ? ((Entity) this.attrs.get("seleccionado")).getKey() : -1L);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion	  		
	
  public String doMasivo() {
    JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Egresos/filtro");
    JsfBase.setFlashAttribute("idTipoMasivo", ECargaMasiva.EGRESOS.getId());
    return "/Paginas/Mantic/Catalogos/Masivos/importar".concat(Constantes.REDIRECIONAR);
	} // doMasivo	
	
	public void doRegistraNota() {
		Transaccion transaccion= null;
		Entity seleccionado    = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
			transaccion= new Transaccion(seleccionado.getKey(), this.attrs.get("nota").toString());
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
				JsfBase.addMessage("Registrar nota", "Se registro de forma correcta la nota.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Registrar nota", "Ocurrió un error al registrar la nota.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doRegistraNota
	
	public String doDetalle() {
		String regresar= null;		
		try {
			JsfBase.setFlashAttribute("idEgreso", ((Entity)this.attrs.get("seleccionado")).getKey());
			regresar= "detalle".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doDetalle
	
	public String doDocumentos() {
		String regresar= null;
		try {
      Entity row= (Entity)this.attrs.get("seleccionado");
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Egresos/filtro");		
			JsfBase.setFlashAttribute("idEgreso", row.getKey());
			regresar= "importar".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doImporta
	
	public StreamedContent getDocumento() {
		StreamedContent regresar= null;		
		Entity seleccionado     = (Entity) this.attrs.get("seleccionado");				
		try {			
			regresar= this.toZipFile(this.toAllFiles(seleccionado), seleccionado.toString("consecutivo"), seleccionado.getKey());
		} // try 
		catch (Exception e) {
			Error.mensaje(e);
		} // catch		
    return regresar;		
	} // getDocumento
	
	private List<ZipEgreso> toAllFiles(Entity seleccionado) throws Exception {
		List<ZipEgreso> regresar  = new ArrayList<>();
		List<String> namesFiles   = null;
		Map<String, Object> params= new HashMap<>();		
		List<Nombres> list        = null;
		ZipEgreso pivote          = null;
		try {			
			params.put("idEgreso", seleccionado.getKey());
			for(ECuentasEgresos item: ECuentasEgresos.values()) {
        if(Objects.equals(item.getGroup(), 1)) {
          list= (List<Nombres>)DaoFactory.getInstance().toEntitySet(Nombres.class, "VistaEgresosDto", item.getIdXml(), params);
          if(!list.isEmpty()) {
            String carpeta= null;
            for(Nombres file: list) {
              String temporal= file.getConsecutivo().concat("/").concat(Cadena.eliminaCaracter(file.getTipo(), ' '));
              if(carpeta== null || !Objects.equals(carpeta, temporal)) {
                if(carpeta!= null) {
                  pivote= new ZipEgreso();
                  pivote.setCarpeta(carpeta);
                  pivote.setFiles(namesFiles);
                  regresar.add(pivote);
                } // if  
                namesFiles= new ArrayList<>();
                carpeta= temporal;
              } // if
              namesFiles.add(file.getAlias());
            } // if  
            if(carpeta!= null) {
              pivote= new ZipEgreso();
              pivote.setCarpeta(carpeta);
              pivote.setFiles(namesFiles);
              regresar.add(pivote);
            } // if  
          } // if
        } // if
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} 
	
	private StreamedContent toZipFile(List<ZipEgreso> files, String descripcion, Long idEgreso) {
		String zipName                 = null;		
		String name                    = null;		
		InputStream stream             = null;
		String temporal                = null;
		DefaultStreamedContent regresar= null;
		try {
			temporal= Archivo.toFormatNameFile("_E".concat(descripcion));
			Zip zip= new Zip();			
			name= "/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.ZIP.name()).concat("/").concat(temporal));
			zipName= name.concat(".").concat(EFormatos.ZIP.name().toLowerCase());
			zip.setDebug(true);
			zip.setEliminar(false);
			zip.compactar(JsfBase.getRealPath(zipName), files, this.loadNotas(idEgreso));
  	  stream = new FileInputStream(new File(JsfBase.getRealPath(zipName)));
			regresar= new DefaultStreamedContent(stream, EFormatos.ZIP.getContent(), temporal.concat(".").concat(EFormatos.ZIP.name().toLowerCase()));		
		} // try // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	} // toZipFile
	
	private List<String> loadNotas(Long idEgreso) throws Exception {
		List<String> regresar    = new ArrayList<>();
		List<Entity> notas       = null;
		Map<String, Object>params= new HashMap<>();
		try {
			params.put("idEgreso", idEgreso);
			notas= DaoFactory.getInstance().toEntitySet("VistaEgresosDto", "observaciones", params, Constantes.SQL_TODOS_REGISTROS);
			if(notas!= null && !notas.isEmpty()){
				for(Entity nota: notas)
          if(nota.toString("observaciones")!= null)
					  regresar.add(nota.toString("observaciones").concat(", ").concat(Fecha.formatear(Fecha.FECHA_HORA_CORTA, nota.toTimestamp("registro"))).concat(", ").concat(nota.toString("persona")).concat("\n"));				
			} // if
      if(regresar.size()> 0)
				regresar.add(0, "Comentario, Registro, Usuario ".concat("\n"));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadNotas
	
	public void doLoadEstatus() {
		Entity seleccionado          = (Entity)this.attrs.get("seleccionado");
		Map<String, Object>params    = new HashMap<>();
		List<UISelectItem> allEstatus= null;
		try {
			params.put(Constantes.SQL_CONDICION, "id_egreso_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticEgresosEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
			this.attrs.put("allEstatus", allEstatus);
			this.attrs.put("estatus", allEstatus.get(0));
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
		Transaccion transaccion            = null;
		TcManticEgresosBitacoraDto bitacora= null;
		Entity seleccionado                = (Entity)this.attrs.get("seleccionado");
		try {
			bitacora= new TcManticEgresosBitacoraDto((String)this.attrs.get("justificacion"), new Long((String)this.attrs.get("estatus")), seleccionado.getKey(), JsfBase.getIdUsuario(), -1L);
			transaccion= new Transaccion(bitacora);
			if(transaccion.ejecutar(EAccion.COMPLEMENTAR))
				JsfBase.addMessage("Cambio estatus", "Se realizó el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally {
			this.attrs.put("justificacion", "");
		} // finally
	}	// doActualizaEstatus
  
	public String doNotaEntrada() {
		JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
		return "/Paginas/Mantic/Catalogos/Empresas/Saldar/filtro".concat(Constantes.REDIRECIONAR);
	}
  
	public void doRowToggle(ToggleEvent event) {
		try {
			this.attrs.put("seleccionado", (Entity) event.getData());
			if (!event.getVisibility().equals(Visibility.HIDDEN)) 
				this.doLoadDetalle();
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doRowToggle
  
  public void doLoadDetalle() {
    List<Columna> columns     = new ArrayList<>();
	  Map<String, Object> params= new HashMap<>();	
    try {
			Entity entity= (Entity)this.attrs.get("seleccionado");
			params.put("sortOrder", "order by tc_mantic_notas_entradas.registro desc");
			params.put("idEgreso", entity.toLong("idEgreso"));
      columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("fechaFactura", EFormatoDinamicos.FECHA_CORTA));    
      columns.add(new Columna("total", EFormatoDinamicos.MILES_CON_DECIMALES));    
			this.lazyModelDetalle = new FormatCustomLazy("VistaEgresosDto", "egresos", params, columns);
      UIBackingUtilities.resetDataTable();		
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
  
  public String doImportar() {
		String regresar= null;
		try {
      Entity row= (Entity)this.attrs.get("seleccionadoDetalle");
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Egresos/filtro");		
			JsfBase.setFlashAttribute("idNotaEntrada", row.toLong("idNotaEntrada"));
			regresar= "/Paginas/Mantic/Catalogos/Empresas/Saldar/importar".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
  }
  
  public String doAsociar() {
		String regresar= null;
		try {
      Entity row= (Entity)this.attrs.get("seleccionadoDetalle");
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Egresos/filtro");		
			JsfBase.setFlashAttribute("idNotaEntrada", row.toLong("idNotaEntrada"));
			regresar= "/Paginas/Mantic/Catalogos/Empresas/Saldar/asociar".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
  }
  
  public String doRelacionar() {
		String regresar= null;
		try {
      Entity row= (Entity)this.attrs.get("seleccionado");
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Egresos/filtro");		
			JsfBase.setFlashAttribute("idEgreso", row.toLong("idEgreso"));
			regresar= "asociar".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
  }
  
  public String toColor(Entity row) {
		return "";
	} 
 
  public String toOcultar(Entity row) {
		return row.toLong("idEliminado").equals(1L)? "janal-display-none": "";
	} 
  
  public void doLoadDocumentos(Entity row) {
    Map<String, Object> params = new HashMap<>();
    try {      
      this.attrs.put("seleccionadoDetalle", row);
      params.put("idEgreso", row.toLong("idEgreso"));      
      params.put("idTipoDocumento", -1L);      
      this.doLoadImportados("VistaEgresosDto", "exportar", params);   
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }

	public List<UISelectEntity> doCompleteProveedor(String codigo) {
 		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= new String(codigo).replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put("codigo", codigo);
			if(buscaPorCodigo)
        this.attrs.put("proveedores", UIEntity.build("TcManticProveedoresDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("proveedores", UIEntity.build("TcManticProveedoresDto", "porNombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("proveedores");
	}	
  
}
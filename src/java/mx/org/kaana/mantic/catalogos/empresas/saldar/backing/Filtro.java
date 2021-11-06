package mx.org.kaana.mantic.catalogos.empresas.saldar.backing;

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
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.backing.Saldos;
import mx.org.kaana.mantic.egresos.beans.ZipEgreso;
import mx.org.kaana.mantic.enums.ECuentasEgresos;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosEmpresasSaldarFiltro")
@ViewScoped
public class Filtro extends Saldos implements Serializable { 

  private static final long serialVersionUID = 8793667741599428834L;	
  private static final Log LOG = LogFactory.getLog(Filtro.class);
  
  private String path;

  public String getPath() {
    return path;
  }
 
  @PostConstruct
  @Override
  protected void init() {
    this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda"));     
    super.init();
    String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/archivos/");
  } // init

  @Override
  public void doLoad() {
    super.doLoad();
    this.attrs.put("importados", null);
    this.lazyPagosRealizados= null;
  } // doLoad

  @Override
	protected Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
	  UISelectEntity proveedor      = (UISelectEntity)this.attrs.get("proveedor");
		List<UISelectEntity>provedores= (List<UISelectEntity>)this.attrs.get("proveedores");
		if(!Cadena.isVacio(this.attrs.get("idNotaEntrada")) && !this.attrs.get("idNotaEntrada").toString().equals("-1")) {
  		sb.append("(tc_mantic_empresas_deudas.id_nota_entrada=").append(this.attrs.get("idNotaEntrada")).append(") and ");
      this.attrs.put("idNotaEntrada", null);
    } // if  
		if(!Cadena.isVacio(this.attrs.get("idEmpresaDeuda")) && !this.attrs.get("idEmpresaDeuda").toString().equals("-1"))
  		sb.append("(tc_mantic_empresas_deudas.id_empresa_deuda=").append(this.attrs.get("idEmpresaDeuda")).append(") and ");
		if(provedores!= null && proveedor!= null && provedores.indexOf(proveedor)>= 0) 
			sb.append("tc_mantic_proveedores.razon_social like '%").append(provedores.get(provedores.indexOf(proveedor)).toString("razonSocial")).append("%' and ");			
		else if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input")))
  		sb.append("tc_mantic_proveedores.razon_social like '%").append(JsfBase.getParametro("razonSocial_input")).append("%' and ");			
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_notas_entradas.consecutivo= ").append(this.attrs.get("consecutivo")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("factura")))
  		sb.append("(tc_mantic_notas_entradas.factura like '%").append(this.attrs.get("factura")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_empresas_deudas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_empresas_deudas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("almacen")))
  		sb.append("tc_mantic_almacenes.nombre like '%").append(this.attrs.get("almacen")).append("%' and ");
		if(!Cadena.isVacio(this.attrs.get("idCompleto")) && !this.attrs.get("idCompleto").toString().equals("-1"))
  		sb.append("(tc_mantic_empresas_deudas.id_completo=").append(this.attrs.get("idCompleto")).append(") and ");
    sb.append("tc_mantic_empresas_deudas.id_empresa_estatus= 4 and ");
    regresar.put("idEmpresa", this.attrs.get("idEmpresa").toString().equals("-1") ? this.attrs.get("allEmpresa") : this.attrs.get("idEmpresa"));			
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	}
 
  @Override
  public void doLoadDetalle() {
    List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {
  	  params = this.toPrepare();
			Entity entity= (Entity)this.attrs.get("seleccionado");
			params.put("sortOrder", "order by consecutivo desc");
			params.put("idProveedor", entity.toLong("idProveedor"));
			this.attrs.put("idProveedor", entity.toLong("idProveedor"));
      columns= new ArrayList<>();
      columns.add(new Columna("pagar", EFormatoDinamicos.MILES_CON_DECIMALES));    
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));    
      columns.add(new Columna("egresos", EFormatoDinamicos.MILES_SIN_DECIMALES));    
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));    
			this.lazyModelDetalle = new FormatCustomLazy("VistaEmpresasDto", "documentos", params, columns);
      UIBackingUtilities.resetDataTable("detalle");		
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
  
  @Override
	public String doImportar() {
		String regresar= null;
		try {
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Empresas/Saldar/filtro");		
			JsfBase.setFlashAttribute("idNotaEntrada",((Entity)this.attrs.get("seleccionadoDetalle")).toLong("idNotaEntrada"));
			JsfBase.setFlashAttribute("idEmpresaDeuda",((Entity)this.attrs.get("seleccionadoDetalle")).toLong("idEmpresaDeuda"));
			regresar= "importar".concat(Constantes.REDIRECIONAR);
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
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Empresas/Saldar/filtro");		
			JsfBase.setFlashAttribute("idNotaEntrada", ((Entity)this.attrs.get("seleccionadoDetalle")).toLong("idNotaEntrada"));
			JsfBase.setFlashAttribute("idEmpresaDeuda", ((Entity)this.attrs.get("seleccionadoDetalle")).toLong("idEmpresaDeuda"));
			regresar= "asociar".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	}
 
  @Override
  public String toColor(Entity row) {
		return row.toLong("idNotaTipo").equals(3L)? "janal-tr-purple": "";
	} 
 
  public String toOcultar(Entity row) {
		return row.toLong("idEliminado").equals(1L)? "janal-display-none": "";
	} 
 
  public void doLoadDocumentos(Entity row) {
    Map<String, Object> params = null;
    try {      
      this.attrs.put("seleccionadoDetalle", row);
      params = new HashMap<>();      
      params.put("idNotaEntrada", row.toLong("idNotaEntrada"));      
      params.put("idTipoDocumento", -1L);      
      this.doLoadImportados("VistaEmpresasDto", "completos", params);   
      this.doLoadDetallePagosRealizados(row);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
 
  public void doLoadDetallePagosRealizados(Entity row) {
    List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {
      params = new HashMap<>();      
      params.put("sortOrder", "order by tc_mantic_egresos_notas.registro desc");      
      params.put("idNotaEntrada", row.toLong("idNotaEntrada"));      
      columns= new ArrayList<>();
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));    
      columns.add(new Columna("importe", EFormatoDinamicos.MILES_CON_DECIMALES));      
			this.lazyPagosRealizados= new FormatCustomLazy("VistaEgresosDto", "notas", params, columns);
      UIBackingUtilities.resetDataTable("detallePagosRealizados");		
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
 
  public void doSeleccionar(Entity row) {
    this.attrs.put("seleccionadoDetalle", row);
  }
  
	public StreamedContent getDocumento() {
		StreamedContent regresar= null;		
		Entity seleccionado     = null;				
		try {			
			seleccionado= (Entity)this.attrs.get("seleccionadoDetalle");						
			regresar= this.toZipFile(this.toAllFiles(seleccionado), seleccionado.toString("consecutivo"), seleccionado.toLong("idNotaEntrada"));
		} // try 
		catch (Exception e) {
			Error.mensaje(e);
		} // catch		
    return regresar;		
	} // doDescargaArchivos
	
	private List<ZipEgreso> toAllFiles(Entity seleccionado) throws Exception{
		List<ZipEgreso> regresar  = null;
		List<String> namesFiles   = null;
		Map<String, Object> params= null;		
		List<Nombres> list        = null;
		ZipEgreso pivote          = null;
		try {			
			regresar= new ArrayList<>();
			params= new HashMap<>();
			params.put("idNotaEntrada", seleccionado.toLong("idNotaEntrada"));
			for(ECuentasEgresos item: ECuentasEgresos.values()) {
        if(Objects.equals(item.getGroup(), 2)) {
          list= (List<Nombres>)DaoFactory.getInstance().toEntitySet(Nombres.class, "VistaEmpresasDto", item.getIdXml(), params);
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
	} // toAllFiles
	
	private StreamedContent toZipFile(List<ZipEgreso> files, String descripcion, Long idNotaEntrada) {
		String zipName                 = null;		
		String name                    = null;		
		InputStream stream             = null;
		String temporal                = null;
		DefaultStreamedContent regresar= null;
		try {
			temporal= Archivo.toFormatNameFile("_N".concat(descripcion));
			Zip zip= new Zip();			
			name= "/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.ZIP.name()).concat("/").concat(temporal));
			zipName= name.concat(".").concat(EFormatos.ZIP.name().toLowerCase());
			zip.setDebug(true);
			zip.setEliminar(false);
			zip.compactar(JsfBase.getRealPath(zipName), files, this.loadNotas(idNotaEntrada));
  	  stream = new FileInputStream(new File(JsfBase.getRealPath(zipName)));
			regresar= new DefaultStreamedContent(stream, EFormatos.ZIP.getContent(), temporal.concat(".").concat(EFormatos.ZIP.name().toLowerCase()));		
		} // try // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	} // toZipFile
	
	private List<String> loadNotas(Long idNotaEntrada) throws Exception{
		List<String> regresar    = null;
		List<Entity> notas       = null;
		Map<String, Object>params= null;
		try {
			regresar= new ArrayList<>();
			params= new HashMap<>();
			params.put("idNotaEntrada", idNotaEntrada);
			notas= DaoFactory.getInstance().toEntitySet("VistaEmpresasDto", "observaciones", params, Constantes.SQL_TODOS_REGISTROS);
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
 
	public String doEstadoCuenta() {
		JsfBase.setFlashAttribute("idEgreso", this.attrs.get("idEgreso"));
		return "/Paginas/Mantic/Egresos/filtro".concat(Constantes.REDIRECIONAR);
	}
  
}
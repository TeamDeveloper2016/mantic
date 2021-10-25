package mx.org.kaana.mantic.catalogos.empresas.saldar.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.backing.Saldos;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    super.init();
    String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/documentos/");
  } // init

  @Override
  public void doLoad() {
    super.doLoad();
  } // doLoad

  @Override
	protected Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
	  UISelectEntity proveedor      = (UISelectEntity)this.attrs.get("proveedor");
		List<UISelectEntity>provedores= (List<UISelectEntity>)this.attrs.get("proveedores");
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
			regresar= "importar".concat(Constantes.REDIRECIONAR);
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
 
  public void doLoadDocumentos(Entity row) {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idNotaEntrada", row.toLong("idNotaEntrada"));      
      params.put("idTipoDocumento", -1L);      
      this.doLoadImportados("VistaNotasEntradasDto", "importados", params);      
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
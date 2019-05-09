package mx.org.kaana.mantic.sincronizar.backing;

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
import mx.org.kaana.libs.formato.Error;
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
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Named(value= "manticSincronizarFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final Log LOG=LogFactory.getLog(Filtro.class);			
	private static final long serialVersionUID=-8219581609413628789L;
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
		  this.doLoad();			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= this.toPrepare();
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));      
      params.put("sortOrder", "order by tc_mantic_ventas.registro desc");
      this.lazyModel = new FormatCustomLazy("VistaSincronizarDto", params, columns);
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
  } // doLoad

	protected Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		UISelectEntity estatus= (UISelectEntity) this.attrs.get("idFicticiaEstatus");
		if(!Cadena.isVacio(this.attrs.get("articulo")))
  		sb.append("(upper(tc_mantic_ventas_detalles.nombre) like upper('%").append(this.attrs.get("articulo")).append("%')) and ");
		if(!Cadena.isVacio(this.attrs.get("razonSocial")) && !this.attrs.get("razonSocial").toString().equals("-1"))
			sb.append("tc_mantic_clientes.id_cliente = ").append(((Entity)this.attrs.get("razonSocial")).getKey()).append(" and ");					
		else if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
			 	 sb.append("tc_mantic_clientes.razon_social regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");
		if(!Cadena.isVacio(this.attrs.get("idFicticia")) && !this.attrs.get("idFicticia").toString().equals("-1"))
  		sb.append("(tc_mantic_ventas.id_venta=").append(this.attrs.get("idFicticia")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_ventas.ticket like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("folio")))
  		sb.append("(tc_mantic_facturas.folio like '%").append(this.attrs.get("folio")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("((date_format(tc_mantic_facturas.timbrado, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') or (date_format(tc_mantic_facturas.cancelada, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("')) and ");			
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("((date_format(tc_mantic_facturas.timbrado, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') or (date_format(tc_mantic_facturas.cancelada, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("')) and ");			
		if(!Cadena.isVacio(this.attrs.get("montoInicio")))
		  sb.append("(tc_mantic_ventas.total>= ").append((Double)this.attrs.get("montoInicio")).append(") and ");			
		if(!Cadena.isVacio(this.attrs.get("montoTermino")))
		  sb.append("(tc_mantic_ventas.total<= ").append((Double)this.attrs.get("montoTermino")).append(") and ");			
		if(estatus!= null && !estatus.getKey().equals(-1L))
  		sb.append("(tc_mantic_ventas.id_venta_estatus= ").append(estatus.getKey()).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	} // toPrepare
	
  public String doAccion() {
		try {
			JsfBase.setFlashAttribute("accion", EAccion.PROCESAR);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Sincronizar/filtro");		
			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Sincronizar/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public String doIndividual() {
		try {
			JsfBase.setFlashAttribute("accion", EAccion.PROCESAR);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Sincronizar/filtro");		
			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Sincronizar/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public void doEliminar() {
		
  } // doEliminar	
	
	protected void toLoadCatalog() {
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
      this.attrs.put("sucursales", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			this.attrs.put("idEmpresa", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	} // toLoadCatalog
	
}
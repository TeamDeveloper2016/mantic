package mx.org.kaana.mantic.consultas.backing;

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
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.enums.EConsultas;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.ventas.comun.IBaseTicket;
import org.primefaces.context.RequestContext;

@Named(value= "manticConsultasFiltro")
@ViewScoped
public class Filtro extends IBaseTicket implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;
	private Reporte reporte;
	
	public Reporte getReporte() {
		return reporte;
	}	// getReporte
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta"));
      this.attrs.put("sortOrder", "order by tc_mantic_ventas.id_empresa, tc_mantic_ventas.ejercicio, tc_mantic_ventas.orden");
			toLoadCatalog();      
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
			params= new HashMap<>();
      columns = new ArrayList<>();
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      columns.add(new Columna("hora", EFormatoDinamicos.HORA_CORTA));      
			params.put(Constantes.SQL_CONDICION, this.attrs.get(Constantes.SQL_CONDICION));
			params.put("idEmpresa", !Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1") ? this.attrs.get("idEmpresa") : JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.lazyModel = new FormatCustomLazy("VistaConsultasDto", params, columns);
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

	public void doConsultar(String consulta){
		EConsultas tipoConsulta= null;
		StringBuilder sb       = null;
		try {
			sb= new StringBuilder();
			tipoConsulta= EConsultas.valueOf(consulta);
			switch(tipoConsulta){
				case VENDEDOR:
					if(!Cadena.isVacio(this.attrs.get("vendedor")))
						sb.append("concat(tc_mantic_personas.nombres, ' ', tc_mantic_personas.paterno, ' ', tc_mantic_personas.materno) like '%").append(this.attrs.get("vendedor")).append("%'");					
					break;
			} // switch			
			this.attrs.put(Constantes.SQL_CONDICION, sb.length()== 0 ? Constantes.SQL_VERDADERO : sb);
			doLoad();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doConsultar
	
  public String doAccion(String accion) {
    EAccion eaccion= null;
		String regresar= "/Paginas/Mantic/Ventas/accion".concat(Constantes.REDIRECIONAR); 
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Ventas/filtro");					
			JsfBase.setFlashAttribute("idVenta", eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
			if(eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)){
				if(((Entity)this.attrs.get("seleccionado")).toString("idManual").equals("1"))
					regresar= "/Paginas/Mantic/Ventas/express".concat(Constantes.REDIRECIONAR); 
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
  } // doAccion    

	protected Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		UISelectEntity estatus= (UISelectEntity) this.attrs.get("idVentaEstatus");
		if(!Cadena.isVacio(this.attrs.get("idVenta")) && !this.attrs.get("idVenta").toString().equals("-1"))
  		sb.append("(tc_mantic_ventas.id_venta=").append(this.attrs.get("idVenta")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_ventas.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_ventas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_ventas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("idCliente")) && !this.attrs.get("idCliente").toString().equals("-1"))
  		sb.append("(tc_mantic_clientes.id_cliente= ").append(this.attrs.get("idCliente")).append(") and ");
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
	}
	
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
      columns.add(new Columna("limiteCredito", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      this.attrs.put("clientes", (List<UISelectEntity>) UIEntity.build("VistaVentasDto", "clientes", params, columns));
			this.attrs.put("idCliente", new UISelectEntity("-1"));
			columns.remove(0);
			columns.remove(1);
      this.attrs.put("estatusFiltro", (List<UISelectEntity>) UIEntity.build("TcManticVentasEstatusDto", "row", params, columns));
			this.attrs.put("idVentaEstatus", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public void doReporte() throws Exception {
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
		try{				
			reporteSeleccion= EReportes.ORDEN_COMPRA;
			this.reporte= JsfBase.toReporte();
			params= new HashMap<>();
			params.put("idVenta", ((Entity)this.attrs.get("seleccionado")).getKey());			
			parametros= new HashMap<>();
			parametros.put("REPORTE_EMPRESA", JsfBase.getAutentifica().getEmpresa().getNombreCorto());
		  parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
			parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
			parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
			this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
			doVerificarReporte();
			this.reporte.doAceptar();			
		} // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
    } // catch	
	} // doReporte
	
	public void doVerificarReporte() {
		RequestContext rc= RequestContext.getCurrentInstance();
		if(this.reporte.getTotal()> 0L)
			rc.execute("start(" + this.reporte.getTotal() + ")");		
		else{
			rc.execute("generalHide()");		
			JsfBase.addMessage("Generar reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
		} // else
	} // doVerificarReporte		
}

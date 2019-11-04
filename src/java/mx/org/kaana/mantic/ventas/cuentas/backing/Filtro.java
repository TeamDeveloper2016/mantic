package mx.org.kaana.mantic.ventas.cuentas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
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
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.EReportes;
import org.primefaces.context.RequestContext;

@Named(value= "manticVentasCuentasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;
	private Reporte reporte;
	
	public void doReporte(String nombre) throws Exception{
    Parametros comunes = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
		try{		
      params= toPrepare();
      params.put("sortOrder", "order by tc_mantic_ventas.id_empresa, tc_mantic_ventas.ejercicio, tc_mantic_ventas.orden");
			reporteSeleccion= EReportes.valueOf(nombre);
      if(reporteSeleccion.equals(EReportes.CUENTAS_DETALLE)){
        seleccionado = ((Entity)this.attrs.get("seleccionado"));
        params.put("letra", "");
        params.put("idCliente", seleccionado.get("id_cliente"));
        params.put("cliente", seleccionado.get("cliente"));
        params.put("idVenta", seleccionado.toLong("idKey"));
        params.put("campoConsecutivo", "consecutivo");
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), -1L, -1L , seleccionado.toLong("idCliente"));
      }
      else
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
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
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta"));
      this.attrs.put("sortOrder", "order by tc_mantic_ventas.id_empresa, tc_mantic_ventas.ejercicio, tc_mantic_ventas.orden");
			toLoadCatalog();
			loadEstatusVentas();
      if(this.attrs.get("idVenta")!= null) 
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
		Map<String, Object> params= toPrepare();
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));      
      this.lazyModel = new FormatCustomLazy("VistaVentasDto", params, columns);
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

  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Ventas/Cuentas/filtro");		
			JsfBase.setFlashAttribute("idVenta", eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Ventas/Cuentas/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion(new TcManticVentasDto(seleccionado.getKey()), this.attrs.get("justificacionEliminar").toString());
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "La cuenta se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar la cuenta.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar

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
		else
			sb.append(this.attrs.get("condicionEstatus").toString().concat(" and "));
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
    Map<String, Object> params= null;
    try {
			params = new HashMap<>();
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
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	private void loadEstatusVentas(){		
		List<Columna> columns     = null;
		Map<String, Object> params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, toCondicionEstatus());
			columns= new ArrayList<>();
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			this.attrs.put("estatusFiltro", (List<UISelectEntity>) UIEntity.build("TcManticVentasEstatusDto", "row", params, columns));
			this.attrs.put("idVentaEstatus", new UISelectEntity("-1"));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadEstatusVentas
	
	private String toCondicionEstatus(){
		StringBuilder regresar = null;
		String estatusAppend   = "";
		String condicionEstatus= "";
		try {
			regresar= new StringBuilder();
			regresar.append("tc_mantic_ventas_estatus.id_venta_estatus in (");
			for(EEstatusVentas estatus : EEstatusVentas.values()){
				if(EEstatusVentas.ELABORADA.equals(estatus) || EEstatusVentas.ABIERTA.equals(estatus))
					estatusAppend= estatusAppend.concat(estatus.getIdEstatusVenta().toString()).concat(",");
			} // for
			regresar.append(estatusAppend.substring(0, estatusAppend.length()-1));
			regresar.append(")");
			condicionEstatus= regresar.toString().concat(" and tc_mantic_ventas.vigencia is null");
			this.attrs.put("condicionEstatus", condicionEstatus);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicionEstatus

	public void doLoadEstatus(){
		Entity seleccionado          = null;
		Map<String, Object>params    = null;
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_venta_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(") and id_venta_estatus not in (").concat(EEstatusVentas.COTIZACION.getIdEstatusVenta().toString()).concat(")"));
			allEstatus= UISelect.build("TcManticVentasEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
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
		Transaccion transaccion           = null;
		TcManticVentasBitacoraDto bitacora= null;
		Entity seleccionado               = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			TcManticVentasDto orden= (TcManticVentasDto)DaoFactory.getInstance().findById(TcManticVentasDto.class, seleccionado.getKey());
			bitacora= new TcManticVentasBitacoraDto(-1L, (String)this.attrs.get("justificacion"), JsfBase.getIdUsuario(), seleccionado.getKey(), Long.valueOf(this.attrs.get("estatus").toString()), orden.getConsecutivo(), orden.getTotal());
			transaccion= new Transaccion(bitacora);
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
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
}

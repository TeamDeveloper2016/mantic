package mx.org.kaana.mantic.explorar.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.enums.EEstatusPedidos;
import mx.org.kaana.mantic.explorar.comun.Pedido;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/05/2018
 *@time 02:19:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticExplorarListado")
@ViewScoped
public class Listado extends Pedido implements Serializable {
	
	private static final Log LOG              = LogFactory.getLog(Listado.class);
	private static final long serialVersionUID= 7893820978859628158L;
	private FormatLazyModel lazyModel;
	private FormatLazyModel detalle;	

	public FormatLazyModel getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(FormatLazyModel lazyModel) {
		this.lazyModel = lazyModel;
	}	
	
	public FormatLazyModel getDetalle() {
		return detalle;
	}

	public void setDetallePagos(FormatLazyModel detalle) {
		this.detalle = detalle;
	}
	
	@Override
	@PostConstruct
	protected void init() {    
		Calendar fechaInicio= null;
		try {		
			super.initPedido();
			fechaInicio= Calendar.getInstance(); 
			fechaInicio.set(Calendar.DAY_OF_YEAR, fechaInicio.get(Calendar.DAY_OF_YEAR)- 30); 			
			this.attrs.put("fechaApartir", new Date(fechaInicio.getTimeInMillis())); 
			this.attrs.put("fechaHasta", new Date(Calendar.getInstance().getTimeInMillis()));
			this.attrs.put("consecutivo", "");
			this.attrs.put("buscaPorCodigo", false);
			loadEstatus();
      doLoad();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);		
		} // catch				
	} // init
	
	private void loadEstatus(){
		List<UISelectItem> allEstatus= null;
		try {
			allEstatus= new ArrayList<>();
			for(EEstatusPedidos estatus: EEstatusPedidos.values())
				allEstatus.add(new UISelectItem(estatus.getIdEstatus(), estatus.name()));
			allEstatus.add(0, new UISelectItem(0L, "TODOS"));
			this.attrs.put("allEstatus", allEstatus);
			this.attrs.put("estatus", UIBackingUtilities.toFirstKeySelectItem(allEstatus));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadEstatus
		
	public void doLoad(){
		List<Columna> columns     = null;
		Map<String, Object> params= null;
		try {
			params= toPrepare();
			columns= new ArrayList<>();
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));									
      columns.add(new Columna("subTotal", EFormatoDinamicos.MONEDA_CON_DECIMALES));									
      columns.add(new Columna("impuestos", EFormatoDinamicos.MONEDA_CON_DECIMALES));									
      columns.add(new Columna("excedentes", EFormatoDinamicos.MONEDA_CON_DECIMALES));									
      columns.add(new Columna("descuentos", EFormatoDinamicos.MONEDA_CON_DECIMALES));									
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));									
			this.lazyModel= new FormatLazyModel("VistaPedidosDto", "pedidosCondicion", params, columns);
			UIBackingUtilities.resetDataTable();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(columns);
		} // finally
	} // doLoad
	
	private Map<String, Object> toPrepare(){
		Map<String, Object>regresar  = null;
		StringBuilder condicion      = null;
		List<UISelectItem> allEstatus= null;
		UISelectItem estatus         = null;
		Date inicio                  = null;
		Date fin                     = null;
		try {
			allEstatus= (List<UISelectItem>) this.attrs.get("allEstatus");
			estatus= allEstatus.get(allEstatus.indexOf(new UISelectItem(Long.valueOf(this.attrs.get("estatus").toString()))));
			condicion= new StringBuilder();
			inicio= (Date) this.attrs.get("fechaApartir");			
		  fin= (Date) this.attrs.get("fechaHasta");			
			condicion.append("date_format(tc_mantic_pedidos.registro, '%Y%m%d') >= ").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, inicio)).append(" and ");
			condicion.append("date_format(tc_mantic_pedidos.registro, '%Y%m%d') <= ").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, fin)).append(" and ");
			if(Numero.getLong(estatus.getValue().toString())> 0L)
				condicion.append(" tc_mantic_pedidos.id_estatus=").append(estatus.getValue()).append(" and ");
			if(!Cadena.isVacio(this.attrs.get("folio")))
				condicion.append(" tc_mantic_pedidos.consecutivo=").append(this.attrs.get("consecutivo")).append(" and ");
			if(!Cadena.isVacio(this.attrs.get("importe")) && !this.attrs.get("importe").toString().equals("0.00"))												
				condicion.append("tc_mantic_pedidos.total like '%").append(Cadena.eliminaCaracter(this.attrs.get("importe").toString(), ',')).append("%' and ");			
			if(this.attrs.get("producto")!= null && !((Entity)this.attrs.get("producto")).getKey().equals(-1L))
				condicion.append("tc_mantic_pedidos_detalles.id_articulo=").append(((Entity)this.attrs.get("producto")).getKey()).append(" and ");									
			regresar= new HashMap<>();
			regresar.put(Constantes.SQL_CONDICION, Cadena.isVacio(condicion) ? Constantes.SQL_VERDADERO : condicion.substring(0, condicion.length()-4));			
			regresar.put("idUsuario", JsfBase.getIdUsuario());			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPrepare
	
	public void onRowToggle(ToggleEvent event) {
		try {
			this.attrs.put("registroSeleccionado", (Entity) event.getData());
			if (!event.getVisibility().equals(Visibility.HIDDEN)) 
				loadDetalle();			
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // onRowToggle
	
	private void loadDetalle() throws Exception{
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {  	  
			params= new HashMap<>();
			params.put("idPedido", ((Entity)this.attrs.get("registroSeleccionado")).getKey());			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));			
			this.detalle= new FormatLazyModel("VistaPedidosDto", "pedido", params, columns);
      UIBackingUtilities.resetDataTable("tablaDetalle");		
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally			
	} // loadHistorialPagos
	
	public List<UISelectEntity> doCompleteProducto(String query) {
		this.attrs.put("codigoProducto", query);
    this.doUpdateArticulos();		
		return (List<UISelectEntity>)this.attrs.get("articulosProducto");
	}
	
	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String)this.attrs.get("codigoProducto"); 
			if(!Cadena.isVacio(search)) {
				buscaPorCodigo= (((boolean)this.attrs.get("buscaPorCodigo")) && !search.startsWith(".")) || (!((boolean)this.attrs.get("buscaPorCodigo")) && search.startsWith("."));  			
				if(search.startsWith("."))
					search= search.trim().substring(1);				
				search= search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);	
			if(buscaPorCodigo)        
        this.attrs.put("articulosProducto", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
			else
        this.attrs.put("articulosProducto", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	} // doUpdateArticulos
	
	public String doBusqueda() {
		String regresar               = null;
		String criterio               = null;
		List<UISelectEntity> articulos= null;
		try {			
			if(this.attrs.get("nombre")!= null && ((UISelectEntity)this.attrs.get("nombre")).getKey()> 0L){
				articulos= (List<UISelectEntity>) this.attrs.get("articulosFiltro");				
				JsfBase.setFlashAttribute("articulo", ((Entity)articulos.get(articulos.indexOf((UISelectEntity)this.attrs.get("nombre")))));
				regresar= "accion".concat(Constantes.REDIRECIONAR);
			} // if
  		else if(!Cadena.isVacio(this.attrs.get("nombreHidden"))){ 
				criterio= this.attrs.get("nombreHidden").toString();		  			
				JsfBase.setFlashAttribute("criterio", criterio!= null ? criterio.toUpperCase() : criterio);
				regresar= "filtro".concat(Constantes.REDIRECIONAR);
			} // else
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doBusqueda
}
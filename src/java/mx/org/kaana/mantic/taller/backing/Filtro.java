package mx.org.kaana.mantic.taller.backing;

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
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticServiciosBitacoraDto;
import mx.org.kaana.mantic.enums.ETipoArticulo;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;
import mx.org.kaana.mantic.taller.reglas.Transaccion;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;

@Named(value = "manticTallerFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("herramienta", "");
			this.attrs.put("cliente", "");
			this.attrs.put("consecutivo", "");
      this.attrs.put("sortOrder", "order by tc_mantic_servicios.registro desc");
      this.attrs.put("idServicio", JsfBase.getFlashAttribute("idServicio"));
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.loadEstatusServicios();
			loadTiposArticulos();
      if(this.attrs.get("idServicio")!= null) 
			  this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadTiposArticulos(){
		List<UISelectItem> tiposArticulos= null;
		StringBuilder tipos              = null;
		tiposArticulos= new ArrayList<>();
		tipos= new StringBuilder("");
		for(ETipoArticulo tipoArticulo: ETipoArticulo.values()){
			tiposArticulos.add(new UISelectItem(tipoArticulo.getIdTipoArticulo(), tipoArticulo.name()));
			tipos.append(tipoArticulo.getIdTipoArticulo()).append(",");
		} // for
		tiposArticulos.add(0, new UISelectItem(tipos.substring(0, tipos.length()-1), "TODOS"));
		this.attrs.put("tiposArticulos", tiposArticulos);
		this.attrs.put("tipoArticulo", UIBackingUtilities.toFirstKeySelectItem(tiposArticulos));
	} // loadTiposArticulos
	
  @Override
  public void doLoad() {
    List<Columna> campos     = null;
		Map<String, Object>params= null;
    try {
      campos = new ArrayList<>();			
      campos.add(new Columna("herramienta", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("consecutivo", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("marca", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("modelo", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("fechaEstimada", EFormatoDinamicos.FECHA_CORTA));      
      campos.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      campos.add(new Columna("total", EFormatoDinamicos.NUMERO_CON_DECIMALES));     		
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, toPrepare());
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put("sortOrder", this.attrs.get("sortOrder"));
      this.lazyModel = new FormatCustomLazy("VistaTallerServiciosDto", "principal", params, campos);
      UIBackingUtilities.resetDataTable();
			this.attrs.put("idServicio", null);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(campos);
      Methods.clean(params);
    } // finally		
  } // doLoad

	private String toPrepare(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder("tc_mantic_servicios_estatus.id_servicio_estatus in (");
			regresar.append(this.attrs.get("estatus")).append(") and ");
			regresar.append("(tc_mantic_articulos.id_articulo_tipo in (").append(this.attrs.get("tipoArticulo")).append(") or tc_mantic_articulos.id_articulo_tipo is null) and ");
			if(this.attrs.get("consecutivo")!= null && !Cadena.isVacio(this.attrs.get("consecutivo")))
				regresar.append("tc_mantic_servicios.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%' and ");			
			if(this.attrs.get("herramienta")!= null && !Cadena.isVacio(this.attrs.get("herramienta")) && this.attrs.get("tipoArticulo").toString().equals(ETipoArticulo.REFACCION.getIdTipoArticulo().toString()))
				regresar.append("upper(tc_mantic_articulos.descripcion) like upper('%").append(this.attrs.get("herramienta")).append("%') and ");
			if(this.attrs.get("cliente")!= null && !Cadena.isVacio(this.attrs.get("cliente")))
				regresar.append("upper(tc_mantic_clientes.razon_social) like upper('%").append(this.attrs.get("cliente")).append("%') and ");			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.substring(0, regresar.length()-4);
	} // tiPrepare
	
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idServicio", (eaccion.equals(EAccion.MODIFICAR)||eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public void doEliminar() {
		Transaccion transaccion  = null;
		Entity seleccionado      = null;
		RegistroServicio registro= null;
    try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			registro= new RegistroServicio();
			registro.setIdServicio(seleccionado.getKey());
			transaccion= new Transaccion(registro);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar servicio", "El servicio de taller se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Eliminar servicio", "Ocurrió un error al eliminar el servicio de taller.", ETipoMensaje.ERROR);								
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminar
	
	private void loadEstatusServicios(){
		List<UISelectItem> allEstatus= null;
		Map<String, Object>params    = null;
		List<String> campos          = null;
		String all                   = ""; 
		try {
			params= new HashMap<>();
			campos= new ArrayList<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos.add("nombre");
			allEstatus= UISelect.build("TcManticServiciosEstatusDto", "row", params, campos, " ", EFormatoDinamicos.MAYUSCULAS);
			for(UISelectItem record: allEstatus)
				all= all.concat(record.getValue().toString()).concat(",");
			allEstatus.add(0, new UISelectItem(all.substring(0, all.length()-1), "TODOS"));
			this.attrs.put("allEstatus", allEstatus);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(campos);
		} // finally
	} // loadEstatusServicios
	
	public void doLoadEstatus(){
		Entity seleccionado          = null;
		Map<String, Object>params    = null;
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_servicio_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticServiciosEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
			this.attrs.put("allEstatusAsigna", allEstatus);
			this.attrs.put("estatusAsigna", allEstatus.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doLoadEstatus
	
	public void doActualizarEstatus(){
		Transaccion transaccion              = null;
		TcManticServiciosBitacoraDto bitacora= null;
		Entity seleccionado                  = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			bitacora= new TcManticServiciosBitacoraDto();
			bitacora.setIdServicio(seleccionado.getKey());
			bitacora.setIdServicioEstatus(Long.valueOf(this.attrs.get("estatusAsigna").toString()));
			bitacora.setSeguimiento((String) this.attrs.get("justificacion"));
			bitacora.setIdUsuario(JsfBase.getIdUsuario());
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
		finally{
			this.attrs.put("justificacion", "");
		} // finally
	}	// doActualizaEstatus
	
	public String doMovimientos() {
		JsfBase.setFlashAttribute("tipo", ETipoMovimiento.SERVICIOS);
		JsfBase.setFlashAttribute(ETipoMovimiento.SERVICIOS.getIdKey(), ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Taller/filtro");
		return "/Paginas/Mantic/Compras/Ordenes/movimientos".concat(Constantes.REDIRECIONAR);
	}
	
	public String doRefacciones() {
  	JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);		
		JsfBase.setFlashAttribute(ETipoMovimiento.SERVICIOS.getIdKey(), ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Taller/filtro");
		return "/Paginas/Mantic/Taller/detalle".concat(Constantes.REDIRECIONAR);
	}
  
  public String doPagarServicio() {  
    mx.org.kaana.mantic.ventas.reglas.Transaccion transaccion= null;
    TicketVenta ticketVenta   = null;    
    List<Articulo> articulos  = null;    
    Map<String, Object> params= null;
		Entity seleccionado       = null;
		String regresar           = null;
    try {
			regresar= "/Paginas/Mantic/Taller/filtro";			
      seleccionado= ((Entity)this.attrs.get("seleccionado"));
			params= new HashMap<>();
			if(seleccionado.get("idVenta").getData()!= null && seleccionado.toLong("idVenta")> 0L){
				params.put("idVenta", seleccionado.toLong("idVenta"));
				ticketVenta= (TicketVenta) DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params);
			} // if
			else{
				ticketVenta= new TicketVenta();
				ticketVenta.setIdEmpresa(seleccionado.toLong("idEmpresa"));				
				params.put("idEmpresa",seleccionado.toLong("idEmpresa"));
				ticketVenta.setIdCliente(seleccionado.toLong("idCliente"));
				Value value= DaoFactory.getInstance().toField("TcManticAlmacenesDto", "almacen", params, "idAlmacen");
				if(value.getData()!= null)
					ticketVenta.setIdAlmacen(value.toLong());
				ticketVenta.setIdUsuario(JsfBase.getIdUsuario());
				ticketVenta.setDescuentos(seleccionado.toDouble("descuentos"));
				ticketVenta.setImpuestos(seleccionado.toDouble("impuestos") != null ? seleccionado.toDouble("impuestos"): 0.0D);
				ticketVenta.setSubTotal(seleccionado.toDouble("subTotal") != null ? seleccionado.toDouble("subTotal"):0.0D);
				ticketVenta.setTotal(seleccionado.toDouble("total") != null ? seleccionado.toDouble("total"):0.0D);
			} // else
      articulos= toListArticulos(seleccionado, ticketVenta);
			transaccion= new mx.org.kaana.mantic.ventas.reglas.Transaccion(ticketVenta, articulos, seleccionado.getKey());
			if (transaccion.ejecutar(EAccion.LISTAR)) {
        doEstatusCaja(seleccionado, transaccion.getOrden().getIdVenta());
        JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
        JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Taller/filtro");		
        JsfBase.setFlashAttribute("idVenta", transaccion.getOrden().getIdVenta());
        JsfBase.setFlashAttribute("fechaRegistro", new Date(Fecha.getFechaCalendar( seleccionado.toString("registro")).getTimeInMillis()));        
        regresar= "/Paginas/Mantic/Ventas/Caja/accion".concat(Constantes.REDIRECIONAR);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la cuenta de venta.", ETipoMensaje.ERROR);        
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
			Methods.clean(params);
		} // finally
    return  regresar;
  } // doPagarServicio 
  
  public List<Articulo> toListArticulos(Entity seleccionado, TicketVenta venta) throws Exception {
		Long idOrdenDetalle= new Long((int)(Math.random()*10000));
		Map<String, Object> params= null;
    List<Articulo> articulos = new ArrayList();
    List<Entity> articulosServicio = null;
		try {
			params=new HashMap<>();
			params.put("idServicio", seleccionado.getKey());
			articulosServicio= DaoFactory.getInstance().toEntitySet("VistaServiciosDetallesDto", "articulosDetalle", params);
      for(Entity articuloServicio: articulosServicio){
        Articulo item= new Articulo(
				false,
				venta.getTipoDeCambio(),
				articuloServicio.toString("nombre"), 
				articuloServicio.toString("codigo")== null? "": articuloServicio.toString("codigo"),
				articuloServicio.toDouble("costo"),
				articuloServicio.toString("descuento")== null? "": articuloServicio.toString("descuento"),
				-1L,
				venta.getExtras(), 
				articuloServicio.toDouble("importe"),
				articuloServicio.toString("propio"),
				articuloServicio.toDouble("iva"), 
				articuloServicio.toDouble("impuestos"), 
				articuloServicio.toDouble("subTotal"), 
				articuloServicio.toDouble("cantidad"), 
				-1* idOrdenDetalle, 
				articuloServicio.toLong("idArticulo"), 
				0D,
				-1L,
				false,//this.attrs.get("ultimo")!= null,
				false,//this.attrs.get("solicitado")!= null,
				articuloServicio.toDouble("stock")== null? 0D: articuloServicio.toDouble("stock"),
				0D,
				articuloServicio.toString("sat"),
				articuloServicio.toString("unidadMedida"),
				1L
        );
        articulos.add(item);
      }
		} // try
		finally {
			Methods.clean(params);
		} // finally
    return articulos;
	}
  
  public void doEstatusCaja(Entity seleccionado, Long idVenta){
		Transaccion transaccion              = null;
		TcManticServiciosBitacoraDto bitacora= null;
		try {
			bitacora= new TcManticServiciosBitacoraDto();
			bitacora.setIdServicio(seleccionado.getKey());
			bitacora.setIdServicioEstatus(8L);
			bitacora.setSeguimiento("SE AGREGÓ LA VENTA CON ID: "+ idVenta);
			bitacora.setIdUsuario(JsfBase.getIdUsuario());
			transaccion= new Transaccion(bitacora);
			transaccion.ejecutar(EAccion.JUSTIFICAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	}	// doEstatusCaja	
}
package mx.org.kaana.mantic.taller.backing;

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
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.IBaseAttachment;
import mx.org.kaana.mantic.db.dto.TcManticServiciosBitacoraDto;
import mx.org.kaana.mantic.enums.EEstatusServicios;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoArticulo;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.Correo;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;
import mx.org.kaana.mantic.taller.reglas.Transaccion;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;

@Named(value = "manticTallerFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  private static final Log LOG = LogFactory.getLog(Filtro.class);

	private List<Correo> correos;
	private List<Correo> selectedCorreos;	
	private Correo correo;
	private Reporte reporte;
  
	public List<Correo> getCorreos() {
		return correos;
	}

	public void setCorreos(List<Correo> correos) {
		this.correos = correos;
	}	
	
	public List<Correo> getSelectedCorreos() {
		return selectedCorreos;
	}

	public void setSelectedCorreos(List<Correo> selectedCorreos) {
		this.selectedCorreos = selectedCorreos;
	}	

	public Correo getCorreo() {
		return correo;
	}

	public void setCorreo(Correo correo) {
		this.correo = correo;
	}
  
  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("herramienta", "");
			this.attrs.put("cliente", "");
			this.attrs.put("consecutivo", "");
      this.attrs.put("idServicio", JsfBase.getFlashAttribute("idServicio"));
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.loadEstatusServicios();
			this.loadTiposArticulos();
      if(this.attrs.get("idServicio")!= null) 
			  this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadTiposArticulos() {
		List<UISelectItem> tiposArticulos= null;
		StringBuilder tipos              = null;
		tiposArticulos= new ArrayList<>();
		tipos= new StringBuilder("");
		for(ETipoArticulo tipoArticulo: ETipoArticulo.values()) {
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
			params= this.toPrepare();
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put("sortOrder", "order by registro desc");
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

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb            = new StringBuilder();
		try {
			if(!Cadena.isVacio(this.attrs.get("estatus")))
  			sb.append("tc_mantic_servicios_estatus.id_servicio_estatus in (").append(this.attrs.get("estatus")).append(") and ");
			if(!Cadena.isVacio(this.attrs.get("tipoArticulo")))
  			sb.append("(tc_mantic_articulos.id_articulo_tipo in (").append(this.attrs.get("tipoArticulo")).append(") or tc_mantic_articulos.id_articulo_tipo is null) and ");
			if(!Cadena.isVacio(this.attrs.get("consecutivo")))
				sb.append("tc_mantic_servicios.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%' and ");			
			if(this.attrs.get("herramienta")!= null && !Cadena.isVacio(this.attrs.get("herramienta")) && this.attrs.get("tipoArticulo").toString().equals(ETipoArticulo.REFACCION.getIdTipoArticulo().toString()))
				sb.append("upper(tc_mantic_articulos.descripcion) like upper('%").append(this.attrs.get("herramienta")).append("%') and ");
			if(this.attrs.get("cliente")!= null && !Cadena.isVacio(this.attrs.get("cliente")))
				sb.append("upper(tc_mantic_clientes.razon_social) like upper('%").append(this.attrs.get("cliente")).append("%') and ");			
      if(this.attrs.get("idServicio")!= null) 
				sb.append("tc_mantic_servicios.id_servicio= ").append(this.attrs.get("idServicio")).append(" and ");
      regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPrepare
	
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
	
	private void loadEstatusServicios() {
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
	
	public void doLoadEstatus() {
		Entity seleccionado          = null;
		Map<String, Object>params    = null;
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
      if(Objects.equals(seleccionado.toLong("idGarantia"), 1L) && Objects.equals(seleccionado.toLong("idServicioEstatus"), EEstatusServicios.REPARADO.getIdEstatusServicio()))
        params.put(Constantes.SQL_CONDICION, "id_servicio_estatus in ("+ EEstatusServicios.ENTREGADO.getIdEstatusServicio()+ ")");
      else
        if(Objects.equals(seleccionado.toLong("idServicioEstatus"), EEstatusServicios.ELABORADA.getIdEstatusServicio()) && seleccionado.toLong("refacciones")<= 0)
          params.put(Constantes.SQL_CONDICION, "id_servicio_estatus in (7)");
        else
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
	
	public void doActualizarEstatus() {
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
			if(transaccion.ejecutar(EAccion.JUSTIFICAR)) {
        switch(bitacora.getIdServicioEstatus().intValue()) {
          case 4: // EEstatusServicios.EN_REPARACION
            if(!Cadena.isVacio(transaccion.getOrdenCompra()))
    				  JsfBase.addMessage("Orde de compra", "Se generó la orden de compra [".concat(transaccion.getOrdenCompra().getConsecutivo()).concat("] por un importe de $ ")+ transaccion.getOrdenCompra().getTotal(), ETipoMensaje.INFORMACION);
            else 
    				  JsfBase.addMessage("Cambio estatus", "Se realizó el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
            break;
          case 8: // EEstatusServicios.EN_CAJA
    				JsfBase.addMessage("Ticket venta", "Se generó la cuenta de venta ["+ transaccion.getVenta().getConsecutivo()+ "] por un importe de $ "+ transaccion.getVenta().getTotal(), ETipoMensaje.INFORMACION);
            break;
          default:
    				JsfBase.addMessage("Cambio estatus", "Se realizó el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
            break;
        } // switch 
      }
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
			if(seleccionado.get("idVenta").getData()!= null && seleccionado.toLong("idVenta")> 0L) {
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
        JsfBase.setFlashAttribute("fechaRegistro", new Date(Fecha.getFechaCalendar(seleccionado.toString("registro")).getTimeInMillis()));        
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
      for(Entity articuloServicio: articulosServicio) {
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
  
  public void doEstatusCaja(Entity seleccionado, Long idVenta) {
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
  
	public void doReporte(String nombre) throws Exception {
		doReporte(nombre, false);
	} // doReporte
	
	private void doReporte(String nombre, boolean email) throws Exception {
		Parametros comunes           = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
		try {
      params= this.toPrepare();	
      seleccionado = ((Entity)this.attrs.get("seleccionado"));
      params.put("idServicio", seleccionado.toLong("idServicio"));	
      reporteSeleccion= EReportes.valueOf(nombre);
      comunes= new Parametros(seleccionado.toLong("idEmpresa"), seleccionado.toLong("idAlmacen"), -1L, seleccionado.toLong("idCliente")== null? -1L: seleccionado.toLong("idCliente"));
      if(seleccionado.toLong("idCliente")== null) {
        comunes.getComunes().put("REPORTE_CLIENTE", seleccionado.toString("cliente")!= null? seleccionado.toString("cliente"): " ");
        comunes.getComunes().put("REPORTE_CLIENTE_TELEFONOS", seleccionado.toString("telefonos")!= null? seleccionado.toString("telefonos"): " ");
        comunes.getComunes().put("REPORTE_CLIENTE_EMAILS", seleccionado.toString("correos")!= null? seleccionado.toString("correos"): " ");
      } // if
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));					
			if(email) 
        this.reporte.doAceptarSimple();			
			else {				
				this.doVerificarReporte();
				this.attrs.put("reporteName", this.reporte.getArchivo());
				this.reporte.doAceptar();			
			} // else		      
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // doReporte  
  
	public void doVerificarReporte() {
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L)
			rc.execute("start(" + this.reporte.getTotal() + ")");		
		else{
			rc.execute("generalHide()");		
			JsfBase.addMessage("Generar reporte","No se encontraron registros para el reporte", ETipoMensaje.ALERTA);
		} // else
	} // doVerificarReporte		
 
	public void doLoadCorreos() {
		Entity seleccionado               = null;
		MotorBusquedaCatalogos motor      = null; 
		List<ClienteTipoContacto>contactos= null;
		Correo correoAdd                  = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			motor= new MotorBusqueda(-1L, seleccionado.toLong("idCliente")== null? -1L: seleccionado.toLong("idCliente"));
			contactos= motor.toClientesTipoContacto();
			setCorreos(new ArrayList<>());
			setSelectedCorreos(new ArrayList<>());			
			for(ClienteTipoContacto contacto: contactos){
				if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey())) {
					correoAdd= new Correo(contacto.getIdClienteTipoContacto(), contacto.getValor());
					getCorreos().add(correoAdd);		
					getSelectedCorreos().add(correoAdd);
				} // if
			} // for
      if(!Cadena.isVacio(seleccionado.toString("correos"))) {
				correoAdd= new Correo(0L, seleccionado.toString("correos"));
				getCorreos().add(correoAdd);		
				getSelectedCorreos().add(correoAdd);
      } // if
			getCorreos().add(new Correo(-1L, ""));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doLoadCorreos
 
  public void doSendMail() {
		StringBuilder sb= new StringBuilder("");
		if(this.selectedCorreos!= null && !this.selectedCorreos.isEmpty()) {
			for(Correo mail: this.selectedCorreos) {
				if(!Cadena.isVacio(mail.getDescripcion()))
					sb.append(mail.getDescripcion()).append(", ");
			} // for
		} // if
		Map<String, Object> params= new HashMap<>();
		//String[] emails= {"jimenez76@yahoo.com", (sb.length()> 0? sb.substring(0, sb.length()- 2): "")};
		String[] emails= {(sb.length()> 0? sb.substring(0, sb.length()- 2): "")};
		List<Attachment> files= new ArrayList<>(); 
		try {
			Entity seleccionado= (Entity)this.attrs.get("seleccionado");
			params.put("header", "...");
			params.put("footer", "...");
			params.put("empresa", JsfBase.getAutentifica().getEmpresa().getNombre());
			params.put("tipo", "Orden de reparación");			
			params.put("razonSocial", seleccionado.toString("razonSocial"));
			params.put("correo", ECorreos.ORDENES_TALLER.getEmail());			
			this.doReporte("ORDEN_TALLER", true);
			Attachment attachments= new Attachment(this.reporte.getNombre(), Boolean.FALSE);
			files.add(attachments);
			files.add(new Attachment("logo", ECorreos.ORDENES_TALLER.getImages().concat("logo.png"), Boolean.TRUE));
			params.put("attach", attachments.getId());
			for (String item: emails) {
				try {
					if(!Cadena.isVacio(item)) {
					  IBaseAttachment notificar= new IBaseAttachment(ECorreos.ORDENES_TALLER, ECorreos.ORDENES_TALLER.getEmail(), item, "controlbonanza@gmail.com", "Ferreteria Bonanza - Orden de reparación", params, files);
					  LOG.info("Enviando correo a la cuenta: "+ item);
					  notificar.send();
					} // if	
				} // try
				finally {
				  if(attachments.getFile().exists()) {
   	  	    LOG.info("Eliminando archivo temporal: "+ attachments.getAbsolute());
				    // user.getFile().delete();
				  } // if	
				} // finally	
			} // for
	  	LOG.info("Se envio el correo de forma exitosa");
			if(sb.length()> 0)
		    JsfBase.addMessage("Se envió el correo de forma exitosa.", ETipoMensaje.INFORMACION);
			else
		    JsfBase.addMessage("No se selecciono ningún correo, por favor verifiquelo e intente de nueva cuenta.", ETipoMensaje.ALERTA);
		} // try // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(files);
		} // finally
	}	// doSendmail  

  public String doViewOrdenCompra() {
		JsfBase.setFlashAttribute("idOrdenCompra", this.attrs.get("idOrdenCompra"));
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Taller/filtro");
		return "/Paginas/Mantic/Compras/Ordenes/filtro".concat(Constantes.REDIRECIONAR);
  } 
  
  public String doViewVenta() {
		JsfBase.setFlashAttribute("idVenta", this.attrs.get("idVenta"));
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Taller/filtro");
		return "/Paginas/Mantic/Ventas/accion".concat(Constantes.REDIRECIONAR);
  } 
  
}
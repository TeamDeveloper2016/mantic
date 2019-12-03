package mx.org.kaana.mantic.facturas.backing;

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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.facturas.reglas.FormatTicket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;

@Named(value= "manticFacturasTickets")
@ViewScoped
public class Tickets extends IBaseFilter implements Serializable {

  private static final long serialVersionUID= 8743667741599428332L;
	private static final Log LOG              = LogFactory.getLog(Tickets.class);	
	private double importe;
	private Entity pivote;
	private List<Entity> acumulado;
	private List<String> folios;
	private FormatCustomLazy lazyTicket;
	private List<Long> ventaPublico;
	private StringBuilder idClientes;

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe=importe;
	}

	public String getSumaImporte() {
		return Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, this.importe);
	}

	public Entity getPivote() {
		return pivote;
	}

	public List<Entity> getAcumulado() {
		return acumulado;
	}

	public List<String> getFolios() {
		return folios;
	}

	public FormatCustomLazy getLazyTicket() {
		return lazyTicket;
	}
	
	@PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("activarCliente", true);
			this.attrs.put("checkCliente", false);
			this.attrs.put("disabledCliente", false);
			this.importe= 0D;
			this.folios = new ArrayList<>();
      this.pivote = (Entity)JsfBase.getFlashAttribute("cliente");
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.acumulado= JsfBase.getFlashAttribute("tickets")!= null? (List<Entity>)JsfBase.getFlashAttribute("tickets"): new ArrayList<>();
			if(!this.acumulado.isEmpty()) 
				for (Entity item: this.acumulado) {
					this.importe+= item.toDouble("importe");
				} // for
			this.toLoadCatalog();
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
      columns.add(new Columna("nombreEmpresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      params.put("sortOrder", "order by tc_mantic_ventas.registro desc");
      this.lazyModel = new FormatTicket("VistaVentasDto", "tickets", params, columns);
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
		if(!Cadena.isVacio(this.attrs.get("articulo")))
  		sb.append("(tc_mantic_ventas_detalles.id_articulo= ").append(((UISelectEntity)this.attrs.get("articulo")).toLong("idArticulo")).append(") and ");
		else
		  if(!Cadena.isVacio(JsfBase.getParametro("articulo_input")))
  		  sb.append("(upper(tc_mantic_ventas_detalles.nombre) like upper('%").append(JsfBase.getParametro("articulo_input")).append("%') or upper(tc_mantic_ventas_detalles.codigo) like upper('%").append(JsfBase.getParametro("articulo_input")).append("%')) and ");
		if(!Cadena.isVacio(this.attrs.get("cliente"))) {
			String keys= this.idClientes.toString()+ ((UISelectEntity)this.attrs.get("cliente")).getKey();
			sb.append("(tc_mantic_clientes.id_cliente in (").append(keys).append(")) and ");		
		}	// if
		else
      if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) {
				String keys= this.idClientes.toString()+ -1;
		  	sb.append("((tc_mantic_clientes.razon_social regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").replaceAll("(,| |\\t)+", ".*.*")).append(".*') or (tc_mantic_clientes.id_cliente in (").append(keys).append("))) and ");
			} // if
		if(!this.folios.isEmpty()) {
			StringBuilder items= new StringBuilder();
			this.folios.forEach((folio) -> {
				items.append("'").append(folio).append("', ");
			}); // for
  		sb.append("(tc_mantic_ventas.ticket in (").append(items.substring(0, items.length()- 2)).append(")) and ");
		} // if	
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_ventas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");			
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_ventas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");			
		if(!Cadena.isVacio(this.attrs.get("cantidadInicial")))
		  sb.append("(tc_mantic_ventas.total>= ").append((Double)this.attrs.get("cantidadInicial")).append(") and ");			
		if(!Cadena.isVacio(this.attrs.get("cantidadFinal")))
		  sb.append("(tc_mantic_ventas.total<= ").append((Double)this.attrs.get("cantidadFinal")).append(") and ");			
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
	
	protected void toLoadCatalog() throws Exception {
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
			this.ventaPublico= new ArrayList<>();
			this.idClientes  = new StringBuilder();
			params.put("razonSocial", Constantes.VENTA_AL_PUBLICO_GENERAL);
			List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticClientesDto", "clientes", params);
			items.stream().map((item) -> {
				this.ventaPublico.add(item.toLong("idCliente"));
				return item;
			}).forEachOrdered((item) -> {
				this.idClientes.append(item.toString("idCliente")).append(", ");
			}); // for
    } // try
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public void doUpdateCliente(String codigo) {
    try {
  		this.doCompleteCliente(codigo);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	
	
	public List<UISelectEntity> doCompleteCliente(String query) {
		this.attrs.put("codigoCliente", query);
    this.doUpdateClientes();		
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	// doCompleteCliente
	
	public List<UISelectEntity> doCompleteClienteAutocomplete(String query) {
		this.attrs.put("codigoClienteAutocomplete", query);
    this.doUpdateClientesAutocomplete();
		return (List<UISelectEntity>)this.attrs.get("clientesAutocomplete");
	}	// doCompleteCliente
	
	public void doUpdateClientes() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params = new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("idEmpresa", this.attrs.get("idEmpresa"));
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
			String search= (String)this.attrs.get("codigoCliente"); 
			search= !Cadena.isVacio(search) ? search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*") : "WXYZ";
  		params.put(Constantes.SQL_CONDICION, "upper(tc_mantic_clientes.razon_social) regexp '.*".concat(search).concat(".*'").concat(" or upper(tc_mantic_clientes.rfc) regexp '.*".concat(search).concat(".*'")));			
      this.attrs.put("clientes", (List<UISelectEntity>) UIEntity.build("VistaClientesDto", "findRazonSocial", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}	// doUpdateClientes
	
	public void doUpdateClientesAutocomplete() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params = new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("idEmpresa", this.attrs.get("idEmpresa"));
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
			String search= (String)this.attrs.get("codigoClienteAutocomplete"); 
			search= !Cadena.isVacio(search) ? search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*") : "WXYZ";
  		params.put(Constantes.SQL_CONDICION, "upper(tc_mantic_clientes.razon_social) regexp '.*".concat(search).concat(".*'").concat(" or upper(tc_mantic_clientes.rfc) regexp '.*".concat(search).concat(".*'")));			
      this.attrs.put("clientesAutocomplete", (List<UISelectEntity>) UIEntity.build("VistaClientesDto", "findRazonSocial", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}	// doUpdateClientes
	
	public void doAsignaCliente(SelectEvent event) {
		UISelectEntity seleccion     = null;
		List<UISelectEntity> clientes= null;
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("cliente", seleccion);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	public void doAsignaClienteAutocomplete(SelectEvent event) {
		UISelectEntity seleccion     = null;
		List<UISelectEntity> clientes= null;
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientesAutocomplete");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("clienteAutocomplete", seleccion);
			this.attrs.put("activarCliente", false);
			this.pivote= new Entity(seleccion.getKey());
			this.pivote.put("idCliente", new Value("idCliente", seleccion.getKey()));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	public void doUpdateArticulo(String codigo) {
    try {
  		this.doCompleteArticulo(codigo);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	
	
	public void doUpdateArticulos() {
		List<Columna> columns         = null;
    Map<String, Object> params    = new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo        = false;
    try {
			columns= new ArrayList<>();
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
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
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
		this.attrs.put("codigo", query);
    this.doUpdateArticulos();		
		return (List<UISelectEntity>)this.attrs.get("articulos");
	}	

	public void doAsignaArticulo(SelectEvent event) {
		UISelectEntity seleccion      = null;
		List<UISelectEntity> articulos= null;
		try {
			articulos= (List<UISelectEntity>) this.attrs.get("articulos");
			seleccion= articulos.get(articulos.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("articulo", seleccion);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
  public void doRemoveConsecutivo(String consecutivo){
		if(this.folios.contains(consecutivo)) {
		  this.folios.remove(consecutivo);
			// JsfBase.addMessage("Se eliminó el consecutivo en la lista", ETipoMensaje.INFORMACION);
		}
		else
			JsfBase.addMessage("No existe el consecutivo en la lista !", ETipoMensaje.ALERTA);
  }

  public void doCleanConsecutivo() {
		this.folios.clear();
  	JsfBase.addMessage("Se limpio la lista consecutivos", ETipoMensaje.INFORMACION);
	}	

  public void doAddConsecutivo(String consecutivo) {
		if(!this.folios.contains(consecutivo)) {
		  this.folios.add(consecutivo);
			// JsfBase.addMessage("Se agregó el consecutivo en la lista", ETipoMensaje.INFORMACION);
		}
		else
			JsfBase.addMessage("Ya existe el consecutivo en la lista !", ETipoMensaje.ALERTA);
	}	
	
	@Override
	public void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.folios);
	}

	private boolean checkIdCliente(Entity row) {
	  boolean regresar= true;
		for (Entity item: this.acumulado) {
			regresar= this.ventaPublico.indexOf(item.toLong("idCliente"))>= 0 || Objects.equals(row.toLong("idCliente"), item.toLong("idCliente"));
			if(!regresar) 
				break;
		} // for
		return regresar;
	}
	
	public void doAddTicket(Entity row) {
		if(this.acumulado.indexOf(row)< 0 && (this.ventaPublico.indexOf(row.toLong("idCliente"))>= 0 || this.checkIdCliente(row))) {
			this.importe+= row.toDouble("importe");
			if(this.ventaPublico.indexOf(row.toLong("idCliente"))< 0){
				this.attrs.put("disabledCliente", true);
				this.attrs.put("clienteAutocomplete", new UISelectEntity(-1L));
				this.attrs.put("activarCliente", true);
			} // if
		  this.acumulado.add(row);
			this.findCliente();
			// JsfBase.addMessage("Se agregó el ticket de forma correcta", ETipoMensaje.INFORMACION);			
		} // if	
		else
			JsfBase.addMessage("No se agregó el ticket porque ya existe o porque es otro cliente !", ETipoMensaje.ALERTA);
	}	
	
	private void findCliente() {
		for (Entity item: this.acumulado) {
			if(this.pivote== null)
				this.pivote= item;
		  else	
			  if(this.ventaPublico.indexOf(item.toLong("idCliente"))< 0)
				  this.pivote= item;
		} // for
		if(this.pivote!= null && this.ventaPublico.indexOf(this.pivote.toLong("idCliente"))>= 0)
			this.pivote= null;
	}
	
	public void doRemoveTicket(Entity row) {
		this.importe-= row.toDouble("importe");
		this.acumulado.remove(row);
		if(this.ventaPublico.indexOf(row.toLong("idCliente"))< 0 && this.acumulado.indexOf(row.toLong("idCliente"))< 0){
			this.attrs.put("disabledCliente", false);
			this.attrs.put("clienteAutocomplete", new UISelectEntity(-1L));
			this.attrs.put("activarCliente", !Boolean.valueOf(this.attrs.get("checkCliente").toString()));
		} // if		
		this.pivote= null;
		this.findCliente();
		JsfBase.addMessage("Se eliminó el ticket del listado", ETipoMensaje.INFORMACION);
	}	
	
	public boolean doDisponible(Entity row) {
		return row.toLong("idFacturar").equals(2L) && row.toDouble("importe")> 0D && (this.ventaPublico.indexOf(row.toLong("idCliente"))>= 0 || this.pivote== null || Objects.equals(this.pivote.toLong("idCliente"), row.toLong("idCliente")));
	}

	public String doRowColor(Entity row) {
		return row.toBoolean("devolucion")? "janal-tr-diferencias": "";
	}

  public void loadTicket() {
    List<Columna> columns     = null;
		Map<String, Object> params= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
			params=new HashMap<>();
			params.put("idVenta", ((Entity)this.attrs.get("seleccionado")).getKey());
			this.lazyTicket= new FormatCustomLazy("TcManticVentasDetallesDto", "detalle", params, columns);
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
	
  public void loadDevolucion() {
    List<Columna> columns     = null;
		Map<String, Object> params= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
			params=new HashMap<>();
			params.put("idVenta", ((Entity)this.attrs.get("seleccionado")).getKey());
			this.lazyTicket= new FormatCustomLazy("VistaVentasDto", "devoluciones", params, columns);
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
	
	public void doCancelar() {
		this.importe= 0D;
	  this.acumulado.clear();	
		//JsfBase.addMessage("Se limpió la lista de ticket(s) seleccionados.", ETipoMensaje.INFORMACION);
	}	
	
	public String doAceptar() {
		String regresar= "facturar";		
		try {
			StringBuilder sb= new StringBuilder("TICKETS DE ESTA FACTURA: ");
			this.acumulado.forEach((item) -> {
				sb.append(item.toString("consecutivo")).append(", ");
			}); // for
			JsfBase.setFlashAttribute("cliente", this.pivote);
			JsfBase.setFlashAttribute("tickets", this.acumulado);
			JsfBase.setFlashAttribute("idCliente", this.pivote.toLong("idCliente"));
			JsfBase.setFlashAttribute("observaciones", sb.substring(0, sb.length()- 2));
			JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Facturas/tickets");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar.concat(Constantes.REDIRECIONAR);
	}		
	
	public void doActivarBusquedaCliente(){
		Boolean check= false;
		try {
			check= Boolean.valueOf(this.attrs.get("checkCliente").toString());
			this.attrs.put("activarCliente", !check);
			if(!check)
				this.attrs.put("clienteAutocomplete", new UISelectEntity(-1L));
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doActivarBusquedaCliente
}
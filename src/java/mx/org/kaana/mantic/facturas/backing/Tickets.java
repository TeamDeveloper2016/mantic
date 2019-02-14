package mx.org.kaana.mantic.facturas.backing;

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
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
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
import org.primefaces.event.SelectEvent;

@Named(value= "manticFacturasTickets")
@ViewScoped
public class Tickets extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8743667741599428332L;
	private static final Log LOG= LogFactory.getLog(Tickets.class);
	
	private List<String> folios;
	private FormatCustomLazy lazyTicket;

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
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.folios= new ArrayList<>();
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
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      params.put("sortOrder", "order by tc_mantic_ventas.registro desc");
      this.lazyModel = new FormatCustomLazy("VistaVentasDto", "tickets", params, columns);
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
		if(!Cadena.isVacio(this.attrs.get("razonSocial")) && !this.attrs.get("razonSocial").toString().equals("-1"))
			sb.append("tc_mantic_clientes.id_cliente = ").append(((Entity)this.attrs.get("razonSocial")).getKey()).append(" and ");					
		else
       if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
			 	 sb.append("tc_mantic_clientes.razon_social regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");
		if(!this.folios.isEmpty()) {
			StringBuilder items= new StringBuilder();
			this.folios.forEach((folio) -> {
				items.append("'").append(folio).append("', ");
			}); // for
  		sb.append("(tc_mantic_ventas.consecutivo in (").append(items.substring(0, items.length()- 2)).append(") and ");
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
	}
	
	public List<UISelectEntity> doCompleteCliente(String query) {
		this.attrs.put("codigoCliente", query);
    this.doUpdateClientes();		
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	// doCompleteCliente
	
	public void doUpdateClientes() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
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
	
	public void doAsignaCliente(SelectEvent event) {
		UISelectEntity seleccion     = null;
		List<UISelectEntity> clientes= null;
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			this.toFindCliente(seleccion);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	private void toFindCliente(UISelectEntity seleccion) {
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null;
		try {
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			motorBusqueda= new mx.org.kaana.mantic.ventas.reglas.MotorBusqueda(-1L);
			clientesSeleccion.add(0, new UISelectEntity(motorBusqueda.toClienteDefault()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // toFindCliente

	public void doUpdateArticulos() {
		List<Columna> columns         = null;
    Map<String, Object> params    = new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo        = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
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
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
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
	
  public void doEliminarItem(SelectEvent event){
    String item= (String)event.getObject();
		this.folios.remove(item);
  }

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.folios);
	}
	
}

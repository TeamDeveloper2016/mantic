package mx.org.kaana.mantic.ventas.backing;

import java.io.File;
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
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Periodo;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Bonanza;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.IBaseAttachment;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.Correo;
import mx.org.kaana.mantic.ventas.comun.IBaseTicket;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@Named(value= "manticVentasFiltro")
@ViewScoped
public class Filtro extends IBaseTicket implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;
  private static final Log LOG = LogFactory.getLog(Filtro.class);
  
	private Reporte reporte;
	private List<Correo> correos;
	private List<Correo> selectedCorreos;	
	private Correo correo;
	private List<Correo> celulares;
	private List<Correo> selectedCelulares;	
	private Correo celular;
	
	public Reporte getReporte() {
		return reporte;
	}	// getReporte

	public void setReporte(Reporte reporte) {
		this.reporte=reporte;
	}
	
	public List<Correo> getCorreos() {
		return correos;
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

  public List<Correo> getCelulares() {
    return celulares;
  }

  public void setCelulares(List<Correo> celulares) {
    this.celulares = celulares;
  }

  public List<Correo> getSelectedCelulares() {
    return selectedCelulares;
  }

  public void setSelectedCelulares(List<Correo> selectedCelulares) {
    this.selectedCelulares = selectedCelulares;
  }

  public Correo getCelular() {
    return celular;
  }

  public void setCelular(Correo celular) {
    this.celular = celular;
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta"));
      this.attrs.put("sortOrder", "order by tc_mantic_ventas.registro desc");
			this.toLoadCatalog();
      if(this.attrs.get("idVenta")!= null) {
			  this.doLoad();
				this.attrs.put("idVenta", null);
			} // if
      this.initBase();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= this.toPrepare();
    try {
			params.put("sortOrder", this.attrs.get("sortOrder"));
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.NUMERO_CON_DECIMALES));
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

	public void initBase() {
		this.correos= new ArrayList<>();
		this.selectedCorreos= new ArrayList<>();
		this.celulares= new ArrayList<>();
		this.selectedCelulares= new ArrayList<>();
	}
  
  public String doAccion(String accion) {
    EAccion eaccion= null;
		String regresar= "/Paginas/Mantic/Ventas/accion".concat(Constantes.REDIRECIONAR); 
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Ventas/filtro");					
			JsfBase.setFlashAttribute("idVenta", eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
			if(eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) {
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
	
  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion(new TcManticVentasDto(seleccionado.getKey()), this.attrs.get("justificacionEliminar").toString());
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "El ticket de venta se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar el ticket de venta.", ETipoMensaje.ERROR);								
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
		if(!Cadena.isVacio(JsfBase.getParametro("codigo_input"))) 
	 	  sb.append("tc_mantic_ventas_detalles.codigo regexp '.*").append(JsfBase.getParametro("codigo_input").replaceAll(Constantes.CLEAN_SQL, "").replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");
//		else 
//		  if(!Cadena.isVacio(this.attrs.get("codigo")) && !this.attrs.get("codigo").toString().equals("-1"))
//			  sb.append("(upper(tc_mantic_ventas_detalles.codigo) like upper('%").append(((Entity)this.attrs.get("codigo")).getKey()).append("%')) and ");					
		if(!Cadena.isVacio(JsfBase.getParametro("articulo_input")))
  		sb.append("(upper(tc_mantic_ventas_detalles.nombre) like upper('%").append(JsfBase.getParametro("articulo_input")).append("%')) and ");
		if(!Cadena.isVacio(this.attrs.get("idVenta")) && !this.attrs.get("idVenta").toString().equals("-1"))
  		sb.append("(tc_mantic_ventas.id_venta=").append(this.attrs.get("idVenta")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_ventas.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_ventas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_ventas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("montoInicio")))
		  sb.append("(tc_mantic_ventas.total>= ").append((Double)this.attrs.get("montoInicio")).append(") and ");			
		if(!Cadena.isVacio(this.attrs.get("montoTermino")))
		  sb.append("(tc_mantic_ventas.total<= ").append((Double)this.attrs.get("montoTermino")).append(") and ");			
		if(!Cadena.isVacio(this.attrs.get("cliente")) && !Objects.equals(((Entity)this.attrs.get("cliente")).getKey(), -1L))
			sb.append("tc_mantic_clientes.id_cliente = ").append(((Entity)this.attrs.get("razonSocial")).getKey()).append(" and ");					
		else 
      if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
				sb.append("tc_mantic_clientes.razon_social regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");
		if(estatus!= null && !estatus.getKey().equals(-1L))
  		sb.append("(tc_mantic_ventas.id_venta_estatus= ").append(estatus.getKey()).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0) {
      Periodo periodo= new Periodo();
      periodo.addMeses(-2);
		  regresar.put(Constantes.SQL_CONDICION, "date_format(tc_mantic_ventas.registro, '%Y%m%d')>= '".concat(periodo.toString()).concat("'"));
    } // if  
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	}
	
	protected void toLoadCatalog() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("sucursales", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			this.attrs.put("idEmpresa", this.toDefaultSucursal((List<UISelectEntity>)this.attrs.get("sucursales")));
			columns.clear();
      this.attrs.put("estatusFiltro", (List<UISelectEntity>) UIEntity.build("TcManticVentasEstatusDto", "row", params, columns));
			this.attrs.put("idVentaEstatus", new UISelectEntity("-1"));
    } // try
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public boolean doVerificarReporte() {
    boolean regresar = false;
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L) {
			rc.execute("start(" + this.reporte.getTotal() + ")");		
      regresar = true;
    }
    else {
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
		} // else
    return regresar;
	} // doVerificarReporte		
	
	public void doLoadEstatus(){
		Entity seleccionado          = null;
		Map<String, Object>params    = new HashMap<>();
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params.put(Constantes.SQL_CONDICION, "id_venta_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
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
	
	public String doTicketExpress() {
		String regresar= null;
		try {
			regresar= "/Paginas/Mantic/Ventas/express".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doTicketExpress
	
	public String doGarantia(){
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
			JsfBase.setFlashAttribute("idVenta", seleccionado.getKey());
			JsfBase.setFlashAttribute("registroVenta", seleccionado.toTimestamp("registro"));
			JsfBase.setFlashAttribute("accionVenta", EAccion.CONSULTAR);
			JsfBase.setFlashAttribute("retornoVenta", "/Paginas/Mantic/Ventas/filtro");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch
		return "/Paginas/Mantic/Ventas/Garantias/accion".concat(Constantes.REDIRECIONAR);
	} // doGrantia
	
	public void doMontoUpdate() {
	  if(this.attrs.get("montoInicio")!= null && this.attrs.get("montoTermino")== null)
			this.attrs.put("montoTermino", this.attrs.get("montoInicio"));
	  if(this.attrs.get("montoTermino")!= null && this.attrs.get("montoInicio")== null)
			this.attrs.put("montoInicio", this.attrs.get("montoTermino"));
	}
		
	public void doUpdateCodigos() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			String search= (String)this.attrs.get("codigoCodigo"); 
			search= !Cadena.isVacio(search) ? search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim(): "WXYZ";
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("sucursales", this.attrs.get("idEmpresa"));
			else
				params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);			
  		params.put("codigo", search);			
      this.attrs.put("codigos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}	// doUpdateCodigos

	public List<UISelectEntity> doCompleteCodigo(String query) {
		this.attrs.put("codigoCodigo", query);
    this.doUpdateCodigos();		
		return (List<UISelectEntity>)this.attrs.get("codigos");
	}	// doCompleteCodigo

	public void doAsignaCodigo(SelectEvent event) {
		UISelectEntity seleccion    = null;
		List<UISelectEntity> codigos= null;
		try {
			codigos= (List<UISelectEntity>) this.attrs.get("codigos");
			seleccion= codigos.get(codigos.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("codigoSeleccion", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCodigo
	
	public void doUpdateArticulos() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			String search= (String)this.attrs.get("codigoArticulo"); 
			search= !Cadena.isVacio(search) ? search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim(): "WXYZ";
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("sucursales", this.attrs.get("idEmpresa"));
			else
				params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);			
  		params.put("codigo", search.replaceAll("[ ]", "*.*"));			
      this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}	// doUpdateArticulo

	public List<UISelectEntity> doCompleteArticulo(String query) {
		this.attrs.put("codigoArticulo", query);
    this.doUpdateArticulos();		
		return (List<UISelectEntity>)this.attrs.get("articulos");
	}	// doCompleteCodigo

	public void doAsignaArticulo(SelectEvent event) {
		UISelectEntity seleccion      = null;
		List<UISelectEntity> articulos= null;
		try {
			articulos= (List<UISelectEntity>) this.attrs.get("articulos");
			seleccion= articulos.get(articulos.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("articulosSeleccion", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaArticulo

	public List<UISelectEntity> doCompleteCliente(String query) {
		this.attrs.put("codigoCliente", query);
    this.doUpdateClientes();		
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	// doCompleteCliente
	
	public void doUpdateClientes() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
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
			motorBusqueda= new MotorBusqueda(-1L);
			clientesSeleccion.add(0, new UISelectEntity(motorBusqueda.toClienteDefault()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // toFindCliente	

	public void doLoadMails() {
		Entity seleccionado               = null;
		MotorBusquedaCatalogos motor      = null; 
		List<ClienteTipoContacto>contactos= null;
		Correo item                       = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			motor= new mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda(seleccionado.toLong("idCliente"));
			contactos= motor.toClientesTipoContacto();
			this.correos.clear();
			this.selectedCorreos.clear();
			for(ClienteTipoContacto contacto: contactos) {
				if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey())){
					item= new Correo(contacto.getIdClienteTipoContacto(), contacto.getValor().toUpperCase(), contacto.getIdPreferido());
					this.correos.add(item);		
					this.selectedCorreos.add(item);
				} // if
			} // for
			this.correos.add(new Correo(-1L, "", 2L, Boolean.TRUE));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(contactos);
		} // finally
	} // doLoadMails
  
	public void doLoadPhones() {
		Entity seleccionado= null;
		mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda motor= null; 
		List<ClienteTipoContacto>contactos= null;
    Correo item                       = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");			
			motor= new mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda(seleccionado.toLong("idCliente"));
			contactos= motor.toClientesTipoContacto();
			this.celulares= new ArrayList<>();
			for(ClienteTipoContacto contacto: contactos) {
				if(contacto.getIdTipoContacto().equals(ETiposContactos.CELULAR.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.CELULAR_NEGOCIO.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.CELULAR_PERSONAL.getKey())) {
          item= new Correo(contacto.getIdClienteTipoContacto(), contacto.getValor(), contacto.getIdPreferido());
					this.celulares.add(item);				
          this.selectedCelulares.add(item);
        } // if
			} // for
			this.celulares.add(new Correo(-1L, "", 2L, Boolean.TRUE));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
    finally {
      Methods.clean(contactos);
    } // finally
	} // doLoadPhones
  
	public void doAgregarCorreo() {
		Entity seleccionado    = null;
		mx.org.kaana.mantic.ventas.facturas.reglas.Transaccion transaccion= null;
		try {
			if(!Cadena.isVacio(this.correo.getDescripcion())) {
				seleccionado= (Entity)this.attrs.get("seleccionado");
				transaccion= new mx.org.kaana.mantic.ventas.facturas.reglas.Transaccion(seleccionado.toLong("idCliente"), seleccionado.toString("cliente"), this.correo);
				if(transaccion.ejecutar(EAccion.COMPLEMENTAR))
					JsfBase.addMessage("Se agregó/modificó el correo electronico correctamente !");
				else
					JsfBase.addMessage("Ocurrió un error al agregar/modificar el correo electronico");
			} // if
			else
				JsfBase.addMessage("Es necesario capturar un correo electronico !");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAgregarCorreo
  
	public void doAgregarCelular() {
		Entity seleccionado    = null;
		mx.org.kaana.mantic.ventas.facturas.reglas.Transaccion transaccion= null;
		try {
			if(!Cadena.isVacio(this.celular.getDescripcion())){
				seleccionado= (Entity)this.attrs.get("seleccionado");
				transaccion= new mx.org.kaana.mantic.ventas.facturas.reglas.Transaccion(seleccionado.toLong("idCliente"), seleccionado.toString("cliente"), this.celular);
				if(transaccion.ejecutar(EAccion.COMPLETO))
					JsfBase.addMessage("Se agregó/modificó el celular correctamente !");
				else
					JsfBase.addMessage("Ocurrió un error al agregar/modificar el celular");
			} // if
			else
				JsfBase.addMessage("Es necesario capturar un celular !");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAgregarCelular    
  
	public void doReporte() throws Exception {
   this.doReporte(Boolean.FALSE);  
  }
  
	public void doReporte(boolean email) throws Exception {
		Map<String, Object>params    = new HashMap<>();
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
		try{				
			reporteSeleccion= EReportes.TICKET_VENTA;
			this.reporte= JsfBase.toReporte();
			params.put("idVenta", ((Entity)this.attrs.get("seleccionado")).getKey());			
      Parametros comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			parametros= comunes.getComunes();
			parametros.put("REPORTE_EMPRESA", JsfBase.getAutentifica().getEmpresa().getNombreCorto());
		  parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
			parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
			parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));		
			parametros.put("REPORTE_DNS", Configuracion.getInstance().getPropiedadServidor("sistema.dns"));		
      switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
        case "mantic":
   			  parametros.put("REPORTE_SUB_TITULO", "CENTRO DE SERVICIO DEWALT Y B&D");		
          break;
        case "kalan":
   			  parametros.put("REPORTE_SUB_TITULO", "LA CALIDAD Y EL SERVICIO NOS DISTINGUE");		
          break;
        case "tsaak":
   			  parametros.put("REPORTE_SUB_TITULO", "LA CALIDAD Y EL SERVICIO NOS DISTINGUE");		
          break;
      } // swtich
			parametros.put("REPORTE_NOTIFICA", Configuracion.getInstance().getEmpresa("celular"));		
      Encriptar encriptado= new Encriptar();
      String codigo= encriptado.encriptar(Fecha.formatear(Fecha.CODIGO_SEGURIDAD, ((Entity)this.attrs.get("seleccionado")).toTimestamp("registro")));
			parametros.put("REPORTE_CODIGO_SEGURIDAD", codigo);			
			if(email) { 
				this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
        File file= new File(JsfBase.getRealPath(this.reporte.getNombre()));
        if(!file.exists())
          this.reporte.doAceptarSimple();			
			} // if
			else {				
        this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
        if(this.doVerificarReporte())
          this.reporte.doAceptar();			
			} // else
		} // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
    } // catch	
	} // doReporte
  
	public void doSendMail() {
		StringBuilder sb= new StringBuilder("");
		if(this.selectedCorreos!= null && !this.selectedCorreos.isEmpty()) {
			for(Correo mail: this.selectedCorreos) {
				if(!Cadena.isVacio(mail.getDescripcion()))
					sb.append(mail.getDescripcion()).append(", ");
			} // for
		} // if
		Map<String, Object> params= new HashMap<>();
		String[] emails= {(sb.length()> 0? sb.substring(0, sb.length()- 2): "")};
		List<Attachment> files= new ArrayList<>(); 
		try {
			Entity seleccionado= (Entity)this.attrs.get("seleccionado");
			params.put("header", "...");
			params.put("footer", "...");
			params.put("titulo", Configuracion.getInstance().getEmpresa("titulo"));
			params.put("empresa", JsfBase.getAutentifica().getEmpresa().getNombre());
			params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
			params.put("host", Configuracion.getInstance().getEmpresa("host"));
			params.put("tipo", "Ticket");			
			params.put("razonSocial", seleccionado.toString("cliente"));
			params.put("correo", ECorreos.TICKET.getEmail());			
			params.put("url", Configuracion.getInstance().getPropiedadServidor("sistema.dns"));			
			this.doReporte(Boolean.TRUE);
			Attachment attachments= new Attachment(this.reporte.getNombre(), Boolean.FALSE);
			files.add(attachments);
			files.add(new Attachment("logo", ECorreos.TICKET.getImages().concat(Configuracion.getInstance().getEmpresa("logo")), Boolean.TRUE));
			params.put("attach", attachments.getId());
			for (String item: emails) {
				try {
					if(!Cadena.isVacio(item)) {
					  IBaseAttachment notificar= new IBaseAttachment(ECorreos.TICKET, ECorreos.TICKET.getEmail(), item, ECorreos.TICKET.getControl(), Configuracion.getInstance().getEmpresa("titulo").concat(" | Ticket"), params, files);
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
	  	LOG.info("Se envió el correo de forma exitosa");
			if(sb.length()> 0)
		    JsfBase.addMessage("Se envió el correo de forma exitosa", ETipoMensaje.INFORMACION);
			else
		    JsfBase.addMessage("No se selecciono ningún correo, por favor verifiquelo e intente de nueva cuenta.", ETipoMensaje.ALERTA);
		} // try // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
			Methods.clean(files);
		} // finally
	} 
  
  public void doSendWhatsup() {
    StringBuilder sb= new StringBuilder();
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      Entity seleccionado= (Entity)this.attrs.get("seleccionado");			
      if(this.selectedCelulares!= null && !this.selectedCelulares.isEmpty()) {
        for(Correo phone: this.selectedCelulares) {
          if(!Cadena.isVacio(phone.getDescripcion()))
            sb.append(phone.getDescripcion()).append(", ");
        } // for
      } // if
      if(sb.length()> 0) {
        try {
			    this.doReporte(Boolean.TRUE);          
          Bonanza notificar= new Bonanza(seleccionado.toString("cliente"), "celular", this.reporte.getAlias(), seleccionado.toString("ticket"), Fecha.formatear(Fecha.FECHA_HORA_CORTA, seleccionado.toTimestamp("registro")));
          String[] phones= sb.substring(0, sb.length()- 2).split("[,]");
          for (String phone: phones) {
            notificar.setCelular(phone, Boolean.TRUE);
            LOG.info("Enviando mensaje por whatsup al celular: "+ celular);
            notificar.doSendTicket();
          } // if  
        } // try
        finally {
          LOG.info("Eliminando archivo temporal: "+ this.reporte.getNombre());				  
        } // finally	
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
	@Override
	protected void finalize() throws Throwable {
    super.finalize();		
	}	// finalize
	
}

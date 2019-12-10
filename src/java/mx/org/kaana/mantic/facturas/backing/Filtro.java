package mx.org.kaana.mantic.facturas.backing;

import java.io.Serializable;
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
import mx.org.kaana.kajool.procesos.reportes.beans.Definicion;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.comun.JuntarReporte;
import mx.org.kaana.mantic.facturas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import mx.org.kaana.mantic.enums.EEstatusFacturas;
import mx.org.kaana.mantic.enums.EEstatusFicticias;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.Correo;
import mx.org.kaana.mantic.facturas.comun.FiltroFactura;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;

@Named(value= "manticFacturasFiltro")
@ViewScoped
public class Filtro extends FiltroFactura implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;
	private static final Log LOG=LogFactory.getLog(Filtro.class);			
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idFicticia", JsfBase.getFlashAttribute("idFicticia"));
			this.toLoadCatalog();
      if(this.attrs.get("idFicticia")!= null) 
			  this.doLoad();			
      this.attrs.remove("idFicticia"); 
      this.attrs.put("facturama", 1L);
			super.initBase();
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
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));      
      columns.add(new Columna("timbrado", EFormatoDinamicos.FECHA_HORA_CORTA));   
      columns.add(new Columna("cancelada", EFormatoDinamicos.FECHA_CORTA));   
      params.put("sortOrder", "order by tc_mantic_ventas.registro desc");
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
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Facturas/filtro");		
			JsfBase.setFlashAttribute("idFicticia", eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
			if(eaccion.equals(EAccion.AGREGAR)){
				JsfBase.setFlashAttribute("observaciones", null);		
				JsfBase.setFlashAttribute("idCliente", null);		
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Facturas/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion(new TcManticFicticiasDto(seleccionado.getKey()), this.attrs.get("justificacionEliminar").toString());
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "La factura se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar la factura.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
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
      columns.add(new Columna("limiteCredito", EFormatoDinamicos.MONEDA_SAT_DECIMALES));      
			this.doLoadDocumentoEstatus();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	} // toLoadCatalog
	
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
	
	public void doReporte(String nombre) throws Exception {
		doReporte(nombre, false);
	} // doReporte	
  
  public void doReporteFacturas(String nombre) throws Exception {
		Map<String, Object>params    = null;
		EReportes reporteSeleccion   = null;
    List<Definicion> definiciones= null;
		try{		
      params= this.toPrepare();	
      //es importante este orden para los grupos en el reporte	
      definiciones = new ArrayList<>();
      params.put("sortOrder", "order by tc_mantic_ventas.id_empresa, tc_mantic_clientes.id_cliente, tc_mantic_ventas.ejercicio, tc_mantic_ventas.orden");
      reporteSeleccion= EReportes.valueOf(nombre);
      this.reporte= JsfBase.toReporte();	
      definiciones.add(new Definicion((Map<String, Object>) ((HashMap) params).clone(), params, reporteSeleccion.getProceso(), reporteSeleccion.getIdXml(), reporteSeleccion.getJrxml()));
      this.reporte.toAsignarReportes(new JuntarReporte(definiciones, reporteSeleccion, "/Paginas/Mantic/Facturas/filtro", false, false));
      if(doVerificarReporte())
        this.reporte.doAceptar();
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // doReporte	
	
	public void doLoadDocumentoEstatus() {
		List<Columna> columns     = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			if(this.attrs.get("idTipoDocumento")== null || (Long)this.attrs.get("idTipoDocumento")== -1L)
			  params.put("idTipoDocumento", "1, 2");
			else
			  params.put("idTipoDocumento", this.attrs.get("idTipoDocumento"));
      this.attrs.put("estatusFiltro", (List<UISelectEntity>) UIEntity.build("TcManticVentasEstatusDto", "rows", params, columns));
			this.attrs.put("idVentaEstatus", new UISelectEntity("-1"));
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
	
	public void doLoadEstatus() {
		Entity seleccionado               = null;
		Map<String, Object>params         = null;
		List<UISelectItem> allEstatus     = null;
		MotorBusquedaCatalogos motor      = null; 
		List<ClienteTipoContacto>contactos= null;
		Correo correoAdd                  = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
			params.put("idTipoDocumento", seleccionado.toLong("idTipoDocumento"));
			params.put("estatusAsociados", seleccionado.toString("estatusAsociados"));
			allEstatus= UISelect.build("TcManticVentasEstatusDto", "estatus", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
			this.attrs.put("allEstatus", allEstatus);
			this.attrs.put("estatus", allEstatus.get(0).getValue().toString());
			motor= new MotorBusqueda(-1L, seleccionado.toLong("idCliente"));
			contactos= motor.toClientesTipoContacto();
			LOG.warn("Inicializando listas de correos y seleccionados");
			getCorreos().clear();
			getSelectedCorreos().clear();
			LOG.warn("Total de contactos" + contactos.size());
			for(ClienteTipoContacto contacto: contactos){
				if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey())){
					correoAdd= new Correo(contacto.getIdClienteTipoContacto(), contacto.getValor().toUpperCase());
					getCorreos().add(correoAdd);		
					getSelectedCorreos().add(correoAdd);
				} // if
			} // for
			LOG.warn("Agregando correo default");
			getCorreos().add(new Correo(-1L, ""));
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
		TcManticFicticiasDto orden           = null;
		TcManticFicticiasBitacoraDto bitacora= null;
		Entity seleccionado                  = null;
		StringBuilder emails                 = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			orden= (TcManticFicticiasDto)DaoFactory.getInstance().findById(TcManticFicticiasDto.class, seleccionado.getKey());
			bitacora= new TcManticFicticiasBitacoraDto(orden.getConsecutivo(), (String)this.attrs.get("justificacion"), Long.valueOf(this.attrs.get("estatus").toString()), JsfBase.getIdUsuario(), seleccionado.getKey(), -1L, orden.getTotal());
			emails= new StringBuilder("");
			if(this.getSelectedCorreos()!= null && !this.getSelectedCorreos().isEmpty()){
				for(Correo mail: this.getSelectedCorreos())
					if(!Cadena.isVacio(mail.getDescripcion()))
						emails.append(mail.getDescripcion()).append(", ");
			} // if
			transaccion= new Transaccion(bitacora, emails.toString(), (String)this.attrs.get("justificacion"));
			if(transaccion.ejecutar(EAccion.JUSTIFICAR)) {
				if(bitacora.getIdFicticiaEstatus().equals(EEstatusFicticias.TIMBRADA.getIdEstatusFicticia()) || bitacora.getIdFicticiaEstatus().equals(EEstatusVentas.ELIMINADA.getIdEstatusVenta()))
					this.doSendMail();				
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally {
			this.attrs.put("justificacion", "");
			setSelectedCorreos(new ArrayList<>());
		} // finally
	}	// doActualizaEstatus
	
	public void doAgregarCorreo() {
		Entity seleccionado    = null;
		Transaccion transaccion= null;
		try {
			if(!Cadena.isVacio(getCorreo().getDescripcion())){
				seleccionado= (Entity)this.attrs.get("seleccionado");
				transaccion= new Transaccion(getCorreo(), seleccionado.toLong("idCliente"));
				if(transaccion.ejecutar(EAccion.COMPLEMENTAR))
					JsfBase.addMessage("Se agrego el correo electronico correctamente !");
				else
					JsfBase.addMessage("Ocurrió un error al agregar el correo electronico");
			} // if
			else
				JsfBase.addMessage("Es necesario capturar un correo electronico !");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAgregarCorreo
	
  public String doSincronizar() {
		JsfBase.setFlashAttribute("accion", EAccion.GENERAR);		
		JsfBase.setFlashAttribute("retorno", "filtro");		
		return "sincronizar".concat(Constantes.REDIRECIONAR);
  } // doSincronizar  

	public String doImportar() {
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Facturas/filtro");		
		JsfBase.setFlashAttribute("idFicticia", ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("idFactura", ((Entity)this.attrs.get("seleccionado")).toLong("idFactura"));
		return "importar".concat(Constantes.REDIRECIONAR);
	} // doImportar
	
	public void doClonar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		TcManticFicticiasDto dto= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			dto= (TcManticFicticiasDto)DaoFactory.getInstance().findById(TcManticFicticiasDto.class, seleccionado.getKey());
			if(dto!= null) {
				TcManticFicticiasDto copia= SerializationUtils.clone(dto);
				transaccion= new Transaccion(copia);
				if(transaccion.ejecutar(EAccion.COPIAR)) {
					UIBackingUtilities.execute("janal.back('clon\\u00F3 la factura ', '"+ copia.getConsecutivo()+ "');");
					JsfBase.addMessage("Clonar", "La factura se ha clonó correctamente.", ETipoMensaje.ERROR);
				} // if	
				else
					JsfBase.addMessage("Clonar", "Ocurrió un error al clonar la factura.", ETipoMensaje.ERROR);								
			} // if	
			else
				JsfBase.addMessage("Clonar", "Ocurrió un error al clonar la factura, por favor intentelo de nuevo.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doClonar

	public String doMovimientos() {
		JsfBase.setFlashAttribute("tipo", ETipoMovimiento.FACTURAS_FICTICIAS);
		JsfBase.setFlashAttribute(ETipoMovimiento.FACTURAS_FICTICIAS.getIdKey(), ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Facturas/filtro");
		return "/Paginas/Mantic/Compras/Ordenes/movimientos".concat(Constantes.REDIRECIONAR);
	} // doMovimientos
	
	public void doMontoUpdate() {
	  if(this.attrs.get("montoInicio")!= null && this.attrs.get("montoTermino")== null)
			this.attrs.put("montoTermino", this.attrs.get("montoInicio"));
	  if(this.attrs.get("montoTermino")!= null && this.attrs.get("montoInicio")== null)
			this.attrs.put("montoInicio", this.attrs.get("montoTermino"));
	} // doMontoUpdate
	
	public void doUpdateCodigos() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
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
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
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

	public String doFacturaColor(Entity row) {
		String regresar= "";
		if(row.toLong("idFacturaEstatus").equals(EEstatusFacturas.AUTOMATICO.getIdEstatusFactura()))
			regresar= "janal-tr-nuevo";
		else if(row.toLong("idTipoDocumento").equals(1L))
			regresar= "janal-tr-diferencias";
		return regresar;
	}  // doFacturaColor

	public String doComplemento(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Facturas/filtro");		
			JsfBase.setFlashAttribute("idVenta", eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
			if(eaccion.equals(EAccion.AGREGAR)) {
				JsfBase.setFlashAttribute("observaciones", null);		
				JsfBase.setFlashAttribute("idCliente", null);		
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Ventas/Facturas/accion".concat(Constantes.REDIRECIONAR);
  } // doComplemento 

	public void doMoveSection() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		List<UISelectEntity> documento= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("impuestos", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("precio", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_HORA));
			params.put("idVenta", ((Entity)this.attrs.get("seleccionado")).getKey());
			documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "venta", params, columns, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("documentos", documento);
			if(documento!= null && !documento.isEmpty()) {
				documento.get(0).put("articulos", new Value("articulos", documento.size()));
        this.attrs.put("documento", documento.get(0));
			} // if	
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}
		
	@Override
	protected void finalize() throws Throwable {
    super.finalize();		
	}	// finalize
	
	public void doCancelarFacturacion() {
		Transaccion transaccion         = null;
		Entity seleccionado             = null;		
		TcManticFicticiasDto ticketVenta= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");						
			ticketVenta= new TcManticFicticiasDto();
			ticketVenta.setKey(seleccionado.getKey());
			ticketVenta.setIdFactura(seleccionado.toLong("idFactura"));
			transaccion= new Transaccion(ticketVenta, (String) this.attrs.get("justificacionCancelar"));
			if(transaccion.ejecutar(EAccion.DEPURAR))
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally {
			this.attrs.put("justificacionCancelar", "");
			setSelectedCorreos(new ArrayList<>());
		} // finally
	}	// doActualizaEstatus
}
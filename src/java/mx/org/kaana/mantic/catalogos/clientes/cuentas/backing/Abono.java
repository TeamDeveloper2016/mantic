package mx.org.kaana.mantic.catalogos.clientes.cuentas.backing;

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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.catalogos.clientes.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosDto;
import mx.org.kaana.mantic.enums.EEstatusClientes;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

@Named(value = "manticCatalogosClientesCuentasAbono")
@ViewScoped
public class Abono extends IBaseImportar implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;	
	
  @PostConstruct
  @Override
  protected void init() {
    try {			
      this.attrs.put("sortOrder", "order by	tc_mantic_clientes_deudas.registro desc");
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));     
      this.attrs.put("idClienteDeuda", JsfBase.getFlashAttribute("idClienteDeuda"));     
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());			
			this.attrs.put("mostrarBanco", false);
			this.attrs.put("saldar", "2");
			setFile(new Importado());
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_IDENTIFICACION);
			this.attrs.put("xml", ""); 
			this.attrs.put("file", ""); 
			if(JsfBase.isAdminEncuestaOrAdmin())
				loadSucursales();							
			doLoadCajas();
			loadBancos();
			loadTiposPagos();
			loadClienteDeuda();
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadBancos(){
		List<UISelectEntity> bancos= null;
		Map<String, Object> params = null;
		List<Columna> campos       = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos= new ArrayList<>();
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			bancos= UIEntity.build("TcManticBancosDto", "row", params, campos, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("bancos", bancos);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadBancos
	
	private void loadSucursales(){
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			sucursales=(List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
			this.attrs.put("sucursales", sucursales);
			this.attrs.put("idEmpresa", sucursales.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
	
	public void doLoadCajas(){
		List<UISelectEntity> cajas= null;
		Map<String, Object>params = null;
		List<Columna> columns     = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			cajas=(List<UISelectEntity>) UIEntity.build("TcManticCajasDto", "cajas", params, columns);
			this.attrs.put("cajas", cajas);
			this.attrs.put("caja", cajas.get(0));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
	} // loadCajas
	
	private void loadClienteDeuda() throws Exception{
		Entity deuda             = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.attrs.get("idCliente"));			
			params.put("sortOrder", this.attrs.get("sortOrder"));
			params.put(Constantes.SQL_CONDICION, " tc_mantic_clientes_deudas.id_cliente_deuda=" + this.attrs.get("idClienteDeuda"));
			deuda= (Entity) DaoFactory.getInstance().toEntity("VistaClientesDto", "cuentas", params);
			this.attrs.put("permitirPago", deuda.toLong("idClienteEstatus").equals(EEstatusClientes.FINALIZADA.getIdEstatus()));
			this.attrs.put("deuda", deuda);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	} // loadClienteDeuda
	
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {  	  
			params= new HashMap<>();
			params.put("idClienteDeuda", this.attrs.get("idClienteDeuda"));			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("pago", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			this.lazyModel = new FormatCustomLazy("VistaClientesDto", "pagosDeuda", params, columns);
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

	public String doRegresar() {	  
		return "saldos".concat(Constantes.REDIRECIONAR);
	} // doRegresar
	
	public void doRegistrarPago(){
		Transaccion transaccion      = null;
		TcManticClientesPagosDto pago= null;
		boolean tipoPago             = false;
		boolean saldar               = false;
		try {
			if(validaPago()){
				saldar= Long.valueOf(this.attrs.get("saldar").toString()).equals(1L);
				pago= new TcManticClientesPagosDto();
				pago.setIdClienteDeuda(Long.valueOf(this.attrs.get("idClienteDeuda").toString()));
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observaciones").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pago").toString()));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("caja").toString()), Long.valueOf(this.attrs.get("idEmpresa").toString()), tipoPago ? -1 : Long.valueOf(this.attrs.get("banco").toString()), tipoPago ? "" : this.attrs.get("referencia").toString(), saldar);
				if(transaccion.ejecutar(EAccion.AGREGAR)){
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta", ETipoMensaje.INFORMACION);
					loadClienteDeuda();
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante y mayor a 0.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doRegistrarPago
	
	private boolean validaPago(){
		boolean regresar= false;
		Double pago     = 0D;
		Double saldo    = 0D;
		Entity deuda    = null;
		try {
			pago= Double.valueOf(this.attrs.get("pago").toString());
			if(pago > 0D){
				deuda= (Entity) this.attrs.get("deuda");
				saldo= Double.valueOf(deuda.toString("saldo"));
				regresar= pago<= saldo;
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch
		return regresar;
	} // validaPago	
	
	private void loadTiposPagos(){
		List<UISelectEntity> tiposPagos= null;
		Map<String, Object>params      = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cobro_caja=1");
			tiposPagos= UIEntity.build("TcManticTiposMediosPagosDto", "row", params);
			this.attrs.put("tiposPagos", tiposPagos);
			this.attrs.put("tipoPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadTiposPagos
	
	public void doValidaTipoPago(){
		Long tipoPago= -1L;
		try {
			tipoPago= Long.valueOf(this.attrs.get("tipoPago").toString());
			this.attrs.put("mostrarBanco", !ETipoMediosPago.EFECTIVO.getIdTipoMedioPago().equals(tipoPago));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doValidaTipoPago
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados();
		else if(event.getTab().getTitle().equals("Importar")) {
			doLoadPagosArchivos();
			this.doLoadFiles();
		} // else if
	}	// doTabChange	
	
	private void doLoadImportados() {
		List<Columna> columns                 = null;
		TcManticClientesDeudasDto clienteDeuda= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			clienteDeuda= (TcManticClientesDeudasDto)DaoFactory.getInstance().findById(TcManticClientesDeudasDto.class, (Long) this.attrs.get("idClienteDeuda"));
		  this.attrs.put("importados", UIEntity.build("VistaClientesDto", "importados", clienteDeuda.toMap(), columns));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
    }// finally
  } // doLoadImportados	
	
	private void doLoadFiles() {
		TcManticClientesPagosArchivosDto tmp= null;
		if((Long) this.attrs.get("idClienteDeuda") > 0L) {
			Map<String, Object> params=null;
			try {
				params=new HashMap<>();
				params.put("idClienteDeuda", this.attrs.get("idClienteDeuda"));				
				params.put("idTipoArchivo", 2L);
				tmp= (TcManticClientesPagosArchivosDto) DaoFactory.getInstance().toEntity(TcManticClientesPagosArchivosDto.class, "VistaClientesDto", "existsPagos", params); 
				if(tmp!= null) {
					setFile(new Importado(tmp.getNombre(), "PDF", EFormatos.PDF, 0L, tmp.getTamanio(), "", tmp.getRuta(), tmp.getObservaciones()));
  				this.attrs.put("file", getFile().getName()); 
				} // if	
			} // try
			catch (Exception e) {
				Error.mensaje(e);
				JsfBase.addMessageError(e);
			} // catch
			finally {
				Methods.clean(params);
			} // finally
		} // if
	} // doLoadFiles	
	
	private void doLoadPagosArchivos(){
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params = new HashMap<>();
			columns= new ArrayList<>();      
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			params.put("idClienteDeuda", this.attrs.get("idClienteDeuda"));
			this.attrs.put("pagos", UIEntity.build("VistaClientesDto", "pagosDeuda", params, columns));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // doLoadPagosArchivos	
	
	public String doImportar() {
		String regresar        = null;
		Transaccion transaccion= null;
		TcManticClientesDeudasDto clienteDeuda= null;
		try {
			clienteDeuda= (TcManticClientesDeudasDto)DaoFactory.getInstance().findById(TcManticClientesDeudasDto.class, (Long) this.attrs.get("idClienteDeuda"));
			transaccion= new Transaccion(getFile(), clienteDeuda, ((Entity)this.attrs.get("pagoCombo")).getKey());
      if(transaccion.ejecutar(EAccion.SUBIR)) {
      	RequestContext.getCurrentInstance().execute("janal.alert('Se importaron los archivos de forma correcta !');");				
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
		finally{
			setFile(new Importado());
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_IDENTIFICACION);
			this.attrs.put("xml", ""); 
			this.attrs.put("file", ""); 
		} // finally
    return regresar;
	} // doAceptar
}
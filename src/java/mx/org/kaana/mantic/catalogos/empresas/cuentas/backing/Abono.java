package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

@Named(value = "manticCatalogosEmpresasCuentasAbono")
@ViewScoped
public class Abono extends IBaseImportar implements Serializable {

  private static final long serialVersionUID= 8793667741599428879L;	
	private static final int BUFFER_SIZE      = 6124;	
	
  @PostConstruct
  @Override
  protected void init() {
    try {			
      this.attrs.put("sortOrder", "order by	tc_mantic_empresas_deudas.registro desc");
      this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));     
      this.attrs.put("idProveedor", JsfBase.getFlashAttribute("idProveedor"));     
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda"));     
			this.attrs.put("empresaDeuda", DaoFactory.getInstance().findById(TcManticEmpresasDeudasDto.class, Long.valueOf(this.attrs.get("idEmpresaDeuda").toString())));
			this.attrs.put("proveedor", DaoFactory.getInstance().findById(TcManticProveedoresDto.class, Long.valueOf(this.attrs.get("idProveedor").toString())));
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));  
			this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());			
			this.attrs.put("mostrarBanco", false);
			setFile(new Importado());
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_IDENTIFICACION);
			this.attrs.put("xml", ""); 
			this.attrs.put("file", ""); 
			if(JsfBase.isAdminEncuestaOrAdmin())
				loadSucursales();							
			doLoadCajas();
			loadTiposPagos();
			loadBancos();
			loadProveedorDeuda();
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
	
	private void loadProveedorDeuda() throws Exception{
		Entity deuda             = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));			
			params.put("sortOrder", this.attrs.get("sortOrder"));
			deuda= (Entity) DaoFactory.getInstance().toEntity("VistaEmpresasDto", "cuentas", params);
			this.attrs.put("deuda", deuda);
			this.attrs.put("saldoPositivo", Double.valueOf(deuda.toString("saldo")) * -1);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	} // loadProveedorDeuda
	
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {  	  
			params= new HashMap<>();
			params.put("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("pago", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			this.lazyModel = new FormatCustomLazy("VistaEmpresasDto", "pagosDeuda", params, columns);
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
		String regresar= null;
		if(this.attrs.get("retorno")!= null){
			JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
			regresar= this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
		} // if
		else
			regresar=	"saldos".concat(Constantes.REDIRECIONAR);
		return regresar;
	} // doRegresar
	
	public void doRegistrarPago(){
		Transaccion transaccion      = null;
		TcManticEmpresasPagosDto pago= null;
		boolean tipoPago             = false;
		try {
			if(validaPago()){
				pago= new TcManticEmpresasPagosDto();
				pago.setIdEmpresaDeuda(Long.valueOf(this.attrs.get("idEmpresaDeuda").toString()));
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observaciones").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pago").toString()));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("caja").toString()), Long.valueOf(this.attrs.get("idEmpresa").toString()), tipoPago ? -1 : Long.valueOf(this.attrs.get("banco").toString()), tipoPago ? "" : this.attrs.get("referencia").toString(), false);
				if(transaccion.ejecutar(EAccion.AGREGAR)){
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta", ETipoMensaje.INFORMACION);
					loadProveedorDeuda();
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
				regresar= pago<= (saldo * -1);
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
		TcManticEmpresasDeudasDto empresaDeuda= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			empresaDeuda= (TcManticEmpresasDeudasDto)DaoFactory.getInstance().findById(TcManticEmpresasDeudasDto.class, (Long) this.attrs.get("idEmpresaDeuda"));
		  this.attrs.put("importados", UIEntity.build("VistaEmpresasDto", "importados", empresaDeuda.toMap(), columns));
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
		TcManticEmpresasArchivosDto tmp= null;
		if((Long) this.attrs.get("idEmpresaDeuda") > 0L) {
			Map<String, Object> params=null;
			try {
				params=new HashMap<>();
				params.put("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));				
				params.put("idTipoArchivo", 2L);
				tmp= (TcManticEmpresasArchivosDto) DaoFactory.getInstance().toEntity(TcManticEmpresasArchivosDto.class, "VistaEmpresasDto", "exists", params); 
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
			params.put("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
			this.attrs.put("pagos", UIEntity.build("VistaEmpresasDto", "pagosDeuda", params, columns));
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
		String regresar                       = null;
		Transaccion transaccion               = null;
		TcManticEmpresasDeudasDto empresaDeuda= null;
		try {
			empresaDeuda= (TcManticEmpresasDeudasDto)DaoFactory.getInstance().findById(TcManticEmpresasDeudasDto.class, (Long) this.attrs.get("idEmpresaDeuda"));
			transaccion= new Transaccion(empresaDeuda, getFile(), ((Entity)this.attrs.get("pagoCombo")).getKey());
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
	
	public void doFileUpload(FileUploadEvent event) {
		StringBuilder path= new StringBuilder();  
		StringBuilder temp= new StringBuilder();  
    File result       = null;		
		Long fileSize     = 0L;
		try {
			Calendar calendar= Calendar.getInstance();
			calendar.setTimeInMillis(((TcManticEmpresasDeudasDto)this.attrs.get("empresaDeuda")).getRegistro().getTime());
      path.append(Configuracion.getInstance().getPropiedadSistemaServidor("pagos"));
      temp.append(JsfBase.getAutentifica().getEmpresa().getNombreCorto().replaceAll(" ", ""));
      temp.append("/");
      temp.append(Calendar.getInstance().get(Calendar.YEAR));
      temp.append("/");
      temp.append(Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase());
      temp.append("/");
      temp.append(((TcManticProveedoresDto)this.attrs.get("proveedor")).getClave());
      temp.append("/");
			path.append(temp.toString());
			result= new File(path.toString());		
			if (!result.exists())
				result.mkdirs();
      path.append(event.getFile().getFileName().toUpperCase());
			result = new File(path.toString());
			if (result.exists())
				result.delete();			      
			this.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			setFile(new Importado(event.getFile().getFileName().toUpperCase(), event.getFile().getContentType(), EFormatos.PDF, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones")));
  		this.attrs.put("file", getFile().getName()); 			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);
			if(result!= null)
			  result.delete();
		} // catch
	} // doFileUpload		
	
	private void toWriteFile(File result, InputStream upload) throws Exception {
		FileOutputStream fos= new FileOutputStream(result);
		InputStream is      = upload;
		byte[] buffer       = new byte[BUFFER_SIZE];
		int bulk;
		while(true) {
			bulk= is.read(buffer);
			if (bulk < 0) 
				break;        
			fos.write(buffer, 0, bulk);
			fos.flush();
		} // while
		fos.close();
		is.close();
	} // toWriteFile
}
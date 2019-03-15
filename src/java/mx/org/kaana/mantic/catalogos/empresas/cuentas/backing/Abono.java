package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

import java.io.File;
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
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.catalogos.comun.IBasePagos;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.enums.ECuentasEgresos;
import mx.org.kaana.mantic.enums.EEstatusEmpresas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import org.primefaces.event.FileUploadEvent;

@Named(value = "manticCatalogosEmpresasCuentasAbono")
@ViewScoped
public class Abono extends IBasePagos implements Serializable {

  private static final long serialVersionUID= 8793667741599428879L;		
	private FormatLazyModel notasEntradaFavor;
	private FormatLazyModel notasCreditoFavor;
	private List<Entity> seleccionadosNotas;
	private List<Entity> seleccionadosCredito;

	public FormatLazyModel getNotasEntradaFavor() {
		return notasEntradaFavor;
	}

	public FormatLazyModel getNotasCreditoFavor() {
		return notasCreditoFavor;
	}

	public List<Entity> getSeleccionadosNotas() {
		return seleccionadosNotas;
	}

	public void setSeleccionadosNotas(List<Entity> seleccionadosNotas) {
		this.seleccionadosNotas = seleccionadosNotas;
	}

	public List<Entity> getSeleccionadosCredito() {
		return seleccionadosCredito;
	}

	public void setSeleccionadosCredito(List<Entity> seleccionadosCredito) {
		this.seleccionadosCredito = seleccionadosCredito;
	}		
	
  @PostConstruct
  @Override
  protected void init() {
    try {			
			if(JsfBase.getFlashAttribute("idEmpresaDeuda")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("sortOrder", "order by	tc_mantic_empresas_deudas.registro desc");
      this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));     
      this.attrs.put("idProveedor", JsfBase.getFlashAttribute("idProveedor"));     
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda"));     
			this.attrs.put("empresaDeuda", DaoFactory.getInstance().findById(TcManticEmpresasDeudasDto.class, Long.valueOf(this.attrs.get("idEmpresaDeuda").toString())));
			this.attrs.put("proveedor", DaoFactory.getInstance().findById(TcManticProveedoresDto.class, Long.valueOf(this.attrs.get("idProveedor").toString())));
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));  
			initValues();
			loadProveedorDeuda();
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init	
	
	private void loadProveedorDeuda() throws Exception{
		Entity deuda             = null;
		Map<String, Object>params= null;
    List<Columna> columns    = null;
		try {
			params = new HashMap<>();
      columns= new ArrayList<>();  
			params.put("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));			
			params.put("sortOrder", this.attrs.get("sortOrder"));
			deuda= (Entity) DaoFactory.getInstance().toEntity("VistaEmpresasDto", "cuentas", params);
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("debe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			UIBackingUtilities.toFormatEntity(deuda, columns);
			
			this.attrs.put("deuda", deuda);
			this.attrs.put("saldoPositivo", deuda.toDouble("saldo")* -1);
			this.attrs.put("pago", deuda.toDouble("saldo")* -1);
			this.attrs.put("permitirPago", deuda.toLong("idEmpresaEstatus").equals(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa()));
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
			columns.add(new Columna("pago", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
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
	
	public void doRegistrarPago() {
		Transaccion transaccion      = null;
		TcManticEmpresasPagosDto pago= null;
		boolean tipoPago             = false;
		try {
			//if(validaPago()){
				pago= new TcManticEmpresasPagosDto();
				pago.setIdEmpresaDeuda(Long.valueOf(this.attrs.get("idEmpresaDeuda").toString()));
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones(this.attrs.get("observaciones").toString());
				pago.setPago(Double.valueOf(this.attrs.get("pago").toString()));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("caja").toString()), -1L, Long.valueOf(this.attrs.get("idEmpresa").toString()), tipoPago ? -1 : Long.valueOf(this.attrs.get("banco").toString()), tipoPago ? "" : this.attrs.get("referencia").toString(), null, false, this.seleccionadosNotas, this.seleccionadosCredito);
				if(transaccion.ejecutar(EAccion.AGREGAR)){
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta", ETipoMensaje.INFORMACION);
					loadProveedorDeuda();
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago", ETipoMensaje.ERROR);
			/*} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante y mayor a 0.", ETipoMensaje.ERROR);*/
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{
			this.attrs.put("observaciones", "");
		} // finally
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
	
	@Override
	public void doLoadImportados() {
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
	
	@Override
	public void doLoadFiles() {
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
	
	@Override
	public void doLoadPagosArchivos(){
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
      	UIBackingUtilities.execute("janal.alert('Se importaron los archivos de forma correcta !');");				
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
		String nameFile   = Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result       = null;		
		Long fileSize     = 0L;
		try {
			Calendar calendar= Calendar.getInstance();
			calendar.setTimeInMillis(((TcManticEmpresasDeudasDto)this.attrs.get("empresaDeuda")).getRegistro().getTime());
      path.append(Configuracion.getInstance().getPropiedadSistemaServidor("pagos"));
      temp.append(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString());
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
      path.append(nameFile);
			result = new File(path.toString());
			if (result.exists())
				result.delete();			      
			Archivo.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			setFile(new Importado(nameFile, event.getFile().getContentType(), EFormatos.PDF, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones"), event.getFile().getFileName().toUpperCase()));
  		this.attrs.put("file", getFile().getName()); 			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);
			if(result!= null)
			  result.delete();
		} // catch
	} // doFileUpload		
	
	public void doLoadCuentas(){
		try {
			doLoadNotasEntradas();
			doLoadNotasCredito();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doLoadCuentas
	
	private void doLoadNotasEntradas(){
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
		try {
			this.seleccionadosNotas= new ArrayList<>();
			params= new HashMap<>();
			params.put("idProveedor", this.attrs.get("idProveedor"));														
			params.put("idEmpresaEstatus", EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa());														
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("limite", EFormatoDinamicos.FECHA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));						
			this.notasEntradaFavor= new FormatLazyModel("VistaEmpresasDto", "saldoFavorEntradas", params, columns);      
      UIBackingUtilities.resetDataTable("tablaNotas");		
		} // try 
		catch (Exception e) {			
			throw e;
		} // catch		
	} // doLoadNotasEntradas
	
	private void doLoadNotasCredito(){
		List<Columna> columns     = null;
	  Map<String, Object> params= null;	
		try {
			this.seleccionadosCredito= new ArrayList<>();
			params= new HashMap<>();
			params.put("idProveedor", this.attrs.get("idProveedor"));						
			params.put("idCreditoEstatus", EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa() + "," + EEstatusEmpresas.PROGRAMADA.getIdEstatusEmpresa());																	
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));						
			this.notasCreditoFavor= new FormatLazyModel("VistaCreditosNotasDto", "saldoFavorCreditos", params, columns);      
      UIBackingUtilities.resetDataTable("tablaCreditos");		
		} // try 
		catch (Exception e) {			
			throw e;
		} // catch		
	} // doLoadNotasCredito
	
	public void doReabrirCuenta(){
		Transaccion transaccion               = null;
		TcManticEmpresasDeudasDto deudaReabrir= null;
		try {
			deudaReabrir= new TcManticEmpresasDeudasDto();
			deudaReabrir.setIdEmpresaDeuda((Long)this.attrs.get("idEmpresaDeuda"));
			deudaReabrir.setObservaciones(toObservacionesReabrir());
			transaccion= new Transaccion(deudaReabrir, null, -1L);
			if(transaccion.ejecutar(EAccion.ACTIVAR)){
				JsfBase.addMessage("Se abrió la cuenta de forma correcta", ETipoMensaje.INFORMACION);
				loadProveedorDeuda();
			} // if
			else
				JsfBase.addMessage("Ocurrió un error al abrir la cuenta", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doReabrirCuenta
	
	private String toObservacionesReabrir(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder("");
			regresar.append(this.attrs.get("observacionesReabrir"));
			regresar.append("[Reapertura [Usuario:");
			regresar.append(JsfBase.getIdUsuario());
			regresar.append("][Fecha:");
			regresar.append(Fecha.getRegistro());
			regresar.append("]]");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toObservacionesReabrir
	
	public String doEgreso() {				
		try {															
			JsfBase.setFlashAttribute("retornoPrincipal", this.attrs.get("retorno"));
			JsfBase.setFlashAttribute("iEmpresa", this.attrs.get("idEmpresa"));
			JsfBase.setFlashAttribute("iProveedor", this.attrs.get("idProveedor"));
			JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
			JsfBase.setFlashAttribute("idCuenta", ((Entity)this.attrs.get("pagoEgreso")).getKey());
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Empresas/Cuentas/abono");
			JsfBase.setFlashAttribute("eCuentaEgreso", ECuentasEgresos.EMPRESA_PAGO);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return "/Paginas/Mantic/Egresos/cuentas".concat(Constantes.REDIRECIONAR);
	} // doEgreso	
	
}

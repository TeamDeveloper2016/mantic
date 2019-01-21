package mx.org.kaana.mantic.egresos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticEgresosDto;
import mx.org.kaana.mantic.egresos.reglas.Transaccion;
import mx.org.kaana.mantic.enums.ECuentasEgresos;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;

@Named(value = "manticEgresosDetalle")
@ViewScoped
public class Detalle extends IBaseImportar implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
	private FormatLazyModel egresosNotasEntradas;
	private FormatLazyModel egresosCreditosNotas;
	private FormatLazyModel egresosEmpresasPagos;
	private FormatLazyModel egresosNotas;		

	public FormatLazyModel getEgresosNotasEntradas() {
		return egresosNotasEntradas;
	}

	public FormatLazyModel getEgresosCreditosNotas() {
		return egresosCreditosNotas;
	}

	public FormatLazyModel getEgresosEmpresasPagos() {
		return egresosEmpresasPagos;
	}

	public FormatLazyModel getEgresosNotas() {
		return egresosNotas;
	}	
	
  @PostConstruct
  @Override
  protected void init() {		
		Long idEgreso = -1L;
		Long idEstatus= 1L;
    try {    	            
			idEgreso= (Long) JsfBase.getFlashAttribute("idEgreso");			
      this.attrs.put("egreso", DaoFactory.getInstance().findById(TcManticEgresosDto.class, idEgreso));  
			idEstatus= ((TcManticEgresosDto)this.attrs.get("egreso")).getIdEgresoEstatus();
			this.attrs.put("estatus", idEstatus.equals(1L) ? "REGISTRADO" : (idEstatus.equals(2L) ? "INCOMPLETO" : "COMPLETO"));
      this.attrs.put("idEgreso", idEgreso);  
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init	
	
  @Override
  public void doLoad() {    		
    try {      			
			doLoadNotasEntradas();
			doLoadCreditosNotas();
			doLoadEmpresasPagos();
			doLoadNotas();			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
    } // finally		
  } // doLoad	

	private void doLoadNotasEntradas() throws Exception{
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.egresosNotasEntradas= new FormatLazyModel("VistaEgresosDto", "notasEntradas", this.attrs, campos);			
			UIBackingUtilities.resetDataTable("tablaNotasEntrada");
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			Methods.clean(campos);
		} // finally
	} // doLoadNotasEntradas
	
	private void doLoadCreditosNotas() throws Exception{
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.egresosCreditosNotas= new FormatLazyModel("VistaEgresosDto", "creditosNotas", this.attrs, campos);	
			UIBackingUtilities.resetDataTable("tablaCreditosNotas");
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			Methods.clean(campos);
		} // finally
	} // doLoadCreditosNotas
	
	private void doLoadEmpresasPagos() throws Exception{
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.egresosEmpresasPagos= new FormatLazyModel("VistaEgresosDto", "empresasPagos", this.attrs, campos);			
			UIBackingUtilities.resetDataTable("tablaEmpresasPagos");
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			Methods.clean(campos);
		} // finally
	} // doLoadEmpresasPagos
	
	private void doLoadNotas() throws Exception{
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.egresosNotas= new FormatLazyModel("VistaEgresosDto", "notas", this.attrs, campos);
			UIBackingUtilities.resetDataTable("tablaNotas");
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			Methods.clean(campos);
		} // finally
	} // doLoadNotas
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idEgreso", this.attrs.get("idEgreso"));
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public void doEliminarNotaEntrada(){
		Transaccion transaccion= null;
		Entity notaEntrada     = null;
		try {			
			notaEntrada= (Entity)this.attrs.get("notaEntrada");			
			transaccion= new Transaccion(notaEntrada.getKey(), ECuentasEgresos.NOTA_ENTRADA);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);
			doLoadNotasEntradas();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarNotaEntrada

	public void doEliminarCreditoNota(){
		Transaccion transaccion= null;
		Entity creditoNota     = null;
		try {			
			creditoNota= (Entity)this.attrs.get("creditoNota");			
			transaccion= new Transaccion(creditoNota.getKey(), ECuentasEgresos.CREDITO_NOTA);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);
			doLoadCreditosNotas();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarCreditoNota
	
	public void doEliminarEmpresaPago(){
		Transaccion transaccion= null;
		Entity empresaPago     = null;
		try {			
			empresaPago= (Entity)this.attrs.get("empresaPago");			
			transaccion= new Transaccion(empresaPago.getKey(), ECuentasEgresos.EMPRESA_PAGO);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);			
			doLoadEmpresasPagos();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEmpresaPago
	
	public void doEliminarNota(){
		Transaccion transaccion= null;
		Entity nota            = null;
		try {			
			nota= (Entity)this.attrs.get("nota");			
			transaccion= new Transaccion(nota.getKey(), ECuentasEgresos.NOTA);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);			
			doLoadNotas();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEmpresaPago
}
package mx.org.kaana.mantic.incidentes.backing;

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
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticIncidentesDto;
import mx.org.kaana.mantic.incidentes.beans.Incidente;
import mx.org.kaana.mantic.incidentes.reglas.Transaccion;

@Named(value = "manticIncidentesAccion")
@ViewScoped
public class Accion extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
	private Incidente incidente;

	public Incidente getIncidente() {
		return incidente;
	}

	public void setIncidente(Incidente incidente) {
		this.incidente = incidente;
	}	
	
  @PostConstruct
  @Override
  protected void init() {
		EAccion eaccion = null;		
		Long idIncidente= null;
    try {    	
			this.loadTiposIncidentes();
      eaccion= (EAccion) JsfBase.getFlashAttribute("eaccion");
			switch(eaccion){
				case AGREGAR:
					this.incidente= new Incidente();
					break;
				case MODIFICAR:
				case CONSULTAR:
					idIncidente= (Long) JsfBase.getFlashAttribute("idIncidente");
					loadIncidente(idIncidente);
					this.attrs.put("nombre", new UISelectEntity(this.incidente.getIdPersona()));
					this.attrs.put("nombreModificar", "XYZ");
					doLoad();
					this.attrs.put("seleccionado", new Entity(this.incidente.getIdPersona()));					
					break;
			} // switch
			this.attrs.put("eaccion", eaccion);
			this.attrs.put("titulo", Cadena.nombrePersona(eaccion.name()));
			this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadIncidente(Long idIncidente) throws Exception{
		TcManticIncidentesDto dto= null;
		try {
			dto= (TcManticIncidentesDto) DaoFactory.getInstance().findById(TcManticIncidentesDto.class, idIncidente);
			this.incidente= new Incidente(dto);
			this.attrs.put("orden", dto.getOrden());
			this.attrs.put("ejercicio", dto.getEjercicio());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadIncidente
	
	private void loadTiposIncidentes(){
		List<UISelectItem> tiposIncidentes= null;
		Map<String, Object>params         = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			tiposIncidentes= UISelect.build("TcManticTiposIncidentesDto", "row", params, "nombre", " ", EFormatoDinamicos.MAYUSCULAS);
			this.attrs.put("tiposIncidentes", tiposIncidentes);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadTiposInicidentes
	
  @Override
  public void doLoad() {
    List<Columna> campos      = null;
		Map<String, Object> params= this.toPrepare();
    try {
      campos= new ArrayList<>();
      campos.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("sexo", EFormatoDinamicos.MAYUSCULAS));      
      this.lazyModel = new FormatCustomLazy("VistaPersonasDto", "rowAutocompleteAccion", params, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(campos);
    } // finally		
  } // doLoad

	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= null;		
		try {											 
			regresar= new HashMap<>();			
			regresar.put("idPersona", -1L);					  
			if(this.attrs.get("nombre")!= null && ((UISelectEntity)this.attrs.get("nombre")).getKey()> 0L)							
				regresar.put("idPersona", ((UISelectEntity)this.attrs.get("nombre")).getKey());					  
			if(!Cadena.isVacio(JsfBase.getParametro("nombre_input"))) { 
				String nombre= JsfBase.getParametro("nombre_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");				
				regresar.put("nombreEmpleado", nombre);
			} // if
			else{
				if(this.attrs.get("nombreModificar")!= null){
					regresar.put("nombreEmpleado", this.attrs.get("nombreModificar"));					
					this.attrs.put("nombreModificar", null);
				} // if
				else
					regresar.put("nombreEmpleado", "");					
			} // else
			regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCondicion
	
	public List<UISelectEntity> doCompleteNombreEmpleado(String query) {
		this.attrs.put("nombreEmpleado", query);
    this.doUpdateNombresEmpleados();		
		return (List<UISelectEntity>)this.attrs.get("nombres");
	}	// doCompleteNombreEmpleado
	
	public void doUpdateNombresEmpleados() {
		List<Columna> columns       = null;
    Map<String, Object> params  = new HashMap<>();
		List<UISelectEntity> nombres= null;		
    try {
			columns= new ArrayList<>();      
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));			
			String nombreEmpleado= (String)this.attrs.get("nombreEmpleado"); 
			nombreEmpleado= !Cadena.isVacio(nombreEmpleado) ? nombreEmpleado.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim(): "WXYZ";		
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("idEmpresa", this.attrs.get("idEmpresa"));
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales()); 
			params.put("nombreEmpleado", nombreEmpleado);	
      nombres= (List<UISelectEntity>) UIEntity.build("VistaPersonasDto", "rowAutocomplete", params, columns, 20L);
      this.attrs.put("nombres", nombres);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}	// doUpdateArticulos  
	
	public String doAceptar(){
		String regresar        = null;
		Transaccion transaccion= null;
		EAccion eaccion        = null;
		Entity seleccionado    = null;
		try {
			eaccion= (EAccion) this.attrs.get("eaccion");
			seleccionado= ((Entity)this.attrs.get("seleccionado"));
			if(seleccionado== null && this.incidente.getIdPersona()>= 1L && eaccion.equals(EAccion.MODIFICAR))
				seleccionado= new Entity(this.incidente.getIdPersona());
			if(seleccionado!= null){
				this.incidente.setIdPersona(seleccionado.getKey());
				transaccion= new Transaccion(this.incidente);				
				if(transaccion.ejecutar(eaccion)){
					JsfBase.addMessage(eaccion.equals(EAccion.AGREGAR) ? "Agregar incidente" : "Modificar incidente", "Se ejecutó la operación de forma correcta.", ETipoMensaje.INFORMACION);
					regresar= "filtro".concat(Constantes.REDIRECIONAR);
				} // if
				else
					JsfBase.addMessage(eaccion.equals(EAccion.AGREGAR) ? "Agregar incidente" : "Modificar incidente", "Ocurrió un error al ejecutar la operación.", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage(eaccion.equals(EAccion.AGREGAR) ? "Agregar incidente" : "Modificar incidente", "Es necesario seleccionar a un empleado.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // doAceptar
	
	public String doCancelar(){
		String regresar= null;
		try {
			regresar= "filtro".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // doCancelar
}
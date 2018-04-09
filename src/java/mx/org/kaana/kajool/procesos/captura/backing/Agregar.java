package mx.org.kaana.kajool.procesos.captura.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/10/2016
 *@time 12:15:35 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.db.dto.TcJanalMuestrasDto;
import mx.org.kaana.kajool.db.dto.TrJanalCaratulaDto;
import mx.org.kaana.kajool.db.dto.TrJanalFamiliasDto;
import mx.org.kaana.kajool.db.dto.TrJanalModuloDto;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TrJanalMovimientosDto;
import mx.org.kaana.kajool.db.dto.TrJanalPersonasDto;
import mx.org.kaana.kajool.db.dto.TrJanalVisitasDto;
import mx.org.kaana.kajool.procesos.enums.ERespuestas;
import mx.org.kaana.kajool.procesos.beans.Cuestionario;
import mx.org.kaana.kajool.procesos.captura.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.enums.EEstatus;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.context.RequestContext;

@ManagedBean(name="kajoolCapturaAgregar")
@ViewScoped
public class Agregar extends IBaseAttribute implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Agregar.class);	
  private static final long serialVersionUID= -1975301652082028292L;
	private TrJanalPersonasDto trJanalPersonaDto;
	private TrJanalVisitasDto trJanalVisitasDto;
	private Cuestionario cuestionario;	
  private List<UISelectItem> estatus;

	public TrJanalPersonasDto getTrJanalPersonaDto() {
		return trJanalPersonaDto;
	}

	public void setTrJanalPersonaDto(TrJanalPersonasDto trJanalPersonaDto) {
		this.trJanalPersonaDto=trJanalPersonaDto;
	}

	public Cuestionario getCuestionario() {
		return cuestionario;
	}

	public void setCuestionario(Cuestionario cuestionario) {
		this.cuestionario=cuestionario;
	}
	
  public List<UISelectItem> getEstatus() {
    return estatus;
  }

  @PostConstruct
  @Override
	protected void init() {
    TrJanalMovimientosDto movimiento= null;
    try {						
			this.attrs.put("index", "0");
			this.attrs.put("indice", "0");	
      this.attrs.put("countPersons", 0); 
      this.attrs.put("addMorePersons", false); 
			this.cuestionario     = new Cuestionario();
			this.trJanalPersonaDto= new TrJanalPersonasDto();	
			this.trJanalVisitasDto= new TrJanalVisitasDto();
			this.trJanalVisitasDto.setFecha(new Date(Calendar.getInstance().getTime().getTime()));
			this.trJanalVisitasDto.setHrIni(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			this.attrs.put("deshabilita", false);
			this.attrs.put("modificar", false);
      movimiento= (TrJanalMovimientosDto) JsfBase.getFlashAttribute("movimiento");
      this.attrs.put("movimiento", movimiento);
			this.attrs.put("idMuestra", JsfBase.getFlashAttribute("idMuestra"));
      this.estatus= new ArrayList<>();
      this.estatus.add(new UISelectItem(EEstatus.VACIO.getKey(), EEstatus.VACIO.name()));
      this.estatus.add(new UISelectItem(EEstatus.PARCIAL.getKey(), EEstatus.PARCIAL.name()));
      this.estatus.add(new UISelectItem(EEstatus.COMPLETO.getKey(), EEstatus.COMPLETO.name()));
      this.attrs.put("estatus", UIBackingUtilities.toFirstKeySelectItem(this.estatus));			
			cargarConsecutivos();
			cargarCaratula(true);			
			cargarIntegrantes(true);
			cargarFamilias();
			cargarModulo();
			this.attrs.put("control", this.cuestionario.getTrJanalCaratulaDto().getFolioCuest());
			this.attrs.put("titulo", "consecutivo ".concat(this.cuestionario.getTrJanalCaratulaDto().getConsecutiv()));			
    } // try // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init
	
	private boolean cargarCaratula(boolean inicio) {
		TcJanalMuestrasDto tcJanalMuestraDto = null;		
		TrJanalCaratulaDto trJanalCaratulaDto= null;
		boolean regresar                     = false;
		try {
			tcJanalMuestraDto= (TcJanalMuestrasDto) DaoFactory.getInstance().findById(TcJanalMuestrasDto.class, (Long)this.attrs.get("idMuestra"));
			if(tcJanalMuestraDto!= null) {
				this.attrs.put("descripcionEntidad", tcJanalMuestraDto.getEntidad());
				this.attrs.put("descripcionMunicipio", tcJanalMuestraDto.getMun());
				this.attrs.put("descripcionLocalidad", tcJanalMuestraDto.getLocalidad());	
				this.attrs.put("cveUpm", tcJanalMuestraDto.getUpm());
				this.attrs.put("folioCuest", this.cuestionario.getTrJanalCaratulaDto().getFolioCuest());
				this.attrs.put("idMuestra", tcJanalMuestraDto.getIdMuestra());				
				this.attrs.put("consecutiv", tcJanalMuestraDto.getConsecutivo());		
				trJanalCaratulaDto= (TrJanalCaratulaDto) DaoFactory.getInstance().findFirst(TrJanalCaratulaDto.class, "identically", this.attrs);
				if(trJanalCaratulaDto== null) {
					if(inicio) {
						this.cuestionario.getTrJanalCaratulaDto().setNomTit(tcJanalMuestraDto.getNombre());
						this.cuestionario.getTrJanalCaratulaDto().setPatTit(tcJanalMuestraDto.getApPaterno());
						this.cuestionario.getTrJanalCaratulaDto().setMatTit(tcJanalMuestraDto.getApMaterno());
						this.cuestionario.getTrJanalCaratulaDto().setEnt(tcJanalMuestraDto.getEnt());
						this.cuestionario.getTrJanalCaratulaDto().setMun(tcJanalMuestraDto.getCveMun());
						this.cuestionario.getTrJanalCaratulaDto().setLoc(tcJanalMuestraDto.getLoc());
						this.cuestionario.getTrJanalCaratulaDto().setCveUpm(tcJanalMuestraDto.getUpm());
						this.cuestionario.getTrJanalCaratulaDto().setConsecutiv(tcJanalMuestraDto.getConsecutivo().toString());
						this.cuestionario.getTrJanalCaratulaDto().setCalle(tcJanalMuestraDto.getCalle());
						this.cuestionario.getTrJanalCaratulaDto().setNumExt(tcJanalMuestraDto.getNoExt());
						this.cuestionario.getTrJanalCaratulaDto().setNumInt(tcJanalMuestraDto.getNoInt());
						this.cuestionario.getTrJanalCaratulaDto().setColonia(tcJanalMuestraDto.getColonia());
						this.cuestionario.getTrJanalCaratulaDto().setCp(tcJanalMuestraDto.getCodigoPostal()!= null? tcJanalMuestraDto.getCodigoPostal().toString(): null);
						this.cuestionario.getTrJanalCaratulaDto().setTelef(tcJanalMuestraDto.getTelefono1().concat("|,|").concat(tcJanalMuestraDto.getTelefono2().concat("|,|").concat(tcJanalMuestraDto.getTelefono3())));
						this.cuestionario.getTrJanalCaratulaDto().setReferencia(tcJanalMuestraDto.getReferencia());
						this.cuestionario.getTrJanalCaratulaDto().setEntreCa1(tcJanalMuestraDto.getEntreCalle());
						this.cuestionario.getTrJanalCaratulaDto().setEntreCa2(tcJanalMuestraDto.getYcalle());
						this.cuestionario.getTrJanalCaratulaDto().setCalleAtra(tcJanalMuestraDto.getCalleAtras());					
						this.cuestionario.getTrJanalCaratulaDto().setIdMuestra(tcJanalMuestraDto.getIdMuestra());
						this.cuestionario.getTrJanalCaratulaDto().setIdUsuario(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
						this.cuestionario.getTrJanalCaratulaDto().setStatus("V");
						this.cuestionario.getTrJanalCaratulaDto().setCreacion(new Timestamp(Calendar.getInstance().getTimeInMillis()));
						this.cuestionario.getTrJanalCaratulaDto().setInicioCaptura(new Timestamp(Calendar.getInstance().getTimeInMillis()));								
						this.cuestionario.getTrJanalCaratulaDto().setFuente(1L);
					} // if
					else {
						if(!this.cuestionario.getTrJanalCaratulaDto().getFolioCuest().equals(1L)) {
							trJanalCaratulaDto= this.cuestionario.getTrJanalCaratulaDto();
							Methods.clean(this.cuestionario);
							this.cuestionario= new Cuestionario();
							this.cuestionario.setTrJanalCaratulaDto(trJanalCaratulaDto);
						} // if
					} // else
				} // if
				else
					if(inicio)
						this.cuestionario.setTrJanalCaratulaDto(trJanalCaratulaDto);
				regresar= true;
			} // if			
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		return regresar;
	}
	
	private void cargarConsecutivos() {
		List<Entity> consecutivos= null;
		try {
			consecutivos= DaoFactory.getInstance().toEntitySet("TrJanalIntegrantesDto", "consecutivos", this.attrs);
			for(Entity consecutivo: consecutivos) {
				this.cuestionario.getTrJanalCaratulaDto().setFolioCuest(consecutivo.get("folioCuest").toLong());
				this.cuestionario.getTrJanalCaratulaDto().setTcuest(consecutivo.get("tcuest").toLong());
			} // for
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(consecutivos);
		} // finally
	}
	
	private boolean cargarIntegrantes(boolean inicio) {
		List<Entity> integrantes         = null;
		List<TrJanalPersonasDto> personas= null;
		TrJanalPersonasDto persona       = null;
		Integer indice                   = 0;
		StringBuilder renglones          = null;					
		boolean regresar                 = false;
		try {			
			renglones= new StringBuilder();		
			if((inicio) || (!this.cuestionario.getTrJanalCaratulaDto().getFolioCuest().equals(Numero.getLong(this.attrs.get("control").toString())))) {
				this.attrs.put("control", this.cuestionario.getTrJanalCaratulaDto().getFolioCuest());
				this.cuestionario.getPersonas().clear();				
				this.attrs.put("folioCuest", this.cuestionario.getTrJanalCaratulaDto().getFolioCuest());			
				personas= DaoFactory.getInstance().findViewCriteria(TrJanalPersonasDto.class, this.attrs, "personas");			
				if(personas.isEmpty()) {
					integrantes= DaoFactory.getInstance().toEntitySet("TrJanalIntegrantesDto", "integrantes", this.attrs);
					if(integrantes.size()> 0) {					
						for(Entity integrante: integrantes) {
							indice++;
							persona= new TrJanalPersonasDto();
							persona.setRg(indice.toString());
							persona.setNombre(integrante.get("nombres").toString());
							persona.setApaterno(integrante.get("apellidoPaterno").toString());
							persona.setAmaterno(integrante.get("apellidoMaterno").toString());				
							persona.setActivo("3");
							this.cuestionario.getPersonas().add(persona);							
						} // for
					} // if				
				} // if
				else {				
					for(TrJanalPersonasDto integrante: personas) 				
						this.cuestionario.getPersonas().add(integrante);											
				} // else
			} // if
			for(TrJanalPersonasDto integrante: this.cuestionario.getPersonas()) 
				doContruirIntegrante(renglones, integrante);			
			this.attrs.put("pintarIntegrantes", renglones.toString());
			this.trJanalPersonaDto.setRg(String.valueOf(this.cuestionario.getPersonas().size()+ 1));			
			regresar= true;			
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(integrantes);
			Methods.clean(personas);
		} // finally
		return regresar;
	}
	
	private void cargarFamilias() {
		Map<String, String> params= null;
		TrJanalFamiliasDto trJanalFamiliaDto;
		try {
			params= new HashMap<>();
			params.put("idMuestra", this.cuestionario.getTrJanalCaratulaDto().getIdMuestra().toString());
			params.put("folioCuest", this.cuestionario.getTrJanalCaratulaDto().getFolioCuest().toString());
			params.put("consecutiv", this.cuestionario.getTrJanalCaratulaDto().getConsecutiv());
			params.put("cveUpm", this.cuestionario.getTrJanalCaratulaDto().getCveUpm());
			trJanalFamiliaDto= (TrJanalFamiliasDto) DaoFactory.getInstance().findIdentically(TrJanalFamiliasDto.class, params);
			if(trJanalFamiliaDto!= null)
				this.cuestionario.setTrJanalFamiliasDto(trJanalFamiliaDto);			
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(params);			
		} // finally
	}
	
	private void cargarModulo() {
		Map<String, String> params			 = null;
		TrJanalModuloDto trJanalModuloDto= null;
		try {
			params= new HashMap<>();
			params.put("idMuestra", this.cuestionario.getTrJanalCaratulaDto().getIdMuestra().toString());
			params.put("folioCuest", this.cuestionario.getTrJanalCaratulaDto().getFolioCuest().toString());
			params.put("consecutiv", this.cuestionario.getTrJanalCaratulaDto().getConsecutiv());
			params.put("cveUpm", this.cuestionario.getTrJanalCaratulaDto().getCveUpm());
			trJanalModuloDto= (TrJanalModuloDto) DaoFactory.getInstance().findIdentically(TrJanalModuloDto.class, params);
			if(trJanalModuloDto!= null)
				this.cuestionario.setTrJanalModuloDto(trJanalModuloDto);			
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(params);			
		} // finally
	}
		
	private boolean capturarPersonas(int siguiente) {
		boolean regresar= true;		
		try {
      Integer index= Numero.getInteger(this.attrs.get("indice").toString())+ siguiente;
      while(index>= 0 && index< this.cuestionario.getPersonas().size() && this.cuestionario.getPersonas().get(index).isDepurado())
        index+= siguiente;
      if(index>= 0 && index< this.cuestionario.getPersonas().size()) {
        this.attrs.put("indice", index);
  			this.attrs.put("titulo", "consecutivo ".concat(this.cuestionario.getTrJanalCaratulaDto().getConsecutiv()).concat(". Persona ").concat(" ").concat(String.valueOf(index+ 1)).concat(" de ").concat(String.valueOf(this.cuestionario.getPersonas().size())));			
        //if(index== this.cuestionario.getPersonas().size()- 1)
        //  this.cuestionario.getPersonas().get(index).setVresid(null);
        regresar= false;
      } // if
      else
  			this.attrs.put("titulo", "consecutivo ".concat(this.cuestionario.getTrJanalCaratulaDto().getConsecutiv()));
  	} // try 
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		return regresar;
	}
	
	private boolean realizarSalto(Integer actual, String nemonico) {
		boolean regresar= false;
		try {
			//if(siguiente) {
			  for(TrJanalPersonasDto persona: this.cuestionario.getPersonas()) {
				  if((persona.toValue(nemonico)!= null) && (!persona.toValue(nemonico).toString().isEmpty()) && persona.isActivo()) {						
						regresar= true;	
					  this.attrs.put("index", actual);
					} // if
				} // for					
			//} // if
		} // try 
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		return regresar;
	}
	
  private boolean realizarSaltoEdad(Integer actual, String nemonico) {
		boolean regresar= false;
		try {
      for(TrJanalPersonasDto persona: this.cuestionario.getPersonas()) {
        if(!Cadena.isVacio(persona.toValue(nemonico)) && persona.isActivo()) {						
          regresar= true;	
          this.attrs.put("index", actual);
          break;
        } // if
      } // for					
		} // try 
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		return regresar;
	}
	
	private void cargarVisita() {
		Value value= null;
		try {
			this.trJanalVisitasDto.setHrTer(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			this.trJanalVisitasDto.setIdMuestra(this.cuestionario.getTrJanalCaratulaDto().getIdMuestra());
			this.trJanalVisitasDto.setIdResultado(Numero.getLong(this.attrs.get("respuesta").toString()));
			this.trJanalVisitasDto.setIdUsuario(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
			this.trJanalVisitasDto.setNomEnt(JsfBase.getAutentifica().getEmpleado().getNombreCompleto());
			this.trJanalVisitasDto.setObservaciones(this.attrs.get("observaciones").toString());
			value= DaoFactory.getInstance().toField("TrJanalVisitasDto", "consecutivo", this.attrs, "visita");
			this.trJanalVisitasDto.setVisita(value!= null? Numero.getLong(value.toString())+ 1L: 1L);			
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	}

  public String doAceptar(boolean finalizar) throws Exception {		
    Transaccion transaccion         = null;
    TrJanalMovimientosDto movimiento= null; 		
    try {
			if(this.cuestionario.getPersonas().size()> 0) {				
				if(finalizar) {
					cargarVisita();				
					movimiento= (TrJanalMovimientosDto) this.attrs.get("movimiento");      
					movimiento.setObservaciones(this.attrs.get("observaciones").toString());
					if(Numero.getLong(this.attrs.get("respuesta").toString()).equals(ERespuestas.ENTREVISTA_COMPLETA.getIdResultado())) {
						movimiento.setIdEstatus(EEstatus.COMPLETO.getKey());
						this.cuestionario.getTrJanalCaratulaDto().setStatus("C");
					} // if
					else {
						this.cuestionario.getTrJanalCaratulaDto().setStatus("I");
						if(Numero.getLong(this.attrs.get("respuesta").toString()).equals(ERespuestas.ENTREVISTA_INCOMPLETA.getIdResultado())) {
							movimiento.setIdEstatus(EEstatus.PARCIAL.getKey());
						} // if
						else {
							movimiento.setIdEstatus(EEstatus.LIBERADO_CAMPO.getKey());
						} // else					
					} // else
				} // if
				else {
					this.cuestionario.getTrJanalCaratulaDto().setStatus("I");
				} // else
				transaccion= new Transaccion(finalizar, movimiento, this.trJanalVisitasDto, this.cuestionario);
				if(transaccion.ejecutar(EAccion.AGREGAR))
					JsfUtilities.addMessage("Se actualizo el estatus de captura correctamente");
				else
					JsfUtilities.addMessage("Error al actualizar el estatus de captura");
			} // if
			else
				JsfUtilities.addMessage("El cuestionario no contiene personas");
    } // try // try
    catch(Exception e) {
			RequestContext.getCurrentInstance().execute("janal.error('".concat(e.getCause().getMessage()).concat("');"));      			
      Error.mensaje(e);
			if(!finalizar)
				throw e;
			else
				JsfUtilities.addMessage(e.getCause().toString());			
    } // catch
    return finalizar? "filtro".concat(Constantes.REDIRECIONAR): null;
  } // doLoad
		
	public void onTabChange(TabChangeEvent event) {
    try {
      RequestContext.getCurrentInstance().execute("janal.refresh();");      
    } //try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  } // onTabChange
	
	public String doRegresar() {
    String regresar = null;
    try {
			this.attrs.put("index", 12);			
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  }
		
	private void deshabilitarPreguntasPersona(String pregunta, String valores, String campo) {		
		Object object  = null;
		String rangos[]= valores.split(",");
    try {
			object= this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).toValue(campo);
			for(String valor: rangos) {
				if((object!= null) && (valor.equals(object.toString()))) {
					this.attrs.put("deshabilitaPregunta".concat(pregunta), true);
					break;
				} // if
				else
					this.attrs.put("deshabilitaPregunta".concat(pregunta), false);			
			} // if
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
	}
	
	private void deshabilitarPreguntas(String pregunta, String valores, String campo) {		
		Object object  = null;
		String rangos[]= valores.split(",");
    try {
			object= this.cuestionario.getTrJanalFamiliasDto().toValue(campo);
			for(String valor: rangos) {
				if((object!= null) && (valor.equals(object.toString()))) {
					this.attrs.put("deshabilitaPregunta".concat(pregunta), true);
					break;
				} // if
				else {
					this.attrs.put("deshabilitaPregunta".concat(pregunta), false);								
				} // else
			} // if
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
	}

	private boolean verificarRespuestaPersonas(String valores, String campo) {		
		Boolean regresar = null;		
    try {
			Object object= this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).toValue(campo);
			regresar= (object!= null) && ((valores.indexOf(object.toString())> 0));
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
    return regresar;
  }
    
	private void limpiarRespuestaPersonas(Integer actual, String valores, String campo) {		
		Object object = null;		
    try {
			object= this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).toValue(campo);
			if((object!= null) && ((valores.indexOf(object.toString())> 0))) {
				switch (actual) {
					case 0:
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setParent(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setVresid(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setAlim(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setSalud(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setEduc(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setOtros(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setNinguno(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setAapoyo(null);
					case 1:
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setSexo(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setEdad(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setEsc(null);
					break;		
					case 2:
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setEsc(null);
					break;
					case 3:
						if(((this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).getAlim()!= null) && (!this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).getAlim().isEmpty())) ||
							((this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).getSalud()!= null) && (!this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).getSalud().isEmpty())) ||
							((this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).getEduc()!= null) && (!this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).getEduc().isEmpty())) ||
							((this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).getOtros()!= null) && (!this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).getOtros().isEmpty()))) {						
							this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setNinguno(null);
						} // if
						else {
							this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setAlim(null);
							this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setSalud(null);
							this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setEduc(null);
							this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setOtros(null);
							this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setAapoyo(null);
						} // else
					break;
					case 4:
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setEduc(null);
						this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setOtros(null);
					break;	
				} // switch
			} // if
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
	}
	
	private void limpiarEspecifica(String valores, String campo) {
		Object object = null;		
    try {
			object= this.cuestionario.getTrJanalFamiliasDto().toValue(campo);
			if((object== null) || (object.toString().isEmpty())) {				
				this.cuestionario.getTrJanalFamiliasDto().setTespeci(null);										
			} // if
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
	}
	
	private void limpiarRespuestaFamilias(Integer actual, String valores, String campo) {		
		Object object = null;		
    try {
			object= this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).toValue(campo);
			if((object!= null) && ((valores.indexOf(object.toString())> 0))) {
				switch (actual) {
					case 0:						
						this.cuestionario.getTrJanalFamiliasDto().setTespeci(null);						
					break;
					case 1:						
						this.cuestionario.getTrJanalFamiliasDto().setAtPpSat(null);						
						this.cuestionario.getTrJanalFamiliasDto().setAtPpCap(null);						
					break;
					case 2:						
						this.cuestionario.getTrJanalFamiliasDto().setCapTsat(null);						
						this.cuestionario.getTrJanalFamiliasDto().setCapTcap(null);						
					break;
					case 3:						
						this.cuestionario.getTrJanalFamiliasDto().setOrFiSat(null);						
						this.cuestionario.getTrJanalFamiliasDto().setOrFiPf(null);						
					break;
				} // switch
			} // if
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
	}
	
	private boolean validarPersonasNinguno() {
		boolean regresar= false;
		Integer contador= 0;
		Integer activos = 0;
		try {
			for(TrJanalPersonasDto persona: this.cuestionario.getPersonas()) {
				if((persona.getNinguno()!= null) && (persona.getNinguno().equals("5"))) 					
					contador++;		
				if(persona.isActivo())
					activos++;
			} // for
			regresar= contador.equals(activos);
		} // try
		catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
		return regresar;
	} 
	
	private boolean validarPersonasInexistentes() {
		boolean regresar= false;
		Integer contador= 0;
		Integer activos = 0;
		try {
			for(TrJanalPersonasDto persona: this.cuestionario.getPersonas()) {
				if((persona.getCresid()!= null) && (persona.getCresid().equals("5"))) 					
					contador++;		
				if(persona.isActivo())
					activos++;
			} // for
			regresar= contador.equals(activos);
		} // try
		catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
		return regresar;
	}
	
	private boolean validarPersonasMayor70() {
		boolean regresar= false;
		try {
			for(TrJanalPersonasDto persona: this.cuestionario.getPersonas()) {
				if((persona.getEdad()!= null) && (Numero.getInteger(persona.getEdad())>= 70) && (Numero.getInteger(persona.getEdad())< 99) && persona.isActivo() && (persona.getSalud()!= null) && (persona.getSalud().equals("2"))) {
					regresar= true;
					break;					
				} // if
			} // for
		} // try
		catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
		return regresar;
	} 
	
	private boolean validarRespuesta8Personas() {
		Integer contador= 0;
		boolean regresar= false;
		try {
			for(TrJanalPersonasDto persona: this.cuestionario.getPersonas()) {
				if(persona.isActivo() && 
					(((persona.getAlim()!= null) && (!persona.getAlim().isEmpty())) || 
					 ((persona.getSalud()!= null) && (!persona.getSalud().isEmpty())) || 
					 ((persona.getEduc()!= null) && (!persona.getEduc().isEmpty())) || 
					((persona.getOtros()!= null) && (!persona.getOtros().isEmpty())) || 
					 ((persona.getNinguno()!= null) && (!persona.getNinguno().isEmpty())))) {					
					contador++;
					break;					
				} // if
			} // for
			regresar= contador== 0;
		} // try
		catch (Exception e) {
      JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch    
		return regresar;
	}
	
  private boolean toReturnIntegrantes() {
    boolean regresar= false;
    String morePersons= this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).getVresid();
    if(morePersons!= null && morePersons.equals("1")) {
      this.attrs.put("index", "1"); 
      this.attrs.put("countPersons", this.cuestionario.getPersonas().size()); 
      this.attrs.put("addMorePersons", true); 
      regresar= true;
    } // if
    return regresar;
  }
  
	public void doAvanzar() {
    Integer actual				 = 0;
    boolean avanza				 = false;    		
    RequestContext request = RequestContext.getCurrentInstance();				
    try {			
      actual = Numero.getInteger(this.attrs.get("index").toString());			
      switch (actual) {
        case 0:
					avanza= cargarCaratula(false) && cargarIntegrantes(false);					
					break;
        case 1:		
					if(this.cuestionario.getPersonas().size()> 0) {						
						avanza= cargarIntegrantes(false);						
						limpiarRespuestaPersonas(0, "[5]", "cresid");
						limpiarRespuestaPersonas(1, "[6,7,8]", "cresid");
						limpiarRespuestaPersonas(3, "[5]", "ninguno");
            if(verificarRespuestaPersonas("[1,2,3,4]", "cresid"))
						  limpiarRespuestaPersonas(4, "[01,02,03,04,05,06,07,08,09]", "edad");
						deshabilitarPreguntasPersona("2_1", "6,7,8", "cresid");						
						deshabilitarPreguntasPersona("2_2", "5", "cresid");			
						deshabilitarPreguntasPersona("2_3", "5", "ninguno");					
						deshabilitarPreguntasPersona("4", "00,01,02", "edad");						
						deshabilitarPreguntasPersona("3_apoyo_5", "5", "ninguno");
						deshabilitarPreguntasPersona("3_edad", "00,01,02,03,04,05", "edad");
						deshabilitarPreguntasPersona("3_edad_1", "00,01,02,03,04,05,06,07,08,09,10,11,12", "edad");
						deshabilitarPreguntasPersona("3_edad_2", "00,01,02", "edad");
						cargarFamilias();
						cargarModulo();
					} // if
					else
						JsfUtilities.addMessage("Debe agregar por lo menos a una persona.");
					if(this.cuestionario.getPersonas().get(0).isActivo())
					  break;
				case 2:
          if((boolean)this.attrs.get("addMorePersons")) {
            this.attrs.put("addMorePersons", false);
            if(this.cuestionario.getPersonas().size()== (int)this.attrs.get("countPersons")) 
              if(toReturnIntegrantes())
                break;
          } // if
          else
            if(Numero.getInteger(this.attrs.get("indice").toString())+ 1== this.cuestionario.getPersonas().size()) {
              if(toReturnIntegrantes())
                break;
            } // if
          avanza= capturarPersonas(1) || (actual== 1);
          limpiarRespuestaPersonas(0, "[5]", "cresid");
          limpiarRespuestaPersonas(1, "[6,7,8]", "cresid");					
          limpiarRespuestaPersonas(3, "[5]", "ninguno");
          if(verificarRespuestaPersonas("[1,2,3,4]", "cresid"))
  					limpiarRespuestaPersonas(4, "[01,02,03,04,05,06,07,08,09]", "edad");
          deshabilitarPreguntasPersona("2_1", "6,7,8", "cresid");					
          deshabilitarPreguntasPersona("2_2", "5", "cresid");	
          deshabilitarPreguntasPersona("2_3", "5", "ninguno");					
          deshabilitarPreguntasPersona("4", "00,01,02", "edad");						
					deshabilitarPreguntasPersona("3_apoyo_5", "5", "ninguno");
					deshabilitarPreguntasPersona("3_edad", "00,01,02,03,04,05", "edad");
  				deshabilitarPreguntasPersona("3_edad_1", "00,01,02,03,04,05,06,07,08,09,10,11,12", "edad");
  				deshabilitarPreguntasPersona("3_edad_2", "00,01,02", "edad");
          deshabilitarPreguntas("13", "7", "totros");		
          if(avanza && validarPersonasNinguno()) {
            actual= 11;
            this.attrs.put("index", actual);	
          } // if
					else
						if(avanza && validarPersonasInexistentes()) {
							actual= 12;
							this.attrs.put("index", actual);	
						} // if
          if(validarRespuesta8Personas()) {
            JsfUtilities.addMessage("En la pregunta 8 tiene que estar contestada al menos una de las opciones 1,2,3,4 y 5");
            avanza= false;
          } // if
					break;
        case 3:
					limpiarEspecifica("[]", "totros");
					avanza= realizarSalto(Numero.getInteger(this.attrs.get("index").toString()), "alim");					
					if(avanza)
						break;
					else
						this.attrs.put("index", ++actual);
				case 4:										
				case 5:
					limpiarEspecifica("[]", "totros");
					avanza= realizarSalto(Numero.getInteger(this.attrs.get("index").toString()), "salud");
					deshabilitarPreguntas("13", "7", "cresid");					
					if(avanza)
						break;
					else {
						this.attrs.put("index", ++actual);				
						this.attrs.put("index", ++actual);										
					} // else
				case 6:
          avanza= validarPersonasMayor70();
					if(avanza) 
						break;
          else
  				  this.attrs.put("index", ++actual);
				case 7:
					avanza= realizarSaltoEdad(Numero.getInteger(this.attrs.get("index").toString()), "educ");
					if(avanza)
						break;
					else
						this.attrs.put("index", ++actual);
				case 8:						
					limpiarRespuestaFamilias(1, "[2]", "atPp");
					limpiarRespuestaFamilias(2, "[2]", "capT");
					limpiarRespuestaFamilias(3, "[2]", "orFi");
					avanza= realizarSaltoEdad(Numero.getInteger(this.attrs.get("index").toString()), "otros");
					deshabilitarPreguntas("51", "2", "atPp");
					deshabilitarPreguntas("54", "2", "capT");
					deshabilitarPreguntas("57", "2", "orFi");
					if(avanza)
						break;
					else {
						this.attrs.put("index", ++actual);				
						this.attrs.put("index", ++actual);				
					} // else
				case 9:
					avanza= true;
				case 10:
					avanza= true;
					break;
				case 11:					
					avanza= true;
					break;    				
				case 12:
					RequestContext.getCurrentInstance().execute("retorno();");
					break;
      } // switch
			doAceptar(false);
      if (avanza)		
        this.attrs.put("index", ++actual);
    } // try 
    catch (Exception e) {  
      if (e.getCause() != null) 
        JsfUtilities.addMessage(e.getCause().getMessage());
      else
			  JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch   
  }//doAvanzar  
	
	public void doRetroceder() {
		Integer actual = 0;
    boolean avanza = false;    
    RequestContext request = RequestContext.getCurrentInstance();
    try {			
      actual = Numero.getInteger(this.attrs.get("index").toString());
      switch (actual) {
				case 12:
					if((validarPersonasNinguno()) || (validarPersonasInexistentes())) {
						actual= 3;
					  this.attrs.put("index", actual);												
					} // if						
					avanza= true;
					break;
				case 11:					
					avanza= realizarSaltoEdad(Numero.getInteger(this.attrs.get("index").toString()), "otros");
					deshabilitarPreguntas("51", "2", "atPp");
					deshabilitarPreguntas("54", "2", "capT");
					deshabilitarPreguntas("57", "2", "orFi");
					if(avanza)
						break;
					else {
						this.attrs.put("index", --actual);						
						this.attrs.put("index", --actual);						
					} // else
				case 10:					
        case 9:	
					deshabilitarPreguntas("51", "2", "atPp");
					deshabilitarPreguntas("54", "2", "capT");
					deshabilitarPreguntas("57", "2", "orFi");
					avanza= realizarSaltoEdad(Numero.getInteger(this.attrs.get("index").toString()), "educ");
					if(avanza)
						break;
					else
						this.attrs.put("index", --actual);
				case 8:
					avanza= validarPersonasMayor70();
					if(avanza)
						break;
					else
						this.attrs.put("index", --actual);					
				case 7:					
				case 6:				
					avanza= realizarSalto(Numero.getInteger(this.attrs.get("index").toString()), "salud");
					if(avanza)
						break;
					else {
						this.attrs.put("index", --actual);
						this.attrs.put("index", --actual);
					} // else									
				case 5:				
					avanza= realizarSalto(Numero.getInteger(this.attrs.get("index").toString()), "alim");
					if(avanza)
						break;
					else
						this.attrs.put("index", --actual);
				case 4:
					avanza= true;		
					deshabilitarPreguntas("13", "7", "totros");
					break;
				case 3:
					avanza= true;		
					limpiarEspecifica("[]", "totros");
					limpiarRespuestaPersonas(0, "[5]", "cresid");
					limpiarRespuestaPersonas(1, "[6,7,8]", "cresid");
 				  limpiarRespuestaPersonas(3, "[5]", "ninguno");
          if(verificarRespuestaPersonas("[1,2,3,4]", "cresid"))
					  limpiarRespuestaPersonas(4, "[01,02,03,04,05,06,07,08,09]", "edad");
					deshabilitarPreguntasPersona("2_1", "6,7,8", "cresid");
					deshabilitarPreguntasPersona("2_2", "5", "cresid");	
					deshabilitarPreguntasPersona("2_3", "5", "ninguno");	
					deshabilitarPreguntasPersona("4", "00,01,02", "edad");							
					deshabilitarPreguntasPersona("3_apoyo_5", "5", "ninguno");
 				  deshabilitarPreguntasPersona("3_edad", "00,01,02,03,04,05", "edad");
  				deshabilitarPreguntasPersona("3_edad_1", "00,01,02,03,04,05,06,07,08,09,10,11,12", "edad");
					deshabilitarPreguntasPersona("3_edad_2", "00,01,02", "edad");
					break;
				case 2:									
					avanza= capturarPersonas(-1);
					limpiarRespuestaPersonas(0, "[5]", "cresid");
					limpiarRespuestaPersonas(1, "[6,7,8]", "cresid");
					limpiarRespuestaPersonas(3, "[5]", "ninguno");
          if(verificarRespuestaPersonas("[1,2,3,4]", "cresid"))
            limpiarRespuestaPersonas(4, "[01,02,03,04,05,06,07,08,09]", "edad");
					deshabilitarPreguntasPersona("2_1", "6,7,8", "cresid");
					deshabilitarPreguntasPersona("2_2", "5", "cresid");				
					deshabilitarPreguntasPersona("2_3", "5", "ninguno");	
					deshabilitarPreguntasPersona("4", "00,01,02", "edad");					
					deshabilitarPreguntasPersona("3_apoyo_5", "5", "ninguno");
					deshabilitarPreguntasPersona("3_edad", "00,01,02,03,04,05", "edad");
  				deshabilitarPreguntasPersona("3_edad_1", "00,01,02,03,04,05,06,07,08,09,10,11,12", "edad");
  				deshabilitarPreguntasPersona("3_edad_2", "00,01,02", "edad");
					break;
				case 1:
					avanza= true;
					break;
				case 0:
      } // switch
      if (avanza) {
        this.attrs.put("index", --actual);        
      } // if            
			request.execute("janal.refresh();");
			String valuesUpdate = "index,aceptar,cancelar,anterior,cuestionario";
			request.update(Arrays.asList(valuesUpdate.split(",")));
    } // try
    catch (Exception e) {
      JsfUtilities.addMessageError(e);
      if (e.getCause() != null) {
        JsfUtilities.addMessage(e.getCause().getMessage());
      } // if
      Error.mensaje(e);
    } // catch   
  } // doRetroceder  
	
	public void doCargarIntegrante() {
		StringBuilder integrantes= null;
		try {
			integrantes= new StringBuilder();						
			if(this.cuestionario.getPersonas().size()< 50) {				
				if((this.trJanalPersonaDto.getNombre()!= null) && (!this.trJanalPersonaDto.getNombre().isEmpty()) && 
					 (this.trJanalPersonaDto.getApaterno()!= null) && (!this.trJanalPersonaDto.getApaterno().isEmpty())) {					 
					this.trJanalPersonaDto.setActivo("1");
					this.cuestionario.getPersonas().add(this.trJanalPersonaDto);
					for(TrJanalPersonasDto integrante: this.cuestionario.getPersonas()) {
						doContruirIntegrante(integrantes, integrante);					
					} // for
					this.attrs.put("pintarIntegrantes", integrantes.toString());
					this.trJanalPersonaDto= new TrJanalPersonasDto();
					this.trJanalPersonaDto.setRg(String.valueOf(this.cuestionario.getPersonas().size()+ 1));				
				} // if
				else
					JsfUtilities.addMessage("Debe capturar nombre y apellido paterno.");
			} // if
			else
				JsfUtilities.addMessage("El limite de integrantes por cuestionario es de 10.");
			if((boolean)this.attrs.get("addMorePersons")) {
				this.cuestionario.getPersonas().get(Numero.getInteger(this.attrs.get("indice").toString())).setVresid(null);
				this.attrs.put("indice", Numero.getInteger(this.attrs.get("indice").toString())+ 1);
				this.attrs.put("addMorePersons", false);
			} // if
		} // try
		catch (Exception e) {
      JsfUtilities.addMessageError(e);            
      Error.mensaje(e);
    } // catch   
	}
	
	public void doContruirIntegrante(StringBuilder integrante, TrJanalPersonasDto trJanalPersonasDto) {		
		try {			
			integrante.append("<tr id=\"row-").append(trJanalPersonasDto.getRg()).append("\" class=\"ui-widget-content ").append(trJanalPersonasDto.isDepurado()? "DispNone": "").append("\" role=\"row\">");
			integrante.append("	<td role=\"gridcell\" class=\"ui-panelgrid-cell TexAlCenter MarAuto\">");
			integrante.append("		<label><input value=\"").append(trJanalPersonasDto.getRg()).append("\" id=\"cuestionario:pagina2_informante\" name=\"cuestionario:pagina2_informante\" type=\"radio\" onclick=\"activePerson('").append(trJanalPersonasDto.getRg()).append("');\" ").append(trJanalPersonasDto.getFilter1()!= null? "checked": "").append("><div><div/><label/>");
			integrante.append("	</td>");
			integrante.append("	<td role=\"gridcell\" class=\"ui-panelgrid-cell TexAlCenter MarAuto\">");
			integrante.append("		<input value=\"").append(trJanalPersonasDto.getRg()).append("\" id=\"cuestionario:pagina2_renglon_").append(trJanalPersonasDto.getRg()).append("\" name=\"cuestionario:pagina2_renglon_").append(trJanalPersonasDto.getRg()).append("\" type=\"text\" style=\"width: 15px\" class=\"ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all TexAlLeft ui-state-disabled delete-key-").append(trJanalPersonasDto.getRg()).append("\" role=\"textbox\" aria-disabled=\"false\" aria-readonly=\"false\">");
			integrante.append("	</td>");
			integrante.append(" <td role=\"gridcell\" class=\"ui-panelgrid-cell\">");
			integrante.append("		<input value=\"").append(trJanalPersonasDto.getApaterno()).append("\" id=\"cuestionario:pagina2_apellido_paterno_").append(trJanalPersonasDto.getRg()).append("\" name=\"cuestionario:pagina2_apellido_paterno_").append(trJanalPersonasDto.getRg()).append("\" type=\"text\" placeholder=\"apellido paterno\" style=\"min-width: 100px\" class=\"ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all TexAlLeft Wid90 delete-key-").append(trJanalPersonasDto.getRg()).append("\" role=\"textbox\" aria-disabled=\"false\" aria-readonly=\"false\">");
			integrante.append("	</td>");
			integrante.append("		<td role=\"gridcell\" class=\"ui-panelgrid-cell\">");
			integrante.append("		<input value=\"").append(trJanalPersonasDto.getAmaterno()).append("\" id=\"cuestionario:pagina2_apellido_materno_").append(trJanalPersonasDto.getRg()).append("\" name=\"cuestionario:pagina2_apellido_materno_").append(trJanalPersonasDto.getRg()).append("\" type=\"text\" placeholder=\"apellido materno\" style=\"min-width: 100px\" class=\"ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all TexAlLeft Wid90 delete-key-").append(trJanalPersonasDto.getRg()).append("\" role=\"textbox\" aria-disabled=\"false\" aria-readonly=\"false\">");
			integrante.append("	</td>");
			integrante.append("	<td role=\"gridcell\" class=\"ui-panelgrid-cell\">");
			integrante.append("		<input value=\"").append(trJanalPersonasDto.getNombre()).append("\" id=\"cuestionario:pagina2_nombre_").append(trJanalPersonasDto.getRg()).append("\" name=\"cuestionario:pagina2_nombre_").append(trJanalPersonasDto.getRg()).append("\" type=\"text\" placeholder=\"nombre\" style=\"min-width: 100px\" class=\"ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all TexAlLeft Wid90 delete-key-").append(trJanalPersonasDto.getRg()).append("\" role=\"textbox\" aria-disabled=\"false\" aria-readonly=\"false\">");
			integrante.append("	</td>");			
			integrante.append("	<td role=\"gridcell\" class=\"ui-panelgrid-cell\">");
      if(trJanalPersonasDto.isNuevo())
			  integrante.append("		<button class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only delete-key-").append(trJanalPersonasDto.getRg()).append("\" onclick=\"disabledPerson('").append(trJanalPersonasDto.getRg()).append("');\" title=\"Eliminar\" type=\"button\" role=\"button\" aria-disabled=\"false\"><span class=\"ui-button-icon-left ui-icon fa fa-lg fa-close\"></span><span class=\"ui-button-text ui-c\">ui-button</span></button>");
			integrante.append("	</td>");
			integrante.append("</tr>");			
      if(RequestContext.getCurrentInstance()!= null && trJanalPersonasDto.isDepurado()) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("disabledPerson('".concat(trJanalPersonasDto.getRg()).concat("');"));
      } // if    
		}
		catch (Exception e) {
      JsfUtilities.addMessageError(e);            
      Error.mensaje(e);
    } // catch   
	}
  
  public void delete(final String rowId, final String active) {
   for(TrJanalPersonasDto persona: this.cuestionario.getPersonas()) 				
		 if(persona.getRg().equals(rowId))
       persona.setActivo(active);
  }
	
  public void activeInformante(final String rowId) {
   for(TrJanalPersonasDto persona: this.cuestionario.getPersonas()) 				
		 if(persona.getRg().equals(rowId))
       persona.setFilter1("1");
     else
       persona.setFilter1(null);
  }
	
	public void deshabilitarPreguntasRemote(String pregunta, String respuesta) {
		Integer caso= Numero.getInteger(pregunta);
		switch(caso) {
			case 8:
				limpiarRespuestaPersonas(3, "[1,2,3,4]", respuesta);
			break;
			case 10:
				if(this.cuestionario.getTrJanalFamiliasDto().getMpFrRe().equals(4L)) {
					this.cuestionario.getTrJanalFamiliasDto().setMpAsist(null);
					this.cuestionario.getTrJanalFamiliasDto().setMpInf(null);
					this.cuestionario.getTrJanalFamiliasDto().setTsalud(null);
					this.cuestionario.getTrJanalFamiliasDto().setTalim(null);
					this.cuestionario.getTrJanalFamiliasDto().setTadic(null);
					this.cuestionario.getTrJanalFamiliasDto().setTplanfa(null);
					this.cuestionario.getTrJanalFamiliasDto().setTedu(null);
					this.cuestionario.getTrJanalFamiliasDto().setTprogra(null);
					this.cuestionario.getTrJanalFamiliasDto().setTotros(null);
					this.cuestionario.getTrJanalFamiliasDto().setTespeci(null);
					this.cuestionario.getTrJanalFamiliasDto().setMpTmpo(null);
					this.cuestionario.getTrJanalFamiliasDto().setMpTrto(null);
					this.attrs.put("deshabilitaPregunta10", true);
				} // if
				else
					this.attrs.put("deshabilitaPregunta10", false);
			break;
			case 51:
				if(this.cuestionario.getTrJanalFamiliasDto().getAtPp().equals(2L)) {
					this.cuestionario.getTrJanalFamiliasDto().setAtPpSat(null);
					this.cuestionario.getTrJanalFamiliasDto().setAtPpCap(null);
					this.attrs.put("deshabilitaPregunta51", true);
				} // if				
				else
					this.attrs.put("deshabilitaPregunta51", false);
			break;			  
			case 54:
				if(this.cuestionario.getTrJanalFamiliasDto().getCapT().equals(2L)) {
					this.cuestionario.getTrJanalFamiliasDto().setCapTsat(null);
					this.cuestionario.getTrJanalFamiliasDto().setCapTcap(null);
					this.attrs.put("deshabilitaPregunta54", true);
				} // if
				else
					this.attrs.put("deshabilitaPregunta54", false);
			break;
			case 57:
				if(this.cuestionario.getTrJanalFamiliasDto().getOrFi().equals("2")) {
					this.cuestionario.getTrJanalFamiliasDto().setOrFiSat(null);
					this.cuestionario.getTrJanalFamiliasDto().setOrFiPf(null);
					this.attrs.put("deshabilitaPregunta57", true);
				} // if
				else
					this.attrs.put("deshabilitaPregunta57", false);
			break;			
			case 60:
				if(this.cuestionario.getTrJanalFamiliasDto().getApSoc().equals("2")) {
					this.cuestionario.getTrJanalFamiliasDto().setApSocSat(null);
					this.cuestionario.getTrJanalFamiliasDto().setApSocNv(null);
					this.attrs.put("deshabilitaPregunta60", true);
				} // if
				else
					this.attrs.put("deshabilitaPregunta60", false);
			break;			
		} // deshabilitarPreguntas	
	} // 
  
}


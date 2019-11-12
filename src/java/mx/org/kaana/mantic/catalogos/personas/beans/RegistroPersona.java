package mx.org.kaana.mantic.catalogos.personas.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.clientes.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.personas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

public class RegistroPersona implements Serializable{

	private static final long serialVersionUID= -8922183204418674058L;
	private Long idPersona;
	private TcManticPersonasDto persona;
	private List<PersonaDomicilio> personasDomicilio;
	private PersonaDomicilio personaDomicilioSelecion;
	private List<PersonaTipoContacto> personasTiposContacto;
	private PersonaTipoContacto personaTipoContactoSeleccion;	
	private List<IBaseDto> deleteList;
	private ContadoresListas contadores;
	private Long countIndice;
	private Domicilio domicilio;
	private Domicilio domicilioPivote;
	private Long idPuesto;
	private Long idEmpresa;
	
	public RegistroPersona() {
		this(-1L, new TcManticPersonasDto(), new ArrayList<PersonaDomicilio>(), new ArrayList<PersonaTipoContacto>(), new Domicilio());
	}
	
	public RegistroPersona(Long idPersona) {
		this.idPersona  = idPersona;
		this.contadores = new ContadoresListas();
		this.countIndice= 0L;
		this.deleteList = new ArrayList<>();
		this.domicilio  = new Domicilio();
		this.domicilioPivote= new Domicilio();
		init();		
	}
	
	public RegistroPersona(Long idPersona, TcManticPersonasDto persona, List<PersonaDomicilio> personasDomicilio, List<PersonaTipoContacto> personasTiposContacto, Domicilio domicilio) {
		this.idPersona             = idPersona;
		this.persona               = persona;
		this.personasDomicilio     = personasDomicilio;
		this.personasTiposContacto = personasTiposContacto;
		this.deleteList            = new ArrayList<>();
		this.contadores            = new ContadoresListas();
		this.countIndice           = 0L;
		this.domicilio             = domicilio;
		this.domicilioPivote       = domicilio;
		this.idPuesto              = -1L;
		this.idEmpresa             = -1L;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public TcManticPersonasDto getPersona() {
		return persona;
	}

	public void setPersona(TcManticPersonasDto persona) {
		this.persona = persona;
	}

	public List<PersonaDomicilio> getPersonasDomicilio() {
		return personasDomicilio;
	}

	public void setPersonasDomicilio(List<PersonaDomicilio> personasDomicilio) {
		this.personasDomicilio = personasDomicilio;
	}

	public PersonaDomicilio getPersonaDomicilioSelecion() {
		return personaDomicilioSelecion;
	}

	public void setPersonaDomicilioSelecion(PersonaDomicilio personaDomicilioSelecion) {
		this.personaDomicilioSelecion = personaDomicilioSelecion;
	}

	public List<PersonaTipoContacto> getPersonasTiposContacto() {
		return personasTiposContacto;
	}

	public void setPersonasTiposContacto(List<PersonaTipoContacto> personasTiposContacto) {
		this.personasTiposContacto = personasTiposContacto;
	}

	public PersonaTipoContacto getPersonaTipoContactoSeleccion() {
		return personaTipoContactoSeleccion;
	}

	public void setPersonaTipoContactoSeleccion(PersonaTipoContacto personaTipoContactoSeleccion) {
		this.personaTipoContactoSeleccion = personaTipoContactoSeleccion;
	}

	public List<IBaseDto> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<IBaseDto> deleteList) {
		this.deleteList = deleteList;
	}		

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}		

	public Domicilio getDomicilioPivote() {
		return domicilioPivote;
	}

	public void setDomicilioPivote(Domicilio domicilioPivote) {
		this.domicilioPivote = domicilioPivote;
	}

	public Long getIdPuesto() {
		return idPuesto;
	}

	public void setIdPuesto(Long idPuesto) {
		this.idPuesto = idPuesto;
	}	

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}	
	
	private void init(){
		MotorBusqueda motorBusqueda= null;
		try {
			motorBusqueda= new MotorBusqueda(this.idPersona);
			this.persona= motorBusqueda.toPersona();									
			initCollections(motorBusqueda);
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // init
	
	private void initCollections(MotorBusqueda motor) throws Exception{
		int count= 0;
		try {
			this.personasDomicilio= motor.toPersonasDomicilio(true);
			for(PersonaDomicilio personaDomicilio: this.personasDomicilio){
				count++;
				personaDomicilio.setConsecutivo(Long.valueOf(count));
			} // for				
			this.personasTiposContacto= motor.toPersonasTipoContacto();		
			this.idPuesto= motor.toPuestoPersona();
			this.idEmpresa= motor.toEmpresaPersona();
		} // try
		catch (Exception e) {
			Error.mensaje(e);			
			throw e;
		} // catch		
	} // initCollections
	
	public void doAgregarClienteDomicilio(){
		PersonaDomicilio personaDomicilio= null;
		try {								
			personaDomicilio= new PersonaDomicilio(this.contadores.getTotalPersonasDomicilios() + this.countIndice, ESql.INSERT, true);	
			setValuesPersonaDomicilio(personaDomicilio, false);			
			this.personasDomicilio.add(personaDomicilio);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarClienteDomicilio
	
	public void doEliminarClienteDomicilio(){
		try {			
			if(this.personasDomicilio.remove(this.personaDomicilioSelecion)){
				if(!this.personaDomicilioSelecion.getNuevo())
					addDeleteList(this.personaDomicilioSelecion);
				JsfBase.addMessage("Se eliminó correctamente el domicilio", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el domicilio", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarClienteDomicilio	
	
	public void doConsultarClienteDomicilio(){
		PersonaDomicilio pivote= null;
		try {			
			pivote= this.personasDomicilio.get(this.personasDomicilio.indexOf(this.personaDomicilioSelecion));
			pivote.setModificar(true);
			this.domicilioPivote= new Domicilio();
			this.domicilioPivote.setIdTipoDomicilio(pivote.getIdTipoDomicilio());
			this.domicilioPivote.setPrincipal(pivote.getIdPrincipal().equals(1L));	
			this.domicilioPivote.setIdDomicilio(pivote.getDomicilio().getKey());
			this.domicilioPivote.setDomicilio(pivote.getDomicilio());
			this.domicilioPivote.setIdEntidad(pivote.getIdEntidad());
			this.domicilioPivote.setIdMunicipio(pivote.getIdMunicipio());
			this.domicilioPivote.setLocalidad(pivote.getIdLocalidad());
			this.domicilioPivote.setIdLocalidad(pivote.getIdLocalidad().getKey());
			this.domicilioPivote.setCodigoPostal(pivote.getCodigoPostal());
			this.domicilioPivote.setCalle(pivote.getCalle());
			this.domicilioPivote.setNumeroExterior(pivote.getExterior());
			this.domicilioPivote.setNumeroInterior(pivote.getInterior());
			this.domicilioPivote.setAsentamiento(pivote.getColonia());
			this.domicilioPivote.setEntreCalle(pivote.getEntreCalle());
			this.domicilioPivote.setYcalle(pivote.getyCalle());
			this.domicilioPivote.setNuevoCp(pivote.getCodigoPostal()!= null && !Cadena.isVacio(pivote.getCodigoPostal()));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doConsultarClienteDomicilio
	
	public void doActualizarClienteDomicilio(){
		PersonaDomicilio pivote= null;
		try {			
			pivote= this.personasDomicilio.get(this.personasDomicilio.indexOf(this.personaDomicilioSelecion));			
			pivote.setModificar(false);
			setValuesPersonaDomicilio(pivote, true);						
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doActualizarClienteDomicilio
	
	private void setValuesPersonaDomicilio(PersonaDomicilio personaDomicilio, boolean actualizar) throws Exception{
		try {
			if(this.domicilio.getPrincipal()){
				for(PersonaDomicilio record: this.personasDomicilio)
					record.setIdPrincipal(0L);
			} // if
			personaDomicilio.setIdPrincipal(this.domicilio.getPrincipal() ? 1L : 2L);			
			personaDomicilio.setDomicilio(this.domicilio.getDomicilio());
			personaDomicilio.setIdDomicilio(this.domicilio.getDomicilio().getKey());
			personaDomicilio.setIdUsuario(JsfBase.getIdUsuario());
			personaDomicilio.setIdTipoDomicilio(this.domicilio.getIdTipoDomicilio());
			if(!actualizar)
				personaDomicilio.setConsecutivo(this.personasDomicilio.size() + 1L);
			personaDomicilio.setIdEntidad(this.domicilio.getIdEntidad());
			personaDomicilio.setIdMunicipio(this.domicilio.getIdMunicipio());
			personaDomicilio.setIdLocalidad(this.domicilio.getLocalidad());
			personaDomicilio.setCodigoPostal(this.domicilio.getCodigoPostal());
			personaDomicilio.setCalle(this.domicilio.getCalle());
			personaDomicilio.setExterior(this.domicilio.getNumeroExterior());
			personaDomicilio.setInterior(this.domicilio.getNumeroInterior());
			personaDomicilio.setEntreCalle(this.domicilio.getEntreCalle());
			personaDomicilio.setyCalle(this.domicilio.getYcalle());
			personaDomicilio.setColonia(this.domicilio.getAsentamiento());
			personaDomicilio.setNuevoCp(this.domicilio.getCodigoPostal()!= null && !Cadena.isVacio(this.domicilio.getCodigoPostal()));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // setValuesClienteDomicilio
	
	public void doAgregarClienteTipoContacto(){
		PersonaTipoContacto personaTipoContacto= null;
		try {					
			personaTipoContacto= new PersonaTipoContacto(this.contadores.getTotalPersonasTipoContacto()+ this.countIndice, ESql.INSERT, true, null);				
			personaTipoContacto.setOrden(this.personasTiposContacto.size() + 1L);
			this.personasTiposContacto.add(personaTipoContacto);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarClienteTipoContacto
	
	public void doEliminarClienteTipoContacto(){
		try {			
			if(this.personasTiposContacto.remove(this.personaTipoContactoSeleccion)){
				if(!this.personaTipoContactoSeleccion.getNuevo())
					addDeleteList(this.personaTipoContactoSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el tipo de contacto", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el tipo de contacto", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarClienteTipoContacto
	
	private void addDeleteList(IBaseDto dto) throws Exception{
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(dto);
			transaccion.ejecutar(EAccion.DEPURAR);
			//this.deleteList.add(dto);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // addDeleteList
	
	private Long registrarDomicilio() throws Exception{
		Long regresar= -1L;
		try {
			this.domicilio.setIdUsuario(JsfBase.getIdUsuario());
			regresar= DaoFactory.getInstance().insert(this.domicilio);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarDomicilio	
}

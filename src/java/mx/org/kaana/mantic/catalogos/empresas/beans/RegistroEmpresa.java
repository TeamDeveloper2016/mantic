package mx.org.kaana.mantic.catalogos.empresas.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.empresas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.empresas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDto;

public class RegistroEmpresa implements Serializable{

	private static final long serialVersionUID = -3264962556592212530L;
	private Long idEmpresa;
	private TcManticEmpresasDto empresa;
	private List<EmpresaDomicilio> empresasDomicilio;
	private EmpresaDomicilio empresaDomicilioSelecion;
	private List<EmpresaTipoContacto> empresasTiposContacto;
	private EmpresaTipoContacto empresaTipoContactoSeleccion;
	private List<IBaseDto> deleteList;
	private ContadoresListas contadores;
	private Long countIndice;
	private Domicilio domicilio;
	private Domicilio domicilioPivote;
	
	public RegistroEmpresa() {
		this(-1L, new TcManticEmpresasDto(), new ArrayList<EmpresaDomicilio>(), new ArrayList<EmpresaTipoContacto>(), new Domicilio());
	}
	
	public RegistroEmpresa(Long idEmpresa) {
		this.idEmpresa  = idEmpresa;
	  this.contadores = new ContadoresListas();
		this.countIndice= 0L;
		this.deleteList = new ArrayList<>();
		this.domicilio  = new Domicilio();
		this.domicilioPivote= new Domicilio();		
		init();		
	} // RegistroEmpresa
	
	public RegistroEmpresa(Long idEmpresa, TcManticEmpresasDto empresa, List<EmpresaDomicilio> empresasDomicilio, List<EmpresaTipoContacto> empresasTiposContactos, Domicilio domicilio) {
		this.idEmpresa             = idEmpresa;
		this.empresa               = empresa;
		this.empresasDomicilio     = empresasDomicilio;
		this.empresasTiposContacto = empresasTiposContactos;
		this.deleteList            = new ArrayList<>();
		this.contadores            = new ContadoresListas();
		this.countIndice           = 0L;
		this.domicilio             = domicilio;
		this.domicilioPivote       = domicilio;		
	} // RegistroEmpresa

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public TcManticEmpresasDto getEmpresa() {
		return empresa;
	}

	public void setEmpresa(TcManticEmpresasDto empresa) {
		this.empresa = empresa;
	}

	public List<EmpresaDomicilio> getEmpresasDomicilio() {
		return empresasDomicilio;
	}

	public void setEmpresasDomicilio(List<EmpresaDomicilio> empresasDomicilio) {
		this.empresasDomicilio = empresasDomicilio;
	}

	public EmpresaDomicilio getEmpresaDomicilioSelecion() {
		return empresaDomicilioSelecion;
	}

	public void setEmpresaDomicilioSelecion(EmpresaDomicilio empresaDomicilioSelecion) {
		this.empresaDomicilioSelecion = empresaDomicilioSelecion;
	}

	public List<EmpresaTipoContacto> getEmpresasTiposContacto() {
		return empresasTiposContacto;
	}

	public void setEmpresasTiposContacto(List<EmpresaTipoContacto> empresasTiposContacto) {
		this.empresasTiposContacto = empresasTiposContacto;
	}

	public EmpresaTipoContacto getEmpresaTipoContactoSeleccion() {
		return empresaTipoContactoSeleccion;
	}

	public void setEmpresaTipoContactoSeleccion(EmpresaTipoContacto empresaTipoContactoSeleccion) {
		this.empresaTipoContactoSeleccion = empresaTipoContactoSeleccion;
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
	
	private void init(){
		MotorBusqueda motorBusqueda= null;
		try {
			motorBusqueda= new MotorBusqueda(this.idEmpresa);
			this.empresa= motorBusqueda.toEmpresa();									
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
			this.empresasDomicilio= motor.toEmpresasDomicilio(true);
			for(EmpresaDomicilio empresaDomicilio: this.empresasDomicilio){
				count++;
				empresaDomicilio.setConsecutivo(Long.valueOf(count));
			} // for				
			this.empresasTiposContacto= motor.toEmpresasTipoContacto();			
		} // try
		catch (Exception e) {
			Error.mensaje(e);			
			throw e;
		} // catch		
	} // initCollections
	
	public void doAgregarEmpresaDomicilio(){
		EmpresaDomicilio empresaDomicilio= null;
		try {								
			empresaDomicilio= new EmpresaDomicilio(this.contadores.getTotalEmpresasDomicilios() + this.countIndice, ESql.INSERT, true);	
			setValuesEmpresaDomicilio(empresaDomicilio, false);			
			this.empresasDomicilio.add(empresaDomicilio);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarEmpresaDomicilio
	
	public void doEliminarEmpresaDomicilio(){
		try {			
			if(this.empresasDomicilio.remove(this.empresaDomicilioSelecion)){
				if(!this.empresaDomicilioSelecion.getNuevo())
					addDeleteList(this.empresaDomicilioSelecion);
				JsfBase.addMessage("Se eliminó correctamente el domicilio", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el domicilio", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEmpresaDomicilio	
	
	public void doConsultarEmpresaDomicilio(){
		EmpresaDomicilio pivote= null;
		try {			
			pivote= this.empresasDomicilio.get(this.empresasDomicilio.indexOf(this.empresaDomicilioSelecion));
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
	} // doConsultarEmpresaDomicilio
	
	public void doActualizarEmpresaDomicilio(){
		EmpresaDomicilio pivote= null;
		try {			
			pivote= this.empresasDomicilio.get(this.empresasDomicilio.indexOf(this.empresaDomicilioSelecion));			
			pivote.setModificar(false);
			setValuesEmpresaDomicilio(pivote, true);						
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doActualizarEmpresaDomicilio
	
	private void setValuesEmpresaDomicilio(EmpresaDomicilio empresaDomicilio, boolean actualizar) throws Exception{
		try {
			if(this.domicilio.getPrincipal()){
				for(EmpresaDomicilio record: this.empresasDomicilio)
					record.setIdPrincipal(0L);
			} // if
			empresaDomicilio.setIdPrincipal(this.domicilio.getPrincipal() ? 1L : 2L);			
			empresaDomicilio.setDomicilio(this.domicilio.getDomicilio());
			empresaDomicilio.setIdDomicilio(this.domicilio.getDomicilio().getKey());
			empresaDomicilio.setIdUsuario(JsfBase.getIdUsuario());
			empresaDomicilio.setIdTipoDomicilio(this.domicilio.getIdTipoDomicilio());
			if(!actualizar)
				empresaDomicilio.setConsecutivo(this.empresasDomicilio.size() + 1L);
			empresaDomicilio.setIdEntidad(this.domicilio.getIdEntidad());
			empresaDomicilio.setIdMunicipio(this.domicilio.getIdMunicipio());
			empresaDomicilio.setIdLocalidad(this.domicilio.getLocalidad());
			empresaDomicilio.setCodigoPostal(this.domicilio.getCodigoPostal());
			empresaDomicilio.setCalle(this.domicilio.getCalle());
			empresaDomicilio.setExterior(this.domicilio.getNumeroExterior());
			empresaDomicilio.setInterior(this.domicilio.getNumeroInterior());
			empresaDomicilio.setEntreCalle(this.domicilio.getEntreCalle());
			empresaDomicilio.setyCalle(this.domicilio.getYcalle());
			empresaDomicilio.setColonia(this.domicilio.getAsentamiento());
			empresaDomicilio.setNuevoCp(this.domicilio.getCodigoPostal()!= null && !Cadena.isVacio(this.domicilio.getCodigoPostal()));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // setValuesEmpresaDomicilio
	
	public void doAgregarEmpresaTipoContacto(){
		EmpresaTipoContacto empresaTipoContacto= null;
		try {					
			empresaTipoContacto= new EmpresaTipoContacto(this.contadores.getTotalEmpresasTipoContacto()+ this.countIndice, ESql.INSERT, true);				
			empresaTipoContacto.setOrden(this.empresasTiposContacto.size() + 1L);
			this.empresasTiposContacto.add(empresaTipoContacto);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarClienteTipoContacto
	
	public void doEliminarEmpresaTipoContacto(){
		try {			
			if(this.empresasTiposContacto.remove(this.empresaTipoContactoSeleccion)){
				if(!this.empresaTipoContactoSeleccion.getNuevo())
					addDeleteList(this.empresaTipoContactoSeleccion);
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
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // addDeleteList
}

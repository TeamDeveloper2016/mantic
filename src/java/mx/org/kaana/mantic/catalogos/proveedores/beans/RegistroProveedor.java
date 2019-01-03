package mx.org.kaana.mantic.catalogos.proveedores.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Condicion;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresPortalesDto;
import mx.org.kaana.mantic.enums.ETiposCuentas;

public class RegistroProveedor implements Serializable{
	
	private static final long serialVersionUID = 4690869520445115664L;
	private Long idProveedor;
	private TcManticProveedoresDto proveedor;
	private TcManticProveedoresPortalesDto portal;
	private List<ProveedorDomicilio> proveedoresDomicilio;
	private ProveedorDomicilio proveedorDomicilioSeleccion;
	private List<ProveedorTipoContacto> proveedoresTipoContacto;
	private ProveedorTipoContacto proveedorTipoContactoSeleccion;
	private List<ProveedorBanca> proveedoresServicio;
	private ProveedorBanca proveedorServicioSeleccion;
	private List<ProveedorBanca> proveedoresTransferencia;
	private ProveedorBanca proveedorTransferenciaSeleccion;
	private List<IBaseDto> deleteList;
	private ContadoresListas contadores;
	private Long countIndice;
	private Domicilio domicilio;
	private Domicilio domicilioPivote;
	private List<ProveedorContactoAgente> personasTiposContacto;
	private PersonaTipoContacto personaTipoContactoSeleccion;
	private ProveedorContactoAgente personaTipoContactoPivote;
	private ProveedorContactoAgente personaTipoContacto;
	private List<ProveedorCondicionPago> proveedoresCondicionPago;
	private ProveedorCondicionPago proveedorCondicionPagoSeleccion;

	public RegistroProveedor() {
		this(-1L, new TcManticProveedoresDto(), new ProveedorDomicilio(), new ArrayList<ProveedorTipoContacto>(), new Domicilio(), new Domicilio(), new ArrayList<ProveedorContactoAgente>(), new ProveedorContactoAgente(), new ProveedorContactoAgente(), new ArrayList<ProveedorCondicionPago>(), new ArrayList<ProveedorDomicilio>(), new TcManticProveedoresPortalesDto(), new ArrayList<ProveedorBanca>(), new ArrayList<ProveedorBanca>());
	} // RegistroProveedor

	public RegistroProveedor(Long idProveedor) {
		this.idProveedor              = idProveedor;
		this.contadores               = new ContadoresListas();
		this.countIndice              = 0L;
		this.deleteList               = new ArrayList<>();
		this.domicilio                = new Domicilio();
		this.domicilioPivote          = new Domicilio();
		this.personaTipoContactoPivote= new ProveedorContactoAgente();
		this.personaTipoContacto      = new ProveedorContactoAgente();
		init();
	} // RegistroProveedor

	public RegistroProveedor(Long idProveedor, TcManticProveedoresDto proveedor, ProveedorDomicilio proveedorDomicilioSeleccion, List<ProveedorTipoContacto> proveedoresTipoContacto, Domicilio domicilio, Domicilio domicilioPivote, List<ProveedorContactoAgente> personasTiposContacto, ProveedorContactoAgente personaTipoContactoPivote, ProveedorContactoAgente personaTipoContacto, List<ProveedorCondicionPago> proveedoresCondicionPago, List<ProveedorDomicilio> proveedoresDomicilio, TcManticProveedoresPortalesDto portal, List<ProveedorBanca> proveedoresServicio, List<ProveedorBanca> proveedoresTransferencia) {
		this.idProveedor                = idProveedor;
		this.proveedor                  = proveedor;
		this.proveedorDomicilioSeleccion= proveedorDomicilioSeleccion;
		this.proveedoresTipoContacto    = proveedoresTipoContacto;
		this.deleteList                 = new ArrayList<>();
		this.contadores                 = new ContadoresListas(); 
		this.countIndice                = 0L;
		this.domicilio                  = domicilio;
		this.domicilioPivote            = domicilioPivote;
		this.personasTiposContacto      = personasTiposContacto;
		this.personaTipoContactoPivote  = personaTipoContactoPivote;
		this.personaTipoContacto        = personaTipoContacto;
		this.proveedoresCondicionPago   = proveedoresCondicionPago;
		this.proveedoresDomicilio       = proveedoresDomicilio;
		this.portal                     = portal;
		this.proveedoresServicio        = proveedoresServicio;
		this.proveedoresTransferencia   = proveedoresTransferencia;
	} // RegistroProveedor	

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public TcManticProveedoresDto getProveedor() {
		return proveedor;
	}

	public void setProveedor(TcManticProveedoresDto proveedor) {
		this.proveedor = proveedor;
	}

	public List<ProveedorDomicilio> getProveedoresDomicilio() {
		return proveedoresDomicilio;
	}

	public void setProveedoresDomicilio(List<ProveedorDomicilio> proveedoresDomicilio) {
		this.proveedoresDomicilio = proveedoresDomicilio;
	}

	public ProveedorDomicilio getProveedorDomicilioSeleccion() {
		return proveedorDomicilioSeleccion;
	}

	public void setProveedorDomicilioSeleccion(ProveedorDomicilio proveedorDomicilioSeleccion) {
		this.proveedorDomicilioSeleccion = proveedorDomicilioSeleccion;
	}

	public List<ProveedorTipoContacto> getProveedoresTipoContacto() {
		return proveedoresTipoContacto;
	}

	public void setProveedoresTipoContacto(List<ProveedorTipoContacto> proveedoresTipoContacto) {
		this.proveedoresTipoContacto = proveedoresTipoContacto;
	}

	public ProveedorTipoContacto getProveedorTipoContactoSeleccion() {
		return proveedorTipoContactoSeleccion;
	}

	public void setProveedorTipoContactoSeleccion(ProveedorTipoContacto proveedorTipoContactoSeleccion) {
		this.proveedorTipoContactoSeleccion = proveedorTipoContactoSeleccion;
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

	public List<ProveedorContactoAgente> getPersonasTiposContacto() {
		return personasTiposContacto;
	}

	public void setPersonasTiposContacto(List<ProveedorContactoAgente> personasTiposContacto) {
		this.personasTiposContacto = personasTiposContacto;
	}

	public PersonaTipoContacto getPersonaTipoContactoSeleccion() {
		return personaTipoContactoSeleccion;
	}

	public void setPersonaTipoContactoSeleccion(PersonaTipoContacto personaTipoContactoSeleccion) {
		this.personaTipoContactoSeleccion = personaTipoContactoSeleccion;
	}

	public ProveedorContactoAgente getPersonaTipoContactoPivote() {
		return personaTipoContactoPivote;
	}

	public void setPersonaTipoContactoPivote(ProveedorContactoAgente personaTipoContactoPivote) {
		this.personaTipoContactoPivote = personaTipoContactoPivote;
	}

	public ProveedorContactoAgente getPersonaTipoContacto() {
		return personaTipoContacto;
	}

	public void setPersonaTipoContacto(ProveedorContactoAgente personaTipoContacto) {
		this.personaTipoContacto = personaTipoContacto;
	}

	public List<ProveedorCondicionPago> getProveedoresCondicionPago() {
		return proveedoresCondicionPago;
	}

	public void setProveedoresCondicionPago(List<ProveedorCondicionPago> proveedoresCondicionPago) {
		this.proveedoresCondicionPago = proveedoresCondicionPago;
	}

	public ProveedorCondicionPago getProveedorCondicionPagoSeleccion() {
		return proveedorCondicionPagoSeleccion;
	}

	public void setProveedorCondicionPagoSeleccion(ProveedorCondicionPago proveedorCondicionPagoSeleccion) {
		this.proveedorCondicionPagoSeleccion = proveedorCondicionPagoSeleccion;
	}

	public TcManticProveedoresPortalesDto getPortal() {
		return portal;
	}

	public void setPortal(TcManticProveedoresPortalesDto portal) {
		this.portal = portal;
	}

	public List<ProveedorBanca> getProveedoresServicio() {
		return proveedoresServicio;
	}

	public void setProveedoresServicio(List<ProveedorBanca> proveedoresServicio) {
		this.proveedoresServicio = proveedoresServicio;
	}

	public ProveedorBanca getProveedorServicioSeleccion() {
		return proveedorServicioSeleccion;
	}

	public void setProveedorServicioSeleccion(ProveedorBanca proveedorServicioSeleccion) {
		this.proveedorServicioSeleccion = proveedorServicioSeleccion;
	}

	public List<ProveedorBanca> getProveedoresTransferencia() {
		return proveedoresTransferencia;
	}

	public void setProveedoresTransferencia(List<ProveedorBanca> proveedoresTransferencia) {
		this.proveedoresTransferencia = proveedoresTransferencia;
	}

	public ProveedorBanca getProveedorTransferenciaSeleccion() {
		return proveedorTransferenciaSeleccion;
	}

	public void setProveedorTransferenciaSeleccion(ProveedorBanca proveedorTransferenciaSeleccion) {
		this.proveedorTransferenciaSeleccion = proveedorTransferenciaSeleccion;
	}
	
	private void init(){
		MotorBusqueda motorBusqueda= null;
		try {
			motorBusqueda= new MotorBusqueda(this.idProveedor);
			this.proveedor= motorBusqueda.toProveedor();					
			this.portal= motorBusqueda.toPortal();
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
			this.proveedoresDomicilio= motor.toProveedoresDomicilio(true);
			for(ProveedorDomicilio proveedorDomicilio: this.proveedoresDomicilio){
				count++;
				proveedorDomicilio.setConsecutivo(Long.valueOf(count));
			} // for				
			this.proveedoresTipoContacto= motor.toProveedoresTipoContacto();			
			this.personasTiposContacto= motor.toAgentes();
			this.proveedoresCondicionPago= motor.toCondicionesPago();
			this.proveedoresServicio= motor.toServicios();
			this.proveedoresTransferencia= motor.toTransferencias();
		} // try
		catch (Exception e) {
			Error.mensaje(e);			
			throw e;
		} // catch		
	} // initCollections
	
	public void doAgregarProveedorDomicilio(){
		ProveedorDomicilio proveedorDomicilio= null;
		try {								
			proveedorDomicilio= new ProveedorDomicilio(this.contadores.getTotalProveedoresDomicilios() + this.countIndice, ESql.INSERT, true);	
			setValuesProveedorDomicilio(proveedorDomicilio, false);			
			this.proveedoresDomicilio.add(proveedorDomicilio);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarProveedorDomicilio
	
	public void doEliminarProveedorDomicilio(){
		try {			
			if(this.proveedoresDomicilio.remove(this.proveedorDomicilioSeleccion)){
				if(!this.proveedorDomicilioSeleccion.getNuevo())
					addDeleteList(this.proveedorDomicilioSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el domicilio", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el domicilio", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarProveedorDomicilio	
	
	public void doConsultarProveedorDomicilio(){
		ProveedorDomicilio pivote= null;
		try {			
			pivote= this.proveedoresDomicilio.get(this.proveedoresDomicilio.indexOf(this.proveedorDomicilioSeleccion));
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
	} // doConsultarProveedorDomicilio
	
	public void doActualizarProveedorDomicilio(){
		ProveedorDomicilio pivote= null;
		try {			
			pivote= this.proveedoresDomicilio.get(this.proveedoresDomicilio.indexOf(this.proveedorDomicilioSeleccion));			
			pivote.setModificar(false);
			setValuesProveedorDomicilio(pivote, true);						
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doActualizarProveedorDomicilio
	
	private void setValuesProveedorDomicilio(ProveedorDomicilio proveedorDomicilio, boolean actualizar) throws Exception{
		try {
			if(this.domicilio.getPrincipal()){
				for(ProveedorDomicilio record: this.proveedoresDomicilio)
					record.setIdPrincipal(0L);
			} // if
			proveedorDomicilio.setIdPrincipal(this.domicilio.getPrincipal() ? 1L : 2L);			
			proveedorDomicilio.setDomicilio(this.domicilio.getDomicilio());
			proveedorDomicilio.setIdDomicilio(this.domicilio.getDomicilio().getKey());
			proveedorDomicilio.setIdUsuario(JsfBase.getIdUsuario());
			proveedorDomicilio.setIdTipoDomicilio(this.domicilio.getIdTipoDomicilio());
			if(!actualizar)
				proveedorDomicilio.setConsecutivo(this.proveedoresDomicilio.size() + 1L);
			proveedorDomicilio.setIdEntidad(this.domicilio.getIdEntidad());
			proveedorDomicilio.setIdMunicipio(this.domicilio.getIdMunicipio());
			proveedorDomicilio.setIdLocalidad(this.domicilio.getLocalidad());
			proveedorDomicilio.setCodigoPostal(this.domicilio.getCodigoPostal());
			proveedorDomicilio.setCalle(this.domicilio.getCalle());
			proveedorDomicilio.setExterior(this.domicilio.getNumeroExterior());
			proveedorDomicilio.setInterior(this.domicilio.getNumeroInterior());
			proveedorDomicilio.setEntreCalle(this.domicilio.getEntreCalle());
			proveedorDomicilio.setyCalle(this.domicilio.getYcalle());
			proveedorDomicilio.setColonia(this.domicilio.getAsentamiento());
			proveedorDomicilio.setNuevoCp(this.domicilio.getCodigoPostal()!= null && !Cadena.isVacio(this.domicilio.getCodigoPostal()));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // setValuesProveedorDomicilio
	
	public void doAgregarProveedorCondicionPago(){
		ProveedorCondicionPago proveedorCondicionPago= null;
		try {					
			proveedorCondicionPago= new ProveedorCondicionPago(this.contadores.getTotalProveedoresPago() + this.countIndice, ESql.INSERT, true);				
			proveedorCondicionPago.setIdTipoPago(1L);
			this.proveedoresCondicionPago.add(proveedorCondicionPago);		
			// doValidaTipoPago(proveedorCondicionPago);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarProveedorTipoContacto
	
	public void doAgregarProveedorTipoContacto(){
		ProveedorTipoContacto proveedorTipoContacto= null;
		try {					
			proveedorTipoContacto= new ProveedorTipoContacto(this.contadores.getTotalProveedoresTipoContacto()+ this.countIndice, ESql.INSERT, true);				
			proveedorTipoContacto.setOrden(this.proveedoresTipoContacto.size() + 1L);
			this.proveedoresTipoContacto.add(proveedorTipoContacto);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarProveedorTipoContacto
	
	public void doAgregarProveedorServicio(){
		ProveedorBanca proveedorBanca= null;
		try {					
			proveedorBanca= new ProveedorBanca(this.contadores.getTotalProveedoresServicio()+ this.countIndice, ESql.INSERT, true);				
			proveedorBanca.setIdTipoCuenta(ETiposCuentas.SERVICIOS.getKey());
			this.proveedoresServicio.add(proveedorBanca);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarProveedorTipoContacto
	
	public void doAgregarProveedorTransferencia(){
		ProveedorBanca proveedorBanca= null;
		try {					
			proveedorBanca= new ProveedorBanca(this.contadores.getTotalProveedoresTransferencia()+ this.countIndice, ESql.INSERT, true);				
			proveedorBanca.setIdTipoCuenta(ETiposCuentas.TRANSFERENCIAS.getKey());
			this.proveedoresTransferencia.add(proveedorBanca);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarProveedorTipoContacto
	
	public void doAgregarAgenteContacto(){
		PersonaTipoContacto personaTipoContacto= null;
		try {
			personaTipoContacto= new PersonaTipoContacto(this.contadores.getTotalPersonasTipoContacto() + this.countIndice, ESql.INSERT, true, "");
			this.personaTipoContactoPivote.getContactos().add(personaTipoContacto);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			this.countIndice++;
		} // finally
	}
	
	public void doEliminarProveedorCondicionPago(){
		try {			
			if(this.proveedoresCondicionPago.remove(this.proveedorCondicionPagoSeleccion)){
				if(!this.proveedorCondicionPagoSeleccion.getNuevo())
					addDeleteList(this.proveedorCondicionPagoSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el tipo de pago", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el tipo de pago", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarProveedorCondicionPago
	
	public void doEliminarProveedorTipoContacto(){
		try {			
			if(this.proveedoresTipoContacto.remove(this.proveedorTipoContactoSeleccion)){
				if(!this.proveedorTipoContactoSeleccion.getNuevo())
					addDeleteList(this.proveedorTipoContactoSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el tipo de contacto", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el tipo de contacto", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarProveedorTipoContacto
	
	public void doEliminarProveedorServicio(){
		try {			
			if(this.proveedoresServicio.remove(this.proveedorServicioSeleccion)){
				if(!this.proveedorServicioSeleccion.getNuevo())
					addDeleteList(this.proveedorServicioSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el registro de servicio", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el registro de servicio", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarProveedorTipoContacto
	
	public void doEliminarProveedorTransferencia(){
		try {			
			if(this.proveedoresTransferencia.remove(this.proveedorTransferenciaSeleccion)){
				if(!this.proveedorTransferenciaSeleccion.getNuevo())
					addDeleteList(this.proveedorTransferenciaSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el registro de transferencia", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el registro de transferencia", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarProveedorTipoContacto
	
	public void doEliminarAgenteContacto(){
		try {			
			if(this.personaTipoContactoPivote.getContactos().remove(this.personaTipoContactoSeleccion)){
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
	} // doEliminarProveedorTipoContacto
	
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
	
	public void doAgregarAgente(){
		ProveedorContactoAgente proveedorContactoAgente= null;
		try {								
			proveedorContactoAgente= new ProveedorContactoAgente(this.contadores.getTotalProveedoresAgentes() + this.countIndice, ESql.INSERT, true);				
			proveedorContactoAgente.setConsecutivo(this.personasTiposContacto.size()+1L);
			proveedorContactoAgente.setNombres(this.personaTipoContactoPivote.getNombres());
			proveedorContactoAgente.setPaterno(this.personaTipoContactoPivote.getPaterno());
			proveedorContactoAgente.setMaterno(this.personaTipoContactoPivote.getMaterno());
			proveedorContactoAgente.setContactos(this.personaTipoContactoPivote.getContactos());
			this.personasTiposContacto.add(proveedorContactoAgente);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarClienteDomicilio
	
	public void doConsultarAgente() {
		ProveedorContactoAgente pivote= null;
		try {			
			pivote= this.personasTiposContacto.get(this.personasTiposContacto.indexOf(this.personaTipoContacto));
			pivote.setModificar(true);
			this.personaTipoContactoPivote= new ProveedorContactoAgente();
			this.personaTipoContactoPivote.setNombres(pivote.getNombres());
			this.personaTipoContactoPivote.setPaterno(pivote.getPaterno());
			this.personaTipoContactoPivote.setMaterno(pivote.getMaterno());
			this.personaTipoContactoPivote.setContactos(pivote.getContactos());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doConsultarAgente
	
	public void doEliminarAgente() {
		try {
			if(this.personasTiposContacto.remove(this.personaTipoContacto)) {
				if(!this.personaTipoContacto.getNuevo())
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
	} // doEliminarAgente
	
	public void doActualizaAgente() {
		ProveedorContactoAgente pivote= null;
		try {			
			pivote= this.personasTiposContacto.get(this.personasTiposContacto.indexOf(this.personaTipoContacto));
			pivote.setModificar(false);
			pivote.setNombres(this.personaTipoContactoPivote.getNombres());
			pivote.setPaterno(this.personaTipoContactoPivote.getPaterno());
			pivote.setMaterno(this.personaTipoContactoPivote.getMaterno());
			pivote.setContactos(this.personaTipoContactoPivote.getContactos());			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch	
	} // doActualizaAgente
	
	public void doValidaTipoPago(ProveedorCondicionPago condicionPago) {
		int countEfectivo=0;
		try {
			if(this.proveedoresCondicionPago.size()> 1) {
				for(ProveedorCondicionPago record: this.proveedoresCondicionPago) {
					if(record.getIdTipoPago().equals(1L) || record.getIdTipoPago().equals(3L))
						countEfectivo++;
				} // 
				if(countEfectivo> 2 && (condicionPago.getIdTipoPago().equals(1L) || condicionPago.getIdTipoPago().equals(3L))) {
					this.proveedoresCondicionPago.get(this.proveedoresCondicionPago.indexOf(condicionPago)).setIdTipoPago(2L);
				} // if
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doValidaTipoPago
}

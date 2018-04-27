package mx.org.kaana.mantic.catalogos.clientes.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;

public class RegistroCliente implements Serializable{
	
	private static final long serialVersionUID = 699751401273986887L;
	private Long idCliente;
	private TcManticClientesDto cliente;
	private List<ClienteDomicilio> clientesDomicilio;
	private ClienteDomicilio clienteDomicilioSelecion;
	private List<ClienteTipoContacto> clientesTiposContacto;
	private ClienteTipoContacto clienteTipoContactoSeleccion;
	private List<ClienteRepresentante> clientesRepresentantes;
	private ClienteRepresentante clienteRepresentanteSeleccion;
	private List<IBaseDto> deleteList;
	private ContadoresListas contadores;
	private Long countIndice;
	private Domicilio domicilio;
	private Domicilio domicilioPivote;

	public RegistroCliente() {
		this(-1L, new TcManticClientesDto(), new ArrayList<ClienteDomicilio>(), new ArrayList<ClienteTipoContacto>(), new ArrayList<ClienteRepresentante>(), new Domicilio());
	}
	
	public RegistroCliente(Long idCliente) {
		this.idCliente  = idCliente;
		this.contadores = new ContadoresListas();
		this.countIndice= 0L;
		this.deleteList = new ArrayList<>();
		this.domicilio  = new Domicilio();
		this.domicilioPivote= new Domicilio();
		init();		
	}
	
	public RegistroCliente(Long idCliente, TcManticClientesDto cliente, List<ClienteDomicilio> clientesDomicilio, List<ClienteTipoContacto> clientesTiposContacto, List<ClienteRepresentante> clientesRepresentantes, Domicilio domicilio) {
		this.idCliente             = idCliente;
		this.cliente               = cliente;
		this.clientesDomicilio     = clientesDomicilio;
		this.clientesTiposContacto = clientesTiposContacto;
		this.clientesRepresentantes= clientesRepresentantes;
		this.deleteList            = new ArrayList<>();
		this.contadores            = new ContadoresListas();
		this.countIndice           = 0L;
		this.domicilio             = domicilio;
		this.domicilioPivote       = domicilio;
	}
	
	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public TcManticClientesDto getCliente() {
		return cliente;
	}

	public void setCliente(TcManticClientesDto cliente) {
		this.cliente = cliente;
	}

	public List<ClienteDomicilio> getClientesDomicilio() {
		return clientesDomicilio;
	}

	public void setClientesDomicilio(List<ClienteDomicilio> clientesDomicilio) {
		this.clientesDomicilio = clientesDomicilio;
	}

	public ClienteDomicilio getClienteDomicilioSelecion() {
		return clienteDomicilioSelecion;
	}

	public void setClienteDomicilioSelecion(ClienteDomicilio clienteDomicilioSelecion) {
		this.clienteDomicilioSelecion = clienteDomicilioSelecion;
	}

	public List<ClienteTipoContacto> getClientesTiposContacto() {
		return clientesTiposContacto;
	}

	public void setClientesTiposContacto(List<ClienteTipoContacto> clientesTiposContacto) {
		this.clientesTiposContacto = clientesTiposContacto;
	}

	public ClienteTipoContacto getClienteTipoContactoSeleccion() {
		return clienteTipoContactoSeleccion;
	}

	public void setClienteTipoContactoSeleccion(ClienteTipoContacto clienteTipoContactoSeleccion) {
		this.clienteTipoContactoSeleccion = clienteTipoContactoSeleccion;
	}

	public List<ClienteRepresentante> getClientesRepresentantes() {
		return clientesRepresentantes;
	}

	public void setClientesRepresentantes(List<ClienteRepresentante> clientesRepresentantes) {
		this.clientesRepresentantes = clientesRepresentantes;
	}

	public ClienteRepresentante getClienteRepresentanteSeleccion() {
		return clienteRepresentanteSeleccion;
	}

	public void setClienteRepresentanteSeleccion(ClienteRepresentante clienteRepresentanteSeleccion) {
		this.clienteRepresentanteSeleccion = clienteRepresentanteSeleccion;
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
			motorBusqueda= new MotorBusqueda(this.idCliente);
			this.cliente= motorBusqueda.toCliente();									
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
			this.clientesDomicilio= motor.toClientesDomicilio();
			for(ClienteDomicilio clienteDomicilio: this.clientesDomicilio){
				count++;
				clienteDomicilio.setConsecutivo(Long.valueOf(count));
			} // for				
			this.clientesRepresentantes= motor.toClientesRepresentantes();
			this.clientesTiposContacto= motor.toClientesTipoContacto();			
		} // try
		catch (Exception e) {
			Error.mensaje(e);			
			throw e;
		} // catch		
	} // initCollections
	
	public void doAgregarClienteDomicilio(){
		ClienteDomicilio clienteDomicilio= null;
		try {								
			clienteDomicilio= new ClienteDomicilio(this.contadores.getTotalClientesDomicilios()+ this.countIndice, ESql.INSERT, true);	
			setValuesClienteDomicilio(clienteDomicilio);			
			this.clientesDomicilio.add(clienteDomicilio);			
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
			if(this.clientesDomicilio.remove(this.clienteDomicilioSelecion)){
				if(!this.clienteDomicilioSelecion.getNuevo())
					addDeleteList(this.clienteDomicilioSelecion);
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
		ClienteDomicilio pivote= null;
		try {			
			pivote= this.clientesDomicilio.get(this.clientesDomicilio.indexOf(this.clienteDomicilioSelecion));
			pivote.setModificar(true);
			this.domicilioPivote= new Domicilio();
			this.domicilioPivote.setIdTipoDomicilio(pivote.getIdTipoDomicilio());
			this.domicilioPivote.setPrincipal(pivote.getIdPrincipal().equals(1L));	
			this.domicilioPivote.setIdDomicilio(pivote.getIdDomicilio());
			this.domicilioPivote.setIdEntidad(pivote.getIdEntidad());
			this.domicilioPivote.setIdMunicipio(pivote.getIdMunicipio());
			this.domicilioPivote.setIdLocalidad(pivote.getIdLocalidad());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doConsultarClienteDomicilio
	
	public void doActualizarClienteDomicilio(){
		ClienteDomicilio pivote= null;
		try {			
			pivote= this.clientesDomicilio.get(this.clientesDomicilio.indexOf(this.clienteDomicilioSelecion));			
			pivote.setModificar(false);
			setValuesClienteDomicilio(pivote);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doActualizarClienteDomicilio
	
	private void setValuesClienteDomicilio(ClienteDomicilio clienteDomicilio) throws Exception{
		try {
			if(this.domicilio.getPrincipal()){
				for(ClienteDomicilio record: this.clientesDomicilio)
					record.setIdPrincipal(0L);
			} // if
			clienteDomicilio.setIdPrincipal(this.domicilio.getPrincipal() ? 1L : 2L);
			if(this.domicilio.getIdDomicilio().equals(-1L))
				clienteDomicilio.setIdDomicilio(registrarDomicilio());
			else
				clienteDomicilio.setIdDomicilio(this.domicilio.getIdDomicilio());
			clienteDomicilio.setIdUsuario(JsfBase.getIdUsuario());
			clienteDomicilio.setIdTipoDomicilio(this.domicilio.getIdTipoDomicilio());
			clienteDomicilio.setConsecutivo(this.clientesDomicilio.size() + 1L);
			clienteDomicilio.setIdEntidad(this.domicilio.getIdEntidad());
			clienteDomicilio.setIdMunicipio(this.domicilio.getIdMunicipio());
			clienteDomicilio.setIdLocalidad(this.domicilio.getIdLocalidad());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // setValuesClienteDomicilio
	
	public void doAgregarClienteRepresentante(){
		ClienteRepresentante clienteRepresentante= null;
		try {					
			clienteRepresentante= new ClienteRepresentante(this.contadores.getTotalClientesRepresentantes()+ this.countIndice, ESql.INSERT, true);				
			this.clientesRepresentantes.add(clienteRepresentante);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarClienteRepresentante
	
	public void doEliminarClienteRepresentante(){
		try {			
			if(this.clientesRepresentantes.remove(this.clienteRepresentanteSeleccion)){
				if(!this.clienteRepresentanteSeleccion.getNuevo())
					addDeleteList(this.clienteRepresentanteSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el representante", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el representante", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarClienteRepresentante
	
	public void doAgregarClienteTipoContacto(){
		ClienteTipoContacto clienteTipoContacto= null;
		try {					
			clienteTipoContacto= new ClienteTipoContacto(this.contadores.getTotalClientesTipoContacto()+ this.countIndice, ESql.INSERT, true);				
			clienteTipoContacto.setOrden(this.clientesTiposContacto.size() + 1L);
			this.clientesTiposContacto.add(clienteTipoContacto);			
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
			if(this.clientesTiposContacto.remove(this.clienteTipoContactoSeleccion)){
				if(!this.clienteTipoContactoSeleccion.getNuevo())
					addDeleteList(this.clienteTipoContactoSeleccion);
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
	
	private void addDeleteList(IBaseDto dto){
		try {
			this.deleteList.add(dto);
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

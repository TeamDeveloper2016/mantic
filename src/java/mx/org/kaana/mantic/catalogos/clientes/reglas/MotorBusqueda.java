package mx.org.kaana.mantic.catalogos.clientes.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteContactoRepresentante;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteDomicilio;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteRepresentante;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

public class MotorBusqueda extends MotorBusquedaCatalogos implements Serializable{
	
	private static final long serialVersionUID = -6959798935949814917L;
	
	public MotorBusqueda(Long idCliente) {
		super(idCliente);
	}
	
	public TcManticClientesDto toCliente() throws Exception {
		TcManticClientesDto regresar= null;
		try {
			regresar= (TcManticClientesDto) DaoFactory.getInstance().findById(TcManticClientesDto.class, this.idCliente);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCliente
	
	public List<ClienteDomicilio> toClientesDomicilio() throws Exception {
		return toClientesDomicilio(false);
	} // toClientesDomicilio
	
	public List<ClienteDomicilio> toClientesDomicilio(boolean update) throws Exception {
		List<ClienteDomicilio> regresar= null;
		TcManticDomiciliosDto domicilio= null;
		Map<String, Object>params      = null;
		Entity entityDomicilio         = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(ClienteDomicilio.class, "TrManticClienteDomicilioDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			for(ClienteDomicilio clienteDomicilio: regresar){
				clienteDomicilio.setIdLocalidad(toLocalidad(clienteDomicilio.getIdDomicilio()));
				clienteDomicilio.setIdMunicipio(toMunicipio(clienteDomicilio.getIdLocalidad().getKey()));
				clienteDomicilio.setIdEntidad(toEntidad(clienteDomicilio.getIdMunicipio().getKey()));
				if(update){
					domicilio= toDomicilio(clienteDomicilio.getIdDomicilio());
					clienteDomicilio.setCalle(domicilio.getCalle());
					clienteDomicilio.setCodigoPostal(domicilio.getCodigoPostal());
					clienteDomicilio.setColonia(domicilio.getAsentamiento());
					clienteDomicilio.setEntreCalle(domicilio.getEntreCalle());
					clienteDomicilio.setyCalle(domicilio.getYcalle());
					clienteDomicilio.setExterior(domicilio.getNumeroExterior());
					clienteDomicilio.setInterior(domicilio.getNumeroInterior());
					entityDomicilio= new Entity(clienteDomicilio.getIdDomicilio());
					entityDomicilio.put("idEntidad", new Value("idEntidad", clienteDomicilio.getIdEntidad().getKey()));
					entityDomicilio.put("idMunicipio", new Value("idMunicipio", clienteDomicilio.getIdMunicipio().getKey()));
					entityDomicilio.put("idLocalidad", new Value("idLocalidad", clienteDomicilio.getIdLocalidad().getKey()));
					clienteDomicilio.setDomicilio(entityDomicilio);
				} // if				
			} // for
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesDomicilio
	
	public Domicilio toClienteDomicilioPrinicipal(boolean update) throws Exception {
		Domicilio regresar               = null;
		TcManticDomiciliosDto domicilio  = null;
		List<ClienteDomicilio> domicilios= null;
		Map<String, Object>params        = null;
		Entity entityDomicilio           = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente);
			domicilios= DaoFactory.getInstance().toEntitySet(ClienteDomicilio.class, "TrManticClienteDomicilioDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			int count= 1;
			for(ClienteDomicilio clienteDomicilio: domicilios) {
				if(clienteDomicilio.getIdPrincipal().equals(1L) || domicilios.size()== 1 || (regresar== null && domicilios.size()== count)) {
					clienteDomicilio.setIdLocalidad(toLocalidad(clienteDomicilio.getIdDomicilio()));
					clienteDomicilio.setIdMunicipio(toMunicipio(clienteDomicilio.getIdLocalidad().getKey()));
					clienteDomicilio.setIdEntidad(toEntidad(clienteDomicilio.getIdMunicipio().getKey()));
					domicilio= toDomicilio(clienteDomicilio.getIdDomicilio());
					regresar= loadDomicilio(domicilio);
					regresar.setIdTipoDomicilio(clienteDomicilio.getIdTipoDomicilio());
					regresar.setIdClienteDomicilio(clienteDomicilio.getIdClienteDomicilio());
					regresar.setIdEntidad(clienteDomicilio.getIdEntidad());
					regresar.setIdMunicipio(clienteDomicilio.getIdMunicipio());
					regresar.setLocalidad(clienteDomicilio.getIdLocalidad());
					entityDomicilio= new Entity(clienteDomicilio.getIdDomicilio());
					entityDomicilio.put("idEntidad", new Value("idEntidad", clienteDomicilio.getIdEntidad().getKey()));
					entityDomicilio.put("idMunicipio", new Value("idMunicipio", clienteDomicilio.getIdMunicipio().getKey()));
					entityDomicilio.put("idLocalidad", new Value("idLocalidad", clienteDomicilio.getIdLocalidad().getKey()));
					entityDomicilio.put("codigoPostal", new Value("codigoPostal", clienteDomicilio.getCodigoPostal()));
					regresar.setDomicilio(entityDomicilio);
				} // if
				count++;
			} // for
			if(regresar== null)
				regresar= new Domicilio();								
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesDomicilio
	
	private Domicilio loadDomicilio(TcManticDomiciliosDto domicilio){
		Domicilio regresar= null;
		try {
			regresar= new Domicilio();
			regresar.setAsentamiento(domicilio.getAsentamiento());
			regresar.setCalle(domicilio.getCalle());
			regresar.setCodigoPostal(domicilio.getCodigoPostal());
			regresar.setEntreCalle(domicilio.getEntreCalle());
			regresar.setLatitud(domicilio.getLatitud());
			regresar.setLongitud(domicilio.getLongitud());
			regresar.setNumeroExterior(domicilio.getNumeroExterior());
			regresar.setNumeroInterior(domicilio.getNumeroInterior());
			regresar.setObservaciones(domicilio.getObservaciones());
			regresar.setYcalle(domicilio.getYcalle());
			regresar.setIdDomicilio(domicilio.getIdDomicilio());
			regresar.setIdLocalidad(domicilio.getIdLocalidad());
			regresar.setIdUsuario(domicilio.getIdUsuario());
			regresar.setRegistro(domicilio.getRegistro());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadDomicilio
	
	public List<ClienteRepresentante> toClientesRepresentantes() throws Exception {
		List<ClienteRepresentante> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(ClienteRepresentante.class, "TrManticClienteRepresentanteDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesRepresentantes	
	
	public List<ClienteContactoRepresentante> toPersonasTipoContacto() throws Exception {
		List<ClienteContactoRepresentante> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_persona=" + this.idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(ClienteContactoRepresentante.class, "TrManticPersonaTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto		
	
	public List<ClienteContactoRepresentante> toRepresentantes() throws Exception{
		List<ClienteContactoRepresentante> regresar= null;
		Map<String, Object>params                  = null;
		TcManticPersonasDto persona                = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(ClienteContactoRepresentante.class, "TrManticClienteRepresentanteDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			if(!regresar.isEmpty()){
				for(ClienteContactoRepresentante contacto: regresar){
					contacto.setContactos(toPersonaContacto(contacto.getIdRepresentante()));
					persona= toPersona(contacto.getIdRepresentante());
					contacto.setNombres(persona.getNombres());
					contacto.setPaterno(persona.getPaterno());
					contacto.setMaterno(persona.getMaterno());
				} // for
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally		
		return regresar;
	} // toRepresentantes		
	
	public Entity toDomicilioCliente() throws Exception{
		Entity regresar          = null;
		Entity domicilio         = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente + " and id_principal=1");
			domicilio= (Entity) DaoFactory.getInstance().toEntity("TrManticClienteDomicilioDto", "row", params);
			if(domicilio!= null){
				params.clear();
				params.put(Constantes.SQL_CONDICION, "tc_mantic_domicilios.id_domicilio=" + domicilio.toString("idDomicilio"));
				regresar= (Entity) DaoFactory.getInstance().toEntity("VistaDomiciliosCatalogosDto", "domicilios", params);
			} // if
			else{
				params.clear();
				params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente);
				domicilio= (Entity) DaoFactory.getInstance().toEntity("TrManticClienteDomicilioDto", "row", params);
				if(domicilio!= null){
					params.clear();
					params.put(Constantes.SQL_CONDICION, "tc_mantic_domicilios.id_domicilio=" + domicilio.toString("idDomicilio"));
					regresar= (Entity) DaoFactory.getInstance().toEntity("VistaDomiciliosCatalogosDto", "domicilios", params);
				} // if
			} // else			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDomicilioCLiente
}

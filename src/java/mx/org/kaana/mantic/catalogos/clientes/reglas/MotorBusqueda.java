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
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteContactoRepresentante;
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteDomicilio;
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteRepresentante;
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

public class MotorBusqueda extends MotorBusquedaCatalogos implements Serializable{
	
	private static final long serialVersionUID = -6959798935949814917L;
	private Long idCliente;
	
	public MotorBusqueda(Long idCliente) {
		this.idCliente = idCliente;
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
	
	public List<ClienteRepresentante> toClientesRepresentantes() throws Exception {
		List<ClienteRepresentante> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(ClienteRepresentante.class, "TrManticClientesRepresentantesDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesRepresentantes
	
	public List<ClienteTipoContacto> toClientesTipoContacto() throws Exception {
		List<ClienteTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(ClienteTipoContacto.class, "TrManticClienteTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
	
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
			regresar= DaoFactory.getInstance().toEntitySet(ClienteContactoRepresentante.class, "TrManticClientesRepresentantesDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
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
	
	private List<PersonaTipoContacto> toPersonaContacto(Long idPersona) throws Exception{
		List<PersonaTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_persona=" + idPersona);
			regresar= DaoFactory.getInstance().toEntitySet(PersonaTipoContacto.class, "TrManticPersonaTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toPersonaContacto
	
	private TcManticPersonasDto toPersona(Long idPersona) throws Exception{
		TcManticPersonasDto regresar= null;
		try {
			regresar= (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, idPersona);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch				
		return regresar;
	} // toPersona
}

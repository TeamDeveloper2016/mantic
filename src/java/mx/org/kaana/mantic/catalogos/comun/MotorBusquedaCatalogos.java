package mx.org.kaana.mantic.catalogos.comun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import org.hibernate.Session;

public abstract class MotorBusquedaCatalogos {

	private static final String VENTA= "VENTA";
	protected Long idCliente;
	
	public MotorBusquedaCatalogos() {
		this(-1L);
	}
	
	public MotorBusquedaCatalogos(Long idCliente) {
		this.idCliente = idCliente;
	}
	
	public TcManticDomiciliosDto toDomicilio(Long idDomicilio) throws Exception {
		TcManticDomiciliosDto regresar= null;
		try {
			regresar= (TcManticDomiciliosDto) DaoFactory.getInstance().findById(TcManticDomiciliosDto.class, idDomicilio);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDomicilio
	
	protected Long toIdLocalidad(Long idDomicilio) throws Exception{
		Long regresar                  = -1L;
		TcManticDomiciliosDto domicilio= null;
		try {
			domicilio= (TcManticDomiciliosDto) DaoFactory.getInstance().findById(TcManticDomiciliosDto.class, idDomicilio);
			if(domicilio!= null)
				regresar= domicilio.getIdLocalidad();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toIdLocalidad
	
	protected Long toIdMunicipio(Long idLocalidad) throws Exception{
		Long regresar            = -1L;
		Entity localidad         = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_localidad=" + idLocalidad);
			localidad=  (Entity) DaoFactory.getInstance().toEntity("TcJanalLocalidadesDto", "row", params);
			if(localidad!= null)
				regresar= localidad.toLong("idMunicipio");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toIdMunicipio
	
	protected Long toIdEntidad(Long idMunicipio) throws Exception{
		Long regresar            = -1L;
		Entity municipio         = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_municipio=" + idMunicipio);
			municipio=  (Entity) DaoFactory.getInstance().toEntity("TcJanalMunicipiosDto", "row", params);
			if(municipio!= null)
				regresar= municipio.toLong("idEntidad");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toIdMunicipio	
	
	protected Entity toEntidad(Long idMunicipio) throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idPais", 1);
      params.put(Constantes.SQL_CONDICION, "id_entidad=" + toIdEntidad(idMunicipio));
			regresar= (Entity) DaoFactory.getInstance().toEntity("TcJanalEntidadesDto", "comboEntidades", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toEntidad
	
	protected Entity toMunicipio(Long idLocalidad) throws Exception{
		Entity regresar           = null;
    Map<String, Object> params= null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_municipio=" + toIdMunicipio(idLocalidad));
      regresar= (Entity) DaoFactory.getInstance().toEntity("TcJanalMunicipiosDto", "row", params);							
		} // try
		catch (Exception e) {
			throw e;			
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toMunicipio
	
	protected Entity toLocalidad(Long idDomicilio) throws Exception{
		Entity regresar           = null;
    Map<String, Object> params= null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_localidad=" + toIdLocalidad(idDomicilio));
      regresar= (Entity) DaoFactory.getInstance().toEntity("TcJanalLocalidadesDto", "row", params);							
		} // try
		catch (Exception e) {
			throw e;			
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toLocalidad
	
	protected List<PersonaTipoContacto> toPersonaContacto(Long idPersona) throws Exception{
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
	
	protected TcManticPersonasDto toPersona(Long idPersona) throws Exception{
		TcManticPersonasDto regresar= null;
		try {
			regresar= (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, idPersona);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch				
		return regresar;
	} // toPersona
	
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
	
	public List<ClienteTipoContacto> toClientesTipoContacto(Session sesion) throws Exception {
		List<ClienteTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(sesion, ClienteTipoContacto.class, "TrManticClienteTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
	
	public Entity toClienteDefault() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("clave", VENTA);
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaClientesDto", "clienteDefault", params);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toClienteDefault	
}

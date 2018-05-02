
package mx.org.kaana.mantic.catalogos.personas.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaDomicilio;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

public class MotorBusqueda implements Serializable{

	private static final long serialVersionUID= 5366287658013154045L;	
	private Long idPersona;

	public MotorBusqueda(Long idPersona) {
		this.idPersona = idPersona;
	}

	public TcManticPersonasDto toPersona() throws Exception {
		TcManticPersonasDto regresar= null;
		try {
			regresar= (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, this.idPersona);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPersona	
	
	public List<PersonaDomicilio> toPersonasDomicilio() throws Exception {
		List<PersonaDomicilio> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_persona=" + this.idPersona);
			regresar= DaoFactory.getInstance().toEntitySet(PersonaDomicilio.class, "TrManticPersonaDomicilioDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			for(PersonaDomicilio personaDomicilio: regresar){
				personaDomicilio.setIdLocalidad(toIdLocalidad(personaDomicilio.getIdDomicilio()));
				personaDomicilio.setIdMunicipio(toIdMunicipio(personaDomicilio.getIdLocalidad()));
				personaDomicilio.setIdEntidad(toIdEntidad(personaDomicilio.getIdMunicipio()));
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
	
	public List<PersonaTipoContacto> toPersonasTipoContacto() throws Exception {
		List<PersonaTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_persona=" + this.idPersona);
			regresar= DaoFactory.getInstance().toEntitySet(PersonaTipoContacto.class, "TrManticPersonaTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
	
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
	
	private Long toIdLocalidad(Long idDomicilio) throws Exception{
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
	
	private Long toIdMunicipio(Long idLocalidad) throws Exception{
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
	
	private Long toIdEntidad(Long idMunicipio) throws Exception{
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
}

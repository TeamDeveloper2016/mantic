package mx.org.kaana.mantic.catalogos.comun;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;

public abstract class MotorBusquedaCatalogos {

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
      regresar= (Entity) DaoFactory.getInstance().toEntity("TcJanalMunicipiosDto", "row", params);							
		} // try
		catch (Exception e) {
			throw e;			
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toLocalidad
}

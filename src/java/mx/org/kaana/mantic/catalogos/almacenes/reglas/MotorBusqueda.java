package mx.org.kaana.mantic.catalogos.almacenes.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.beans.AlmacenArticulo;
import mx.org.kaana.mantic.catalogos.almacenes.beans.AlmacenDomicilio;
import mx.org.kaana.mantic.catalogos.almacenes.beans.AlmacenTipoContacto;
import mx.org.kaana.mantic.catalogos.almacenes.beans.AlmacenUbicacion;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;

public class MotorBusqueda extends MotorBusquedaCatalogos implements Serializable {
	
	private static final long serialVersionUID = -1960934950293962600L;
	private Long idAlmacen;
	
	public MotorBusqueda(Long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}
	
	public TcManticAlmacenesDto toAlmacen() throws Exception {
		TcManticAlmacenesDto regresar= null;
		try {
			regresar= (TcManticAlmacenesDto) DaoFactory.getInstance().findById(TcManticAlmacenesDto.class, this.idAlmacen);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toAlmacen
	
	public List<AlmacenDomicilio> toAlmacenesDomicilio() throws Exception {
		return toAlmacenesDomicilio(false);
	} // toAlmacenesDomicilio
	
	public List<AlmacenDomicilio> toAlmacenesDomicilio(boolean update) throws Exception {
		List<AlmacenDomicilio> regresar= null;
		Map<String, Object>params      = null;
		TcManticDomiciliosDto domicilio= null;
		Entity entityDomicilio         = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_almacen=" + this.idAlmacen);
			regresar= DaoFactory.getInstance().toEntitySet(AlmacenDomicilio.class, "TrManticAlmacenDomicilioDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			for(AlmacenDomicilio almacenDomicilio: regresar){
				almacenDomicilio.setIdLocalidad(toLocalidad(almacenDomicilio.getIdDomicilio()));
				almacenDomicilio.setIdMunicipio(toMunicipio(almacenDomicilio.getIdLocalidad().getKey()));
				almacenDomicilio.setIdEntidad(toEntidad(almacenDomicilio.getIdMunicipio().getKey()));
				if(update){
					domicilio= toDomicilio(almacenDomicilio.getIdDomicilio());
					almacenDomicilio.setCalle(domicilio.getCalle());
					almacenDomicilio.setCodigoPostal(domicilio.getCodigoPostal());
					almacenDomicilio.setColonia(domicilio.getAsentamiento());
					almacenDomicilio.setEntreCalle(domicilio.getEntreCalle());
					almacenDomicilio.setyCalle(domicilio.getYcalle());
					almacenDomicilio.setExterior(domicilio.getNumeroExterior());
					almacenDomicilio.setInterior(domicilio.getNumeroInterior());
					entityDomicilio= new Entity(almacenDomicilio.getIdDomicilio());
					entityDomicilio.put("idEntidad", new Value("idEntidad", almacenDomicilio.getIdEntidad().getKey()));
					entityDomicilio.put("idMunicipio", new Value("idMunicipio", almacenDomicilio.getIdMunicipio().getKey()));
					entityDomicilio.put("idLocalidad", new Value("idLocalidad", almacenDomicilio.getIdLocalidad().getKey()));
					almacenDomicilio.setDomicilio(entityDomicilio);
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
	} // toAlmacenesDomicilio		
	
	public List<AlmacenTipoContacto> toAlmacenesTipoContacto() throws Exception {
		List<AlmacenTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_almacen=" + this.idAlmacen);
			regresar= DaoFactory.getInstance().toEntitySet(AlmacenTipoContacto.class, "TrManticAlmacenTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toAlmacenesTipoContacto
	
	public List<AlmacenUbicacion> toAlmacenUbicacion() throws Exception {
		List<AlmacenUbicacion> regresar= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_almacen=" + this.idAlmacen);
			regresar= DaoFactory.getInstance().toEntitySet(AlmacenUbicacion.class, "TcManticAlmacenesUbicacionesDto", "row", params);			
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toAlmacenUbicacion
	
	public List<AlmacenArticulo> toAlmacenArticulos() throws Exception{
		List<AlmacenArticulo> regresar= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_almacen=" + this.idAlmacen);
			regresar= DaoFactory.getInstance().toEntitySet(AlmacenArticulo.class, "TcManticAlmacenesArticulosDto", "row", params);			
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toAlmacenArticulos
	
	public Entity toArticulo() throws Exception{
		Entity regresar= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idArticulo", this.idAlmacen);
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaArticulosAlmacenDto", "findIdArticulo", params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toArticulo
}

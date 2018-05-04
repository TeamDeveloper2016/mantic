package mx.org.kaana.mantic.catalogos.almacenes.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.bean.AlmacenDomicilio;
import mx.org.kaana.mantic.catalogos.almacenes.bean.AlmacenTipoContacto;
import mx.org.kaana.mantic.catalogos.almacenes.bean.AlmacenUbicacion;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesDto;

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
		List<AlmacenDomicilio> regresar= null;
		Map<String, Object>params      = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_almacen=" + this.idAlmacen);
			regresar= DaoFactory.getInstance().toEntitySet(AlmacenDomicilio.class, "TrManticAlmacenDomicilioDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			for(AlmacenDomicilio almacenDomicilio: regresar){
				almacenDomicilio.setIdLocalidad(toIdLocalidad(almacenDomicilio.getIdDomicilio()));
				almacenDomicilio.setIdMunicipio(toIdMunicipio(almacenDomicilio.getIdLocalidad()));
				almacenDomicilio.setIdEntidad(toIdEntidad(almacenDomicilio.getIdMunicipio()));
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
	
	public AlmacenUbicacion toAlmacenUbicacion() throws Exception {
		AlmacenUbicacion regresar= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_almacen=" + this.idAlmacen);
			regresar= (AlmacenUbicacion) DaoFactory.getInstance().toEntity(AlmacenUbicacion.class, "TcManticAlmacenesUbicacionesDto", "row", params);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toAlmacenUbicacion
}

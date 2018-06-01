package mx.org.kaana.mantic.catalogos.empresas.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.catalogos.empresas.beans.EmpresaDomicilio;
import mx.org.kaana.mantic.catalogos.empresas.beans.EmpresaTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDto;

public class MotorBusqueda extends MotorBusquedaCatalogos implements Serializable{

	private static final long serialVersionUID = -5922545219839522227L;
	private Long idEmpresa;
	
	public MotorBusqueda(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	} // MotorBusqueda
	
	public TcManticEmpresasDto toEmpresa() throws Exception {
		TcManticEmpresasDto regresar= null;
		try {
			regresar= (TcManticEmpresasDto) DaoFactory.getInstance().findById(TcManticEmpresasDto.class, this.idEmpresa);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toEmpresa
	
	public List<EmpresaDomicilio> toEmpresasDomicilio() throws Exception{
		return toEmpresasDomicilio(false);
	}
	
	public List<EmpresaDomicilio> toEmpresasDomicilio(boolean update) throws Exception{
		List<EmpresaDomicilio> regresar= null;
		TcManticDomiciliosDto domicilio= null;
		Map<String, Object>params      = null;
		Entity entityDomicilio         = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_empresa=" + this.idEmpresa);
			regresar= DaoFactory.getInstance().toEntitySet(EmpresaDomicilio.class, "TrManticEmpresaDomicilioDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			for(EmpresaDomicilio clienteDomicilio: regresar){
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
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toEmpresasDomicilio
	
	public List<EmpresaTipoContacto> toEmpresasTipoContacto() throws Exception {
		List<EmpresaTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_empresa=" + this.idEmpresa);
			regresar= DaoFactory.getInstance().toEntitySet(EmpresaTipoContacto.class, "TrManticEmpresaTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
}
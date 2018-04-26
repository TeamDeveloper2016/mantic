package mx.org.kaana.mantic.catalogos.clientes.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteDomicilio;
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteRepresentante;
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;

public class MotorBusqueda implements Serializable{
	
	private static final long serialVersionUID = -6959798935949814917L;
	private Long idCliente;
	
	public MotorBusqueda(Long idCliente) {
		this.idCliente = idCliente;
	}
	
	public TcManticClientesDto toCliente() throws Exception {
		TcManticClientesDto regresar= null;
		try {
			regresar= (TcManticClientesDto) DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idCliente);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCliente
	
	public List<ClienteDomicilio> toClientesDomicilio() throws Exception {
		List<ClienteDomicilio> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(ClienteDomicilio.class, "TrManticClienteDomicilioDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
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
}

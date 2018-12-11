package mx.org.kaana.mantic.taller.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDto;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.taller.beans.ContactoCliente;

public class MotorBusqueda extends MotorBusquedaCatalogos implements Serializable{

	private static final long serialVersionUID = 7475485235141275332L;
	private Long idServicio;

	public MotorBusqueda(Long idServicio) {
		this.idServicio = idServicio;
	}		
	
	public TcManticServiciosDto toServicio() throws Exception{
		TcManticServiciosDto regresar= null;
		try {
			regresar= (TcManticServiciosDto) DaoFactory.getInstance().findById(TcManticServiciosDto.class, this.idServicio);
		} // try
		catch (Exception e) {
			throw e;
		} // catch	
		return regresar;
	} // toServicio
	
	public TcManticClientesDto toCliente(Long idCliente) throws Exception{
		TcManticClientesDto regresar=null;
		try {
			regresar= (TcManticClientesDto) DaoFactory.getInstance().findById(TcManticClientesDto.class, idCliente);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // toCliente
	
	public ContactoCliente toContactoCliente(Long idCliente) throws Exception{
		ContactoCliente regresar           = null;
		List<ClienteTipoContacto> contactos= null;
		try {
			regresar= new ContactoCliente();
			contactos= toClientesTipoContacto(idCliente);
			if(!contactos.isEmpty()){
				for(ClienteTipoContacto tipoContacto: contactos){
					if(tipoContacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO.getKey()))
						regresar.setTelefono(tipoContacto.getValor());
					if(tipoContacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey()))
						regresar.setEmail(tipoContacto.getValor());
				} // if
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // toContactoCliente
	
	public List<ClienteTipoContacto> toClientesTipoContacto(Long idCliente) throws Exception {
		List<ClienteTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + idCliente);
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
}

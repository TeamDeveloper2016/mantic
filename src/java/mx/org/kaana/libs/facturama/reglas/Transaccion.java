package mx.org.kaana.libs.facturama.reglas;

import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturamaBitacoraDto;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private static final String REGISTRO_CLIENTE = "REGISTRO DE CLIENTE";
	private static final String REGISTRO_ARTICULO= "REGISTRO DE ARTICULO";
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		try {
			switch(accion){
				case PROCESAR:
					regresar= procesarClientes(sesion);
					break;		
				case REPROCESAR:
					regresar= procesarArticulos(sesion);
					break;				
			} // switch			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // ejecutar
	
	private boolean procesarClientes(Session sesion) throws Exception{
		boolean regresar              = true;
		CFDIGestor gestor             = null;
		List<ClienteFactura> clientes = null;
		List<Client> clientesFacturama= null;
		Client clientePivote          = null;		
		String id                     = null;
		try {
			gestor= new CFDIGestor();
			clientes= gestor.toAllClientesFactura(sesion);
			if(!clientes.isEmpty()){
				clientesFacturama= CFDIFactory.getInstance().getClients();
				if(!clientesFacturama.isEmpty()){
					for(ClienteFactura recordCliente: clientes){
						clientePivote= new Client(recordCliente.getRfc());
						if(clientesFacturama.indexOf(clientePivote)== -1){
							id= CFDIFactory.getInstance().createClientId(recordCliente);
							if(isCorrectId(id))
								actualizarCliente(sesion, recordCliente.getId(), id);
							else
								registrarBitacora(sesion, recordCliente.getId(), id);								
						} // if
					} // for
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarClientes	
	
	private boolean actualizarCliente(Session sesion, String id, String idFacturama) throws Exception{
		boolean regresar           = false;
		TcManticClientesDto cliente= null;
		try {
			cliente= (TcManticClientesDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, Long.valueOf(id));
			cliente.setIdFacturama(idFacturama);
			regresar= DaoFactory.getInstance().update(sesion, cliente)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizarCliente
	
	private boolean registrarBitacora(Session sesion, String id, String error) throws Exception{
		return registrarBitacora(sesion, id, error, true);
	} // registrarBitacora
	
	private boolean registrarBitacora(Session sesion, String id, String error, boolean cliente) throws Exception{
		boolean regresar                     = false;		
		TcManticFacturamaBitacoraDto bitacora= null;
		try {
			bitacora= new TcManticFacturamaBitacoraDto();
			bitacora.setIdKey(Long.valueOf(id));
			bitacora.setProceso(cliente ? REGISTRO_CLIENTE : REGISTRO_ARTICULO);
			bitacora.setObservacion(error);
			bitacora.setCodigo("99");
			regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizarCliente
	
	private boolean isCorrectId(String id){
		Long idConvert= -1L;
		try {
			idConvert= Long.valueOf(id);
		} // try
		catch (Exception e) {			
			return false;
		} // catch		
		return true;
	} // isCorrectId
	
	private boolean procesarArticulos(Session sesion){
		boolean regresar= false;		
		try {
			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarClientes
}

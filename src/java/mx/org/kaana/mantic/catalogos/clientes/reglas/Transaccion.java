package mx.org.kaana.mantic.catalogos.clientes.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteDomicilio;
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteRepresentante;
import mx.org.kaana.mantic.catalogos.clientes.bean.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.bean.RegistroCliente;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticClientesRepresentantesDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private RegistroCliente registroCliente;
	private String messageError;
	
	public Transaccion(RegistroCliente registroCliente) {
		this.registroCliente = registroCliente;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {
			this.registroCliente.getCliente().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			switch(accion){
				case AGREGAR:
					regresar= procesarCliente(sesion);
					break;
				case MODIFICAR:
					regresar= actualizarCliente(sesion);
					break;				
				case ELIMINAR:
					regresar= eliminarCliente(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception(this.messageError);
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError);
		} // catch		
		return regresar;
	} // ejecutar
	
	private boolean procesarCliente(Session sesion) throws Exception{
		boolean regresar= false;
		Long idCliente  = -1L;
		try {
			this.messageError= "Error al registrar el articulo";
			if(eliminarRegistros(sesion)){
				this.registroCliente.getCliente().setIdUsuario(JsfBase.getIdUsuario());
				this.registroCliente.getCliente().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
				idCliente= DaoFactory.getInstance().insert(sesion, this.registroCliente.getCliente());
				if(registraClientesDomicilios(sesion, idCliente)){
					if(registraClientesRepresentantes(sesion, idCliente))
						regresar= registraClientesTipoContacto(sesion, idCliente);																	
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarCliente
	
	private boolean actualizarCliente(Session sesion) throws Exception{
		boolean regresar= false;
		Long idCliente  = -1L;
		try {
			idCliente= this.registroCliente.getIdCliente();
			if(registraClientesDomicilios(sesion, idCliente)){
				if(registraClientesRepresentantes(sesion, idCliente)){
					if(registraClientesTipoContacto(sesion, idCliente))
						regresar= DaoFactory.getInstance().update(sesion, this.registroCliente.getCliente())>= 1L;
 				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizarCliente
	
	private boolean eliminarCliente(Session sesion) throws Exception{
		boolean regresar          = false;
		Map<String, Object> params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.registroCliente.getIdCliente());
			if(DaoFactory.getInstance().deleteAll(sesion, TrManticClienteDomicilioDto.class, params)> -1L){
				if(DaoFactory.getInstance().deleteAll(sesion, TrManticClientesRepresentantesDto.class, params)> -1L){
					if(DaoFactory.getInstance().deleteAll(sesion, TrManticClienteTipoContactoDto.class, params)> -1L)
						regresar= DaoFactory.getInstance().delete(sesion, TcManticClientesDto.class, this.registroCliente.getIdCliente())>= 1L;
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // eliminarCliente
	
	private boolean registraClientesDomicilios(Session sesion, Long idCliente) throws Exception{
		TrManticClienteDomicilioDto dto= null;
		ESql sqlAccion  = null;		
		int count       = 0;
		boolean validate= false;
		boolean regresar= false;
		try {
			for(ClienteDomicilio clienteDomicilio: this.registroCliente.getClientesDomicilio()){
				clienteDomicilio.setIdCliente(idCliente);				
				clienteDomicilio.setIdUsuario(JsfBase.getIdUsuario());
				dto= (TrManticClienteDomicilioDto) clienteDomicilio;				
				sqlAccion= clienteDomicilio.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdClienteDomicilio(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.registroCliente.getClientesDomicilio().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar los domicilios, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraClientesDomicilios
	
	private boolean registraClientesRepresentantes(Session sesion, Long idCliente) throws Exception{
		TrManticClientesRepresentantesDto dto= null;
		ESql sqlAccion  = null;		
		int count       = 0;
		boolean validate= false;
		boolean regresar= false;
		try {
			for(ClienteRepresentante clienteRepresentante: this.registroCliente.getClientesRepresentantes()){
				clienteRepresentante.setIdCliente(idCliente);				
				clienteRepresentante.setIdUsuario(JsfBase.getIdUsuario());
				dto= (TrManticClientesRepresentantesDto) clienteRepresentante;				
				sqlAccion= clienteRepresentante.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdClienteRepresentante(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.registroCliente.getClientesRepresentantes().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar los representantes, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraClientesRepresentantes
	
	private boolean registraClientesTipoContacto(Session sesion, Long idCliente) throws Exception{
		TrManticClienteTipoContactoDto dto= null;
		ESql sqlAccion  = null;		
		int count       = 0;
		boolean validate= false;
		boolean regresar= false;
		try {
			for(ClienteTipoContacto clienteTipoContacto: this.registroCliente.getClientesTiposContacto()){
				clienteTipoContacto.setIdCliente(idCliente);				
				clienteTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
				dto= (TrManticClienteTipoContactoDto) clienteTipoContacto;				
				sqlAccion= clienteTipoContacto.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdClienteTipoContacto(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.registroCliente.getClientesTiposContacto().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar los tipos de contacto, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraClientesTipoContacto
	
	private boolean eliminarRegistros(Session sesion) throws Exception{
		boolean regresar= true;
		int count       = 0;
		try {
			for(IBaseDto dto: this.registroCliente.getDeleteList()){
				if(DaoFactory.getInstance().delete(sesion, dto)>= 1L)
					count++;
			} // for
			regresar= count== this.registroCliente.getDeleteList().size();
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al eliminar registros";
		} // finally
		return regresar;
	} // eliminarRegistros
	
	private boolean registrar(Session sesion, IBaseDto dto) throws Exception{
		return DaoFactory.getInstance().insert(sesion, dto) >= 1L;
	} // registrar
	
	private boolean actualizar(Session sesion, IBaseDto dto) throws Exception{
		return DaoFactory.getInstance().update(sesion, dto) >= 1L;
	} // actualizar
}

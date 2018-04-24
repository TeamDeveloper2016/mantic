package mx.org.kaana.mantic.catalogos.clientes.reglas;

import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.clientes.bean.RegistroCliente;
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
	
	private boolean procesarCliente(Session sesion){
		return false;
	} // procesarCliente
	
	private boolean actualizarCliente(Session sesion){
		return false;
	} // actualizarCliente
	
	private boolean eliminarCliente(Session sesion){
		return false;
	} // eliminarCliente
}

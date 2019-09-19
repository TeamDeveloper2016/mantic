package mx.org.kaana.mantic.catalogos.grupos.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.catalogos.grupos.beans.Cliente;
import mx.org.kaana.mantic.db.dto.TcManticGruposClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticGruposDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private TcManticGruposDto grupo;	
	private List<Cliente> clientes;
	private String messageError;

	public Transaccion(TcManticGruposDto grupo) {
		this(grupo, new ArrayList<Cliente>());
	}

	public Transaccion(TcManticGruposDto grupo, List<Cliente> clientes) {
		this.grupo    = grupo;		
		this.clientes= clientes;
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("idGrupo", this.grupo.getIdGrupo());
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el grupo de clientes");
			switch(accion){
				case AGREGAR:
					regresar= DaoFactory.getInstance().insert(sesion, this.grupo)>= 1L;
					toFillClientes(sesion);
					break;
				case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.grupo)>= 1L;
					toFillClientes(sesion);
					break;				
				case ELIMINAR:
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticGruposClientesDto.class, params)>= 1L;
					regresar= regresar && DaoFactory.getInstance().delete(sesion, this.grupo)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	// ejecutar

	private void toFillClientes(Session sesion) throws Exception {
		List<Cliente> todos= (List<Cliente>)DaoFactory.getInstance().toEntitySet(sesion, Cliente.class, "TcManticGruposClientesDto", "clientes", this.grupo.toMap());
		for (Cliente item: todos) 
			if(this.clientes.indexOf(item)< 0)
				DaoFactory.getInstance().delete(sesion, item);
		for (Cliente cliente: this.clientes) {
			cliente.setIdGrupo(this.grupo.getIdGrupo());
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticGruposClientesDto.class, cliente.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, cliente);
		} // for
	}
	
}
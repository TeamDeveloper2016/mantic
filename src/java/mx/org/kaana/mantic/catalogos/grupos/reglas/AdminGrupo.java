package mx.org.kaana.mantic.catalogos.grupos.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.mantic.catalogos.grupos.beans.Cliente;
import mx.org.kaana.mantic.db.dto.TcManticGruposDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jun 15, 2012
 *@time 12:23:54 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class AdminGrupo implements Serializable {

  private static final long serialVersionUID = 8793667741599422569L;
	
	private TcManticGruposDto grupo;
	private List<Cliente> clientes;

	public AdminGrupo(TcManticGruposDto grupo) throws Exception {
		this.grupo=grupo;
		if(this.grupo.isValid()) {
			this.clientes= (List<Cliente>)DaoFactory.getInstance().toEntitySet(Cliente.class, "TcManticGruposClientesDto", "clientes", this.grupo.toMap());
			for (Cliente cliente: clientes) 
				cliente.getIdEntity().setKey(cliente.getIdCliente());
		}	
		else	
		  this.clientes= new ArrayList<>();
	}

	public TcManticGruposDto getGrupo() {
		return this.grupo;
	}

	public void setGrupo(TcManticGruposDto grupo) {
		this.grupo = grupo;
	}	

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes=clientes;
	}

  public void add() {
		Long idGrupoCliente= new Long((int)(Math.random()*10000));
		this.clientes.add(new Cliente(this.grupo.getIdGrupo(), -1L, -1* idGrupoCliente));
	}

	public void remove(Cliente seleccionado) {
		if(this.clientes.indexOf(seleccionado)>= 0)
		  this.clientes.remove(seleccionado);
	}
	
}

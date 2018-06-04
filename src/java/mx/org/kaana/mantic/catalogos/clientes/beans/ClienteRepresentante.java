package mx.org.kaana.mantic.catalogos.clientes.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticClienteRepresentanteDto;

public class ClienteRepresentante extends TrManticClienteRepresentanteDto{
	
	private static final long serialVersionUID = 3147968352567619231L;
	private ESql sqlAccion;
	private Boolean nuevo;
	private Boolean principal;

	public ClienteRepresentante() {
		this(-1L);
	}

	public ClienteRepresentante(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public ClienteRepresentante(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public ClienteRepresentante(Long key, ESql sqlAccion, Boolean nuevo) {
		this(key, sqlAccion, nuevo, false);
	}
	
	public ClienteRepresentante(Long key, ESql sqlAccion, Boolean nuevo, Boolean principal) {
		super(key);
		this.sqlAccion= sqlAccion;
		this.nuevo    = nuevo;
		this.principal= principal;						
	}

	public ESql getSqlAccion() {
		return sqlAccion;
	}

	public void setSqlAccion(ESql sqlAccion) {
		this.sqlAccion = sqlAccion;
	}	

	public Boolean getNuevo() {
		return nuevo;
	}

	public void setNuevo(Boolean nuevo) {
		this.nuevo = nuevo;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}	
}

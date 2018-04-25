package mx.org.kaana.mantic.catalogos.clientes.bean;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticClienteDomicilioDto;

public class ClienteDomicilio extends TrManticClienteDomicilioDto{
	
	private static final long serialVersionUID = 731679150148040999L;	
	private ESql sqlAccion;
	private Boolean nuevo;
	private Boolean modificar;	

	public ClienteDomicilio() {
		this(-1L);
	}

	public ClienteDomicilio(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public ClienteDomicilio(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public ClienteDomicilio(Long key, ESql sqlAccion, Boolean nuevo) {
		this(key, sqlAccion, nuevo, false);
	}
	
	public ClienteDomicilio(Long key, ESql sqlAccion, Boolean nuevo, Boolean modificar) {
		super(key);
		this.sqlAccion= sqlAccion;
		this.nuevo    = nuevo;
		this.modificar= modificar;
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

	public Boolean getModificar() {
		return modificar;
	}

	public void setModificar(Boolean modificar) {
		this.modificar = modificar;
	}
}

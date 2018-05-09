package mx.org.kaana.mantic.catalogos.personas.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticPersonaTipoContactoDto;

public class PersonaTipoContacto extends TrManticPersonaTipoContactoDto{
	
	private static final long serialVersionUID = 4804251646663919847L;
	private ESql sqlAccion;
	private Boolean nuevo;
	private String nombreCompleto;

	public PersonaTipoContacto() {
		this(-1L);
	}

	public PersonaTipoContacto(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public PersonaTipoContacto(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false, null);
	}
	
	public PersonaTipoContacto(Long key, ESql sqlAccion, Boolean nuevo, String nombreCompleto) {
		super(key);
		this.sqlAccion     = sqlAccion;
		this.nuevo         = nuevo;
		this.nombreCompleto= nombreCompleto;
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

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
}

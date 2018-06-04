package mx.org.kaana.mantic.catalogos.almacenes.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticAlmacenTipoContactoDto;

public class AlmacenTipoContacto extends TrManticAlmacenTipoContactoDto{
	
	private static final long serialVersionUID = 4804251646663919847L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public AlmacenTipoContacto() {
		this(-1L);
	}

	public AlmacenTipoContacto(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public AlmacenTipoContacto(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public AlmacenTipoContacto(Long key, ESql sqlAccion, Boolean nuevo) {
		super(key);
		this.sqlAccion= sqlAccion;
		this.nuevo    = nuevo;
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
}

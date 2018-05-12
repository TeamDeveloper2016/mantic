package mx.org.kaana.mantic.catalogos.proveedores.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticProveedorTipoContactoDto;

public class ProveedorTipoContacto extends TrManticProveedorTipoContactoDto{
	
	private static final long serialVersionUID = -1199288187458829924L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public ProveedorTipoContacto() {
		this(-1L);
	}

	public ProveedorTipoContacto(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public ProveedorTipoContacto(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public ProveedorTipoContacto(Long key, ESql sqlAccion, Boolean nuevo) {
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

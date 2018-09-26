package mx.org.kaana.mantic.catalogos.proveedores.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticProveedorPagoDto;

public class ProveedorCondicionPago extends TrManticProveedorPagoDto {
	
	private static final long serialVersionUID = 4330788422027394579L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public ProveedorCondicionPago() {
		this(-1L);
	}

	public ProveedorCondicionPago(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public ProveedorCondicionPago(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public ProveedorCondicionPago(Long key, ESql sqlAccion, Boolean nuevo) {
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

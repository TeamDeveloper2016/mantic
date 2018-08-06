package mx.org.kaana.mantic.compras.requisiciones.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticRequisicionesProveedoresDto;

public class RequisicionProveedor extends TcManticRequisicionesProveedoresDto implements Serializable{
	
	private ESql sqlAccion;
	private Boolean nuevo;

	public RequisicionProveedor() {
		this(-1L);
	}

	public RequisicionProveedor(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public RequisicionProveedor(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public RequisicionProveedor(Long key, ESql sqlAccion, Boolean nuevo) {
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

package mx.org.kaana.mantic.catalogos.empresas.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticEmpresaTipoContactoDto;

public class EmpresaTipoContacto extends TrManticEmpresaTipoContactoDto implements Serializable{

	private static final long serialVersionUID = -1308427992335649165L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public EmpresaTipoContacto() {
		this(-1L);
	}

	public EmpresaTipoContacto(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public EmpresaTipoContacto(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public EmpresaTipoContacto(Long key, ESql sqlAccion, Boolean nuevo) {
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
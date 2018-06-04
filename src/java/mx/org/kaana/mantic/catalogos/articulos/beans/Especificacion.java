package mx.org.kaana.mantic.catalogos.articulos.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticArticulosEspecificacionesDto;

public class Especificacion extends TcManticArticulosEspecificacionesDto{
	
	private static final long serialVersionUID = -3471561607536143002L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public Especificacion() {
		this(-1L);
	}

	public Especificacion(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public Especificacion(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public Especificacion(Long key, ESql sqlAccion, Boolean nuevo) {
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

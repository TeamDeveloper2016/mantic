package mx.org.kaana.mantic.catalogos.articulos.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDimencionesDto;

public class ArticuloDimencion extends TcManticArticulosDimencionesDto{

	private static final long serialVersionUID = -9204076933249586658L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public ArticuloDimencion() {
		this(-1L);
	}

	public ArticuloDimencion(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public ArticuloDimencion(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public ArticuloDimencion(Long key, ESql sqlAccion, Boolean nuevo) {
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

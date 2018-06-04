package mx.org.kaana.mantic.catalogos.articulos.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticArticuloPrecioSugeridoDto;

public class PrecioSugerido extends TrManticArticuloPrecioSugeridoDto{
	
	private static final long serialVersionUID = 5136123009517907872L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public PrecioSugerido() {
		this(-1L);
	}

	public PrecioSugerido(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public PrecioSugerido(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public PrecioSugerido(Long key, ESql sqlAccion, Boolean nuevo) {
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

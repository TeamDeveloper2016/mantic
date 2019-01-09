package mx.org.kaana.mantic.catalogos.inventarios.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;

public class ArticuloInventario extends TcManticInventariosDto {
	
	private ESql sqlAccion;
	private Boolean nuevo;

	public ArticuloInventario() {
		this(-1L);
	}

	public ArticuloInventario(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public ArticuloInventario(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public ArticuloInventario(Long key, ESql sqlAccion, Boolean nuevo) {
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

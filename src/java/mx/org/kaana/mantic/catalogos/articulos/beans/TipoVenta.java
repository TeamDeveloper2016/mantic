package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticArticuloTipoVentaDto;

public class TipoVenta extends TrManticArticuloTipoVentaDto implements Serializable{
	
	private static final long serialVersionUID = -8447000750173028692L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public TipoVenta() {
		this(-1L);
	}

	public TipoVenta(Long key) {
		this(key, ESql.INSERT);
	}
	
	public TipoVenta(Long key, ESql sqlAccion) {
		this(key, sqlAccion, true);
	}
	
	public TipoVenta(Long key, ESql sqlAccion, Boolean nuevo) {
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

package mx.org.kaana.mantic.catalogos.articulos.bean;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticArticulosCodigosDto;

public class ArticuloCodigo extends TcManticArticulosCodigosDto implements Serializable{
	
	private static final long serialVersionUID = -8447000750173028692L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public ArticuloCodigo() {
		this(-1L);
	}

	public ArticuloCodigo(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public ArticuloCodigo(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public ArticuloCodigo(Long key, ESql sqlAccion, Boolean nuevo) {
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

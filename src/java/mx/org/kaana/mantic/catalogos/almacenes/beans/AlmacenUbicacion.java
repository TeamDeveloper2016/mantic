package mx.org.kaana.mantic.catalogos.almacenes.beans;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;

public class AlmacenUbicacion extends TcManticAlmacenesUbicacionesDto{

	private static final long serialVersionUID = 8147501938429310994L;
	private ESql sqlAccion;
	private Boolean nuevo;

	public AlmacenUbicacion() {
		this(-1L);
	}

	public AlmacenUbicacion(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public AlmacenUbicacion(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public AlmacenUbicacion(Long key, ESql sqlAccion, Boolean nuevo) {
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
	
	public String getUbicacion(){
		if(getPiso()!= null && getCuarto()!= null && getCharola()!= null && getAnaquel()!= null)
			return getPiso().concat(" --> ").concat(getCuarto()).concat(" --> ").concat(getAnaquel()).concat(" --> ").concat(getCharola());
		else
			return null;
	}
}

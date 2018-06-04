package mx.org.kaana.mantic.catalogos.almacenes.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;

public class AlmacenArticulo extends TcManticAlmacenesArticulosDto implements Serializable{

	private static final long serialVersionUID = -1631386325836402627L;
	private ESql sqlAccion;
	private Boolean nuevo;	
	private Boolean modificar;
	//private AlmacenUbicacion articulo;
	private Entity articulo;
	private String piso;
	private String cuarto;
	private String anaquel;
	private String charola;

	public AlmacenArticulo() {
		this(-1L);
	} // AlmacenArticulo

	public AlmacenArticulo(Long key) {
		this(key, ESql.UPDATE, false);
	} // AlmacenArticulo
	
	public AlmacenArticulo(Long key, ESql sqlAccion, Boolean nuevo) {		
		this(key, sqlAccion, nuevo, false);
	} // AlmacenArticulo
	
	public AlmacenArticulo(Long key, ESql sqlAccion, Boolean nuevo, Boolean modificar) {
		this(key, sqlAccion, nuevo, modificar, null, null, null, null);
	}	
	
	public AlmacenArticulo(Long key, ESql sqlAccion, Boolean nuevo, Boolean modificar, String piso, String cuarto, String anaquel, String charola) {		
		super(key);
		this.sqlAccion= sqlAccion;
		this.nuevo    = nuevo;
		this.modificar= modificar;
		this.piso     = piso;
		this.cuarto   = cuarto;
		this.anaquel  = anaquel;
		this.charola  = charola;
	}	// // AlmacenArticulo

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

	public Boolean getModificar() {
		return modificar;
	}

	public void setModificar(Boolean modificar) {
		this.modificar = modificar;
	}

	public Entity getArticulo() {
		return articulo;
	}

	public void setArticulo(Entity articulo) {
		this.articulo = articulo;
		if(this.articulo!= null && this.articulo.getKey() > -1L)
			setIdArticulo(this.articulo.getKey());
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getCuarto() {
		return cuarto;
	}

	public void setCuarto(String cuarto) {
		this.cuarto = cuarto;
	}

	public String getAnaquel() {
		return anaquel;
	}

	public void setAnaquel(String anaquel) {
		this.anaquel = anaquel;
	}

	public String getCharola() {
		return charola;
	}

	public void setCharola(String charola) {
		this.charola = charola;
	}	
}
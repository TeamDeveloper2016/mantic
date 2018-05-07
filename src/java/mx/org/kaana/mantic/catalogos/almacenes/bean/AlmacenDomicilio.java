package mx.org.kaana.mantic.catalogos.almacenes.bean;

import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticAlmacenDomicilioDto;

public class AlmacenDomicilio extends TrManticAlmacenDomicilioDto{
	
	private static final long serialVersionUID = 731679150148040999L;	
	private ESql sqlAccion;
	private Boolean nuevo;	
	private Long consecutivo;
	private Boolean modificar;
	private Entity idEntidad;
	private Entity idMunicipio;
	private Entity idLocalidad;
	private Entity domicilio;
	private String codigoPostal;

	public AlmacenDomicilio() {
		this(-1L);
	}

	public AlmacenDomicilio(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public AlmacenDomicilio(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public AlmacenDomicilio(Long key, ESql sqlAccion, Boolean nuevo) {		
		this(key, sqlAccion, nuevo, 0L, false, null, null, null, null, null);
	}
	
	public AlmacenDomicilio(Long key, ESql sqlAccion, Boolean nuevo, Long consecutivo, Boolean modificar, Entity idEntidad, Entity idMunicipio, Entity idLocalidad, Entity domicilio, String codigoPostal) {		
		super(key);
		this.sqlAccion   = sqlAccion;
		this.nuevo       = nuevo;
		this.consecutivo = consecutivo;
		this.modificar   = modificar;
		this.idEntidad   = idEntidad;
		this.idMunicipio = idMunicipio;
		this.idLocalidad = idLocalidad;
		this.codigoPostal= codigoPostal;
		this.domicilio   = domicilio;
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

	public Long getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Boolean getModificar() {
		return modificar;
	}

	public void setModificar(Boolean modificar) {
		this.modificar = modificar;
	}	

	public Entity getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(Entity idEntidad) {
		this.idEntidad = idEntidad;
	}

	public Entity getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Entity idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public Entity getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Entity idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

	public Entity getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Entity domicilio) {
		this.domicilio = domicilio;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}	
}

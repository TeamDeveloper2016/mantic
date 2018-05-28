package mx.org.kaana.mantic.catalogos.personas.beans;

import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticPersonaDomicilioDto;

public class PersonaDomicilio extends TrManticPersonaDomicilioDto{
	
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
	private String calle;
	private String exterior;
	private String interior;
	private String colonia;
	private String entreCalle;
	private String yCalle;
	private boolean nuevoCp;

	public PersonaDomicilio() {
		this(-1L);
	}

	public PersonaDomicilio(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public PersonaDomicilio(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public PersonaDomicilio(Long key, ESql sqlAccion, Boolean nuevo) {		
		this(key, sqlAccion, nuevo, 0L, false, null, null, null, null, null, null, null, null, null, null, null, false);
	}
	
	public PersonaDomicilio(Long key, ESql sqlAccion, Boolean nuevo, Long consecutivo, Boolean modificar, Entity idEntidad, Entity idMunicipio, Entity idLocalidad, Entity domicilio, String codigoPostal, String calle, String exterior, String interior, String colonia, String entreCalle, String yCalle, boolean nuevoCp) {		
		super(key);
		this.sqlAccion   = sqlAccion;
		this.nuevo       = nuevo;
		this.consecutivo = consecutivo;
		this.modificar   = modificar;
		this.idEntidad   = idEntidad;
		this.idMunicipio = idMunicipio;
		this.idLocalidad = idLocalidad;
		this.codigoPostal= codigoPostal;
		this.calle       = calle;
		this.exterior    = exterior;
		this.interior    = interior;
		this.colonia     = colonia;
		this.entreCalle  = entreCalle;
		this.yCalle      = yCalle;		
		this.nuevoCp     = nuevoCp;
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

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getExterior() {
		return exterior;
	}

	public void setExterior(String exterior) {
		this.exterior = exterior;
	}

	public String getInterior() {
		return interior;
	}

	public void setInterior(String interior) {
		this.interior = interior;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getEntreCalle() {
		return entreCalle;
	}

	public void setEntreCalle(String entreCalle) {
		this.entreCalle = entreCalle;
	}

	public String getyCalle() {
		return yCalle;
	}

	public void setyCalle(String yCalle) {
		this.yCalle = yCalle;
	}

	public boolean isNuevoCp() {
		return nuevoCp;
	}

	public void setNuevoCp(boolean nuevoCp) {
		this.nuevoCp = nuevoCp;
	}	
}
package mx.org.kaana.mantic.catalogos.almacenes.bean;

import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticAlmacenDomicilioDto;

public class AlmacenDomicilio extends TrManticAlmacenDomicilioDto{
	
	private static final long serialVersionUID = 731679150148040999L;	
	private ESql sqlAccion;
	private Boolean nuevo;	
	private Long consecutivo;
	private Boolean modificar;
	private Long idEntidad;
	private Long idMunicipio;
	private Long idLocalidad;

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
		this(key, sqlAccion, nuevo, 0L, false, -1L, -1L, -1L);
	}
	
	public AlmacenDomicilio(Long key, ESql sqlAccion, Boolean nuevo, Long consecutivo, Boolean modificar, Long idEntidad, Long idMunicipio, Long idLocalidad) {		
		super(key);
		this.sqlAccion     = sqlAccion;
		this.nuevo         = nuevo;
		this.consecutivo   = consecutivo;
		this.modificar     = modificar;
		this.idEntidad     = idEntidad;
		this.idMunicipio   = idMunicipio;
		this.idLocalidad   = idLocalidad;
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

	public Long getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(Long idEntidad) {
		this.idEntidad = idEntidad;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	
	public Long getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Long idLocalidad) {
		this.idLocalidad = idLocalidad;
	}	
}

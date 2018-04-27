package mx.org.kaana.mantic.catalogos.clientes.bean;

import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;

public class Domicilio extends TcManticDomiciliosDto{
	
	private static final long serialVersionUID = -3112775965972427532L;
	private Long idEntidad;
	private Long idMunicipio;
	private Long idCodigoPostal;	
	private boolean nuevoCp;
	private Boolean principal;	
	private Long idTipoDomicilio;
	
	public Domicilio() {
		this(-1L,-1L,-1L, false, false, -1L);
	}

	public Domicilio(Long idEntidad, Long idMunicipio, Long idCodigoPostal, boolean nuevoCp, Boolean principal, Long idTipoDomicilio) {
		this.idEntidad      = idEntidad;
		this.idMunicipio    = idMunicipio;
		this.idCodigoPostal = idCodigoPostal;
		this.nuevoCp        = nuevoCp;
		this.principal      = principal;
		this.idTipoDomicilio= idTipoDomicilio;
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

	public Long getIdCodigoPostal() {
		return idCodigoPostal;
	}

	public void setIdCodigoPostal(Long idCodigoPostal) {
		this.idCodigoPostal = idCodigoPostal;
	}	

	public boolean isNuevoCp() {
		return nuevoCp;
	}

	public void setNuevoCp(boolean nuevoCp) {
		this.nuevoCp = nuevoCp;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}	

	public Long getIdTipoDomicilio() {
		return idTipoDomicilio;
	}

	public void setIdTipoDomicilio(Long idTipoDomicilio) {
		this.idTipoDomicilio = idTipoDomicilio;
	}	
}
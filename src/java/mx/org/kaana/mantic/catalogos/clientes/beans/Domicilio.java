package mx.org.kaana.mantic.catalogos.clientes.beans;

import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;

public class Domicilio extends TcManticDomiciliosDto{
	
	private static final long serialVersionUID = -3112775965972427532L;
	private Entity idEntidad;
	private Entity idMunicipio;
	private Entity localidad;
	private Entity domicilio;
	private Long idCodigoPostal;	
	private boolean nuevoCp;
	private Boolean principal;	
	private Long idTipoDomicilio;
	private Long idClienteDomicilio;
	
	public Domicilio() {
		this(null, null, null, null, -1L, false, false, -1L);
	}

	public Domicilio(Entity idEntidad, Entity idMunicipio, Entity localidad, Entity domicilio, Long idCodigoPostal, boolean nuevoCp, Boolean principal, Long idTipoDomicilio) {
		this(null, null, null, null, -1L, false, false, -1L, null);
	}
		
	public Domicilio(Entity idEntidad, Entity idMunicipio, Entity localidad, Entity domicilio, Long idCodigoPostal, boolean nuevoCp, Boolean principal, Long idTipoDomicilio, Long idClienteDomicilio) {
		this.idEntidad      = idEntidad;
		this.idMunicipio    = idMunicipio;
		this.idCodigoPostal = idCodigoPostal;
		this.nuevoCp        = nuevoCp;
		this.principal      = principal;
		this.idTipoDomicilio= idTipoDomicilio;
		this.localidad      = localidad;
		this.domicilio      = domicilio;
		this.idClienteDomicilio= idClienteDomicilio;
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

	public Entity getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Entity localidad) {
		this.localidad = localidad;
	}	

	public Entity getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Entity domicilio) {
		this.domicilio = domicilio;
	}	

	public Long getIdClienteDomicilio() {
		return idClienteDomicilio;
	}

	public void setIdClienteDomicilio(Long idClienteDomicilio) {
		this.idClienteDomicilio = idClienteDomicilio;
	}	
}
package mx.org.kaana.mantic.catalogos.clientes.bean;

import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;

public class Domicilio extends TcManticDomiciliosDto{
	
	private static final long serialVersionUID = -3112775965972427532L;
	private Long idEntidad;
	private Long idMunicipio;
	private Long idCodigoPostal;	
	
	public Domicilio() {
		this(-1L,-1L,-1L);
	}

	public Domicilio(Long idEntidad, Long idMunicipio, Long idCodigoPostal) {
		this.idEntidad     = idEntidad;
		this.idMunicipio   = idMunicipio;
		this.idCodigoPostal= idCodigoPostal;
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
}
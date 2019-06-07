package mx.org.kaana.mantic.catalogos.empaques.beans;

import java.io.Serializable;

public class EmpaqueUnidad implements Serializable{

	private static final long serialVersionUID = 3977528317511688039L;
	
	private Long idEmpaque;	
	private String claveEmpaque;
	private String nombreEmpaque;
	private String descripcionEmpaque;
	private Boolean empaqueExistente;	
	private Long idUnidad;	
	private String claveUnidad;
	private String nombreUnidad;
	private String descripcionUnidad;
	private Boolean unidadExistente;	
	private Long proporcion;
	private String observaciones;

	public EmpaqueUnidad() {
		this(new Long(-1L), "", "", "", true, new Long(-1L), "", "", "", true, 0L, "");
	}

	public EmpaqueUnidad(Long idEmpaque, String claveEmpaque, String nombreEmpaque, String descripcionEmpaque, Boolean empaqueExistente, Long idUnidad, String claveUnidad, String nombreUnidad, String descripcionUnidad, Boolean unidadExistente, Long proporcion, String observaciones) {
		this.idEmpaque         = idEmpaque;
		this.claveEmpaque      = claveEmpaque;
		this.nombreEmpaque     = nombreEmpaque;
		this.descripcionEmpaque= descripcionEmpaque;
		this.empaqueExistente  = empaqueExistente;
		this.idUnidad          = idUnidad;
		this.claveUnidad       = claveUnidad;
		this.nombreUnidad      = nombreUnidad;
		this.descripcionUnidad = descripcionUnidad;
		this.unidadExistente   = unidadExistente;
		this.proporcion        = proporcion;
		this.observaciones     = observaciones;
	}		

	public Long getIdEmpaque() {
		return idEmpaque;
	}

	public void setIdEmpaque(Long idEmpaque) {
		this.idEmpaque = idEmpaque;
	}

	public String getClaveEmpaque() {
		return claveEmpaque;
	}

	public void setClaveEmpaque(String claveEmpaque) {
		this.claveEmpaque = claveEmpaque;
	}

	public String getNombreEmpaque() {
		return nombreEmpaque;
	}

	public void setNombreEmpaque(String nombreEmpaque) {
		this.nombreEmpaque = nombreEmpaque;
	}

	public String getDescripcionEmpaque() {
		return descripcionEmpaque;
	}

	public void setDescripcionEmpaque(String descripcionEmpaque) {
		this.descripcionEmpaque = descripcionEmpaque;
	}

	public Boolean getEmpaqueExistente() {
		return empaqueExistente;
	}

	public void setEmpaqueExistente(Boolean empaqueExistente) {
		this.empaqueExistente = empaqueExistente;
	}

	public Long getIdUnidad() {
		return idUnidad;
	}

	public void setIdUnidad(Long idUnidad) {
		this.idUnidad = idUnidad;
	}

	public String getClaveUnidad() {
		return claveUnidad;
	}

	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}

	public String getNombreUnidad() {
		return nombreUnidad;
	}

	public void setNombreUnidad(String nombreUnidad) {
		this.nombreUnidad = nombreUnidad;
	}

	public String getDescripcionUnidad() {
		return descripcionUnidad;
	}

	public void setDescripcionUnidad(String descripcionUnidad) {
		this.descripcionUnidad = descripcionUnidad;
	}

	public Boolean getUnidadExistente() {
		return unidadExistente;
	}

	public void setUnidadExistente(Boolean unidadExistente) {
		this.unidadExistente = unidadExistente;
	}

	public Long getProporcion() {
		return proporcion;
	}

	public void setProporcion(Long proporcion) {
		this.proporcion = proporcion;
	}		

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}	
}

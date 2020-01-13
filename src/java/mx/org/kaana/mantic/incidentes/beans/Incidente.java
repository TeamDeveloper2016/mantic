package mx.org.kaana.mantic.incidentes.beans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import mx.org.kaana.mantic.db.dto.TcManticIncidentesDto;

public class Incidente implements Serializable{

	private static final long serialVersionUID= 5491973442869961621L;	
	private Long idIncidente;
	private Long idPersona;
	private Long idTipoIncidente;
	private Long idIncidenteEstatus;
	private Date vigenciaInicio;
	private Date vigenciaFin;
	private String observaciones;

	public Incidente() {
		this(-1L, -1L, -1L, -1L, new Date(Calendar.getInstance().getTimeInMillis()), new Date(Calendar.getInstance().getTimeInMillis()), "");
	}

	public Incidente(TcManticIncidentesDto dto){
		this(dto.getIdIncidente(), dto.getIdPersona(), dto.getIdTipoIncidente(), dto.getIdIncidenteEstatus(), dto.getVigenciaInicio(), dto.getVigenciaFin(), dto.getObservaciones());
	}
	
	public Incidente(Long idIncidente, Long idPersona, Long idTipoIncidente, Long idIncidenteEstatus, Date vigenciaInicio, Date vigenciaFin, String observaciones) {
		this.idIncidente       = idIncidente;
		this.idPersona         = idPersona;
		this.idTipoIncidente   = idTipoIncidente;
		this.idIncidenteEstatus= idIncidenteEstatus;
		this.vigenciaInicio    = vigenciaInicio;
		this.vigenciaFin       = vigenciaFin;
		this.observaciones     = observaciones;
	}	

	public Long getIdIncidente() {
		return idIncidente;
	}

	public void setIdIncidente(Long idIncidente) {
		this.idIncidente = idIncidente;
	}
	
	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Long getIdTipoIncidente() {
		return idTipoIncidente;
	}

	public void setIdTipoIncidente(Long idTipoIncidente) {
		this.idTipoIncidente = idTipoIncidente;
	}

	public Long getIdIncidenteEstatus() {
		return idIncidenteEstatus;
	}

	public void setIdIncidenteEstatus(Long idIncidenteEstatus) {
		this.idIncidenteEstatus = idIncidenteEstatus;
	}

	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}

	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}

	public Date getVigenciaFin() {
		return vigenciaFin;
	}

	public void setVigenciaFin(Date vigenciaFin) {
		this.vigenciaFin = vigenciaFin;
	}
	
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}	
}
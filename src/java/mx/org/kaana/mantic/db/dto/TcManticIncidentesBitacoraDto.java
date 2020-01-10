package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_incidentes_bitacora")
public class TcManticIncidentesBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_incidente")
  private Long idIncidente;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_incidente_bitacora")
  private Long idIncidenteBitacora;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_incidente_estatus")
  private Long idIncidenteEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticIncidentesBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticIncidentesBitacoraDto(Long key) {
    this(null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticIncidentesBitacoraDto(String justificacion, Long idIncidente, Long idIncidenteBitacora, Long idUsuario, Long idIncidenteEstatus) {
    setJustificacion(justificacion);
    setIdIncidente(idIncidente);
    setIdIncidenteBitacora(idIncidenteBitacora);
    setIdUsuario(idUsuario);
    setIdIncidenteEstatus(idIncidenteEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdIncidente(Long idIncidente) {
    this.idIncidente = idIncidente;
  }

  public Long getIdIncidente() {
    return idIncidente;
  }

  public void setIdIncidenteBitacora(Long idIncidenteBitacora) {
    this.idIncidenteBitacora = idIncidenteBitacora;
  }

  public Long getIdIncidenteBitacora() {
    return idIncidenteBitacora;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdIncidenteEstatus(Long idIncidenteEstatus) {
    this.idIncidenteEstatus = idIncidenteEstatus;
  }

  public Long getIdIncidenteEstatus() {
    return idIncidenteEstatus;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdIncidenteBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idIncidenteBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdIncidente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdIncidenteBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdIncidenteEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("justificacion", getJustificacion());
		regresar.put("idIncidente", getIdIncidente());
		regresar.put("idIncidenteBitacora", getIdIncidenteBitacora());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idIncidenteEstatus", getIdIncidenteEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getJustificacion(), getIdIncidente(), getIdIncidenteBitacora(), getIdUsuario(), getIdIncidenteEstatus(), getRegistro()
    };
    return regresar;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idIncidenteBitacora~");
    regresar.append(getIdIncidenteBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdIncidenteBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticIncidentesBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdIncidenteBitacora()!= null && getIdIncidenteBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticIncidentesBitacoraDto other = (TcManticIncidentesBitacoraDto) obj;
    if (getIdIncidenteBitacora() != other.idIncidenteBitacora && (getIdIncidenteBitacora() == null || !getIdIncidenteBitacora().equals(other.idIncidenteBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdIncidenteBitacora() != null ? getIdIncidenteBitacora().hashCode() : 0);
    return hash;
  }
}
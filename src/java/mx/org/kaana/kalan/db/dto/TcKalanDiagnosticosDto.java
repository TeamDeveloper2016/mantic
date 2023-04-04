package mx.org.kaana.kalan.db.dto;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Lob;
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
@Table(name="tc_kalan_diagnosticos")
public class TcKalanDiagnosticosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_diagnostico")
  private Long idDiagnostico;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="diagnostico")
  private String diagnostico;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_cita")
  private Long idCita;
  @Column (name="tratamiento")
  private String tratamiento;
  @Column (name="registro")
  private Timestamp registro;

  public TcKalanDiagnosticosDto() {
    this(new Long(-1L));
  }

  public TcKalanDiagnosticosDto(Long key) {
    this(new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcKalanDiagnosticosDto(Long idDiagnostico, Long idUsuario, String diagnostico, String observaciones, Long idCita, String tratamiento) {
    setIdDiagnostico(idDiagnostico);
    setIdUsuario(idUsuario);
    setDiagnostico(diagnostico);
    setObservaciones(observaciones);
    setIdCita(idCita);
    setTratamiento(tratamiento);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdDiagnostico(Long idDiagnostico) {
    this.idDiagnostico = idDiagnostico;
  }

  public Long getIdDiagnostico() {
    return idDiagnostico;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDiagnostico(String diagnostico) {
    this.diagnostico = diagnostico;
  }

  public String getDiagnostico() {
    return diagnostico;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdCita(Long idCita) {
    this.idCita = idCita;
  }

  public Long getIdCita() {
    return idCita;
  }

  public void setTratamiento(String tratamiento) {
    this.tratamiento = tratamiento;
  }

  public String getTratamiento() {
    return tratamiento;
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
  	return getIdDiagnostico();
  }

  @Override
  public void setKey(Long key) {
  	this.idDiagnostico = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdDiagnostico());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDiagnostico());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCita());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTratamiento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idDiagnostico", getIdDiagnostico());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("diagnostico", getDiagnostico());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idCita", getIdCita());
		regresar.put("tratamiento", getTratamiento());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdDiagnostico(), getIdUsuario(), getDiagnostico(), getObservaciones(), getIdCita(), getTratamiento(), getRegistro()
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
    regresar.append("idDiagnostico~");
    regresar.append(getIdDiagnostico());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDiagnostico());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanDiagnosticosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDiagnostico()!= null && getIdDiagnostico()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanDiagnosticosDto other = (TcKalanDiagnosticosDto) obj;
    if (getIdDiagnostico() != other.idDiagnostico && (getIdDiagnostico() == null || !getIdDiagnostico().equals(other.idDiagnostico))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDiagnostico() != null ? getIdDiagnostico().hashCode() : 0);
    return hash;
  }

}



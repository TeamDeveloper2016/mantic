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
@Table(name="tc_kalan_diagnosticos_detalles")
public class TcKalanDiagnosticosDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="frecuencia")
  private Double frecuencia;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_diagnostico_detalle")
  private Long idDiagnosticoDetalle;
  @Column (name="id_medicamento")
  private Long idMedicamento;
  @Column (name="id_diagnostico")
  private Long idDiagnostico;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="dosis")
  private Double dosis;
  @Column (name="medicamento")
  private String medicamento;
  @Column (name="registro")
  private Timestamp registro;

  public TcKalanDiagnosticosDetallesDto() {
    this(new Long(-1L));
  }

  public TcKalanDiagnosticosDetallesDto(Long key) {
    this(null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcKalanDiagnosticosDetallesDto(Double frecuencia, Long idDiagnosticoDetalle, Long idMedicamento, Long idDiagnostico, Long idUsuario, Double dosis, String medicamento) {
    setFrecuencia(frecuencia);
    setIdDiagnosticoDetalle(idDiagnosticoDetalle);
    setIdMedicamento(idMedicamento);
    setIdDiagnostico(idDiagnostico);
    setIdUsuario(idUsuario);
    setDosis(dosis);
    setMedicamento(medicamento);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setFrecuencia(Double frecuencia) {
    this.frecuencia = frecuencia;
  }

  public Double getFrecuencia() {
    return frecuencia;
  }

  public void setIdDiagnosticoDetalle(Long idDiagnosticoDetalle) {
    this.idDiagnosticoDetalle = idDiagnosticoDetalle;
  }

  public Long getIdDiagnosticoDetalle() {
    return idDiagnosticoDetalle;
  }

  public void setIdMedicamento(Long idMedicamento) {
    this.idMedicamento = idMedicamento;
  }

  public Long getIdMedicamento() {
    return idMedicamento;
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

  public void setDosis(Double dosis) {
    this.dosis = dosis;
  }

  public Double getDosis() {
    return dosis;
  }

  public void setMedicamento(String medicamento) {
    this.medicamento = medicamento;
  }

  public String getMedicamento() {
    return medicamento;
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
  	return getIdDiagnosticoDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idDiagnosticoDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getFrecuencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDiagnosticoDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMedicamento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDiagnostico());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDosis());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMedicamento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("frecuencia", getFrecuencia());
		regresar.put("idDiagnosticoDetalle", getIdDiagnosticoDetalle());
		regresar.put("idMedicamento", getIdMedicamento());
		regresar.put("idDiagnostico", getIdDiagnostico());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("dosis", getDosis());
		regresar.put("medicamento", getMedicamento());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getFrecuencia(), getIdDiagnosticoDetalle(), getIdMedicamento(), getIdDiagnostico(), getIdUsuario(), getDosis(), getMedicamento(), getRegistro()
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
    regresar.append("idDiagnosticoDetalle~");
    regresar.append(getIdDiagnosticoDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDiagnosticoDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanDiagnosticosDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDiagnosticoDetalle()!= null && getIdDiagnosticoDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanDiagnosticosDetallesDto other = (TcKalanDiagnosticosDetallesDto) obj;
    if (getIdDiagnosticoDetalle() != other.idDiagnosticoDetalle && (getIdDiagnosticoDetalle() == null || !getIdDiagnosticoDetalle().equals(other.idDiagnosticoDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDiagnosticoDetalle() != null ? getIdDiagnosticoDetalle().hashCode() : 0);
    return hash;
  }

}



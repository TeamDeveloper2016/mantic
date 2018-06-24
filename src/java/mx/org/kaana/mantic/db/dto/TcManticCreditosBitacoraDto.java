package mx.org.kaana.mantic.db.dto;

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
@Table(name="tc_mantic_creditos_bitacora")
public class TcManticCreditosBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_credito_estatus")
  private Long idCreditoEstatus;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_credito_bitacora")
  private Long idCreditoBitacora;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_credito_nota")
  private Long idCreditoNota;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticCreditosBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticCreditosBitacoraDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcManticCreditosBitacoraDto(String consecutivo, String justificacion, Long idCreditoEstatus, Long idCreditoBitacora, Long idUsuario, Long idCreditoNota, Double importe) {
    setConsecutivo(consecutivo);
    setJustificacion(justificacion);
    setIdCreditoEstatus(idCreditoEstatus);
    setIdCreditoBitacora(idCreditoBitacora);
    setIdUsuario(idUsuario);
    setIdCreditoNota(idCreditoNota);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdCreditoEstatus(Long idCreditoEstatus) {
    this.idCreditoEstatus = idCreditoEstatus;
  }

  public Long getIdCreditoEstatus() {
    return idCreditoEstatus;
  }

  public void setIdCreditoBitacora(Long idCreditoBitacora) {
    this.idCreditoBitacora = idCreditoBitacora;
  }

  public Long getIdCreditoBitacora() {
    return idCreditoBitacora;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdCreditoNota(Long idCreditoNota) {
    this.idCreditoNota = idCreditoNota;
  }

  public Long getIdCreditoNota() {
    return idCreditoNota;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
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
  	return getIdCreditoBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idCreditoBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCreditoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCreditoBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCreditoNota());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("justificacion", getJustificacion());
		regresar.put("idCreditoEstatus", getIdCreditoEstatus());
		regresar.put("idCreditoBitacora", getIdCreditoBitacora());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idCreditoNota", getIdCreditoNota());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getJustificacion(), getIdCreditoEstatus(), getIdCreditoBitacora(), getIdUsuario(), getIdCreditoNota(), getImporte(), getRegistro()
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
    regresar.append("idCreditoBitacora~");
    regresar.append(getIdCreditoBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCreditoBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCreditosBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCreditoBitacora()!= null && getIdCreditoBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCreditosBitacoraDto other = (TcManticCreditosBitacoraDto) obj;
    if (getIdCreditoBitacora() != other.idCreditoBitacora && (getIdCreditoBitacora() == null || !getIdCreditoBitacora().equals(other.idCreditoBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCreditoBitacora() != null ? getIdCreditoBitacora().hashCode() : 0);
    return hash;
  }

}



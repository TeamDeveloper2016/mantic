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
@Table(name="tc_mantic_garantias_bitacora")
public class TcManticGarantiasBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_garantia")
  private Long idGarantia;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_garantia_estatus")
  private Long idGarantiaEstatus;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_garantia_bitacora")
  private Long idGarantiaBitacora;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticGarantiasBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticGarantiasBitacoraDto(Long key) {
    this(null, null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticGarantiasBitacoraDto(Long idGarantia, String consecutivo, String justificacion, Long idUsuario, Long idGarantiaEstatus, Long idGarantiaBitacora, Double importe) {
    setIdGarantia(idGarantia);
    setConsecutivo(consecutivo);
    setJustificacion(justificacion);
    setIdUsuario(idUsuario);
    setIdGarantiaEstatus(idGarantiaEstatus);
    setIdGarantiaBitacora(idGarantiaBitacora);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdGarantia(Long idGarantia) {
    this.idGarantia = idGarantia;
  }

  public Long getIdGarantia() {
    return idGarantia;
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

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdGarantiaEstatus(Long idGarantiaEstatus) {
    this.idGarantiaEstatus = idGarantiaEstatus;
  }

  public Long getIdGarantiaEstatus() {
    return idGarantiaEstatus;
  }

  public void setIdGarantiaBitacora(Long idGarantiaBitacora) {
    this.idGarantiaBitacora = idGarantiaBitacora;
  }

  public Long getIdGarantiaBitacora() {
    return idGarantiaBitacora;
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
  	return getIdGarantiaBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idGarantiaBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdGarantia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGarantiaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGarantiaBitacora());
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
		regresar.put("idGarantia", getIdGarantia());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("justificacion", getJustificacion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idGarantiaEstatus", getIdGarantiaEstatus());
		regresar.put("idGarantiaBitacora", getIdGarantiaBitacora());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdGarantia(), getConsecutivo(), getJustificacion(), getIdUsuario(), getIdGarantiaEstatus(), getIdGarantiaBitacora(), getImporte(), getRegistro()
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
    regresar.append("idGarantiaBitacora~");
    regresar.append(getIdGarantiaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdGarantiaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticGarantiasBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdGarantiaBitacora()!= null && getIdGarantiaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticGarantiasBitacoraDto other = (TcManticGarantiasBitacoraDto) obj;
    if (getIdGarantiaBitacora() != other.idGarantiaBitacora && (getIdGarantiaBitacora() == null || !getIdGarantiaBitacora().equals(other.idGarantiaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdGarantiaBitacora() != null ? getIdGarantiaBitacora().hashCode() : 0);
    return hash;
  }

}



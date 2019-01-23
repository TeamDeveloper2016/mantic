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
@Table(name="tc_mantic_confrontas")
public class TcManticConfrontasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_confronta")
  private Long idConfronta;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="orden")
  private Long orden;
  @Column (name="id_transferencia")
  private Long idTransferencia;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticConfrontasDto() {
    this(new Long(-1L));
  }

  public TcManticConfrontasDto(Long key) {
    this(null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticConfrontasDto(String consecutivo, Long idConfronta, Long idUsuario, String observaciones, Long orden, Long idTransferencia, Long ejercicio) {
    setConsecutivo(consecutivo);
    setIdConfronta(idConfronta);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setOrden(orden);
    setIdTransferencia(idTransferencia);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setIdConfronta(Long idConfronta) {
    this.idConfronta = idConfronta;
  }

  public Long getIdConfronta() {
    return idConfronta;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdTransferencia(Long idTransferencia) {
    this.idTransferencia = idTransferencia;
  }

  public Long getIdTransferencia() {
    return idTransferencia;
  }

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
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
  	return getIdConfronta();
  }

  @Override
  public void setKey(Long key) {
  	this.idConfronta = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConfronta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idConfronta", getIdConfronta());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("orden", getOrden());
		regresar.put("idTransferencia", getIdTransferencia());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getIdConfronta(), getIdUsuario(), getObservaciones(), getOrden(), getIdTransferencia(), getEjercicio(), getRegistro()
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
    regresar.append("idConfronta~");
    regresar.append(getIdConfronta());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdConfronta());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticConfrontasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdConfronta()!= null && getIdConfronta()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticConfrontasDto other = (TcManticConfrontasDto) obj;
    if (getIdConfronta() != other.idConfronta && (getIdConfronta() == null || !getIdConfronta().equals(other.idConfronta))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdConfronta() != null ? getIdConfronta().hashCode() : 0);
    return hash;
  }

}



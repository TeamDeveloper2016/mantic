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
@Table(name="tc_mantic_cierres_retiros")
public class TcManticCierresRetirosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cierre_retiro")
  private Long idCierreRetiro;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_cierre_caja")
  private Long idCierreCaja;
  @Column (name="orden")
  private Long orden;
  @Column (name="importe")
  private Double importe;
  @Column (name="dia")
  private Date dia;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticCierresRetirosDto() {
    this(new Long(-1L));
  }

  public TcManticCierresRetirosDto(Long key) {
    this(null, null, new Long(-1L), null, null, null, null, new Date(Calendar.getInstance().getTimeInMillis()));
    setKey(key);
  }

  public TcManticCierresRetirosDto(String consecutivo, Long idUsuario, Long idCierreRetiro, String observaciones, Long idCierreCaja, Long orden, Double importe, Date dia) {
    setConsecutivo(consecutivo);
    setIdUsuario(idUsuario);
    setIdCierreRetiro(idCierreRetiro);
    setObservaciones(observaciones);
    setIdCierreCaja(idCierreCaja);
    setOrden(orden);
    setImporte(importe);
    setDia(dia);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdCierreRetiro(Long idCierreRetiro) {
    this.idCierreRetiro = idCierreRetiro;
  }

  public Long getIdCierreRetiro() {
    return idCierreRetiro;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdCierreCaja(Long idCierreCaja) {
    this.idCierreCaja = idCierreCaja;
  }

  public Long getIdCierreCaja() {
    return idCierreCaja;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
  }

  public void setDia(Date dia) {
    this.dia = dia;
  }

  public Date getDia() {
    return dia;
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
  	return getIdCierreRetiro();
  }

  @Override
  public void setKey(Long key) {
  	this.idCierreRetiro = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierreRetiro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierreCaja());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idCierreRetiro", getIdCierreRetiro());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idCierreCaja", getIdCierreCaja());
		regresar.put("orden", getOrden());
		regresar.put("importe", getImporte());
		regresar.put("dia", getDia());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getIdUsuario(), getIdCierreRetiro(), getObservaciones(), getIdCierreCaja(), getOrden(), getImporte(), getDia(), getRegistro()
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
    regresar.append("idCierreRetiro~");
    regresar.append(getIdCierreRetiro());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCierreRetiro());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCierresRetirosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCierreRetiro()!= null && getIdCierreRetiro()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCierresRetirosDto other = (TcManticCierresRetirosDto) obj;
    if (getIdCierreRetiro() != other.idCierreRetiro && (getIdCierreRetiro() == null || !getIdCierreRetiro().equals(other.idCierreRetiro))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCierreRetiro() != null ? getIdCierreRetiro().hashCode() : 0);
    return hash;
  }

}



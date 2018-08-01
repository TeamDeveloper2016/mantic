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
@Table(name="tc_mantic_cierres_cajas")
public class TcManticCierresCajasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
  @Column (name="id_cierre")
  private Long idCierre;
  @Column (name="acumulado")
  private Double acumulado;
  @Column (name="id_caja")
  private Long idCaja;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cierre_caja")
  private Long idCierreCaja;
  @Column (name="saldo")
  private Double saldo;
  @Column (name="dia")
  private Date dia;
  @Column (name="importe")
  private Double importe;
  @Column (name="disponible")
  private Double disponible;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticCierresCajasDto() {
    this(new Long(-1L));
  }

  public TcManticCierresCajasDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, new Date(Calendar.getInstance().getTimeInMillis()), null, null);
    setKey(key);
  }

  public TcManticCierresCajasDto(Long idTipoMedioPago, Long idCierre, Double acumulado, Long idCaja, Long idCierreCaja, Double saldo, Date dia, Double importe, Double disponible) {
    setIdTipoMedioPago(idTipoMedioPago);
    setIdCierre(idCierre);
    setAcumulado(acumulado);
    setIdCaja(idCaja);
    setIdCierreCaja(idCierreCaja);
    setSaldo(saldo);
    setDia(dia);
    setImporte(importe);
    setDisponible(disponible);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdTipoMedioPago(Long idTipoMedioPago) {
    this.idTipoMedioPago = idTipoMedioPago;
  }

  public Long getIdTipoMedioPago() {
    return idTipoMedioPago;
  }

  public void setIdCierre(Long idCierre) {
    this.idCierre = idCierre;
  }

  public Long getIdCierre() {
    return idCierre;
  }

  public void setAcumulado(Double acumulado) {
    this.acumulado = acumulado;
  }

  public Double getAcumulado() {
    return acumulado;
  }

  public void setIdCaja(Long idCaja) {
    this.idCaja = idCaja;
  }

  public Long getIdCaja() {
    return idCaja;
  }

  public void setIdCierreCaja(Long idCierreCaja) {
    this.idCierreCaja = idCierreCaja;
  }

  public Long getIdCierreCaja() {
    return idCierreCaja;
  }

  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

  public Double getSaldo() {
    return saldo;
  }

  public void setDia(Date dia) {
    this.dia = dia;
  }

  public Date getDia() {
    return dia;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
  }

  public void setDisponible(Double disponible) {
    this.disponible = disponible;
  }

  public Double getDisponible() {
    return disponible;
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
  	return getIdCierreCaja();
  }

  @Override
  public void setKey(Long key) {
  	this.idCierreCaja = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAcumulado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCaja());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierreCaja());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSaldo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDisponible());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idCierre", getIdCierre());
		regresar.put("acumulado", getAcumulado());
		regresar.put("idCaja", getIdCaja());
		regresar.put("idCierreCaja", getIdCierreCaja());
		regresar.put("saldo", getSaldo());
		regresar.put("dia", getDia());
		regresar.put("importe", getImporte());
		regresar.put("disponible", getDisponible());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdTipoMedioPago(), getIdCierre(), getAcumulado(), getIdCaja(), getIdCierreCaja(), getSaldo(), getDia(), getImporte(), getDisponible(), getRegistro()
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
    regresar.append("idCierreCaja~");
    regresar.append(getIdCierreCaja());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCierreCaja());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCierresCajasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCierreCaja()!= null && getIdCierreCaja()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCierresCajasDto other = (TcManticCierresCajasDto) obj;
    if (getIdCierreCaja() != other.idCierreCaja && (getIdCierreCaja() == null || !getIdCierreCaja().equals(other.idCierreCaja))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCierreCaja() != null ? getIdCierreCaja().hashCode() : 0);
    return hash;
  }

}



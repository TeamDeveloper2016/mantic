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
@Table(name="tr_mantic_garantia_medio_pago")
public class TrManticGarantiaMedioPagoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_garantia")
  private Long idGarantia;
  @Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
  @Column (name="id_cierre")
  private Long idCierre;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_garantia_medio_pago")
  private Long idGarantiaMedioPago;
  @Column (name="importe")
  private Double importe;
  @Column (name="id_banco")
  private Long idBanco;
  @Column (name="referencia")
  private String referencia;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticGarantiaMedioPagoDto() {
    this(new Long(-1L));
  }

  public TrManticGarantiaMedioPagoDto(Long key) {
    this(null, null, null, null, null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TrManticGarantiaMedioPagoDto(Long idGarantia, Long idTipoMedioPago, Long idCierre, Long idUsuario, String observaciones, Long idVentaMedioPago, Double importe, Long idBanco, String referencia) {
    setIdGarantia(idGarantia);
    setIdTipoMedioPago(idTipoMedioPago);
    setIdCierre(idCierre);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdGarantiaMedioPago(idVentaMedioPago);
    setImporte(importe);
    setIdBanco(idBanco);
    setReferencia(referencia);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdGarantia(Long idGarantia) {
    this.idGarantia = idGarantia;
  }

  public Long getIdGarantia() {
    return idGarantia;
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

  public void setIdGarantiaMedioPago(Long idGarantiaMedioPago) {
    this.idGarantiaMedioPago = idGarantiaMedioPago;
  }

  public Long getIdGarantiaMedioPago() {
    return idGarantiaMedioPago;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
  }

  public void setIdBanco(Long idBanco) {
    this.idBanco = idBanco;
  }

  public Long getIdBanco() {
    return idBanco;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
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
  	return getIdGarantiaMedioPago();
  } 

  @Override
  public void setKey(Long key) {
  	this.idGarantiaMedioPago = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdGarantia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGarantiaMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBanco());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idGarantia", getIdGarantia());
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idCierre", getIdCierre());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idVentaMedioPago", getIdGarantiaMedioPago());
		regresar.put("importe", getImporte());
		regresar.put("idBanco", getIdBanco());
		regresar.put("referencia", getReferencia());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdGarantia(), getIdTipoMedioPago(), getIdCierre(), getIdUsuario(), getObservaciones(), getIdGarantiaMedioPago(), getImporte(), getIdBanco(), getReferencia(), getRegistro()
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
    regresar.append("idGarantiaMedioPago~");
    regresar.append(getIdGarantiaMedioPago());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdGarantiaMedioPago());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticGarantiaMedioPagoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdGarantiaMedioPago()!= null && getIdGarantiaMedioPago()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticGarantiaMedioPagoDto other = (TrManticGarantiaMedioPagoDto) obj;
    if (getIdGarantiaMedioPago() != other.idGarantiaMedioPago && (getIdGarantiaMedioPago() == null || !getIdGarantiaMedioPago().equals(other.idGarantiaMedioPago))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdGarantiaMedioPago() != null ? getIdGarantiaMedioPago().hashCode() : 0);
    return hash;
  }
}



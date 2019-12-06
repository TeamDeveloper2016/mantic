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
@Table(name="tr_mantic_venta_medio_pago")
public class TrManticVentaMedioPagoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
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
	@Column (name="id_venta_medio_pago")
  private Long idVentaMedioPago;
  @Column (name="id_venta")
  private Long idVenta;
  @Column (name="importe")
  private Double importe;
  @Column (name="total")
  private Double total;
  @Column (name="id_banco")
  private Long idBanco;
  @Column (name="referencia")
  private String referencia;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticVentaMedioPagoDto() {
    this(new Long(-1L));
  }

  public TrManticVentaMedioPagoDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TrManticVentaMedioPagoDto(Long idTipoMedioPago, Long idCierre, Long idUsuario, String observaciones, Long idVentaMedioPago, Long idVenta, Double importe, Long idBanco, String referencia, Double total) {
    setIdTipoMedioPago(idTipoMedioPago);
    setIdCierre(idCierre);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdVentaMedioPago(idVentaMedioPago);
    setIdVenta(idVenta);
    setImporte(importe);
    setTotal(total);
    setIdBanco(idBanco);
    setReferencia(referencia);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdTipoMedioPago(Long idTipoMedioPago) {
    this.idTipoMedioPago = idTipoMedioPago;
  }

  public Long getIdTipoMedioPago() {
    return idTipoMedioPago;
  }

	public Long getIdCierre() {
		return idCierre;
	}

	public void setIdCierre(Long idCierre) {
		this.idCierre = idCierre;
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

  public void setIdVentaMedioPago(Long idVentaMedioPago) {
    this.idVentaMedioPago = idVentaMedioPago;
  }

  public Long getIdVentaMedioPago() {
    return idVentaMedioPago;
  }

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
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

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}	
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdVentaMedioPago();
  }

  @Override
  public void setKey(Long key) {
  	this.idVentaMedioPago = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVentaMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBanco());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idCierre", getIdCierre());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idVentaMedioPago", getIdVentaMedioPago());
		regresar.put("idVenta", getIdVenta());
		regresar.put("importe", getImporte());
		regresar.put("idBanco", getIdBanco());
		regresar.put("referencia", getReferencia());
		regresar.put("registro", getRegistro());
		regresar.put("total", getTotal());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdTipoMedioPago(), getIdCierre(), getIdUsuario(), getObservaciones(), getIdVentaMedioPago(), getIdVenta(), getImporte(), getIdBanco(), getReferencia(), getRegistro(), getTotal()
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
    regresar.append("idVentaMedioPago~");
    regresar.append(getIdVentaMedioPago());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdVentaMedioPago());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticVentaMedioPagoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdVentaMedioPago()!= null && getIdVentaMedioPago()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticVentaMedioPagoDto other = (TrManticVentaMedioPagoDto) obj;
    if (getIdVentaMedioPago() != other.idVentaMedioPago && (getIdVentaMedioPago() == null || !getIdVentaMedioPago().equals(other.idVentaMedioPago))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdVentaMedioPago() != null ? getIdVentaMedioPago().hashCode() : 0);
    return hash;
  }
}